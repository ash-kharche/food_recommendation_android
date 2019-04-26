package com.done.recommendation.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.done.recommendation.Constants;
import com.done.recommendation.R;
import com.done.recommendation.database.DbOperations;
import com.done.recommendation.network.response.Product;
import com.done.recommendation.ui.Activity_ProductDetail;

import java.util.List;

public class Adapter_Products extends RecyclerView.Adapter<Adapter_Products.ProductViewHolder> {
    public static final int TYPE_PRODUCT = 0;
    public static final int TYPE_TRENDING = 1;
    public static final int TYPE_RECOMMENDED = 2;

    private List<Product> productList;
    private Context mContext;
    private int viewType = TYPE_PRODUCT;

    public void updateProductList(List<Product> p) {
        this.productList = p;
    }

    public Adapter_Products(Context context, List<Product> p, int t) {
        mContext = context;
        productList = p;
        viewType = t;
    }

    @Override
    public int getItemCount() {
        if (productList != null) {
            return productList.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return viewType;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == TYPE_PRODUCT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_product, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recommended_product, parent, false);
        }
        return new ProductViewHolder(view);
    }

    public void setVisibility(View view, int visibility) {
        if (view != null) {
            view.setVisibility(visibility);
        }
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        Product product = productList.get(holder.getAdapterPosition());

        try {
            holder.ivProductImage.invalidate();
            String productImage = product.getImageUrl();
            Glide.with(mContext).load(productImage).centerCrop()
                    .placeholder(R.drawable.menu_default)
                    .error(R.drawable.menu_default)
                    .into(holder.ivProductImage);
        } catch (Exception e) {
            Log.e(Constants.TAG, "onBindViewHolder", e);
        }

        holder.tvProductTitle.setText(product.getProductName());
        holder.tvProductDescription.setText(product.getDescription());
        holder.tvProductPrice.setText(String.valueOf(product.getPrice()));

        if (product.getIsVeg() == 1) {
            holder.ivFoodType.setImageResource(R.drawable.veg_redesign);
        } else {
            holder.ivFoodType.setImageResource(R.drawable.non_veg);
        }

        float ratingValue = product.getRating();
        if (ratingValue > 0.0f) {
            setVisibility(holder.rating, View.VISIBLE);
            holder.rating.setText(String.valueOf(ratingValue));

            if(ratingValue <= 5 && ratingValue >= 4) {
                holder.rating.setBackgroundColor(ContextCompat.getColor(mContext, R.color.rating_high));
            } else if(ratingValue < 4 && ratingValue >= 3) {
                holder.rating.setBackgroundColor(ContextCompat.getColor(mContext, R.color.rating_middle));

            } else {
                holder.rating.setBackgroundColor(ContextCompat.getColor(mContext, R.color.rating_low));
            }

        } else {
            setVisibility(holder.rating, View.GONE);
        }

        holder.cell.setOnClickListener(v -> {
            Intent intent = null;
            intent = new Intent(mContext, Activity_ProductDetail.class);

            intent.putExtra(Activity_ProductDetail.PRODUCT_ID, product.getProductId());
            mContext.startActivity(intent);
        });
        holder.tvQuantity.setText("" + DbOperations.getCartProductQuantity(mContext, product.getProductId()));

        holder.ivAdd.setOnClickListener(v -> {
            int count = DbOperations.addCartProduct(mContext, product);
            holder.ivSubtract.setAlpha(1.0f);
            holder.tvQuantity.setText("" + count);
        });

        holder.ivSubtract.setOnClickListener(v -> {
            int count = DbOperations.removeCartProduct(mContext, product);
            holder.tvQuantity.setText("" + count);
            if (count != 0) {
                holder.ivSubtract.setAlpha(1.0f);
            } else {
                holder.ivSubtract.setAlpha(0.6f);
            }
        });

    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        protected ImageView ivProductImage;
        protected ImageView ivFoodType;
        protected TextView tvProductTitle;
        protected TextView tvProductDescription;
        protected TextView tvQuantity;
        protected TextView tvProductPrice;
        protected ImageView ivSubtract, ivAdd;
        protected TextView rating;
        public View cell;


        private ProductViewHolder(View view) {
            super(view);
            this.ivProductImage = view.findViewById(R.id.iv_image);
            this.tvProductTitle = view.findViewById(R.id.tv_title);
            this.tvProductDescription = view.findViewById(R.id.tv_desc);
            this.ivFoodType = view.findViewById(R.id.iv_foodType);
            this.tvProductPrice = view.findViewById(R.id.tv_price);
            this.ivSubtract = view.findViewById(R.id.ivSubtractProduct);
            this.ivAdd = view.findViewById(R.id.ivAddProduct);
            this.tvQuantity = view.findViewById(R.id.tvCurrentQuantity);
            this.rating = view.findViewById(R.id.tv_rating);
            cell = view;
        }
    }
}