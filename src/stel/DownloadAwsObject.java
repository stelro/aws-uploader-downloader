package stel;

import java.io.*;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;


public class DownloadAwsObject implements Runnable {

    private Thread thread;
    private File filepath;
    private String filename;

    public DownloadAwsObject(File passedFilePath, String passedFileName) {
        filepath = passedFilePath;
        filename = passedFileName;
    }


    public void downloadObject() throws IOException {
        AmazonS3 s3client = new AmazonS3Client(new ProfileCredentialsProvider());
        try {

            MainModel.getInstance().print("Downloading " + filename + " from " + MainModel.getInstance().getBucketName() + " bucket!");


            S3Object s3object = s3client.getObject(new GetObjectRequest(
                    MainModel.getInstance().getBucketName(), filename));

            MainModel.getInstance().print("Content-Type: "  +
                    s3object.getObjectMetadata().getContentType());

            s3client.getObject(new GetObjectRequest(MainModel.getInstance().getBucketName(), filename),
                    new File(String.valueOf(filepath)));



            MainModel.getInstance().print("Download Complete");


        } catch (AmazonServiceException ase) {
            MainModel.getInstance().print("Caught an AmazonServiceException, which" +
                    " means your request made it " +
                    "to Amazon S3, but was rejected with an error response" +
                    " for some reason.");
            MainModel.getInstance().print("Error Message:    " + ase.getMessage());
            MainModel.getInstance().print("HTTP Status Code: " + ase.getStatusCode());
            MainModel.getInstance().print("AWS Error Code:   " + ase.getErrorCode());
            MainModel.getInstance().print("Error Type:       " + ase.getErrorType());
            MainModel.getInstance().print("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            MainModel.getInstance().print("Caught an AmazonClientException, which means"+
                    " the client encountered " +
                    "an internal error while trying to " +
                    "communicate with S3, " +
                    "such as not being able to access the network.");
            MainModel.getInstance().print("Error Message: " + ace.getMessage());
        }
    }

    public void run() {

        try {
            downloadObject();
            Thread.sleep(50);
        } catch (IOException e) {

        } catch (InterruptedException e) {

        }
    }

    public void start() {
        thread = new Thread(this);
        thread.start();
    }

}
