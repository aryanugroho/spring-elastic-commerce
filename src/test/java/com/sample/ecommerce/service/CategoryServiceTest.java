/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sample.ecommerce.service;

import com.sample.ecommerce.TestConfiguration;
import com.sample.ecommerce.domain.Navigation;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import static junit.framework.TestCase.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.zols.datastore.query.Filter;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TestConfiguration.class)
public class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;

    @Test
    public void test_listProductsWithPagination() {
        List<Map> fiveproducts = categoryService.listProducts("electronics", 1, 5).getProducts();
        assertTrue("Only 5 Products",fiveproducts.size() == 5);
    }
    @Test
    public void test_listProducts() {
        List<Navigation> electronicsNavigations = categoryService.listProducts("electronics", 1, 20).getNavigations();
        assertTrue("Only One Navigation (Brand)", electronicsNavigations.size() == 1);

        List<Navigation> mobilesNavigations = categoryService.listProducts("mobiles", 1, 20).getNavigations();
        assertTrue("Only One Navigation (Brand and Operating System)", mobilesNavigations.size() == 2);
    }

    @Test
    public void test_listWithFilter() {
        List<Filter> filters = Arrays.asList(new Filter<String>("brand", Filter.Operator.EQUALS, "apple"));
        List<Map> mobileProductsByApple = categoryService.listProducts("mobiles", filters, 1, 20).getProducts();
        assertTrue("Only One Mobile with apple brand", mobileProductsByApple.size() == 1);
    }

    @Test
    public void test_listWithMultipleValueFilter() {
        List<Filter> filters
                = Arrays.asList(new Filter<String>("brand", Filter.Operator.EQUALS, "google"),
                        new Filter<String>("brand", Filter.Operator.EQUALS, "apple"));
        List<Map> mobileProductsByApple = categoryService.listProducts("electronics", filters, 1, 20).getProducts();
        assertTrue("Four Itesn with apple or google brand", mobileProductsByApple.size() == 4);

    }

    @Test
    public void test_listWithMultipleTypeFilter() {
        List<Filter> filters
                = Arrays.asList(new Filter<String>("brand", Filter.Operator.EQUALS, "google"),
                        new Filter<String>("operatingSystem", Filter.Operator.EQUALS, "android"));
        List<Map> mobileProductsByApple = categoryService.listProducts("electronics", filters, 0, 20).getProducts();
        assertTrue("Only One Electronics item with google brand", mobileProductsByApple.size() == 1);

    }

    @Test
    public void test_listWithMultipleType_MultipleValueFilter() {
        List<Filter> filters
                = Arrays.asList(new Filter<String>("brand", Filter.Operator.EQUALS, "google"),
                        new Filter<String>("brand", Filter.Operator.EQUALS, "sony"),
                        new Filter<String>("operatingSystem", Filter.Operator.EQUALS, "android"));
        List<Map> mobileProductsByApple = categoryService.listProducts("electronics", filters, 1, 20).getProducts();
        assertTrue("google or sony products  with android operating system", mobileProductsByApple.size() == 2);

    }

}
