package edu.neumont.lopez.view;

import edu.neumont.lopez.battleship.controller.BetweenTurnsController;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


public class BetweenTurnsView implements edu.neumont.lopez.battleship.view.BetweenTurnsView {

    public Label label;
    private BetweenTurnsController controller;
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void registerController(BetweenTurnsController betweenTurnsController) {
        this.controller = betweenTurnsController;
    }

    @Override
    public void init() {
        drawLabel();
        this.stage.setTitle("Battleship");
        this.stage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("Battleship-icon.png")));
        this.stage.centerOnScreen();
        this.stage.show();
    }

    private void drawLabel() {

        EventHandler<MouseEvent> click = event -> {




        };

        label.setText("Click anywhere when the computer has \nbeen passed around to the next player");
        label.getStyleClass().add("label");
        label.setOnMouseClicked(click);



    }
}

