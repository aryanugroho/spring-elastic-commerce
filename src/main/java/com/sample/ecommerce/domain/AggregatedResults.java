/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sample.ecommerce.domain;

import java.util.List;
import org.springframework.data.domain.Page;

public class AggregatedResults {

    private List<Bucket> buckets;
    private Page<List> page;

    public List<Bucket> getBuckets() {
        return buckets;
    }

    public void setBuckets(List<Bucket> buckets) {
        this.buckets = buckets;
    }

    public Page<List> getPage() {
        return page;
    }

    public void setPage(Page<List> page) {
        this.page = page;
    }

}
