/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sample.ecommerce.web;

import com.sample.ecommerce.service.ProductService;
import static com.sample.ecommerce.util.HttpUtil.getPageUrl;
import static com.sample.ecommerce.util.HttpUtil.getQuery;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.zols.datastore.query.Query;
import org.zols.datatore.exception.DataStoreException;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

//    @RequestMapping(value = "/search/{keyword}")
//    public String keywordSearch(Model model,@PathVariable("keyword") String keyword) {
//        model.addAttribute("products", productService.searchByKeyword(keyword));        
//        return "shop";
//    }
    @RequestMapping(value = "/{productId}", method = RequestMethod.GET)
    public String get(Model model, @PathVariable("productId") String productId) throws DataStoreException {
        model.addAttribute("product", productService.findOne(productId));
        return "product-details";
    }

    @RequestMapping(value = "/search/{keyword}", method = RequestMethod.GET)
    public String search(Model model,
            HttpServletRequest request,
            @PathVariable("keyword") String keyword,
            Pageable pageable) throws DataStoreException {
        Query query = getQuery(request);
        model.addAttribute("query", query);
        model.addAttribute("aggregations", productService.search(keyword, pageable, query));
        String pageUrl = getPageUrl(request);
        model.addAttribute("pageurl", pageUrl);
        int indexOfQuestionMark = pageUrl.indexOf("?");
        model.addAttribute("cleanpageurl", (indexOfQuestionMark == -1) ? pageUrl : pageUrl.substring(0, indexOfQuestionMark));
        return "shop";
    }
}
