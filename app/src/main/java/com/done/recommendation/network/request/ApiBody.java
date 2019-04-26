package com.done.recommendation.network.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApiBody {
    @SerializedName("user_id")
    @Expose
    private int userId;
    @SerializedName("is_veg")
    @Expose
    private int isVeg;
    @SerializedName("is_diabetes")
    @Expose
    private int isDiabetes;
    @SerializedName("is_cholestrol")
    @Expose
    private int isCholestrol;
    @SerializedName("is_kid")
    @Expose
    private int isKid;
    @SerializedName("is_senior")
    @Expose
    private int isSenior;
    @SerializedName("collections")
    @Expose
    private String collections;
    @SerializedName("products")
    @Expose
    private String products;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getIsVeg() {
        return isVeg;
    }

    public void setIsVeg(int isVeg) {
        this.isVeg = isVeg;
    }

    public int getIsDiabetes() {
        return isDiabetes;
    }

    public void setIsDiabetes(int isDiabetes) {
        this.isDiabetes = isDiabetes;
    }

    public int getIsCholestrol() {
        return isCholestrol;
    }

    public void setIsCholestrol(int isCholestrol) {
        this.isCholestrol = isCholestrol;
    }

    public int getIsKid() {
        return isKid;
    }

    public void setIsKid(int isKid) {
        this.isKid = isKid;
    }

    public int getIsSenior() {
        return isSenior;
    }

    public void setIsSenior(int isSenior) {
        this.isSenior = isSenior;
    }

    public String getCollections() {
        return collections;
    }

    public void setCollections(String collections) {
        this.collections = collections;
    }

    public String getProducts() {
        return products;
    }

    public void setProducts(String products) {
        this.products = products;
    }
}
