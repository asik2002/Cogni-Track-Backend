package com.cogniwide.CogniTrack.DTO;
import lombok.Data;

@Data
public class UserDetailsDto {
    private String employeeId;
    private String userName;
    private String email;
    private String phoneNumber;
    private String roleName;
    private String managerName;
}
