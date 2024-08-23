package com.cogniwide.CogniTrack.DTO;

import lombok.Data;

@Data
public class AddLeaveDto {
    private Long id;
    private String employeeId;
    private Long leaveTypeId;
    private Long balanceDays;
}
