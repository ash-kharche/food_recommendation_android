package com.done.recommendation.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.done.recommendation.Constants;
import com.done.recommendation.R;
import com.done.recommendation.adapter.Adapter_Checkout;
import com.done.recommendation.database.DatabaseHelper;
import com.done.recommendation.database.DbOperations;
import com.done.recommendation.network.ApiClient;
import com.done.recommendation.network.ServerApi;
import com.done.recommendation.network.response.Order;
import com.done.recommendation.utils.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_OrderDetail extends Activity {

    public static final String CURRENT_ORDER_ID = "current_order_id";

    @BindView(R.id.recycler_view_checkout_products)
    RecyclerView recyclerViewCheckoutProducts;
    @BindView(R.id.bt_place_order)
    Button btPlaceOrder;

    @BindView(R.id.tv_cart_amount)
    TextView tvCartAmount;

    @BindView(R.id.tvOrderDate)
    TextView tvOrderDate;
    @BindView(R.id.tvOrderId)
    TextView tvOrderNumber;
    @BindView(R.id.tvOrderAmount)
    TextView tvTotalAmount;
    @BindView(R.id.tv_order_addres)
    TextView tvOrderAddress;

    private Order order;
    private Integer orderId;

    @OnClick(R.id.bt_place_order)
    public void placeOrderClicked() {
        btPlaceOrder.setEnabled(false);
        Intent i = new Intent(Activity_OrderDetail.this, Activity_Main.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        ButterKnife.bind(Activity_OrderDetail.this);

        orderId = getIntent().getExtras().getInt(CURRENT_ORDER_ID);

        int userId = 0;
        try {
            userId = Integer.parseInt(DbOperations.getKeyValue(Activity_OrderDetail.this, DatabaseHelper.Keys.USER_ID));
        } catch (NumberFormatException e) {
            Log.e(Constants.TAG, "Activity_Orders: userId is null", e);
        }

        webservice_getOrderDetail(userId, orderId);
    }

    private void init() {
        if (order != null) {
            tvOrderNumber.setText("#" + order.getOrderId());
            tvOrderDate.setText(Util.formatDate(order.getOrderDate()));
            tvOrderAddress.setText(String.valueOf(order.getOrderAddress()));
            tvTotalAmount.setText(getString(R.string.rupee_symbol) + order.getTotalAmount());
        }

        if (order.getProductList() != null && order.getProductList().size() > 0) {
            Adapter_Checkout adapter_checkout = new Adapter_Checkout(Activity_OrderDetail.this, order.getProductList());
            recyclerViewCheckoutProducts.setLayoutManager(new LinearLayoutManager(this));
            recyclerViewCheckoutProducts.setAdapter(adapter_checkout);
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Activity_OrderDetail.this, Activity_Main.class));
        super.onBackPressed();
    }

    private void webservice_getOrderDetail(Integer userId, Integer orderId) {
        Util.showProgressDialog(Activity_OrderDetail.this, false);
        try {
            ServerApi serverApi = ApiClient.getApiClient();
            Call<Order> call = serverApi.getOrderDetail(userId, orderId);

            call.enqueue(new Callback<Order>() {
                @Override
                public void onResponse(Call<Order> call, Response<Order> response) {
                    if (response.isSuccessful()) {
                        Util.hideProgressDialog();

                        order = response.body();
                        init();

                    } else {
                        Log.d(Constants.TAG, "onResponse error:  ");
                    }
                }

                @Override
                public void onFailure(Call<Order> call, Throwable t) {
                    Log.e(Constants.TAG, "onFailure  " + t);
                    Util.hideProgressDialog();
                }
            });


        } catch (Exception e) {
            Util.hideProgressDialog();
            showSnackBar();
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