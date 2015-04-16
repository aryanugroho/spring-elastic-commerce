/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sample.ecommerce.service;

import com.sample.ecommerce.domain.Term;
import com.sample.ecommerce.repositories.TermRepository;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.FacetedPage;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

@Service
public class TermService {

    @Autowired
    private TermRepository termRepository;

    public <S extends Term> S index(S s) {
        return termRepository.index(s);
    }

    public Iterable<Term> search(QueryBuilder qb) {
        return termRepository.search(qb);
    }

    public FacetedPage<Term> search(QueryBuilder qb, Pageable pgbl) {
        return termRepository.search(qb, pgbl);
    }

    public FacetedPage<Term> search(SearchQuery sq) {
        return termRepository.search(sq);
    }

    public Page<Term> searchSimilar(Term t, String[] strings, Pageable pgbl) {
        return termRepository.searchSimilar(t, strings, pgbl);
    }

    public Iterable<Term> findAll(Sort sort) {
        return termRepository.findAll(sort);
    }

    public Page<Term> findAll(Pageable pgbl) {
        return termRepository.findAll(pgbl);
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

    public Iterable<Term> findAll(Iterable<String> itrbl) {
        return termRepository.findAll(itrbl);
    }

    public long count() {
        return termRepository.count();
    }

    public void delete(String id) {
        termRepository.delete(id);
    }

    public void delete(Term t) {
        termRepository.delete(t);
    }

    public void delete(Iterable<? extends Term> itrbl) {
        termRepository.delete(itrbl);
    }

    public void deleteAll() {
        termRepository.deleteAll();
    }

}
