package edu.neumont.lopez.battleship.controller;

import edu.neumont.lopez.battleship.enumeration.Ships;
import edu.neumont.lopez.battleship.enumeration.State;
import edu.neumont.lopez.battleship.model.*;
import edu.neumont.lopez.battleship.view.BattleShipView;

import java.io.*;
import java.util.List;

public class BattleShipController {

    private BattleShipView view;
    private Board board = new Board();
    private Board attackingBoard = new Board();
    //private Player player1 = this.board.getPlayerOne();
    //private Player player2 = this.board.getPlayerTwo();
    private Player turn = this.board.getPlayerOne();
    private Player notTurn = this.board.getPlayerTwo();

    private int userEx = -1;
    private boolean horizontal;
    private Coordinate coordinate;


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
        setupScreen();
    }

    public void setupScreen() {
        this.board.resetBoard();
        this.view.updateTurnDisplay(this.board.getTurn());
        this.view.updateBoardDisplay(this.board);
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

//    public void switchTurn() {
//        turn = (turn == player1 ? player2 : player1);
//        notTurn = (notTurn == player2 ? player1 : player2);
//    }

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

    public void coordinateHelper(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public boolean placeShips(Coordinate coordinate, int shipSize, boolean posistion) {
        Coordinate whereInBoard;

        whereInBoard = coordinate;
        horizontal = posistion;

        Ship ship;

        System.out.println("controller: " + coordinate);

        if (shipSize == 0) {
            ship = new Ship(Ships.CARRIER);
            return isValid(whereInBoard, ship);
        } else if (shipSize == 1) {
            ship = new Ship(Ships.BATTLESHIP);
            return isValid(whereInBoard, ship);
        } else if (shipSize == 2) {
            ship = new Ship(Ships.SUBMARINE);
            return isValid(whereInBoard, ship);
        } else if (shipSize == 3) {
            ship = new Ship(Ships.CRUISER);
            return isValid(whereInBoard, ship);
        } else if (shipSize == 4) {
            ship = new Ship(Ships.DESTROYER);
            return isValid(whereInBoard, ship);
        }

        return false;
    }

    private boolean isValid(Coordinate whereInBoard, Ship ship) {

        try {
            if (checkInsideBoard(ship, whereInBoard)) {
                if (isValidPosition(whereInBoard)) {
                    view.showSquareAlreadyTaken(whereInBoard);
                    System.out.println(whereInBoard + " is not valid");
                    return false;
                } else {
                    updateBoardDirection(ship, whereInBoard);
                }
            } else {
                return false;
            }
        } catch (IndexOutOfBoundsException ex) {
            view.showSquareOutOfBounds(whereInBoard);
            System.out.println("outside of board");
            return false;
        }
        return true;
    }


    public boolean isValidPosition(Coordinate coordinate) {
        return turn.getBoard().getSquares()[coordinate.getRow()][coordinate.getCol()].getState() != State.EMPTY;
    }

    public boolean checkInsideBoard(Ship s, Coordinate c) {
        if (horizontal) {
            if (!((Board.BOARD_WIDTH) - c.getCol() >= s.getSize())) {
                view.showSquareOutOfBounds(c);
                return false;
            } else {
                return true;
            }
        } else {
            if (!((Board.BOARD_HEIGHT) - c.getRow() >= s.getSize())) {
                view.showSquareOutOfBounds(c);
                return false;
            } else {
                return true;
            }
        }
    }

    public void updateBoardDirection(Ship s, Coordinate c) {
        if (validate(c, s, horizontal)) {
            paintShip(c, s, horizontal);
        }
    }

    private void paintShip(Coordinate c, Ship s, boolean horizontal) {
        if (horizontal) {
            if (s.getSize() == Ships.CARRIER.getSize()) {
                for (int i = 0; i < Ships.CARRIER.getSize(); i++) {
                    Coordinate newToCheck = new Coordinate(c.getRow(), c.getCol() + i);
                    onSquareSelected(newToCheck);
                }
            } else if (s.getSize() == Ships.BATTLESHIP.getSize()) {
                for (int i = 0; i < Ships.BATTLESHIP.getSize(); i++) {
                    Coordinate newToCheck = new Coordinate(c.getRow(), c.getCol() + i);
                    onSquareSelected(newToCheck);
                }
            } else if (s.getSize() == Ships.CRUISER.getSize()) {
                for (int i = 0; i < Ships.CRUISER.getSize(); i++) {
                    Coordinate newToCheck = new Coordinate(c.getRow(), c.getCol() + i);
                    onSquareSelected(newToCheck);
                }
            } else if (s.getSize() == Ships.SUBMARINE.getSize()) {
                for (int i = 0; i < Ships.SUBMARINE.getSize(); i++) {
                    Coordinate newToCheck = new Coordinate(c.getRow(), c.getCol() + i);
                    onSquareSelected(newToCheck);
                }
            } else if (s.getSize() == Ships.DESTROYER.getSize()) {
                for (int i = 0; i < Ships.DESTROYER.getSize(); i++) {
                    Coordinate newToCheck = new Coordinate(c.getRow(), c.getCol() + i);
                    onSquareSelected(newToCheck);
                }
            }
        } else {
            if (s.getSize() == Ships.CARRIER.getSize()) {
                for (int i = 0; i < Ships.CARRIER.getSize(); i++) {
                    Coordinate newToCheck = new Coordinate(c.getRow() + i, c.getCol());
                    onSquareSelected(newToCheck);
                }
            } else if (s.getSize() == Ships.BATTLESHIP.getSize()) {
                for (int i = 0; i < Ships.BATTLESHIP.getSize(); i++) {
                    Coordinate newToCheck = new Coordinate(c.getRow() + i, c.getCol());
                    onSquareSelected(newToCheck);
                }
            } else if (s.getSize() == Ships.CRUISER.getSize()) {
                for (int i = 0; i < Ships.CRUISER.getSize(); i++) {
                    Coordinate newToCheck = new Coordinate(c.getRow() + i, c.getCol());
                    onSquareSelected(newToCheck);
                }
            } else if (s.getSize() == Ships.SUBMARINE.getSize()) {
                for (int i = 0; i < Ships.SUBMARINE.getSize(); i++) {
                    Coordinate newToCheck = new Coordinate(c.getRow() + i, c.getCol());
                    onSquareSelected(newToCheck);
                }
            } else if (s.getSize() == Ships.DESTROYER.getSize()) {
                for (int i = 0; i < Ships.DESTROYER.getSize(); i++) {
                    Coordinate newToCheck = new Coordinate(c.getRow() + i, c.getCol());
                    onSquareSelected(newToCheck);
                }
            }
        }
    }


    private boolean validate(Coordinate c, Ship s, boolean horizontal) {
        if (horizontal) {
            if (s.getSize() == Ships.CARRIER.getSize()) {
                for (int i = 0; i < Ships.CARRIER.getSize(); i++) {
                    Coordinate coordinate = new Coordinate(c.getRow(), c.getCol() + i);
                    if (coordinate.getCol() > Board.BOARD_WIDTH) {
                        System.out.println(coordinate);
                        return false;
                    }
                }
            } else if (s.getSize() == Ships.BATTLESHIP.getSize()) {
                for (int i = 0; i < Ships.BATTLESHIP.getSize(); i++) {
                    Coordinate coordinate = new Coordinate(c.getRow(), c.getCol() + i);
                    if (coordinate.getCol() > Board.BOARD_WIDTH) {
                        System.out.println(coordinate);
                        return false;
                    }
                }
            } else if (s.getSize() == Ships.CRUISER.getSize()) {
                for (int i = 0; i < Ships.CRUISER.getSize(); i++) {
                    Coordinate coordinate = new Coordinate(c.getRow(), c.getCol() + i);
                    if (coordinate.getCol() > Board.BOARD_WIDTH) {
                        System.out.println(coordinate);
                        return false;
                    }
                }
            } else if (s.getSize() == Ships.SUBMARINE.getSize()) {
                for (int i = 0; i < Ships.SUBMARINE.getSize(); i++) {
                    Coordinate coordinate = new Coordinate(c.getRow(), c.getCol() + i);
                    if (coordinate.getCol() > Board.BOARD_WIDTH) {
                        System.out.println(coordinate);
                        return false;
                    }
                }
            } else if (s.getSize() == Ships.DESTROYER.getSize()) {
                for (int i = 0; i < Ships.DESTROYER.getSize(); i++) {
                    Coordinate coordinate = new Coordinate(c.getRow(), c.getCol() + i);
                    if (coordinate.getCol() > Board.BOARD_WIDTH) {
                        System.out.println(coordinate);
                        return false;
                    }
                }
            }
        } else {

            if (s.getSize() == Ships.CARRIER.getSize()) {
                for (int i = 0; i < Ships.CARRIER.getSize(); i++) {
                    Coordinate coordinate = new Coordinate(c.getRow() + i, c.getCol());
                    if (coordinate.getRow() > Board.BOARD_HEIGHT) {
                        System.out.println(coordinate);
                        return false;
                    }
                }
            } else if (s.getSize() == Ships.BATTLESHIP.getSize()) {
                for (int i = 0; i < Ships.BATTLESHIP.getSize(); i++) {
                    Coordinate coordinate = new Coordinate(c.getRow() + i, c.getCol());
                    if (coordinate.getRow() > Board.BOARD_HEIGHT) {
                        System.out.println(coordinate);
                        return false;
                    }
                }
            }
            if (s.getSize() == Ships.CRUISER.getSize()) {
                for (int i = 0; i < Ships.CRUISER.getSize(); i++) {
                    Coordinate coordinate = new Coordinate(c.getRow() + i, c.getCol());
                    if (coordinate.getRow() > Board.BOARD_HEIGHT) {
                        System.out.println(coordinate);
                        return false;
                    }
                }
            }
            if (s.getSize() == Ships.SUBMARINE.getSize()) {
                for (int i = 0; i < Ships.SUBMARINE.getSize(); i++) {
                    Coordinate coordinate = new Coordinate(c.getRow() + i, c.getCol());
                    if (coordinate.getRow() > Board.BOARD_HEIGHT) {
                        System.out.println(coordinate);
                        return false;
                    }
                }
            }
            if (s.getSize() == Ships.DESTROYER.getSize()) {
                for (int i = 0; i < Ships.DESTROYER.getSize(); i++) {
                    Coordinate coordinate = new Coordinate(c.getRow() + i, c.getCol());
                    if (coordinate.getRow() > Board.BOARD_HEIGHT) {
                        System.out.println(coordinate);
                        return false;
                    }
                }
            }
        }

        return true;
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
        this.view.setCountOnShipsPlaced(0);
        run();
    }

    public void notHappy() {
        this.view.setCountOnShipsPlaced(0);
        this.view.hideButtons();
        setupScreen();
    }

    public void onSquareSelected(Coordinate coordinate) {
//        try {
        this.board.takeSquare(State.UNHIT, coordinate);
        this.view.updateBoardDisplay(this.board);
//        } catch (CoordinateOutOfBoundException ex) {
//            this.view.showSquareOutOfBounds(coordinate);
//        } catch (SquareAlreadyTakenException ex) {
//            this.view.showSquareAlreadyTaken(coordinate);
//        }
        //this.completingTurn();
    }

}
