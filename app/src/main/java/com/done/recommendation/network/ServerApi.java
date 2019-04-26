package com.done.recommendation.network;

import com.done.recommendation.network.request.ApiBody;
import com.done.recommendation.network.request.LoginBody;
import com.done.recommendation.network.request.SignUpUserBody;
import com.done.recommendation.network.response.MenuData;
import com.done.recommendation.network.response.Order;
import com.done.recommendation.network.response.Product;
import com.done.recommendation.network.response.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ServerApi {
    String BASE_URL = "http://food-recommendation.herokuapp.com";

    String GET_DATA = "/getData";
    String GET_CART_RECOMMENDATIONS = "/getCartRecommendedProducts";
    String GET_USER_RECOMMENDATIONS = "/getUserRecommendedProducts";
    String GET_USER_RECOMMENDATIONS_1 = "/getUserRecommendedProducts_1/{user_id}";

    String PLACE_ORDER = "/placeOrder";
    String GET_ALL_ORDERS = "/getAllOrders/{user_id}";
    String GET_ORDER_DETAILS = "/getOrderDetails/{user_id}/{order_id}";
    String RATE_ORDER = "/rateOrder";

    String SUBMIT_ANSWERS = "/submitAnswers";

    String SIGN_UP_USER = "/signUpUser";
    String LOGIN = "/login";
    String GET_USER = "/getUser/{user_id}";

    @POST(GET_DATA)
    Call<MenuData> getMenuData(@Body ApiBody apiBody);

    @POST(GET_CART_RECOMMENDATIONS)
    Call<List<Product>> getCartRecommendations(@Body ApiBody apiBody);

    @POST(GET_USER_RECOMMENDATIONS)
    Call<List<Product>> getUserRecommendations(@Body ApiBody apiBody);

    @GET(GET_USER_RECOMMENDATIONS_1)
    Call<List<Product>> getUserRecommendations_1(@Path("user_id") int userId);

    @PUT(SIGN_UP_USER)
    Call<User> signUpUser(@Body SignUpUserBody body);

    @PUT(LOGIN)
    Call<User> login(@Body LoginBody body);

    @PUT(SUBMIT_ANSWERS)
    Call<User> submitAnswers(@Body User user);

    @PUT(PLACE_ORDER)
    Call<Order> placeOrder(@Body Order order);

    @GET(GET_ALL_ORDERS)
    Call<List<Order>> getAllOrdersOfUser(@Path("user_id") int userId);

    @GET(GET_ORDER_DETAILS)
    Call<Order> getOrderDetail(@Path("user_id") int userId, @Path("order_id") int orderId);

    @GET(GET_USER)
    Call<User> getUser(@Path("user_id") int userId);

    @POST(RATE_ORDER)
    Call<Void> rateOrder(@Body Order order);

}
