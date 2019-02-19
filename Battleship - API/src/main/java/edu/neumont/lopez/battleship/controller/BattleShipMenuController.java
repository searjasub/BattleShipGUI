package edu.neumont.lopez.battleship.controller;

import edu.neumont.lopez.battleship.view.BattleShipMenuView;

public class BattleShipMenuController {

    private BattleShipMenuView view;

    public BattleShipMenuController(BattleShipMenuView ui) {
        this.view = ui;
        this.view.registerController(this);
    }

    public void run(){
        this.view.init();
    }
}
