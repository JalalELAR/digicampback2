package com.capgemini.test1.service.serviceSecurity;

import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private long expiresIn;

}
