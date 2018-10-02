package com.example.android.foodreco.network;

import com.example.android.foodreco.network.request.LoginBody;
import com.example.android.foodreco.network.request.SignUpUserBody;
import com.example.android.foodreco.network.response.ProductList;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface ServerApi {
    String BASE_URL = "https://food-recommendation.herokuapp.com";

    String SIGN_UP_USER = "/signUpUser";
    String LOGIN = "/login";
    String GET_ALL_PRODUCTS = "/getAllProducts";
    String GET_COLLECTIONS = "/getCollections";

    @GET(GET_ALL_PRODUCTS)
    Call<ProductList> getAllProducts();

    @GET(GET_COLLECTIONS)
    Call<List<com.example.android.foodreco.network.response.Collections>> getCollections();

    @PUT(SIGN_UP_USER)
    Call<Void> signUpUser(@Body SignUpUserBody body);

    @PUT(LOGIN)
    Call<Void> login(@Body LoginBody body);
}
