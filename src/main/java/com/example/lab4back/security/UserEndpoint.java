package com.example.lab4back.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.lab4back.security.model.User;
import com.example.lab4back.security.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class UserEndpoint {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @CrossOrigin
    @PostMapping ("/user/refresh")
    private ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response){
        String token = request.getHeader(AUTHORIZATION);
        User dbUser = userService.getUserByRefreshToken(token);
        if (dbUser != null) {
            Algorithm accessTokenAlgorithm = Algorithm.HMAC256("secret".getBytes());
            Algorithm refreshTokenAlgorithm = Algorithm.HMAC256("secret".getBytes());
            String newAccessToken = JWT.create()
                    .withSubject(dbUser.getUsername())
                    .withExpiresAt(new Date(System.currentTimeMillis() + 60 * 30 * 1000))
                    .withIssuer(request.getRequestURL().toString())
                    .sign(accessTokenAlgorithm);
            String newRefreshToken = JWT.create()
                    .withSubject(dbUser.getUsername())
                    .withExpiresAt(new Date(System.currentTimeMillis() + 60 * 60 * 24 * 365 * 1000))
                    .withIssuer(request.getRequestURL().toString())
                    .sign(refreshTokenAlgorithm);

            userService.setToken(newRefreshToken, dbUser.getUsername());

            Map<String, String> tokens = new HashMap<>();
            tokens.put("access_token", newAccessToken);
            tokens.put("refresh_token", newRefreshToken);
            response.setContentType(APPLICATION_JSON_VALUE);
            return ResponseEntity.ok().body(tokens);
        } else {
            log.error("Error refreshing token: Invalid refresh token");
            Map<String, String> error = new HashMap<>();
            error.put("refresh_token", "Invalid refresh token");
            return ResponseEntity.badRequest().body(error);
        }
    }

    @CrossOrigin
    @GetMapping("/user/auth")
    private void checkAuth() {}

    @CrossOrigin
    @PostMapping("/user/save")
    private ResponseEntity<?> saveUser(@RequestBody User user) {
        User dbUser = userService.getUser(user.getUsername());
        if (dbUser == null) {
            try {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/save").toString());
                return ResponseEntity.created(uri).body(userService.saveUser(user));
            }catch (IllegalArgumentException e){
                String error = "User are null";
                return ResponseEntity.badRequest().header("Error", error).body("");
            }
        } else {
            String error = "User already exists";
            return ResponseEntity.badRequest().header("Error", error).body("");
        }

    }

}
