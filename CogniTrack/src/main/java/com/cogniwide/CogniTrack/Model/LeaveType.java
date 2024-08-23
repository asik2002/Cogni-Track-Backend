package com.cogniwide.CogniTrack.Model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "Leave_Type")
public class LeaveType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long leaveTypeId;
    private String leaveType;
}
