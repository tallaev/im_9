module ru.vorotov.simulationslab9 {
    requires javafx.controls;
    requires javafx.fxml;


    opens ru.vorotov.simulationslab9 to javafx.fxml;
    exports ru.vorotov.simulationslab9;
}