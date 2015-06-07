/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sample.ecommerce.service;

import com.sample.ecommerce.TestConfiguration;
import com.sample.ecommerce.domain.Navigation;
import java.util.List;
import static junit.framework.TestCase.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TestConfiguration.class)
public class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;

    @Test
    public void test_listProducts() {
        List<Navigation> electronicsNavigations = categoryService.listProducts("electronics").getNavigations();
        assertTrue("Only One Navigation (Brand)", electronicsNavigations.size() == 1);

        List<Navigation> mobilesNavigations = categoryService.listProducts("mobiles").getNavigations();
        assertTrue("Only One Navigation (Brand and Operating System)", mobilesNavigations.size() == 2);
    }

    @Test
    public void test_listWithFilter() {

    }

}
