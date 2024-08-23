package com.cogniwide.CogniTrack.Service;

import com.cogniwide.CogniTrack.Model.Roles;
import com.cogniwide.CogniTrack.Repository.RoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    @Autowired
    RoleRepo roleRepo;
    public List<Roles> getAll(){
        return this.roleRepo.findAll();
    }
}
