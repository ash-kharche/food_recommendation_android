package com.done.recommendation.network.response;

import java.util.List;

public class MenuData {

    List<Product> products;
    List<Product> trending_products;
    List<Collections> collections;
    List<Product> recommendation_products;

    public List<Product> getProducts() {
        return products;
    }

    public List<Product> getTrending() {
        return trending_products;
    }

    public List<Collections> getCollections() {
        return collections;
    }

    public List<Product> getRecommendation() {
        return recommendation_products;
    }
}