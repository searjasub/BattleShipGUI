package edu.neumont.lopez.battleship.model;

import java.io.Serializable;

public class Player implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private Board board;
    private Board attackingBoard;
    private int lives = 17;

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives -= lives;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Board getAttackingBoard() {
        return attackingBoard;
    }

    public void setAttackingBoard(Board attackingBoard) {
        this.attackingBoard = attackingBoard;
    }
}
