/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sample.ecommerce.repositories;

import com.sample.ecommerce.domain.Product;
import java.util.List;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 *
 * @author wz07
 */
public interface ProductRepository extends ElasticsearchRepository<Product,String> {    
    public List<Product> findByCategoriesIn(String[] categories);
}
