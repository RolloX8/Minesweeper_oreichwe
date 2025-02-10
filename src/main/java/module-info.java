module com.oreichwe.minesweeper {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens com.oreichwe.minesweeper to javafx.fxml;
    exports com.oreichwe.minesweeper;
}