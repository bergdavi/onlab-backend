package com.bergdavi.onlab.gameservice.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.bergdavi.onlab.gameservice.jpa.model.GameplayResult;
import com.bergdavi.onlab.gameservice.jpa.model.JpaGame;
import com.bergdavi.onlab.gameservice.jpa.model.JpaGameplay;
import com.bergdavi.onlab.gameservice.jpa.model.JpaUserGameplay;
import com.bergdavi.onlab.gameservice.jpa.model.JpaUserGameplayPk;
import com.bergdavi.onlab.gameservice.jpa.repository.GameRepository;
import com.bergdavi.onlab.gameservice.jpa.repository.GameplayRepository;
import com.bergdavi.onlab.gameservice.jpa.repository.UserGameplayRepository;
import com.bergdavi.onlab.gameservice.model.Game;
import com.bergdavi.onlab.gameservice.model.Gameplay;
import com.bergdavi.onlab.gameservice.model.Status;
import com.bergdavi.onlab.gameservice.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * GameplayService
 */
@Service
public class CommonGameService {
    
    private GameRepository gameRepository;

    @Autowired
    private GameplayRepository gameplayRepository;    
    
    @Autowired
    private UserGameplayRepository userGameplayRepository;

    @Autowired
    private ConversionService conversionService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    private Map<String, AbstractGameService<?, ?>> delegateServices = new HashMap<>();

    @Autowired
    public CommonGameService(GameRepository gameRepository)
            throws IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        this.gameRepository = gameRepository;

    
        Reflections r = new Reflections("com.bergdavi.onlab.gameservice.service");

        for (Class<?> c : r.getTypesAnnotatedWith(GameService.class)) {
            try {
                if (AbstractGameService.class.isAssignableFrom(c)) {
                    Class<?> gameStateType = c.getAnnotation(GameService.class).gameStateType();
                    Class<?> gameTurnType = c.getAnnotation(GameService.class).gameTurnType();
                    String gameId = c.getAnnotation(GameService.class).id();

                    AbstractGameService<?, ?> gameService = (AbstractGameService<?, ?>) c.newInstance();
                    gameService.setGameStateType(gameStateType);
                    gameService.setGameTurnType(gameTurnType);
                    delegateServices.put(gameId, gameService);
                    try {
                        updateGameInDatabase(c.getAnnotation(GameService.class), gameService.getInitialState());
                    } catch(Exception e) {
                        // TODO better error handling
                        System.err.println("Bad state: " + c.getSimpleName());
                    }
                } else {
                    // TODO better error handling
                    System.err.println("Bad class: " + c.getSimpleName());
                }
            } catch (InstantiationException | IllegalAccessException e) {
                // TODO better exception handling
                System.err.println(e.toString());
            }
        }        
    }

    /**
     * This function inserts / updates the initial game entries in the database
     * @param gameService game service to access the DB
     * @param initialState Initial state of the game
     * @throws JsonProcessingException
     */
    private void updateGameInDatabase(GameService gameService, Object initialState) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String gameState = objectMapper.writeValueAsString(initialState);

        JpaGame jpaGame  = null;
        Optional<JpaGame> gameInDatabase = gameRepository.findById(gameService.id());
        if(gameInDatabase.isPresent()) {
            jpaGame = gameInDatabase.get();
        } else {
            jpaGame = new JpaGame();
            jpaGame.setId(gameService.id());
        }
        jpaGame.setName(gameService.name());
        jpaGame.setDescription(gameService.description());
        jpaGame.setMinPlayers(gameService.minPlayers());
        jpaGame.setMaxPlayers(gameService.maxPlayers());
        jpaGame.setInitialState(gameState);        

        gameRepository.save(jpaGame);
    }

    public Iterable<String> getGameIds() {
        return delegateServices.keySet();
    }

    // TODO this class shouldn't access Jpa directly
    public String playTurn(String userId, String gameplayId, String gameTurn) {
        JpaGameplay jpaGameplay = gameplayRepository.findById(gameplayId).get();
        if (jpaGameplay.getStatus() != Status.IN_PROGRESS) {
            throw new RuntimeException("GAME OVER");
        }

        Integer nextUserIdx = jpaGameplay.getNextUserIdx();
        Integer currentUserIdx = userGameplayRepository.getGameplayUserIdx(new JpaUserGameplayPk(userId, gameplayId));
        if(nextUserIdx != currentUserIdx) {
            throw new RuntimeException("BAD USER");
        }
        AbstractGameService<?, ?> delegateService = delegateServices.get(jpaGameplay.getGame().getId());
        String gameState = delegateService.playTurn(nextUserIdx, gameTurn, jpaGameplay.getGameState());
        Optional<List<Integer>> winnersOpt = delegateService.getGameWinners(gameState);
        // Game ended?
        if(winnersOpt.isPresent()) {
            Date now = new Date();
            List<Integer> winners = winnersOpt.get();
            jpaGameplay.setStatus(Status.FINISHED);
            jpaGameplay.setFinished(now);
            // If winners is empty, or contains all users the game is a draw
            if(winners.isEmpty() || winners.size() == jpaGameplay.getUserGameplays().size()){
                for(JpaUserGameplay jpaUserGameplay : jpaGameplay.getUserGameplays()) {
                    jpaUserGameplay.setResult(GameplayResult.DRAW);
                    // TODO save all results at once
                    userGameplayRepository.save(jpaUserGameplay);
                    // TODO send a proper result object
                    simpMessagingTemplate.convertAndSendToUser(jpaUserGameplay.getUser().getUsername(), "/topic/gameplay/" + gameplayId, "e|{\"result\":\"draw\"}");
                }
            } else {
                for(JpaUserGameplay jpaUserGameplay : jpaGameplay.getUserGameplays()) {
                    if(winners.contains(jpaUserGameplay.getUserIdx())) {
                        // TODO save all results at once
                        jpaUserGameplay.setResult(GameplayResult.WIN);
                        // TODO send a proper result object
                        simpMessagingTemplate.convertAndSendToUser(jpaUserGameplay.getUser().getUsername(), "/topic/gameplay/" + gameplayId, "e|{\"result\":\"win\"}");
                    } else {
                        // TODO save all results at once
                        jpaUserGameplay.setResult(GameplayResult.LOSE);
                        // TODO send a proper result object
                        simpMessagingTemplate.convertAndSendToUser(jpaUserGameplay.getUser().getUsername(), "/topic/gameplay/" + gameplayId, "e|{\"result\":\"lose\"}");
                    }
                    userGameplayRepository.save(jpaUserGameplay);
                }
            }            
        }

        jpaGameplay.setGameState(gameState);
        jpaGameplay.setNextUserIdx((nextUserIdx+1)%jpaGameplay.getUserCount());

        gameplayRepository.save(jpaGameplay);
        return gameState;
    }

    public Gameplay getGameplayById(String gameplayId) {
        Optional<JpaGameplay> jpaGameplayOpt = gameplayRepository.findById(gameplayId);
        if(!jpaGameplayOpt.isPresent()) {
            // TODO proper exception handling
            return null;
        }
        return conversionService.convert(jpaGameplayOpt.get(), Gameplay.class);
    }

    public List<Game> getAllGames() {
        return StreamSupport.stream(gameRepository.findAll().spliterator(), false)
            .map(g -> conversionService.convert(g, Game.class)).collect(Collectors.toList());
    }

    public Game getGameById(String gameId) {
        Optional<JpaGame> jpaGameOpt = gameRepository.findById(gameId);
        if(!jpaGameOpt.isPresent()) {
            // TODO proper exception handling
            return null;
        }
        return conversionService.convert(jpaGameOpt.get(), Game.class);
    }

    public List<User> getAllUsersInGameplay(String gameplayId) {
        Optional<JpaGameplay> jpaGameplayOpt = gameplayRepository.findById(gameplayId);
        if(!jpaGameplayOpt.isPresent()) {
            // TODO proper exception handling
            return null;        
        }
        List<User> users = new ArrayList<>();
        for(JpaUserGameplay jpaUserGameplay : jpaGameplayOpt.get().getUserGameplays()) {
            users.add(conversionService.convert(jpaUserGameplay.getUser(), User.class));
        }
        return users;
    }
}
