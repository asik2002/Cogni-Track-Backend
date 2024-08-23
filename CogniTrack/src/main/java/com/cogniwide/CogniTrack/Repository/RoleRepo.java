package com.cogniwide.CogniTrack.Repository;

import com.cogniwide.CogniTrack.Model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepo extends JpaRepository<Roles,Long> {
    Roles findByRole(String role);
}
