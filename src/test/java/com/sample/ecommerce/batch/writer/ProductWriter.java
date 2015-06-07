/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sample.ecommerce.batch.writer;

import java.util.List;
import org.springframework.batch.item.ItemWriter;
import com.sample.ecommerce.domain.Product;
import com.sample.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductWriter implements ItemWriter<Product> {
    
    @Autowired
    private ProductService productService;

    @Override
    public void write(List<? extends Product> products) throws Exception {
        productService.save(products);
    }
    
}
