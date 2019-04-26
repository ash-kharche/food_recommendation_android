package com.done.recommendation.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.done.recommendation.Constants;
import com.done.recommendation.network.response.Collections;
import com.done.recommendation.network.response.MenuData;
import com.done.recommendation.network.response.Product;
import com.done.recommendation.network.response.User;

import java.util.ArrayList;
import java.util.List;

public class DbOperations {

    private static final String TAG = Constants.TAG;

    public synchronized static List<Collections> isDatabaseLoaded(Context context) {
        List<Collections> collectionsList = null;
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(DatabaseProvider.URI_COLLECTIONS,
                    null, null, null, null);

            Collections collection = null;
            if (cursor != null && cursor.getCount() > 0) {
                collectionsList = new ArrayList<>(cursor.getCount());

                while (cursor.moveToNext()) {
                    collection = new Collections();

                    int collectionId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.Collection.COLLECTION_ID));
                    collection.setCollectionId(collectionId);
                    collection.setCollectionName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.Collection.NAME)));
                    collection.setImageUrl(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.Collection.IMAGE_URL)));
                    collectionsList.add(collection);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "isDatabaseLoaded", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return collectionsList;
    }

    public synchronized static List<Product> getProductsOfCollection(Context context, int collectionId) {
        List<Product> productList = null;
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(DatabaseProvider.URI_PRODUCTS,
                    null,
                    DatabaseHelper.Product.COLLECTION_ID + " = ?",
                    new String[]{String.valueOf(collectionId)}, null, null);

            Product product = null;
            if (cursor != null && cursor.getCount() > 0) {
                productList = new ArrayList<>(cursor.getCount());
                while (cursor.moveToNext()) {
                    product = new Product();
                    product.setProductId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.Product.PRODUCT_ID)));
                    product.setProductName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.Product.NAME)));
                    product.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.Product.DESCRIPTION)));
                    product.setCollectionId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.Product.COLLECTION_ID)));
                    product.setCollectionName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.Product.COLLECTION_NAME)));
                    product.setImageUrl(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.Product.IMAGE_URL)));
                    product.setRating(cursor.getFloat(cursor.getColumnIndexOrThrow(DatabaseHelper.Product.RATING)));
                    product.setIsVeg(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.Product.IS_VEG)));
                    product.setPrice(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.Product.PRICE)));
                    product.setIngredients(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.Product.INGREDIENTS)));
                    product.setIngredientText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.Product.INGREDIENTS_TEXT)));
                    product.setFats(cursor.getFloat(cursor.getColumnIndexOrThrow(DatabaseHelper.Product.FATS)));
                    product.setProtiens(cursor.getFloat(cursor.getColumnIndexOrThrow(DatabaseHelper.Product.PROTIENS)));
                    product.setCarbs(cursor.getFloat(cursor.getColumnIndexOrThrow(DatabaseHelper.Product.CARBS)));
                    productList.add(product);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "getProductsOfCollection", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return productList;
    }

    public synchronized static List<Product> getTrendingProducts(Context context) {
        List<Product> productList = null;
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(DatabaseProvider.URI_TRENDING_PRODUCTS,
                    null, null, null, null, null);

            Product product = null;
            if (cursor != null && cursor.getCount() > 0) {
                productList = new ArrayList<>(cursor.getCount());
                while (cursor.moveToNext()) {
                    product = new Product();
                    product.setProductId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.Product.PRODUCT_ID)));
                    product.setProductName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.Product.NAME)));
                    product.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.Product.DESCRIPTION)));
                    product.setCollectionId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.Product.COLLECTION_ID)));
                    product.setCollectionName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.Product.COLLECTION_NAME)));
                    product.setImageUrl(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.Product.IMAGE_URL)));
                    product.setRating(cursor.getFloat(cursor.getColumnIndexOrThrow(DatabaseHelper.Product.RATING)));
                    product.setIsVeg(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.Product.IS_VEG)));
                    product.setPrice(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.Product.PRICE)));
                    product.setIngredients(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.Product.INGREDIENTS)));
                    product.setIngredientText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.Product.INGREDIENTS_TEXT)));
                    product.setFats(cursor.getFloat(cursor.getColumnIndexOrThrow(DatabaseHelper.Product.FATS)));
                    product.setProtiens(cursor.getFloat(cursor.getColumnIndexOrThrow(DatabaseHelper.Product.PROTIENS)));
                    product.setCarbs(cursor.getFloat(cursor.getColumnIndexOrThrow(DatabaseHelper.Product.CARBS)));
                    productList.add(product);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "getTrendingProducts", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return productList;
    }

    public synchronized static void insertMenu(Context context, MenuData menuData) {
        Log.d(Constants.TAG, "menuData  " + menuData);
        if (menuData != null) {
            insertCollections(context, menuData.getCollections());
            insertProducts(context, menuData.getProducts());
            insertTrendingProducts(context, menuData.getTrending());
        }
    }

    private synchronized static void insertCollections(Context context, List<Collections> collectionList) {
        Log.d(TAG, "collections:  " + collectionList.size());
        List<ContentValues> contentValues = new ArrayList();
        ContentValues values = new ContentValues();
        try {
            for (Collections collection : collectionList) {

                values.clear();
                values.put(DatabaseHelper.Collection.COLLECTION_ID, collection.getCollectionId());
                values.put(DatabaseHelper.Collection.NAME, collection.getCollectionName());
                values.put(DatabaseHelper.Collection.DESCRIPTION, collection.getCollectionName());
                values.put(DatabaseHelper.Collection.IMAGE_URL, collection.getImageUrl());
                context.getContentResolver().insert(DatabaseProvider.URI_COLLECTIONS, values);

            }
        } catch (Exception e) {
            Log.e(TAG, "Error insertMenu", e);
        } finally {
            values.clear();
        }
    }

    private synchronized static void insertProducts(Context context, List<Product> productList, int collectionId, String collectionName) {

        List<ContentValues> contentValues = new ArrayList();
        ContentValues values = new ContentValues();
        try {
            for (Product product : productList) {

                values.clear();
                values.put(DatabaseHelper.Product.PRODUCT_ID, product.getProductId());
                values.put(DatabaseHelper.Product.NAME, product.getProductName());
                values.put(DatabaseHelper.Product.DESCRIPTION, product.getDescription());
                values.put(DatabaseHelper.Product.IMAGE_URL, product.getImageUrl());
                values.put(DatabaseHelper.Product.COLLECTION_ID, collectionId);
                values.put(DatabaseHelper.Product.COLLECTION_NAME, collectionName);
                values.put(DatabaseHelper.Product.IS_VEG, product.getIsVeg());
                values.put(DatabaseHelper.Product.PRICE, product.getPrice());
                values.put(DatabaseHelper.Product.RATING, product.getRating());
                values.put(DatabaseHelper.Product.INGREDIENTS, product.getIngredients().toString());
                values.put(DatabaseHelper.Product.INGREDIENTS_TEXT, product.getIngredientText());
                values.put(DatabaseHelper.Product.FATS, product.getFats());
                values.put(DatabaseHelper.Product.PROTIENS, product.getProtiens());
                values.put(DatabaseHelper.Product.CARBS, product.getCarbs());
                context.getContentResolver().insert(DatabaseProvider.URI_PRODUCTS, values);

            }

        } catch (Exception e) {
            Log.e(TAG, "Error insertMenu - Products", e);
        } finally {
            values.clear();
        }
    }

    private synchronized static void insertProducts(Context context, List<Product> productList) {
        insertProducts(context, productList, DatabaseProvider.URI_PRODUCTS);
    }

    private synchronized static void insertTrendingProducts(Context context, List<Product> productList) {
        Log.d(Constants.TAG, "insertTrendingProducts:  " + productList.size());
        insertProducts(context, productList, DatabaseProvider.URI_TRENDING_PRODUCTS);
    }

    private synchronized static void insertProducts(Context context, List<Product> productList, Uri insertUri) {
        ContentValues values = new ContentValues();
        try {
            for (Product product : productList) {

                values.clear();
                values.put(DatabaseHelper.Product.PRODUCT_ID, product.getProductId());
                values.put(DatabaseHelper.Product.NAME, product.getProductName());
                values.put(DatabaseHelper.Product.DESCRIPTION, product.getDescription());
                values.put(DatabaseHelper.Product.IMAGE_URL, product.getImageUrl());
                values.put(DatabaseHelper.Product.COLLECTION_ID, product.getCollectionId());
                values.put(DatabaseHelper.Product.COLLECTION_NAME, product.getCollectionName());
                values.put(DatabaseHelper.Product.IS_VEG, product.getIsVeg());
                values.put(DatabaseHelper.Product.PRICE, product.getPrice());
                values.put(DatabaseHelper.Product.RATING, product.getRating());
                values.put(DatabaseHelper.Product.INGREDIENTS, product.getIngredients().toString());
                values.put(DatabaseHelper.Product.INGREDIENTS_TEXT, product.getIngredientText());
                values.put(DatabaseHelper.Product.FATS, product.getFats());
                values.put(DatabaseHelper.Product.PROTIENS, product.getProtiens());
                values.put(DatabaseHelper.Product.CARBS, product.getCarbs());
                context.getContentResolver().insert(insertUri, values);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error insertMenu - Products", e);
        } finally {
            values.clear();
        }
    }

    public synchronized static Product getProductByProductId(Context context, int productId) {

        Product product = null;
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(DatabaseProvider.URI_PRODUCTS,
                    null,
                    DatabaseHelper.Product.PRODUCT_ID + " = ?",
                    new String[]{String.valueOf(productId)}, null, null);

            if (cursor != null && cursor.moveToNext()) {
                product = new Product();
                product.setProductId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.Product.PRODUCT_ID)));
                product.setProductName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.Product.NAME)));
                product.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.Product.DESCRIPTION)));
                product.setCollectionId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.Product.COLLECTION_ID)));
                product.setCollectionName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.Product.COLLECTION_NAME)));
                product.setImageUrl(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.Product.IMAGE_URL)));
                product.setRating(cursor.getFloat(cursor.getColumnIndexOrThrow(DatabaseHelper.Product.RATING)));
                product.setIsVeg(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.Product.IS_VEG)));
                product.setPrice(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.Product.PRICE)));
                product.setIngredients(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.Product.INGREDIENTS)));
                product.setIngredientText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.Product.INGREDIENTS_TEXT)));
                product.setFats(cursor.getFloat(cursor.getColumnIndexOrThrow(DatabaseHelper.Product.FATS)));
                product.setProtiens(cursor.getFloat(cursor.getColumnIndexOrThrow(DatabaseHelper.Product.PROTIENS)));
                product.setCarbs(cursor.getFloat(cursor.getColumnIndexOrThrow(DatabaseHelper.Product.CARBS)));
            }
        } catch (Exception e) {
            Log.e(TAG, "getProductByProductId", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return product;
    }

    public synchronized static List<Product> getCartItems(Context context) {

        List<Product> productList = null;
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(DatabaseProvider.URI_CART,
                    null, null, null, null, null);

            if (cursor != null && cursor.getCount() > 0) {
                Product product = null;
                productList = new ArrayList<>(cursor.getCount());

                while (cursor.moveToNext()) {
                    product = new Product();
                    product.setProductId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.Cart.PRODUCT_ID)));
                    product.setProductName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.Cart.PRODUCT_NAME)));
                    product.setImageUrl(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.Cart.PRODUCT_IMAGE_URL)));
                    product.setCollectionId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.Cart.COLLECTION_ID)));
                    product.setCollectionName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.Cart.COLLECTION_NAME)));
                    product.setQuantity(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.Cart.QUANTITY)));
                    product.setPrice(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.Cart.PRICE)));
                    product.setIngredients(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.Cart.INGREDIENTS)));

                    productList.add(product);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "getCartItems", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return productList;
    }

    public synchronized static int getCartProductQuantity(Context context, int productId) {
        int count = 0;
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(DatabaseProvider.URI_CART,
                    null,
                    DatabaseHelper.Cart.PRODUCT_ID + " = ?",
                    new String[]{String.valueOf(productId)}, null, null);

            if (cursor != null && cursor.moveToNext()) {
                count = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.Cart.QUANTITY));
            }
        } catch (Exception e) {
            Log.e(TAG, "getCartProductQuantity", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return count;
    }

    public synchronized static int addCartProduct(Context context, Product product) {
        int quantity = getCartProductQuantity(context, product.getProductId());

        if (quantity == 0) {
            quantity = 1;
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.Cart.PRODUCT_ID, product.getProductId());
            values.put(DatabaseHelper.Cart.PRODUCT_NAME, product.getProductName());
            values.put(DatabaseHelper.Cart.PRODUCT_IMAGE_URL, product.getImageUrl());
            values.put(DatabaseHelper.Cart.COLLECTION_ID, product.getCollectionId());
            values.put(DatabaseHelper.Cart.COLLECTION_NAME, product.getCollectionName());
            values.put(DatabaseHelper.Cart.PRICE, product.getPrice());
            values.put(DatabaseHelper.Cart.QUANTITY, quantity);
            values.put(DatabaseHelper.Cart.INGREDIENTS, product.getIngredients().toString());
            Uri uri = context.getContentResolver().insert(DatabaseProvider.URI_CART, values);
            Log.d(TAG, "ADD CART " + uri.getLastPathSegment());

        } else {
            quantity++;
            ContentValues values = new ContentValues(1);
            values.put(DatabaseHelper.Cart.QUANTITY, quantity);
            int row = context.getContentResolver().update(DatabaseProvider.URI_CART, values,
                    DatabaseHelper.Cart.PRODUCT_ID + "=?", new String[]{String.valueOf(product.getProductId())});
            Log.d(TAG, "UPDATE CART " + row);

        }

        return quantity;
    }

    public synchronized static int removeCartProduct(Context context, Product product) {
        int quantity = getCartProductQuantity(context, product.getProductId());

        if (quantity == 0) {
            return 0;

        } else if (quantity == 1) {
            deleteProductFromCart(context, product.getProductId());
            return 0;

        } else {
            quantity = quantity - 1;
            ContentValues values = new ContentValues(1);
            values.put(DatabaseHelper.Cart.QUANTITY, quantity);
            int row = context.getContentResolver().update(DatabaseProvider.URI_CART, values,
                    DatabaseHelper.Cart.PRODUCT_ID + "=?", new String[]{String.valueOf(product.getProductId())});
            Log.d(TAG, "REMOVE CART " + quantity);
            return quantity;
        }
    }

    private static void deleteProductFromCart(Context context, int productId) {
        try {
            int delete = context.getContentResolver().delete(DatabaseProvider.URI_CART, DatabaseHelper.Cart.PRODUCT_ID + "=?", new String[]{String.valueOf(productId)});
            Log.d(TAG, "deleteProductFromCart:   " + delete);
        } catch (Exception e) {
            Log.e(TAG, "Error deleteProductFromCart : ", e);
        }
    }

    public static void clearCart(Context context) {
        try {
            context.getContentResolver().delete(DatabaseProvider.URI_CART, null, null);
        } catch (Exception e) {
            Log.e(TAG, "Error resetKeys : ", e);
        }
    }

    public static void logout(Context context) {
        try {
            resetKeys(context);
            clearCart(context);
            deleteData(context);
        } catch (Exception e) {
            Log.e(TAG, "Error resetKeys : ", e);
        }
    }

    public static void deleteData(Context context) {
        try {
            context.getContentResolver().delete(DatabaseProvider.URI_COLLECTIONS, null, null);
            context.getContentResolver().delete(DatabaseProvider.URI_PRODUCTS, null, null);
            context.getContentResolver().delete(DatabaseProvider.URI_TRENDING_PRODUCTS, null, null);
        } catch (Exception e) {
            Log.e(TAG, "Error resetKeys : ", e);
        }
    }

    public static void resetKeys(Context context) {
        try {
            /*insertOrUpdateKeys(context, DatabaseHelper.Keys.USER_ID, "-1");
            insertOrUpdateKeys(context, DatabaseHelper.Keys.USER_NAME, "");
            insertOrUpdateKeys(context, DatabaseHelper.Keys.USER_EMAIL, "");
            insertOrUpdateKeys(context, DatabaseHelper.Keys.USER_MOBILE, "");

            insertOrUpdateKeys(context, DatabaseHelper.Keys.QUESTIONS_DONE, "-1");

            insertOrUpdateKeys(context, DatabaseHelper.Keys.IS_VEG, "-1");
            insertOrUpdateKeys(context, DatabaseHelper.Keys.IS_DIABETES, "-1");
            insertOrUpdateKeys(context, DatabaseHelper.Keys.IS_BP, "-1");
            insertOrUpdateKeys(context, DatabaseHelper.Keys.IS_CHOLESTROL, "-1");
            insertOrUpdateKeys(context, DatabaseHelper.Keys.DIABETES_LEVEL, "-1");
            insertOrUpdateKeys(context, DatabaseHelper.Keys.BP_LEVEL, "-1");
            insertOrUpdateKeys(context, DatabaseHelper.Keys.CHOLESTROL_LEVEL, "-1");*/
            context.getContentResolver().delete(DatabaseProvider.URI_KEYS, null, null);
        } catch (Exception e) {
            Log.e(TAG, "Error resetKeys : ", e);
        }
    }

    public static void insertOrUpdateKeys(Context context, String key, String value) {
        ContentValues values = new ContentValues(1);
        values.put(DatabaseHelper.Keys.VALUE, value);

        int u = context.getContentResolver().update(DatabaseProvider.URI_KEYS, values, DatabaseHelper.Keys._ID + " = ?", new String[]{key});
        //Log.i(TAG, "DatabaseUtils :: Update : KEYS table  " + key + "\"  ::  " + value + "  u  " + u);

        if (u <= 0) {
            values = new ContentValues(2);
            values.put(DatabaseHelper.Keys._ID, key);
            values.put(DatabaseHelper.Keys.VALUE, value);

            context.getContentResolver().insert(DatabaseProvider.URI_KEYS, values);
        }
    }

    public static String getKeyValue(Context context, String key) {
        String value = null;
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(DatabaseProvider.URI_KEYS,
                    new String[]{DatabaseHelper.Keys.VALUE},
                    DatabaseHelper.Keys._ID + " = ?",
                    new String[]{key},
                    null);
            if (cursor != null && cursor.moveToNext()) {
                value = cursor.getString(0);
            }
        } catch (Exception e) {
            Log.e(TAG, "getKeys", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return value;
    }

    public static void insertLoggedInUser(Context context, User user) {
        if (user != null) {
            Log.d(TAG, "insertLoggedInUser:  " + user.getEmail() + "   " + user.getUserId());
            insertOrUpdateKeys(context, DatabaseHelper.Keys.USER_ID, String.valueOf(user.getUserId()));
            insertOrUpdateKeys(context, DatabaseHelper.Keys.USER_EMAIL, user.getEmail());
            insertOrUpdateKeys(context, DatabaseHelper.Keys.USER_NAME, user.getName());
            insertOrUpdateKeys(context, DatabaseHelper.Keys.USER_MOBILE, user.getMobile());

            insertOrUpdateKeys(context, DatabaseHelper.Keys.QUESTIONS_DONE, String.valueOf(user.getIsQuestionDone()));

            insertOrUpdateKeys(context, DatabaseHelper.Keys.IS_VEG, String.valueOf(user.getIsVeg()));
            insertOrUpdateKeys(context, DatabaseHelper.Keys.IS_DIABETES, String.valueOf(user.getIsDiabetes()));
            insertOrUpdateKeys(context, DatabaseHelper.Keys.IS_CHOLESTROL, String.valueOf(user.getIsCholestrol()));
            insertOrUpdateKeys(context, DatabaseHelper.Keys.IS_KID, String.valueOf(user.getIsKid()));
            insertOrUpdateKeys(context, DatabaseHelper.Keys.IS_SENIOR, String.valueOf(user.getIsSenior()));
        }
    }

    public static int getKeyInt(Context context, String key) {
        int value = 0;
        try {
            value = Integer.parseInt(DbOperations.getKeyValue(context, key));
        } catch (NumberFormatException e) {
            Log.e(Constants.TAG, "getKeyInt: " + key + "    " + e.getMessage());
        }
        return value;

    }
}