package com.company.model;

import com.company.enums.PieceEnum;

public class Piece {
    private String pieceText;
    private PieceEnum pieceEnum;
    private boolean isWhite; // true is for white & false means black pieces

    public String getPieceText() {
        return pieceText;
    }

    public void setPieceText(String pieceText) {
        this.pieceText = pieceText;
    }

    public Piece(PieceEnum pieceEnum, boolean isWhite) {
        this.pieceEnum = pieceEnum;
        this.isWhite = isWhite;
        this.pieceText = getPieceTextFromPiece();
    }

    public PieceEnum getPieceEnum() {
        return pieceEnum;
    }

    public void setPieceEnum(PieceEnum pieceEnum) {
        this.pieceText = getPieceTextFromPiece();
        this.pieceEnum = pieceEnum;
    }

    public boolean isWhite() {
        return isWhite;
    }

    public void setWhite(boolean white) {
        isWhite = white;
        this.pieceText = getPieceTextFromPiece();
    }


    private String getPieceTextFromPiece() {
        String text = "";

        switch (pieceEnum) {
            case ROOK:
                text = (isWhite) ? "R" : "r";
                break;
            case NIGHT:
                text = (isWhite) ? "N" : "n";
                break;
            case BISHOP:
                text = (isWhite) ? "B" : "b";
                break;
            case KING:
                text = (isWhite) ? "K" : "k";
                break;
            case QUEEN:
                text = (isWhite) ? "Q" : "q";
                break;
            case PAWN:
                text = (isWhite) ? "P" : "p";
                break;
        }
        return text;
    }



}
