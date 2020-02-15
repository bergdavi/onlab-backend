package com.bergdavi.onlab.gameservice.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.bergdavi.onlab.gameservice.UserController;
import com.bergdavi.onlab.gameservice.model.Type;
import com.bergdavi.onlab.gameservice.model.User;
import com.bergdavi.onlab.gameservice.model.UserDetails;
import com.bergdavi.onlab.gameservice.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

/**
 * UserControllerImpl
 */
@RestController
public class UserControllerImpl implements UserController {

    @Autowired
    UserService userService;

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<User>> getUsers(HttpServletRequest httpRequest) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ResponseEntity<User> createUser(@Valid User user, HttpServletRequest httpRequest) {
        if(user.getType() == Type.ADMIN && !httpRequest.isUserInRole("ROLE_ADMIN")) {
            // TODO throw exception instead
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        userService.registerUser(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<UserDetails> getUserDetailsByUserId(String userId, HttpServletRequest httpRequest) {
        // TODO Auto-generated method stub
        return null;
    }

    
   

    
}