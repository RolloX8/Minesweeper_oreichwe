package com.oreichwe.minesweeper;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class MinesweeperButtonController {

    private boolean bomb;
    private boolean flagged;
    private boolean revealed;
    private int bombsNearby;
    private int positionX;
    private int positionY;
    private GamefieldController gamefieldController;
    @FXML
    private Button button;
    @FXML
    private Label label;

    public void initialize() {
        button.setFocusTraversable(false);
        setDefaults();
    }

    public void setDefaults(){
        setBomb(false);
        setFlagged(false);
        setRevealed(false);
        setBombsNearby(0);
    }


    //wird aufgerufen, wenn der Button angeklickt wird und gibt dann weiter auf Rechts bzw Linksklick
    public void onButtonClicked(MouseEvent mouseEvent) {
        System.out.println("onButtonClicked()");

        if (mouseEvent.getButton() == MouseButton.PRIMARY) {
            onButtonClickedPRIMARY();
        } else if (mouseEvent.getButton() == MouseButton.SECONDARY) {
            onButtonClickedSECUNDARY();
        }

    }


    //wird von onButtonClicked() aufgerufen, beihaltet die Abläufe, wenn ein Button linksgeklickt wird
    public void onButtonClickedPRIMARY() {
        System.out.println("onButtonClickedPRIMARY()");
        if (!isFlagged()) {
            setRevealed(true);
            getButton().setVisible(false);
            if (isBomb()) {
                getGamefieldController().gameOver();
            }
        }
    }


    //wird von onButtonClicked() aufgerufen, beihaltet die Abläufe, wenn ein Button rechtsgeklickt wird
    public void onButtonClickedSECUNDARY() {
        System.out.println("onButtonClickedSECUNDARY()");


        if (!isFlagged()) {
            setFlagged(true);
            getButton().setText("flag");
            getGamefieldController().updateFlags();

        } else {
            setFlagged(false);
            getButton().setText("");
            getGamefieldController().setNumberOfFlags(getGamefieldController().getNumberOfFlags() + 1);
        }
    }


    //setzt das Feld hinter dem Button auf bomb oder auf die Anzahl der Bomben
    public void setLabelBehindButton() {
        System.out.println("setLabelBehindButton()");
        if (isBomb()) {
            getLabel().setText("bomb");
        } else {
            getLabel().setText(String.valueOf(getBombsNearby()));
        }
    }


    //speichert die Position des MinesweeperButtons im Grid
    public void setPosition(int x, int y) {
        System.out.println("setPosition(" + x + "," + y + ")");
        setPositionX(x);
        setPositionY(y);
    }


    //Getter & Setter -------------------------------------------
    public boolean isBomb() {
        return bomb;
    }

    public void setBomb(boolean bomb) {
        this.bomb = bomb;
        setLabelBehindButton();
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
        setLabelBehindButton();
    }

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public GamefieldController getGamefieldController() {
        return gamefieldController;
    }

    public void setGamefieldController(GamefieldController gamefieldController) {
        this.gamefieldController = gamefieldController;
    }
}
