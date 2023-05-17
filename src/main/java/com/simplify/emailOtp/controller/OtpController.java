package com.simplify.emailOtp.controller;

import com.simplify.emailOtp.dao.OtpService;
import com.simplify.emailOtp.model.OtpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/otp")
public class OtpController {


    @Autowired
    private OtpService otpService;
    @PostMapping("/generateOtp")
    ResponseEntity<String> generateOtp(@Valid @RequestBody OtpRequest request){
      String msg=  this.otpService.generateNewOtp(request.getEmailId())?"Otp is sent on Mail":"regenerate after one minute";
     return ResponseEntity.ok(msg);
    }
}
