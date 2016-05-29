package stel;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
    private static String OS = System.getProperty("os.name").toLowerCase();
    java.util.Properties properties = System.getProperties();




    public void initialize() {



        if (isWindows()) {

            path = "C:" + File.separator + "Users" + File.separator + System.getProperty("user.name") + File.separator + ".aws" +
                    File.separator + "credentials";
            labelFile.setText("win");

        } else if (isUnix()) {

            path =  properties.get("user.home").toString() + properties.get("file.separator").toString()  + ".aws" +
                    properties.get("file.separator").toString()  + "credentials";


        }

         file = new File(path);

        if(file.exists() && !file.isDirectory()) {
            //labelFile.setText("Credential Exists");
            MainModel.getInstance().setText("Cred Exsist");


        } else {
            labelFile.setText("File Not Found");
            MainModel.getInstance().setText("Cred Exsist");
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

    public static boolean isWindows() {
        return (OS.indexOf("win") >= 0);
    }

    public static boolean isUnix() {
        return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 );
    }

}
