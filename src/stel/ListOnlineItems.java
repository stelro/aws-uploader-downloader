package stel;


import javafx.beans.property.SimpleStringProperty;

public  class ListOnlineItems {

    private final SimpleStringProperty fileName;
    private final SimpleStringProperty fileSize;
    private final SimpleStringProperty fileOwner;

    public ListOnlineItems(String fName, String fSize, String fOwner) {
        this.fileName = new SimpleStringProperty(fName);
        this.fileSize = new SimpleStringProperty(fSize);
        this.fileOwner = new SimpleStringProperty(fOwner);
    }

    public String getFileName() {
        return fileName.get();
    }

    public String getFileSize() {
        return fileSize.get();
    }

    public String getFileOwner() {
        return fileOwner.get();
    }

    public void setFileName(String fName) {
        fileName.set(fName);
    }

    public void setFileSize(String fSize) {
        fileSize.set(fSize);
    }

    public void setFileOwner(String fOwner) {
        fileOwner.set(fOwner);
    }

}

