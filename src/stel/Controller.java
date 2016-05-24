package stel;

import com.amazonaws.util.IOUtils;
import com.sun.istack.internal.logging.Logger;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.*;


import javafx.scene.control.Label;


public class Controller  {


    @FXML private Label pathLabel;
    @FXML private Label listingObjectsLabel;
    @FXML private TableView<ListOnlineItems> tableView;
    @FXML private TableColumn<ListOnlineItems, String> filenameColumn;
    @FXML private TableColumn<ListOnlineItems, String> sizeColumn;
    @FXML private TableColumn<ListOnlineItems, String> ownerColumn;
    @FXML private Button downloadButton;
    @FXML private Button deleteButton;
    @FXML private Button uploadFileButton;
    private String filePath;
    private static Stage primaryStage;
    private File file;
    private AwsUploader aws;
    private Thread awsUploadThread;
    private boolean uploadThreadRunning;
    private ObservableList<ListOnlineItems> data;
    private DeleteAwsObject awsobj;
    private ListBucketItems bucketItems;


    public static void setStage(Stage passedStage) throws IOException {
        primaryStage = passedStage;
    }

    public void initialize() {

        uploadThreadRunning = false;
        filenameColumn.setCellValueFactory(new PropertyValueFactory<>("fileName"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<>("fileSize"));
        ownerColumn.setCellValueFactory(new PropertyValueFactory<>("fileOwner"));
        downloadButton.setDisable(true);
        deleteButton.setDisable(true);
        uploadFileButton.setDisable(true);

    }

    @FXML public void getFilePath(ActionEvent event) {
       final FileChooser fileChooser = new FileChooser();
       file = fileChooser.showOpenDialog(primaryStage);
       if (file != null) {
           uploadFileButton.setDisable(false);
            filePath = file.getAbsolutePath();
            pathLabel.setText(filePath);
        }
    }


    @FXML public void submitAction(ActionEvent event) throws InterruptedException, IOException {
        aws = new AwsUploader();
        awsUploadThread = new Thread(aws);
        aws.setFileName(file);
        awsUploadThread.start();
        uploadThreadRunning = true;

    }

    @FXML public void killApplication(ActionEvent event) {

        if (uploadThreadRunning)
            awsUploadThread.interrupt();
        System.exit(0);

    }


    @FXML public void showItemsAction(ActionEvent event) throws IOException {

        listingObjectsLabel.setText("Listing Objects...");
        listBucketItems();

    }

    @FXML public void deleteAction(ActionEvent event) throws IOException {

        awsobj = new DeleteAwsObject();
        String filename = String.valueOf(tableView.getSelectionModel().getSelectedItem().getFileName());
        awsobj.setKeyName(filename);
        awsobj.deleteObject();
        listingObjectsLabel.setText("Refresing...");
        listBucketItems();

    }

    @FXML public void settingsMenuAction(ActionEvent event) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("settings.fxml"));
        Parent root1 = (Parent)fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("Settings");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("icon.png")));
        stage.setScene(new Scene(root1));
        stage.setResizable(false);
        stage.show();

    }

    @FXML public void aboutAction(ActionEvent event) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("about.fxml"));
        Parent root2 = (Parent)fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("About");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("icon.png")));
        stage.setScene(new Scene(root2));
        stage.setResizable(false);
        stage.show();

    }

    public String extension(String filename) {
        int dot = filename.lastIndexOf('.');
        return filename.substring(dot);
    }

    @FXML public void downloadAction(ActionEvent event) throws IOException {

            String filename = String.valueOf(tableView.getSelectionModel().getSelectedItem().getFileName());
            FileChooser fileChooser = new FileChooser();
            DownloadAwsObject aws = new DownloadAwsObject();

            fileChooser.setInitialFileName(filename);

            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("*" + extension(filename),"*" + extension(filename));
            fileChooser.getExtensionFilters().addAll(extFilter);

            File filepath = fileChooser.showSaveDialog(primaryStage);
            aws.downloadObject(filepath,filename);

    }


    private void listBucketItems() throws IOException {

        bucketItems = new ListBucketItems("awsdowup");
        bucketItems.listItems();
        data = bucketItems.getData();
        tableView.setItems(data);

        if(!tableView.getItems().isEmpty()) {

            downloadButton.setDisable(false);
            deleteButton.setDisable(false);
        }
        else {
            downloadButton.setDisable(true);
            deleteButton.setDisable(true);
        }
    }

}
