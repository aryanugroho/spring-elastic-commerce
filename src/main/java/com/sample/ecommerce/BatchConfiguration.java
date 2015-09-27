/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sample.ecommerce;

import com.sample.ecommerce.service.CategoryService;
import com.sample.ecommerce.service.ProductService;
import com.sample.ecommerce.service.TermService;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.zols.datastore.service.SchemaService;
import static org.zols.datastore.util.JsonUtil.asMap;
import org.zols.datatore.exception.DataStoreException;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TermService termService;

    @Autowired
    private SchemaService schemaService;

    @PostConstruct
    private void setup() throws DataStoreException {

        try {
            uploadSchema();
        } catch (URISyntaxException | IOException | DataStoreException ex) {
            Logger.getLogger(BatchConfiguration.class.getName()).log(Level.SEVERE, null, ex);
        }

        productService.deleteAll();
        categoryService.deleteAll();
        termService.deleteAll();

    }

    private static String getContentFromClasspath(String resourcePath) {
        InputStream inputStream = BatchConfiguration.class.getResourceAsStream(resourcePath);
        java.util.Scanner scanner = new java.util.Scanner(inputStream, "UTF-8").useDelimiter("\\A");
        String theString = scanner.hasNext() ? scanner.next() : "";
        scanner.close();
        return theString;
    }

    private void uploadSchema() throws URISyntaxException, IOException, DataStoreException {
        Map<String, Object> map = asMap(getContentFromClasspath("/schema.json"));
        map.forEach((k, v) -> {
            try {
                schemaService.delete(k);
                ((Map) v).put("id", k);
                schemaService.create(((Map) v));
            } catch (DataStoreException ex) {
                Logger.getLogger(BatchConfiguration.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
    }

}
