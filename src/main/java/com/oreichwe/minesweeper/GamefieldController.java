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
    private FXMLLoader loader = new FXMLLoader(getClass().getResource("minesweeperButton-view.fxml"));
    private MinesweeperButtonController minesweeperButtonController = loader.getController();


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
        System.out.println(getDifficulties().getValue());

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
        System.out.println("public void setDropDownDifficulties()");

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
        } else {
            System.out.println("No difficulty selected");
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

    public int getNumberOfMines() {
        return numberOfMines;
    }

    public void setNumberOfMines(int numberOfMines) {
        this.numberOfMines = numberOfMines;
    }


}
