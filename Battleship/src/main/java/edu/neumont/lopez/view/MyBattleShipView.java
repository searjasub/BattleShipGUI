package edu.neumont.lopez.view;

import edu.neumont.lopez.battleship.controller.BattleShipController;
import edu.neumont.lopez.battleship.enumeration.State;
import edu.neumont.lopez.battleship.model.Board;
import edu.neumont.lopez.battleship.model.Coordinate;
import edu.neumont.lopez.battleship.model.Player;
import edu.neumont.lopez.battleship.model.Ship;
import edu.neumont.lopez.battleship.view.BattleShipView;
import interfaces.ConsoleUI;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class MyBattleShipView implements BattleShipView {

    public Label playerTurnLabel;
    public GridPane boardGridPane;
    public GridPane boardGridPaneAttacking;
    public Button doneBtn;
    public Text textAboveButton;
    private BattleShipController controller;
    private Stage turnStage;
    private Stage secondStage;
    private Stage firstStage;
    private Map<Coordinate, Label> boardLabels = new HashMap<>();

    private int countOnShipsPlaced = 0;


    public Stage getTurnStage() {
        return turnStage;
    }

    public void setTurnStage(Stage turnStage) {
        this.turnStage = turnStage;
    }

    @Override
    public void registerController(BattleShipController battleShipController) {
        this.controller = battleShipController;
    }

    @Override
    public boolean setShipOrientation() {
        return false;
    }

    public void init() {

        initPlayer(controller.getTurn());

        this.turnStage.setTitle("Battleship");
        this.turnStage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("Battleship-icon.png")));
        this.turnStage.centerOnScreen();
        this.turnStage.show();
    }

    private void initPlayer(Player turn) {
        controller.initPlayers(getPlayersName());
        drawShootingGrid();
        drawOwnGrid();
        showShipInfo();
        controller.initPlayer(turn);
        placeSwitchViewButton();

    }

    public Coordinate viewPlacingShips() {


        //how to get the label clicked


        return null;
    }

    private EventHandler<MouseEvent> handleOnMouseClick() {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (countOnShipsPlaced < 5) {
                    showShipInfo();
                    Label labelOwnPlayer = (Label) event.getSource();
                    String buttonId = labelOwnPlayer.getId();
                    String[] pieces = buttonId.split("x");

                    Coordinate coordinate = new Coordinate((Integer.parseInt(pieces[0])), Integer.parseInt(pieces[1]));
                    System.out.println("View: " + coordinate);
                    if (event.getButton() == MouseButton.PRIMARY) {
                        controller.placeShips(coordinate, countOnShipsPlaced, true);
                        countOnShipsPlaced++;
                    } else if (event.getButton() == MouseButton.SECONDARY) {
                        controller.placeShips(coordinate, countOnShipsPlaced, false);
                        countOnShipsPlaced++;
                    }

                } else {
                    //BetweenTurns screen
                }


            }
        };
    }



    public void showShipInfo() {

        //textAboveButton.setText("Let's place your ships on the board\nLEFT CLICK = Horizontal\nRIGHT CLICK = Vertical\n\n" + "Current Ship size: 5");
        if (countOnShipsPlaced == 0){
        textAboveButton.setText("Let's place your ships on the board\nLEFT CLICK = Horizontal\nRIGHT CLICK = Vertical\n\n" + "Current Ship size: 4");
        } else if (countOnShipsPlaced == 2){
        textAboveButton.setText("Let's place your ships on the board\nLEFT CLICK = Horizontal\nRIGHT CLICK = Vertical\n\n" + "Current Ship size: 3");
        }else if (countOnShipsPlaced == 3){
        textAboveButton.setText("Let's place your ships on the board\nLEFT CLICK = Horizontal\nRIGHT CLICK = Vertical\n\n" + "Current Ship size: 2");
        }else if (countOnShipsPlaced == 4){
        textAboveButton.setText("Let's place your ships on the board\nLEFT CLICK = Horizontal\nRIGHT CLICK = Vertical\n\n" + "Current Ship size: 1");
        }

    }


    private List<String> getPlayersName() {
        Optional<String> playerOneName = Optional.empty();
        boolean isValid1 = false;
        while (!isValid1) {
            TextInputDialog dialog1 = new TextInputDialog("Enter your name");
            dialog1.setTitle("Enter your name");
            dialog1.setContentText("Please enter player 1 name:");
            playerOneName = dialog1.showAndWait();
            if (playerOneName.isPresent()) {
                isValid1 = true;
            }
        }

        Optional<String> playerTwoName = Optional.empty();
        boolean isValid2 = false;
        while (!isValid2) {
            TextInputDialog dialog2 = new TextInputDialog("Enter your name");
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
        EventHandler<ActionEvent> clickHandler = event -> {
            doneBtn = (Button) event.getSource();
            doneBtn.getStyleClass().add("switchview");

            this.setTurnStage(secondStage);
            controller.switchTurn();
            updateTurnDisplay(controller.getTurn());

            System.out.println("button clicked");
        };
        doneBtn.setOnAction(clickHandler);
    }

    private void drawShootingGrid() {

        for (int c = 0; c < Board.BOARD_WIDTH; c++) {
            for (int r = 0; r < Board.BOARD_HEIGHT; r++) {

                Label labelAttacking = new Label();
                labelAttacking.getStyleClass().add("grid");
                //labelAttacking.addEventFilter(MouseEvent.MOUSE_CLICKED,handleOnMouseClick());
                labelAttacking.setId("" + r + "x" + c);
                this.boardGridPaneAttacking.add(labelAttacking, c, r);
            }

        }
        for (int c = 0; c < 1; c++) {
            for (int r = 0; r < Board.BOARD_HEIGHT; r++) {
                Label column = new Label();
                column.setText("" + r);
                column.getTextAlignment();
                column.getStyleClass().add("info");
                this.boardGridPaneAttacking.add(column, c, r);
            }
        }

        for (int c = 0; c < Board.BOARD_WIDTH; c++) {
            for (int r = 0; r < 1; r++) {
                Label column = new Label();
                char upper = (char) ('@' + c);
                column.setText("" + upper);
                column.getStyleClass().add("info");
                this.boardGridPaneAttacking.add(column, c, r);
            }
        }
    }

    private void drawOwnGrid() {
        for (int c = 0; c < Board.BOARD_WIDTH; c++) {
            for (int r = 0; r < Board.BOARD_HEIGHT; r++) {
                Label labelOwnPlayer = new Label();
                if (c == 0 && r == 0) {
                    labelOwnPlayer.getStyleClass().add("firstSquare");
                    this.boardGridPane.add(labelOwnPlayer, c, r);
                } else {
                    labelOwnPlayer.getStyleClass().add("grid");
                    labelOwnPlayer.addEventFilter(MouseEvent.MOUSE_CLICKED, handleOnMouseClick());
                    labelOwnPlayer.setId("" + r + "x" + c);
                    this.boardGridPane.add(labelOwnPlayer, c, r);
                    //System.out.println("" + c + "x" + r);
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
                this.boardGridPane.add(row, c, r);
            }
        }

        for (int c = 1; c < Board.BOARD_WIDTH; c++) {
            for (int r = 0; r < 1; r++) {
                Label column = new Label();
                char upper = (char) ('@' + c);
                column.setText("" + upper);
                column.getStyleClass().add("info");
                this.boardGridPane.add(column, c, r);
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
    public void showSquareAlreadyTaken(Coordinate boardSquareCoordinate) {
        Alert alert = new Alert(Alert.AlertType.ERROR, "That square is already taken", ButtonType.OK);
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
            File file = chooser.showSaveDialog(this.turnStage);
            if (file == null) {
                return;
            }
            try {
                this.controller.save(file);
                saved = true;

            } catch (IOException ex) {
                new Alert(Alert.AlertType.ERROR, "An error occured trying to save the file. Please select another location", ButtonType.OK);
            }

        } while (!saved);
    }

    public void onLoad(ActionEvent actionEvent) {
        File file;
        do {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Select saved file");
            file = chooser.showOpenDialog(this.turnStage);
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
        this.turnStage.close();
    }

    public void onAbout(ActionEvent actionEvent) {
        Alert popup = new Alert(Alert.AlertType.NONE, "CSC150 BattleShip v1.0 - by Searjasub Lopez", ButtonType.CLOSE);
        popup.setTitle("About BattleShip");
        popup.show();
    }

    public boolean done() {
        try {
            return ConsoleUI.promptForBool("Are you ready to pass the laptop?", "Y", "N");
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

}
