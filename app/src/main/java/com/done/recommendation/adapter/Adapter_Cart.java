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
import com.done.recommendation.database.DbOperations;
import com.done.recommendation.network.response.Product;
import com.done.recommendation.utils.widgets.SqaureImageView;

import java.util.List;

import butterknife.ButterKnife;

public class Adapter_Cart extends RecyclerView.Adapter<Adapter_Cart.CartViewHolder> {

    private Context context;
    private List<Product> productList;

    public Adapter_Cart(Context c, List<Product> list) {
        context = c;
        productList = list;
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_cart_item, viewGroup, false);
        CartViewHolder cartViewHolder = new CartViewHolder(v);
        return cartViewHolder;
    }

    @Override
    public void onBindViewHolder(CartViewHolder holder, int position) {
        Product currentProduct = productList.get(position);
        holder.tvTitle.setText(currentProduct.getProductName());

        double price = currentProduct.getPrice() * currentProduct.getQuantity();
        holder.tvItemCost.setText(context.getString(R.string.rupee_symbol) + String.valueOf(price));
        holder.tvQuantity.setText("" + currentProduct.getQuantity());

        Glide.with(context).load(currentProduct.getImageUrl()).centerCrop().into(holder.image);

        holder.ivAdd.setTag(position);
        holder.ivAdd.setOnClickListener(v -> {
                    int quantity = DbOperations.addCartProduct(context, currentProduct);
                    holder.tvQuantity.setText("" + quantity);
                }
        );
        holder.ivSubtract.setTag(position);
        holder.ivSubtract.setOnClickListener(v -> {
            int quantity = DbOperations.removeCartProduct(context, currentProduct);
            holder.tvQuantity.setText("" + quantity);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        SqaureImageView image;
        TextView tvTitle;
        TextView tvItemCost;
        protected ImageView ivSubtract, ivAdd;
        protected TextView tvQuantity;
        protected View view;

        public CartViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(itemView);
            this.image = itemView.findViewById(R.id.lci_iv_image);
            this.tvTitle = itemView.findViewById(R.id.tvTitle);
            this.tvItemCost = itemView.findViewById(R.id.tvItemCost);
            this.ivSubtract = itemView.findViewById(R.id.ivSubtractProduct);
            this.ivAdd = itemView.findViewById(R.id.ivAddProduct);
            this.tvQuantity = itemView.findViewById(R.id.tvCurrentQuantity);
            view = itemView;
        }
    }
}