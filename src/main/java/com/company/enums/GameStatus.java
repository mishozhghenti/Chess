package com.company.enums;

public enum GameStatus {
    NOT_STARTED,
    PLAYING,
    NO_MORE_MOVES,
    CHECK,
    STOPPED_BY_ILLEGAL_MOVE;


    @Override
    public String toString() {
        String str = "Playing";
        switch (this) {
            case NOT_STARTED:
                str = "Not Started";
                break;
            case CHECK:
                str = "Check";
                break;
            case STOPPED_BY_ILLEGAL_MOVE:
                str = "Stopped by Illegal Move";
                break;
            case NO_MORE_MOVES:
                str="No More Moves";
                break;
        }
        return str;
    }
}
