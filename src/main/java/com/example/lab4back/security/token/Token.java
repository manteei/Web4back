package com.example.lab4back.security.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.lab4back.security.model.User;
import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

public class Token {
    public static String createAccessToken(String username, HttpServletRequest request ){
        Algorithm accessTokenAlgorithm = Algorithm.HMAC256("secret".getBytes());
        String access_token = JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + 60 * 30 * 1000))
                .withIssuer(request.getRequestURL().toString())
                .sign(accessTokenAlgorithm);
        return access_token;
    }
    public static String createRefreshToken(String username, HttpServletRequest request ){
        Algorithm refreshTokenAlgorithm = Algorithm.HMAC256("secret".getBytes());
        String refresh_token = JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + 60 * 60 * 24 * 365 * 1000))
                .withIssuer(request.getRequestURL().toString())
                .sign(refreshTokenAlgorithm);
        return refresh_token;
    }

}
