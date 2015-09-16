/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sample.ecommerce.service;

import com.sample.ecommerce.TestConfiguration;
import com.sample.ecommerce.domain.Category;
import java.util.List;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.zols.datatore.exception.DataStoreException;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TestConfiguration.class)
public class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;

    @Test
    public void test_getCategories() throws DataStoreException {
        Category category = categoryService.findOne("electronics");
        assertNotNull("Create Category", category);
    }

    @Test
    public void test_relatedCategories() throws DataStoreException {
        String[] relatedCategories = categoryService.relatedCategories("tv");
        assertNotNull("Related Categories", relatedCategories);
    }
    
    @Test
    public void test_getParents() throws DataStoreException {
        List<Category> parents = categoryService.getParents("lcdtv");
        assertNotNull("Get Parents", parents);
    }
    
    @Test
    public void test_getCatogories() throws DataStoreException {
        List<Category> categories = categoryService.getCategories();
        assertNotNull("Get Catogories", categories);
    }

}
