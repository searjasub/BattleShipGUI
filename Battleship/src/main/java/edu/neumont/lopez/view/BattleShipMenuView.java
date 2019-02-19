package edu.neumont.lopez.view;

import edu.neumont.lopez.battleship.controller.BattleShipController;
import edu.neumont.lopez.battleship.controller.BattleShipMenuController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

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
        this.stage.setMinWidth(420);
        this.stage.setMinHeight(270);
        this.stage.setMaxWidth(430);
        this.stage.setMaxHeight(280);
        this.stage.show();
    }

    private void drawOptions() {

        hbox.getStyleClass().add("hbox");
        hbox.setSpacing(25);

        EventHandler<ActionEvent> click = event -> {
            URL location = this.getClass().getClassLoader().getResource("MyBattleShipView.fxml");
            FXMLLoader loader = new FXMLLoader(location);
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Scene scene = new Scene(root);
            this.stage.setScene(scene);
            this.stage.setMinWidth(720);
            this.stage.setMinHeight(870);
            this.stage.setMaxWidth(730);
            this.stage.setMaxHeight(880);

            MyBattleShipView viewController = loader.getController();
            viewController.setTurnStage(this.stage);

            BattleShipController controller = new BattleShipController(viewController);
            controller.run();
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
