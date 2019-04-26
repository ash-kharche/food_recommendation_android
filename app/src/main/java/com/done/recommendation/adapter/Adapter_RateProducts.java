package com.done.recommendation.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.done.recommendation.R;
import com.done.recommendation.network.response.Product;

import java.util.List;

public class Adapter_RateProducts extends RecyclerView.Adapter<Adapter_RateProducts.RateProductViewHolder> {
    private List<Product> productList;
    private Context mContext;

    public Adapter_RateProducts(Context context, List<Product> p) {
        mContext = context;
        productList = p;
    }

    @Override
    public int getItemCount() {
        if (productList != null) {
            return productList.size();
        }
        return 0;
    }

    @Override
    public RateProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_rate_order_product_item, parent, false);
        return new RateProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RateProductViewHolder holder, int position) {
        Product product = productList.get(holder.getAdapterPosition());

        holder.ivProductImage.invalidate();
        String productImage = product.getImageUrl();
        Glide.with(mContext).load(productImage).centerCrop()
                .placeholder(R.drawable.menu_default)
                .error(R.drawable.menu_default)
                .into(holder.ivProductImage);

        holder.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                product.setRating(rating);
            }
        });
        holder.tvProductTitle.setText(product.getProductName());
        product.setRating(holder.ratingBar.getRating());
    }

    public class RateProductViewHolder extends RecyclerView.ViewHolder {
        protected ImageView ivProductImage;
        protected TextView tvProductTitle;
        protected RatingBar ratingBar;
        public View cell;


        private RateProductViewHolder(View view) {
            super(view);
            this.ivProductImage = view.findViewById(R.id.iv_image);
            this.tvProductTitle = view.findViewById(R.id.tv_product_name);
            this.ratingBar = view.findViewById(R.id.rb_product_rating_bar);
            cell = view;
        }
    }
}