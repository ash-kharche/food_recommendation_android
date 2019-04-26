package com.done.recommendation.network.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Product implements Serializable {
    @SerializedName("product_id")
    @Expose
    private int productId;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("price")
    @Expose
    private int price;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("collection_id")
    @Expose
    private int collectionId;
    @SerializedName("collection_name")
    @Expose
    private String collectionName;
    @SerializedName("is_veg")
    @Expose
    private int isVeg;
    @SerializedName("rating")
    @Expose
    private float rating;
    @SerializedName("ingredients")
    @Expose
    private List<Integer> ingredients = new ArrayList<>();
    @SerializedName("ingredient_text")
    @Expose
    private String ingredientText;
    @SerializedName("fats")
    @Expose
    private float fats;
    @SerializedName("protiens")
    @Expose
    private float protiens;
    @SerializedName("carbs")
    @Expose
    private float carbs;

    private int quantity; //Used for DB purpose only

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

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

    public int getIsVeg() {
        return isVeg;
    }

    public void setIsVeg(int isVeg) {
        this.isVeg = isVeg;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public List<Integer> getIngredients() {
        return ingredients;
    }

    public void setIngredients(String s) {
        ingredients.clear();

        s = s.replace("[", "");
        s = s.replace("]", "");

        String[] array = s.split(", ");
        for (int i = 0; i < array.length; i++) {
            ingredients.add(Integer.parseInt(array[i]));
        }
    }

    public void setIngredients(List<Integer> ingredients) {
        this.ingredients = ingredients;
    }

    public String getIngredientText() {
        return ingredientText;
    }

    public void setIngredientText(String ingredientText) {
        this.ingredientText = ingredientText;
    }

    public String getFats() {
        return fats + " gms";
    }

    public void setFats(float fats) {
        this.fats = fats;
    }

    public String getProtiens() {
        return protiens + " gms";
    }

    public void setProtiens(float protiens) {
        this.protiens = protiens;
    }

    public String getCarbs() {
        return carbs + " kcal";
    }

    public void setCarbs(float carbs) {
        this.carbs = carbs;
    }
}
