package stel;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.event.ProgressEvent;
import com.amazonaws.event.ProgressListener;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;

import java.io.File;


public class AwsUploader implements Runnable {


    private File file;
    private Thread thread;


    public void setFileName(File passedFile) {
        file = passedFile;
    }

    public void uploadFile() throws InterruptedException {


        TransferManager tr = new TransferManager(new ProfileCredentialsProvider());
        Upload upload = tr.upload(MainModel.getInstance().getBucketName(), file.getName(), file);


        upload.addProgressListener(new ProgressListener() {
            // This method is called periodically as your transfer progresses
            public void progressChanged(ProgressEvent progressEvent) {

                MainModel.getInstance().print(Math.round(upload.getProgress().getPercentTransferred()) + " %");

                if (progressEvent.getEventCode() == ProgressEvent.COMPLETED_EVENT_CODE) {
                    MainModel.getInstance().print("Upload complete!!!");
                    //thread.interrupt();
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

}
