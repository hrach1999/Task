package com.sflpro.chat.security;

import com.sflpro.chat.model.User;
import com.sflpro.chat.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserServiceImpl userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> byEmail = Optional.ofNullable(userService.findByEmail(email));
        if (!byEmail.isPresent()) {
            throw new UsernameNotFoundException("User does not exists");
        }
        return new SpringUser(byEmail.get());
    }
}
