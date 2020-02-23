package com.bergdavi.onlab.gameservice.service;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.bergdavi.onlab.gameservice.jpa.model.JpaGame;
import com.bergdavi.onlab.gameservice.jpa.model.JpaGameplay;
import com.bergdavi.onlab.gameservice.jpa.repository.GameRepository;
import com.bergdavi.onlab.gameservice.jpa.repository.GameplayRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * GameplayService
 */
@Service
public class CommonGameService {
    private GameplayRepository gameplayRepository;
    private GameRepository gameRepository;

    private Map<String, AbstractGameService<?, ?>> delegateServices = new HashMap<>();

    @Autowired
    public CommonGameService(GameRepository gameRepository, GameplayRepository gameplayRepository)
            throws IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        this.gameRepository = gameRepository;
        this.gameplayRepository = gameplayRepository;

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

    public void playTurn(String gameplayId, String gameTurn) {
        JpaGameplay jpaGameplay = gameplayRepository.findById(gameplayId).get();
        Integer nextUserIdx = jpaGameplay.getNextUserIdx();
        String gameState = delegateTurn(jpaGameplay.getGame().getId(), nextUserIdx, gameTurn, jpaGameplay.getGameState());

        jpaGameplay.setGameState(gameState);
        jpaGameplay.setNextUserIdx(((nextUserIdx + 1)%jpaGameplay.getUserCount())+1);

        gameplayRepository.save(jpaGameplay);

        System.out.println(gameTurn);
    }

    private String delegateTurn(String gameType, Integer userIdx, String gameTurn, String gameState) {
        delegateServices.get(gameType).playTurn(userIdx, gameTurn, gameState);
        return "";
    }
}