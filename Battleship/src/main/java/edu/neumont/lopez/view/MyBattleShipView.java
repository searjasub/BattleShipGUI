package edu.neumont.lopez.view;

import edu.neumont.lopez.battleship.controller.BattleShipController;
import edu.neumont.lopez.battleship.enumeration.State;
import edu.neumont.lopez.battleship.model.Board;
import edu.neumont.lopez.battleship.model.Coordinate;
import edu.neumont.lopez.battleship.model.Player;
import edu.neumont.lopez.battleship.view.BattleShipView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class MyBattleShipView implements BattleShipView {

    public Label playerTurnLabel;
    public GridPane player1PaneAttacking;
    public GridPane player1GridPane;
    public GridPane player2PaneAttacking;
    public GridPane player2GridPane;
    public Label doneBtn;
    public Label notDoneBtn;
    public Text textAboveButton;
    public HBox hbox;
    public Text trackingBoardPlayer1;
    public Text boardPlayer1;
    public  Text trackingBoardPlayer2;
    public Text boardPlayer2;
    public BorderPane borderPane;

    private BattleShipController controller;
    private Stage stage;
    private Map<Coordinate, Label> boardLabels = new HashMap<>();
    private Map<Coordinate, Label> boardLabels2 = new HashMap<>();
    private int countOnShipsPlaced = 0;

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void registerController(BattleShipController battleShipController) {
        this.controller = battleShipController;
    }

    public void init() {
        hideButtons();
        stage.hide();
        controller.initPlayers(getPlayersName());
        initPlayer(controller.getTurn());
        initPlayer(controller.getNotTurn());

        this.stage.setTitle("Battleship");
        this.stage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("Battleship-icon.png")));
        this.stage.centerOnScreen();
        this.stage.setMinWidth(1200);
        this.stage.setMinHeight(880);
        this.stage.show();

    }

    public void setCountOnShipsPlaced(int countOnShipsPlaced) {
        this.countOnShipsPlaced = countOnShipsPlaced;
    }

    private void initPlayer(Player turn) {
        stage.hide();
        drawShootingGridPlayer1();
        drawOwnGridPlayer1();
        drawShootingGridPlayer2();
        drawOwnGridPlayer2();
        textAboveBtn();
        hidePlayer2();

        controller.initPlayer(turn);
    }

    public void hidePlayer2(){
        trackingBoardPlayer2.setVisible(false);
        player2PaneAttacking.setVisible(false);
        boardPlayer2.setVisible(false);
        player2GridPane.setVisible(false);
    }
    public void showPlayer2(){
        trackingBoardPlayer2.setVisible(true);
        player2PaneAttacking.setVisible(true);
        boardPlayer2.setVisible(true);
        player2GridPane.setVisible(true);
    }

    public void hidePlayer1(){
        trackingBoardPlayer1.setVisible(false);
        player1PaneAttacking.setVisible(false);
        boardPlayer1.setVisible(false);
        player1GridPane.setVisible(false);
    }

    public void showPlayer1(){
        trackingBoardPlayer1.setVisible(true);
        player1PaneAttacking.setVisible(true);
        boardPlayer1.setVisible(true);
        player1GridPane.setVisible(true);
    }

    public void hideButtons() {
        doneBtn.setVisible(false);
        notDoneBtn.setVisible(false);
    }

    private void textAboveBtn() {
        textAboveButton.setText("Let's place your ships on the board\nLEFT CLICK = Horizontal\nRIGHT CLICK = Vertical\n\n" + "Current Ship Waiting to be placed: Carrier | Size: 5");
    }

    private EventHandler<MouseEvent> handleOnMouseClickPlayer1() {
        return event -> {
            if (countOnShipsPlaced < 5) {
                Label labelOwnPlayer = (Label) event.getSource();
                String buttonId = labelOwnPlayer.getId();
                String[] pieces = buttonId.split("x");
                Coordinate coordinate = new Coordinate((Integer.parseInt(pieces[0])), Integer.parseInt(pieces[1]));
                System.out.println("View: " + coordinate);
                if (event.getButton() == MouseButton.PRIMARY) {
                    if (controller.placeShips(coordinate, countOnShipsPlaced, true, controller.getTurn())) {
                        countOnShipsPlaced++;
                        showShipInfo();
                    } else {
                        showShipInfo();
                    }
                } else if (event.getButton() == MouseButton.SECONDARY) {
                    if (controller.placeShips(coordinate, countOnShipsPlaced, false, controller.getTurn())) {
                        countOnShipsPlaced++;
                        showShipInfo();
                    } else {
                        showShipInfo();
                    }
                }
            }
            doneWithPlacing();
        };
    }

    private EventHandler<MouseEvent> handleOnMouseClickPlayer2() {
        return event -> {
            if (countOnShipsPlaced < 5) {
                Label labelPlayer2 = (Label) event.getSource();
                String buttonId = labelPlayer2.getId();
                String[] pieces = buttonId.split("x");
                Coordinate coordinate = new Coordinate((Integer.parseInt(pieces[0])), Integer.parseInt(pieces[1]));
                System.out.println("Board 2: " + coordinate);
                if (event.getButton() == MouseButton.PRIMARY) {
                    if (controller.placeShips(coordinate, countOnShipsPlaced, true, controller.getNotTurn())) {
                        countOnShipsPlaced++;
                        showShipInfo();
                    } else {
                        showShipInfo();
                    }
                } else if (event.getButton() == MouseButton.SECONDARY) {
                    if (controller.placeShips(coordinate, countOnShipsPlaced, false, controller.getNotTurn())) {
                        countOnShipsPlaced++;
                        showShipInfo();
                    } else {
                        showShipInfo();
                    }
                }
            }
            doneWithPlacing();
        };
    }

    private void doneWithPlacing() {
        if (countOnShipsPlaced == 5) {
            textAboveButton.setText("Do you like your board setup this way?\n");
            hbox.setSpacing(20);
            doneBtn.setText("Yes");
            notDoneBtn.setText("No");
            doneBtn.setVisible(true);
            notDoneBtn.setVisible(true);
            placeSwitchViewButton();

            //BetweenTurnsView screen
        }
    }

    public void showShipInfo() {
        if (countOnShipsPlaced == 1) {
            textAboveButton.setText("Let's place your ships on the board\nLEFT CLICK = Horizontal\nRIGHT CLICK = Vertical\n\n" + "Current Ship: Battleship | Size: 4");
        } else if (countOnShipsPlaced == 2) {
            textAboveButton.setText("Let's place your ships on the board\nLEFT CLICK = Horizontal\nRIGHT CLICK = Vertical\n\n" + "Current Ship: Submarine | Size: 3");
        } else if (countOnShipsPlaced == 3) {
            textAboveButton.setText("Let's place your ships on the board\nLEFT CLICK = Horizontal\nRIGHT CLICK = Vertical\n\n" + "Current Shi: Cruiser | Size: 3");
        } else if (countOnShipsPlaced == 4) {
            textAboveButton.setText("Let's place your ships on the board\nLEFT CLICK = Horizontal\nRIGHT CLICK = Vertical\n\n" + "Current Ship: Destroyer | Size: 2");
        }
    }

    private List<String> getPlayersName() {
        Optional<String> playerOneName = Optional.empty();
        boolean isValid1 = false;
        while (!isValid1) {
            TextInputDialog dialog1 = new TextInputDialog("Enter your name");
            dialog1.setTitle("Enter your name");
            dialog1.setContentText("Please enter player 1 name:");
            Stage stage = (Stage) dialog1.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("Battleship-icon.png")));
            playerOneName = dialog1.showAndWait();
            if (playerOneName.isPresent()) {
                isValid1 = true;
            }
        }
        Optional<String> playerTwoName = Optional.empty();
        boolean isValid2 = false;
        while (!isValid2) {
            TextInputDialog dialog2 = new TextInputDialog("Enter your name");
            Stage stage = (Stage) dialog2.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("Battleship-icon.png")));
            dialog2.setContentText("Please enter player 2 name");
            playerTwoName = dialog2.showAndWait();
            if (playerTwoName.isPresent()) {
                isValid2 = true;
            }
        }
        List<String> players = new ArrayList<>();
        players.add(playerOneName.get());
        players.add(playerTwoName.get());
        return players;
    }

    private void placeSwitchViewButton() {

        doneBtn.getStyleClass().add("doneBtn");
        EventHandler<MouseEvent> happy = event -> {
            doneBtn = (Label) event.getSource();

            //MainView.showBetween();
            controller.switchTurn();
            updateTurnDisplay(controller.getTurn());
            hidePlayer1();
            showPlayer2();
        };

        doneBtn.setOnMouseClicked(happy);

        notDoneBtn.getStyleClass().add("notDoneBtn");
        EventHandler<MouseEvent> noHappy = event -> {
            notDoneBtn = (Label) event.getSource();
            controller.notHappy();
            textAboveBtn();
            System.out.println("No button clicked");
        };
        notDoneBtn.setOnMouseClicked(noHappy);
    }

    private void drawShootingGridPlayer1() {

        for (int c = 0; c < Board.BOARD_WIDTH; c++) {
            for (int r = 0; r < Board.BOARD_HEIGHT; r++) {
                Label labelAttacking = new Label();
                labelAttacking.getStyleClass().add("grid");
                labelAttacking.setId("" + r + "x" + c);
                this.player1PaneAttacking.add(labelAttacking, c, r);
            }
        }
        for (int c = 0; c < 1; c++) {
            for (int r = 0; r < Board.BOARD_HEIGHT; r++) {
                Label column = new Label();
                column.setText("" + r);
                column.getTextAlignment();
                column.getStyleClass().add("info");
                this.player1PaneAttacking.add(column, c, r);
            }
        }
        for (int c = 0; c < Board.BOARD_WIDTH; c++) {
            for (int r = 0; r < 1; r++) {
                Label column = new Label();
                char upper = (char) ('@' + c);
                column.setText("" + upper);
                column.getStyleClass().add("info");
                this.player1PaneAttacking.add(column, c, r);
            }
        }
    }

    private void drawShootingGridPlayer2() {

        for (int c = 0; c < Board.BOARD_WIDTH; c++) {
            for (int r = 0; r < Board.BOARD_HEIGHT; r++) {
                Label labelAttacking = new Label();
                labelAttacking.getStyleClass().add("grid");
                labelAttacking.setId("" + r + "x" + c);
                this.player2PaneAttacking.add(labelAttacking, c, r);
            }
        }
        for (int c = 0; c < 1; c++) {
            for (int r = 0; r < Board.BOARD_HEIGHT; r++) {
                Label column = new Label();
                column.setText("" + r);
                column.getTextAlignment();
                column.getStyleClass().add("info");
                this.player2PaneAttacking.add(column, c, r);
            }
        }
        for (int c = 0; c < Board.BOARD_WIDTH; c++) {
            for (int r = 0; r < 1; r++) {
                Label column = new Label();
                char upper = (char) ('@' + c);
                column.setText("" + upper);
                column.getStyleClass().add("info");
                this.player2PaneAttacking.add(column, c, r);
            }
        }
    }

    private void drawOwnGridPlayer1() {
        for (int c = 0; c < Board.BOARD_WIDTH; c++) {
            for (int r = 0; r < Board.BOARD_HEIGHT; r++) {
                Label labelOwnPlayer = new Label();
                if (c == 0 && r == 0) {
                    labelOwnPlayer.getStyleClass().add("firstSquare");
                    this.player1GridPane.add(labelOwnPlayer, c, r);
                } else {
                    labelOwnPlayer.getStyleClass().add("grid");
                    labelOwnPlayer.addEventFilter(MouseEvent.MOUSE_CLICKED, handleOnMouseClickPlayer1());
                    labelOwnPlayer.setId("" + r + "x" + c);
                    this.player1GridPane.add(labelOwnPlayer, c, r);
                    this.boardLabels.put(new Coordinate(r, c), labelOwnPlayer);
                }
            }
        }
        for (int c = 0; c < 1; c++) {
            for (int r = 0; r < Board.BOARD_HEIGHT; r++) {
                Label row = new Label();
                row.setText("" + r);
                row.getTextAlignment();
                row.getStyleClass().add("info");
                this.player1GridPane.add(row, c, r);
            }
        }

        for (int c = 1; c < Board.BOARD_WIDTH; c++) {
            for (int r = 0; r < 1; r++) {
                Label column = new Label();
                char upper = (char) ('@' + c);
                column.setText("" + upper);
                column.getStyleClass().add("info");
                this.player1GridPane.add(column, c, r);
            }
        }
    }

    private void drawOwnGridPlayer2() {
        for (int c = 0; c < Board.BOARD_WIDTH; c++) {
            for (int r = 0; r < Board.BOARD_HEIGHT; r++) {
                Label labelOwnPlayer = new Label();
                if (c == 0 && r == 0) {
                    labelOwnPlayer.getStyleClass().add("firstSquare");
                    this.player2GridPane.add(labelOwnPlayer, c, r);
                } else {
                    labelOwnPlayer.getStyleClass().add("grid");
                    labelOwnPlayer.addEventFilter(MouseEvent.MOUSE_CLICKED, handleOnMouseClickPlayer2());
                    labelOwnPlayer.setId("" + r + "x" + c);
                    this.player2GridPane.add(labelOwnPlayer, c, r);
                    this.boardLabels2.put(new Coordinate(r, c), labelOwnPlayer);
                }
            }
        }
        for (int c = 0; c < 1; c++) {
            for (int r = 0; r < Board.BOARD_HEIGHT; r++) {
                Label row = new Label();
                row.setText("" + r);
                row.getTextAlignment();
                row.getStyleClass().add("info");
                this.player2GridPane.add(row, c, r);
            }
        }

        for (int c = 1; c < Board.BOARD_WIDTH; c++) {
            for (int r = 0; r < 1; r++) {
                Label column = new Label();
                char upper = (char) ('@' + c);
                column.setText("" + upper);
                column.getStyleClass().add("info");
                this.player2GridPane.add(column, c, r);
            }
        }
    }

    @Override
    public void updateBoardDisplay(Board board) {
        for (int c = 0; c < Board.BOARD_WIDTH; c++) {
            for (int r = 0; r < Board.BOARD_HEIGHT; r++) {
                State state = board.getLocation(r, c);
                Label label = this.boardLabels.get(new Coordinate(r, c));

                if (label != null) {
                    switch (state) {
                        case UNHIT:
                            label.getStyleClass().add("unhit");
                            break;
                        case HIT:
                            label.getStyleClass().add("hit");
                            break;
                        case MISS:
                            label.getStyleClass().add("miss");
                            break;
                        case EMPTY:
                            label.getStyleClass().removeAll("hit", "miss", "unhit");
                            break;
                    }
                }
            }
        }
    }

    @Override
    public void updateBoardDisplay2(Board board) {
        for (int c = 0; c < Board.BOARD_WIDTH; c++) {
            for (int r = 0; r < Board.BOARD_HEIGHT; r++) {
                State state = board.getLocation(r, c);
                Label label = this.boardLabels2.get(new Coordinate(r, c));
                if (label != null) {
                    switch (state) {
                        case UNHIT:
                            label.getStyleClass().add("unhit");
                            break;
                        case HIT:
                            label.getStyleClass().add("hit");
                            break;
                        case MISS:
                            label.getStyleClass().add("miss");
                            break;
                        case EMPTY:
                            label.getStyleClass().removeAll("hit", "miss", "unhit");
                            break;
                    }
                }
            }
        }
    }

    @Override
    public void showSquareAlreadyTaken(Coordinate boardSquareCoordinate) {
        Alert alert = new Alert(Alert.AlertType.ERROR, "The Ship can't be placed due to interference with another ship", ButtonType.OK);
        alert.show();
    }

    @Override
    public void showSquareOutOfBounds(Coordinate boardSquareCoordinate) {
        new Alert(Alert.AlertType.ERROR, "The ship will be sticking out.", ButtonType.OK).show();
    }

    @Override
    public void updateTurnDisplay(Player turn) {
        this.playerTurnLabel.setText(String.valueOf(turn.getName()));
    }

    public void onRestart(ActionEvent actionEvent) {
        this.controller.onResetRequested();
    }

    public void onSave(ActionEvent actionEvent) {
        boolean saved = false;
        do {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Select save file");
            File file = chooser.showSaveDialog(this.stage);
            if (file == null) {
                return;
            }
            try {
                this.controller.save(file);
                saved = true;
            } catch (IOException ex) {
                new Alert(Alert.AlertType.ERROR, "An error occured trying to save the file. Please select another location", ButtonType.OK).showAndWait();
            }
        } while (!saved);
    }

    public void onLoad(ActionEvent actionEvent) {
        File file;
        do {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Select saved file");
            file = chooser.showOpenDialog(this.stage);
            if (file == null) {
                return;
            }
            try {
                this.controller.load(file);
            } catch (IOException | ClassNotFoundException ex) {
                file = null;
                new Alert(Alert.AlertType.ERROR, "An error occurred trying to load the file. Please select another", ButtonType.OK).show();
            }
        } while (file == null);
    }

    public void onExit(ActionEvent actionEvent) {
        this.shutdown();
    }

    private void shutdown() {
        this.stage.close();
    }

    public void onAbout(ActionEvent actionEvent) {
        Alert popup = new Alert(Alert.AlertType.NONE, "CSC150 BattleShip v1.0 - by Searjasub Lopez", ButtonType.CLOSE);
        popup.setTitle("About BattleShip");
        popup.show();
    }


}
