package com.cogniwide.CogniTrack.Controller;

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

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private JWTService jwtService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody SignUpDto signUpDto) {

        boolean newUser = userService.register(signUpDto);
        if(newUser) {
            String token = jwtService.generateToken(signUpDto.getUsername());
            return ResponseEntity.status(HttpStatus.CREATED).body(token);
        }
        else
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");
    }

    @PostMapping("/login")
    public String login(@RequestBody Users user) {
        return userService.verify(user);
    }
    @PostMapping("/update-role/{employeeId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public boolean updateRole(@PathVariable String employeeId,@RequestBody String role){
        return this.userService.updateRole(employeeId,role);
    }
    @PostMapping("/assign-manager/{employeeId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public boolean assignManager(@PathVariable String employeeId,@RequestBody String managerId){
        return this.userService.assignManager(employeeId,managerId);
    }
    @GetMapping("/get-all/{role}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<UserDetailsDto> getUsersByRole(@PathVariable String role){
        return userService.getUsersByRole(role);
    }
    @GetMapping("/get-all-users")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<UserDetailsDto> getAllUsers(){
        return this.userService.getAllUsers();
    }
    @GetMapping("/get-profile/{employeeId}")
    public UserDetailsDto getProfile(@PathVariable String employeeId){
        return this.userService.getProfile(employeeId);
    }

}

