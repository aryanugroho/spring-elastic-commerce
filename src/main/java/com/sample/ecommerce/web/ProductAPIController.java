/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sample.ecommerce.web;

import com.sample.ecommerce.domain.AggregatedResults;
import com.sample.ecommerce.service.ProductService;
import java.util.Map;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.zols.datatore.exception.DataStoreException;

@RestController
@RequestMapping(value = "/api/products")
public class ProductAPIController {

    private static final Logger LOGGER = getLogger(ProductAPIController.class);

    @Autowired
    private ProductService productService;

    @RequestMapping(value = "/{id}", method = GET)
    public Map<String, Object> read(@PathVariable(value = "id") String id) throws DataStoreException {
        LOGGER.info("Getting product {}", id);
        return productService.findOne(id);
    }


    @RequestMapping(value = "/search/{keyword}", method = GET)
    public AggregatedResults keywordSearch(@PathVariable(value = "keyword") String keyword,Pageable pageable) throws DataStoreException {
        LOGGER.info("Searching products for keyword {}",keyword);
        return productService.search(keyword,pageable);
    }

}
