/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sample.ecommerce;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.zols.datastore.DataStore;
import org.zols.datastore.service.SchemaService;
import static org.zols.datastore.util.JsonUtil.asList;
import static org.zols.datastore.util.JsonUtil.asMap;
import org.zols.datatore.exception.DataStoreException;

/**
 *
 * @author sathish
 */
@Configuration
public class CoreConfiguration {

    @Autowired
    private SchemaService schemaService;

    @Autowired
    private DataStore dataStore;

    @PostConstruct
    private void setup() throws DataStoreException {
//        try {
//            uploadSchema();
//        } catch (URISyntaxException | IOException | DataStoreException ex) {
//            Logger.getLogger(CoreConfiguration.class.getName()).log(Level.SEVERE, null, ex);
//        }

    }

    private static String getContentFromStream(InputStream inputStream) {
        java.util.Scanner scanner = new java.util.Scanner(inputStream, "UTF-8").useDelimiter("\\A");
        String theString = scanner.hasNext() ? scanner.next() : "";
        scanner.close();
        return theString;
    }

    private void uploadSchema(final InputStream inputStream) throws URISyntaxException, IOException, DataStoreException {

        Map<String, Object> map = asMap(getContentFromStream(inputStream));
        map.forEach((schemaId, schemaAsMap) -> {
            try {
                schemaService.delete(schemaId);
                ((Map) schemaAsMap).put("id", schemaId);
                schemaService.create(((Map) schemaAsMap));
                dataStore.delete(schemaId);
            } catch (DataStoreException ex) {
                Logger.getLogger(CoreConfiguration.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        map.keySet().forEach(schemaId -> {
            InputStream dataInputStream = null;

            try {
                if (inputStream instanceof FileInputStream) {
                    File dataFile = new File("schema" + File.separator + "data" + File.separator + schemaId + ".json");
                    if (dataFile.exists()) {
                        dataInputStream = new FileInputStream(dataFile);
                    }
                } else {
                    dataInputStream = CoreConfiguration.class.getResourceAsStream("/default_schema/data/" + schemaId + ".json");
                }
                if (dataInputStream != null) {
                    List dataValues = asList(getContentFromStream(dataInputStream));

                    dataValues.forEach(data -> {
                        try {
                            dataStore.create(schemaId, (Map<String, Object>) data);
                        } catch (DataStoreException ex) {
                            Logger.getLogger(CoreConfiguration.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (dataInputStream != null) {
                        dataInputStream.close();
                    }

                } catch (IOException ex) {
                    Logger.getLogger(CoreConfiguration.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        });
    }

    private void uploadSchema() throws URISyntaxException, IOException, DataStoreException {
        InputStream inputStream;
        File schemaFolder = new File("schema");
        if (schemaFolder.exists()) {
            inputStream = new FileInputStream(new File(schemaFolder, "schema.json"));
        } else {
            inputStream = CoreConfiguration.class.getResourceAsStream("/default_schema/schema.json");
        }
        uploadSchema(inputStream);
        inputStream.close();
    }

}
