package edu.neumont.lopez.battleship.model;

import edu.neumont.lopez.battleship.enumeration.State;

import java.io.Serializable;

public class BoardSquare implements Serializable {

    private static final long serialVersionUID = 1L;

    private State state = State.EMPTY;

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }


}
