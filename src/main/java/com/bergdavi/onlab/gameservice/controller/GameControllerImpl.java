package com.bergdavi.onlab.gameservice.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.bergdavi.onlab.gameservice.GameController;
import com.bergdavi.onlab.gameservice.model.Game;
import com.bergdavi.onlab.gameservice.model.User;
import com.bergdavi.onlab.gameservice.service.CommonGameService;
import com.bergdavi.onlab.gameservice.service.GameQueueService;
import com.bergdavi.onlab.gameservice.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * GameControllerImpl
 */
@RestController
public class GameControllerImpl implements GameController {

    @Autowired
    private GameQueueService gameQueueService;

    @Autowired
    private CommonGameService commonGameService;

    @Autowired
    private UserService userService;

    @Override
    public ResponseEntity<List<Game>> getAllGames(String all, HttpServletRequest httpRequest) {
        boolean includeDisabled = false;
        if(all != null) {
            if(httpRequest.isUserInRole("ROLE_ADMIN")) {
                includeDisabled = true;
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }
        }
        return new ResponseEntity<>(commonGameService.getAllGames(includeDisabled), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Game> getGameById(String gameId, HttpServletRequest httpRequest) {
        return new ResponseEntity<>(commonGameService.getGameById(gameId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Long> joinGameQueue(String gameId, HttpServletRequest httpRequest) {
        User user = userService.getUserByUsername(httpRequest.getUserPrincipal().getName()).getUser();
        if(user.getBanned()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        long queueLength = gameQueueService.joinQueue(gameId, user.getId());
        return new ResponseEntity<>(queueLength, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> inviteUsersToGame(String gameId, @Valid List<String> userIds, HttpServletRequest httpRequest) {
        User inviterUser = userService.getUserByUsername(httpRequest.getUserPrincipal().getName()).getUser();
        if(inviterUser.getBanned()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        gameQueueService.inviteUsersToGame(gameId, inviterUser.getId(), userIds);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> enableGame(String gameId, HttpServletRequest httpRequest) {
        if(!httpRequest.isUserInRole("ROLE_ADMIN")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        commonGameService.setGameEnabled(gameId, true);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> disableGame(String gameId, HttpServletRequest httpRequest) {
        if(!httpRequest.isUserInRole("ROLE_ADMIN")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        commonGameService.setGameEnabled(gameId, false);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
