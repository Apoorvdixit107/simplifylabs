package com.simplify.emailOtp.controller;

import com.simplify.emailOtp.dao.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/otp")
public class OtpController {


    @Autowired
    private OtpService otpService;
    @PostMapping("/generateOtp/{emailId}")
    ResponseEntity<String> generateOtp(@PathVariable("emailId") String emailId){
        this.otpService.generateNewOtp(emailId);
     return ResponseEntity.ok("Otp Mail Send");
    }
}
