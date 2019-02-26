package edu.neumont.lopez;

import edu.neumont.lopez.battleship.controller.BattleShipMenuController;
import edu.neumont.lopez.view.BattleShipMenuView;
import edu.neumont.lopez.view.MainView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class BattleShip extends Application {

    public static void main(String[] args) {
        Application.launch(BattleShip.class, args);
    }

    public void start(Stage stage) throws Exception {

//        URL locationMain = this.getClass().getClassLoader().getResource("MainMenuView.fxml");
//        FXMLLoader loaderMain = new FXMLLoader(locationMain);
//        Parent rootMain = loaderMain.load();
//        Scene sceneMain = new Scene(rootMain);
//        stage.setScene(sceneMain);
//
//        BattleShipMenuView menuController = loaderMain.getController();
//        menuController.setStage(stage);
//
//        BattleShipMenuController controller = new BattleShipMenuController(menuController);
//        controller.run();

        MainView view = new MainView();
        view.init(stage);
        //view.initViewToGame();
        MainView.showMainMenu();
    }
}
