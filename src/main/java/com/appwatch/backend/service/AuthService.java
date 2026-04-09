package com.appwatch.backend.service;

import java.time.ZoneId;
import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.appwatch.backend.dto.AuthResponse;
import com.appwatch.backend.model.User;
import com.appwatch.backend.repository.UserRepository;
import com.appwatch.backend.util.JwtUtil;

@Service
public class AuthService {

    private final UserRepository repo;
    private final BCryptPasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository repo,BCryptPasswordEncoder encoder,JwtUtil jwtUtil) {
        this.repo = repo;
        this.encoder = encoder;
        this.jwtUtil = jwtUtil;
    }

    // get all users
    public List<User> getAllUsers() {
        return repo.findAll();
    }

    // REGISTER
    public AuthResponse register(User user) {

        if (user.getEmail() == null || user.getPassword() == null || user.getName() == null) {
            throw new RuntimeException("Email, password, and name are required");
        }
        if (repo.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already in use: " + user.getEmail());
        }
        user.setPassword(encoder.encode(user.getPassword()));
        repo.save(user);
        return new AuthResponse("User registered!", user.getEmail(), user.getName(), user.getRole(), user.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());

    }

    // LOGIN
    public AuthResponse login(String email, String password) {
        User user = repo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!encoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtUtil.generateToken(email);
        AuthResponse response = new AuthResponse("Login successful!", token);
        response.setEmail(user.getEmail());
        response.setName(user.getName());
        response.setRole(user.getRole());
        response.setCreateAt(user.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        System.out.println(response.showall());
        return response;
    }
}

