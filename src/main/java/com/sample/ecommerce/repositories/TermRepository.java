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

    @Query("{\n"
            + "                    \"bool\": {\n"
            + "                        \"should\": [{\n"
            + "    \"ids\" : {\n"
            + "       \n"
            + "        \"values\" : [\"?0\"]\n"
            + "    }\n"
            + "}, {\n"
            + "                                \"prefix\": {\n"
            + "                                    \"term\": {\n"
            + "                                        \"value\": \"?0\",\n"
            + "                                        \"boost\": 2.0\n"
            + "                                    }\n"
            + "                                }\n"
            + "                            }, {\n"
            + "                                \"term\": {\n"
            + "                                    \"term\": {\n"
            + "                                        \"value\": \"?0\",\n"
            + "                                        \"boost\": 1.0\n"
            + "                                    }\n"
            + "                                }\n"
            + "                            }]\n"
            + "                    }\n"
            + "                }")
    List<Term> suggest(String keyword);
}
