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
import org.springframework.beans.factory.annotation.Autowired;
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
        return children.size() > 0 ? children : null;
    }

    public List<Product> findByCategory(String categoryId) {
        List<String> categoriesToLookFor = new ArrayList<>();
        wallThroughChildren(categoriesToLookFor, categoryId);
        return productRepository.findByCategoriesIn(categoriesToLookFor);
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

    public <S extends Category> S save(S s) {
        return categoryRepository.save(s);
    }

    public <S extends Category> Iterable<S> save(Iterable<S> itrbl) {
        return categoryRepository.save(itrbl);
    }

    public Category findOne(String id) {
        Category category = categoryRepository.findOne(id);
        if (category != null) {
            category.setChildren(getChildren(id));
        }
        return category;
    }

    public Iterable<Category> findAll() {
        return categoryRepository.findAll();
    }

    public void deleteAll() {
        categoryRepository.deleteAll();
    }

}
