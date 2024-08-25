package com.cogniwide.CogniTrack.Controller;

import com.cogniwide.CogniTrack.CustomResponse.ApiResponse;
import com.cogniwide.CogniTrack.Model.Roles;
import com.cogniwide.CogniTrack.Service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiResponse> getAll(){
        ApiResponse apiResponse= new ApiResponse();
        List<Roles> roles= this.roleService.getAll();
        apiResponse.setSuccess(true);
        apiResponse.setData(roles);
        apiResponse.setMessage("fetched Successfully");
        apiResponse.setStatusCode(HttpStatus.OK.value());
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
