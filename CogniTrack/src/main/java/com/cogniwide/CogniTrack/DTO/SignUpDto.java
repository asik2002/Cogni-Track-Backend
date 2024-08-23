package com.cogniwide.CogniTrack.DTO;

import lombok.Data;

@Data
public class SignUpDto {
    private String username;
    private String email;
    private String phoneNumber;
    private String password;
    private String roleName ="EMPLOYEE";
    private String name;
}
