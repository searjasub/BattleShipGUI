package edu.neumont.lopez.battleship.view;

import edu.neumont.lopez.battleship.controller.BattleShipController;
import edu.neumont.lopez.battleship.model.Board;
import edu.neumont.lopez.battleship.model.Coordinate;
import edu.neumont.lopez.battleship.model.Player;
import edu.neumont.lopez.battleship.model.Ship;

public interface BattleShipView {

    void init();
    void registerController(BattleShipController battleShipController);
    boolean setShipOrientation();
    boolean done();


    void updateBoardDisplay(Board board);
    void updateTurnDisplay(Player turn);
    Coordinate viewPlacingShips();
    void showSquareAlreadyTaken(Coordinate coordinate);
    void showSquareOutOfBounds(Coordinate coordinate);

    void setCountOnShipsPlaced(int countOnShipsPlaced);

    void hideButtons();
}
