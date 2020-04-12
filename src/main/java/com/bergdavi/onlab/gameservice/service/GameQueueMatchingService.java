package com.bergdavi.onlab.gameservice.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.transaction.Transactional;

import com.bergdavi.onlab.gameservice.jpa.model.JpaGame;
import com.bergdavi.onlab.gameservice.jpa.model.JpaGameInvite;
import com.bergdavi.onlab.gameservice.jpa.model.JpaGameInvited;
import com.bergdavi.onlab.gameservice.jpa.model.JpaGameQueue;
import com.bergdavi.onlab.gameservice.jpa.model.JpaGameplay;
import com.bergdavi.onlab.gameservice.jpa.model.JpaUser;
import com.bergdavi.onlab.gameservice.jpa.model.JpaUserGameplay;
import com.bergdavi.onlab.gameservice.jpa.repository.GameInviteRepository;
import com.bergdavi.onlab.gameservice.jpa.repository.GameInvitedRepository;
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
        private GameInviteRepository gameInviteRepository;

        @Autowired
        private GameInvitedRepository gameInvitedRepository;

        @Autowired
        private SimpMessagingTemplate simpMessagingTemplate;

        @Transactional
        public void matchQueue(String gameId) {
            JpaGame jpaGame = gameRepository.findById(gameId).get();
            List<JpaGameQueue> matches = gameQueueRepository.getTopUsersInQueue(gameId, PageRequest.of(0, jpaGame.getMaxPlayers()));
            if(matches.size() >= jpaGame.getMinPlayers()) {
                startGame(
                    jpaGame, 
                    StreamSupport.stream(matches.spliterator(), false).map(ugp -> ugp.getUser()).collect(Collectors.toList())
                );
                gameQueueRepository.deleteAll(matches);
                System.out.println("Match found");
            } else {
                // System.err.println("No match found!");
            }
        }
        
        @Transactional
        public void matchInvites(String inviteId) {
            Optional<JpaGameInvite> jpaGameInviteOpt = gameInviteRepository.findById(inviteId);
            if(!jpaGameInviteOpt.isPresent()) {
                return;
            }
            JpaGameInvite jpaGameInvite = jpaGameInviteOpt.get();
            boolean allAccepted = true;
            for(JpaGameInvited jpaGameInvited : jpaGameInvite.getInvitees()) {
                if(!jpaGameInvited.getAccepted()) {
                    allAccepted = false;
                    break;
                }
            }
            if(allAccepted) {
                List<JpaUser> users = StreamSupport.stream(jpaGameInvite.getInvitees().spliterator(), false).map(ugp -> ugp.getInvited()).collect(Collectors.toList());
                users.add(jpaGameInvite.getInviter());
                startGame(
                    jpaGameInvite.getGame(),
                    users
                );
                gameInvitedRepository.deleteAll(jpaGameInvite.getInvitees());
                gameInviteRepository.delete(jpaGameInvite);
                System.out.println("Invite found");
            }
        }

        private void startGame(JpaGame jpaGame, List<JpaUser> users) {
            Date now = new Date();
            JpaGameplay jpaGameplay = new JpaGameplay();
            jpaGameplay.setGame(jpaGame);
            jpaGameplay.setUserCount(users.size());
            jpaGameplay.setGameState(jpaGame.getInitialState());
            jpaGameplay.setStarted(now);
            gameplayRepository.save(jpaGameplay);

            List<JpaUserGameplay> userGameplays = new ArrayList<>();
            int idx = 0;
            for(JpaUser user : users) {
                JpaUserGameplay userGameplay = new JpaUserGameplay(user.getId(), jpaGameplay.getId(), idx++);
                userGameplays.add(userGameplay);
            }
            userGameplayRepository.saveAll(userGameplays);            
            
            for(JpaUser user : users) {
                simpMessagingTemplate.convertAndSendToUser(user.getUsername(), "/topic/notification", new Notification("Match found", "Match found for game: " + jpaGame.getName()));
            }
        }
}