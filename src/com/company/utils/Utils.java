package com.company.utils;

import com.company.Location;
import com.company.enums.PieceEnum;
import com.company.model.Move;
import com.company.model.Piece;

import java.util.HashSet;
import java.util.Set;

import static com.company.enums.PieceEnum.*;

public class Utils {


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

    public static boolean validateMove(Piece[][] board, Move move, boolean currentPlayer) {
        if (!isInBounds(move)) {
            return false; // board out of bounds
        }

        Piece currentPiece = board[move.getMoveFromX()][move.getMoveFromY()];
        if (currentPiece == null || currentPiece.isWhite() != currentPlayer) {
            return false; // [No Piece On This Cell] or [it is not the right players' turn]
        }
        Piece destination = board[move.getMoveToX()][move.getMoveToY()];
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

    private static boolean isValidKingMove(Piece[][] board, Move move) {
        int fromX = move.getMoveFromX();
        int fromY = move.getMoveFromY();
        int toX = move.getMoveToX();
        int toY = move.getMoveToY();

        return getValidLocationsForKing(board, new Location(fromX, fromY)).contains(new Location(toX, toY));
    }

    private static boolean isValidPawnMove(Piece[][] board, Move move) {
        int fromX = move.getMoveFromX();
        int fromY = move.getMoveFromY();
        int toX = move.getMoveToX();
        int toY = move.getMoveToY();

        return getValidLocationsForPawn(board, new Location(fromX, fromY)).contains(new Location(toX, toY));
    }


    private static boolean isValidQueenMove(Piece[][] board, Move move) {
        int fromX = move.getMoveFromX();
        int fromY = move.getMoveFromY();
        int toX = move.getMoveToX();
        int toY = move.getMoveToY();

        return getValidLocationsForRook(board, new Location(fromX, fromY)).contains(new Location(toX, toY)) ||
                getValidLocationsForBishop(board, new Location(fromX, fromY)).contains(new Location(toX, toY));
    }

    private static boolean isValidBishopMove(Piece[][] board, Move move) {
        int fromX = move.getMoveFromX();
        int fromY = move.getMoveFromY();
        int toX = move.getMoveToX();
        int toY = move.getMoveToY();

        return getValidLocationsForBishop(board, new Location(fromX, fromY)).contains(new Location(toX, toY));
    }

    private static boolean isValidNightMove(Piece[][] board, Move move) {
        int fromX = move.getMoveFromX();
        int fromY = move.getMoveFromY();
        int toX = move.getMoveToX();
        int toY = move.getMoveToY();

        return getValidLocationsForNight(board, new Location(fromX, fromY)).contains(new Location(toX, toY));
    }

    private static boolean isValidRookMove(Piece[][] board, Move move) {
        int fromX = move.getMoveFromX();
        int fromY = move.getMoveFromY();
        int toX = move.getMoveToX();
        int toY = move.getMoveToY();

        return getValidLocationsForRook(board, new Location(fromX, fromY)).contains(new Location(toX, toY));
    }

    private static Set<Location> getValidLocationsForKing(Piece[][] board, Location fromLocation) {
        Set<Location> possibleMoveLocation = new HashSet<>();

        int fromX = fromLocation.getX();
        int fromY = fromLocation.getY();

        Piece currentPiece = board[fromX][fromY];

        if (fromX - 1 >= 0 && fromY - 1 >= 0) {
            if (board[fromX - 1][fromY - 1] == null || board[fromX - 1][fromY - 1].isWhite() != currentPiece.isWhite()) {
                possibleMoveLocation.add(new Location(fromX - 1, fromY - 1));
            }
        }

        if (fromY - 1 >= 0) {
            if (board[fromX][fromY - 1] == null || board[fromX][fromY - 1].isWhite() != currentPiece.isWhite()) {
                possibleMoveLocation.add(new Location(fromX, fromY - 1));
            }
        }

        if (fromX + 1 <= 7 && fromY - 1 >= 0) {

            if (board[fromX + 1][fromY - 1] == null || board[fromX + 1][fromY - 1].isWhite() != currentPiece.isWhite()) {
                possibleMoveLocation.add(new Location(fromX + 1, fromY - 1));
            }

        }

        if (fromX + 1 <= 7) {
            if (board[fromX + 1][fromY] == null || board[fromX + 1][fromY].isWhite() != currentPiece.isWhite()) {
                possibleMoveLocation.add(new Location(fromX + 1, fromY));
            }
        }

        if (fromX + 1 <= 7 && fromY + 1 <= 7) {
            if (board[fromX + 1][fromY + 1] == null || board[fromX + 1][fromY + 1].isWhite() != currentPiece.isWhite()) {
                possibleMoveLocation.add(new Location(fromX + 1, fromY + 1));
            }
        }

        if (fromY + 1 <= 7) {
            if (board[fromX][fromY + 1] == null || board[fromX][fromY + 1].isWhite() != currentPiece.isWhite()) {
                possibleMoveLocation.add(new Location(fromX, fromY + 1));
            }
        }

        if (fromX - 1 >= 0 && fromY + 1 <= 7) {
            if (board[fromX - 1][fromY + 1] == null || board[fromX - 1][fromY - +1].isWhite() != currentPiece.isWhite()) {
                possibleMoveLocation.add(new Location(fromX - 1, fromY + 1));
            }
        }

        if (fromX - 1 >= 0) {
            if (board[fromX - 1][fromY] == null || board[fromX - 1][fromY].isWhite() != currentPiece.isWhite()) {
                possibleMoveLocation.add(new Location(fromX - 1, fromY));
            }
        }

        return possibleMoveLocation;
    }

    private static Set<Location> getValidLocationsForBishop(Piece[][] board, Location fromLocation) {
        Set<Location> possibleMoveLocation = new HashSet<>();

        int fromX = fromLocation.getX();
        int fromY = fromLocation.getY();

        Piece currentPiece = board[fromX][fromY];
        Piece opponentPiece;
        Location location;


        for (int i = fromX - 1, j = fromY - 1; i >= 0 && j >= 0; i--, j--) {
            opponentPiece = board[i][j];
            location = new Location(i, j);

            if (opponentPiece != null) {
                if (opponentPiece.isWhite() != currentPiece.isWhite()) {
                    possibleMoveLocation.add(location);
                }
                break;
            }
            possibleMoveLocation.add(location);
        }


        for (int i = fromX + 1, j = fromY + 1; i <= 7 && j <= 7; i++, j++) {
            opponentPiece = board[i][j];
            location = new Location(i, j);
            if (opponentPiece != null) {
                if (opponentPiece.isWhite() != currentPiece.isWhite()) {
                    possibleMoveLocation.add(location);
                }
                break;
            }
            possibleMoveLocation.add(location);
        }


        for (int i = fromX + 1, j = fromY - 1; i <= 7 && j >= 0; i++, j--) {
            opponentPiece = board[i][j];
            location = new Location(i, j);
            if (opponentPiece != null) {
                if (opponentPiece.isWhite() != currentPiece.isWhite()) {
                    possibleMoveLocation.add(location);
                }
                break;
            }
            possibleMoveLocation.add(location);
        }


        for (int i = fromX - 1, j = fromY + 1; i >= 0 && j <= 7; i--, j++) {
            opponentPiece = board[i][j];
            location = new Location(i, j);
            if (opponentPiece != null) {
                if (opponentPiece.isWhite() != currentPiece.isWhite()) {
                    possibleMoveLocation.add(location);
                }
                break;
            }
            possibleMoveLocation.add(location);
        }

        return possibleMoveLocation;
    }

    private static Set<Location> getValidLocationsForNight(Piece[][] board, Location fromLocation) {
        Set<Location> possibleMoveLocation = new HashSet<>();

        int fromX = fromLocation.getX();
        int fromY = fromLocation.getY();

        Piece currentPiece = board[fromX][fromY];
        Piece opponentPiece;


        if (fromX + 1 <= 7 && fromY - 2 >= 0) {
            opponentPiece = board[fromX + 1][fromY - 2];
            if (opponentPiece == null || opponentPiece.isWhite() != currentPiece.isWhite()) {
                possibleMoveLocation.add(new Location(fromX + 1, fromY - 2));
            }
        }


        if (fromX + 2 <= 7 && fromY - 1 >= 0) {
            opponentPiece = board[fromX + 2][fromY - 1];
            if (opponentPiece == null || opponentPiece.isWhite() != currentPiece.isWhite()) {
                possibleMoveLocation.add(new Location(fromX + 2, fromY - 1));
            }
        }
        if (fromX + 2 <= 7 && fromY + 1 <= 7) {
            opponentPiece = board[fromX + 2][fromY + 1];
            if (opponentPiece == null || opponentPiece.isWhite() != currentPiece.isWhite()) {
                possibleMoveLocation.add(new Location(fromX + 2, fromY + 1));
            }
        }
        if (fromX + 1 <= 7 && fromY + 2 <= 7) {
            opponentPiece = board[fromX + 1][fromY + 2];
            if (opponentPiece == null || opponentPiece.isWhite() != currentPiece.isWhite()) {
                possibleMoveLocation.add(new Location(fromX + 1, fromY + 2));
            }
        }

        if (fromX - 1 >= 0 && fromY + 2 <= 7) {
            opponentPiece = board[fromX - 1][fromY + 2];
            if (opponentPiece == null || opponentPiece.isWhite() != currentPiece.isWhite()) {
                possibleMoveLocation.add(new Location(fromX - 1, fromY + 2));
            }
        }
        if (fromX - 2 >= 0 && fromY + 1 <= 7) {
            opponentPiece = board[fromX - 2][fromY + 1];
            if (opponentPiece == null || opponentPiece.isWhite() != currentPiece.isWhite()) {
                possibleMoveLocation.add(new Location(fromX - 2, fromY + 1));
            }
        }
        if (fromX - 2 >= 0 && fromY - 1 >= 0) {
            opponentPiece = board[fromX - 2][fromY - 1];
            if (opponentPiece == null || opponentPiece.isWhite() != currentPiece.isWhite()) {
                possibleMoveLocation.add(new Location(fromX - 2, fromY - 1));
            }
        }
        if (fromX - 1 >= 0 && fromY - 2 >= 0) {
            opponentPiece = board[fromX - 1][fromY - 2];
            if (opponentPiece == null || opponentPiece.isWhite() != currentPiece.isWhite()) {
                possibleMoveLocation.add(new Location(fromX - 1, fromY - 2));
            }
        }

        return possibleMoveLocation;
    }

    private static Set<Location> getValidLocationsForRook(Piece[][] board, Location fromLocation) {
        Set<Location> possibleMoveLocation = new HashSet<>();

        int fromX = fromLocation.getX();
        int fromY = fromLocation.getY();

        Piece currentPiece = board[fromX][fromY];
        Piece piece;
        Location location;

        for (int i = fromX - 1; i >= 0; i--) {
            piece = board[i][fromY];
            location = new Location(i, fromY);
            if (piece != null) {
                if (piece.isWhite() != currentPiece.isWhite()) {
                    possibleMoveLocation.add(location);
                }
                break;
            }
            possibleMoveLocation.add(location);
        }


        for (int i = fromX + 1; i <= 7; i++) {
            piece = board[i][fromY];
            location = new Location(i, fromY);
            if (piece != null) {
                if (piece.isWhite() != currentPiece.isWhite()) {
                    possibleMoveLocation.add(location);
                }
                break;
            }
            possibleMoveLocation.add(location);
        }


        for (int i = fromY - 1; i >= 0; i--) {
            piece = board[fromX][i];
            location = new Location(fromX, i);

            if (piece != null) {
                if (piece.isWhite() != currentPiece.isWhite()) {
                    possibleMoveLocation.add(location);
                }
                break;
            }
            possibleMoveLocation.add(location);
        }

        for (int i = fromY + 1; i <= 7; i++) {
            piece = board[fromX][i];
            location = new Location(fromX, i);

            if (piece != null) {
                if (piece.isWhite() != currentPiece.isWhite()) {
                    possibleMoveLocation.add(location);
                }
                break;
            }
            possibleMoveLocation.add(location);
        }

        return possibleMoveLocation;
    }

    private static Set<Location> getValidLocationsForPawn(Piece[][] board, Location fromLocation) {
        Set<Location> possibleMoveLocation = new HashSet<>();

        int fromX = fromLocation.getX();
        int fromY = fromLocation.getY();

        Piece currentPiece = board[fromX][fromY];
        Piece opponentPiece;


        if (currentPiece.isWhite()) {
            if (fromY == 6) { // opportunity to change 2 steps forward on the board
                if (board[fromX][fromY - 1] == null && board[fromX][fromY - 2] == null) {
                    possibleMoveLocation.add(new Location(fromX, fromY - 2));
                }
            } else if (fromY > 0) {// opportunity to change 1 step forward on the board
                if (board[fromX][fromY - 1] == null) {
                    possibleMoveLocation.add(new Location(fromX, fromY - 1));
                }
            }

            if (fromX - 1 >= 0 && fromY - 1 >= 0) { // forcing
                opponentPiece = board[fromX - 1][fromY - 1];
                if (opponentPiece != null && opponentPiece.isWhite() != currentPiece.isWhite()) {
                    possibleMoveLocation.add(new Location(fromX - 1, fromY - 1));
                }
            }

            if (fromX + 1 <= 7 && fromY - 1 >= 0) {
                opponentPiece = board[fromX + 1][fromY - 1];
                if (opponentPiece != null && opponentPiece.isWhite() != currentPiece.isWhite()) {
                    possibleMoveLocation.add(new Location(fromX + 1, fromY - 1));
                }
            }
        } else {
            if (fromY == 1) {
                if (board[fromX][fromY + 1] == null && board[fromX][fromY + 2] == null) {
                    possibleMoveLocation.add(new Location(fromX, fromY + 2));
                }

            } else if (fromY > 0) {// opportunity to change 1 step forward on the board
                if (board[fromX][fromY + 1] == null) {
                    possibleMoveLocation.add(new Location(fromX, fromY + 1));
                }
            }

            if (fromX - 1 >= 0 && fromY + 1 <= 7) { // forcing
                opponentPiece = board[fromX - 1][fromY + 1];
                if (opponentPiece != null && opponentPiece.isWhite() != currentPiece.isWhite()) {
                    possibleMoveLocation.add(new Location(fromX - 1, fromY + 1));
                }
            }

            if (fromX + 1 <= 7 && fromY + 1 <= 7) {
                opponentPiece = board[fromX + 1][fromY + 1];
                if (opponentPiece != null && opponentPiece.isWhite() != currentPiece.isWhite()) {
                    possibleMoveLocation.add(new Location(fromX + 1, fromY + 1));
                }
            }

        }
        return possibleMoveLocation;
    }


    public static boolean isCheckForPlayer(Piece[][] board, boolean player, int kingX, int kingY) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                Piece currentPiece = board[i][j];
                if (currentPiece != null && currentPiece.isWhite() != player) {
                    if (isForced(board, new Move(i, j, kingX, kingY))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean isForced(Piece[][] board, Move move) {
        PieceEnum otherPlayerPiece = board[move.getMoveFromX()][move.getMoveFromY()].getPieceEnum();
        boolean isForced = false;
        switch (otherPlayerPiece) {
            case PAWN:
                isForced = isForcedByPawn(board, move);
                break;
            case ROOK:
                isForced = isForcedByRook(board, move);
                break;
            case NIGHT:
                isForced = isForcedByNight(board, move);
                break;
            case BISHOP:
                isForced = isForcedByBishop(board, move);
                break;
            case QUEEN:
                isForced = isForcedByQueen(board, move);
                break;
            case KING:
                isForced = isForcedByKing(board, move);
                break;
        }
        return isForced;
    }

    private static boolean isForcedByPawn(Piece[][] board, Move move) {
        int fromX = move.getMoveFromX();
        int fromY = move.getMoveFromY();
        int toX = move.getMoveToX();
        int toY = move.getMoveToY();

        Piece pieceForcedBy = board[fromX][fromY];
        Piece pieceForced = board[toX][toY];
        if (pieceForcedBy == null || pieceForcedBy.getPieceEnum() != PieceEnum.PAWN
                || pieceForced == null || pieceForced.isWhite() == pieceForcedBy.isWhite()) {
            return false;
        }

        if (pieceForcedBy.isWhite()) {
            return (fromX - 1 == toX && fromY - 1 == toY) ||
                    (fromX + 1 == toX && fromY - 1 == toY);
        } else {
            return (fromX - 1 == toX && fromY + 1 == toY) ||
                    (fromX + 1 == toX && fromY + 1 == toY);
        }
    }

    private static boolean isForcedByRook(Piece[][] board, Move move) {
        int fromX = move.getMoveFromX();
        int fromY = move.getMoveFromY();
        int toX = move.getMoveToX();
        int toY = move.getMoveToY();

        Piece pieceForcedBy = board[fromX][fromY];
        Piece pieceForced = board[toX][toY];
        if (pieceForcedBy == null || pieceForcedBy.getPieceEnum() != PieceEnum.ROOK
                || pieceForced == null || pieceForced.isWhite() == pieceForcedBy.isWhite()) {
            return false;
        }
        return isRookObstacleFree(board, move);
    }

    private static boolean isForcedByNight(Piece[][] board, Move move) {
        int fromX = move.getMoveFromX();
        int fromY = move.getMoveFromY();
        int toX = move.getMoveToX();
        int toY = move.getMoveToY();
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

    private static boolean isForcedByBishop(Piece[][] board, Move move) {
        int fromX = move.getMoveFromX();
        int fromY = move.getMoveFromY();
        int toX = move.getMoveToX();
        int toY = move.getMoveToY();

        Piece pieceForcedBy = board[fromX][fromY];
        Piece pieceForced = board[toX][toY];
        if (pieceForcedBy == null || pieceForcedBy.getPieceEnum() != PieceEnum.BISHOP
                || pieceForced == null || pieceForced.isWhite() == pieceForcedBy.isWhite()) {
            return false;
        }

        return isBishopObstacleFree(board, move);
    }

    private static boolean isForcedByQueen(Piece[][] board, Move move) {
        int fromX = move.getMoveFromX();
        int fromY = move.getMoveFromY();
        int toX = move.getMoveToX();
        int toY = move.getMoveToY();

        Piece pieceForcedBy = board[fromX][fromY];
        Piece pieceForced = board[toX][toY];
        if (pieceForcedBy == null || pieceForcedBy.getPieceEnum() != PieceEnum.QUEEN
                || pieceForced == null || pieceForced.isWhite() == pieceForcedBy.isWhite()) {
            return false;
        }
        return isBishopObstacleFree(board, move) || isRookObstacleFree(board, move);
    }

    private static boolean isForcedByKing(Piece[][] board, Move move) {
        if (!isInBounds(move)) {
            return false;
        }
        int fromX = move.getMoveFromX();
        int fromY = move.getMoveFromY();
        int toX = move.getMoveToX();
        int toY = move.getMoveToY();

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

    private static boolean isRookObstacleFree(Piece[][] board, Move move) {
        int fromX = move.getMoveFromX();
        int fromY = move.getMoveFromY();
        int toX = move.getMoveToX();
        int toY = move.getMoveToY();

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

    private static boolean isBishopObstacleFree(Piece[][] board, Move move) {
        int fromX = move.getMoveFromX();
        int fromY = move.getMoveFromY();
        int toX = move.getMoveToX();
        int toY = move.getMoveToY();

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

    private static boolean isInBounds(Move move) {
        return (move.getMoveFromX() >= 0 && move.getMoveFromX() < 8 && move.getMoveFromY() >= 0 && move.getMoveFromY() < 8
                && move.getMoveToX() >= 0 && move.getMoveToX() < 8 && move.getMoveToY() >= 0 && move.getMoveToY() < 8);
    }

}
