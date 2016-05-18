package sample;

import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import javafx.scene.control.Label;

public class Controller {

    @FXML
    private Label pathLabel;
    private String filePath;
    private static Stage stage;
    File file;



    public static  void setStage(Stage passedStage) throws IOException {
        stage = passedStage;
    }


    public void getFilePath(ActionEvent event) {
       final FileChooser fileChooser = new FileChooser();
       file = fileChooser.showOpenDialog(stage);
       if (file != null) {
            filePath = file.getAbsolutePath();
            pathLabel.setText(filePath);
        }
    }

    public void submitAction(ActionEvent event) {

        AwsUploader aws = new AwsUploader("awsdowup");
        aws.uploadFile(file);

    }

}
