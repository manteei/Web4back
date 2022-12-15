package com.example.lab4back.beans;

import lombok.Data;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@Entity
@Table(name = "points")
public class PointData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "xcord")
    private Double x;

    @Column(name = "ycord")
    private Double y;

    @Column(name = "radius")
    private Double radius;

    @Column(name = "datetime")
    private String date;

    @Column(name = "duration")
    private String duration;

    @Column(name = "result")
    private boolean hit;

    @Column(name = "username")
    private String username;

    public void setDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyy HH:mm:ss");
        this.date = sdf.format(date);
    }
}
