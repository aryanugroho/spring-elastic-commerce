/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sample.ecommerce;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.util.stream.Collectors.joining;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import static org.elasticsearch.node.NodeBuilder.nodeBuilder;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import org.springframework.stereotype.Service;

@Service
public class ElasticService {

    private static final Logger LOGGER = getLogger(ElasticService.class);

    private static final String ELASTIC_URL = "http://localhost:9200/ecommerce";

    public ElasticService() {
        nodeBuilder().node();
    }

    @PostConstruct
    public void init() throws IOException, URISyntaxException {
        deleteIndex();
        populateIndex();
    }

    private void deleteIndex() throws IOException {
        LOGGER.info("Deleting Index");

        URL url = new URL(ELASTIC_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("DELETE");
        conn.setRequestProperty("Accept", "application/json");

        if (conn.getResponseCode() == 404) {
            LOGGER.info("Index does not exist");
        } else if (conn.getResponseCode() == 200) {
            LOGGER.info("Index deleted");
        } else {
            throw new RuntimeException("Failed : HTTP error code : "
                    + conn.getResponseCode());
        }

        conn.disconnect();
    }

    private void populateIndex() throws IOException, URISyntaxException {
        Path path = Paths.get(ClassLoader.getSystemResource("data/products.json").toURI());
        Stream<String> lines = Files.lines(path);
        String input = lines.collect(joining("\n")) + "\n";

        URL url = new URL(ELASTIC_URL + "/products/_bulk");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");

        OutputStream os = conn.getOutputStream();
        os.write(input.getBytes());
        os.flush();

        LOGGER.info("Bulk Uploaded" + conn.getResponseCode());

        BufferedReader br = new BufferedReader(new InputStreamReader(
                (conn.getInputStream())));

        String output;
        System.out.println("Output from Server .... \n");
        while ((output = br.readLine()) != null) {
            System.out.println(output);
        }

        conn.disconnect();
    }

}
