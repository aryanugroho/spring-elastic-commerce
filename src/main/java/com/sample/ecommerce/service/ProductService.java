/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sample.ecommerce.service;

import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zols.datastore.DataStore;
import org.zols.datatore.exception.DataStoreException;

@Service
public class ProductService {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(ProductService.class);

    @Autowired
    private DataStore dataStore;
    public Map<String, Object> save(Map<String, Object> product) throws DataStoreException {
        return dataStore.create(product);
    }

    public <S extends Map<String, Object>> Iterable<Map<String, Object>> save(Iterable<Map<String, Object>> itrbl) throws DataStoreException {
        itrbl.forEach(product -> {
            try {
                dataStore.create(product);
            } catch (DataStoreException ex) {
                LOGGER.error("Unable to crete Product", ex);
            }
        });
        return null;
    }

    public void deleteAll() throws DataStoreException {
        dataStore.delete("product");
    }

    public Map<String, Object> findOne(String id) throws DataStoreException {
        return dataStore.read("product", id);
    }

    public List<Map<String, Object>> findAll() throws DataStoreException {
        return dataStore.list("product");
    }

}
