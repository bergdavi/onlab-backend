package com.bergdavi.onlab.gameservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.bergdavi.onlab.gameservice.jpa.model.JpaUser;
import com.bergdavi.onlab.gameservice.jpa.repository.UserRepository;
import com.bergdavi.onlab.gameservice.model.Type;
import com.bergdavi.onlab.gameservice.model.User;
import com.bergdavi.onlab.gameservice.model.UserDetails;

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
        if (user.getType() == Type.ADMIN) {
            roles.add("ADMIN");
        }
        if (jdbcUserDetailsManager.userExists(user.getUsername())) {
            // TODO replace with more appropriate exception
            // Return HTTP 409
            throw new RuntimeException();
        }       
        JpaUser jpaUser = new JpaUser(user.getUsername(), user.getEmail());
        userRepository.save(jpaUser);
        user.setId(jpaUser.getId());
        jdbcUserDetailsManager.createUser(org.springframework.security.core.userdetails.User
            .withUsername(user.getUsername()).password(passwordEncoder.encode(user.getPassword()))
            .roles(roles.toArray(new String[roles.size()])).build());
        return user;
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        for (JpaUser user : userRepository.findAll()) {
            // TODO fix type
            users.add(new User(user.getId(), user.getUsername(), user.getEmail(), "", Type.USER));
        }
        return users;
    }

    public UserDetails getUserById(String id) {
        Optional<JpaUser> optUser = userRepository.findById(id);
        if(optUser.isPresent()) {
            JpaUser user = optUser.get();
            // TODO fix type
            return new UserDetails(new User(user.getId(), user.getUsername(), user.getEmail(), "", Type.USER));
        } else {
            // TODO throw exception instead
            return null;
        }
    }

    public UserDetails getUserByUsername(String username) {
        JpaUser user = userRepository.findByUsername(username);
        // TODO fix type
        return new UserDetails(new User(user.getId(), user.getUsername(), user.getEmail(), "", Type.USER));
    }

}