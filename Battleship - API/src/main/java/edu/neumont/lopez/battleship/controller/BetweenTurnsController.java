package edu.neumont.lopez.battleship.controller;

import edu.neumont.lopez.battleship.view.BetweenTurnsView;

public class BetweenTurnsController {

    private BetweenTurnsView view;

    public BetweenTurnsController(BetweenTurnsView view) {
        this.view = view;
        this.view.registerController(this);
    }

    public void run(){
        this.view.init();
    }


}
