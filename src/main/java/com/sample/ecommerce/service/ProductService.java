/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sample.ecommerce.service;

import com.sample.ecommerce.domain.Product;
import com.sample.ecommerce.repositories.ProductRepository;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.FacetedPage;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    
    @Autowired
    private ProductRepository productRepository;

    public <S extends Product> S index(S s) {
        return productRepository.index(s);
    }

    public Iterable<Product> search(QueryBuilder qb) {
        return productRepository.search(qb);
    }

    public FacetedPage<Product> search(QueryBuilder qb, Pageable pgbl) {
        return productRepository.search(qb, pgbl);
    }

    public FacetedPage<Product> search(SearchQuery sq) {
        return productRepository.search(sq);
    }

    public Page<Product> searchSimilar(Product t, String[] strings, Pageable pgbl) {
        return productRepository.searchSimilar(t, strings, pgbl);
    }

    public Iterable<Product> findAll(Sort sort) {
        return productRepository.findAll(sort);
    }

    public Page<Product> findAll(Pageable pgbl) {
        return productRepository.findAll(pgbl);
    }

    public <S extends Product> S save(S s) {
        return productRepository.save(s);
    }

    public <S extends Product> Iterable<S> save(Iterable<S> itrbl) {
        return productRepository.save(itrbl);
    }

    public Product findOne(String id) {
        return productRepository.findOne(id);
    }

    public boolean exists(String id) {
        return productRepository.exists(id);
    }

    public Iterable<Product> findAll() {
        return productRepository.findAll();
    }

    public Iterable<Product> findAll(Iterable<String> itrbl) {
        return productRepository.findAll(itrbl);
    }

    public long count() {
        return productRepository.count();
    }

    public void delete(String id) {
        productRepository.delete(id);
    }

    public void delete(Product t) {
        productRepository.delete(t);
    }

    public void delete(Iterable<? extends Product> itrbl) {
        productRepository.delete(itrbl);
    }

    public void deleteAll() {
        productRepository.deleteAll();
    }

    public Iterable<Product> searchByKeyword(String keyword) {
        return search(QueryBuilders.queryString(keyword));
    }

       
    
    
}
