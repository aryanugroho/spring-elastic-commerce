/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sample.ecommerce.web;

import com.sample.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/products")
public class ProductController {
    
    @Autowired
    private ProductService productService;
    
    @RequestMapping(value = "/search/{keyword}")
    public String keywordSearch(Model model,@PathVariable("keyword") String keyword) {
        model.addAttribute("products", productService.searchByKeyword(keyword));        
        return "shop";
    }
    
    @RequestMapping(value = "/{productId}", method = RequestMethod.GET)
    public String get(Model model,@PathVariable("productId") String productId) {
        model.addAttribute("product", productService.findOne(productId));    
        return "product-details";
    }
}
