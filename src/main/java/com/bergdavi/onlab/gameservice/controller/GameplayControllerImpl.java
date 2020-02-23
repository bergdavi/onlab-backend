package com.bergdavi.onlab.gameservice.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.bergdavi.onlab.gameservice.GameplayController;
import com.bergdavi.onlab.gameservice.model.Gameplay;
import com.bergdavi.onlab.gameservice.service.CommonGameService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * GameplayControllerImpl
 */
@RestController
public class GameplayControllerImpl implements GameplayController {

    @Autowired
    private CommonGameService commonGameService;

    @Override
    public ResponseEntity<Gameplay> getGameplayById(String gameplayId, HttpServletRequest httpRequest) {
        return null;
    }

    @Override
    public ResponseEntity<String> playGameTurn(String gamePlayId, @Valid String gameTurn,
            HttpServletRequest httpRequest) {
        commonGameService.playTurn(gamePlayId, gameTurn);
        return null;
    }

    
}