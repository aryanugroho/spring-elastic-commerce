/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sample.ecommerce.batch.writer;

import java.util.List;
import org.springframework.batch.item.ItemWriter;
import com.sample.ecommerce.domain.Category;
import com.sample.ecommerce.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CategoryWriter implements ItemWriter<Category> {
    
    @Autowired
    private CategoryService categoryService;

    @Override
    public void write(List<? extends Category> categories) throws Exception {
        categoryService.save(categories);
    }
    
}
