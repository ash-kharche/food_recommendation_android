package com.done.recommendation.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.done.recommendation.Constants;
import com.done.recommendation.R;
import com.done.recommendation.adapter.Adapter_RateProducts;
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

public class Activity_RateOrder extends Activity {
    public static final String CURRENT_ORDER_ID = "current_order_id";

    @BindView(R.id.recycler_view_products)
    RecyclerView recyclerViewProducts;
    @BindView(R.id.bt_rate_order)
    Button btRateOrder;

    @BindView(R.id.tvOrderId)
    TextView tvOrderId;
    @BindView(R.id.tvOrderDate)
    TextView tvOrderDate;
    @BindView(R.id.tvOrderAmount)
    TextView tvOrderAmount;
    @BindView(R.id.tv_order_addres)
    TextView tvOrderAddress;

    @BindView(R.id.order_tax_text)
    TextView tvPaymentModeText;
    @BindView(R.id.tvOrderTax)
    TextView tvPaymentMode;

    private Order order;
    private Integer userId;
    private Integer orderId;
    private List<Product> productList = null;

    @OnClick(R.id.bt_rate_order)
    public void placeOrderClicked() {
        btRateOrder.setEnabled(false);
        webservice_rateOrder();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_order);
        ButterKnife.bind(Activity_RateOrder.this);
        orderId = getIntent().getExtras().getInt(CURRENT_ORDER_ID);

        try {
            userId = Integer.parseInt(DbOperations.getKeyValue(Activity_RateOrder.this, DatabaseHelper.Keys.USER_ID));
        } catch (NumberFormatException e) {
            Log.e(Constants.TAG, "Activity_Orders: userId is null", e);
        }

        getOrderDetail(userId, orderId);
    }

    private void init() {
        if (order != null) {
            tvOrderId.setText("#" + order.getOrderId());
            tvOrderDate.setText(Util.formatDate(order.getOrderDate()));
            tvOrderAddress.setText(String.valueOf(order.getOrderAddress()));
            tvOrderAmount.setText(getString(R.string.rupee_symbol) + order.getTotalAmount());

            productList = order.getProductList();
            if (productList != null && productList.size() > 0) {
                Adapter_RateProducts adapter_rateProducts = new Adapter_RateProducts(Activity_RateOrder.this, order.getProductList());
                recyclerViewProducts.setLayoutManager(new LinearLayoutManager(this));
                recyclerViewProducts.setAdapter(adapter_rateProducts);
            }

        }
    }

    private void getOrderDetail(Integer userId, Integer orderId) {
        Util.showProgressDialog(Activity_RateOrder.this, false);
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

    private void webservice_rateOrder() {
        Util.showProgressDialog(Activity_RateOrder.this, false);

        Order body = new Order();
        body.setUserId(userId);
        body.setOrderId(orderId);
        body.setProductList(productList);
        ServerApi serverApi = ApiClient.getApiClient();
        Call<Void> call = serverApi.rateOrder(body);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Util.hideProgressDialog();
                if (response.isSuccessful()) {
                    Intent i = new Intent(Activity_RateOrder.this, Activity_Orders.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(i);
                    finish();
                } else {
                    Toast.makeText(Activity_RateOrder.this, "Order couldn't be rated", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
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
}