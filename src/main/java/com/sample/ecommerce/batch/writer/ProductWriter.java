/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sample.ecommerce.batch.writer;

import java.util.List;
import org.springframework.batch.item.ItemWriter;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zols.datastore.service.DataService;
import static org.zols.datastore.util.JsonUtil.asMap;

@Component
public class ProductWriter implements ItemWriter<Map<String, Object>> {

    @Autowired
    private DataService dataService;

    @Override
    public void write(List<? extends Map<String, Object>> products) throws Exception {
        for (Map<String, Object> product : products) {
            dataService.create("product", asMap(product));
        }
    }

}
