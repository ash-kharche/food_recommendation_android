package com.done.recommendation.database;

public class DatabaseHelper {
    public interface Tables {
        String COLLECTIONS = "collections";
        String PRODUCTS = "products";
        String CART = "cart";
        String ORDERS = "orders";
        String KEYS = "keys";
        String TRENDING_PRODUCTS = "trending_products";
    }

    public interface Keys {
        String _ID = "_id";
        String VALUE = "value";

        String USER_ID = "user_id";
        String USER_EMAIL = "user_email";
        String USER_NAME = "user_name";
        String USER_MOBILE = "user_mobile";
        String QUESTIONS_DONE = "questions_done";

        String IS_VEG = "is_veg";
        String IS_DIABETES = "is_diabetes";
        String IS_CHOLESTROL = "is_cholestrol";
        String IS_KID = "is_kid";
        String IS_SENIOR = "is_senior";
    }

    public interface Cart {
        String PRODUCT_ID = "product_id";
        String PRODUCT_NAME = "product_name";
        String PRODUCT_IMAGE_URL = "product_image_url";
        String COLLECTION_ID = "collection_id";
        String COLLECTION_NAME = "collection_name";
        String PRICE = "price";
        String QUANTITY = "quantity";
        String INGREDIENTS = "ingredients";
    }

    public interface Product {
        String PRODUCT_ID = "product_id";
        String NAME = "name";
        String DESCRIPTION = "description";
        String PRICE = "price";
        String RATING = "rating";
        String COLLECTION_ID = "collection_id";
        String COLLECTION_NAME = "collection_name";
        String IMAGE_URL = "image_url";
        String IS_VEG = "is_veg";
        String INGREDIENTS = "ingredients";
        String INGREDIENTS_TEXT = "ingredients_text";
        String FATS = "fats";
        String PROTIENS = "protiens";
        String CARBS = "carbs";
    }

    public interface TrendingProducts extends Product {
    }


    public interface Collection {
        String COLLECTION_ID = "collection_id";
        String NAME = "name";
        String DESCRIPTION = "description";
        String IMAGE_URL = "image_url";
    }
}