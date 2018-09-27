package com.example.android.foodreco.network.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Category {
    @SerializedName("category_id")
    @Expose
    private int categoryId;
    @SerializedName("category_name")
    @Expose
    private String categoryName;
    @SerializedName("no_of_products")
    @Expose
    private int noOfProducts;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;

    @SerializedName("products")
    @Expose
    private List<Product> products;

    public int getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public int getNoOfProducts() {
        return noOfProducts;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public List<Product> getProducts() {
        return products;
    }
}
