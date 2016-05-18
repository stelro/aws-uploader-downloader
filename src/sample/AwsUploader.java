package sample;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.IOException;

public class AwsUploader extends Thread {

    private static String bucketName; //= "awsdowup";
    private static String keyName = "AKIAILD6GOHVWNDH5XTA";
    private BasicAWSCredentials awsCreds;

    AwsUploader(String passedBucketName) {
        bucketName = passedBucketName;
    }

    public void uploadFile(File passedFile) throws IOException {

        awsCreds = new BasicAWSCredentials("AKIAIMRXS7J2TGSJR6CQ","51W3QxeKy8Xg42PiQeP4PU/VmL6u502bJHcefh3l");
        AmazonS3 s3client = new AmazonS3Client(awsCreds);

        try {

            System.out.println("Uploading a new object from a file");
            s3client.putObject(new PutObjectRequest(
                    bucketName,keyName,passedFile));


        } catch (AmazonServiceException ase) {

            System.out.println("Caught an AmazonServiceException, which " +
                    "means your request made it " +
                    "to Amazon S3, but was rejected with an error response" +
                    " for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());

        }catch (AmazonClientException ace) {

            System.out.println("Caught an AmazonClientException, which " +
                    "means the client encountered " +
                    "an internal error while trying to " +
                    "communicate with S3, " +
                    "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }

    }

    public void run() {

    }

}
