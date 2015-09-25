/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sample.ecommerce.domain;

import java.util.List;
import java.util.Map;

/**
 *
 * @author WZ07
 */
public class ProductsList {

    private List<Navigation> navigations;
    private List<Map> products;

    public List<Navigation> getNavigations() {
        return navigations;
    }

    public void setNavigations(List<Navigation> navigations) {
        this.navigations = navigations;
    }

    public List<Map> getProducts() {
        return products;
    }

    public void setProducts(List<Map> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return "ProductsList{" + "navigations=" + navigations + ", products=" + products + '}';
    }

}
