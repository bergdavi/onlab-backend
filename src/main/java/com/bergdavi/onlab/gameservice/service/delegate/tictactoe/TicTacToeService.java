package com.bergdavi.onlab.gameservice.service.delegate.tictactoe;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.bergdavi.onlab.gameservice.service.AbstractGameService;
import com.bergdavi.onlab.gameservice.service.GameService;
import com.bergdavi.onlab.gameservice.service.delegate.tictactoe.dto.TicTacToeState;
import com.bergdavi.onlab.gameservice.service.delegate.tictactoe.dto.TicTacToeTurn;

/**
 * TicTacToeService
 */
@GameService(id = "TICTACTOE", name = "Tic Tac Toe", description = "Tic Tac Toe game", minPlayers = 2, maxPlayers = 2, gameStateType = TicTacToeState.class, gameTurnType = TicTacToeTurn.class)
public class TicTacToeService extends AbstractGameService<TicTacToeState, TicTacToeTurn> {

    @Override
    public TicTacToeState playTurn(Integer playerIdx, TicTacToeTurn gameTurn, TicTacToeState gameState) {
        Integer target = gameState.getBoard()[gameTurn.getX()][gameTurn.getY()];
        if (target == null) {
            gameState.getBoard()[gameTurn.getX()][gameTurn.getY()] = playerIdx;
            return gameState;
        }
        throw new RuntimeException("Bad state");
    }

    @Override
    public TicTacToeState getInitialState() {
        Integer[][] board = { { null, null, null }, { null, null, null }, { null, null, null } };
        TicTacToeState gameState = new TicTacToeState();
        gameState.setBoard(board);
        return gameState;
    }

    @Override
    public Optional<Iterable<Integer>> getGameWinners(TicTacToeState gameState) {
        List<Integer> winners = new ArrayList<>();
        boolean gameOver = false;
        Integer[][] b = gameState.getBoard();
        for(int i = 0; i <= 1; i++) {
            if(
              (b[0][0] == i && b[0][1] == i && b[0][2] == i) || 
              (b[1][0] == i && b[1][1] == i && b[1][2] == i) ||
              (b[2][0] == i && b[2][1] == i && b[2][2] == i) ||
              (b[0][0] == i && b[1][0] == i && b[2][0] == i) ||
              (b[0][1] == i && b[1][1] == i && b[2][1] == i) ||
              (b[0][2] == i && b[1][2] == i && b[2][2] == i) ||
              (b[0][0] == i && b[1][1] == i && b[2][2] == i) ||
              (b[0][2] == i && b[1][1] == i && b[2][0] == i)
            ) {
                winners.add(i);
                gameOver = true;
            }
        }
        if(!gameOver) {
            gameOver = true;
            for(int i = 0; i < b.length; i++) {
                for(int j = 0; j < b[i].length; j++) {
                    if(b[i][j] == null) {
                        gameOver = false;
                    }
                }
            }
        }
        if(!gameOver) {
            return Optional.empty();
        }
        return Optional.of(winners);
    }
    
}