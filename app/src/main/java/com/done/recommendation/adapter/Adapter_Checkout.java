package com.done.recommendation.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.done.recommendation.R;
import com.done.recommendation.network.response.Product;
import com.done.recommendation.utils.widgets.SqaureImageView;

import java.util.List;

import butterknife.ButterKnife;

public class Adapter_Checkout extends RecyclerView.Adapter<Adapter_Checkout.CartViewHolder> {

    private Context context;
    private List<Product> productList;

    public Adapter_Checkout(Context c, List<Product> list) {
        context = c;
        productList = list;
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_checkout_item, viewGroup, false);
        CartViewHolder cartViewHolder = new CartViewHolder(v);
        return cartViewHolder;
    }

    @Override
    public void onBindViewHolder(CartViewHolder holder, int position) {
        Product currentProduct = productList.get(position);
        holder.tvTitle.setText(currentProduct.getProductName());

        double price = currentProduct.getPrice() * currentProduct.getQuantity();
        holder.tvItemCost.setText(context.getString(R.string.rupee_symbol) + String.valueOf(price));
        holder.tvQuantity.setText("(" + currentProduct.getQuantity() + ")");

        Glide.with(context).load(currentProduct.getImageUrl()).centerCrop().into(holder.image);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        SqaureImageView image;
        TextView tvTitle;
        TextView tvItemCost;
        protected ImageView ivSubtract, ivAdd;
        protected TextView tvQuantity;
        protected View view;

        public CartViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(itemView);
            this.image = itemView.findViewById(R.id.iv_image);
            this.tvTitle = itemView.findViewById(R.id.tvTitle);
            this.tvItemCost = itemView.findViewById(R.id.tvItemCost);
            this.tvQuantity = itemView.findViewById(R.id.tvQuantity);
            view = itemView;
        }
    }
}