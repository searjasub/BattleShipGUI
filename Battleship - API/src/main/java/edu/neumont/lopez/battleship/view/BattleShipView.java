package edu.neumont.lopez.battleship.view;

import edu.neumont.lopez.battleship.controller.BattleShipController;
import edu.neumont.lopez.battleship.model.Board;
import edu.neumont.lopez.battleship.model.Coordinate;
import edu.neumont.lopez.battleship.model.Player;
import edu.neumont.lopez.battleship.model.Ship;

public interface BattleShipView {

    void init();
    void registerController(BattleShipController battleShipController);
    void updateBoardDisplay(Board board);
    void updateTurnDisplay(Player turn);
    void showSquareAlreadyTaken(Coordinate coordinate);
    void showSquareOutOfBounds(Coordinate coordinate);
    void setCountOnShipsPlaced(int countOnShipsPlaced);
    void hideButtons();

    void updateBoardDisplay2(Board board);
}
