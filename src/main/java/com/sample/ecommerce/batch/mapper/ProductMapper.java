/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sample.ecommerce.batch.mapper;

import com.sample.ecommerce.domain.Product;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

/**
 *
 * @author WZ07
 */
public class ProductMapper implements FieldSetMapper<Product> {

    @Override
    public Product mapFieldSet(FieldSet fieldSet) throws BindException {
        Product product = new Product();
        product.setId(fieldSet.readString(0));
        product.setTitle(fieldSet.readString(1));
        product.setBrand(fieldSet.readString(2));
        String categoriesTxt = fieldSet.readString(3);
        if (categoriesTxt != null
                && categoriesTxt.trim().length() != 0) {
            product.setCategories(Arrays.asList(categoriesTxt.substring(1, categoriesTxt.length() - 1).split(":")));
        }
        return product;
    }

}
