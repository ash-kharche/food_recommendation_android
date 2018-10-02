package com.example.android.foodreco.network.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Collections {
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

    public String getCollectionName() {
        return collectionName;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
