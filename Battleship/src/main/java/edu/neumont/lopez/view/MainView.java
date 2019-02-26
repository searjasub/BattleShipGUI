package edu.neumont.lopez.view;

import edu.neumont.lopez.battleship.controller.BattleShipController;
import edu.neumont.lopez.battleship.controller.BattleShipMenuController;
import edu.neumont.lopez.battleship.controller.BetweenTurnsController;
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
    private static BetweenTurnsView betweenTurnsView;

    private static BattleShipMenuController battleShipMenuController;
    private static BattleShipController myBattleShipController;
    private static BetweenTurnsController betweenTurnsController;

    private static Parent parentMenu, parentGame1, parentGame2, parentBetween;
    private static Stage stage;


    public void init(Stage s) throws IOException {
        stage = s;
        URL locationMain = this.getClass().getClassLoader().getResource("MainMenuView.fxml");
        FXMLLoader loaderMenu = new FXMLLoader(locationMain);

        URL location = this.getClass().getClassLoader().getResource("MyBattleShipView.fxml");
        FXMLLoader loaderGame = new FXMLLoader(location);

        URL locationBetween = this.getClass().getClassLoader().getResource("BetweenTurnsView.fxml");
        FXMLLoader loaderBetween = new FXMLLoader(locationBetween);

        parentMenu = loaderMenu.load();
        parentGame1 = loaderGame.load();
        parentBetween = loaderBetween.load();

        menuView = loaderMenu.getController();
        menuView.setStage(s);
        battleShipMenuController = new BattleShipMenuController(menuView);

        battleShipView = loaderGame.getController();
        battleShipView.setStage(s);
        myBattleShipController = new BattleShipController(battleShipView);

        betweenTurnsView = loaderBetween.getController();
        betweenTurnsView.setStage(s);
        betweenTurnsController = new BetweenTurnsController(betweenTurnsView);

    }

    public static void showMainMenu(){
        stage.setScene(new Scene(parentMenu));
        battleShipMenuController.run();
    }

    public static void showGame(){
        Scene scene = new Scene(parentGame1);
        stage.setScene(scene);
        myBattleShipController.run();
    }

    public static void showGame2(){
        Scene scene = new Scene(parentGame2);
        stage.setScene(scene);
        myBattleShipController.switchTurn();
        myBattleShipController.runAfterSetup();
    }

    public static void showBetween(){
       if (myBattleShipController.getTurn() == myBattleShipController.getPlayer1()){

        stage.setScene(new Scene(parentBetween));
        betweenTurnsController.run();
       }
       else if (myBattleShipController.getTurn() == myBattleShipController.getPlayer2()){
           stage.setScene(new Scene( parentBetween));
           betweenTurnsController.run();
           myBattleShipController.setupScreenPlayer2(myBattleShipController.getPlayer2());
       }

    }



//    public void startGame() throws IOException {
//
//        root = loaderMain.load();
//        Scene sceneMain = new Scene(root);
//        stage.setScene(sceneMain);
//
//        menuView = loaderMain.getController();
//        menuView.setStage(stage);
//
//        BattleShipMenuController controller = new BattleShipMenuController(menuView);
//        controller.run();
//
//    }

    public void initViewToGame(){
        this.stage.setTitle("Battleship");
        this.stage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("Battleship-icon.png")));
        this.stage.setMinWidth(430);
        this.stage.setMinHeight(280);
        this.stage.setMaxWidth(430);
        this.stage.setMaxHeight(280);
        this.stage.show();
    }

//    public void switchViewToGame(){
//
//        try {
//            root = loader.load();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        Scene scene = new Scene(root);
//
//        stage.setScene(scene);
//
//        battleShipView = loader.getController();
//        battleShipView.setStage(stage);
//
//        BattleShipController controller = new BattleShipController(battleShipView);
//        controller.run();
//    }




}
