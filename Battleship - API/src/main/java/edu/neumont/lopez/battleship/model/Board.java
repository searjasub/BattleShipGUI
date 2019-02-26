package edu.neumont.lopez.battleship.model;

import edu.neumont.lopez.battleship.enumeration.Ships;
import edu.neumont.lopez.battleship.enumeration.State;
import edu.neumont.lopez.battleship.exceptions.SquareAlreadyTakenException;

import java.io.Serializable;

public class Board implements Serializable {

    public static final int BOARD_WIDTH = 11;
    public static final int BOARD_HEIGHT = 11;
    private static final long serialVersionUID = 1L;
    private Ship[] ships;
    private BoardSquare[][] squares = new BoardSquare[BOARD_WIDTH][BOARD_HEIGHT];

    public Board() {
        initBoard();
        resetBoard();
    }

    public void resetBoard() {
        for (int r = 0; r < BOARD_WIDTH; r++) {
            for (int c = 0; c < BOARD_HEIGHT; c++) {
                squares[r][c].setState(State.EMPTY);
            }
        }
    }

    public void initBoard() {
        for (int row = 0; row < Board.BOARD_WIDTH; row++) {
            for (int col = 0; col < Board.BOARD_HEIGHT; col++) {
                squares[row][col] = new BoardSquare();
            }
        }
        initShips();
    }

    public Ship[] getShips() {
        return ships;
    }

    private void initShips() {
        ships = new Ship[]{
                new Ship(Ships.CARRIER),
                new Ship(Ships.BATTLESHIP),
                new Ship(Ships.CRUISER),
                new Ship(Ships.SUBMARINE),
                new Ship(Ships.DESTROYER)};
    }

    public BoardSquare[][] getSquares() {
        return squares;
    }


    public State getLocation(int row, int col) {
        return squares[row][col].getState();
    }

    public void takeSquare(State s, Coordinate coordinate) {
        checkSquareTaken(coordinate);
        squares[coordinate.getRow()][coordinate.getCol()].setState(s);
    }

    public void checkSquareTaken(Coordinate coordinate) {
        if (isSquareTaken(coordinate)) {
            System.out.println("square " + coordinate + " already taken");
        }
    }

    public void setSquareState(State s, Coordinate coordinate){
        squares[coordinate.getRow()][coordinate.getCol()].setState(s);
    }

    public boolean isSquareTaken(Coordinate coordinate) {
        return squares[coordinate.getRow()][coordinate.getCol()].getState() != State.EMPTY;
    }

    public State checkStateOfSquare(Coordinate c){
        return squares[c.getRow()][c.getCol()].getState();
    }
}
