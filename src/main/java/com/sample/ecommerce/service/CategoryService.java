/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sample.ecommerce.service;

import com.sample.ecommerce.domain.Category;
import com.sample.ecommerce.domain.Product;
import com.sample.ecommerce.domain.ProductsList;
import java.util.ArrayList;
import java.util.List;
import org.elasticsearch.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.zols.datastore.DataStore;
import org.zols.datastore.elasticsearch.ElasticSearchDataStore;
import org.zols.datastore.query.Filter;
import static org.zols.datastore.query.Filter.Operator.EQUALS;
import static org.zols.datastore.query.Filter.Operator.EXISTS_IN;
import org.zols.datastore.query.Query;
import org.zols.datatore.exception.DataStoreException;

@Service
public class CategoryService {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(CategoryService.class);

    private final DataStore dataStore;

    public CategoryService() {
        dataStore = new ElasticSearchDataStore("ecommerce");
    }

    public ProductsList listProducts(String categoryName, int pageNumber, int pageSize) {
        return null;
    }

    public ProductsList listProducts(String categoryName, List<Filter> navigationFilters, int pageNumber, int pageSize) {
        return null;
    }

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

    private String[] categoriesToLookForProducts(String categoryId) throws DataStoreException {
        List<String> categoriesToLookFor = new ArrayList<>();
        wallThroughChildren(categoriesToLookFor, categoryId);
        return categoriesToLookFor.toArray(new String[categoriesToLookFor.size()]);
    }

    public List<Product> findByCategory(String categoryId) throws DataStoreException {
        Query query = new Query();
        query.addFilter(new Filter<>("categories", EXISTS_IN, categoriesToLookForProducts(categoryId)));
        return dataStore.list(Product.class, query);
    }

    private void wallThroughChildren(List<String> categoriesToLookFor, String categoryId) throws DataStoreException {
        List<Category> children = getChildren(categoryId);
        categoriesToLookFor.add(categoryId);
        if (children != null) {
            for (Category child : children) {
                categoriesToLookFor.add(child.getId());
                wallThroughChildren(categoriesToLookFor, child.getId());
            }
        }
    }

    public <S extends Category> Iterable<Category> save(Iterable<Category> itrbl) throws DataStoreException {
        return dataStore.create(Category.class, itrbl);
    }

    public Category findOne(String id) throws DataStoreException {
        Category category = dataStore.read(Category.class, id);
        if (category != null) {
            category.setChildren(getChildren(id));
        }
        return category;
    }

    public void deleteAll() throws DataStoreException {
        dataStore.delete(Category.class);
    }

}