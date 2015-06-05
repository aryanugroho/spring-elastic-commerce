/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sample.ecommerce.domain;

import java.util.List;

/**
 *
 * @author WZ07
 * @param <T>
 */
public class Navigation<T extends NavigationItem> {

    private String name;

    private List<T> items;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "Navigation{" + "name=" + name + ", items=" + items + '}';
    }

    
}
