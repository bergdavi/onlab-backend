package com.bergdavi.onlab.gameservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.bergdavi.onlab.gameservice.jpa.model.JpaUser;
import com.bergdavi.onlab.gameservice.jpa.repository.UserRepository;
import com.bergdavi.onlab.gameservice.model.User;
import com.bergdavi.onlab.gameservice.model.UserDetails;
import com.bergdavi.onlab.gameservice.model.UserType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/**
 * UserService
 */
@Service
public class UserService {

    private JdbcUserDetailsManager jdbcUserDetailsManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConversionService conversionService;

    @Autowired
    public UserService(JdbcUserDetailsManager jdbcUserDetailsManager, AuthenticationManager authenticationManager) {
        jdbcUserDetailsManager.setAuthenticationManager(authenticationManager);
        this.jdbcUserDetailsManager = jdbcUserDetailsManager;
    }

    public User registerUser(User user) {
        List<String> roles = new ArrayList<>();
        roles.add("USER");
        if (user.getUserType() == UserType.ADMIN) {
            roles.add("ADMIN");
        }
        if (jdbcUserDetailsManager.userExists(user.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }       
        JpaUser jpaUser = new JpaUser(user.getUsername(), user.getEmail());
        userRepository.save(jpaUser);
        user.setId(jpaUser.getId());
        jdbcUserDetailsManager.createUser(org.springframework.security.core.userdetails.User
            .withUsername(user.getUsername()).password(passwordEncoder.encode(user.getPassword()))
            .roles(roles.toArray(new String[roles.size()])).build());
        return user;
    }

    public void changeUserPassword(String oldPassword, String newPassword) {
        jdbcUserDetailsManager.changePassword(oldPassword, passwordEncoder.encode(newPassword));
    }

    public List<User> getAllUsers() {
        return StreamSupport.stream(userRepository.findAll().spliterator(), false)
            .map(u -> conversionService.convert(u, User.class)).collect(Collectors.toList());
    }

    public List<User> searchUserByName(String username) {
        return StreamSupport.stream(userRepository.searchByName(username).spliterator(), false)
            .map(u -> conversionService.convert(u, User.class)).collect(Collectors.toList());
    }

    public UserDetails getUserById(String id) {
        Optional<JpaUser> optUser = userRepository.findById(id);
        if(!optUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return conversionService.convert(optUser.get(), UserDetails.class);
    }

    public UserDetails getUserByUsername(String username) {
        return getUserById(getUserIdByUsername(username));
    }

    public String getUserIdByUsername(String username) {
        return userRepository.getIdByUsername(username);
    }

    public String getUsernameById(String id) {
        return userRepository.getUsernameById(id);
    }

    public void banUserById(String id) {
        Optional<JpaUser> optUser = userRepository.findById(id);
        if(!optUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        JpaUser user = optUser.get();
        user.setBanned(true);
        userRepository.save(user);
    }
}