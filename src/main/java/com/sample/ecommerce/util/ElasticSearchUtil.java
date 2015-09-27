/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sample.ecommerce.util;

import java.util.Map;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import static org.zols.datastore.util.JsonUtil.asMap;

@Component
public class ElasticSearchUtil {
//http://teknosrc.com/execute-raw-elasticsearch-query-using-transport-client-java-api/

    @Autowired
    private Client client;

    public Map<String, Object> searchResponse(String indexName,String type,String query) {
        SearchResponse response = client.prepareSearch(indexName)
                .setTypes(type)
                .setSource(query)
                .execute()
                .actionGet();
        client.close();
        return asMap(response.toString());
    }

}
