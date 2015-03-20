/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sample.ecommerce.web;

import com.sample.ecommerce.domain.Product;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/products")
public class ProductController {
    
    @RequestMapping(value = "/search/{keyword}", method = RequestMethod.GET)
    public List<Product> keywordSearch(@PathVariable("keyword") String keyword) {
        return null;
    }
    
    @RequestMapping(value = "/{categoryName}/search/{keyword}", method = RequestMethod.GET)
    public List<Product> keywordSearchByCategory(@PathVariable("categoryName") String categoryName,@PathVariable("keyword") String keyword) {
        return null;
    }
    
    @RequestMapping(value = "/{productId}", method = RequestMethod.GET)
    public Product get(@PathVariable("productId") String productId) {
        return null;
    }
}
