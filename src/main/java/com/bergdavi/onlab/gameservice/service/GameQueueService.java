package com.bergdavi.onlab.gameservice.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.bergdavi.onlab.gameservice.jpa.model.JpaGame;
import com.bergdavi.onlab.gameservice.jpa.model.JpaGameInvite;
import com.bergdavi.onlab.gameservice.jpa.model.JpaGameInvited;
import com.bergdavi.onlab.gameservice.jpa.model.JpaGameInvitedPK;
import com.bergdavi.onlab.gameservice.jpa.model.JpaGameQueue;
import com.bergdavi.onlab.gameservice.jpa.model.JpaGameQueuePk;
import com.bergdavi.onlab.gameservice.jpa.model.JpaUser;
import com.bergdavi.onlab.gameservice.jpa.repository.GameInviteRepository;
import com.bergdavi.onlab.gameservice.jpa.repository.GameInvitedRepository;
import com.bergdavi.onlab.gameservice.jpa.repository.GameQueueRepository;
import com.bergdavi.onlab.gameservice.model.Game;

import org.springframework.beans.factory.annotation.Autowired;
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
    private GameInviteRepository gameInviteRepository;

    @Autowired
    private GameInvitedRepository gameInvitedRepository;

    @Autowired
    private GameQueueMatchingService gameQueueMatchingService;    

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

    public void inviteUsersToGame(String gameId, String inviterId, List<String> inviteeIds) {
        Game game = commonGameService.getGameById(gameId);
        if(game.getMinPlayers() > inviteeIds.size() + 1 || game.getMaxPlayers() < inviteeIds.size() + 1) {
            // TODO proper exception handling
            throw new RuntimeException();
        }
        JpaGameInvite jpaGameInvite = new JpaGameInvite();
        jpaGameInvite.setGame(new JpaGame(gameId));
        jpaGameInvite.setInviter(new JpaUser(inviterId));
        jpaGameInvite.setInviteDate(new Date());

        jpaGameInvite = gameInviteRepository.save(jpaGameInvite);

        for(String inviteeId : inviteeIds) {
            jpaGameInvite.addInvitee(inviteeId);
        }

        gameInvitedRepository.saveAll(jpaGameInvite.getInvitees());
    }

    public void acceptGameInvite(String inviteId, String userId) {
        JpaGameInvitedPK invitedPK = new JpaGameInvitedPK(inviteId, userId);
        Optional<JpaGameInvited> gameInvitedOpt = gameInvitedRepository.findById(invitedPK);
        if(!gameInvitedOpt.isPresent()) {
            // TODO better exception handling
            throw new RuntimeException();
        }
        JpaGameInvited gameInvited = gameInvitedOpt.get();
        gameInvited.setAccepted(true);
        gameInvitedRepository.save(gameInvited);
    }

    public void declineGameInvite(String inviteId, String userId) {
        Optional<JpaGameInvite> gameInviteOpt = gameInviteRepository.findById(inviteId);
        if(!gameInviteOpt.isPresent()) {
            // TODO better exception handling
            throw new RuntimeException();
        }
        JpaGameInvite gameInvite = gameInviteOpt.get();
        gameInvitedRepository.deleteAll(gameInvite.getInvitees());
        gameInviteRepository.delete(gameInvite);
    }

    @Scheduled(fixedRate = 1000)
    public void findMatchesInQueue() {
        for(String gameId : commonGameService.getGameIds()) {
            gameQueueMatchingService.matchQueue(gameId);
        }
        for(JpaGameInvite invite : gameInviteRepository.findAll()) {
            // TODO avoid double query
            gameQueueMatchingService.matchInvites(invite.getInviteId());
        }
    }
}
