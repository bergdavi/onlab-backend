package com.bergdavi.onlab.gameservice.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import com.bergdavi.onlab.gameservice.jpa.model.JpaGame;
import com.bergdavi.onlab.gameservice.jpa.model.JpaGameQueue;
import com.bergdavi.onlab.gameservice.jpa.model.JpaGameplay;
import com.bergdavi.onlab.gameservice.jpa.model.JpaUserGameplay;
import com.bergdavi.onlab.gameservice.jpa.repository.GameQueueRepository;
import com.bergdavi.onlab.gameservice.jpa.repository.GameRepository;
import com.bergdavi.onlab.gameservice.jpa.repository.GameplayRepository;
import com.bergdavi.onlab.gameservice.jpa.repository.UserGameplayRepository;
import com.bergdavi.onlab.gameservice.model.Notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * GameQueueMatchingService
 */
@Service
public class GameQueueMatchingService {

        @Autowired
        private GameQueueRepository gameQueueRepository;
        @Autowired
        private GameRepository gameRepository;
        @Autowired
        private GameplayRepository gameplayRepository;
        @Autowired 
        private UserGameplayRepository userGameplayRepository;

        @Autowired
        private UserService userService;

        @Autowired
        private SimpMessagingTemplate simpMessagingTemplate;


        @Transactional
        public void matchQueue(String gameId) {
            JpaGame jpaGame = gameRepository.findById(gameId).get();
            List<JpaGameQueue> matches = gameQueueRepository.getTopUsersInQueue(gameId, PageRequest.of(0, jpaGame.getMaxPlayers()));
            if(matches.size() >= jpaGame.getMinPlayers()) {
                Date now = new Date();
                JpaGameplay jpaGameplay = new JpaGameplay();
                jpaGameplay.setGame(jpaGame);
                jpaGameplay.setUserCount(matches.size());
                jpaGameplay.setGameState(jpaGame.getInitialState());
                jpaGameplay.setStarted(now);
                gameplayRepository.save(jpaGameplay);

                List<JpaUserGameplay> userGameplays = new ArrayList<>();
                int idx = 0;
                for(JpaGameQueue match : matches) {
                    JpaUserGameplay userGameplay = new JpaUserGameplay(match.getUser(), jpaGameplay.getId(), idx++);
                    userGameplays.add(userGameplay);
                }
                userGameplayRepository.saveAll(userGameplays);
                
                gameQueueRepository.deleteAll(matches);
                
                for(JpaGameQueue match : matches) {
                    simpMessagingTemplate.convertAndSendToUser(userService.getUsernameById(match.getUser()), "/topic/notification", new Notification("Match found", "Match found for game: " + jpaGame.getName()));
                }
                System.out.println("Match found");
            } else {
                // System.err.println("No match found!");
            }
        }
    
}