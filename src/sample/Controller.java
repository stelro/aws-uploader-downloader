package sample;


import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import javafx.scene.control.Label;

public class Controller  {



    @FXML
    private Label pathLabel;
    private String filePath;
    private static Stage stage;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Label label2;
    private File file;
    private long someval;
    private AwsUploader aws;
    private Thread awsThread;




    public static void setStage(Stage passedStage) throws IOException {
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



    public void submitAction(ActionEvent event) throws InterruptedException {
        aws = new AwsUploader();
        awsThread = new Thread(aws);
        aws.setFileName(file);
        awsThread.start();

    }

    public void killApplication(ActionEvent event) {
        awsThread.interrupt();
        System.exit(0);

    }

}
