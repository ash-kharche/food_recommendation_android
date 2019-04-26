package com.done.recommendation.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.done.recommendation.Constants;
import com.done.recommendation.R;
import com.done.recommendation.adapter.Adapter_Collections;
import com.done.recommendation.adapter.Adapter_Products;
import com.done.recommendation.database.DatabaseHelper;
import com.done.recommendation.database.DbOperations;
import com.done.recommendation.network.ApiClient;
import com.done.recommendation.network.ServerApi;
import com.done.recommendation.network.request.ApiBody;
import com.done.recommendation.network.response.Collections;
import com.done.recommendation.network.response.MenuData;
import com.done.recommendation.network.response.Product;
import com.done.recommendation.utils.Util;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Fragment_Main extends Fragment {
    @BindView(R.id.recycler_view_collections)
    RecyclerView recyclerViewCollections;
    @BindView(R.id.recycler_view_trending_products)
    RecyclerView recyclerViewTrendingProducts;

    @BindView(R.id.recycler_view_recommended_products)
    RecyclerView recyclerViewRecommendedProducts;
    @BindView(R.id.tv_cart_recommendation_text)
    TextView tv_user_recommendation_text;

    private Adapter_Collections adapter_collections;
    private Adapter_Products adapter_TrendingProducts;

    public Fragment_Main() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(Fragment_Main.this, view);

        init();
        return view;
    }

    private void init() {
        //Collections
        List<Collections> collectionsList = DbOperations.isDatabaseLoaded(getActivity());
        if (collectionsList == null || collectionsList.size() == 0) {
            webservice_getMenuData();

        } else {
            loadCusinies(collectionsList);

            //Trending products
            List<Product> trendingProductList = DbOperations.getTrendingProducts(getActivity());
            if (trendingProductList != null && trendingProductList.size() > 0) {
                loadTrendingProducts(trendingProductList);
            }
        }

        webservice_getUserRecommendations();
    }

    private void loadCusinies(List<Collections> collections) {
        adapter_collections = new Adapter_Collections(getActivity(), collections);
        recyclerViewCollections.setHasFixedSize(true);
        recyclerViewCollections.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerViewCollections.setAdapter(adapter_collections);
    }

    private void loadTrendingProducts(List<Product> trendingProducts) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        adapter_TrendingProducts = new Adapter_Products(getActivity(), trendingProducts, Adapter_Products.TYPE_TRENDING);
        recyclerViewTrendingProducts.setHasFixedSize(true);
        recyclerViewTrendingProducts.setLayoutManager(layoutManager);
        recyclerViewTrendingProducts.setAdapter(adapter_TrendingProducts);
    }


    private void webservice_getMenuData() {
        int userId = DbOperations.getKeyInt(getActivity(), DatabaseHelper.Keys.USER_ID);
        if (userId == 0) return;

        ApiBody apiBody = new ApiBody();
        apiBody.setUserId(userId);
        apiBody.setIsVeg(DbOperations.getKeyInt(getActivity(), DatabaseHelper.Keys.IS_VEG));
        apiBody.setIsDiabetes(DbOperations.getKeyInt(getActivity(), DatabaseHelper.Keys.IS_DIABETES));
        apiBody.setIsCholestrol(DbOperations.getKeyInt(getActivity(), DatabaseHelper.Keys.IS_CHOLESTROL));
        apiBody.setIsKid(DbOperations.getKeyInt(getActivity(), DatabaseHelper.Keys.IS_KID));
        apiBody.setIsSenior(DbOperations.getKeyInt(getActivity(), DatabaseHelper.Keys.IS_SENIOR));

        Util.showProgressDialog(getActivity(), false);
        try {
            ServerApi serverApi = ApiClient.getApiClient();
            Call<MenuData> call = serverApi.getMenuData(apiBody);

            call.enqueue(new Callback<MenuData>() {
                @Override
                public void onResponse(Call<MenuData> call, Response<MenuData> response) {
                    if (response.isSuccessful()) {
                        MenuData menuData = response.body();
                        Log.d(Constants.TAG, "onResponse success:  " + menuData);

                        DbOperations.insertMenu(getActivity(), menuData);
                        Util.hideProgressDialog();

                        loadCusinies(menuData.getCollections());
                        loadTrendingProducts(menuData.getTrending());

                    } else {
                        Log.d(Constants.TAG, "onResponse error:  ");
                    }
                }

                @Override
                public void onFailure(Call<MenuData> call, Throwable t) {
                    Log.e(Constants.TAG, "onFailure  " + t);
                    Util.hideProgressDialog();
                }
            });


        } catch (Exception e) {
            Util.hideProgressDialog();
            Log.d(Constants.TAG, "error  ", e);
            ((Activity_Main) getActivity()).showSnackBar();
        }
    }

    private void webservice_getUserRecommendations() {
        //Util.showProgressDialog(getActivity(), false);
        try {
            int userId = DbOperations.getKeyInt(getActivity(), DatabaseHelper.Keys.USER_ID);
            if (userId == 0) return;

            ApiBody apiBody = new ApiBody();
            apiBody.setUserId(userId);
            apiBody.setIsVeg(DbOperations.getKeyInt(getActivity(), DatabaseHelper.Keys.IS_VEG));
            apiBody.setIsDiabetes(DbOperations.getKeyInt(getActivity(), DatabaseHelper.Keys.IS_DIABETES));
            apiBody.setIsCholestrol(DbOperations.getKeyInt(getActivity(), DatabaseHelper.Keys.IS_CHOLESTROL));
            apiBody.setIsKid(DbOperations.getKeyInt(getActivity(), DatabaseHelper.Keys.IS_KID));
            apiBody.setIsSenior(DbOperations.getKeyInt(getActivity(), DatabaseHelper.Keys.IS_SENIOR));

            ServerApi serverApi = ApiClient.getApiClient();
            Call<List<Product>> call = serverApi.getUserRecommendations(apiBody);

            call.enqueue(new Callback<List<Product>>() {
                @Override
                public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                    if (response.isSuccessful()) {
                        List<Product> productList = response.body();
                        Log.d(Constants.TAG, "onResponse success:  " + productList);

                        //Util.hideProgressDialog();
                        loadUserRecommendations(productList);

                    } else {
                        Log.d(Constants.TAG, "onResponse error:  ");
                    }
                }

                @Override
                public void onFailure(Call<List<Product>> call, Throwable t) {
                    Log.e(Constants.TAG, "onFailure  " + t);
                    //Util.hideProgressDialog();
                }
            });


        } catch (Exception e) {
            Util.hideProgressDialog();
            Log.d(Constants.TAG, "error  ", e);
        }
    }

    private void webservice_getUserRecommendations_1() {
        //Util.showProgressDialog(getActivity(), false);
        try {
            int userId = DbOperations.getKeyInt(getActivity(), DatabaseHelper.Keys.USER_ID);
            if (userId == 0) return;

            ApiBody apiBody = new ApiBody();
            apiBody.setUserId(userId);
            apiBody.setIsVeg(DbOperations.getKeyInt(getActivity(), DatabaseHelper.Keys.IS_VEG));
            apiBody.setIsDiabetes(DbOperations.getKeyInt(getActivity(), DatabaseHelper.Keys.IS_DIABETES));
            apiBody.setIsCholestrol(DbOperations.getKeyInt(getActivity(), DatabaseHelper.Keys.IS_CHOLESTROL));
            apiBody.setIsKid(DbOperations.getKeyInt(getActivity(), DatabaseHelper.Keys.IS_KID));
            apiBody.setIsSenior(DbOperations.getKeyInt(getActivity(), DatabaseHelper.Keys.IS_SENIOR));

            ServerApi serverApi = ApiClient.getApiClient();
            Call<List<Product>> call = serverApi.getUserRecommendations_1(userId);

            call.enqueue(new Callback<List<Product>>() {
                @Override
                public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                    if (response.isSuccessful()) {
                        List<Product> productList = response.body();
                        Log.d(Constants.TAG, "onResponse success:  " + productList);

                        //Util.hideProgressDialog();
                        loadUserRecommendations(productList);

                    } else {
                        Log.d(Constants.TAG, "onResponse error:  ");
                    }
                }

                @Override
                public void onFailure(Call<List<Product>> call, Throwable t) {
                    Log.e(Constants.TAG, "onFailure  " + t);
                    //Util.hideProgressDialog();
                }
            });


        } catch (Exception e) {
            Util.hideProgressDialog();
            Log.d(Constants.TAG, "error  ", e);
        }
    }

    private void loadUserRecommendations(List<Product> productList) {
        if (productList != null && productList.size() > 0) {
            tv_user_recommendation_text.setVisibility(View.VISIBLE);
            recyclerViewRecommendedProducts.setVisibility(View.VISIBLE);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            recyclerViewRecommendedProducts.setLayoutManager(layoutManager);

            Adapter_Products adapter_RecommendedProduct = new Adapter_Products(getActivity(), productList, Adapter_Products.TYPE_RECOMMENDED);
            recyclerViewRecommendedProducts.setAdapter(adapter_RecommendedProduct);
        }
    }
}