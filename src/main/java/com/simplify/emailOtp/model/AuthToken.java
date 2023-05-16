package com.simplify.emailOtp.model;

import lombok.Data;
import org.springframework.security.core.Authentication;

@Data
public class AuthToken {
    private final String status = "success";
    private final String token;
    private String message;

    private boolean isVerified;


    public AuthToken(String token, String message,boolean isVerified) {
        this.token = token;
        this.message = message;
        this.isVerified=isVerified;
    }


}

