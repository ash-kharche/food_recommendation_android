package com.example.android.foodreco.network;

import com.example.android.foodreco.network.request.LoginBody;
import com.example.android.foodreco.network.request.SignUpUserBody;
import com.example.android.foodreco.network.response.ProductList;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ServerApi {
    String BASE_URL = "https://food-recommendation.herokuapp.com";

    String SIGN_UP_USER = "/signUpUser";
    String LOGIN = "/login";
    String GET_ALL_PRODUCTS = "/getAllProducts";

    @GET(GET_ALL_PRODUCTS)
    Call<ProductList> getAllProducts();

    @POST(SIGN_UP_USER)
    Call<Void> signUpUser(@Body SignUpUserBody body);

    @GET(LOGIN)
    Call<Void> login(@Body LoginBody body);
}
