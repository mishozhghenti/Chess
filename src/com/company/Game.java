package com.company;

import com.company.enums.GameStatus;
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
    private boolean nextPlayerTurn; // true for  white, false for black

    public Game(UserInputFile userInputFile) {
        this.userInputFile = userInputFile;
        this.board = Utils.setUpBoard();
        this.gameStatus = NOT_STARTED;
        this.nextPlayerTurn = true;
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

                boolean isValid = Utils.validateMove(board, move, nextPlayerTurn);
                commitMove(move);

                nextPlayerTurn = !nextPlayerTurn;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void commitMove(Move move) {
        board[move.getMoveToX()][move.getMoveToY()] = board[move.getMoveFromX()][move.getMoveFromY()];
        board[move.getMoveFromX()][move.getMoveFromY()] = null;
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
