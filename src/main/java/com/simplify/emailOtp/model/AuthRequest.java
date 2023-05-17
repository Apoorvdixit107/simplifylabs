package com.simplify.emailOtp.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import lombok.Data;


@Data
public class AuthRequest {

    @NotEmpty
    @Email(message = "Email is not valid")
    private String emailId;
    @NotEmpty
    private long otp;
}
