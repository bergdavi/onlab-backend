package com.bergdavi.onlab.gameservice.service.delegate.chess.dto;

import com.bergdavi.onlab.gameservice.service.delegate.chess.figures.Color;
import com.bergdavi.onlab.gameservice.service.delegate.chess.figures.Figure;
import com.bergdavi.onlab.gameservice.service.delegate.chess.figures.FigureType;

/**
 * ChessState
 */
public class ChessState {
    private Figure[][] board;
    private Boolean offeredDraw = false;
    private Boolean acceptedDraw = false;
    private Color forfeited = null;

    public ChessState() {
        board = new Figure[8][8];

        board[0][0] = new Figure(Color.WHITE, FigureType.ROOK);
        board[1][0] = new Figure(Color.WHITE, FigureType.KNIGHT);
        board[2][0] = new Figure(Color.WHITE, FigureType.BISHOP);
        board[3][0] = new Figure(Color.WHITE, FigureType.QUEEN);
        board[4][0] = new Figure(Color.WHITE, FigureType.KING);
        board[5][0] = new Figure(Color.WHITE, FigureType.BISHOP);
        board[6][0] = new Figure(Color.WHITE, FigureType.KNIGHT);
        board[7][0] = new Figure(Color.WHITE, FigureType.ROOK);
        for(int i = 0; i < 8; i++) {
            board[i][1] = new Figure(Color.WHITE, FigureType.PAWN);
        }

        board[0][7] = new Figure(Color.BLACK, FigureType.ROOK);
        board[1][7] = new Figure(Color.BLACK, FigureType.KNIGHT);
        board[2][7] = new Figure(Color.BLACK, FigureType.BISHOP);
        board[3][7] = new Figure(Color.BLACK, FigureType.QUEEN);
        board[4][7] = new Figure(Color.BLACK, FigureType.KING);
        board[5][7] = new Figure(Color.BLACK, FigureType.BISHOP);
        board[6][7] = new Figure(Color.BLACK, FigureType.KNIGHT);
        board[7][7] = new Figure(Color.BLACK, FigureType.ROOK);
        for(int i = 0; i < 8; i++) {
            board[i][6] = new Figure(Color.BLACK, FigureType.PAWN);
        }
    }


    public Figure[][] getBoard() {
        return this.board;
    }

    public void setBoard(Figure[][] board) {
        this.board = board;
    }


    public Boolean isOfferedDraw() {
        return this.offeredDraw;
    }

    public Boolean getOfferedDraw() {
        return this.offeredDraw;
    }

    public void setOfferedDraw(Boolean offeredDraw) {
        this.offeredDraw = offeredDraw;
    }


    public Boolean isAcceptedDraw() {
        return this.acceptedDraw;
    }

    public Boolean getAcceptedDraw() {
        return this.acceptedDraw;
    }

    public void setAcceptedDraw(Boolean acceptedDraw) {
        this.acceptedDraw = acceptedDraw;
    }

    public Color getForfeited() {
        return this.forfeited;
    }

    public void setForfeited(Color forfeited) {
        this.forfeited = forfeited;
    }
}
