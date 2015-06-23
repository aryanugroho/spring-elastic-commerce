/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sample.ecommerce.service;

import com.sample.ecommerce.domain.Term;
import java.util.List;
import org.springframework.stereotype.Service;
import org.zols.datastore.DataStore;
import org.zols.datastore.elasticsearch.ElasticSearchDataStore;
import org.zols.datatore.exception.DataStoreException;

@Service
public class TermService {

    private final DataStore dataStore;

    public TermService() {
        dataStore = new ElasticSearchDataStore("ecommerce");
    }

    public List<Term> suggest(String keyword) {
        return null;
    }

    public <S extends Term> Iterable<Term> save(Iterable<Term> itrbl) throws DataStoreException {
        return dataStore.create(Term.class, itrbl);
    }

    public void deleteAll() throws DataStoreException {
        dataStore.delete(Term.class);
    }

}
