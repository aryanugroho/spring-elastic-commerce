/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sample.ecommerce.batch.mapper;


import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

/**
 *
 * @author WZ07
 */
public class ProductMapper implements FieldSetMapper<Map<String,Object>> {

    @Override
    public Map<String,Object> mapFieldSet(FieldSet fieldSet) throws BindException {
        Map<String,Object> product = new HashMap<>(4);
        product.put("id",fieldSet.readString(0));
        product.put("title",fieldSet.readString(1));
        if(fieldSet.readString(2).trim().length() != 0) {
            product.put("brand",fieldSet.readString(2));
        }        
        String categoriesTxt = fieldSet.readString(3);
        if (categoriesTxt != null
                && categoriesTxt.trim().length() != 0) {
            product.put("categories",Arrays.asList(categoriesTxt.substring(1, categoriesTxt.length() - 1).split(":")));
        }
        product.put("imageUrl",fieldSet.readString(4));
        return product;
    }

}
