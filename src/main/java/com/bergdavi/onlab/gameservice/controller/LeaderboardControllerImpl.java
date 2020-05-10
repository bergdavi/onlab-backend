package com.bergdavi.onlab.gameservice.controller;

import javax.servlet.http.HttpServletRequest;

import com.bergdavi.onlab.gameservice.LeaderboardController;
import com.bergdavi.onlab.gameservice.model.GameLeaderboard;
import com.bergdavi.onlab.gameservice.model.Leaderboard;
import com.bergdavi.onlab.gameservice.service.LeaderboardService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LeaderboardControllerImpl implements LeaderboardController {

    @Autowired
    private LeaderboardService leaderboardService;

    @Override
    public ResponseEntity<Leaderboard> getLeaderboard(HttpServletRequest httpRequest) {
        return new ResponseEntity<Leaderboard>(leaderboardService.getGlobalLeaderboard(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<GameLeaderboard> getGameLeaderboard(String gameId, HttpServletRequest httpRequest) {        
        return new ResponseEntity<GameLeaderboard>(leaderboardService.getGameLeaderboard(gameId), HttpStatus.OK);
    }
}
