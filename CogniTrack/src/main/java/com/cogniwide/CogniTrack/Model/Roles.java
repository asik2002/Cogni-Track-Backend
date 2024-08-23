package com.cogniwide.CogniTrack.Model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Roles_Details")
public class Roles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleId;
    private String role;
}
