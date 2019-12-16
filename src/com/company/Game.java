package com.company;

import com.company.enums.PieceEnum;
import com.company.model.Move;
import com.company.model.Piece;
import com.whitehatgaming.UserInputFile;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;

import java.io.IOException;

import static com.company.constant.Constants.HEIGHT;
import static com.company.constant.Constants.WIDTH;
import static com.company.enums.PieceEnum.*;

public class Game {
    private UserInputFile userInputFile;
    private boolean isGameFinished = false;
    private boolean whiteWon = false;

    private ObservableList<Node> nodes;
    private Piece[][] board; // for inner logic
    private Label[][] boardLabels; // saves labels for each pieces

    public Game(UserInputFile userInputFile, ObservableList<Node> nodes) {
        this.userInputFile = userInputFile;
        this.nodes = nodes;
        this.board = new Piece[8][8];
        this.boardLabels = new Label[8][8];
        setUpPieces();
    }

    public void nextGameMove() {
        try {
            int[] nextMove = this.userInputFile.nextMove();
            if (nextMove == null) {
                this.isGameFinished = true;
            } else {

                Move move = new Move(nextMove);

                try {
                    validate(board[move.getMoveFromX()][move.getMoveFromY()], move);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void validate(Piece currentPiece, Move move) throws Exception {
        if (currentPiece == null) {
            throw new Exception("No Piece On This Cell");
        }
        PieceEnum pieceEnum = currentPiece.getPieceEnum();
        Piece destination = board[move.getMoveToX()][move.getMoveToY()];
        if (destination != null) { // something is placed on this place
            if (destination.isWhite() == currentPiece.isWhite()) { // on the destination cell is placed the same colored piece
                throw new Exception("Illegal Move");
            }
        }

        boolean isOk = true;

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
        displayPieces();
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


    private void displayPieces() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece current = board[j][i];
                locateLabel((current == null) ? " " : current.getPieceText(), j, i);
                System.out.print(((current == null) ? " " : current.getPieceText()) + " ");
            }
            System.out.println("");
        }
    }

    private void locateLabel(String s, int i, int j) {
        Label currentLabel = new Label(s);
        currentLabel.setLayoutX(i * WIDTH / 8 + 24);
        currentLabel.setLayoutY(j * HEIGHT / 8 + 24);
        boardLabels[i][j] = currentLabel;
        this.nodes.add(currentLabel);
    }


}
