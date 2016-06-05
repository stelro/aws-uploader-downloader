package stel;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.event.ProgressEvent;
import com.amazonaws.event.ProgressListener;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import javafx.scene.control.Button;

import java.io.File;
import java.net.URL;


public class AwsUploader implements Runnable {


    private File file;
    private Button uploadButton;
    private Thread thread;


    public void setFileName(File passedFile) {
        file = passedFile;
    }

    public void uploadFile() throws InterruptedException {

        AmazonS3 s3client = new AmazonS3Client(new ProfileCredentialsProvider());

        TransferManager tr = new TransferManager(new ProfileCredentialsProvider());
        Upload upload = tr.upload(MainModel.getInstance().getBucketName(), file.getName(), file);

        java.util.Date expiration = new java.util.Date();
        long milliseconds = expiration.getTime();
        milliseconds += 1000 * 60 * 60; //Add 1 hour
        expiration.setTime(milliseconds);

        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(MainModel.getInstance().getBucketName(), file.getName());
        generatePresignedUrlRequest.setMethod(HttpMethod.GET);
        generatePresignedUrlRequest.setExpiration(expiration);

        URL url = s3client.generatePresignedUrl(generatePresignedUrlRequest);

        upload.addProgressListener(new ProgressListener() {
            // This method is called periodically as your transfer progresses
            public void progressChanged(ProgressEvent progressEvent) {

                MainModel.getInstance().print(Math.round(upload.getProgress().getPercentTransferred()) + " %");

                if (progressEvent.getEventCode() == ProgressEvent.COMPLETED_EVENT_CODE) {
                    MainModel.getInstance().print("Upload complete!!!");
                    MainModel.getInstance().print("URL to download the object item, url expires in 1 hour: ");
                    MainModel.getInstance().print("Url: " + url);
                    uploadButton.setDisable(true);

                }
            }
        });

        // waitForCompletion blocks the current thread until the transfer completes
        // and will throw an AmazonClientException or AmazonServiceException if
        // anything went wrong.
        upload.waitForCompletion();
    }


    public void run() {

        try {
            uploadFile();
            Thread.sleep(50);

        } catch (InterruptedException e) {

        }
    }

    public void start() {
        thread = new Thread(this);
        thread.start();
    }

    public void setUploadButton(Button passedButton) {
        uploadButton = passedButton;
    }

}
