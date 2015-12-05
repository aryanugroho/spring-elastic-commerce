/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sample.ecommerce.web;

import com.sample.ecommerce.service.OfferService;
import static com.sample.ecommerce.util.HttpUtil.getQuery;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import org.springframework.web.bind.annotation.RestController;
import org.zols.datatore.exception.DataStoreException;

@RestController
@RequestMapping(value = "/api/offers")
public class OfferAPIController {

    private static final Logger LOGGER = getLogger(OfferAPIController.class);

    @Autowired
    private OfferService offerService;

    @RequestMapping(method = GET)
    public Page<List> list(HttpServletRequest request, Pageable pageable) throws DataStoreException {
        LOGGER.info("Listing offers");
        return offerService.search(pageable, getQuery(request));
    }

}
