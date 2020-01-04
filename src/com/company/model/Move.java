package com.company.model;

public class Move {
    private Location fromLocation;
    private Location toLocation;

    public Move(int[] nextMove) {
        this.fromLocation = new Location(nextMove[0], nextMove[1]);
        this.toLocation = new Location(nextMove[2], nextMove[3]);
    }


    public Move(int moveFromX, int moveFromY, int moveToX, int moveToY) {
        this.fromLocation = new Location(moveFromX, moveFromY);
        this.toLocation = new Location(moveToX, moveToY);
    }

    public Location getFromLocation() {
        return fromLocation;
    }

    public void setFromLocation(Location fromLocation) {
        this.fromLocation = fromLocation;
    }

    public Location getToLocation() {
        return toLocation;
    }

    public void setToLocation(Location toLocation) {
        this.toLocation = toLocation;
    }
}
