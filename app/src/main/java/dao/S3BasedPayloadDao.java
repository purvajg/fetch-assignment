package dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import model.Item;

public class S3BasedPayloadDao {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public Long readFileMetadata(final String url) throws URISyntaxException, IOException {
        URI fileToBeDownloaded = new URI(url);
        HttpURLConnection connection = (HttpURLConnection) fileToBeDownloaded.toURL().openConnection();
        if(connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            long lastModified = connection.getLastModified();
            connection.getInputStream().close();
            return lastModified;
        } else {
            connection.getInputStream().close();
        }
        throw new IOException();
    }

    public List<Item> readData(final String url) throws IOException, URISyntaxException {
        URI fileToBeDownloaded = new URI(url);
        HttpURLConnection connection = (HttpURLConnection) fileToBeDownloaded.toURL().openConnection();
        if(connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            List<Item> items = MAPPER.readValue(connection.getInputStream(), new TypeReference<ArrayList<Item>>(){});
            connection.getInputStream().close();
            return items;
        } else {
            connection.getInputStream().close();
        }
        throw new IOException();
    }
}