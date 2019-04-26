package com.done.recommendation.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.done.recommendation.Constants;
import com.done.recommendation.R;
import com.done.recommendation.adapter.Adapter_Order;
import com.done.recommendation.database.DatabaseHelper;
import com.done.recommendation.database.DbOperations;
import com.done.recommendation.network.ApiClient;
import com.done.recommendation.network.ServerApi;
import com.done.recommendation.network.response.Order;
import com.done.recommendation.utils.Util;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_Orders extends Activity {
    @BindView(R.id.tv_error)
    TextView tvError;
    @BindView(R.id.bt_login)
    Button btLogin;
    @BindView(R.id.recycler_view_orders)
    RecyclerView recyclerViewOrders;

    private Adapter_Order adapter_order;

    private int myRequestCode = 123;

    @OnClick(R.id.bt_login)
    public void onLoginClicked(View v) {
        Intent i = new Intent(Activity_Orders.this, Activity_Login.class);
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivityForResult(i, myRequestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == myRequestCode && resultCode == Activity.RESULT_OK) {
            init();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        ButterKnife.bind(Activity_Orders.this);
        init();
    }

    private void init() {
        int userId = 0;
        try {
            userId = Integer.parseInt(DbOperations.getKeyValue(Activity_Orders.this, DatabaseHelper.Keys.USER_ID));
        } catch (NumberFormatException e) {
            Log.e(Constants.TAG, "Activity_Orders: userId is null:  " + e.getMessage());
        }

        if (userId == 0) {
            btLogin.setVisibility(View.VISIBLE);
            recyclerViewOrders.setVisibility(View.GONE);

        } else {
            btLogin.setVisibility(View.GONE);
            recyclerViewOrders.setVisibility(View.VISIBLE);

            webservice_fetchOrders(userId);
        }
    }

    private void webservice_fetchOrders(int userId) {
        Util.showProgressDialog(Activity_Orders.this, false);
        ServerApi serverApi = ApiClient.getApiClient();

        Call<List<Order>> call = serverApi.getAllOrdersOfUser(userId);

        call.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                Util.hideProgressDialog();
                if (response.isSuccessful()) {
                    List<Order> orderList = response.body();

                    if (orderList == null || orderList.size() <= 0) {
                        tvError.setVisibility(View.VISIBLE);

                    } else {
                        adapter_order = new Adapter_Order(Activity_Orders.this, orderList);
                        recyclerViewOrders.setLayoutManager(new LinearLayoutManager(Activity_Orders.this));
                        recyclerViewOrders.setAdapter(adapter_order);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
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