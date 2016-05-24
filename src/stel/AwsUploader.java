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
    private long progressStatus;


    public void setFileName(File passedFile) {
        file = passedFile;
    }

    public void uploadFile() throws InterruptedException {

        //awsCreds = new BasicAWSCredentials("AKIAIMRXS7J2TGSJR6CQ","51W3QxeKy8Xg42PiQeP4PU/VmL6u502bJHcefh3l");
        TransferManager tr = new TransferManager(new ProfileCredentialsProvider());
        Upload upload = tr.upload(bucketName, file.getName(), file);


        upload.addProgressListener(new ProgressListener() {
            // This method is called periodically as your transfer progresses
            public void progressChanged(ProgressEvent progressEvent) {

                System.out.println(upload.getProgress().getPercentTransferred() + "%");

                progressStatus = upload.getProgress().getBytesTransferred();



                if (progressEvent.getEventCode() == ProgressEvent.COMPLETED_EVENT_CODE) {
                    System.out.println("Upload complete!!!");
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
