/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sample.ecommerce.util;

import com.sample.ecommerce.Application;
import com.sample.ecommerce.domain.AggregatedResults;
import static com.sample.ecommerce.util.ElasticSearchUtil.getContentFromClasspath;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class ElasticSearchUtilTest {

    @Autowired
    private ElasticSearchUtil elasticSearchUtil;

    @Test
    public void testSearchResponse() {
        Map<String, Object> response = elasticSearchUtil.searchResponse( "product", getContentFromClasspath("/com/sample/ecommerce/util/queries/search_products.json"));
        Assert.assertNotNull(response);
    }

    @Test
    public void testSearchTemplateResponse() throws IOException {
        List<String> categoriesToLookFor = new ArrayList<>();
        categoriesToLookFor.add("lcdtv");
        categoriesToLookFor.add("laptops");
        Map<String,Object> browseQuery = new HashMap<>();
        browseQuery.put("categories", categoriesToLookFor);
        AggregatedResults response = elasticSearchUtil.aggregatedSearch( "product", "browse_products",browseQuery);
        Assert.assertNotNull(response);
    }

}
