package edu.neumont.lopez.battleship.view;

import edu.neumont.lopez.battleship.controller.BattleShipMenuController;

public interface BattleShipMenuView {
    void registerController(BattleShipMenuController battleShipMenuController);
    void init();
}
