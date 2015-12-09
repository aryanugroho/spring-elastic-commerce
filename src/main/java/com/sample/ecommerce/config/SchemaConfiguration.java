/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sample.ecommerce.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import org.elasticsearch.action.exists.ExistsResponse;
import org.elasticsearch.client.Client;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.zols.datastore.DataStore;
import static org.zols.datastore.util.JsonUtil.asList;
import static org.zols.datastore.util.JsonUtil.asMap;
import static org.zols.datastore.util.JsonUtil.asString;
import org.zols.datatore.exception.DataStoreException;

@Configuration
public class SchemaConfiguration {

    private static final org.slf4j.Logger LOGGER = LoggerFactory
            .getLogger(SchemaConfiguration.class);

    @Autowired
    private Client client;

    @Autowired
    private DataStore dataStore;

    @Value("${spring.application.name}")
    private String indexName;

    @PostConstruct
    private void setup() throws DataStoreException {
        ExistsResponse existsResponse = client.prepareExists().setIndices(indexName).setTypes("schema").execute().actionGet();

        if (!existsResponse.exists()) {
            InputStream inputStream = SchemaConfiguration.class.getResourceAsStream("/default_schema/schema.json");

            Map<String, Object> schemaMap = asMap(getContentFromStream(inputStream));
            schemaMap.forEach((schemaId, schemaAsMap) -> {
                try {
                    ((Map) schemaAsMap).put("id", schemaId);
                    dataStore.createSchema(asString(schemaAsMap));
                } catch (DataStoreException ex) {
                    LOGGER.error("Error Creating Schma", ex);
                }
            });

            schemaMap.keySet().forEach((String schemaId) -> {
                InputStream dataInputStream = null;
                try {
                    dataInputStream = SchemaConfiguration.class.getResourceAsStream("/default_schema/data/" + schemaId + ".json");
                    if (dataInputStream != null) {
                        dataStore.delete(schemaId);
                        List dataValues = asList(getContentFromStream(dataInputStream));
                        dataValues.forEach(data -> {
                            try {
                                dataStore.create(schemaId, (Map<String, Object>) data);
                            } catch (DataStoreException ex) {
                                LOGGER.error("Error Creating Schma", ex);
                            }
                        });
                    }

                } catch (Exception e) {
                    Logger.getLogger(SchemaConfiguration.class.getName()).log(Level.SEVERE, null, e);
                } finally {
                    try {
                        if (dataInputStream != null) {
                            dataInputStream.close();
                        }

                    } catch (IOException ex) {
                        LOGGER.error("Error Creating Schma", ex);
                    }
                }

            });
        }

    }

    private static String getContentFromStream(InputStream inputStream) {
        String theString;
        try (java.util.Scanner scanner = new java.util.Scanner(inputStream, "UTF-8").useDelimiter("\\A")) {
            theString = scanner.hasNext() ? scanner.next() : "";
        }
        return theString;
    }

}
