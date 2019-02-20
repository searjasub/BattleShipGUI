package edu.neumont.lopez.view;

import edu.neumont.lopez.battleship.controller.BattleShipController;
import edu.neumont.lopez.battleship.controller.BetweenTurnsController;
import edu.neumont.lopez.battleship.enumeration.State;
import edu.neumont.lopez.battleship.model.Board;
import edu.neumont.lopez.battleship.model.Coordinate;
import edu.neumont.lopez.battleship.model.Player;
import edu.neumont.lopez.battleship.view.BattleShipView;
import interfaces.ConsoleUI;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class MyBattleShipView implements BattleShipView {

    public Label playerTurnLabel;
    public GridPane boardGridPane;
    public GridPane boardGridPaneAttacking;
    public Label doneBtn;
    public Label notDoneBtn;
    public Text textAboveButton;
    public HBox hbox;
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

    public void init() {
        hideButtons();
        initPlayer(controller.getTurn());
        this.turnStage.setTitle("Battleship");
        //TODO hint to add icon (Search on Google)
        this.turnStage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("Battleship-icon.png")));
        this.turnStage.centerOnScreen();
        this.turnStage.show();
    }

    public void setCountOnShipsPlaced(int countOnShipsPlaced) {
        this.countOnShipsPlaced = countOnShipsPlaced;
    }

    private void initPlayer(Player turn) {
        turnStage.hide();
        controller.initPlayers(getPlayersName());
        turnStage.show();
        drawShootingGrid();
        drawOwnGrid();
        textAboveBtn();
        controller.initPlayer(turn);
    }

    public void hideButtons() {
        doneBtn.setVisible(false);
        notDoneBtn.setVisible(false);
    }

    private void textAboveBtn() {
        textAboveButton.setText("Let's place your ships on the board\nLEFT CLICK = Horizontal\nRIGHT CLICK = Vertical\n\n" + "Current Ship Waiting to be placed: Carrier | Size: 5");
    }

    private EventHandler<MouseEvent> handleOnMouseClick() {
        return event -> {
            if (countOnShipsPlaced < 5) {
                Label labelOwnPlayer = (Label) event.getSource();
                String buttonId = labelOwnPlayer.getId();
                String[] pieces = buttonId.split("x");
                Coordinate coordinate = new Coordinate((Integer.parseInt(pieces[0])), Integer.parseInt(pieces[1]));
                System.out.println("View: " + coordinate);
                if (event.getButton() == MouseButton.PRIMARY) {
                    if (controller.placeShips(coordinate, countOnShipsPlaced, true)) {
                        countOnShipsPlaced++;
                        showShipInfo();
                    } else {
                        showShipInfo();
                    }
                } else if (event.getButton() == MouseButton.SECONDARY) {
                    if (controller.placeShips(coordinate, countOnShipsPlaced, false)) {
                        countOnShipsPlaced++;
                        showShipInfo();
                    } else {
                        showShipInfo();
                    }
                }
            }
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
        };
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
            //TODO add icon
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

        doneBtn.getStyleClass().add("doneBtn");
        EventHandler<MouseEvent> happy = event -> {
            doneBtn = (Label) event.getSource();
            URL location = this.getClass().getClassLoader().getResource("BetweenTurnsView.fxml");
            FXMLLoader loader = new FXMLLoader(location);
            try {
                Parent root = loader.load();
                Scene scene = new Scene(root);
                this.turnStage.setScene(scene);
                this.turnStage.setMinWidth(800);
                this.turnStage.setMinHeight(880);
                this.turnStage.setMaxWidth(800);
                this.turnStage.setMaxHeight(880);
            } catch (IOException e) {
                e.printStackTrace();
            }
            BetweenTurnsView viewController = loader.getController();
            viewController.setStage(this.turnStage);
            BetweenTurnsController controller1 = new BetweenTurnsController(viewController);
            controller1.run();

            this.setTurnStage(secondStage);
            updateTurnDisplay(controller.getTurn());

            System.out.println("button clicked");
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

    private void drawShootingGrid() {

        for (int c = 0; c < Board.BOARD_WIDTH; c++) {
            for (int r = 0; r < Board.BOARD_HEIGHT; r++) {
                Label labelAttacking = new Label();
                labelAttacking.getStyleClass().add("grid");
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
