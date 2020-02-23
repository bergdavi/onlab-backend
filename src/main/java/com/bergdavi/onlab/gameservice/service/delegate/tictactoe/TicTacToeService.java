package com.bergdavi.onlab.gameservice.service.delegate.tictactoe;

import com.bergdavi.onlab.gameservice.service.AbstractGameService;
import com.bergdavi.onlab.gameservice.service.GameService;
import com.bergdavi.onlab.gameservice.service.delegate.tictactoe.dto.TicTacToeState;
import com.bergdavi.onlab.gameservice.service.delegate.tictactoe.dto.TicTacToeTurn;

/**
 * TicTacToeService
 */
@GameService(id = "TICTACTOE", name = "Tic Tac Toe", description = "Tic Tac Toe game", minPlayers = 2, maxPlayers = 2,
    gameStateType = TicTacToeState.class, gameTurnType = TicTacToeTurn.class)
public class TicTacToeService extends AbstractGameService<TicTacToeState, TicTacToeTurn> {

    @Override
    public TicTacToeState playTurn(Integer playerIdx, TicTacToeTurn gameTurn, TicTacToeState gameState) {        
        // Integer target = gameState.getBoard().get(gameTurn.getX()).get(gameTurn.getY());
        // if(target.equals(0)) {
        //     gameState.getBoard().get(gameTurn.getX().intValue()).set(gameTurn.getY(), playerIdx);
        // } else {
            
        // }
        return null;
    }

    @Override
    public TicTacToeState getInitialState() {
        Integer[][] board = {{0,0,0}, {0,0,0}, {0,0,0}};
        TicTacToeState gameState = new TicTacToeState();
        gameState.setBoard(board);
        return gameState;
    }
    
}