package com.simplify.emailOtp.controller;

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

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {

            AuthRequest authRequest = new AuthRequest();
            authRequest.setEmailId(request.getEmailId());
            authRequest.setOtp(request.getOtp());
            AuthToken authToken = this.userService.login(authRequest.getEmailId(), authRequest.getOtp());
            return ResponseEntity.ok(new ApiResponse("success", authToken.getMessage(), authToken.getToken()==null?
                    "None":authToken.getToken()));

        }
        catch (Exception e){
            return ResponseEntity.ok(new ResponseModel("failure",e.getMessage()));
        }
    }
}
