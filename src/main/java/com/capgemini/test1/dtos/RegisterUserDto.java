package com.capgemini.test1.dtos;

import lombok.Data;

@Data
public class RegisterUserDto {
    private String fullName;
    private String email;
}