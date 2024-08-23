package com.cogniwide.CogniTrack.Model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "Leave_Balance")
public class LeaveBalance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "employee_id")
    private String employeeId;
    private Long leaveTypeId;
    private Long balanceDays;
}
