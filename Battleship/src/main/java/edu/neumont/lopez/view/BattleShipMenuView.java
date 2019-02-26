package edu.neumont.lopez.view;

import edu.neumont.lopez.battleship.controller.BattleShipMenuController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class BattleShipMenuView implements edu.neumont.lopez.battleship.view.BattleShipMenuView {

    public HBox hbox;
    public Button start;
    public Button load;
    public Button exit;

    private Stage stage;
    private BattleShipMenuController controller;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void registerController(BattleShipMenuController battleShipMenuController) {
        this.controller = battleShipMenuController;
    }

    @Override
    public void init() {
        drawOptions();

        this.stage.setTitle("Battleship");
        this.stage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("Battleship-icon.png")));
        this.stage.setMinWidth(430);
        this.stage.setMinHeight(280);
        this.stage.setMaxWidth(430);
        this.stage.setMaxHeight(280);
        this.stage.show();
    }

    private void drawOptions() {

        hbox.getStyleClass().add("hbox");
        hbox.setSpacing(25);

        EventHandler<ActionEvent> click = event -> {

            MainView.showGame();
        };

        start.setText("New Game");
        start.getStyleClass().add("start");
        start.setOnAction(click);

        EventHandler<ActionEvent> clickLoad = event -> {



        };
        load.setText("Load");
        load.getStyleClass().add("load");
        load.setOnAction(clickLoad);

        EventHandler<ActionEvent> clickExit = event -> {
            this.stage.close();
        };
        exit.setText("Exit");
        exit.getStyleClass().add("exit");
        exit.setOnAction(clickExit);
    }
}
