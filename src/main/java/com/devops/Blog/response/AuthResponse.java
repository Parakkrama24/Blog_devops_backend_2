package com.devops.Blog.response;

import com.devops.Blog.domain.USER_ROLE;
import lombok.Data;

@Data
public class AuthResponse {


    private String message;
    private String jwt;
    private USER_ROLE role;

    // Getters and Setters

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public USER_ROLE getRole() {
        return role;
    }

    public void setRole(USER_ROLE role) {
        this.role = role;
    }
}
