package com.cogniwide.CogniTrack.DTO;

import lombok.Data;

import java.sql.Date;

@Data
public class LeaveRequestDto {
    private Long requestId;
    private String employeeId;
    private Long leaveTypeId;
    private Date startDate;
    private Date endDate;
    private String approverId;
    private String reason;
    private String status;
    private Boolean isRead;
    private String comments;
}
