package com.company;

import com.company.enums.GameStatus;
import com.company.enums.PieceEnum;
import com.company.model.Location;
import com.company.model.Move;
import com.company.model.Piece;
import com.company.utils.Utils;
import com.whitehatgaming.UserInputFile;

import java.io.IOException;

import static com.company.enums.GameStatus.NOT_STARTED;
import static com.company.enums.GameStatus.PLAYING;


public class Game {
    private UserInputFile userInputFile;
    private Piece[][] board;
    private GameStatus gameStatus;
    private boolean isGameOver;
    private boolean currentPlayer; // true for  white, false for black
    private Location whiteKingLocation;
    private Location blackKingLocation;

    public Game(UserInputFile userInputFile) {
        this.userInputFile = userInputFile;
        this.board = Utils.setUpBoard();
        this.gameStatus = NOT_STARTED;
        this.currentPlayer = true;
        this.whiteKingLocation = new Location(4, 7);
        this.blackKingLocation = new Location(4, 0);
    }

    public void nextGameMove() {
        if (gameStatus == NOT_STARTED) {
            gameStatus = PLAYING;
        }

        try {
            int[] nextMove = this.userInputFile.nextMove();
            if (nextMove == null) {
                this.isGameOver = true;
            } else {
                Move move = new Move(nextMove);

                boolean isValid = Utils.validateMove(board, move, currentPlayer);

                if (!isValid) {
                    // end game
                    isGameOver= true;
                    gameStatus = GameStatus.STOPPED_BY_ILLEGAL_MOVE;
                    return;
                }

                commitMove(move);
                boolean isCheck = isCheck();

                if (isCheck) {
                    // end game
                    isGameOver=true;
                    gameStatus = GameStatus.CHECK;
                    return;
                }
                currentPlayer = !currentPlayer;
            }
        } catch (IOException e) {
        }
    }

    private boolean isCheck() {
        return Utils.isCheckForPlayer(board, whiteKingLocation) ||
                Utils.isCheckForPlayer(board, blackKingLocation);
    }


    private void commitMove(Move move) {
        int fromX = move.getFromLocation().getX();
        int fromY = move.getFromLocation().getY();
        int toX = move.getToLocation().getX();
        int toY = move.getToLocation().getY();

        Piece piece = board[fromX][fromY];
        if (piece != null && piece.getPieceEnum() == PieceEnum.KING) {
            Location newLocation = new Location(toX, toY);
            if (piece.isWhite()) {
                whiteKingLocation = newLocation;
            } else {
                blackKingLocation = newLocation;
            }
        }

        board[toX][toY] = board[fromX][fromY];
        board[fromX][fromY] = null;
    }


    public boolean isGameFinished() {
        return isGameOver;
    }


    public Piece[][] getBoard() {
        return board;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }
}
