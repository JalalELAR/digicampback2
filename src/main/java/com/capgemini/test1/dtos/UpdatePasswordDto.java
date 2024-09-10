package com.capgemini.test1.dtos;

import lombok.Data;

@Data
public class UpdatePasswordDto  {
    private String currentPassword;
    private String newPassword;
}
