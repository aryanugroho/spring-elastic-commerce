/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sample.ecommerce.service;

import com.sample.ecommerce.domain.Product;
import org.springframework.stereotype.Service;
import org.zols.datastore.DataStore;
import org.zols.datastore.elasticsearch.ElasticSearchDataStore;
import org.zols.datatore.exception.DataStoreException;

@Service
public class ProductService {

    private final DataStore dataStore;

    public ProductService() {
        dataStore = new ElasticSearchDataStore("ecommerce");
    }

    public <S extends Product> Iterable<Product> save(Iterable<Product> itrbl) throws DataStoreException {
        return dataStore.create(Product.class, itrbl);
    }

    public void deleteAll() throws DataStoreException {
        dataStore.delete(Product.class);
    }

    public Iterable<Product> searchByKeyword(String keyword) {
        return null;
    }

}
