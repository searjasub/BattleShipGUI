package edu.neumont.lopez.view;

import edu.neumont.lopez.battleship.controller.BattleShipController;
import edu.neumont.lopez.battleship.controller.BattleShipMenuController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class MainView {

    private static BattleShipMenuView menuView;
    private static MyBattleShipView battleShipView;

    private static BattleShipMenuController battleShipMenuController;
    private static BattleShipController myBattleShipController;

    private static Parent parentMenu, parentGame1;
    private static Stage stage;

    public static void showMainMenu() {
        stage.setScene(new Scene(parentMenu));
        battleShipMenuController.run();
    }

    public static void mainMenu(){
        battleShipMenuController.run();
    }

    public static void showGame() {
        Scene scene = new Scene(parentGame1);
        stage.setScene(scene);
        myBattleShipController.run();
    }

    public void init(Stage s) throws IOException {
        stage = s;
        URL locationMain = this.getClass().getClassLoader().getResource("MainMenuView.fxml");
        FXMLLoader loaderMenu = new FXMLLoader(locationMain);

        URL location = this.getClass().getClassLoader().getResource("MyBattleShipView.fxml");
        FXMLLoader loaderGame = new FXMLLoader(location);


        parentMenu = loaderMenu.load();
        parentGame1 = loaderGame.load();

        menuView = loaderMenu.getController();
        menuView.setStage(s);
        battleShipMenuController = new BattleShipMenuController(menuView);

        battleShipView = loaderGame.getController();
        battleShipView.setStage(s);
        myBattleShipController = new BattleShipController(battleShipView);

    }

    public void initViewToGame() {
        this.stage.setTitle("Battleship");
        this.stage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("Battleship-icon.png")));
        this.stage.setMinWidth(430);
        this.stage.setMinHeight(280);
        this.stage.setMaxWidth(430);
        this.stage.setMaxHeight(280);
        this.stage.show();
    }
}
