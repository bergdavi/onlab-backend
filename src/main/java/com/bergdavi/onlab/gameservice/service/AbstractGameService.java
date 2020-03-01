package com.bergdavi.onlab.gameservice.service;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * AbstractGameService
 */
public abstract class AbstractGameService<GameStateType, GameTurnType> {

    private Class<GameStateType> gameStateType;
    private Class<GameTurnType> gameTurnType;

    /**
     * Apply a game turn to an existing game state
     * @param playerIdx Index of the user playing the turn
     * @param gameTurn The turn object being played
     * @param gameState The game state before the turn
     * @return The game state after the turn
     */
    public abstract GameStateType playTurn(Integer playerIdx, GameTurnType gameTurn, GameStateType gameState);

    /**
     * Get a list of winners for a game state
     * @param gameState Game state to check for winners
     * @return If the optional exists the game ends. The list contains the winners. If the list is empty / contains all players the result is a draw
     */
    public abstract Optional<List<Integer>> getGameWinners(GameStateType gameState);

    /**
     * Get initial state for the game
     * @return The initial state for the game
     */
    public abstract GameStateType getInitialState();


    final String playTurn(Integer playerIdx, String gameTurnString, String gameStateString) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            GameTurnType gameTurn = objectMapper.readValue(gameTurnString, gameTurnType);
            GameStateType gameState = objectMapper.readValue(gameStateString, gameStateType);
            GameStateType newState = playTurn(playerIdx, gameTurn, gameState);
            return objectMapper.writeValueAsString(newState);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public final Optional<List<Integer>> getGameWinners(String gameStateString) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            GameStateType gameState = objectMapper.readValue(gameStateString, gameStateType);
            return getGameWinners(gameState);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return Optional.empty();
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