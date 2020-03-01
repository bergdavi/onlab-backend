package com.bergdavi.onlab.gameservice.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.bergdavi.onlab.gameservice.GameplayController;
import com.bergdavi.onlab.gameservice.model.Gameplay;
import com.bergdavi.onlab.gameservice.service.CommonGameService;
import com.bergdavi.onlab.gameservice.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * GameplayControllerImpl
 */
@RestController
public class GameplayControllerImpl implements GameplayController {

    @Autowired
    private CommonGameService commonGameService;

    @Autowired
    private UserService userService;

    @Override
    public ResponseEntity<Gameplay> getGameplayById(String gameplayId, HttpServletRequest httpRequest) {
        // TODO check user permission
        return new ResponseEntity<>(commonGameService.getGameplayById(gameplayId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> playGameTurn(String gameplayId, @Valid String gameTurn,
            HttpServletRequest httpRequest) {
        // TODO check user permission
        String userId = userService.getUserIdByUsername(httpRequest.getUserPrincipal().getName());
        return new ResponseEntity<>(commonGameService.playTurn(userId, gameplayId, gameTurn), HttpStatus.OK);
    }    
}