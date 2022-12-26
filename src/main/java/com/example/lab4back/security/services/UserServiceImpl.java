package com.example.lab4back.security.services;

import com.example.lab4back.security.model.User;
import com.example.lab4back.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public User saveUser(User user) {
        log.info("Saving user {}", user.getUsername());
        return userRepository.save(user);
    }

    @Override
    public User getUser(String username) {
        log.info("Fetching user {} from database", username);
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> getUsers() {
        log.info("Fetching all users from database");
        return userRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Username not found");
        } else {
            log.info("Username {} found", username);
        }
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), new ArrayList<>()
        );
    }

    @Override
    public User setToken(String refreshtoken, String username) {
        User user = getUser(username);
        user.setRefreshtoken(refreshtoken);
        return saveUser(user);
       // log.info("Refresh token is update");
    }

    @Override
    public User getUserByRefreshToken(String refreshToken) {
        refreshToken = refreshToken.replace("Bearer ", "");
        return userRepository.findByRefreshtoken(refreshToken);
    }
}
