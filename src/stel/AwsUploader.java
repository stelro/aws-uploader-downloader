package stel;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.event.ProgressEvent;
import com.amazonaws.event.ProgressListener;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;

import java.io.File;


public class AwsUploader extends Thread {

    private static String bucketName = "awsdowup";
    private BasicAWSCredentials awsCreds;
    private File file;


    public void setFileName(File passedFile) {
        file = passedFile;
    }

    public void uploadFile() throws InterruptedException {


        TransferManager tr = new TransferManager(new ProfileCredentialsProvider());
        Upload upload = tr.upload(bucketName, file.getName(), file);


        upload.addProgressListener(new ProgressListener() {
            // This method is called periodically as your transfer progresses
            public void progressChanged(ProgressEvent progressEvent) {

                MainModel.getInstance().textOnArea(Math.round(upload.getProgress().getPercentTransferred()) + "%");

                if (progressEvent.getEventCode() == ProgressEvent.COMPLETED_EVENT_CODE) {
                    MainModel.getInstance().textOnArea("Upload complete!!!");
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

}
