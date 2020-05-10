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
    private Color checked = null;
    private ChessTurn lastTurn = null;

    private Boolean whiteRook0Moved = false;
    private Boolean whiteRook7Moved = false;
    private Boolean whiteKingMoved = false;

    private Boolean blackRook0Moved = false;
    private Boolean blackRook7Moved = false;
    private Boolean blackKingMoved = false;

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

    public Color getChecked() {
        return this.checked;
    }

    public void setChecked(Color checked) {
        this.checked = checked;
    }

    public ChessTurn getLastTurn() {
        return this.lastTurn;
    }

    public void setLastTurn(ChessTurn lastTurn) {
        this.lastTurn = lastTurn;
    }

    public Boolean isWhiteRook0Moved() {
        return this.whiteRook0Moved;
    }

    public Boolean getWhiteRook0Moved() {
        return this.whiteRook0Moved;
    }

    public void setWhiteRook0Moved(Boolean whiteRook0Moved) {
        this.whiteRook0Moved = whiteRook0Moved;
    }

    public Boolean isWhiteRook7Moved() {
        return this.whiteRook7Moved;
    }

    public Boolean getWhiteRook7Moved() {
        return this.whiteRook7Moved;
    }

    public void setWhiteRook7Moved(Boolean whiteRook7Moved) {
        this.whiteRook7Moved = whiteRook7Moved;
    }

    public Boolean isWhiteKingMoved() {
        return this.whiteKingMoved;
    }

    public Boolean getWhiteKingMoved() {
        return this.whiteKingMoved;
    }

    public void setWhiteKingMoved(Boolean whiteKingMoved) {
        this.whiteKingMoved = whiteKingMoved;
    }

    public Boolean isBlackRook0Moved() {
        return this.blackRook0Moved;
    }

    public Boolean getBlackRook0Moved() {
        return this.blackRook0Moved;
    }

    public void setBlackRook0Moved(Boolean blackRook0Moved) {
        this.blackRook0Moved = blackRook0Moved;
    }

    public Boolean isBlackRook7Moved() {
        return this.blackRook7Moved;
    }

    public Boolean getBlackRook7Moved() {
        return this.blackRook7Moved;
    }

    public void setBlackRook7Moved(Boolean blackRook7Moved) {
        this.blackRook7Moved = blackRook7Moved;
    }

    public Boolean isBlackKingMoved() {
        return this.blackKingMoved;
    }

    public Boolean getBlackKingMoved() {
        return this.blackKingMoved;
    }

    public void setBlackKingMoved(Boolean blackKingMoved) {
        this.blackKingMoved = blackKingMoved;
    }
}
