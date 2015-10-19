/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sample.ecommerce.util;

import java.util.List;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author sathish
 */
public class PageContentImpl extends PageImpl {

    private List items;

    public PageContentImpl(List content, Pageable pageable, long total) {
        super(content, pageable, total);
        items = items;
    }

    public List getItems() {
        return items;
    }

}
