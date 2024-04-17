package com.projectx.microservice.authenticationservice.services;

import com.projectx.microservice.authenticationservice.entity.CustomUserDetails;
import com.projectx.microservice.authenticationservice.entity.Users;
import com.projectx.microservice.authenticationservice.repository.UserRepository;
import com.projectx.microservice.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class CustomUserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users = null;
        if (Constants.isMobile(username)) {
            users = userRepository.findByUserMobile(Long.parseLong(username));
        } else {
            users = userRepository.findByUserEmail(username);
        }

        if(users==null)
            throw new UsernameNotFoundException("User with email: " +username +" not found !");
        else {
            return new CustomUserDetails(users);
        }
    }
}
