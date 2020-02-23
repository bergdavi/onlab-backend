package com.bergdavi.onlab.gameservice.service.delegate.tictactoe.dto;

/**
 * TicTacToeState
 */
public class TicTacToeState {

    private Integer[][] board;

    public TicTacToeState() {}


    public Integer[][] getBoard() {
        return this.board;
    }

    public void setBoard(Integer[][] board) {
        this.board = board;
    }

}