package com.done.recommendation.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.done.recommendation.R;
import com.done.recommendation.network.response.Order;
import com.done.recommendation.ui.Activity_OrderDetail;
import com.done.recommendation.ui.Activity_RateOrder;
import com.done.recommendation.utils.Util;

import java.util.List;

import butterknife.ButterKnife;

public class Adapter_Order extends RecyclerView.Adapter<Adapter_Order.OrderViewHolder> {

    private Context context;
    private List<Order> orderList;

    public Adapter_Order(Context c, List<Order> list) {
        context = c;
        orderList = list;
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_order_item, viewGroup, false);
        OrderViewHolder cartViewHolder = new OrderViewHolder(v);
        return cartViewHolder;
    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {
        Order currentOrder = orderList.get(position);
        holder.tvDate.setText(Util.formatDate(currentOrder.getOrderDate()));
        holder.tvOrderNo.setText("#" + currentOrder.getOrderId());
        holder.tvAmount.setText(context.getString(R.string.rupee_symbol) + currentOrder.getTotalAmount());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, Activity_OrderDetail.class);
                i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                i.putExtra(Activity_RateOrder.CURRENT_ORDER_ID, currentOrder.getOrderId());
                context.startActivity(i);
            }
        });

        holder.btRateOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, Activity_RateOrder.class);
                i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                i.putExtra(Activity_RateOrder.CURRENT_ORDER_ID, currentOrder.getOrderId());
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvOrderNo, tvAmount;
        Button btRateOrder;
        protected View view;

        public OrderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(itemView);
            tvDate = itemView.findViewById(R.id.tvOrderDate);
            tvOrderNo = itemView.findViewById(R.id.tvOrderId);
            tvAmount = itemView.findViewById(R.id.tvOrderAmount);
            btRateOrder = itemView.findViewById(R.id.btRateOrder);
            view = itemView;
        }
    }
}