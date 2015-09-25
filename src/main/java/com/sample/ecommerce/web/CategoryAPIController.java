/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sample.ecommerce.web;

import com.sample.ecommerce.domain.Category;
import com.sample.ecommerce.service.CategoryService;
import java.util.List;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping(value = "/api/categories")
public class CategoryAPIController {

    private static final Logger LOGGER = getLogger(CategoryAPIController.class);

    @Autowired
    private CategoryService categoryService;

    @RequestMapping(method = POST)
    public Category create(@RequestBody Category category) throws DataStoreException {
        LOGGER.info("Creating new category {}", category);
        return categoryService.save(category);
    }

    @RequestMapping(value = "/{id}", method = GET)
    public Category read(@PathVariable(value = "id") String id) throws DataStoreException {
        LOGGER.info("Getting category {}", id);
        return categoryService.findOne(id);
    }

    @RequestMapping(value = "/{id}/parents", method = GET)
    public List<Category> parents(@PathVariable(value = "id") String id) throws DataStoreException {
        LOGGER.info("Getting parents of category {}", id);
        return categoryService.getParents(id);
    }

    @RequestMapping(value = "/{id}/children", method = GET)
    public List<Category> children(@PathVariable(value = "id") String id) throws DataStoreException {
        LOGGER.info("Getting children of category {}", id);
        return categoryService.getChildren(id);
    }

    @RequestMapping(value = "/{id}", method = PUT)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@PathVariable(value = "id") String id,
            @RequestBody Category category) throws DataStoreException {
        if (id.equals(category.getId())) {
            LOGGER.info("Updating category with id {} ", id);
            categoryService.save(category);
        }
    }

//    @RequestMapping(method = GET)
//    public Iterable<Category> list() {
//        LOGGER.info("Getting categories ");
//        return categoryService.l
//    }

}
