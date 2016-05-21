package stel;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;


public class SettingsController {

    @FXML private Button closeButton;

    @FXML public void closeAction(ActionEvent event) {

        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();

    }

    @FXML public void applyAction(ActionEvent event) {

    }

}
