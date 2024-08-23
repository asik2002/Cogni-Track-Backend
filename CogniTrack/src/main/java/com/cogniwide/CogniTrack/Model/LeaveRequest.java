package com.cogniwide.CogniTrack.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;

@Data
@Entity
@Table(name = "Leave_Request")
public class LeaveRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Long requestId;
    private String employeeId;
    private Long leaveTypeId;
    private Date startDate;
    private Date endDate;
    private String approverId;
    private String status;
    private Boolean isRead;
    private String attachment;
    private String reason;
    private String comments;
}
