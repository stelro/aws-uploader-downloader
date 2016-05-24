package stel;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;


public class SettingsController {

    @FXML private Button closeButton;
    @FXML private PasswordField accessKeyField;
    @FXML private PasswordField securityKeyField;
    @FXML private Label labelFile;
    private String path;
    private File file;

    public void initialize() {

         path = "C:" + File.separator + "Users" + File.separator + System.getProperty("user.name") + File.separator + ".aws" +
                File.separator + "credentials ";

         file = new File(path);


        if(file.exists() && !file.isDirectory()) {
            labelFile.setText("Credential Exists");
        } else {
            labelFile.setText("File Not Found");
        }

    }

    @FXML public void closeAction(ActionEvent event) {

        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();

    }

    @FXML public void applyAction(ActionEvent event) throws IOException {

        file.getParentFile().mkdirs();
        file.createNewFile();

        PrintWriter writer = new PrintWriter(path, "UTF-8");
        writer.println("[default]");
        writer.println("aws_access_key_id = " + accessKeyField.getText());
        writer.println("aws_secret_access_key = " + securityKeyField.getText());
        writer.close();
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();


    }

}
