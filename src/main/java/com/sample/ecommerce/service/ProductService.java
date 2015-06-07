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
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Iterable<Product> search(QueryBuilder qb) {
        return productRepository.search(qb);
    }

    public <S extends Product> S save(S s) {
        return productRepository.save(s);
    }

    public <S extends Product> Iterable<S> save(Iterable<S> itrbl) {
        return productRepository.save(itrbl);
    }

    public void deleteAll() {
        productRepository.deleteAll();
    }

    public Iterable<Product> searchByKeyword(String keyword) {
        return search(QueryBuilders.queryString(keyword));
    }

}
