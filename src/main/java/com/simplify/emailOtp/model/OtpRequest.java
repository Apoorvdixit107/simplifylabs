package com.simplify.emailOtp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OtpRequest {
    @Email(message = "Email is not valid")
    private String emailId;

}
