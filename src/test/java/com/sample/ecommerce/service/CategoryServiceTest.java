/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sample.ecommerce.service;

import com.sample.ecommerce.TestConfiguration;
import com.sample.ecommerce.domain.Navigation;
import com.sample.ecommerce.domain.NavigationFilter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
        List<NavigationFilter> filters = Arrays.asList(new NavigationFilter<String>("brand", NavigationFilter.Operator.EQUALS, "apple"));
        List<Map> mobileProductsByApple = categoryService.listProducts("mobiles", filters).getProducts();
        assertTrue("Only One Mobile with apple brand", mobileProductsByApple.size() == 1);
    }

    @Test
    public void test_listWithMultipleValueFilter() {
        List<NavigationFilter> filters
                = Arrays.asList(new NavigationFilter<String>("brand", NavigationFilter.Operator.EQUALS, "google"),
                        new NavigationFilter<String>("brand", NavigationFilter.Operator.EQUALS, "apple"));
        List<Map> mobileProductsByApple = categoryService.listProducts("electronics", filters).getProducts();
        assertTrue("Only One Mobile with apple brand", mobileProductsByApple.size() == 4);

    }

    @Test
    public void test_listWithMultipleTypeFilter() {
        List<NavigationFilter> filters
                = Arrays.asList(new NavigationFilter<String>("brand", NavigationFilter.Operator.EQUALS, "google"),
                        new NavigationFilter<String>("operatingSystem", NavigationFilter.Operator.EQUALS, "android"));
        List<Map> mobileProductsByApple = categoryService.listProducts("electronics", filters).getProducts();
        assertTrue("Only One Mobile with apple brand", mobileProductsByApple.size() == 1);

    }

    @Test
    public void test_listWithMultipleType_MultipleValueFilter() {
        List<NavigationFilter> filters
                = Arrays.asList(new NavigationFilter<String>("brand", NavigationFilter.Operator.EQUALS, "google"),
                        new NavigationFilter<String>("brand", NavigationFilter.Operator.EQUALS, "sony"),
                        new NavigationFilter<String>("operatingSystem", NavigationFilter.Operator.EQUALS, "android"));
        List<Map> mobileProductsByApple = categoryService.listProducts("electronics", filters).getProducts();
        assertTrue("Only One Mobile with apple brand", mobileProductsByApple.size() == 2);

    }

}
