package com.bergdavi.onlab.gameservice.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.bergdavi.onlab.gameservice.GameplayController;
import com.bergdavi.onlab.gameservice.model.Gameplay;
import com.bergdavi.onlab.gameservice.model.User;
import com.bergdavi.onlab.gameservice.service.CommonGameService;
import com.bergdavi.onlab.gameservice.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
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

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

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

    @MessageMapping("/gameplay/{gameplayId}")
    public void playGameTurn(@Payload String gameTurn, @DestinationVariable String gameplayId, Principal principal) {
        String userId = userService.getUserIdByUsername(principal.getName());
        String gameState = commonGameService.playTurn(userId, gameplayId, gameTurn);
        for(User user : commonGameService.getAllUsersInGameplay(gameplayId)) {
            simpMessagingTemplate.convertAndSendToUser(user.getUsername(), "/topic/gameplay/" + gameplayId, gameState);
        }
    }

    @SubscribeMapping("/topic/gameplay/{gameplayId}")
    public String gameplaySubscribe(@DestinationVariable String gameplayId, Principal principal) {
        return commonGameService.getGameplayById(gameplayId).getGameState();
    }
}