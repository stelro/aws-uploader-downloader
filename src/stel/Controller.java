package stel;


import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;


public class Controller implements Initializable {


    @FXML private TableView<ListOnlineItems> tableView;
    @FXML private TableColumn<ListOnlineItems, String> filenameColumn;
    @FXML private TableColumn<ListOnlineItems, String> sizeColumn;
    @FXML private TableColumn<ListOnlineItems, String> ownerColumn;
    @FXML private Button downloadButton;
    @FXML private Button deleteButton;
    @FXML private Button uploadFileButton;
    @FXML private TextArea textArea;
    private String filePath;
    private static Stage primaryStage;
    private File file;
    private AwsUploader aws;
    private ObservableList<ListOnlineItems> data;
    private DeleteAwsObject awsobj;
    private ListBucketItems bucketItems;
    private String pathBucket;
    private File fileBucket;
    private static String OS = System.getProperty("os.name").toLowerCase();
    java.util.Properties properties = System.getProperties();




    public static void setStage(Stage passedStage) throws IOException {
        primaryStage = passedStage;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        filenameColumn.setCellValueFactory(new PropertyValueFactory<>("fileName"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<>("fileSize"));
        ownerColumn.setCellValueFactory(new PropertyValueFactory<>("fileOwner"));
        downloadButton.setDisable(true);
        deleteButton.setDisable(true);
        uploadFileButton.setDisable(true);
        MainModel.getInstance().setTextArea(textArea);


        if (isWindows()) {


            pathBucket = "C:" + File.separator + "Users" + File.separator + System.getProperty("user.name") + File.separator + ".aws" +
                    File.separator + "bucketname";

        } else if (isUnix()) {


            pathBucket =  properties.get("user.home").toString() + properties.get("file.separator").toString()  + ".aws" +
                    properties.get("file.separator").toString()  + "bucketname";

        }

        fileBucket = new File(pathBucket);

        if(fileBucket.exists() && !fileBucket.isDirectory()) {

            try {
                BufferedReader br = new BufferedReader(new FileReader(pathBucket));
                String line = br.readLine();
                StringBuilder sb = new StringBuilder();
                sb.append(line);

                MainModel.getInstance().setBucketName(sb.toString());
                MainModel.getInstance().print("Backet name: " + sb.toString());

            }catch (IOException e) {

            }


        } else {

            MainModel.getInstance().setText("Backet Name Not Found");
            MainModel.getInstance().setText("You should enter new BucketName");

        }


    }

    @FXML public void getFilePath(ActionEvent event) {
       final FileChooser fileChooser = new FileChooser();
       file = fileChooser.showOpenDialog(primaryStage);
       if (file != null) {
           uploadFileButton.setDisable(false);
            filePath = file.getAbsolutePath();
           printText("File to upload: " + filePath);



        }
    }

    public void printText(String text) {

        textArea.setWrapText(true);
        textArea.setPrefRowCount(10);
        textArea.appendText(text + " \n");

    }



    @FXML public void submitAction(ActionEvent event) throws InterruptedException, IOException {
        aws = new AwsUploader();
        aws.setFileName(file);
        aws.start();

    }

    @FXML public void killApplication(ActionEvent event) {

        System.exit(0);

    }


    @FXML public void showItemsAction(ActionEvent event) throws IOException {

        MainModel.getInstance().print("Listing Objects...");
        listBucketItems();

    }

    @FXML public void deleteAction(ActionEvent event) throws IOException {

        awsobj = new DeleteAwsObject();
        String filename = String.valueOf(tableView.getSelectionModel().getSelectedItem().getFileName());
        awsobj.setKeyName(filename);
        awsobj.start();
        MainModel.getInstance().print("Deleting and Refresing...");
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
        printText(MainModel.getInstance().getText());


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


            fileChooser.setInitialFileName(filename);

            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("*" + extension(filename),"*" + extension(filename));
            fileChooser.getExtensionFilters().addAll(extFilter);

            File filepath = fileChooser.showSaveDialog(primaryStage);
            DownloadAwsObject aws = new DownloadAwsObject(filepath,filename);
            aws.start();

    }


    private void listBucketItems() throws IOException {

        bucketItems = new ListBucketItems("awsdowup");
        bucketItems.setDownloadButton(downloadButton);
        bucketItems.setDeleteButton(deleteButton);
        bucketItems.start();
        data = bucketItems.getData();
        tableView.setItems(data);


    }

    public static boolean isWindows() {
        return (OS.indexOf("win") >= 0);
    }

    public static boolean isUnix() {
        return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 );
    }

}
