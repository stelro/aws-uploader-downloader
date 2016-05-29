package stel;


import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;

import java.io.IOException;

public class DeleteAwsObject {

    private static String bucketName = "awsdowup";
    private static String keyName;

    public void setKeyName(String fName) {
        keyName = fName;
    }

    public void deleteObject() throws IOException {
        AmazonS3 s3Client = new AmazonS3Client(new ProfileCredentialsProvider());
        try {
            s3Client.deleteObject(new DeleteObjectRequest(bucketName, keyName));
            MainModel.getInstance().textOnArea(keyName + " Object Deleted.");
        } catch (AmazonServiceException ase) {
            MainModel.getInstance().textOnArea("Caught an AmazonServiceException.");
            MainModel.getInstance().textOnArea("Error Message:    " + ase.getMessage());
            MainModel.getInstance().textOnArea("HTTP Status Code: " + ase.getStatusCode());
            MainModel.getInstance().textOnArea("AWS Error Code:   " + ase.getErrorCode());
            MainModel.getInstance().textOnArea("Error Type:       " + ase.getErrorType());
            MainModel.getInstance().textOnArea("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            MainModel.getInstance().textOnArea("Caught an AmazonClientException.");
            MainModel.getInstance().textOnArea("Error Message: " + ace.getMessage());
        }
    }
}
