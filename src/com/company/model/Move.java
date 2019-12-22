package com.company.model;

public class Move {
    private int moveFromX;
    private int moveFromY;
    private int moveToX;
    private int moveToY;



    public Move(int[] nextMove) {
        this.moveFromX = nextMove[0];
        this.moveFromY = nextMove[1];
        this.moveToX = nextMove[2];
        this.moveToY = nextMove[3];
    }


    public Move(int moveFromX, int moveFromY, int moveToX, int moveToY) {
        this.moveFromX = moveFromX;
        this.moveFromY = moveFromY;
        this.moveToX = moveToX;
        this.moveToY = moveToY;
    }

    public int getMoveFromX() {
        return moveFromX;
    }

    public int getMoveFromY() {
        return moveFromY;
    }

    public int getMoveToX() {
        return moveToX;
    }

    public int getMoveToY() {
        return moveToY;
    }

    @Override
    public String toString() {
        return "From: " + getMoveFromX() + ":" + getMoveFromY() + " To: " + getMoveToX() + ":" + getMoveToY();
    }
}
