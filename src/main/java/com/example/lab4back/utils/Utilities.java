package com.example.lab4back.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

public class Utilities {

    public static String decodeUsername(String header) {
        System.out.println(1);
        String token = header.substring("Bearer ".length());
        System.out.println(2);
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        System.out.println(3);
        JWTVerifier verifier = JWT.require(algorithm).build();
        System.out.println(4);
        DecodedJWT decodedJWT = verifier.verify(token);
        System.out.println(5);
        return decodedJWT.getSubject();
    }
}
