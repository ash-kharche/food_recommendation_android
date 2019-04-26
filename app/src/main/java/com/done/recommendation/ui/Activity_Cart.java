package com.done.recommendation.ui;

import android.app.Activity;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.done.recommendation.Constants;
import com.done.recommendation.R;
import com.done.recommendation.adapter.Adapter_Cart;
import com.done.recommendation.adapter.Adapter_Products;
import com.done.recommendation.database.DatabaseHelper;
import com.done.recommendation.database.DatabaseProvider;
import com.done.recommendation.database.DbOperations;
import com.done.recommendation.network.ApiClient;
import com.done.recommendation.network.ServerApi;
import com.done.recommendation.network.request.ApiBody;
import com.done.recommendation.network.response.Product;
import com.done.recommendation.utils.Util;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_Cart extends Activity {
    @BindView(R.id.tv_cart_amount)
    TextView tvCartAmount;
    @BindView(R.id.recycler_view_cart_products)
    RecyclerView recyclerViewCartProducts;
    @BindView(R.id.recycler_view_recommended_products)
    RecyclerView recyclerViewRecommendedProducts;
    @BindView(R.id.bt_checkout)
    Button btnCheckout;
    @BindView(R.id.tv_cart_recommendation_text)
    TextView tv_cart_recommendation_text;

    private List<Product> cartItems = null;

    @OnClick(R.id.bt_checkout)
    public void checkOutClicked() {
        Intent i = new Intent(Activity_Cart.this, Activity_Checkout.class);
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
    }

    private CartObserver mCartObserver = null;
    private Adapter_Cart adapterCart;
    private Adapter_Products adapter_RecommendedProduct;

    public class CartObserver extends ContentObserver {

        public CartObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            loadCartProducts();
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            loadCartProducts();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        ButterKnife.bind(Activity_Cart.this);
        init();
    }

    @Override
    protected void onPause() {
        if (mCartObserver != null) {
            getContentResolver().unregisterContentObserver(mCartObserver);
        }
        super.onPause();
    }

    private void init() {
        mCartObserver = new CartObserver(new Handler());
        if (mCartObserver != null) {
            getContentResolver().registerContentObserver(DatabaseProvider.URI_CART, true, mCartObserver);
        }
        loadCartProducts();
    }

    private void loadRecommendedProducts() {
        webservice_getCartRecommendations();
    }

    private void loadCartProducts() {
        cartItems = DbOperations.getCartItems(Activity_Cart.this);

        if (cartItems != null && cartItems.size() > 0) {
            adapterCart = new Adapter_Cart(Activity_Cart.this, cartItems);
            recyclerViewCartProducts.setLayoutManager(new LinearLayoutManager(this));
            recyclerViewCartProducts.setAdapter(adapterCart);

            float amount = 0;
            for (Product product : cartItems) {
                amount += product.getPrice();
            }
            tvCartAmount.setText("PAY Rs. " + amount);

            loadRecommendedProducts();
        } else {
            loadCartRecommendations(null);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void webservice_getCartRecommendations() {
        //Util.showProgressDialog(Activity_Cart.this, false);
        try {
            Set<Integer> setCollections = new HashSet<>();
            Set<Integer> setProducts = new HashSet<>();
            StringBuilder sbCollectionsId = new StringBuilder();
            StringBuilder sbProductsId = new StringBuilder();
            String collectionIdList = "";
            String productIdList = "";

            for (Product p : cartItems) {
                if (p.getQuantity() > 0) {
                    setCollections.add(p.getCollectionId());
                    setProducts.add(p.getProductId());
                }
            }
            if (setCollections != null) {
                Iterator<Integer> it = setCollections.iterator();
                while (it.hasNext()) {
                    sbCollectionsId.append(it.next());
                    sbCollectionsId.append(",");
                }
                collectionIdList = sbCollectionsId.toString().substring(0, sbCollectionsId.toString().length() - 1);
            }

            if (setProducts != null) {
                Iterator<Integer> it = setProducts.iterator();
                while (it.hasNext()) {
                    sbProductsId.append(it.next());
                    sbProductsId.append(",");
                }
                productIdList = sbProductsId.toString().substring(0, sbProductsId.toString().length() - 1);
            }

            ServerApi serverApi = ApiClient.getApiClient();
            int userId = DbOperations.getKeyInt(Activity_Cart.this, DatabaseHelper.Keys.USER_ID);
            if (userId == 0) return;

            ApiBody apiBody = new ApiBody();
            apiBody.setUserId(userId);
            apiBody.setIsVeg(DbOperations.getKeyInt(Activity_Cart.this, DatabaseHelper.Keys.IS_VEG));
            apiBody.setIsDiabetes(DbOperations.getKeyInt(Activity_Cart.this, DatabaseHelper.Keys.IS_DIABETES));
            apiBody.setIsCholestrol(DbOperations.getKeyInt(Activity_Cart.this, DatabaseHelper.Keys.IS_CHOLESTROL));
            apiBody.setIsKid(DbOperations.getKeyInt(Activity_Cart.this, DatabaseHelper.Keys.IS_KID));
            apiBody.setIsSenior(DbOperations.getKeyInt(Activity_Cart.this, DatabaseHelper.Keys.IS_SENIOR));
            apiBody.setCollections(collectionIdList);
            apiBody.setProducts(productIdList);
            Call<List<Product>> call = serverApi.getCartRecommendations(apiBody);

            call.enqueue(new Callback<List<Product>>() {
                @Override
                public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                    if (response.isSuccessful()) {
                        List<Product> productList = response.body();
                        Log.d(Constants.TAG, "onResponse success:  " + productList);

                        Util.hideProgressDialog();
                        loadCartRecommendations(productList);

                    } else {
                        Log.d(Constants.TAG, "onResponse error:  ");
                    }
                }

                @Override
                public void onFailure(Call<List<Product>> call, Throwable t) {
                    Log.e(Constants.TAG, "onFailure  " + t);
                    Util.hideProgressDialog();
                }
            });


        } catch (Exception e) {
            Util.hideProgressDialog();
            Log.d(Constants.TAG, "error  ", e);
            showSnackBar();
        }
    }

    private void loadCartRecommendations(List<Product> productList) {
        if (productList != null && productList.size() > 0) {
            tv_cart_recommendation_text.setVisibility(View.VISIBLE);
            recyclerViewRecommendedProducts.setVisibility(View.VISIBLE);
            LinearLayoutManager layoutManager = new LinearLayoutManager(Activity_Cart.this, LinearLayoutManager.HORIZONTAL, false);
            recyclerViewRecommendedProducts.setLayoutManager(layoutManager);

            //Dummy data productList = DbOperations.getRecommendedProducts(Activity_Cart.this);
            adapter_RecommendedProduct = new Adapter_Products(this, productList, Adapter_Products.TYPE_RECOMMENDED);
            recyclerViewRecommendedProducts.setAdapter(adapter_RecommendedProduct);
        } else {
            tv_cart_recommendation_text.setVisibility(View.GONE);
            recyclerViewRecommendedProducts.setVisibility(View.GONE);
        }
    }

    public void showSnackBar() {
        if (!isDestroyed() || !isFinishing()) {
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), null, Snackbar.LENGTH_SHORT);
            snackbar.setText(getString(R.string.error_no_response));
            snackbar.setAction(R.string.dismiss, view -> snackbar.dismiss());
            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.snackbar_alert));
            snackbar.show();
        }
    }
}