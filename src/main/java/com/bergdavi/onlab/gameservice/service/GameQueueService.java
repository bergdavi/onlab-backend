package com.bergdavi.onlab.gameservice.service;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import com.bergdavi.onlab.gameservice.jpa.model.JpaGame;
import com.bergdavi.onlab.gameservice.jpa.model.JpaGameQueue;
import com.bergdavi.onlab.gameservice.jpa.model.JpaGameQueuePk;
import com.bergdavi.onlab.gameservice.jpa.model.JpaGameplay;
import com.bergdavi.onlab.gameservice.jpa.repository.GameQueueRepository;
import com.bergdavi.onlab.gameservice.jpa.repository.GameRepository;
import com.bergdavi.onlab.gameservice.jpa.repository.GameplayRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * GameQueueService
 */
@Service
public class GameQueueService {
        
    @Autowired
    private CommonGameService commonGameService;

    @Autowired
    private GameQueueRepository gameQueueRepository;

    @Autowired
    private GameQueueMatchingService gameQueueMatchingService;

    // @Autowired
    // private GameRepository gameRepository;

    // @Autowired
    // private GameplayRepository gameplayRepository;

    

    public long joinQueue(String gameId, String userId) {
        JpaGameQueue jpaGameQueue = new JpaGameQueue();
        JpaGameQueuePk jpaGameQueuePk = new JpaGameQueuePk();
        jpaGameQueuePk.setGame(gameId);
        jpaGameQueuePk.setUser(userId);
        jpaGameQueue.setQueueId(jpaGameQueuePk);
        jpaGameQueue.setJoined(new Date());

        gameQueueRepository.save(jpaGameQueue);
        Long queueLength = gameQueueRepository.count();

        return queueLength;
    }

    // @Async
    // @Transactional
    // public void matchQueue(String gameId, String userId) {
    //     System.out.println("Trying to match user in queue");
    //     JpaGame jpaGame = gameRepository.findById(gameId).get();
    //     List<JpaGameQueue> matches = gameQueueRepository.getTopUsersInQueue(gameId, userId, PageRequest.of(0, jpaGame.getMaxPlayers()));
    //     if(matches.size() >= jpaGame.getMinPlayers()) {
    //         // JpaGameQueue match = matches.get(0);

    //         // JpaGame jpaGame = new JpaGame();
    //         // jpaGame.setId(gameId);
            
    //         JpaGameplay jpaGameplay = new JpaGameplay();
    //         jpaGameplay.setGame(jpaGame);
    //         jpaGameplay.setUserCount(matches.size());
    //         jpaGameplay.setGameState("{}");

            
    //         // gameQueueRepository.delete(match);
    //         // gameQueueRepository.deleteById(new JpaGameQueuePk(userId, gameId));

            
    //         System.out.println("Match found");
    //     } else {
    //         System.err.println("No match found!");
    //     }
    // }

    @Scheduled(fixedRate = 1000)
    public void findMatchesInQueue() {
        for(String gameId : commonGameService.getGameIds()) {
            gameQueueMatchingService.matchQueue(gameId);
        }
    }



}