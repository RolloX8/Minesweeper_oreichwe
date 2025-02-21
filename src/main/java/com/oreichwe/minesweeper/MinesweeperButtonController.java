package com.oreichwe.minesweeper;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.util.Objects;

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
    private ImageView mineImage;
    @FXML
    private AnchorPane MineCellAnchor;
    @FXML
    private ImageView flagImage;


    public void initialize() {
        getButton().setFocusTraversable(false);
        getButton().setDisable(false);
        setDefaults();
    }

    //setzt die Werte auf Default, die dann überschrieben werden
    public void setDefaults() {
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
            revealField();
            if (isBomb()) {
                setMineImage("/img/mine2_exploded.png");
                getGamefieldController().gameOver(false);
            } else if (getBombsNearby() == 0) {
                getGamefieldController().revealFieldsAround(getPositionX(), getPositionY());
            }
        }
    }

    //deckt das Feld auf
    public void revealField() {
        setRevealed(true);
        getButton().setVisible(false);
    }

    //wird von onButtonClicked() aufgerufen, beihaltet die Abläufe, wenn ein Button rechtsgeklickt wird
    public void onButtonClickedSECUNDARY() {
        System.out.println("onButtonClickedSECUNDARY()");


        if (!isFlagged()) {
            setFlagged(true);
            setFlagImage("/img/falg.png");
            getGamefieldController().updateFlags();

        } else {
            setFlagged(false);
            getButton().setText("");
            getGamefieldController().setNumberOfFlags(getGamefieldController().getNumberOfFlags() + 1);
        }
    }

    //setzt das Feld hinter dem Button auf bomb oder auf die Anzahl der Bomben
    public void setImageBehindButton() {
        if (isBomb()) {
            setMineImage("/img/mine2.png");
        } else {
            if (getBombsNearby() != 0) {
                setMineImage("/img/" + getBombsNearby() + ".png");
            }
        }
    }

    //speichert die Position des MinesweeperButtons im Grid
    public void setPosition(int x, int y) {
        System.out.println("setPosition(" + x + "," + y + ")");
        setPositionX(x);
        setPositionY(y);
    }

    public void setMineImage(String path) {
        getMineImage().setImage(getImage(path));
        getMineImage().setFitHeight(36);
        getMineImage().setFitWidth(36);
    }

    public void setFlagImage(String path) {
        getFlagImage().setImage(getImage(path));
        getFlagImage().setFitHeight(30);
        getFlagImage().setFitWidth(30);
    }

    public Image getImage(String path){
        return new Image(Objects.requireNonNull(getClass().getResource(path)).toExternalForm());
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
        setImageBehindButton();
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

    public ImageView getMineImage() {
        return mineImage;
    }

    public void setMineImage(ImageView mineImage) {
        this.mineImage = mineImage;
    }

    public AnchorPane getMineCellAnchor() {
        return MineCellAnchor;
    }

    public void setMineCellAnchor(AnchorPane mineCellAnchor) {
        MineCellAnchor = mineCellAnchor;
    }

    public ImageView getFlagImage() {
        return flagImage;
    }

    public void setFlagImage(ImageView flagImage) {
        this.flagImage = flagImage;
    }
}
