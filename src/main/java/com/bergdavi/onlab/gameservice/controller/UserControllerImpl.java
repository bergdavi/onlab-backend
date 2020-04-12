package com.bergdavi.onlab.gameservice.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.bergdavi.onlab.gameservice.UserController;
import com.bergdavi.onlab.gameservice.model.ChangePassword;
import com.bergdavi.onlab.gameservice.model.User;
import com.bergdavi.onlab.gameservice.model.UserDetails;
import com.bergdavi.onlab.gameservice.service.GameQueueService;
import com.bergdavi.onlab.gameservice.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * UserControllerImpl
 */
@RestController
public class UserControllerImpl implements UserController {

    @Autowired
    UserService userService;

    @Autowired
    GameQueueService gameQueueService;

    @Override
    public ResponseEntity<List<User>> getAllUsers(String username, HttpServletRequest httpRequest) {
        if(username == null) {
            return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
        } else if(username.isEmpty()) {
            return new ResponseEntity<>(new ArrayList<User>(), HttpStatus.OK);
        }
        return new ResponseEntity<>(userService.searchUserByName(username), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<User> registerUser(@Valid User user, HttpServletRequest httpRequest) {
        // if(user.getType() == Type.ADMIN && !httpRequest.isUserInRole("ROLE_ADMIN")) {
        // // TODO throw exception instead
        // return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        // }
        userService.registerUser(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
    
    @Override
    public ResponseEntity<?> changeUserPassword(@Valid ChangePassword changePassword, HttpServletRequest httpRequest) {
        userService.changeUserPassword(changePassword.getOldPassword(), changePassword.getNewPassword());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<UserDetails> getUserById(String userId, HttpServletRequest httpRequest) {
        // TODO add security check
        return new ResponseEntity<>(userService.getUserById(userId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<UserDetails> getCurrentUser(HttpServletRequest httpRequest) {
        return new ResponseEntity<>(userService.getUserByUsername(httpRequest.getUserPrincipal().getName()),
                HttpStatus.OK);
    }

    @SubscribeMapping("/topic/notifications")
    public String notificationSubscribe() {
        // TODO is this needed?
        return "";
    }

    @Override
    public ResponseEntity<?> acceptGameInvite(String inviteId, HttpServletRequest httpRequest) {
        gameQueueService.acceptGameInvite(inviteId, userService.getUserIdByUsername(httpRequest.getUserPrincipal().getName()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> declineGameInvite(String inviteId, HttpServletRequest httpRequest) {
        gameQueueService.declineGameInvite(inviteId, userService.getUserIdByUsername(httpRequest.getUserPrincipal().getName()));
        return null;
    }
}