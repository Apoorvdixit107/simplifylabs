package com.simplify.emailOtp.model;

import lombok.Data;

@Data
public class ResponseModel {
    private String message;
    private String status;

    public ResponseModel(String message, String status) {
        this.message = message;
        this.status = status;
    }
}
