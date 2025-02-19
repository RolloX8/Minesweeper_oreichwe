package com.oreichwe.minesweeper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.Random;

public class GamefieldController {

    private int gridLength;
    private int gridWidth;
    private int numberOfBombs;
    private int numberOfFlags;
    @FXML
    private GridPane grid;
    @FXML
    private ComboBox<String> difficulties = new ComboBox<>();
    @FXML
    private AnchorPane rootAnchor;

    @FXML
    private void initialize() {
        setDefaults();
        setDropDownDifficulties();
    }

    public void setStyles(){
        int minesweeperButtonWidthAndLength = 100;
        int gameWidth = getGridWidth() * minesweeperButtonWidthAndLength;
        int gameLength = getGridLength() * minesweeperButtonWidthAndLength;

        getRootAnchor().setStyle("-fx-pref-width: " + gameWidth + "px;");
        getRootAnchor().setStyle("-fx-pref-height: " + gameLength + "px;");
    }

    //setzt die Werte auf Default, die dann überschrieben werden
    private void setDefaults() {
        setGridLength(0);
        setGridWidth(0);
        setNumberOfFlags(0);
        setNumberOfBombs(0);
        clearGrid();
    }

    //füllt das Grid entsprechend dem Schwierigkeitsgrad
    public void fillGrid() throws IOException {
        if (getGridLength() != -1 && getGridWidth() != -1) {

            for (int i = 0; i < getGridLength(); i++) {
                for (int j = 0; j < getGridWidth(); j++) {
                    getGrid().add(createMinesweeperButton(i, j), i, j);
                }
            }
        } else {
            System.out.println("no gridlength or gridwidth provided");
        }

        System.out.println("Grid Children: " + grid.getChildren().size());
    }

    //leert das Grid wieder
    public void clearGrid() {
        grid.getChildren().clear();
    }

    //gibt das Element(Node) das in einem Grid an den stellen X, Y steht
    public Node getNodeFromGrid(int x, int y) {
        for (Node node : getGrid().getChildren()) {
            if (getGrid().getColumnIndex(node).equals(x) && getGrid().getRowIndex(node).equals(y)) {
                return node;
            }
        }
        return null;
        //NOTE@MYSELF: https://stackoverflow.com/questions/20825935/javafx-get-node-by-row-and-column
    }

    //erstellt einen MinesweeperButton
    public Node createMinesweeperButton(int x, int y) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("minesweeperButton-view.fxml"));
        Node minesweeperButton = fxmlLoader.load();

        MinesweeperButtonController controller = fxmlLoader.getController();
        controller.setPosition(x, y);
        controller.setGamefieldController(this);
        //controller.setDefaults();

        minesweeperButton.setUserData(controller);

        return minesweeperButton;
    }

    //setzt die Optionen des Schwierigkeitsgrads. zB Beginners, Pro, etc...
    public void setDropDownDifficulties() {
        ObservableList<String> options = FXCollections.observableArrayList();
        options.add("Beginners");
        options.add("Advanced");
        options.add("Pro");

        difficulties.setItems(options);
    }

    //setzt die Variablen gridLength, gridWidth, die Anzahl der Bomben und Anzahl der Flaggen basierend auf den Schwierigkeitsgrad
    public void setDifficultyDefaults() {
        setDefaults();

        switch (getDifficulties().getValue()) {
            case "Beginners":
                setGridLength(8);
                setGridWidth(8);
                setNumberOfBombs(10);
                break;
            case "Advanced":
                setGridLength(16);
                setGridWidth(16);
                setNumberOfBombs(40);
                break;
            case "Pro":
                setGridLength(30);
                setGridWidth(16);
                setNumberOfBombs(99);
                break;
            default:
                break;
        }
        setNumberOfFlags(getNumberOfBombs());
    }

    @FXML
    //startet das Spiel, wenn der Start-Knopf gedrückt wird
    public void onStartClicked() throws IOException {
        if (getDifficulties().getValue() != null) {
            setDifficultyDefaults();
            setStyles();
            fillGrid();
            spreadBombs();
            setBombsNearby();

        } else {
            System.out.println("No difficulty selected");
        }
    }

    //aktualisiert die Anzahl der Flaggen und ruft gegebenenfalls checkForGameOver() auf
    public void updateFlags() {
        setNumberOfFlags(getNumberOfFlags() - 1);
        if (getNumberOfFlags() == 0) {
            checkForGameOver();
        }
    }

    //prüft, ob das Spiel vorbei ist, anhand der Anzahl der flaggen
    public void checkForGameOver() {
        int bombsIdentified = 0;
        for (int i = 0; i < getGridLength(); i++) {
            for (int j = 0; j < getGridWidth(); j++) {
                if (getMinesweeperButtonController(i, j).isBomb() && getMinesweeperButtonController(i, j).isFlagged()) {
                    bombsIdentified++;
                }
            }
        }
        if (bombsIdentified == getNumberOfBombs()) {
            gameOver(true);
        }

    }

    public void gameOver(boolean won) {
        for (int i = 0; i < getGridLength(); i++) {
            for (int j = 0; j < getGridWidth(); j++) {

                getMinesweeperButtonController(i, j).getButton().setDisable(true);

                if (won && !getMinesweeperButtonController(i, j).isBomb()) {
                    getMinesweeperButtonController(i, j).revealField();
                    System.out.println("you won!");

                } else if (!won && getMinesweeperButtonController(i, j).isBomb()) {
                    getMinesweeperButtonController(i, j).revealField();
                    System.out.println("you blew up!");
                }
            }
        }
    }

    //verteilt die Bomben zufällig auf den MinesweeperButtons
    public void spreadBombs() {
        int bombCount = 0;
        while (bombCount < getNumberOfBombs()) {

            int randomX = new Random().nextInt(getGridWidth());
            int randomY = new Random().nextInt(getGridLength());

            MinesweeperButtonController minesweeperButtonController = getMinesweeperButtonController(randomX, randomY);
            if (minesweeperButtonController != null) {
                if (!minesweeperButtonController.isBomb()) {
                    minesweeperButtonController.setBomb(true);
                    bombCount++;
                }
            } else {
                System.out.println("minesweeperButtonController is null");
            }
        }
    }

    //setzt die Anzahl der Bomben rundum eines Feldes im MinesweeperButtonController
    public void setBombsNearby() {
        for (int i = 0; i < getGridLength(); i++) {
            for (int j = 0; j < getGridWidth(); j++) {
                getMinesweeperButtonController(i, j).setBombsNearby(getBombsRoundPosition(i, j));
            }
        }
    }

    //gibt die Anzahl der Bomben rundum eines Elements(Node), das in einem Grid an der Stelle X, Y ist, zurück
    public int getBombsRoundPosition(int x, int y) {
        int bombCounter = 0;

        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                MinesweeperButtonController controller = getMinesweeperButtonController(x + i, y + j);
                if (controller != null && controller.isBomb()) {
                    bombCounter++;
                }
            }
        }
        return bombCounter;
    }

    //gibt den MinesweeperButtonController zu dem Element das an der Stelle X, Y im Grid ist zurück
    public MinesweeperButtonController getMinesweeperButtonController(int x, int y) {
        Node node = getNodeFromGrid(x, y);
        if (node != null) {
            return (MinesweeperButtonController) node.getUserData();
        } else {
            return null;
        }
    }

    //deckt die umliegenden Felder auf. Wenn ein umliegendes Feld ebenfalls keine Bomben rundum hat(getBombsNearby() == 0) ruft sich die Methode rekursiv wieder auf
    public void revealFieldsAround(int x, int y) {
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                MinesweeperButtonController controller = getMinesweeperButtonController(x + i, y + j);
                if (controller != null) {
                    if (!controller.isRevealed()) {
                        controller.revealField();
                        if (controller.getBombsNearby() == 0) {
                            revealFieldsAround(x + i, y + j);
                        }

                    }
                }
            }
        }
    }


    //getter&setter---------------------------------------
    public GridPane getGrid() {
        return grid;
    }

    public void setGrid(GridPane grid) {
        this.grid = grid;
    }

    public ComboBox<String> getDifficulties() {
        return difficulties;
    }

    public void setDifficulties(ComboBox<String> difficulties) {
        this.difficulties = difficulties;
    }

    public int getGridLength() {
        return gridLength;
    }

    public void setGridLength(int gridLength) {
        this.gridLength = gridLength;
    }

    public int getGridWidth() {
        return gridWidth;
    }

    public void setGridWidth(int gridWidth) {
        this.gridWidth = gridWidth;
    }

    public int getNumberOfBombs() {
        return numberOfBombs;
    }

    public void setNumberOfBombs(int numberOfBombs) {
        this.numberOfBombs = numberOfBombs;
    }

    public int getNumberOfFlags() {
        return numberOfFlags;
    }

    public void setNumberOfFlags(int numberOfFlags) {
        this.numberOfFlags = numberOfFlags;
    }

    public AnchorPane getRootAnchor() {
        return rootAnchor;
    }

    public void setRootAnchor(AnchorPane rootAnchor) {
        this.rootAnchor = rootAnchor;
    }
}
