/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sample.ecommerce.web;

import com.sample.ecommerce.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/categories")
public class CategoryController {
      
    @Autowired
    private CategoryService categoryService;
    
    @RequestMapping(value = "/{categoryName}")
    public String keywordSearchByCategory(Model model,@PathVariable("categoryName") String categoryName) {
        model.addAttribute("parents", categoryService.getParents(categoryName));
        model.addAttribute("products", categoryService.findByCategory(categoryName));    
        return "shop";
    }

//    @RequestMapping(value = "/{categoryName}/search/{keyword}")
//    public String browseByCategory(@PathVariable("categoryName") String categoryName, @PathVariable("keyword") String keyword) {
//        return "shop";
//    }
}
