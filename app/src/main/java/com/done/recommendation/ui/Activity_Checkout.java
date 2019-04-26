package com.done.recommendation.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.done.recommendation.Constants;
import com.done.recommendation.R;
import com.done.recommendation.adapter.Adapter_Checkout;
import com.done.recommendation.database.DatabaseHelper;
import com.done.recommendation.database.DbOperations;
import com.done.recommendation.network.ApiClient;
import com.done.recommendation.network.ServerApi;
import com.done.recommendation.network.response.Order;
import com.done.recommendation.network.response.Product;
import com.done.recommendation.utils.Util;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_Checkout extends Activity {
    @BindView(R.id.recycler_view_checkout_products)
    RecyclerView recyclerViewCheckoutProducts;
    @BindView(R.id.bt_place_order)
    Button btPlaceOrder;

    @BindView(R.id.tv_cart_amount)
    TextView tvCartAmount;
    @BindView(R.id.tv_total)
    TextView tvTotalAmount;

    private float totalAmount = 0;
    private List<Product> productList = null;
    private List<Integer> productIdList = null;

    @OnClick(R.id.bt_place_order)
    public void placeOrderClicked() {
        btPlaceOrder.setEnabled(false);
        webservice_placeOrder();
    }

    private Adapter_Checkout adapter_checkout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        ButterKnife.bind(Activity_Checkout.this);
        init();
    }

    private void init() {
        productList = DbOperations.getCartItems(Activity_Checkout.this);
        if (productList != null) {
            float amount = 0;
            for (Product product : productList) {
                amount += product.getPrice();
            }
            tvCartAmount.setText("PAY Rs. " + amount);
        }

        loadCartProducts(productList);
    }

    private void loadCartProducts(List<Product> productList) {
        if (productList != null && productList.size() > 0) {
            adapter_checkout = new Adapter_Checkout(Activity_Checkout.this, productList);
            recyclerViewCheckoutProducts.setLayoutManager(new LinearLayoutManager(this));
            recyclerViewCheckoutProducts.setAdapter(adapter_checkout);
        }

        if (productList != null) {
            float amount = 0;
            for (Product product : productList) {
                amount += product.getPrice();
            }
            tvCartAmount.setText(getString(R.string.rupee_symbol) + amount);

            totalAmount = amount + 25 + 100; //25 --> Delivbery charges, 100 --> tax
            tvTotalAmount.setText(getString(R.string.rupee_symbol) + totalAmount);
        }
    }

    private void webservice_placeOrder() {
        Util.showProgressDialog(Activity_Checkout.this, false);

        int userId = 0;
        try {
            userId = Integer.parseInt(DbOperations.getKeyValue(Activity_Checkout.this, DatabaseHelper.Keys.USER_ID));
        } catch (NumberFormatException e) {
            Log.e(Constants.TAG, "Profile: userId is null", e);
        }

        if (userId == 0) {
            Toast.makeText(Activity_Checkout.this, "User is not logged in", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(Activity_Checkout.this, Activity_Login.class);
            i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivityForResult(i, 1000);
            return;
        }

        /*productIdList = new ArrayList<Integer>(productList.size());
        for(Product p : productList) {
            productIdList.add(p.getProductId());
        }*/

        Order body = new Order();
        body.setUserId(userId);
        body.setTotalAmount(totalAmount);
        body.setPaymentMode("COD");
        //body.setOrderDate(new Date().toString());
        body.setOrderAddress("MY HOME ADDRESS");
        body.setProductList(productList);
        ServerApi serverApi = ApiClient.getApiClient();
        Call<Order> call = serverApi.placeOrder(body);

        call.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                Util.hideProgressDialog();

                if (response.isSuccessful()) {

                    DbOperations.clearCart(Activity_Checkout.this);
                    Order responseOrder = response.body();

                    Intent i = new Intent(Activity_Checkout.this, Activity_OrderDetail.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    i.putExtra(Activity_OrderDetail.CURRENT_ORDER_ID, responseOrder.getOrderId());
                    startActivity(i);
                    finish();

                } else {
                    Toast.makeText(Activity_Checkout.this, "Order couldn't be placed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                Log.e(Constants.TAG, "onFailure  " + t);
                Util.hideProgressDialog();
            }
        });
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == 1000) {
            webservice_placeOrder();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}