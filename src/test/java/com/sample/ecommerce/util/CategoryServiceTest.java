/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sample.ecommerce.util;


import com.sample.ecommerce.Application;
import com.sample.ecommerce.service.CategoryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.zols.datatore.exception.DataStoreException;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;

    @Test
    public void testSearchResponse() throws DataStoreException {
//        List<Map<String, Object>> children = categoryService.getChildren("slippersandflipflops");
//        Assert.assertNotNull(children);
    }

}
