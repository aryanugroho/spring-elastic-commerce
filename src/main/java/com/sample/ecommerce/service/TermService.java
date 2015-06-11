/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sample.ecommerce.service;

import com.sample.ecommerce.domain.Term;
import com.sample.ecommerce.repositories.TermRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.FacetedPage;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

@Service
public class TermService {

    @Autowired
    private TermRepository termRepository;

    public List<Term> suggest(String keyword) {
        return termRepository.suggest(keyword);
    }

    public <S extends Term> S index(S s) {
        return termRepository.index(s);
    }

    public FacetedPage<Term> search(SearchQuery sq) {
        return termRepository.search(sq);
    }

    public <S extends Term> S save(S s) {
        return termRepository.save(s);
    }

    public <S extends Term> Iterable<S> save(Iterable<S> itrbl) {
        return termRepository.save(itrbl);
    }

    public Term findOne(String id) {
        return termRepository.findOne(id);
    }

    public boolean exists(String id) {
        return termRepository.exists(id);
    }

    public Iterable<Term> findAll() {
        return termRepository.findAll();
    }

    public long count() {
        return termRepository.count();
    }

    public void delete(String id) {
        termRepository.delete(id);
    }

    public void deleteAll() {
        termRepository.deleteAll();
    }

}
