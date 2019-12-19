package com.company;

import com.company.enums.GameStatus;
import com.company.enums.PieceEnum;
import com.company.model.Move;
import com.company.model.Piece;
import com.whitehatgaming.UserInputFile;

import java.io.IOException;

import static com.company.enums.GameStatus.NOT_STARTED;
import static com.company.enums.GameStatus.PLAYING;
import static com.company.enums.PieceEnum.*;

public class Game {
    private GameStatus gameStatus;
    private UserInputFile userInputFile;
    private boolean isGameFinished = false;
    private boolean whiteWon = false;
    private Piece[][] board; // for inner logic

    public Game(UserInputFile userInputFile) {
        this.userInputFile = userInputFile;
        this.board = new Piece[8][8];
        this.gameStatus = NOT_STARTED;
        setUpPieces();
    }

    public void nextGameMove() {
        if (gameStatus == NOT_STARTED) {
            gameStatus = PLAYING;
        }

        try {
            int[] nextMove = this.userInputFile.nextMove();
            if (nextMove == null) {
                this.isGameFinished = true;
            } else {
                Move move = new Move(nextMove);

//                boolean isValid = validateMove(move);

                commitMove(move);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void commitMove(Move move) {
        board[move.getMoveToX()][move.getMoveToY()] = board[move.getMoveFromX()][move.getMoveFromY()];
        board[move.getMoveFromX()][move.getMoveFromY()] = null;
    }


    private boolean validateMove(Move move) {
        if (!isInBounds(move)) {
            return false; // board out of bounds
        }

        Piece currentPiece = board[move.getMoveFromX()][move.getMoveFromY()];
        if (currentPiece == null) {
            return false; // No Piece On This Cell
        }
        PieceEnum piece = currentPiece.getPieceEnum();
        Piece destination = board[move.getMoveToX()][move.getMoveToY()];
        if (destination != null) { // something is placed on this place
            if (destination.isWhite() == currentPiece.isWhite()) { // on the destination cell is placed the same colored piece
                return false;
            }
        }

        boolean isOk = true;

        // TODO just check if the move of direction is correct regarding current board state and regarding the piece ability
        switch (piece) {
            case ROOK:
                isOk = validateRook(currentPiece, move);
                break;
            case NIGHT:
                isOk = validateNight(currentPiece, move);
                break;
            case BISHOP:
                isOk = validateBishop(currentPiece, move);
                break;
            case KING:
                isOk = validateKing(currentPiece, move);
                break;
            case QUEEN:
                isOk = validateQueen(currentPiece, move);
                break;
            case PAWN:
                isOk = validatePawn(currentPiece, move);
                break;
        }
        return isOk;
    }


    private boolean validatePawn(Piece currentPiece, Move move) {
        if (move.getMoveFromX() != move.getMoveToX()) {
            return false; // not forward move
        }
        boolean isValid = true;
        if (currentPiece.isWhite()) { // white players
            if (move.getMoveFromY() == 6) { // if it's first move
                // its possible to make one or two step long move
                if (!(move.getMoveFromY() - 1 == move.getMoveToY() ||
                        move.getMoveFromY() - 2 == move.getMoveToY())) {
                    isValid = false;
                }
            } else {
                if (!(move.getMoveFromY() - 1 == move.getMoveToY())) {
                    isValid = false;
                }
            }
        } else { // black players
            if (move.getMoveFromY() == 1) {
                if (!(move.getMoveFromY() + 1 == move.getMoveToY() ||
                        move.getMoveFromY() + 2 == move.getMoveToY())) {
                    isValid = false;
                }
            } else {
                if (!(move.getMoveFromY() + 1 == move.getMoveToY())) {
                    isValid = false;
                }
            }
        }
        return isValid;
    }

    private boolean validateQueen(Piece currentPiece, Move move) {
        return false;
    }

    private boolean validateKing(Piece currentPiece, Move move) {
        return false;
    }

    private boolean validateBishop(Piece currentPiece, Move move) {
        return false;
    }

    private boolean validateNight(Piece currentPiece, Move move) {
        return false;
    }

    private boolean validateRook(Piece currentPiece, Move move) {
        return false;
    }

    private boolean isInBounds(Move move) {
        return (move.getMoveFromX() >= 0 && move.getMoveFromX() < 8 && move.getMoveFromY() >= 0 && move.getMoveFromY() < 8
                && move.getMoveToX() >= 0 && move.getMoveToX() < 8 && move.getMoveToY() >= 0 && move.getMoveToY() < 8);
    }


    public boolean isGameFinished() {
        return isGameFinished;
    }

    public boolean getWinner() {
        return this.whiteWon;
    }

    private void setUpPieces() {
        setUpWhites();
        setUpBlacks();
    }

    private void setUpWhites() {
        for (int i = 0; i < 8; i++) {
            board[i][6] = new Piece(PieceEnum.PAWN, true);
        }
        board[0][7] = new Piece(ROOK, true);
        board[7][7] = new Piece(ROOK, true);
        board[1][7] = new Piece(NIGHT, true);
        board[6][7] = new Piece(NIGHT, true);
        board[2][7] = new Piece(BISHOP, true);
        board[5][7] = new Piece(BISHOP, true);
        board[3][7] = new Piece(PieceEnum.QUEEN, true);
        board[4][7] = new Piece(PieceEnum.KING, true);
    }

    private void setUpBlacks() {
        for (int i = 0; i < 8; i++) {
            board[i][1] = new Piece(PieceEnum.PAWN, false);
        }
        board[0][0] = new Piece(ROOK, false);
        board[7][0] = new Piece(ROOK, false);
        board[1][0] = new Piece(NIGHT, false);
        board[6][0] = new Piece(NIGHT, false);
        board[2][0] = new Piece(BISHOP, false);
        board[5][0] = new Piece(BISHOP, false);
        board[3][0] = new Piece(PieceEnum.QUEEN, false);
        board[4][0] = new Piece(PieceEnum.KING, false);
    }


    public Piece[][] getBoard() {
        return board;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }
}
