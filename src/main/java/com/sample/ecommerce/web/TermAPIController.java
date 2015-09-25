/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sample.ecommerce.web;

import com.sample.ecommerce.domain.Term;
import com.sample.ecommerce.service.TermService;
import java.util.List;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import org.springframework.web.bind.annotation.RestController;
import org.zols.datatore.exception.DataStoreException;

@RestController
@RequestMapping(value = "/api/terms")
public class TermAPIController {

    private static final Logger LOGGER = getLogger(TermAPIController.class);

    @Autowired
    private TermService termService;

    @RequestMapping(method = GET)
    public Iterable<Term> list() throws DataStoreException {
        LOGGER.info("Getting terms ");
        return termService.findAll();
    }

    @RequestMapping(value = "/suggest/{keyword}")
    public List<Term> suggest(@PathVariable("keyword") String keyword) {
        LOGGER.info("Getting terms ");
        return termService.suggest(keyword);
    }

}
