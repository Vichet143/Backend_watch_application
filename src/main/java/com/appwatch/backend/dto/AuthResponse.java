package com.appwatch.backend.dto;

public class AuthResponse {
    private String message;
    private String token;
    private String email;
    private String name;
    private String role;   
    private long create_at;

    public AuthResponse(String message) {
        this.message = message;
    }

    public AuthResponse(String message, String token) {
        this.message = message;
        this.token = token;
    }

    public AuthResponse(String message, String email, String name, String role , long create_at) {
        this.message = message;
        this.email = email;
        this.name = name;
        this.role = role;
        this.create_at = create_at;
    }

    // Getters and Setters
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public long getCreateAt() { return create_at; }

    public void setCreateAt(long create_at) {
        this.create_at = create_at;
    }
    
    public String showall() {
        return "AuthResponse{message='" + message + "', token='" + token + "', email='" + email + "', name='" + name + "', role='" + role + "', create_at=" + create_at + "}";
    }
}
