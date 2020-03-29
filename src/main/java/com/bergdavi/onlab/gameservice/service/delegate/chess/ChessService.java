package com.bergdavi.onlab.gameservice.service.delegate.chess;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.bergdavi.onlab.gameservice.service.AbstractGameService;
import com.bergdavi.onlab.gameservice.service.GameService;
import com.bergdavi.onlab.gameservice.service.delegate.chess.dto.ChessState;
import com.bergdavi.onlab.gameservice.service.delegate.chess.dto.ChessTurn;
import com.bergdavi.onlab.gameservice.service.delegate.chess.figures.Color;
import com.bergdavi.onlab.gameservice.service.delegate.chess.figures.Figure;
import com.bergdavi.onlab.gameservice.service.delegate.chess.figures.FigureType;

import org.springframework.data.util.Pair;

/**
 * TicTacToeService
 */
@GameService(id = "CHESS", name = "Chess", description = "Chess game", minPlayers = 2, maxPlayers = 2, gameStateType = ChessState.class, gameTurnType = ChessTurn.class)
public class ChessService extends AbstractGameService<ChessState, ChessTurn> {

    @Override
    public ChessState playTurn(Integer playerIdx, ChessTurn gameTurn, ChessState gameState) {
        Color playerColor = getColorFromIdx(playerIdx);
        if(gameTurn.getForfeit()) {
            gameState.setForfeited(playerColor);
            return gameState;
        }
        if(gameTurn.getOfferDraw()) {
            if(gameState.getOfferedDraw()) {
                gameState.setAcceptedDraw(true);
            } else {
                gameState.setOfferedDraw(true);
            }
            return gameState;
        } else {
            if(gameState.getOfferedDraw()) {
                gameState.setOfferedDraw(false);
                return gameState;
            }
        }
        gameState.setOfferedDraw(false);
        Figure figure = gameState.getBoard()[gameTurn.getFromX()][gameTurn.getFromY()];
        if(figure == null) {
            // TODO better exception handling
            throw new RuntimeException();
        }

        if(playerColor != figure.getColor()) {
            throw new RuntimeException();
        }

        if(getPossibleSteps(gameState.getBoard(), gameTurn.getFromX(), gameTurn.getFromY()).contains(Pair.of(gameTurn.getToX(), gameTurn.getToY()))) {
            Figure[][] oldBoard = copyBoard(gameState.getBoard());
            performStep(figure, gameTurn, gameState);
            if(isChecked(playerColor, gameState.getBoard())) {
                gameState.setBoard(oldBoard);
                // TODO better exception handling
                throw new RuntimeException();
            }
        } else {
            // TODO better exception handling
            throw new RuntimeException();
        }

        return gameState;
    }

    @Override
    public Optional<List<Integer>> getGameWinners(ChessState gameState) {
        Optional<List<Integer>> winners = Optional.empty();
        if(gameState.getAcceptedDraw()) {
            winners = Optional.of(new ArrayList<>());
        }
        if(gameState.getForfeited() != null) {
            winners = Optional.of(new ArrayList<>());
            winners.get().add(getIdxFromColor(Color.invert(gameState.getForfeited())));
        }
        return winners;
    }

    @Override
    public ChessState getInitialState() {
        return new ChessState();
    }

    public Color getColorFromIdx(int playerIdx) {
        return playerIdx == 0 ? Color.WHITE : Color.BLACK;
    }

    public int getIdxFromColor(Color color) {
        return color == Color.WHITE ? 0 : 1;
    }

    private Figure[][] copyBoard(Figure[][] board) {
        Figure[][] newBoard = new Figure[8][8];
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                newBoard[i][j] = board[i][j];
            }
        }
        return newBoard;
    }

    public void performStep(Figure figure, ChessTurn gameTurn, ChessState gameState) {
        gameState.getBoard()[gameTurn.getFromX()][gameTurn.getFromY()] = null;
        gameState.getBoard()[gameTurn.getToX()][gameTurn.getToY()] = figure;
    }

    private boolean isChecked(Color color, Figure[][] board) {
        Figure king = null;
        int kingX = -1;
        int kingY = -1;
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if(board[i][j] != null && board[i][j].getColor() == color && board[i][j].getType() == FigureType.KING) {
                    king = board[i][j];
                    kingX = i;
                    kingY = j;
                    break;
                }
            }
            if(king != null) break;
        }
        return isChecked(color, board, kingX, kingY);
    }

    private boolean isChecked(Color color, Figure[][] board, int kingX, int kingY) {        
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                Figure figure = board[i][j];
                if(figure != null && figure.getColor() != color) {
                    List<Pair<Integer, Integer>> possibleSteps = getPossibleSteps(board, i, j);
                    if(possibleSteps.contains(Pair.of(kingX, kingY))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public List<Pair<Integer, Integer>> getPossibleSteps(Figure[][] board, int x, int y) {
        Figure figure = board[x][y];

        List<Pair<Integer, Integer>> possibleSteps = new ArrayList<>();

        switch(figure.getType()) {
            case KING:
                testCanStep(possibleSteps, board, figure, x + 1, y);
                testCanStep(possibleSteps, board, figure, x + 1, y + 1);
                testCanStep(possibleSteps, board, figure, x + 1, y - 1);
                testCanStep(possibleSteps, board, figure, x - 1, y);
                testCanStep(possibleSteps, board, figure, x - 1, y + 1);
                testCanStep(possibleSteps, board, figure, x - 1, y - 1);
                testCanStep(possibleSteps, board, figure, x, y + 1);
                testCanStep(possibleSteps, board, figure, x, y - 1);
                break;
            case QUEEN:
                for(int tmpX = x + 1, tmpY = y; tmpX < 8; tmpX++) {
                    testCanStep(possibleSteps, board, figure, tmpX, tmpY);
                    if(board[tmpX][tmpY] != null) break;
                }
                for(int tmpX = x - 1, tmpY = y; tmpX >= 0; tmpX--) {
                    testCanStep(possibleSteps, board, figure, tmpX, tmpY);
                    if(board[tmpX][tmpY] != null) break;
                }
                for(int tmpX = x, tmpY = y + 1; tmpY < 8; tmpY++) {
                    testCanStep(possibleSteps, board, figure, tmpX, tmpY);
                    if(board[tmpX][tmpY] != null) break;
                }
                for(int tmpX = x, tmpY = y - 1; tmpY >= 0; tmpY--) {
                    testCanStep(possibleSteps, board, figure, tmpX, tmpY);
                    if(board[tmpX][tmpY] != null) break;
                }

                for(int tmpX = x + 1, tmpY = y + 1; tmpX < 8 && tmpY < 8; tmpX++, tmpY++) {
                    testCanStep(possibleSteps, board, figure, tmpX, tmpY);
                    if(board[tmpX][tmpY] != null) break;
                }
                for(int tmpX = x + 1, tmpY = y - 1; tmpX < 8 && tmpY >= 0; tmpX++, tmpY--) {
                    testCanStep(possibleSteps, board, figure, tmpX, tmpY);
                    if(board[tmpX][tmpY] != null) break;
                }
                for(int tmpX = x - 1, tmpY = y + 1; tmpX >= 0 && tmpY < 8; tmpX--, tmpY++) {
                    testCanStep(possibleSteps, board, figure, tmpX, tmpY);
                    if(board[tmpX][tmpY] != null) break;
                }
                for(int tmpX = x - 1, tmpY = y - 1; tmpX >= 0 && tmpY >= 0; tmpX--, tmpY--) {
                    testCanStep(possibleSteps, board, figure, tmpX, tmpY);
                    if(board[tmpX][tmpY] != null) break;
                }
                break;
            case BISHOP:
                for(int tmpX = x + 1, tmpY = y + 1; tmpX < 8 && tmpY < 8; tmpX++, tmpY++) {
                    testCanStep(possibleSteps, board, figure, tmpX, tmpY);
                    if(board[tmpX][tmpY] != null) break;
                }
                for(int tmpX = x + 1, tmpY = y - 1; tmpX < 8 && tmpY >= 0; tmpX++, tmpY--) {
                    testCanStep(possibleSteps, board, figure, tmpX, tmpY);
                    if(board[tmpX][tmpY] != null) break;
                }
                for(int tmpX = x - 1, tmpY = y + 1; tmpX >= 0 && tmpY < 8; tmpX--, tmpY++) {
                    testCanStep(possibleSteps, board, figure, tmpX, tmpY);
                    if(board[tmpX][tmpY] != null) break;
                }
                for(int tmpX = x - 1, tmpY = y - 1; tmpX >= 0 && tmpY >= 0; tmpX--, tmpY--) {
                    testCanStep(possibleSteps, board, figure, tmpX, tmpY);
                    if(board[tmpX][tmpY] != null) break;
                }
                break;
            case KNIGHT:
                testCanStep(possibleSteps, board, figure, x + 2, y + 1);
                testCanStep(possibleSteps, board, figure, x + 2, y - 1);
                testCanStep(possibleSteps, board, figure, x - 2, y + 1);
                testCanStep(possibleSteps, board, figure, x - 2, y - 1);
                testCanStep(possibleSteps, board, figure, x + 1, y + 2);
                testCanStep(possibleSteps, board, figure, x - 1, y + 2);
                testCanStep(possibleSteps, board, figure, x + 1, y - 2);
                testCanStep(possibleSteps, board, figure, x - 1, y - 2);
                break;
            case ROOK:
                for(int tmpX = x + 1, tmpY = y; tmpX < 8; tmpX++) {
                    testCanStep(possibleSteps, board, figure, tmpX, tmpY);
                    if(board[tmpX][tmpY] != null) break;
                }
                for(int tmpX = x - 1, tmpY = y; tmpX >= 0; tmpX--) {
                    testCanStep(possibleSteps, board, figure, tmpX, tmpY);
                    if(board[tmpX][tmpY] != null) break;
                }
                for(int tmpX = x, tmpY = y + 1; tmpY < 8; tmpY++) {
                    testCanStep(possibleSteps, board, figure, tmpX, tmpY);
                    if(board[tmpX][tmpY] != null) break;
                }
                for(int tmpX = x, tmpY = y - 1; tmpY >= 0; tmpY--) {
                    testCanStep(possibleSteps, board, figure, tmpX, tmpY);
                    if(board[tmpX][tmpY] != null) break;
                }
                break;
            case PAWN:
                int direction = 1;
                if(figure.getColor() == Color.WHITE) {
                    if(y == 1 && board[x][y + 1] == null) {
                        testCanStep(possibleSteps, board, figure, x, y + 2, false, false);
                    }
                } else {
                    direction = -1;
                    if(y == 6 && board[x][y - 1] == null) {
                        testCanStep(possibleSteps, board, figure, x, y - 2, false, false);
                    }
                }
                testCanStep(possibleSteps, board, figure, x, y + direction, false, false);
                testCanStep(possibleSteps, board, figure, x + 1, y + direction, true, true);
                testCanStep(possibleSteps, board, figure, x - 1, y + direction, true, true);
                break;
            default:
                break;
        }

        return possibleSteps;
    }

    public void testCanStep(List<Pair<Integer, Integer>> possibleSteps, Figure[][] board, Figure figure, int x, int y) {
        testCanStep(possibleSteps, board, figure, x, y, true, false);
    }

    public void testCanStep(List<Pair<Integer, Integer>> possibleSteps, Figure[][] board, Figure figure, int x, int y, boolean canTake, boolean mustTake) {
        if(canStep(board, figure, x, y, canTake, mustTake)) {
            possibleSteps.add(Pair.of(x, y));
        }
    }

    public boolean canStep(Figure[][] board, Figure figure, int x, int y, boolean canTake, boolean mustTake) {
        if(x < 0 || x >= 8 || y < 0 || y >= 8) {
            return false;
        }
        Figure to = board[x][y];
        if((to == null && !mustTake) || (to != null && canTake && figure.getColor() != to.getColor())) {
            return true;
        }
        return false;
    }
}
