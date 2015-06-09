/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sample.ecommerce.service;

import com.sample.ecommerce.domain.Category;
import com.sample.ecommerce.domain.Navigation;
import com.sample.ecommerce.domain.NavigationFilter;
import static com.sample.ecommerce.domain.NavigationFilter.Operator.*;
import com.sample.ecommerce.domain.Product;
import com.sample.ecommerce.domain.ProductsList;
import com.sample.ecommerce.domain.TextItem;
import com.sample.ecommerce.repositories.CategoryRepository;
import com.sample.ecommerce.repositories.ProductRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static java.util.stream.Collectors.toList;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.collect.Lists;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import static org.elasticsearch.search.aggregations.AggregationBuilders.terms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    public ProductsList listProducts(String categoryName) {
        return listProducts(categoryName, null);
    }

    private QueryBuilder getQueryBuilder(NavigationFilter navigationFilter) {
        QueryBuilder queryBuilder = null;
        switch (navigationFilter.getOperator()) {
            case EXISTS_IN:
                queryBuilder = QueryBuilders.termsQuery(navigationFilter.getName(), navigationFilter.getValue());
                break;
        }
        return queryBuilder;
    }

    private QueryBuilder getQueryBuilder(List<NavigationFilter> navigationFilters) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        Map<String, List<NavigationFilter>> groups = new HashMap<>();

        for (NavigationFilter navigationFilter : navigationFilters) {
            groups.getOrDefault(navigationFilter.getName(), new ArrayList<>()).add(navigationFilter);
        }

        for (Map.Entry<String, List<NavigationFilter>> entrySet : groups.entrySet()) {
            String key = entrySet.getKey();
            List<NavigationFilter> value = entrySet.getValue();
            if (value.size() == 1) {
                boolQuery.must(getQueryBuilder(value.get(0)));
            } else {

            }
        }

        return boolQuery;
    }

    public ProductsList listProducts(String categoryName, List<NavigationFilter> navigationFilters) {

        if (navigationFilters == null) {
            navigationFilters = new ArrayList<>(1);
        }

        NavigationFilter navigationFilter
                = new NavigationFilter("categories", EXISTS_IN, categoriesToLookForProducts(categoryName));

        navigationFilters.add(navigationFilter);

        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder()
                .withIndices("ecommerce").withTypes("products")
                .addAggregation(terms("brand").field("brand"))
                .withQuery(getQueryBuilder(navigationFilters));

        if (categoryName.equals("mobiles")) {
            queryBuilder.addAggregation(terms("operatingSystem").field("operatingSystem"));
        }

        return elasticsearchTemplate.query(queryBuilder.build(), new ResultsExtractor<ProductsList>() {
            @Override
            public ProductsList extract(SearchResponse response) {
                ProductsList productsList = new ProductsList();

                List<Aggregation> aggregations = response.getAggregations().asList();

                productsList.setNavigations(
                        aggregations.stream().map(aggregation -> {
                            Navigation navigation = new Navigation();
                            navigation.setName(aggregation.getName());
                            if (aggregation instanceof StringTerms) {
                                StringTerms stringTerms = (StringTerms) aggregation;
                                navigation.setItems(
                                        stringTerms.getBuckets().stream().map(bucket -> {
                                            TextItem textItem = new TextItem();
                                            textItem.setName(bucket.getKey());
                                            textItem.setCount(bucket.getDocCount());
                                            return textItem;
                                        }).collect(toList()));
                            }

                            return navigation;
                        }).collect(toList()));

                List<Map> products = new ArrayList<>();

                SearchHit[] searchHits = response.getHits().hits();
                for (SearchHit searchHit : searchHits) {
                    products.add(searchHit.getSource());
                }

                productsList.setProducts(products);

                return productsList;
            }
        });
    }

    public List<Category> getParents(String categoryId) {
        List<Category> parents = new ArrayList<>();
        Category category;
        String parent;
        category = findOne(categoryId);
        while (category != null) {
            parent = category.getParent();
            if (parent != null) {
                category = findOne(parent);
                parents.add(category);
            } else {
                break;
            }
        }
        return (parents.isEmpty()) ? null : Lists.reverse(parents);
    }

    public List<Category> getChildren(String categoryId) {
        List<Category> children = categoryRepository.findByParent(categoryId);
        if (children != null) {
            for (Category child : children) {
                child.setChildren(getChildren(child.getId()));
            }
        }
        return children.size() > 0 ? children : null;
    }

    private String[] categoriesToLookForProducts(String categoryId) {
        List<String> categoriesToLookFor = new ArrayList<>();
        wallThroughChildren(categoriesToLookFor, categoryId);
        return categoriesToLookFor.toArray(new String[categoriesToLookFor.size()]);
    }

    public List<Product> findByCategory(String categoryId) {
        return productRepository.findByCategoriesIn(categoriesToLookForProducts(categoryId));
    }

    private void wallThroughChildren(List<String> categoriesToLookFor, String categoryId) {
        List<Category> children = getChildren(categoryId);
        categoriesToLookFor.add(categoryId);
        if (children != null) {
            for (Category child : children) {
                categoriesToLookFor.add(child.getId());
                wallThroughChildren(categoriesToLookFor, child.getId());
            }
        }
    }

    public <S extends Category> Iterable<S> save(Iterable<S> itrbl) {
        return categoryRepository.save(itrbl);
    }

    public Category findOne(String id) {
        Category category = categoryRepository.findOne(id);
        if (category != null) {
            category.setChildren(getChildren(id));
        }
        return category;
    }

    public void deleteAll() {
        categoryRepository.deleteAll();
    }

}
