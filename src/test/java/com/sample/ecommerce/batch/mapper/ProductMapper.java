/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sample.ecommerce.batch.mapper;

import com.sample.ecommerce.domain.Product;
import java.util.Arrays;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

/**
 *
 * @author WZ07
 */
public class ProductMapper implements FieldSetMapper<Product> {

    private Integer productId = 0;

    @Override
    public Product mapFieldSet(FieldSet fieldSet) throws BindException {
        productId++;
        Product product = new Product();
        product.setId(productId.toString());
        product.setTitle(fieldSet.readString(0));
        product.setBrand(fieldSet.readString(1));
        String categoriesTxt = fieldSet.readString(2);
        if (categoriesTxt != null
                && categoriesTxt.trim().length() != 0) {
            product.setCategories(Arrays.asList(categoriesTxt.substring(1, categoriesTxt.length() - 1).split(":")));
        }
        product.setImageUrl(fieldSet.readString(3));
        product.setOperatingSystem(fieldSet.readString(4).trim().length() == 0 ? null : fieldSet.readString(4));
        return product;
    }

}
