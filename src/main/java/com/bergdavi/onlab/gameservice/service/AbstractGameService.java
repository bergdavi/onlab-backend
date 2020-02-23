package com.bergdavi.onlab.gameservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * AbstractGameService
 */
public abstract class AbstractGameService<GameStateType, GameTurnType> {

    private Class<GameStateType> gameStateType;
    private Class<GameTurnType> gameTurnType;

    public abstract GameStateType playTurn(Integer playerIdx, GameTurnType gameTurn, GameStateType gameState);
    public abstract GameStateType getInitialState();


    final GameStateType playTurn(Integer playerIdx, String gameTurnString, String gameStateString) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            GameTurnType gameTurn = objectMapper.readValue(gameTurnString, gameTurnType);
            GameStateType gameState = objectMapper.readValue(gameStateString, gameStateType);
            return playTurn(playerIdx, gameTurn, gameState);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    final Class<GameStateType> getGameStateType() {
        return this.gameStateType;
    }

    @SuppressWarnings("unchecked")
    final void setGameStateType(Class<?> gameStateType) {
        this.gameStateType = (Class<GameStateType>) gameStateType;
    }

    final Class<GameTurnType> getGameTurnType() {
        return this.gameTurnType;
    }

    @SuppressWarnings("unchecked")
    final void setGameTurnType(Class<?> gameTurnType) {
        this.gameTurnType = (Class<GameTurnType>) gameTurnType;
    }
    
}