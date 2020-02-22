package com.bergdavi.onlab.gameservice.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.bergdavi.onlab.gameservice.GameplayController;
import com.bergdavi.onlab.gameservice.model.GamePlay;
import com.bergdavi.onlab.gameservice.model.GameState;
import com.bergdavi.onlab.gameservice.model.GameTurn;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * GameplayControllerImpl
 */
@RestController
public class GameplayControllerImpl implements GameplayController {

    @Override
    public ResponseEntity<GamePlay> getGamePlayById(String gamePlayId, HttpServletRequest httpRequest) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ResponseEntity<GameState> playGameTurn(String gamePlayId, @Valid GameTurn gameTurn,
            HttpServletRequest httpRequest) {
        // TODO Auto-generated method stub
        return null;
    }

    
}