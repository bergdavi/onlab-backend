package com.bergdavi.onlab.gameservice.controller;

import java.security.Principal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

/**
 * GameplayWebSocketController
 */
@Controller
public class GameplayWebSocketController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/gameplay/{gameplayId}")
    @SendTo("/topic/gameplay/{gameplayId}")
    public void testGameplay(@Payload String message, @DestinationVariable String gameplayId, Principal principal) {
        Date now = new Date();
        String response = now.toString() + " | " + gameplayId + " | " + message;
        simpMessagingTemplate.convertAndSendToUser("user", "/topic/gameplay/" + gameplayId, response);
        // simpMessagingTemplate.convertAndSend("/topic/gameplay/" + gameplayId, response);
        // return response;
    }

    @SubscribeMapping("/gameplay/{gameplayId}")
    public String testSubscribe(@DestinationVariable String gameplayId, Principal principal) {
        System.out.println("somebody subbed");
        return "Hello there!";
    }
    
}