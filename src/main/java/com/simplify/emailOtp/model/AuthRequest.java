package com.simplify.emailOtp.model;

import javax.validation.constraints.NotEmpty;

import lombok.Data;


@Data
public class AuthRequest {

    @NotEmpty
    private String emailId;
    @NotEmpty
    private long otp;
}
