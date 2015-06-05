/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sample.ecommerce.service;

import com.sample.ecommerce.domain.Navigation;
import com.sample.ecommerce.domain.Product;
import com.sample.ecommerce.domain.ProductsList;
import com.sample.ecommerce.repositories.ProductRepository;
import java.util.ArrayList;
import java.util.List;
import static java.util.stream.Collectors.toList;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import static org.elasticsearch.search.aggregations.AggregationBuilders.terms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    
    public ProductsList list() {
        
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withIndices("ecommerce").withTypes("products")
                .addAggregation(terms("brand").field("brand"))
                .build();
        
        return elasticsearchTemplate.query(searchQuery, new ResultsExtractor<ProductsList>() {
            @Override
            public ProductsList extract(SearchResponse response) {
                ProductsList productsList = new ProductsList();
                
                List<Aggregation> aggregations = response.getAggregations().asList();
                
                productsList.setNavigations(
                        aggregations.stream().map(aggregation -> {
                            Navigation navigation = new Navigation();
                            navigation.setName(aggregation.getName());
                            System.out.println("aggregation.toString() " + aggregation.toString());
                            return navigation;
                        }).collect(toList()));
                
                List<Product> products = new ArrayList<>();
                productsList.setProducts(elasticsearchTemplate.getElasticsearchConverter().getConversionService().convert(response.getHits().hits(), products.getClass()));
                return productsList;
            }
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
