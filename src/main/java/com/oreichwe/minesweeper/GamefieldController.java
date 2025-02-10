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
    private int numberOfMines;


    @FXML
    private void initialize() {
        setDropDownDifficulties();
    }


    //erstellt das Grid das später mit Buttons gefüllt wird
    public void createGrid() {
        System.out.println("createGrid()");

        getGrid().getChildren().clear();

        for (int i = 0; i < getGridLength(); i++) {
            getGrid().addRow(i);
            for (int j = 0; j < getGridWidth(); j++) {
                getGrid().addColumn(j);
            }
        }
    }


    //füllt das Grid entsprechend dem Schwierigkeitsgrad
    public void fillGrid() throws IOException {
        System.out.println("fillGrid()");

        for (int i = 0; i < getGridLength(); i++) {
            for (int j = 0; j < getGridWidth(); j++) {

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("minesweeperButton-view.fxml"));
                Node minesweeperButton = fxmlLoader.load();

                MinesweeperButtonController controller = fxmlLoader.getController();
                controller.setPosition(i, j);
                controller.setGamefieldController(this);

                minesweeperButton.setUserData(controller);

                getGrid().add(minesweeperButton, i, j);
            }
        }
    }


    //leert das Grid wieder
    public void clearGrid() {
        System.out.println("clearGrid()");
        grid.getChildren().clear();
    }


    @FXML
    //setzt die Variablen gridLength, gridWidth und die Anzahl der Minen
    public void onDifficultySelected() {
        System.out.println("public void setDifficulty()");
        System.out.println("Difficulty: " + getDifficulties().getValue());

        clearGrid();

        switch (getDifficulties().getValue()) {
            case "Beginners":
                setGridLength(8);
                setGridWidth(8);
                setNumberOfMines(10);
                break;
            case "Advanced":
                setGridLength(16);
                setGridWidth(16);
                setNumberOfMines(40);
                break;
            case "Pro":
                setGridLength(30);
                setGridWidth(16);
                setNumberOfMines(99);

        }

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
    //startet das Spiel, wenn der Start-Knopf gedrückt wird
    public void onStartClicked() throws IOException {
        System.out.println("startClicked()");

        if (getDifficulties().getValue() != null) {
            fillGrid();
            spreadBombs();
        } else {
            System.out.println("No difficulty selected");
        }
    }


    //verteilt die Bomben zufällig auf den MinesweeperButtons
    public void spreadBombs() {
        System.out.println("spreadBombs()");

        int bombCount = 0;

        while (bombCount < getNumberOfMines()) {

            int randomX = new Random().nextInt(getGridWidth());
            int randomY = new Random().nextInt(getGridLength());

            MinesweeperButtonController minesweeperButtonController = getMinesweeperButtonController(randomX, randomY);
            if (minesweeperButtonController != null) {
                if (!minesweeperButtonController.isBomb()) {
                    minesweeperButtonController.setBomb(true);
                    bombCount++;
                }
            }else{
                System.out.println("minesweeperButtonController is null");
            }
        }
    }


    //gibt das Element(Node) das in einem Grid an den stellen X,Y steht
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


    //Testcode von ChatGPT
    /*    public Node getNodeFromGrid(int row, int col) {
        System.out.println("getNodeFromGrid() called with x=" + row + " y=" + col);

        for (Node node : getGrid().getChildren()) {
            Integer rowIndex = getGrid().getRowIndex(node);
            Integer colIndex = getGrid().getColumnIndex(node);

            System.out.println("Checking node: " + node);
            System.out.println("Row: " + rowIndex + ", Column: " + colIndex);

            if (rowIndex != null && colIndex != null && rowIndex == row && colIndex == col) {
                System.out.println("✅ Found node at (" + row + "," + col + ")");
                return node;
            }
        }

        System.out.println("❌ No node found at (" + row + "," + col + ")");
        return null;
    }

 */



    //gibt den MinesweeperButtonController zu dem Element das an der Stelle X,Y im Grid ist zurück
    public MinesweeperButtonController getMinesweeperButtonController(int x, int y) {
        System.out.println("getMinesweeperButtonController()");

        Node node = getNodeFromGrid(x, y);
        return (MinesweeperButtonController) node.getUserData();
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

    public int getNumberOfMines() {
        return numberOfMines;
    }

    public void setNumberOfMines(int numberOfMines) {
        this.numberOfMines = numberOfMines;
    }


}
