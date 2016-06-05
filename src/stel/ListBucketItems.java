package stel;


import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;

import java.io.IOException;


public class ListBucketItems implements Runnable {

    private static String bucketName;
    private ObservableList<ListOnlineItems> data;
    private Button downloadButton;
    private Button deleteButton;
    private Thread thread;

    ListBucketItems(String passedBucketName) {
        bucketName = passedBucketName;
        data = FXCollections.observableArrayList();
    }

    public void listItems() throws IOException, InterruptedException {


        AmazonS3 s3client = new AmazonS3Client(new ProfileCredentialsProvider());

        try {



            final ListObjectsV2Request req = new ListObjectsV2Request().withBucketName(bucketName).withMaxKeys(2);
            ListObjectsV2Result result;

            do {


                result = s3client.listObjectsV2(req);

                for (S3ObjectSummary objectSummary :
                        result.getObjectSummaries()) {

                    data.add(new ListOnlineItems(objectSummary.getKey(),String.valueOf(objectSummary.getSize() + " Bytes"),String.valueOf(objectSummary.getLastModified())));
                }

                req.setContinuationToken(result.getNextContinuationToken());


            } while(result.isTruncated());

            if (data.isEmpty()) {
                downloadButton.setDisable(true);
                deleteButton.setDisable(true);
                MainModel.getInstance().print("Bucket is empty!");
            } else {
                downloadButton.setDisable(false);
                deleteButton.setDisable(false);
                MainModel.getInstance().print("All Objects Loaded");
            }

        } catch (AmazonServiceException ase) {
            MainModel.getInstance().print("Caught an AmazonServiceException, " +
                    "which means your request made it " +
                    "to Amazon S3, but was rejected with an error response " +
                    "for some reason.");
            MainModel.getInstance().print("Error Message:    " + ase.getMessage());
            MainModel.getInstance().print("HTTP Status Code: " + ase.getStatusCode());
            MainModel.getInstance().print("AWS Error Code:   " + ase.getErrorCode());
            MainModel.getInstance().print("Error Type:       " + ase.getErrorType());
            MainModel.getInstance().print("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            MainModel.getInstance().print("Caught an AmazonClientException, " +
                    "which means the client encountered " +
                    "an internal error while trying to communicate" +
                    " with S3, " +
                    "such as not being able to access the network.");
            MainModel.getInstance().print("Error Message: " + ace.getMessage());
        }
    }

    public ObservableList<ListOnlineItems> getData() {
        return data;
    }

    public void run() {

        try {
            listItems();
            Thread.sleep(50);
        } catch (IOException e) {

        } catch (InterruptedException e) {

        }
    }

    public void start() {
        thread = new Thread(this);
        thread.start();
    }

    public void  setDownloadButton(Button passedButton) {
        downloadButton = passedButton;
    }

    public void setDeleteButton(Button passedButton) {
        deleteButton = passedButton;
    }

}
