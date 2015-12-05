/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sample.ecommerce.service;

import com.sample.ecommerce.util.ElasticSearchUtil;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.zols.datastore.query.Query;
import org.zols.datatore.exception.DataStoreException;

@Service
public class OfferService {

    @Autowired
    private ElasticSearchUtil elasticSearchUtil;

    public Page<List> search(
            Pageable pageable,
            Query query) throws DataStoreException {
        return elasticSearchUtil.pageOf("offer",
                "search_offers",
                pageable);
    }
}
