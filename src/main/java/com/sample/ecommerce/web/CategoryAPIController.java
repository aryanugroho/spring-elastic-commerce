/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sample.ecommerce.web;

import com.sample.ecommerce.domain.AggregatedResults;
import com.sample.ecommerce.domain.Category;
import com.sample.ecommerce.service.CategoryService;
import static com.sample.ecommerce.util.HttpUtil.getQuery;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import org.springframework.web.bind.annotation.RestController;
import org.zols.datatore.exception.DataStoreException;

@RestController
@RequestMapping(value = "/api/categories")
public class CategoryAPIController {

    private static final Logger LOGGER = getLogger(CategoryAPIController.class);

    @Autowired
    private CategoryService categoryService;

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

    @RequestMapping(value = "/{categoryId}")
    public AggregatedResults browseByCategory(@PathVariable("categoryId") String categoryId,
            Pageable pageable,HttpServletRequest request) throws DataStoreException {
        return categoryService.findByCategory(categoryId, null, getQuery(request),pageable);
    }

}
