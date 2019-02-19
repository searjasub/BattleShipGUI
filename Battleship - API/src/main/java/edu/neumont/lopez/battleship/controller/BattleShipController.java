package edu.neumont.lopez.battleship.controller;

import edu.neumont.lopez.battleship.enumeration.Ships;
import edu.neumont.lopez.battleship.enumeration.State;
import edu.neumont.lopez.battleship.exceptions.CoordinateOutOfBoundException;
import edu.neumont.lopez.battleship.exceptions.SquareAlreadyTakenException;
import edu.neumont.lopez.battleship.model.*;
import edu.neumont.lopez.battleship.view.BattleShipView;

import java.io.*;
import java.util.List;

public class BattleShipController {

    private BattleShipView view;
    private Board board = new Board();
    private Board attackingBoard = new Board();
    private Player player1 = this.board.getPlayerOne();
    private Player player2 = this.board.getPlayerTwo();
    private Player turn = player1;
    private Player notTurn = player2;

    private int userEx = -1;
    private boolean horizontal;

    public BattleShipController(BattleShipView ui) {
        this.view = ui;
        this.view.registerController(this);
    }


    public void initPlayers(List<String> playerNames) {
        this.board.getPlayerOne().setName(playerNames.get(0));
        this.board.getPlayerTwo().setName(playerNames.get(1));
    }

    public void initPlayer(Player player) {
        player.setBoard(board);
        player.setAttackingBoard(attackingBoard);
    }

    public void run() {
        this.view.init();
        this.board.resetBoard();
        this.view.updateTurnDisplay(this.board.getTurn());
        this.view.updateBoardDisplay(this.board);

    }

    public void switchTurn() {
        turn = (turn == player1 ? player2 : player1);
        notTurn = (notTurn == player2 ? player1 : player2);
    }

    /*public void takeTurn() {
        System.out.println("Ok " + turn.getName() + " , let's attack " + notTurn.getName() + "'s board.\nHere is an empty board where we will be tracking your opponent's board");
        boolean repeatedAttacked;
        do {
            turn.getAttackingBoard().printBoard();
            Coordinate coordinatesOfAttack;
            coordinatesOfAttack = view.getCoordinate();
            repeatedAttacked = updateBothBoards(coordinatesOfAttack, notTurn.getBoard().getSquares(), turn.getAttackingBoard().getSquares());
        } while (!repeatedAttacked);
        do {
            System.out.println("\n\nAfter that moved here is what you got.\n\nYour attacking board");
            turn.getAttackingBoard().printBoard();
            if (notTurn.getLives() == 0) {
                break;
            }
            System.out.println("\n------------------------------------------\n\nYour board");
            turn.getBoard().printBoard();
        } while (!view.done());
    }*/

    public boolean updateBothBoards(Coordinate c, BoardSquare[][] notTurnBoard, BoardSquare[][] turnAttackingBoard) {
        if (notTurnBoard[c.getRow() + userEx][c.getCol()].getState() == State.HIT) {
            System.out.println("\n\nYou already attacked that one before, try another one");
            return false;
        } else if (notTurnBoard[c.getRow() + userEx][c.getCol()].getState() == State.UNHIT) {
            notTurnBoard[c.getRow() + userEx][c.getCol()].setState(State.HIT);
            turnAttackingBoard[c.getRow() + userEx][c.getCol()].setState(State.HIT);
            notTurn.setLives(-1);
            System.out.println("\n\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\nGood job " + turn.getName() + "! You hit " + notTurn.getName() + "'s ship\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            return true;
        } else {
            notTurnBoard[c.getRow() + userEx][c.getCol()].setState(State.MISS);
            turnAttackingBoard[c.getRow() + userEx][c.getCol()].setState(State.MISS);
            System.out.println("\n\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\nYou missed, better luck next time!\n" +
                    "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            return true;
        }
    }

    private Coordinate coordinate;

    public void coordinateHelper(Coordinate coordinate){
        this.coordinate = coordinate;
    }

    public void placeShips(Coordinate coordinate, int shipSize, boolean posistion) {
        Coordinate whereInBoard;
        //for (Ship ship : turn.getBoard().getShips()) {
            //System.out.println("\nLet's place the " + ship.getName() + "\nRemember that the size is " + ship.getSize());
            boolean isValid = false;
            while (!isValid) {
                //view.showShipInfo(ship);
                whereInBoard = coordinate;
                horizontal = posistion;

                if (shipSize == 0){

                }


                isValid = checkInsideBoard(ship, whereInBoard);
                if (checkForOverlapping(whereInBoard)) {
                    throw new SquareAlreadyTakenException(whereInBoard);
                } else {
                    updateBoardDirection(ship, whereInBoard);
                }
            }
            //turn.getBoard().printBoard();
       // }
        //switchTurn();
    }

    public void updateBoardDirection(Ship s, Coordinate c) {
        if (horizontal) {
            if (s.getSize() == Ships.CARRIER.getSize()) {
                for (int i = Ships.CARRIER.getSize() - 1; i >= 0; i--) {
                    Coordinate newToCheck = new Coordinate(c.getRow(), c.getCol() + i);
                    if (!checkInsideBoard(s, newToCheck)) {
                        throw new SquareAlreadyTakenException(newToCheck);
                    }
                    //turn.getBoard().getSquares()[c.getRow() + userEx][c.getCol() + i].setState(State.UNHIT);
                    onSquareSelected(c);
                }
            } else if (s.getSize() == Ships.BATTLESHIP.getSize()) {
                for (int i = Ships.BATTLESHIP.getSize() - 1; i >= 0; i--) {
                    Coordinate newToCheck = new Coordinate(c.getRow(), c.getCol() + i);
                    if (!checkInsideBoard(s, newToCheck)) {
                        throw new SquareAlreadyTakenException(newToCheck);
                    }
                    //turn.getBoard().getSquares()[c.getRow() + userEx][c.getCol() + i].setState(State.UNHIT);
                    onSquareSelected(c);
                }
            } else if (s.getSize() == Ships.CRUISER.getSize()) {
                for (int i = Ships.CRUISER.getSize() - 1; i >= 0; i--) {
                    Coordinate newToCheck = new Coordinate(c.getRow(), c.getCol() + i);
                    if (!checkInsideBoard(s, newToCheck)) {
                        throw new SquareAlreadyTakenException(newToCheck);
                    }
                    //turn.getBoard().getSquares()[c.getRow() + userEx][c.getCol() + i].setState(State.UNHIT);
                    onSquareSelected(c);
                }
            } else if (s.getSize() == Ships.SUBMARINE.getSize()) {
                for (int i = Ships.SUBMARINE.getSize() - 1; i >= 0; i--) {
                    Coordinate newToCheck = new Coordinate(c.getRow(), c.getCol() + i);
                    if (!checkInsideBoard(s, newToCheck)) {
                        throw new SquareAlreadyTakenException(newToCheck);
                    }
                    //turn.getBoard().getSquares()[c.getRow() + userEx][c.getCol() + i].setState(State.UNHIT);
                    onSquareSelected(c);
                }
            } else if (s.getSize() == Ships.DESTROYER.getSize()) {
                for (int i = Ships.DESTROYER.getSize() - 1; i >= 0; i--) {
                    Coordinate newToCheck = new Coordinate(c.getRow(), c.getCol() + i);
                    if (!checkInsideBoard(s, newToCheck)) {
                        throw new SquareAlreadyTakenException(newToCheck);
                    }
                    //turn.getBoard().getSquares()[c.getRow() + userEx][c.getCol() + i].setState(State.UNHIT);
                    onSquareSelected(c);
                }
            }
        } else {
            if (s.getSize() == Ships.CARRIER.getSize()) {
                for (int i = Ships.CARRIER.getSize() - 1; i >= 0; i--) {
                    Coordinate newToCheck = new Coordinate(c.getRow() + i - 1, c.getCol());
                    if (!checkInsideBoard(s, newToCheck)) {
                        throw new SquareAlreadyTakenException(newToCheck);
                    }
                    //turn.getBoard().getSquares()[c.getRow() + i - 1][c.getCol()].setState(State.UNHIT);
                    onSquareSelected(c);
                }
            } else if (s.getSize() == Ships.BATTLESHIP.getSize()) {
                for (int i = Ships.BATTLESHIP.getSize() - 1; i >= 0; i--) {
                    Coordinate newToCheck = new Coordinate(c.getRow() + i - 1, c.getCol());
                    if (!checkInsideBoard(s, newToCheck)) {
                        throw new SquareAlreadyTakenException(newToCheck);
                    }
                    //turn.getBoard().getSquares()[c.getRow() + i - 1][c.getCol()].setState(State.UNHIT);
                    onSquareSelected(c);
                }
            } else if (s.getSize() == Ships.CRUISER.getSize()) {
                for (int i = Ships.CRUISER.getSize() - 1; i >= 0; i--) {
                    Coordinate newToCheck = new Coordinate(c.getRow() + i - 1, c.getCol());
                    if (!checkInsideBoard(s, newToCheck)) {
                        throw new SquareAlreadyTakenException(newToCheck);
                    }
                    //turn.getBoard().getSquares()[c.getRow() + i - 1][c.getCol()].setState(State.UNHIT);
                    onSquareSelected(c);
                }
            } else if (s.getSize() == Ships.SUBMARINE.getSize()) {
                for (int i = Ships.SUBMARINE.getSize() - 1; i >= 0; i--) {
                    Coordinate newToCheck = new Coordinate(c.getRow() + 1 - 1, c.getCol());
                    if (!checkInsideBoard(s, newToCheck)) {
                        throw new SquareAlreadyTakenException(newToCheck);
                    }
                    //turn.getBoard().getSquares()[c.getRow() + i - 1][c.getCol()].setState(State.UNHIT);
                    onSquareSelected(c);
                }
            } else if (s.getSize() == Ships.DESTROYER.getSize()) {
                for (int i = Ships.DESTROYER.getSize() - 1; i >= 0; i--) {
                    Coordinate newToCheck = new Coordinate(c.getRow() + i - 1, c.getCol());
                    if (!checkInsideBoard(s, newToCheck)) {
                        throw new SquareAlreadyTakenException(newToCheck);
                    }
                    //turn.getBoard().getSquares()[c.getRow() + i - 1][c.getCol()].setState(State.UNHIT);
                    onSquareSelected(c);
                }
            }

        }
    }

    public boolean checkForOverlapping(Coordinate coordinate) {
        if (turn.getBoard().getSquares()[coordinate.getRow() - 1][coordinate.getCol()].getState() == State.EMPTY) {
            return false;
        }
        return true;
    }

    public boolean checkInsideBoard(Ship s, Coordinate c) {

        if (horizontal) {
            if ((c.getCol() <= 0 || c.getCol() > 9)
                    && (Board.BOARD_WIDTH - c.getCol() < s.getSize())) {
                System.out.println("\nSorry that is not a valid column, the ship will be sticking out.\nTry a new coordinate\n");
                return false;
            } else {
                return true;
            }
        } else {
            if ((c.getRow() <= 0 || c.getRow() >= 10)
                    && (Board.BOARD_HEIGHT - c.getRow() < s.getSize())) {
                System.out.println("\nSorry that is not a valid row, the ship will be sticking out.\nTry a new coordinate\n");
                return false;
            } else {
                return true;
            }
        }
    }


    public void save(File file) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(this.board);
        }
    }

    public void load(File file) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            this.board = (Board) in.readObject();
        }
        this.view.updateBoardDisplay(this.board);
        this.view.updateTurnDisplay(this.getTurn());
    }

    public Player getTurn() {
        return this.turn;
    }

    public Player getNotTurn() {
        return notTurn;
    }

    public void onResetRequested() {
        this.board.resetBoard();
        this.view.updateBoardDisplay(this.board);
        this.view.updateBoardDisplay(this.attackingBoard);
        this.view.updateTurnDisplay(this.getTurn());
    }

    public void onSquareSelected(Coordinate coordinate) {
        try {
            this.board.takeSquare(State.UNHIT, coordinate);
            this.view.updateBoardDisplay(this.board);
        } catch (CoordinateOutOfBoundException ex) {
            this.view.showSquareOutOfBounds(coordinate);
        } catch (SquareAlreadyTakenException ex) {
            this.view.showSquareAlreadyTaken(coordinate);
        }
        //this.completingTurn();


    }
}
