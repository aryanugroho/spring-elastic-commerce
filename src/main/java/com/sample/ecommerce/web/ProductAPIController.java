/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sample.ecommerce.web;

import com.sample.ecommerce.domain.Product;
import com.sample.ecommerce.service.ProductService;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value="/api/products")
public class ProductAPIController {

    private static final Logger LOGGER = getLogger(ProductAPIController.class);
    
    @Autowired
    private ProductService productService;    

    @RequestMapping(method = POST)    
    public Product create(@RequestBody Product product)  {
        LOGGER.info("Creating new product {}", product);
        return productService.save(product);
    }

    @RequestMapping(value = "/{id}", method = GET)    
    public Product read(@PathVariable(value = "id") String id)  {
        LOGGER.info("Getting product ", id);
        return productService.findOne(id);
    }

    @RequestMapping(value = "/{id}", method = PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@PathVariable(value = "id") String id,
            @RequestBody Product product)  {        
        if (id.equals(product.getId())) {
            LOGGER.info("Updating product with id {} ", id);
            productService.save(product);
        }
    }

    @RequestMapping(value = "/{id}", method = DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(value = "id") String id)  {
        LOGGER.info("Deleting products with id {}", id);
        productService.delete(id);
    }
    
    @RequestMapping(method = GET)    
    public Iterable<Product> list()  {
        LOGGER.info("Getting products ");
        return productService.findAll();
    }    
    
}
