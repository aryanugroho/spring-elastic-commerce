/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sample.ecommerce.domain;

import java.util.List;

/**
 *
 * @param <T>
 */
public class Bucket<T extends BucketItem> {

    private String name;
    
    private String type;

    private List<T> items;

    public Bucket(String name, String type) {
        this.name = name;
        this.type = type;
    }    
    
    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }    

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "Bucket{" + "name=" + name + ", type=" + type + ", items=" + items + '}';
    }

}
