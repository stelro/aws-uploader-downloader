package stel;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;


public class SettingsController {

    @FXML private Button closeButton;
    @FXML private PasswordField accessKeyField;
    @FXML private PasswordField securityKeyField;
    @FXML private TextField bucketName;
    @FXML private Label labelFile;
    private String pathCredentials;
    private String pathBucket;
    private File fileCredentials;
    private File fileBucket;
    private boolean settingsExsists;
    private static String OS = System.getProperty("os.name").toLowerCase();
    java.util.Properties properties = System.getProperties();




    public void initialize() {



        if (isWindows()) {

            pathCredentials = "C:" + File.separator + "Users" + File.separator + System.getProperty("user.name") + File.separator + ".aws" +
                    File.separator + "credentials";
            pathBucket = "C:" + File.separator + "Users" + File.separator + System.getProperty("user.name") + File.separator + ".aws" +
                    File.separator + "bucketname";

            MainModel.getInstance().print("Operating System : Windows");

        } else if (isUnix()) {

            pathCredentials =  properties.get("user.home").toString() + properties.get("file.separator").toString()  + ".aws" +
                    properties.get("file.separator").toString()  + "credentials";
            pathBucket =  properties.get("user.home").toString() + properties.get("file.separator").toString()  + ".aws" +
                    properties.get("file.separator").toString()  + "bucketname";

            MainModel.getInstance().print("Operating System : Unix/Linux");


        }

        fileCredentials = new File(pathCredentials);
        fileBucket = new File(pathBucket);

        if(fileCredentials.exists() && !fileCredentials.isDirectory()) {
            MainModel.getInstance().setText("Credential Exsist");


        } else {
            MainModel.getInstance().setText("Credential Not Found");
            MainModel.getInstance().setText("You should enter new Credentials's");
        }


        if(fileBucket.exists() && !fileBucket.isDirectory()) {
            MainModel.getInstance().setText("Bucket Name Exsist");


        } else {
            MainModel.getInstance().setText("Backet Name Not Found");
            MainModel.getInstance().setText("You should enter new BucketName");
        }

    }

    @FXML public void closeAction(ActionEvent event) {

        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();

    }

    @FXML public void applyAction(ActionEvent event) throws IOException {

        fileCredentials.getParentFile().mkdirs();
        fileCredentials.createNewFile();

        PrintWriter writer = new PrintWriter(pathCredentials, "UTF-8");
        writer.println("[default]");
        writer.println("aws_access_key_id = " + accessKeyField.getText());
        writer.println("aws_secret_access_key = " + securityKeyField.getText());
        writer.close();

        PrintWriter bucketWriter = new PrintWriter(pathBucket, "UTF-8");
        bucketWriter.println(bucketName.getText());
        bucketWriter.close();

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
