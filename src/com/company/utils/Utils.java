package com.company.utils;

import com.company.enums.PieceEnum;
import com.company.model.Move;
import com.company.model.Piece;

import static com.company.enums.PieceEnum.*;

public class Utils {

    public static boolean isInBounds(Move move) {
        return (move.getMoveFromX() >= 0 && move.getMoveFromX() < 8 && move.getMoveFromY() >= 0 && move.getMoveFromY() < 8
                && move.getMoveToX() >= 0 && move.getMoveToX() < 8 && move.getMoveToY() >= 0 && move.getMoveToY() < 8);
    }

    public static Piece[][] setUpBoard() {
        Piece[][] board = new Piece[8][8];
        setUpWhites(board);
        setUpBlacks(board);
        return board;
    }

    private static void setUpWhites(Piece[][] board) {
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

    private static void setUpBlacks(Piece[][] board) {
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


    public static boolean isForcedByPawn(Piece[][] board, int forcedByX, int forcedByY, int forcedX, int forcedY) {
        if (!isInBounds(new Move(forcedByX, forcedByY, forcedX, forcedY))) {
            return false;
        }
        Piece pieceForcedBy = board[forcedByX][forcedByY];
        Piece pieceForced = board[forcedX][forcedY];
        if (pieceForcedBy == null || pieceForcedBy.getPieceEnum() != PieceEnum.PAWN
                || pieceForced == null || pieceForced.isWhite() == pieceForcedBy.isWhite()) {
            return false;
        }

        if (pieceForcedBy.isWhite()) {
            return (forcedByX - 1 == forcedX && forcedByY - 1 == forcedY) ||
                    (forcedByX + 1 == forcedX && forcedByY - 1 == forcedY);
        } else {
            return (forcedByX - 1 == forcedX && forcedByY + 1 == forcedY) ||
                    (forcedByX + 1 == forcedX && forcedByY + 1 == forcedY);
        }
    }

    public static boolean isForcedByNight(Piece[][] board, int fromX, int fromY, int toX, int toY) {
        if (!isInBounds(new Move(fromX, fromY, toX, toY))) {
            return false;
        }
        Piece pieceForcedBy = board[fromX][fromY];
        Piece pieceForced = board[toX][toY];
        if (pieceForcedBy == null || pieceForcedBy.getPieceEnum() != PieceEnum.NIGHT
                || pieceForced == null || pieceForced.isWhite() == pieceForcedBy.isWhite()) {
            return false;
        }
        return fromX + 1 == toX && fromY - 2 == toY ||
                fromX + 2 == toX && fromY - 1 == toY ||
                fromX + 2 == toX && fromY + 1 == toY ||
                fromX + 1 == toX && fromY + 2 == toY ||
                fromX - 1 == toX && fromY + 2 == toY ||
                fromX - 2 == toX && fromY + 1 == toY ||
                fromX - 2 == toX && fromY - 1 == toY ||
                fromX - 1 == toX && fromY - 2 == toY;
    }

    private static boolean hasObstaclesRookForce(Piece[][] board, int fromX, int fromY, int toX, int toY) {
        if (fromX == toX || fromY == toY) { // on vertical or horizontal line
            // checks fro obstacle between pieces
            if (toX < fromX) {
                for (int i = toX + 1; i < fromX; i++) {
                    if (board[i][toY] != null) {
                        return false;
                    }
                }
            } else if (fromX < toX) {
                for (int i = fromX + 1; i < toX; i++) {
                    if (board[i][fromY] != null) {
                        return false;
                    }
                }
            } else if (toY < fromY) {
                for (int i = fromY - 1; i > toY; i--) {
                    if (board[fromX][i] != null) {
                        return false;
                    }
                }
            } else if (fromY < toY) {
                for (int i = fromY + 1; i < toY; i++) {
                    if (board[fromX][i] != null) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;

    }

    public static boolean isForcedByRook(Piece[][] board, int fromX, int fromY, int toX, int toY) {
        if (!isInBounds(new Move(fromX, fromY, toX, toY))) {
            return false;
        }

        Piece pieceForcedBy = board[fromX][fromY];
        Piece pieceForced = board[toX][toY];
        if (pieceForcedBy == null || pieceForcedBy.getPieceEnum() != PieceEnum.ROOK
                || pieceForced == null || pieceForced.isWhite() == pieceForcedBy.isWhite()) {
            return false;
        }
        return hasObstaclesRookForce(board, fromX, fromY, toX, toY);
    }


    public static boolean isForcedByQueen(Piece[][] board, int fromX, int fromY, int toX, int toY) {
        if (!isInBounds(new Move(fromX, fromY, toX, toY))) {
            return false;
        }
        Piece pieceForcedBy = board[fromX][fromY];
        Piece pieceForced = board[toX][toY];
        if (pieceForcedBy == null || pieceForcedBy.getPieceEnum() != PieceEnum.QUEEN
                || pieceForced == null || pieceForced.isWhite() == pieceForcedBy.isWhite()) {
            return false;
        }
        return hasObstaclesBishopForce(board, fromX, fromY, toX, toY) || hasObstaclesRookForce(board, fromX, fromY, toX, toY);
    }

    public static boolean isForcedByKing(Piece[][] board, int fromX, int fromY, int toX, int toY) {
        if (!isInBounds(new Move(fromX, fromY, toX, toY))) {
            return false;
        }
        Piece pieceForcedBy = board[fromX][fromY];
        Piece pieceForced = board[toX][toY];
        if (pieceForcedBy == null || pieceForcedBy.getPieceEnum() != PieceEnum.KING
                || pieceForced == null || pieceForced.isWhite() == pieceForcedBy.isWhite()
                || pieceForced.getPieceEnum() == PieceEnum.KING) {
            return false;
        }
        return fromX - 1 == toX && fromY - 1 == toY ||
                fromX == toX && fromY - 1 == toY ||
                fromX + 1 == toX && fromY - 1 == toY ||
                fromX + 1 == toX && fromY == toY ||
                fromX + 1 == toX && fromY + 1 == toY ||
                fromX == toX && fromY + 1 == toY ||
                fromX - 1 == toX && fromY + 1 == toY ||
                fromX - 1 == toX && fromY == toY ||
                fromX - 1 == toX && fromY - 1 == toY;
    }


    public static boolean isForcedByBishop(Piece[][] board, int fromX, int fromY, int toX, int toY) {
        if (!isInBounds(new Move(fromX, fromY, toX, toY))) {
            return false;
        }
        Piece pieceForcedBy = board[fromX][fromY];
        Piece pieceForced = board[toX][toY];
        if (pieceForcedBy == null || pieceForcedBy.getPieceEnum() != PieceEnum.BISHOP
                || pieceForced == null || pieceForced.isWhite() == pieceForcedBy.isWhite()) {
            return false;
        }

        return hasObstaclesBishopForce(board, fromX, fromY, toX, toY);
    }

    private static boolean hasObstaclesBishopForce(Piece[][] board, int fromX, int fromY, int toX, int toY) {
        if (Math.abs(toX - fromX) == Math.abs(toY - fromY)) { // they are on the same diagonal
            if (toX < fromX && toY < fromY) { // left top diagonal
                for (int i = toX + 1, j = toY + 1; i < fromX && j < fromY; i++, j++) {
                    if (board[i][j] != null) { // has 'obstacle' on the diagonal, so we do not have forcing
                        return false;
                    }
                }
            } else if (toX > fromX && toY > fromY) { // right bottom diagonal
                for (int i = fromX + 1, j = fromY + 1; i < toX && j < toY; i++, j++) {
                    if (board[i][j] != null) {
                        return false;
                    }
                }
            } else if (fromX < toX && fromY > toY) { // top right diagonal
                for (int i = fromX + 1, j = fromY - 1; i < toX && j > toY; i++, j--) {
                    if (board[i][j] != null) {
                        return false;
                    }
                }
            } else if (fromX > toX && toY > fromY) { // left bottom diagonal
                for (int i = toX + 1, j = toY + 1; i < fromX && j > fromY; i++, j--) {
                    if (board[i][j] != null) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }


    public static boolean validateMove(Piece[][] board, Move move, boolean nextPlayerTurn) {
        if (!isInBounds(move)) {
            return false; // board out of bounds
        }

        Piece currentPiece = board[move.getMoveFromX()][move.getMoveFromY()];
        if (currentPiece == null || currentPiece.isWhite() != nextPlayerTurn) {
            return false; // [No Piece On This Cell] or [it is not the right players' turn]
        }
        Piece destination = board[move.getMoveToX()][move.getMoveToY()];
        if (destination != null) { // something is placed on this place
            if (destination.isWhite() == currentPiece.isWhite()) { // on the destination cell is placed the same colored piece
                return false;
            }
        }

        PieceEnum piece = currentPiece.getPieceEnum();
        boolean isOk = true;

        // TODO just check if the move of direction is correct regarding current board state and regarding the piece ability
      /*  switch (piece) {
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
        }*/
        return isOk;
    }


}
