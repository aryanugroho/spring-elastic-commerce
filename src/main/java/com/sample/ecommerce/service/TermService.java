/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sample.ecommerce.service;

import com.sample.ecommerce.domain.Term;
import com.sample.ecommerce.util.ElasticSearchUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zols.datastore.DataStore;
import org.zols.datatore.exception.DataStoreException;

@Service
public class TermService {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(TermService.class);
    @Autowired
    private DataStore dataStore;
    
    
    @Autowired
    private ElasticSearchUtil elasticSearchUtil;

    public List<Map<String, Object>> suggest(String keyword) {
        Map<String, Object> browseQuery = new HashMap<>();
        browseQuery.put("keyword", keyword);
        return elasticSearchUtil.resultsOf("term", "suggest_terms_with_keyword", browseQuery);
    }

    public <S extends Term> List<Term> save(List<? extends Term> itrbl) throws DataStoreException {
        itrbl.forEach(term -> {
            try {
                dataStore.create(term);
            } catch (DataStoreException ex) {
                LOGGER.error("Unable to crete term", ex);
            }
        });
        return null;
    }

    public void deleteAll() throws DataStoreException {
        dataStore.delete(Term.class);
    }

    public List<Term> findAll() throws DataStoreException {
        return dataStore.list(Term.class);
    }

}
