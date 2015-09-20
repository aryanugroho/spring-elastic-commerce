package com.sample.ecommerce.test.aggregation;

import com.sample.ecommerce.Application;
import java.util.List;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.Aggregations;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.elasticsearch.action.search.SearchType.COUNT;
import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import org.elasticsearch.search.aggregations.Aggregation;
import static org.elasticsearch.search.aggregations.AggregationBuilders.terms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

/**
 * @author Rizwan Idrees
 * @author Mohsin Husen
 * @author Jonathan Yan
 * @author Artur Konczak
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class})
public class ElasticsearchTemplateAggregationTests {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Test
    public void shouldReturnAggregatedResponseForGivenSearchQuery() {
        // given
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchAllQuery())
                .withSearchType(COUNT)
                .withIndices("ecommerce").withTypes("products")
                .addAggregation(terms("brand").field("brand"))
                .build();
        // when
        Aggregations aggregations = elasticsearchTemplate.query(searchQuery,
                (SearchResponse response) -> {
                    System.out.println(response);
                    return response.getAggregations();
                });
        
        List<Aggregation> aggregationsList = aggregations.asList();
        for (Aggregation aggregation : aggregationsList) {
            if(aggregation instanceof StringTerms) {
                StringTerms stringTerms = (StringTerms) aggregation;
                System.out.println("aggregation Name" + aggregation.getName());          
            }
            
        }
        
        // then
        assertThat(aggregations, is(notNullValue()));
        assertThat(aggregations.asMap().get("brand"), is(notNullValue()));
    }
}
