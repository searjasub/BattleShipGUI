package edu.neumont.lopez.battleship.model;

import edu.neumont.lopez.battleship.exceptions.CoordinateOutOfBoundException;

import java.util.Objects;

public class Coordinate {

    private int row;
    private int col;

    public Coordinate(int row, int col) {
        this.setRow(row);
        this.setCol(col);
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        if (row < 0 || row > Board.BOARD_HEIGHT){
            System.out.println("invalid row " + row);
        }
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        if (col < 0 || col > Board.BOARD_WIDTH){
            System.out.println("invalid column " + col);
        }
        this.col = col;
    }

    @Override
    public String toString() {
        return "(" + row + "," + col + ")";
    }

    @Override
    public boolean equals(Object o) {
        Coordinate other = (Coordinate) o;
        return this.getRow() == other.getRow() && this.getCol() == other.getCol();
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}
