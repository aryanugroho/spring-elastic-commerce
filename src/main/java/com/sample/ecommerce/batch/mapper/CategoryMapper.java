/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sample.ecommerce.batch.mapper;

import com.sample.ecommerce.domain.Category;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

/**
 *
 * @author WZ07
 */
public class CategoryMapper implements FieldSetMapper<Category> {

    @Override
    public Category mapFieldSet(FieldSet fieldSet) throws BindException {
        Category category = new Category();        
        category.setId(fieldSet.readString (0));
        category.setLabel(fieldSet.readString (1));
        String parent = fieldSet.readString (2);
        category.setParent((parent.trim().length() == 0 ? null : parent));
        return category;
    }
    
}
