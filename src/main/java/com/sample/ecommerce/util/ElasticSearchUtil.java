/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sample.ecommerce.util;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.sample.ecommerce.BatchConfiguration;
import com.sample.ecommerce.domain.AggregatedResults;
import com.sample.ecommerce.domain.Bucket;
import com.sample.ecommerce.domain.BucketItem;
import com.sample.ecommerce.domain.TextItem;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import static org.zols.datastore.util.JsonUtil.asMap;

@Component
public class ElasticSearchUtil {

    @Value("${spring.application.name}")
    private String indexName;

    //http://teknosrc.com/execute-raw-elasticsearch-query-using-transport-client-java-api/
    @Autowired
    private Client client;

    public AggregatedResults aggregatedSearch(String type, String template, Object model) {
        AggregatedResults aggregatedResults = new AggregatedResults();
        Map<String, Object> searchResponse = searchResponse(type, template, model);
        aggregatedResults.setItems(resultsOf(searchResponse));
        aggregatedResults.setBuckets(bucketsOf(searchResponse));
        return aggregatedResults;
    }

    public List<Map<String, Object>> search(String type, String template, Object model) {
        return resultsOf(searchResponse(type, template, model));
    }

    private List<Bucket> bucketsOf(Map<String, Object> searchResponse) {
        List<Bucket> buckets = null;
        if (searchResponse != null) {
            Map<String, Object> aggregations = (Map<String, Object>) searchResponse.get("aggregations");
            if (aggregations != null) {
                Bucket bucket;
                BucketItem bucketItem;
                List<BucketItem> bucketItems;
                List<Map<String, Object>> bucketsMaps;

                buckets = new ArrayList<>();
                for (Map.Entry<String, Object> entrySet : aggregations.entrySet()) {
                    bucket = new Bucket();
                    bucket.setName(entrySet.getKey());
                    bucketsMaps = (List<Map<String, Object>>) ((Map<String, Object>) entrySet.getValue()).get("buckets");

                    bucketItems = new ArrayList<>();
                    for (Map<String, Object> bucketsMap : bucketsMaps) {
                        bucketItem = new TextItem();
                        bucketItem.setName(bucketsMap.get("key").toString());
                        bucketItem.setCount((Integer) bucketsMap.get("doc_count"));
                        bucketItems.add(bucketItem);
                    }
                    bucket.setItems(bucketItems);
                    buckets.add(bucket);
                }

            }
        }
        return buckets;
    }

    private List<Map<String, Object>> resultsOf(Map<String, Object> searchResponse) {
        List<Map<String, Object>> list = null;
        if (searchResponse != null) {
            Integer noOfRecords = (Integer) ((Map<String, Object>) searchResponse.get("hits")).get("total");
            if (0 != noOfRecords) {
                List<Map<String, Object>> recordsMapList = (List<Map<String, Object>>) ((Map<String, Object>) searchResponse.get("hits")).get("hits");
                list = new ArrayList<>(recordsMapList.size());
                for (Map<String, Object> recordsMapList1 : recordsMapList) {
                    list.add((Map<String, Object>) recordsMapList1.get("_source"));
                }
            }
        }
        return list;
    }

    public Map<String, Object> searchResponse(String type, String query) {
        SearchResponse response = client.prepareSearch(indexName)
                .setTypes(type)
                .setSource(query)
                .execute()
                .actionGet();
        client.close();
        return asMap(response.toString());
    }

    public Map<String, Object> searchResponse(String type, String template, Object model) {
        SearchResponse response = client.prepareSearch(indexName)
                .setTypes(type)
                .setSource(render(template, model))
                .execute()
                .actionGet();
        client.close();
        return asMap(response.toString());
    }

    public static String getContentFromClasspath(String resourcePath) {
        InputStream inputStream = BatchConfiguration.class.getResourceAsStream(resourcePath);
        java.util.Scanner scanner = new java.util.Scanner(inputStream, "UTF-8").useDelimiter("\\A");
        String theString = scanner.hasNext() ? scanner.next() : "";
        scanner.close();
        return theString;
    }

    public static String render(String templateName, Object model) {
        Writer writer = new StringWriter();
        MustacheFactory mf = new DefaultMustacheFactory();
        Mustache mustache = mf.compile(new StringReader(getContentFromClasspath("/search/templates/" + templateName + ".mustache")), "example");
        mustache.execute(writer, model);
        try {
            writer.flush();
        } catch (IOException ex) {
            Logger.getLogger(ElasticSearchUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return writer.toString();
    }

}
