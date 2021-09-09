package dao;

import android.content.Context;

import com.amazonaws.mobileconnectors.s3.transfermanager.TransferManager;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3URI;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import model.Item;

public class S3BasedPayloadDao {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private AmazonS3 s3Client;

    public S3BasedPayloadDao(AmazonS3 s3Client, Context context) {
        this.s3Client = s3Client;
        s3Client.setRegion(Region.getRegion(Regions.US_EAST_1));

    }

    public ObjectMetadata readFileMetadata(String bucketName, String key) throws URISyntaxException {
        URI fileToBeDownloaded = new URI("https://fetch-hiring.s3.amazonaws.com/hiring.json");
        AmazonS3URI s3URI = new AmazonS3URI(fileToBeDownloaded);
        return s3Client.getObjectMetadata(s3URI.getBucket(), s3URI.getKey());
    }

    public List<Item> readData(String bucketName, String key) throws IOException, URISyntaxException {
        URI fileToBeDownloaded = new URI("https://fetch-hiring.s3.amazonaws.com/hiring.json");
        AmazonS3URI s3URI = new AmazonS3URI(fileToBeDownloaded);


        S3ObjectInputStream s3ObjectInputStream = s3Client.getObject(s3URI.getBucket(), s3URI.getKey()).getObjectContent();
        return MAPPER.readValue(s3ObjectInputStream, new TypeReference<ArrayList<Item>>(){});
    }
}
