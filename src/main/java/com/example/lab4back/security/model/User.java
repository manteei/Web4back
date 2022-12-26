package com.example.lab4back.security.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "users")
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class User {
    @Id
    private String username;
    @Column
    private String password;
    @Column
    private String refreshtoken;
}
