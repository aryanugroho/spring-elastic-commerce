/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sample.ecommerce.service;

import com.sample.ecommerce.domain.AggregatedResults;
import com.sample.ecommerce.util.ElasticSearchUtil;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.zols.datastore.DataStore;
import org.zols.datatore.exception.DataStoreException;

@Service
public class ProductService {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(ProductService.class);

    @Autowired
    private DataStore dataStore;

    @Autowired
    private ElasticSearchUtil elasticSearchUtil;

    public void deleteAll() throws DataStoreException {
        dataStore.delete("product");
    }

    public Map<String, Object> findOne(String id) throws DataStoreException {
        return dataStore.read("product", id);
    }
    
    public AggregatedResults search(String keyword,Pageable pageable) throws DataStoreException {
        Map<String, Object> browseQuery = new HashMap<>();
        browseQuery.put("keyword", keyword);
        return elasticSearchUtil.aggregatedSearch("product",
                "search_products_with_keyword",
                browseQuery,pageable);
    }

}
