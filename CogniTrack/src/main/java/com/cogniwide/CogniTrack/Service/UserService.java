package com.cogniwide.CogniTrack.Service;

import com.cogniwide.CogniTrack.DTO.SignUpDto;
import com.cogniwide.CogniTrack.DTO.UserDetailsDto;
import com.cogniwide.CogniTrack.Repository.RoleRepo;
import com.cogniwide.CogniTrack.Model.Roles;
import com.cogniwide.CogniTrack.Repository.UserRepo;
import com.cogniwide.CogniTrack.Model.Users;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private JWTService jwtService;
    @Autowired
    private UserRepo repo;
    @Autowired
    private RoleRepo roleRepo;
    @Autowired
    private AuthenticationManager authManager;
    private BCryptPasswordEncoder encoder=new BCryptPasswordEncoder(12);

    public Boolean register(SignUpDto signUpDto){
        Users existingUser = repo.findByUsername(signUpDto.getUsername());
        if (existingUser != null) {
           return false;
        }
        Users users = new Users();
        users.setUsername(signUpDto.getUsername());
        users.setEmail(signUpDto.getEmail());
        users.setPhoneNumber(signUpDto.getPhoneNumber());
        users.setPassword(encoder.encode(signUpDto.getPassword()));
        users.setName(signUpDto.getName());

        // Fetch the Role entity by role name
        Roles role = roleRepo.findByRole(signUpDto.getRoleName());
        users.setRole(role);

         repo.save(users);
         return true;
    }

    public String verify(Users user) {
        Authentication authentication =
                authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword()));

        if(authentication.isAuthenticated()){
            return jwtService.generateToken(user.getUsername());
        }
        return "";
    }

    public boolean updateRole(String employeeId,String role) {
        try{
        Users user = this.repo.findByUsername(employeeId);
        Roles newRole = roleRepo.findByRole(role);
        user.setRole(newRole);
        this.repo.save(user);
        return true;
        }
        catch (Exception e){
            return false;
        }
    }

    public boolean assignManager(String employeeId, String managerId) {
        try {
            Users user = this.repo.findByUsername(employeeId);
            user.setManager_id(managerId);
            this.repo.save(user);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    public List<UserDetailsDto> getUsersByRole(String role) {
        return this.repo.findByRoleRole(role).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    public List<UserDetailsDto> getAllUsers() {
        return this.repo.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private UserDetailsDto convertToDto(Users users) {
        if (users!=null){
        UserDetailsDto userDetailsDto=new UserDetailsDto();
        BeanUtils.copyProperties(users,userDetailsDto);
       userDetailsDto.setEmployeeId(users.getUsername());
       userDetailsDto.setUserName(users.getName());
       userDetailsDto.setRoleName(users.getRole().getRole());
       userDetailsDto.setManagerName(getManagerName(users.getManager_id()));
       return userDetailsDto;
    }
       return null;}
    private String getManagerName(String employeeId){
        Users users= this.repo.findByUsername(employeeId);
        if (users!=null){
            return users.getName();
        }
        return "";
    }

    public UserDetailsDto getProfile(String employeeId) {
        return convertToDto(this.repo.findByUsername(employeeId));
    }
}
