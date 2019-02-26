package edu.neumont.lopez.battleship.controller;

import edu.neumont.lopez.battleship.enumeration.Ships;
import edu.neumont.lopez.battleship.enumeration.State;
import edu.neumont.lopez.battleship.model.*;
import edu.neumont.lopez.battleship.view.BattleShipView;

import java.io.*;
import java.util.List;

public class BattleShipController {

    private BattleShipView view;
    //    private Board board = new Board();
//    private Board attackingBoard = new Board();
    private Player player1 = new Player();
    private Player player2 = new Player();
    private Player turn = player1;
    private Player notTurn = player2;

    private int userEx = -1;
    private boolean horizontal;


    public BattleShipController(BattleShipView ui) {
        this.view = ui;
        this.view.registerController(this);
    }

    public void initPlayers(List<String> playerNames) {
        player1.setName(playerNames.get(0));
        player2.setName(playerNames.get(1));
    }

    public void initPlayer(Player player) {
        Board ownBoard = new Board();
        Board attackingBoard = new Board();
        player.setBoard(ownBoard);
        player.setAttackingBoard(attackingBoard);
    }

    public void run() {
        this.view.init();
        setupScreen(turn);
    }

    public void runAfterSetup() {
        setupScreen(turn);
    }

    public void setupScreen(Player turn) {
        turn.getBoard().resetBoard();
        this.view.updateTurnDisplay(turn);
        this.view.updateBoardDisplay(turn.getBoard());
    }

    public void setupScreenPlayer2(Player notTurn){
        notTurn.getBoard().resetBoard();
        this.view.updateTurnDisplay(notTurn);
        this.view.updateBoardDisplay2(notTurn.getBoard());
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

    public void switchTurn() {
        if (turn == player1) {
            turn = player2;
        } else {
            turn = player1;
        }
        if (notTurn == player2) {
            notTurn = player1;
        } else {
            notTurn = player2;
        }
    }

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


    public boolean placeShips(Coordinate coordinate, int shipSize, boolean position, Player turn) {
        Coordinate whereInBoard;
        whereInBoard = coordinate;
        horizontal = position;
        Ship ship;
        if (shipSize == 0) {
            ship = new Ship(Ships.CARRIER);
            return isValid(whereInBoard, ship, turn);
        } else if (shipSize == 1) {
            ship = new Ship(Ships.BATTLESHIP);
            return isValid(whereInBoard, ship, turn);
        } else if (shipSize == 2) {
            ship = new Ship(Ships.SUBMARINE);
            return isValid(whereInBoard, ship, turn);
        } else if (shipSize == 3) {
            ship = new Ship(Ships.CRUISER);
            return isValid(whereInBoard, ship, turn);
        } else if (shipSize == 4) {
            ship = new Ship(Ships.DESTROYER);
            return isValid(whereInBoard, ship, turn);
        }
        return false;
    }

    private boolean isValid(Coordinate whereInBoard, Ship ship, Player turn) {
        try {
            if (checkInsideBoard(ship, whereInBoard)) {
                if (!isValidPosition(whereInBoard, ship , turn)) {
                    view.showSquareAlreadyTaken(whereInBoard);
                    return false;
                } else {
                    updateBoardDirection(ship, whereInBoard, turn);
                }
            } else {
                return false;
            }
        } catch (IndexOutOfBoundsException ex) {
            view.showSquareOutOfBounds(whereInBoard);
            return false;
        }
        return true;
    }

    private boolean isValidPosition(Coordinate c, Ship ship, Player turn) {

        if (horizontal) {
            if (ship.getSize() == Ships.CARRIER.getSize()) {
                for (int i = 0; i < Ships.CARRIER.getSize(); i++) {
                    if (turn.getBoard().getSquares()[c.getRow()][c.getCol() + i].getState() != State.EMPTY) {
                        return false;
                    }
                }
            } else if (ship.getSize() == Ships.BATTLESHIP.getSize()) {
                for (int i = 0; i < Ships.BATTLESHIP.getSize(); i++) {
                    if (turn.getBoard().getSquares()[c.getRow()][c.getCol() + i].getState() != State.EMPTY) {
                        return false;
                    }
                }
            } else if (ship.getSize() == Ships.CRUISER.getSize()) {
                for (int i = 0; i < Ships.CRUISER.getSize(); i++) {
                    if (turn.getBoard().getSquares()[c.getRow()][c.getCol() + i].getState() != State.EMPTY) {
                        return false;
                    }
                }
            } else if (ship.getSize() == Ships.SUBMARINE.getSize()) {
                for (int i = 0; i < Ships.SUBMARINE.getSize(); i++) {
                    if (turn.getBoard().getSquares()[c.getRow()][c.getCol() + i].getState() != State.EMPTY) {
                        return false;
                    }
                }
            } else if (ship.getSize() == Ships.DESTROYER.getSize()) {
                for (int i = 0; i < Ships.DESTROYER.getSize(); i++) {
                    if (turn.getBoard().getSquares()[c.getRow()][c.getCol() + i].getState() != State.EMPTY) {
                        return false;
                    }
                }
            }
        } else {
            if (ship.getSize() == Ships.CARRIER.getSize()) {
                for (int i = 0; i < Ships.CARRIER.getSize(); i++) {
                    if (turn.getBoard().getSquares()[c.getRow() + i][c.getCol()].getState() != State.EMPTY) {
                        return false;
                    }
                }
            } else if (ship.getSize() == Ships.BATTLESHIP.getSize()) {
                for (int i = 0; i < Ships.BATTLESHIP.getSize(); i++) {
                    if (turn.getBoard().getSquares()[c.getRow() + i][c.getCol()].getState() != State.EMPTY) {
                        return false;
                    }
                }
            } else if (ship.getSize() == Ships.CRUISER.getSize()) {
                for (int i = 0; i < Ships.CRUISER.getSize(); i++) {
                    if (turn.getBoard().getSquares()[c.getRow() + i][c.getCol()].getState() != State.EMPTY) {
                        return false;
                    }
                }
            } else if (ship.getSize() == Ships.SUBMARINE.getSize()) {
                for (int i = 0; i < Ships.SUBMARINE.getSize(); i++) {
                    if (turn.getBoard().getSquares()[c.getRow() + i][c.getCol()].getState() != State.EMPTY) {
                        return false;
                    }
                }
            } else if (ship.getSize() == Ships.DESTROYER.getSize()) {
                for (int i = 0; i < Ships.DESTROYER.getSize(); i++) {
                    if (turn.getBoard().getSquares()[c.getRow() + i][c.getCol()].getState() != State.EMPTY) {
                        return false;
                    }
                }
            }
        }
        return true;
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

    public void updateBoardDirection(Ship s, Coordinate c, Player turn) {
        if (validate(c, s, horizontal)) {
            paintShip(c, s, horizontal, turn);
        }
    }

    private void paintShip(Coordinate c, Ship s, boolean horizontal, Player turn) {
        if (horizontal) {
            if (s.getSize() == Ships.CARRIER.getSize()) {
                for (int i = 0; i < Ships.CARRIER.getSize(); i++) {
                    Coordinate newToCheck = new Coordinate(c.getRow(), c.getCol() + i);
                    onSquareSelected(newToCheck, turn);
                }
            } else if (s.getSize() == Ships.BATTLESHIP.getSize()) {
                for (int i = 0; i < Ships.BATTLESHIP.getSize(); i++) {
                    Coordinate newToCheck = new Coordinate(c.getRow(), c.getCol() + i);
                    onSquareSelected(newToCheck, turn);
                }
            } else if (s.getSize() == Ships.CRUISER.getSize()) {
                for (int i = 0; i < Ships.CRUISER.getSize(); i++) {
                    Coordinate newToCheck = new Coordinate(c.getRow(), c.getCol() + i);
                    onSquareSelected(newToCheck, turn);
                }
            } else if (s.getSize() == Ships.SUBMARINE.getSize()) {
                for (int i = 0; i < Ships.SUBMARINE.getSize(); i++) {
                    Coordinate newToCheck = new Coordinate(c.getRow(), c.getCol() + i);
                    onSquareSelected(newToCheck, turn);
                }
            } else if (s.getSize() == Ships.DESTROYER.getSize()) {
                for (int i = 0; i < Ships.DESTROYER.getSize(); i++) {
                    Coordinate newToCheck = new Coordinate(c.getRow(), c.getCol() + i);
                    onSquareSelected(newToCheck, turn);
                }
            }
        } else {
            if (s.getSize() == Ships.CARRIER.getSize()) {
                for (int i = 0; i < Ships.CARRIER.getSize(); i++) {
                    Coordinate newToCheck = new Coordinate(c.getRow() + i, c.getCol());
                    onSquareSelected(newToCheck, turn);
                }
            } else if (s.getSize() == Ships.BATTLESHIP.getSize()) {
                for (int i = 0; i < Ships.BATTLESHIP.getSize(); i++) {
                    Coordinate newToCheck = new Coordinate(c.getRow() + i, c.getCol());
                    onSquareSelected(newToCheck,turn);
                }
            } else if (s.getSize() == Ships.CRUISER.getSize()) {
                for (int i = 0; i < Ships.CRUISER.getSize(); i++) {
                    Coordinate newToCheck = new Coordinate(c.getRow() + i, c.getCol());
                    onSquareSelected(newToCheck, turn);
                }
            } else if (s.getSize() == Ships.SUBMARINE.getSize()) {
                for (int i = 0; i < Ships.SUBMARINE.getSize(); i++) {
                    Coordinate newToCheck = new Coordinate(c.getRow() + i, c.getCol());
                    onSquareSelected(newToCheck,turn);
                }
            } else if (s.getSize() == Ships.DESTROYER.getSize()) {
                for (int i = 0; i < Ships.DESTROYER.getSize(); i++) {
                    Coordinate newToCheck = new Coordinate(c.getRow() + i, c.getCol());
                    onSquareSelected(newToCheck,turn);
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

    //TODO save and load boards
    public void save(File file) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            // out.writeObject(this.board);
        }
    }

    public void load(File file) throws IOException, ClassNotFoundException {
        try (
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            //this.board = (Board) in.readObject();
        }
        //this.view.updateBoardDisplay(this.board);
        this.view.updateTurnDisplay(this.getTurn());
    }

    public Player getTurn() {
        return this.turn;
    }

    public Player getNotTurn() {
        return notTurn;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void onResetRequested() {
        this.view.setCountOnShipsPlaced(0);
        run();
    }

    public void notHappy() {
        if (turn == player1){
        this.view.setCountOnShipsPlaced(0);
        this.view.hideButtons();
        setupScreen(turn);
        } else if (turn == player2){
            this.view.setCountOnShipsPlaced(0);
            this.view.hideButtons();
            setupScreenPlayer2(turn);
        }
    }

    public void onSquareSelected(Coordinate coordinate, Player turn) {
        if (turn == player1){

        turn.getBoard().takeSquare(State.UNHIT, coordinate);
        this.view.updateBoardDisplay(turn.getBoard());
        } else if (turn == player2){
            turn.getBoard().takeSquare(State.UNHIT, coordinate);
            this.view.updateBoardDisplay2(turn.getBoard());
        }
    }

}
