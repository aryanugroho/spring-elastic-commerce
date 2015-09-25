/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sample.ecommerce.service;

import com.sample.ecommerce.domain.Product;
import com.sample.ecommerce.repositories.ProductRepository;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import static org.elasticsearch.search.aggregations.AggregationBuilders.terms;
import org.elasticsearch.search.aggregations.Aggregations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    public Aggregations list() {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withIndices("ecommerce").withTypes("product")
                .addAggregation(terms("brand").field("brand"))
                .build();

        return elasticsearchTemplate.query(searchQuery, (SearchResponse response) -> {
            System.out.println(response.toString());
            return response.getAggregations();
        });
    }

    public Iterable<Product> search(QueryBuilder qb) {
        return productRepository.search(qb);
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

    public Iterable<Product> findAll() {
        return productRepository.findAll();
    }

    public void deleteAll() {
        productRepository.deleteAll();
    }

    public Iterable<Product> searchByKeyword(String keyword) {
        return search(QueryBuilders.queryString(keyword));
    }

}
