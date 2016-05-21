package stel;

import java.io.*;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;


public class DownloadAwsObject {

    private static String bucketName = "awsdowup";
    private static String key        = "PersonList.xls";

    S3ObjectInputStream ogogo;

    BasicAWSCredentials awsCreds = new BasicAWSCredentials("AKIAIMRXS7J2TGSJR6CQ","51W3QxeKy8Xg42PiQeP4PU/VmL6u502bJHcefh3l");

    public S3ObjectInputStream getFile() {

        return ogogo;

    }

    public void downloadObject(File filepath,String filename) throws IOException {
        AmazonS3 s3client = new AmazonS3Client(new BasicAWSCredentials("AKIAIMRXS7J2TGSJR6CQ","51W3QxeKy8Xg42PiQeP4PU/VmL6u502bJHcefh3l"));
        try {
            System.out.println("Downloading an object");
            S3Object s3object = s3client.getObject(new GetObjectRequest(
                    bucketName, filename));
            System.out.println("Content-Type: "  +
                    s3object.getObjectMetadata().getContentType());
            displayTextInputStream(s3object.getObjectContent());

            // Get a range of bytes from an object.

            GetObjectRequest rangeObjectRequest = new GetObjectRequest(
                    bucketName, filename);

//            rangeObjectRequest.setRange(0, 10);
//            S3Object objectPortion = s3client.getObject(rangeObjectRequest);
//
//            System.out.println("Printing bytes retrieved.");
//            displayTextInputStream(objectPortion.getObjectContent());

//            ogogo = s3object.getObjectContent();
//            IOUtils.copy(ogogo, new FileOutputStream(filepath));

            s3client.getObject(new GetObjectRequest(bucketName, filename),
                    new File(String.valueOf(filepath)));

        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which" +
                    " means your request made it " +
                    "to Amazon S3, but was rejected with an error response" +
                    " for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which means"+
                    " the client encountered " +
                    "an internal error while trying to " +
                    "communicate with S3, " +
                    "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
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
