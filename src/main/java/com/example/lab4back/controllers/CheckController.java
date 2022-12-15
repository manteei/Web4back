package com.example.lab4back.controllers;

import com.example.lab4back.beans.PointData;
import com.example.lab4back.data.PointDataRepository;
import com.example.lab4back.utils.Utilities;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@Slf4j
@RequestMapping("/api")
public class CheckController {

    private final PointDataRepository pointDataRepo;

    public CheckController(PointDataRepository pointDataRepo) {
        this.pointDataRepo = pointDataRepo;
    }

    @CrossOrigin
    @PostMapping("/login")
    public void loging() {}  // needed just to enable cors on /api/login URL

    @CrossOrigin
    @GetMapping("/points")
    public List<PointData> getAllPoints(HttpServletRequest request) {
        String header = request.getHeader(AUTHORIZATION);
        String username = Utilities.decodeUsername(header);
        return pointDataRepo.findAllByUsername(username);
    }

    @CrossOrigin
    @PostMapping("/points/clear")
    public void clearTable(HttpServletRequest request) {
        String header = request.getHeader(AUTHORIZATION);
        String username = Utilities.decodeUsername(header);
        pointDataRepo.deleteByUsername(username);
    }

    @CrossOrigin
    @PostMapping("/points/check")
    public PointData checkHit(@RequestBody PointData point, HttpServletRequest request) {
        long start = System.nanoTime();
        point.setDate(new Date());

        double x = point.getX();
        double y = point.getY();
        double r = point.getRadius();
        boolean hitResult = (x >= 0 && y <= 0 && x * x + y * y <= r * r) || (x <= 0 && y >= 0 && y - x <= r) || (x <= 0 && y <= 0 && x >= -r && y >= -r);
        point.setHit(hitResult);

        String header = request.getHeader(AUTHORIZATION);
        String username = Utilities.decodeUsername(header);
        point.setUsername(username);
        point.setDuration((System.nanoTime() - start) / 1000 + " мкс");
        pointDataRepo.save(point);
        log.info(point.toString());
        return point;
    }

}
