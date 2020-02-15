package com.bergdavi.onlab.gameservice.service;

import java.util.ArrayList;
import java.util.List;

import com.bergdavi.onlab.gameservice.jpa.model.JpaUser;
import com.bergdavi.onlab.gameservice.jpa.repository.UserRepository;
import com.bergdavi.onlab.gameservice.model.Type;
import com.bergdavi.onlab.gameservice.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;

/**
 * UserService
 */
@Service
public class UserService {

    @Autowired
    private JdbcUserDetailsManager jdbcUserDetailsManager;

    @Autowired
    PasswordEncoder passwordEncoder;
    
    @Autowired
    UserRepository userRepository;
    
    public User registerUser(User user) {
        List<String> roles = new ArrayList<>();
        roles.add("USER");
        if(user.getType() == Type.ADMIN) {
            roles.add("ADMIN");
        }
        if(jdbcUserDetailsManager.userExists(user.getUsername())) {
            // TODO replace with more appropriate exception
            // Return HTTP 409
            throw new RuntimeException();
        }
        jdbcUserDetailsManager.createUser(org.springframework.security.core.userdetails.User.withUsername(
            user.getUsername()).password(passwordEncoder.encode(user.getPassword())).roles(roles.toArray(new String[roles.size()])).build());
        JpaUser jpaUser = new JpaUser(user.getUsername(), user.getEmail());
        // JpaUser jpaUser = new JpaUser(UUID.randomUUID().toString(), user.getUsername(), user.getEmail());
        userRepository.save(jpaUser);
        return user;
    }

}