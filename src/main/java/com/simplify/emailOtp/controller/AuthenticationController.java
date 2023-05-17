package com.simplify.emailOtp.controller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.simplify.emailOtp.dao.UserService;
import com.simplify.emailOtp.model.ApiResponse;
import com.simplify.emailOtp.model.AuthRequest;
import com.simplify.emailOtp.model.AuthToken;
import com.simplify.emailOtp.model.ResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody AuthRequest request) {
        try {

            AuthRequest authRequest = new AuthRequest();
            authRequest.setEmailId(request.getEmailId());
            authRequest.setOtp(request.getOtp());
            AuthToken authToken = this.userService.login(authRequest.getEmailId(), authRequest.getOtp());
            return ResponseEntity.ok(new ApiResponse("success", authToken.getMessage(), authToken.getToken()));

        }
        catch (Exception e){
            return ResponseEntity.ok(new ApiResponse("failure","Authentication Failed",null));
        }
    }
}
