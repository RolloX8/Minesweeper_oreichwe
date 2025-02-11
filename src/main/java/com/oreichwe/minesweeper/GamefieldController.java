package com.oreichwe.minesweeper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.Random;

public class GamefieldController {
    @FXML
    private GridPane grid = new GridPane();
    @FXML
    private ComboBox<String> difficulties = new ComboBox<>();
    @FXML
    private Button startButton = new Button("Start");

    private int gridLength;
    private int gridWidth;
    private int numberOfBombs;
    private int numberOfFlags;


    @FXML
    private void initialize() {
        setDropDownDifficulties();
    }

    //füllt das Grid entsprechend dem Schwierigkeitsgrad
    public void fillGrid() throws IOException {
        System.out.println("fillGrid()");

        for (int i = 0; i < getGridLength(); i++) {
            for (int j = 0; j < getGridWidth(); j++) {
                getGrid().add(createMinesweeperButton(i, j), i, j);
            }
        }
    }

    //leert das Grid wieder
    public void clearGrid() {
        System.out.println("clearGrid()");
        grid.getChildren().clear();
    }

    //gibt das Element(Node) das in einem Grid an den stellen X, Y steht
    public Node getNodeFromGrid(int x, int y) {
        System.out.println("getNodeFromGrid()");

        Node result = null;
        for (Node node : getGrid().getChildren()) {
            if (getGrid().getRowIndex(node).equals(x) && getGrid().getColumnIndex(node).equals(y)) {
                result = node;
                break;
            }
        }
        return result;
        //NOTE@MYSELF: https://stackoverflow.com/questions/20825935/javafx-get-node-by-row-and-column
    }

    //erstellt einen MinesweeperButton
    public Node createMinesweeperButton(int x, int y) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("minesweeperButton-view.fxml"));
        Node minesweeperButton = fxmlLoader.load();

        MinesweeperButtonController controller = fxmlLoader.getController();
        controller.setPosition(x, y);
        controller.setGamefieldController(this);
        controller.setDefaults();

        minesweeperButton.setUserData(controller);

        return minesweeperButton;
    }

    //setzt die Optionen des Schwierigkeitsgrads. zB Beginners, Pro, etc...
    public void setDropDownDifficulties() {
        System.out.println("setDropDownDifficulties()");

        ObservableList<String> options = FXCollections.observableArrayList();
        options.add("Beginners");
        options.add("Advanced");
        options.add("Pro");

        difficulties.setItems(options);


    }

    @FXML
    //setzt die Variablen gridLength, gridWidth, die Anzahl der Minen und Anzahl der Flaggen
    public void onDifficultySelected() {
        System.out.println("public void setDifficulty()");
        System.out.println("Difficulty: " + getDifficulties().getValue());

        clearGrid();

        switch (getDifficulties().getValue()) {
            case "Beginners":
                setGridLength(8);
                setGridWidth(8);
                setNumberOfBombs(1);
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
        }
        setNumberOfFlags(getNumberOfBombs());
    }

    @FXML
    //startet das Spiel, wenn der Start-Knopf gedrückt wird
    public void onStartClicked() throws IOException {
        System.out.println("startClicked()");

        if (getDifficulties().getValue() != null) {
            fillGrid();
            spreadBombs();
            setBombsNearby();

        } else {
            System.out.println("No difficulty selected");
        }
    }

    //aktualisiert die Anzahl der Flaggen und ruft gegebenenfalls checkForGameOver() auf
    public void updateFlags(){
        System.out.println("updateFlags()");
        setNumberOfFlags(getNumberOfFlags() - 1);
        if(getNumberOfFlags() == 0){
            checkForGameOver();
        }
    }

    //prüft, ob das Spiel vorbei ist, anhand der Anzahl der flaggen
    public void checkForGameOver(){
        System.out.println("checkForGameOver()");
        int bombsIdentified = 0;
        for (int i = 0; i < getGridLength(); i++) {
            for (int j = 0; j < getGridWidth(); j++) {
                if(getMinesweeperButtonController(i, j).isBomb() && getMinesweeperButtonController(i,j).isFlagged()){
                    bombsIdentified++;
                }
            }
        }
        if(bombsIdentified == getNumberOfBombs()){
            gameOver();
        }

    }

    public void gameOver(){
        System.out.println("gameOver()");

        for(int i = 0; i < getGridLength(); i++){
            for(int j = 0; j < getGridWidth(); j++){
                getMinesweeperButtonController(i, j).getButton().setVisible(false);
            }
        }
    }

    //verteilt die Bomben zufällig auf den MinesweeperButtons
    public void spreadBombs() {
        System.out.println("spreadBombs()");

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
        System.out.println("bombsNearby()");

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
        System.out.println("getMinesweeperButtonController()");

        Node node = getNodeFromGrid(x, y);
        if (node != null) {
            return (MinesweeperButtonController) node.getUserData();
        } else {
            return null;
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
}
