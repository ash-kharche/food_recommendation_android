package com.done.recommendation.network.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Collections implements Serializable {
    @SerializedName("collection_id")
    @Expose
    private int collectionId;
    @SerializedName("collection_name")
    @Expose
    private String collectionName;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;

    public int getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(int collectionId) {
        this.collectionId = collectionId;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
