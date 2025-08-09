package com.ujwal.TrackYourExpense.Service;

import com.ujwal.TrackYourExpense.Model.userRegister;
import com.ujwal.TrackYourExpense.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        userRegister user = userRepo.findByEmailId(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return User.builder()
                .username(user.getEmailId())
                .password(user.getPassword())
                .roles("USER")
                .build();
    }
}