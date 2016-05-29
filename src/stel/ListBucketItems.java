package stel;


import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;


public class ListBucketItems {

    private static String bucketName;
    private ObservableList<ListOnlineItems> data;

    ListBucketItems(String passedBucketName) {
        bucketName = passedBucketName;
        data = FXCollections.observableArrayList();
    }

    public void listItems() throws IOException {


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

            } while(result.isTruncated() == true );

        } catch (AmazonServiceException ase) {
            MainModel.getInstance().textOnArea("Caught an AmazonServiceException, " +
                    "which means your request made it " +
                    "to Amazon S3, but was rejected with an error response " +
                    "for some reason.");
            MainModel.getInstance().textOnArea("Error Message:    " + ase.getMessage());
            MainModel.getInstance().textOnArea("HTTP Status Code: " + ase.getStatusCode());
            MainModel.getInstance().textOnArea("AWS Error Code:   " + ase.getErrorCode());
            MainModel.getInstance().textOnArea("Error Type:       " + ase.getErrorType());
            MainModel.getInstance().textOnArea("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            MainModel.getInstance().textOnArea("Caught an AmazonClientException, " +
                    "which means the client encountered " +
                    "an internal error while trying to communicate" +
                    " with S3, " +
                    "such as not being able to access the network.");
            MainModel.getInstance().textOnArea("Error Message: " + ace.getMessage());
        }
    }

    public ObservableList<ListOnlineItems> getData() {
        return data;
    }


}
