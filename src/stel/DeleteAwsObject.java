package stel;


import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;

import java.io.IOException;

public class DeleteAwsObject implements Runnable {

    private static String keyName;
    private Thread thread;

    public void setKeyName(String fName) {
        keyName = fName;
    }

    public void deleteObject() throws IOException {
        AmazonS3 s3Client = new AmazonS3Client(new ProfileCredentialsProvider());
        try {
            s3Client.deleteObject(new DeleteObjectRequest(MainModel.getInstance().getBucketName(), keyName));
            MainModel.getInstance().print(keyName + " Object Deleted.");
        } catch (AmazonServiceException ase) {
            MainModel.getInstance().print("Caught an AmazonServiceException.");
            MainModel.getInstance().print("Error Message:    " + ase.getMessage());
            MainModel.getInstance().print("HTTP Status Code: " + ase.getStatusCode());
            MainModel.getInstance().print("AWS Error Code:   " + ase.getErrorCode());
            MainModel.getInstance().print("Error Type:       " + ase.getErrorType());
            MainModel.getInstance().print("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            MainModel.getInstance().print("Caught an AmazonClientException.");
            MainModel.getInstance().print("Error Message: " + ace.getMessage());
        }
    }

    public void run() {

        try {
            deleteObject();
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
