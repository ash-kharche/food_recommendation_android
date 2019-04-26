package com.done.recommendation.ui;

import android.app.Activity;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.done.recommendation.R;
import com.done.recommendation.database.DatabaseProvider;
import com.done.recommendation.database.DbOperations;
import com.done.recommendation.network.response.Product;
import com.done.recommendation.utils.widgets.SqaureImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Activity_ProductDetail extends Activity {
    @BindView(R.id.ivProductImage)
    SqaureImageView ivProductImage;
    @BindView(R.id.tvProductPrice)
    TextView tvProductPrice;
    @BindView(R.id.tv_product_title_productCoincise)
    TextView tvProductName;
    @BindView(R.id.ivSubtractProduct)
    ImageView ivSubtract;
    @BindView(R.id.ivAddProduct)
    ImageView ivAdd;
    @BindView(R.id.tvCurrentQuantity)
    TextView tvCurrentQuantity;
    @BindView(R.id.tvProductDescription)
    TextView tvProductDescription;
    @BindView(R.id.tvIngredients)
    TextView tvIngredients;
    @BindView(R.id.apd_tv_rating)
    TextView rating;
    @BindView(R.id.layout_nutrients)
    View layoutNutrients;
    @BindView(R.id.tvFats)
    TextView tvFats;
    @BindView(R.id.tvProtiens)
    TextView tvProtiens;
    @BindView(R.id.tvCarbs)
    TextView tvCarbs;
    @BindView(R.id.cart_bottomBar)
    View cartBottomBar;
    @BindView(R.id.cart_items)
    TextView tvCartItems;
    @BindView(R.id.cart_amount)
    TextView tvCartAmount;

    private CartObserver mCartObserver = null;// Database changes
    public static String PRODUCT_ID = "product_id";
    private Product mProduct;

    @OnClick(R.id.tvIngredients)
    public void onIngredientsClicked(View v) {
        if (layoutNutrients.getVisibility() == View.VISIBLE) {
            layoutNutrients.setVisibility(View.GONE);
        } else {
            layoutNutrients.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.cart_bottomBar)
    public void onCartBottomBarClick(View view) {
        Intent intent = new Intent(Activity_ProductDetail.this, Activity_Cart.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        ButterKnife.bind(this);

        init();

    }

    private void init() {
        initProduct();
        updateCartBottomBar();

        mCartObserver = new CartObserver(new Handler());
        if (mCartObserver != null) {
            getContentResolver().registerContentObserver(DatabaseProvider.URI_CART, true, mCartObserver);
        }
    }

    private void updateCartBottomBar() {
        List<Product> productList = DbOperations.getCartItems(Activity_ProductDetail.this);

        if (productList != null) {
            cartBottomBar.setVisibility(View.VISIBLE);

            int totalAmount = 0;
            int quantity = 0;
            for (Product product : productList) {

                if (product.getQuantity() > 0) {
                    quantity += product.getQuantity();
                    int amount = quantity * product.getPrice();
                    totalAmount = totalAmount + amount;
                }
            }
            tvCartItems.setText(quantity + " items in cart ");
            tvCartAmount.setText("PAY " + getString(R.string.rupee_symbol) + totalAmount);

        } else {
            cartBottomBar.setVisibility(View.GONE);
        }
    }

    private void initProduct() {
        Bundle bundle = getIntent().getExtras();
        int productId = bundle.getInt(PRODUCT_ID);

        if (productId > 0) {
            mProduct = DbOperations.getProductByProductId(Activity_ProductDetail.this, productId);

            if (mProduct != null) {
                Glide.with(this)
                        .load(mProduct.getImageUrl()).centerCrop()
                        .placeholder(R.drawable.menu_default)
                        .error(R.drawable.menu_default)
                        .into(ivProductImage);

                ImageView ivFoodType = findViewById(R.id.iv_foodTypeProductConcise);
                if (mProduct.getIsVeg() == 1) {
                    ivFoodType.setImageResource(R.drawable.veg_redesign);
                } else if (mProduct.getIsVeg() == 0) {
                    ivFoodType.setImageResource(R.drawable.non_veg);
                }

                tvProductName.setText(mProduct.getProductName());
                float ratingValue = mProduct.getRating();
                if (ratingValue > 0.0f) {
                    rating.setVisibility(View.VISIBLE);
                    rating.setText(String.valueOf(ratingValue));
                }

                if (ratingValue <= 5 && ratingValue >= 4) {
                    rating.setBackgroundColor(ContextCompat.getColor(Activity_ProductDetail.this, R.color.rating_high));
                } else if (ratingValue < 4 && ratingValue >= 3) {
                    rating.setBackgroundColor(ContextCompat.getColor(Activity_ProductDetail.this, R.color.rating_middle));

                } else {
                    rating.setBackgroundColor(ContextCompat.getColor(Activity_ProductDetail.this, R.color.rating_low));
                }

                tvProductPrice.setText(getString(R.string.rupee_symbol) + String.valueOf(mProduct.getPrice()));
                int productCount = DbOperations.getCartProductQuantity(Activity_ProductDetail.this, productId);
                tvCurrentQuantity.setText(String.valueOf(productCount));

                if (!TextUtils.isEmpty(mProduct.getDescription()) && !mProduct.getDescription().equalsIgnoreCase("name")) {
                    tvProductDescription.setText(mProduct.getDescription());
                    tvProductDescription.setVisibility(View.VISIBLE);
                } else {
                    tvProductDescription.setVisibility(View.GONE);
                }

                String ingredientText = mProduct.getIngredientText();
                String[] array = mProduct.getIngredientText().split(",");
                if (array != null) {
                    StringBuilder sb = new StringBuilder();
                    for (String s : array) {
                        sb.append(s.substring(0, 1).toUpperCase() + s.substring(1));
                        sb.append(", ");
                    }
                    ingredientText = sb.toString().substring(0, (sb.toString().length() - 2));
                }

                if (!TextUtils.isEmpty(ingredientText)) {
                    tvIngredients.setText(ingredientText);
                }

                tvFats.setText("Fats:  " + mProduct.getFats());
                tvProtiens.setText("Protiens:  " + mProduct.getProtiens());
                tvCarbs.setText("Carbs:  " + mProduct.getCarbs());

                ivAdd.setOnClickListener(v -> {
                    int count = DbOperations.addCartProduct(Activity_ProductDetail.this, mProduct);
                    if (count > 0) {
                        ivSubtract.setAlpha(1.0f);
                    }
                    tvCurrentQuantity.setText("" + count);
                });


                ivSubtract.setOnClickListener(v -> {
                    int count = DbOperations.removeCartProduct(Activity_ProductDetail.this, mProduct);
                    tvCurrentQuantity.setText("" + count);
                    if (count != 0) {
                        ivSubtract.setAlpha(1.0f);
                    } else {
                        ivSubtract.setAlpha(0.6f);
                    }
                });
            }
        } else {
            finish();
        }
    }

    public class CartObserver extends ContentObserver {

        public CartObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            updateCartBottomBar();
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            updateCartBottomBar();
        }
    }

}
