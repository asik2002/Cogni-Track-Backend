package com.cogniwide.CogniTrack.Repository;

import com.cogniwide.CogniTrack.Model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface UserRepo extends JpaRepository<Users,String> {

    Users findByUsername(String username);
    List<Users> findByRoleRole(String roleName);
}
