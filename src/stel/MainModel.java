package stel;


import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TextArea;


public class MainModel {

    private final static MainModel instance = new MainModel();
    private SimpleStringProperty text = new SimpleStringProperty("");
    private TextArea textArea;
    private SimpleStringProperty bucketName = new SimpleStringProperty("");


    public static MainModel getInstance() {
        return instance;
    }

    public String getText() {
        return text.get();
    }

    public void setText(String value) {
        text.set(value);
    }

    public void setTextArea(TextArea passedTextArea) {

        textArea = passedTextArea;

    }

    public void print(String text) {

       Platform.runLater( () -> textArea.appendText("> " + text + " \n"));
    }

    public void setBucketName(String value) {
        bucketName.set(value);
    }

    public String getBucketName() {
        return bucketName.get();
    }


    @Override
    public String toString() {
        return getText();
    }


}
