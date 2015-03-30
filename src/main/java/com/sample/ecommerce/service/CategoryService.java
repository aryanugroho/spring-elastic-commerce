/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sample.ecommerce.service;

import com.sample.ecommerce.domain.Category;
import com.sample.ecommerce.domain.Product;
import com.sample.ecommerce.repositories.CategoryRepository;
import com.sample.ecommerce.repositories.ProductRepository;
import java.util.ArrayList;
import java.util.List;
import org.elasticsearch.common.collect.Lists;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.FacetedPage;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<Category> getParents(String categoryId) {
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

    public List<Category> getChildren(String categoryId) {
        List<Category> children = categoryRepository.findByParent(categoryId);
        if (children != null) {
            for (Category child : children) {
                child.setChildren(getChildren(child.getId()));
            }
        }
        return children;
    }

    public List<Product> findByCategory(String categoryId) {
        List<String> categoriesToLookFor = new ArrayList<>();
        wallThroughChildren(categoriesToLookFor, categoryId); 
        return productRepository.findByCategoryIn(categoriesToLookFor);
    }

    private void wallThroughChildren(List<String> categoriesToLookFor, String categoryId) {
        List<Category> children = getChildren(categoryId);
        categoriesToLookFor.add(categoryId);
        if (children != null) {
            for (Category child : children) {
                categoriesToLookFor.add(child.getId());
                wallThroughChildren(categoriesToLookFor, child.getId());
            }
        }
    }

    public <S extends Category> S index(S s) {
        return categoryRepository.index(s);
    }

    public Iterable<Category> search(QueryBuilder qb) {
        return categoryRepository.search(qb);
    }

    public FacetedPage<Category> search(QueryBuilder qb, Pageable pgbl) {
        return categoryRepository.search(qb, pgbl);
    }

    public FacetedPage<Category> search(SearchQuery sq) {
        return categoryRepository.search(sq);
    }

    public Page<Category> searchSimilar(Category t, String[] strings, Pageable pgbl) {
        return categoryRepository.searchSimilar(t, strings, pgbl);
    }

    public Iterable<Category> findAll(Sort sort) {
        return categoryRepository.findAll(sort);
    }

    public Page<Category> findAll(Pageable pgbl) {
        return categoryRepository.findAll(pgbl);
    }

    public <S extends Category> S save(S s) {
        return categoryRepository.save(s);
    }

    public <S extends Category> Iterable<S> save(Iterable<S> itrbl) {
        return categoryRepository.save(itrbl);
    }

    public Category findOne(String id) {
        return categoryRepository.findOne(id);
    }

    public boolean exists(String id) {
        return categoryRepository.exists(id);
    }

    public Iterable<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Iterable<Category> findAll(Iterable<String> itrbl) {
        return categoryRepository.findAll(itrbl);
    }

    public long count() {
        return categoryRepository.count();
    }

    public void delete(String id) {
        categoryRepository.delete(id);
    }

    public void delete(Category t) {
        categoryRepository.delete(t);
    }

    public void delete(Iterable<? extends Category> itrbl) {
        categoryRepository.delete(itrbl);
    }

    public void deleteAll() {
        categoryRepository.deleteAll();
    }

}
