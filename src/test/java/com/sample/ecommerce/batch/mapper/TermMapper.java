/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sample.ecommerce.batch.mapper;

import com.sample.ecommerce.domain.Term;
import java.util.Arrays;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

/**
 *
 * @author WZ07
 */
public class TermMapper implements FieldSetMapper<Term> {

    @Override
    public Term mapFieldSet(FieldSet fieldSet) throws BindException {
        Term term = new Term();
        term.setTerm(fieldSet.readString(0));
        term.setVisits(Integer.parseInt(fieldSet.readString(1)));
        String productsTxt = fieldSet.readString(2);
        if (productsTxt != null
                && productsTxt.trim().length() != 0) {
            term.setProducts(Arrays.asList(productsTxt.substring(1, productsTxt.length() - 1).split(":")));
        }
        return term;
    }

}
