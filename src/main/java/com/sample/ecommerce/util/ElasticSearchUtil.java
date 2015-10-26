/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sample.ecommerce.util;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.sample.ecommerce.domain.AggregatedResults;
import com.sample.ecommerce.domain.Bucket;
import com.sample.ecommerce.domain.BucketItem;
import com.sample.ecommerce.domain.MinMaxItem;
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
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import static org.slf4j.LoggerFactory.getLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import static org.zols.datastore.elasticsearch.ElasticSearchDataStore.getQueryBuilder;
import org.zols.datastore.query.Query;
import static org.zols.datastore.util.JsonUtil.asMap;
import static org.zols.datastore.util.JsonUtil.asString;

@Component
public class ElasticSearchUtil {

    private static final org.slf4j.Logger LOGGER = getLogger(ElasticSearchUtil.class);

    @Value("${spring.application.name}")
    private String indexName;

    //http://teknosrc.com/execute-raw-elasticsearch-query-using-transport-client-java-api/
    @Autowired
    private Client client;

    public AggregatedResults aggregatedSearch(String type,
            String template,
            Map<String, Object> browseQuery,
            Pageable pageable, Query query) {
        AggregatedResults aggregatedResults = new AggregatedResults();
        browseQuery.put("size", pageable.getPageSize());
        browseQuery.put("from", (pageable.getPageNumber() * pageable.getPageSize()));
        Map<String, Object> searchResponse = searchResponse(type, template, browseQuery, query);
        aggregatedResults.setPage(resultsOf(searchResponse, pageable));
        aggregatedResults.setBuckets(bucketsOf(searchResponse));
        return aggregatedResults;
    }

    public AggregatedResults aggregatedSearch(String type,
            String template,
            Map<String, Object> browseQuery,
            Pageable pageable) {
        return aggregatedSearch(type, template, browseQuery, pageable, null);
    }

//    public List<Map<String, Object>> search(String type, String template, Object model) {
//        return resultsOf(searchResponse(type, template, model));
//    }
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
                String aggregationName;
                for (Map.Entry<String, Object> entrySet : aggregations.entrySet()) {

                    aggregationName = entrySet.getKey();
                    if (!aggregationName.startsWith("max_")) {
                        bucket = new Bucket();
                        if (!aggregationName.startsWith("min_")) {
                            bucket.setName(aggregationName);
                            bucketsMaps = (List<Map<String, Object>>) ((Map<String, Object>) entrySet.getValue()).get("buckets");
                            bucketItems = new ArrayList<>();
                            for (Map<String, Object> bucketsMap : bucketsMaps) {
                                bucketItem = new TextItem();
                                bucketItem.setName(bucketsMap.get("key").toString());
                                bucketItem.setCount((Integer) bucketsMap.get("doc_count"));
                                bucketItems.add(bucketItem);
                            }
                            bucket.setItems(bucketItems);
                        } else {
                            bucket.setName(aggregationName.replaceAll("min_", ""));
                            bucketsMaps = (List<Map<String, Object>>) ((Map<String, Object>) entrySet.getValue()).get("buckets");
                            bucketItems = new ArrayList<>();

                            bucketItem = new MinMaxItem();
                            ((MinMaxItem) bucketItem).setMin(1);
                            ((MinMaxItem) bucketItem).setMax(8);
//                                bucketItem.setName(bucketsMap.get("key").toString());
//                                bucketItem.setCount((Integer) bucketsMap.get("doc_count"));
                            bucketItems.add(bucketItem);

                            bucket.setItems(bucketItems);
                        }
                        buckets.add(bucket);
                    }

                }

            }
        }
        return buckets;
    }

    private Page<List> resultsOf(Map<String, Object> searchResponse, Pageable pageable) {
        Page<List> page = null;
        List<Map<String, Object>> list = null;
        if (searchResponse != null) {
            Integer noOfRecords = (Integer) ((Map<String, Object>) searchResponse.get("hits")).get("total");

            if (0 != noOfRecords) {
                List<Map<String, Object>> recordsMapList = (List<Map<String, Object>>) ((Map<String, Object>) searchResponse.get("hits")).get("hits");
                list = new ArrayList<>(recordsMapList.size());
                for (Map<String, Object> recordsMapList1 : recordsMapList) {
                    list.add((Map<String, Object>) recordsMapList1.get("_source"));
                }
                page = new PageContentImpl(list, pageable, noOfRecords);
            }
        }
        return page;
    }

    public Map<String, Object> searchResponse(String type, String queryText, Query query) {

        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(indexName)
                .setTypes(type);
        if (query != null) {
            QueryBuilder builder = getQueryBuilder(query);
            Map<String, Object> queryAsMap = asMap(queryText);
            List filter = (List) ((Map<String, Object>) ((Map<String, Object>) queryAsMap.get("query")).get("bool")).get("filter");
            filter.add(asMap(builder.toString()));
            String filterAppendedQuery = asString(queryAsMap);
            LOGGER.debug("Executing Elastic Search Query\n{}", filterAppendedQuery);
            searchRequestBuilder
                    .setSource(filterAppendedQuery);
        } else {
            LOGGER.debug("Executing Elastic Search Query\n{}", queryText);
            searchRequestBuilder
                    .setSource(queryText);
        }
        SearchResponse response = searchRequestBuilder
                .execute()
                .actionGet();
        client.close();
        return asMap(response.toString());
    }

    public Map<String, Object> searchResponse(String type, String queryText) {
        return searchResponse(type, queryText, null);
    }

    public Map<String, Object> searchResponse(String type, String template, Object model, Query query) {
        return searchResponse(type, render(template, model), query);
    }

    public Map<String, Object> searchResponse(String type, String template, Object model) {
        return searchResponse(type, template, model, null);
    }

    public static String getContentFromClasspath(String resourcePath) {
        InputStream inputStream = ElasticSearchUtil.class.getResourceAsStream(resourcePath);
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
