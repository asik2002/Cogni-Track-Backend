package com.cogniwide.CogniTrack.Controller;

import com.cogniwide.CogniTrack.Model.Roles;
import com.cogniwide.CogniTrack.Service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/roles")
public class RoleController {
    @Autowired
    RoleService roleService;
    @GetMapping("/get-roles")
    public List<Roles> getAll(){
        return this.roleService.getAll();
    }
}
