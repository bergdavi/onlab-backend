package com.bergdavi.onlab.gameservice.controller;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.bergdavi.onlab.gameservice.GameController;
import com.bergdavi.onlab.gameservice.model.Game;
import com.bergdavi.onlab.gameservice.service.CommonGameService;
import com.bergdavi.onlab.gameservice.service.GameQueueService;
import com.bergdavi.onlab.gameservice.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<List<Game>> getAllGames(HttpServletRequest httpRequest) {
        return new ResponseEntity<>(commonGameService.getAllGames(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Game> getGameById(String gameId, HttpServletRequest httpRequest) {
        return new ResponseEntity<>(commonGameService.getGameById(gameId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Long> joinGameQueue(String gameId, HttpServletRequest httpRequest) {
        String userId = userService.getUserIdByUsername(httpRequest.getUserPrincipal().getName());
        long queueLength = gameQueueService.joinQueue(gameId, userId);
        return new ResponseEntity<>(queueLength, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> leaveGameQueue(String gameId, HttpServletRequest httpRequest) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ResponseEntity<?> inviteUsersToGame(String gameId, @Valid List<String> userIds, HttpServletRequest httpRequest) {
        String inviterId = userService.getUserIdByUsername(httpRequest.getUserPrincipal().getName());
        gameQueueService.inviteUsersToGame(gameId, inviterId, userIds);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
