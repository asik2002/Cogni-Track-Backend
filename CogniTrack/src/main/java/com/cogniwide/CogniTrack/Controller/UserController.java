package com.cogniwide.CogniTrack.Controller;

import com.cogniwide.CogniTrack.CustomResponse.ApiError;
import com.cogniwide.CogniTrack.CustomResponse.ApiResponse;
import com.cogniwide.CogniTrack.DTO.SignUpDto;
import com.cogniwide.CogniTrack.DTO.UserDetailsDto;
import com.cogniwide.CogniTrack.Model.Users;
import com.cogniwide.CogniTrack.Service.JWTService;
import com.cogniwide.CogniTrack.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private JWTService jwtService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody SignUpDto signUpDto) {
        ApiResponse apiResponse=new ApiResponse();
        boolean newUser = userService.register(signUpDto);
        if(newUser) {
            String token = jwtService.generateToken(signUpDto.getUsername());
            apiResponse.setSuccess(true);
            apiResponse.setData(token);
            apiResponse.setMessage("Signup Success");
            apiResponse.setStatusCode(HttpStatus.CREATED.value());
            return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
        }
        else
        {
            ApiError error = ApiError.setError("Data violation");
            apiResponse.setSuccess(false);
            apiResponse.setErrors(Collections.singletonList(error));
            apiResponse.setMessage("User Already exists");
            apiResponse.setStatusCode(HttpStatus.CONFLICT.value());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(apiResponse);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody Users user) {
        ApiResponse apiResponse= new ApiResponse();
        try {
            String token = userService.verify(user);
            apiResponse.setSuccess(true);
            apiResponse.setData(token);
            apiResponse.setMessage("Login Success");
            apiResponse.setStatusCode(HttpStatus.OK.value());
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        }
        catch (Exception e){
            ApiError error = ApiError.setError("Unauthorized");
            apiResponse.setSuccess(false);
            apiResponse.setErrors(Collections.singletonList(error));
            apiResponse.setMessage("Check Employee ID or Password");
            apiResponse.setStatusCode(HttpStatus.UNAUTHORIZED.value());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiResponse);
        }
    }
    @PostMapping("/update-role/{employeeId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> updateRole(@PathVariable String employeeId,@RequestBody String role){
        ApiResponse apiResponse=new ApiResponse();
        if(this.userService.updateRole(employeeId,role)){
            apiResponse.setSuccess(true);
            apiResponse.setMessage("Role Updated");
            apiResponse.setStatusCode(HttpStatus.OK.value());
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        }
        ApiError error = ApiError.setError("Bad Request");
        apiResponse.setSuccess(false);
        apiResponse.setErrors(Collections.singletonList(error));
        apiResponse.setMessage("Check the Employee ID or Role");
        apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
    }
    @PostMapping("/assign-manager/{employeeId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> assignManager(@PathVariable String employeeId,@RequestBody String managerId){
        ApiResponse apiResponse=new ApiResponse();
        if (this.userService.assignManager(employeeId,managerId)){
            apiResponse.setSuccess(true);
            apiResponse.setMessage("Manager Assigned Successfully");
            apiResponse.setStatusCode(HttpStatus.OK.value());
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        }
        ApiError error = ApiError.setError("Bad Request");
        apiResponse.setSuccess(false);
        apiResponse.setErrors(Collections.singletonList(error));
        apiResponse.setMessage("Check the Employee ID or Manager Id");
        apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
    }
    @GetMapping("/get-all/{role}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> getUsersByRole(@PathVariable String role){
        ApiResponse apiResponse=new ApiResponse();
        List<UserDetailsDto> users =userService.getUsersByRole(role);
        apiResponse.setSuccess(true);
        apiResponse.setData(users);
        apiResponse.setMessage("Users Fetched Successfully");
        apiResponse.setStatusCode(HttpStatus.OK.value());
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
    @GetMapping("/get-all-users")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> getAllUsers(){
        ApiResponse apiResponse=new ApiResponse();
        List<UserDetailsDto> users= this.userService.getAllUsers();
        apiResponse.setSuccess(true);
        apiResponse.setData(users);
        apiResponse.setMessage("Users Fetched Successfully");
        apiResponse.setStatusCode(HttpStatus.OK.value());
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
    @GetMapping("/get-profile/{employeeId}")
    public ResponseEntity<ApiResponse> getProfile(@PathVariable String employeeId){
        ApiResponse apiResponse=new ApiResponse();
        UserDetailsDto user = this.userService.getProfile(employeeId);
        apiResponse.setSuccess(true);
        apiResponse.setData(user);
        apiResponse.setMessage("Profile Fetched Successfully");
        apiResponse.setStatusCode(HttpStatus.OK.value());
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}

