package com.example.lab4back.security.services;

import com.example.lab4back.security.model.User;
import java.util.List;

public interface UserService {
    User saveUser(User user);
    User getUser(String username);
    List<User> getUsers();
    User setToken(String token, String username);
    User getUserByRefreshToken(String refresh);
}
