package com.bergdavi.onlab.gameservice.controller;

import java.security.Principal;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.bergdavi.onlab.gameservice.GameplayController;
import com.bergdavi.onlab.gameservice.exception.BadUserException;
import com.bergdavi.onlab.gameservice.exception.GameOverException;
import com.bergdavi.onlab.gameservice.exception.InvalidStepException;
import com.bergdavi.onlab.gameservice.model.GameTurnStatus;
import com.bergdavi.onlab.gameservice.model.Gameplay;
import com.bergdavi.onlab.gameservice.model.Status;
import com.bergdavi.onlab.gameservice.model.User;
import com.bergdavi.onlab.gameservice.model.UserGameplay;
import com.bergdavi.onlab.gameservice.service.CommonGameService;
import com.bergdavi.onlab.gameservice.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
    public ResponseEntity<UserGameplay> getUserGameplayByGameplayId(String gameplayId, HttpServletRequest httpRequest) {      
        String userId = userService.getUserIdByUsername(httpRequest.getUserPrincipal().getName());  
        return new ResponseEntity<>(commonGameService.getUserGameplayByGameplayId(gameplayId, userId), HttpStatus.OK);
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
        ObjectMapper objectMapper = new ObjectMapper();
        Status turnStatus = null;
        try {
            String gameState = commonGameService.playTurn(userId, gameplayId, gameTurn);
            for (User user : commonGameService.getAllUsersInGameplay(gameplayId)) {
                simpMessagingTemplate.convertAndSendToUser(user.getUsername(), "/topic/gameplay/" + gameplayId,
                        "s|" + gameState);
            }
        } catch(BadUserException e) {
            turnStatus = Status.INVALID_USER;
        } catch(InvalidStepException e) {
            turnStatus = Status.INVALID_STEP;
        } catch(GameOverException e) {
            turnStatus = Status.FINISHED;
        } catch(Exception e) {
            turnStatus = Status.UNEXPECTED_ERROR;
        } finally {
            if(turnStatus != null) {
                Date now = new Date();
                GameTurnStatus gameTurnStatus = new GameTurnStatus(turnStatus, now.toString(), gameTurn);
                try {
                    String gameTurnStatusString = objectMapper.writeValueAsString(gameTurnStatus);
                    // TODO create a seperate channel for this
                    simpMessagingTemplate.convertAndSendToUser(principal.getName(), "/topic/gameplay/" + gameplayId,
                            "t|" + gameTurnStatusString);
                } catch (JsonProcessingException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }            
        }
    }

    @SubscribeMapping("/topic/gameplay/{gameplayId}")
    public String gameplaySubscribe(@DestinationVariable String gameplayId, Principal principal) {
        return "s|" + commonGameService.getGameplayById(gameplayId).getGameState();
    }
}