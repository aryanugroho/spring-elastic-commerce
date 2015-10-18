/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sample.ecommerce.service;

import com.google.common.collect.Lists;
import com.sample.ecommerce.domain.Category;
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

    public List<Category> getParents(String categoryId) throws DataStoreException {
        List<Category> parents = new ArrayList<>();
        Category category;
        String parent;
        category = findOne(categoryId);
        while (category != null) {
            parent = category.getParent();
            if (parent != null) {
                category = findOne(parent);
                parents.add(category);
            } else {
                break;
            }
        }
        return (parents.isEmpty()) ? null : Lists.reverse(parents);
    }

    public List<Category> getChildren(String categoryId) throws DataStoreException {
        Query query = new Query();
        query.addFilter(new Filter<>("parent", EQUALS, categoryId));
        List<Category> children = dataStore.list(Category.class, query);
        if (children != null) {
            for (Category child : children) {
                child.setChildren(getChildren(child.getId()));
            }
        }
        return children.size() > 0 ? children : null;
    }

    public AggregatedResults findByCategory(String categoryId,
            String keyword,
            Pageable pageable) throws DataStoreException {
        Map<String, Object> browseQuery = new HashMap<>();
        List<Category> parents = getParents(categoryId);
        if (parents == null) {
            parents = new ArrayList<>();
        }
        parents.add(findOne(categoryId));
        browseQuery.put("categoriesMap", parents.stream().map(category -> category.getId()).collect(joining("_")));
        browseQuery.put("keyword", keyword);
        browseQuery.put("size", pageable.getPageSize());
        browseQuery.put("from", (pageable.getPageNumber() * pageable.getPageSize()) + 1);
        return elasticSearchUtil.aggregatedSearch("product",
                (keyword == null) ? "browse_products" : "browse_products_with_keyword",
                browseQuery);
    }

    public Category findOne(String id) throws DataStoreException {
        Category category = dataStore.read(Category.class, id);
        if (category != null) {
            category.setChildren(getChildren(id));
        }
        return category;
    }

}
