package com.cogniwide.CogniTrack.Model;

import jakarta.persistence.*;
import lombok.Data;
@Data
@Entity
@Table(name = "User_Details")
public class Users {
    @Id
    @Column(name = "employee_id")
    private String username;
    private String email;
    private String phoneNumber;
    @Column(name = "password_hash")
    private String password;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", referencedColumnName = "role_id")
    private Roles role;
    private String manager_id;
    private String name;
}
