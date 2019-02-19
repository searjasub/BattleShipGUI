package edu.neumont.lopez.battleship.exceptions;

import edu.neumont.lopez.battleship.model.Coordinate;

public class SquareAlreadyTakenException extends RuntimeException {

    private Coordinate coordinate;

    public SquareAlreadyTakenException() {
    }

    public SquareAlreadyTakenException(Coordinate coordinate) {
        super("Coordinate " + coordinate + " already occupied");
        this.setCoordinate(coordinate);
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }
}
