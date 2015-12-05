/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sample.ecommerce.service;

import com.sample.ecommerce.domain.AggregatedResults;
import com.sample.ecommerce.util.ElasticSearchUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.zols.datastore.DataStore;
import org.zols.datastore.query.Query;
import org.zols.datatore.exception.DataStoreException;

@Service
public class ProductService {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(ProductService.class);

    @Autowired
    private DataStore dataStore;
    
    @Autowired
    private CategoryService categoryService;


    @Autowired
    private ElasticSearchUtil elasticSearchUtil;

    public void deleteAll() throws DataStoreException {
        dataStore.delete("product");
    }

    public Map<String, Object> findOne(String id) throws DataStoreException {
        return dataStore.read("product", id);
    }

    public List<Map<String, Object>> suggest(String keyword) {
        Map<String, Object> browseQuery = new HashMap<>();
        browseQuery.put("keyword", keyword);
        return elasticSearchUtil.resultsOf("product", "suggest_products_with_keyword", browseQuery);
    }

    public AggregatedResults search(String keyword,
            Pageable pageable,
            Query query) throws DataStoreException {
        Map<String, Object> browseQuery = new HashMap<>();
        browseQuery.put("keyword", keyword);
        AggregatedResults aggregatedResults = elasticSearchUtil.aggregatedSearch("product",
                "search_products_with_keyword",
                browseQuery, pageable, query);
        if (aggregatedResults != null && aggregatedResults.getBuckets() != null) {
            aggregatedResults.getBuckets().stream()
                    .filter(map -> map.get("name").equals("categories"))
                    .forEach(map -> {
                        ((List<Map<String, Object>>) map.get("items")).forEach(categoryMap -> {
                            try {
                                Map<String,Object> category = categoryService.findOne(categoryMap.get("name").toString());
                                categoryMap.put("label", category.get("label"));
                            } catch (DataStoreException ex) {
                                java.util.logging.Logger.getLogger(ProductService.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        });
                    });
        }
        return aggregatedResults;
    }

}
