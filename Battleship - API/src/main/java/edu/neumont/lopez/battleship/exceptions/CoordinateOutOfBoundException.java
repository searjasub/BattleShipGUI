package edu.neumont.lopez.battleship.exceptions;

public class CoordinateOutOfBoundException extends RuntimeException{
    private int index;

    public CoordinateOutOfBoundException() {
    }

    public CoordinateOutOfBoundException(int index) {
        super("Index " + index + " out of bounds");
        this.setIndex(index);
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
