package com.bergdavi.onlab.gameservice.service.delegate.chess;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.bergdavi.onlab.gameservice.exception.InvalidStepException;
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
        ChessTurn lastTurn = gameState.getLastTurn();
        gameState.setLastTurn(gameTurn);

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
            throw new InvalidStepException();
        }

        if(playerColor != figure.getColor()) {
            throw new InvalidStepException();
        }

        if(getPossibleSteps(gameState.getBoard(), gameTurn.getFromX(), gameTurn.getFromY()).contains(Pair.of(gameTurn.getToX(), gameTurn.getToY()))) {
            Figure[][] oldBoard = copyBoard(gameState.getBoard());
            performStep(figure, gameTurn, gameState);
            if(figure.getType() == FigureType.PAWN) {
                if((figure.getColor() == Color.WHITE && gameTurn.getToY() == 7) || (figure.getColor() == Color.BLACK && gameTurn.getToY() == 0)) {
                    if(gameTurn.getPromote() != null) {
                        gameState.getBoard()[gameTurn.getToX()][gameTurn.getToY()] = new Figure(figure.getColor(), gameTurn.getPromote());
                    } else {
                        gameState.setBoard(oldBoard);
                        // TODO better exception handling
                        throw new InvalidStepException();        
                    }
                }                
            }
            if(isCheck(playerColor, gameState.getBoard())) {
                gameState.setBoard(oldBoard);
                // TODO better exception handling
                throw new InvalidStepException();
            }
            if(isCheck(Color.invert(playerColor), gameState.getBoard())) {
                gameState.setChecked(Color.invert(playerColor));
            } else {
                gameState.setChecked(null);
            }
        } else {
            Pair<Integer, Integer> enPassant = getEnPassant(gameState.getBoard(), lastTurn);
            if(enPassant != null && enPassant.equals(Pair.of(gameTurn.getToX(), gameTurn.getToY()))) {
                performStep(figure, gameTurn, gameState);
                gameState.getBoard()[lastTurn.getToX()][lastTurn.getToY()] = null;
                if(isCheck(playerColor, gameState.getBoard())) {
                    // TODO better exception handling
                    throw new InvalidStepException();
                }
                if(isCheck(Color.invert(playerColor), gameState.getBoard())) {
                    gameState.setChecked(Color.invert(playerColor));
                } else {
                    gameState.setChecked(null);
                }
            } else if(performCastle(gameState, gameTurn)) {
                if(isCheck(playerColor, gameState.getBoard())) {
                    // TODO better exception handling
                    throw new InvalidStepException();
                }
                if(isCheck(Color.invert(playerColor), gameState.getBoard())) {
                    gameState.setChecked(Color.invert(playerColor));
                } else {
                    gameState.setChecked(null);
                }
            } else {
                // TODO better exception handling
                throw new InvalidStepException();
            }
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
        if(gameState.getChecked() != null) {
            if(isMate(gameState.getChecked(), gameState.getBoard())) {
                winners = Optional.of(new ArrayList<>());
                winners.get().add(getIdxFromColor(Color.invert(gameState.getChecked())));
            }
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

    private Pair<Integer, Integer> getEnPassant(Figure[][] board, ChessTurn lastTurn) {
        if(lastTurn == null) {
            return null;
        }
        Figure lastFigure = board[lastTurn.getToX()][lastTurn.getToY()];
        if(lastFigure != null && lastFigure.getType() == FigureType.PAWN && Math.abs(lastTurn.getFromY() - lastTurn.getToY()) == 2) {
            return Pair.of(lastTurn.getToX(), (lastTurn.getToY() + lastTurn.getFromY())/2);
        }
        return null;
    }

    private boolean performCastle(ChessState gameState, ChessTurn gameTurn) {
        Figure[][] board = gameState.getBoard();
        Figure figureKing = board[gameTurn.getFromX()][gameTurn.getFromY()];
        Figure toFigureRook = board[gameTurn.getToX()][gameTurn.getToY()];
        if(figureKing.getType() != FigureType.KING || toFigureRook.getType() == null || toFigureRook.getType() != FigureType.ROOK) {
            return false;
        }
        if(gameTurn.getFromY() != gameTurn.getToY()) {
            return false;
        }
        if((figureKing.getColor() == Color.WHITE && gameState.getWhiteKingMoved()) || (figureKing.getColor() == Color.BLACK && gameState.getBlackKingMoved())){
            return false;
        }
        if((figureKing.getColor() == Color.WHITE && gameTurn.getToX() == 0 && gameState.getWhiteRook0Moved()) ||
            (figureKing.getColor() == Color.WHITE && gameTurn.getToX() == 7 && gameState.getWhiteRook7Moved()) ||
            (figureKing.getColor() == Color.BLACK && gameTurn.getToX() == 0 && gameState.getBlackRook0Moved()) ||
            (figureKing.getColor() == Color.BLACK && gameTurn.getToX() == 7 && gameState.getBlackRook7Moved())) {
            return false;
        }

        int kingToX = -1;
        int rookToX = -1;

        if(gameTurn.getToX() == 0) {
            kingToX = 2;
            rookToX = 3;
        } else if(gameTurn.getToX() == 7) {
            kingToX = 6;
            rookToX = 5;
        } else {
            return false;
        }

        int from = Math.min(gameTurn.getFromX(), gameTurn.getToX());
        int to = Math.max(gameTurn.getFromX(), gameTurn.getToX());
        for(int i = from + 1; i < to; i++) {
            if(board[i][gameTurn.getFromY()] != null){
                return false;
            }
        }

        from = Math.min(gameTurn.getFromX(), kingToX);
        to = Math.max(gameTurn.getFromX(), kingToX);
        for(int i = from; i <= to; i++) {            
            Figure[][] tmpBoard = copyBoard(board);
            tmpBoard[gameTurn.getFromX()][gameTurn.getFromY()] = null;
            tmpBoard[i][gameTurn.getToY()] = figureKing;
            if(isCheck(figureKing.getColor(), tmpBoard)) {
                return false;
            }            
        }

        board[gameTurn.getFromX()][gameTurn.getFromY()] = null;
        board[kingToX][gameTurn.getFromY()] = figureKing;
        board[gameTurn.getToX()][gameTurn.getToY()] = null;
        board[rookToX][gameTurn.getToY()] = toFigureRook;

        return true;
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
        if(figure.getColor() == Color.WHITE) {
            if(figure.getType() == FigureType.KING) {
                gameState.setWhiteKingMoved(true);
            } else if(figure.getType() == FigureType.ROOK) {
                if(gameTurn.getFromX() == 0 && gameTurn.getFromY() == 0) {
                    gameState.setWhiteRook0Moved(true);
                } else if(gameTurn.getFromX() == 7 && gameTurn.getFromY() == 0) {
                    gameState.setWhiteRook7Moved(true);
                }
            }
        } else {
            if(figure.getType() == FigureType.KING) {
                gameState.setBlackKingMoved(true);
            } else if(figure.getType() == FigureType.ROOK) {
                if(gameTurn.getFromX() == 0 && gameTurn.getFromY() == 7) {
                    gameState.setBlackRook0Moved(true);
                } else if(gameTurn.getFromX() == 7 && gameTurn.getFromY() == 7) {
                    gameState.setBlackRook7Moved(true);
                }
            }
        }
        gameState.getBoard()[gameTurn.getFromX()][gameTurn.getFromY()] = null;
        gameState.getBoard()[gameTurn.getToX()][gameTurn.getToY()] = figure;
    }

    public void performStep(Figure[][] board, int fromX, int fromY, int toX, int toY) {
        Figure figure = board[fromX][fromY];
        board[fromX][fromY] = null;
        board[toX][toY] = figure;
    }

    private Pair<Integer, Integer> findKing(Color color, Figure[][] board) {
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
        return Pair.of(kingX, kingY);
    }

    private boolean isCheck(Color color, Figure[][] board) {
        Pair<Integer, Integer> kingPos = findKing(color, board);
        return isCheck(color, board, kingPos.getFirst(), kingPos.getSecond());
    }

    private boolean isCheck(Color color, Figure[][] board, int kingX, int kingY) {        
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

    private boolean isMate(Color color, Figure[][] board) {
        Pair<Integer, Integer> kingPos = findKing(color, board);
        return isMate(color, board, kingPos.getFirst(), kingPos.getSecond());
    }

    private boolean isMate(Color color, Figure[][] board, int kingX, int kingY) {
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                Figure figure = board[i][j];
                if(figure == null || figure.getColor() != color) continue;
                for(Pair<Integer, Integer> step : getPossibleSteps(board, i, j)) {
                    Figure[][] tmpBoard = copyBoard(board);
                    performStep(tmpBoard, i, j, step.getFirst(), step.getSecond());
                    if(!isCheck(color, tmpBoard)) {
                        return false;
                    }
                }
                
            }
        }
        return true;
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
