package com.oreichwe.minesweeper;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;

public class MinesweeperButtonController {

    private boolean bomb;
    private boolean flagged;
    private boolean revealed;
    private int bombsNearby;
    @FXML
    private Button button;
    @FXML
    private Label label;

    //wird aufgerufen, wenn der Button angeklickt wird und gibt dann weiter auf Rechts bzw Linksklick
    public void onButtonClicked() {

        getButton().setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                onButtonClickedPRIMARY();
            } else if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                onButtonClickedSECUNDARY();
            }
        });
    }

    //wird von onButtonClicked() aufgerufen, beihaltet die Abläufe, wenn ein Button linksgeklickt wird
    public void onButtonClickedPRIMARY() {
        if (!isFlagged()) {
            setRevealed(true);
            getButton().setVisible(false);
        }
    }

    //wird von onButtonClicked() aufgerufen, beihaltet die Abläufe, wenn ein Button rechtsgeklickt wird
    public void onButtonClickedSECUNDARY() {
        setFlagged(!isFlagged());
    }

    //setzt das Feld hinter dem Button auf bomb oder auf die Anzahl der Bomben
    public void setLabelBehindButton(){
        if(bomb){
            getLabel().setText("bomb");
        } else {
            getLabel().setText(String.valueOf(getBombsNearby()));
        }
    }





    //Getter & Setter -------------------------------------------

    public boolean isBomb() {
        return bomb;
    }

    public void setBomb(boolean bomb) {
        this.bomb = bomb;
    }

    public boolean isFlagged() {
        return flagged;
    }

    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }

    public boolean isRevealed() {
        return revealed;
    }

    public void setRevealed(boolean revealed) {
        this.revealed = revealed;
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    public int getBombsNearby() {




        return bombsNearby;
    }

    public void setBombsNearby(int bombsNearby) {
        this.bombsNearby = bombsNearby;
    }

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }
}
