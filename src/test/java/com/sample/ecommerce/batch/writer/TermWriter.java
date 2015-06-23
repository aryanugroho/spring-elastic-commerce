/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sample.ecommerce.batch.writer;

import com.sample.ecommerce.domain.Term;
import com.sample.ecommerce.service.TermService;
import java.util.List;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TermWriter implements ItemWriter<Term> {

    @Autowired
    private TermService termService;

    @Override
    public void write(List<? extends Term> terms) throws Exception {
        termService.save((Iterable<Term>) terms);
    }

}
