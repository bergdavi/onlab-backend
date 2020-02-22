package com.bergdavi.onlab.gameservice.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.bergdavi.onlab.gameservice.GameController;
import com.bergdavi.onlab.gameservice.model.Game;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * GameControllerImpl
 */
@RestController
public class GameControllerImpl implements GameController {

    @Override
    public ResponseEntity<List<Game>> getAllGames(HttpServletRequest httpRequest) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ResponseEntity<Game> getGameById(String gameId, HttpServletRequest httpRequest) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ResponseEntity<Long> joinGameQueue(String gameId, HttpServletRequest httpRequest) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ResponseEntity<?> leaveGameQueue(String gameId, HttpServletRequest httpRequest) {
        // TODO Auto-generated method stub
        return null;
    }    
}