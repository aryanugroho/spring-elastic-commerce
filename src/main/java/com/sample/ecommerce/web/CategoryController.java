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
import org.zols.datatore.exception.DataStoreException;

@Controller
@RequestMapping("/categories")
public class CategoryController {
      
    @Autowired
    private CategoryService categoryService;
    
    @RequestMapping(value = "/{categoryId}")
    public String browseByCategory(Model model,@PathVariable("categoryId") String categoryId) throws DataStoreException {
        model.addAttribute("category", categoryService.findOne(categoryId));
        model.addAttribute("parents", categoryService.getParents(categoryId));
        model.addAttribute("aggregations", categoryService.findByCategory(categoryId));    
        return "shop";
    }

//    @RequestMapping(value = "/{categoryId}/search/{keyword}")
//    public String browseByCategory(@PathVariable("categoryId") String categoryId, @PathVariable("keyword") String keyword) {
//        return "shop";
//    }
}
