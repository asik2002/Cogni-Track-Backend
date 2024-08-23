package com.cogniwide.CogniTrack.Service;

import com.cogniwide.CogniTrack.DTO.LoginDto;
import com.cogniwide.CogniTrack.Repository.UserRepo;
import com.cogniwide.CogniTrack.Model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LoginService implements UserDetailsService {
    @Autowired
    UserRepo userRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users userDetails=userRepo.findByUsername(username);
        if(userDetails==null){
            throw new UsernameNotFoundException("User Not Found");
        }
        return new LoginDto(userDetails);
    }
}
