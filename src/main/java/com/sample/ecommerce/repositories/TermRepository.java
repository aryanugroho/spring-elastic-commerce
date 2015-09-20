/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sample.ecommerce.repositories;

import com.sample.ecommerce.domain.Term;
import java.util.List;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 *
 * @author wz07
 */
public interface TermRepository extends ElasticsearchRepository<Term, String> {

    @Query("{ \"bool\": { \"should\": [ { \"match\": { \"term\": { \"type\": \"phrase_prefix\", \"query\": \"?0\", \"fuzziness\": \"AUTO\", \"prefix_length\": 1, \"max_expansions\": 100, \"boost\": 1 } } }, { \"match\": { \"term\": { \"query\": \"?0\", \"type\": \"phrase_prefix\", \"boost\": 2 } } } ] } }")
    List<Term> findByKeyword(String keyword);
}
