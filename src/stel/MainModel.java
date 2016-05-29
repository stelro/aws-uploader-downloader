package stel;


import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TextArea;

public class MainModel {

    private final static MainModel instance = new MainModel();
    private SimpleStringProperty text = new SimpleStringProperty("");
    private TextArea textArea;

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

    public void textOnArea(String text) {
        textArea.setWrapText(true);
        textArea.setPrefRowCount(10);
        textArea.appendText(text + " \n");
    }

    @Override
    public String toString() {
        return getText();
    }




}
