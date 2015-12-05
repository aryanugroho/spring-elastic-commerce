/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sample.ecommerce.service;

import com.google.common.collect.Lists;
import com.sample.ecommerce.domain.AggregatedResults;
import com.sample.ecommerce.util.ElasticSearchUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static java.util.stream.Collectors.joining;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.zols.datastore.DataStore;
import org.zols.datastore.query.Filter;
import static org.zols.datastore.query.Filter.Operator.EQUALS;
import org.zols.datastore.query.Query;
import org.zols.datatore.exception.DataStoreException;

@Service
public class CategoryService {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(CategoryService.class);

    @Autowired
    private DataStore dataStore;

    @Autowired
    private ElasticSearchUtil elasticSearchUtil;

    public List<Map<String, Object>> getParents(String categoryId) throws DataStoreException {
        List<Map<String, Object>> parents = new ArrayList<>();
        Map<String, Object> category;
        String parent;
        category = findOne(categoryId);
        while (category != null) {
            parent = (String) category.get("parent");
            if (parent != null) {
                category = findOne(parent);
                parents.add(category);
            } else {
                break;
            }
        }
        return (parents.isEmpty()) ? null : Lists.reverse(parents);
    }

    public List<Map<String, Object>> getChildren(String parentCategoryId) throws DataStoreException {
        Map<String, Object> browseQuery = new HashMap<>();
        browseQuery.put("parent", parentCategoryId);        
        List<Map<String, Object>> children = elasticSearchUtil.resultsOf("category", "getchildren_of_categories", browseQuery);
        if (children != null) {
            for (Map<String, Object> child : children) {
                child.put("children", getChildren((java.lang.String) child.get("id")));
            }
        }
        return children ;
    }

    public AggregatedResults findByCategory(String categoryId,
            String keyword,
            Query query,
            Pageable pageable) throws DataStoreException {
        AggregatedResults aggregatedResults = null;
        Map<String, Object> category = findOne(categoryId);
        if (category != null) {
            Map<String, Object> browseQuery = new HashMap<>();
            List<Map<String, Object>> parents = getParents(categoryId);
            if (parents == null) {
                parents = new ArrayList<>();
            }
            parents.add(category);
            browseQuery.put("categoriesMap", parents.stream().map(parentCategory -> parentCategory.get("id").toString()).collect(joining("_")));
            browseQuery.put("keyword", keyword);
            aggregatedResults = elasticSearchUtil.aggregatedSearch("product",
                    (keyword == null) ? "browse_products" : "browse_products_with_keyword",
                    browseQuery, pageable, query);
        }
        return aggregatedResults;
    }

    public Map<String, Object> findOne(String id) throws DataStoreException {
        return findOne(id, Boolean.FALSE);
    }

    public Map<String, Object> findOne(String id, Boolean withChildren) throws DataStoreException {
        Map<String, Object> category = dataStore.read("category", id);
        if (withChildren && category != null) {
            try {
                category.put("children", getChildren(id));
            } catch (NullPointerException e) {
                LOGGER.error("Elastic Search Issue", e);
            }

        }
        return category;
    }

}
