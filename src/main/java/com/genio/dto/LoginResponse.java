package com.genio.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginResponse {
    private String message;

    public LoginResponse() {}

    public LoginResponse(String message) {
        this.message = message;
    }

}