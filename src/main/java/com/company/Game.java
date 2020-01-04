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
import static com.company.utils.Utils.*;


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
                gameStatus = GameStatus.NO_MORE_MOVES;
                isGameOver = true;
            } else {
                Move move = new Move(nextMove);

                boolean isValid = validateMove(move, currentPlayer);

                if (!isValid) {
                    // end game
                    isGameOver = true;
                    gameStatus = GameStatus.STOPPED_BY_ILLEGAL_MOVE;
                    return;
                }

                commitMove(move);
                boolean isCheck = isCheck();

                if (isCheck) {
                    // end game
                    isGameOver = true;
                    gameStatus = GameStatus.CHECK;
                    return;
                }
                currentPlayer = !currentPlayer;
            }
        } catch (IOException ignored) {
        }
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

    private boolean isCheck() {
        return isCheckForPlayer(whiteKingLocation) ||
                isCheckForPlayer(blackKingLocation);
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

    private boolean validateMove(Move move, boolean currentPlayer) {
        if (!isInBounds(move)) {
            return false; //  out of bounds
        }

        Piece currentPiece = board[move.getFromLocation().getX()][move.getFromLocation().getY()];
        if (currentPiece == null || currentPiece.isWhite() != currentPlayer) {
            return false; // [No Piece On This Cell or [it is not the right players' turn]
        }
        Piece destination = board[move.getToLocation().getX()][move.getToLocation().getY()];
        if (destination != null) { // something is placed on this place
            if (destination.isWhite() == currentPiece.isWhite()) { // on the destination cell is placed the same colored piece
                return false;
            }
        }

        PieceEnum piece = currentPiece.getPieceEnum();
        boolean isValid = true;
        switch (piece) {
            case PAWN:
                isValid = isValidPawnMove(board, move);
                break;
            case NIGHT:
                isValid = isValidNightMove(board, move);
                break;
            case BISHOP:
                isValid = isValidBishopMove(board, move);
                break;
            case ROOK:
                isValid = isValidRookMove(board, move);
                break;
            case QUEEN:
                isValid = isValidQueenMove(board, move);
                break;
            case KING:
                isValid = isValidKingMove(board, move);
                break;
        }
        return isValid;
    }

    private boolean isCheckForPlayer(Location kingLocation) {
        int kingX = kingLocation.getX();
        int kingY = kingLocation.getY();

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {

                Piece currentPiece = board[i][j];

                if (currentPiece != null && currentPiece.isWhite() != board[kingX][kingY].isWhite()) {

                    if (isForced(board, new Move(i, j, kingX, kingY))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
