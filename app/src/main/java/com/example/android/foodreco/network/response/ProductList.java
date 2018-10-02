package com.example.android.foodreco.network.response;

import java.util.List;

public class ProductList {

    List<Product> trending;
    List<Collections> categories;
    List<Product> recommendation;

    public List<Product> getTrending() {
        return trending;
    }

    public List<Collections> getCategories() {
        return categories;
    }

    public List<Product> getRecommendation() {
        return recommendation;
    }
}
