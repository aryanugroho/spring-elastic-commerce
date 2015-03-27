/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sample.ecommerce.repositories;

import com.sample.ecommerce.domain.Category;
import java.util.List;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 *
 * @author wz07
 */
public interface CategoryRepository extends ElasticsearchRepository<Category,String> {
    public List<Category> findByParent(String parent);
}
