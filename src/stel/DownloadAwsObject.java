package stel;

import java.io.*;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;


public class DownloadAwsObject {

    private static String bucketName = "awsdowup";


    public void downloadObject(File filepath,String filename) throws IOException {
        AmazonS3 s3client = new AmazonS3Client(new ProfileCredentialsProvider());
        try {

            MainModel.getInstance().textOnArea("Downloading an object");

            s3client.getObject(new GetObjectRequest(bucketName, filename),
                    new File(String.valueOf(filepath)));


        } catch (AmazonServiceException ase) {
            MainModel.getInstance().textOnArea("Caught an AmazonServiceException, which" +
                    " means your request made it " +
                    "to Amazon S3, but was rejected with an error response" +
                    " for some reason.");
            MainModel.getInstance().textOnArea("Error Message:    " + ase.getMessage());
            MainModel.getInstance().textOnArea("HTTP Status Code: " + ase.getStatusCode());
            MainModel.getInstance().textOnArea("AWS Error Code:   " + ase.getErrorCode());
            MainModel.getInstance().textOnArea("Error Type:       " + ase.getErrorType());
            MainModel.getInstance().textOnArea("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            MainModel.getInstance().textOnArea("Caught an AmazonClientException, which means"+
                    " the client encountered " +
                    "an internal error while trying to " +
                    "communicate with S3, " +
                    "such as not being able to access the network.");
            MainModel.getInstance().textOnArea("Error Message: " + ace.getMessage());
        }
    }

    private static void displayTextInputStream(InputStream input)
            throws IOException {
        // Read one text line at a time and display.
        BufferedReader reader = new BufferedReader(new
                InputStreamReader(input));
        while (true) {
            String line = reader.readLine();
            if (line == null) break;

            System.out.println("    " + line);
        }
        System.out.println();
    }

}
