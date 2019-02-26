package edu.neumont.lopez.battleship.view;

import edu.neumont.lopez.battleship.controller.BetweenTurnsController;

public interface BetweenTurnsView {
    void registerController(BetweenTurnsController controller);
    void init();
}