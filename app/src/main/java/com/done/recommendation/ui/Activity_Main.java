package com.done.recommendation.ui;

import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.done.recommendation.Constants;
import com.done.recommendation.R;
import com.done.recommendation.database.DatabaseHelper;
import com.done.recommendation.database.DatabaseProvider;
import com.done.recommendation.database.DbOperations;
import com.done.recommendation.network.response.Product;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Activity_Main extends AppCompatActivity {

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.cart_bottomBar)
    View cartBottomBar;
    @BindView(R.id.cart_items)
    TextView tvCartItems;
    @BindView(R.id.cart_amount)
    TextView tvCartAmount;

    private View navigationViewHeaderView = null;
    private CartObserver mCartObserver = null;// Database changes
    private KeysObserver mKeysObserver = null;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initUi();
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
        addFragmentMain();
        updateCartBottomBar();
        loadUserProfile();
    }

    @Override
    protected void onPause() {
        if (mCartObserver != null) {
            getContentResolver().unregisterContentObserver(mCartObserver);
        }
        if (mKeysObserver != null) {
            getContentResolver().unregisterContentObserver(mKeysObserver);
        }
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawers();
        } else {
            super.onBackPressed();
        }
    }

    private void init() {
        mCartObserver = new CartObserver(new Handler());
        if (mCartObserver != null) {
            getContentResolver().registerContentObserver(DatabaseProvider.URI_CART, true, mCartObserver);
        }

        mKeysObserver = new KeysObserver(new Handler());
        if (mKeysObserver != null) {
            getContentResolver().registerContentObserver(DatabaseProvider.URI_KEYS, true, mKeysObserver);
        }
    }

    private void addFragmentMain() {
        Fragment_Main fragmentMain = new Fragment_Main();
        getSupportFragmentManager().beginTransaction().replace(R.id.am_fragmentContainer, fragmentMain, "Fragment_Main").commitAllowingStateLoss();
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

    public class KeysObserver extends ContentObserver {

        public KeysObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            loadUserProfile();
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            loadUserProfile();
        }
    }

    private void updateCartBottomBar() {
        List<Product> productList = DbOperations.getCartItems(Activity_Main.this);

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

    private void initUi() {
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_back);

        if (mDrawerToggle == null)
            mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {
                @Override
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                }

                @Override
                public void onDrawerClosed(View drawerView) {
                    super.onDrawerClosed(drawerView);
                }
            };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here
                        int id = menuItem.getItemId();

                        switch (id) {
                            case R.id.nav_home:
                                addFragmentMain();
                                break;
                            case R.id.nav_questions:
                                Intent ii = new Intent(Activity_Main.this, Activity_Questions.class);
                                ii.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(ii);
                                break;
                            case R.id.nav_cart:
                                DbOperations.clearCart(Activity_Main.this);
                                break;
                            case R.id.nav_orders:
                                Intent i = new Intent(Activity_Main.this, Activity_Orders.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(i);
                                break;
                            case R.id.nav_logout:
                                showLogoutDialog();
                                break;
                        }
                        return true;
                    }
                });

        navigationViewHeaderView = navigationView.getHeaderView(0);
        loadUserProfile();

        cartBottomBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity_Main.this, Activity_Cart.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
    }

    private void loadUserProfile() {
        if (navigationViewHeaderView != null) {
            navigationViewHeaderView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int userId = 0;
                    try {
                        userId = Integer.parseInt(DbOperations.getKeyValue(Activity_Main.this, DatabaseHelper.Keys.USER_ID));
                    } catch (NumberFormatException e) {
                        Log.e(Constants.TAG, "Profile: userId is null", e);
                    }

                    if (userId == 0) {
                        Intent i = new Intent(Activity_Main.this, Activity_Login.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(i);
                    } else {
                        Intent i = new Intent(Activity_Main.this, Activity_SignUp.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(i);
                    }
                }
            });

            try {
                ((TextView) navigationViewHeaderView.findViewById(R.id.tv_name)).setText(DbOperations.getKeyValue(Activity_Main.this, DatabaseHelper.Keys.USER_NAME));
                ((TextView) navigationViewHeaderView.findViewById(R.id.tv_email)).setText(DbOperations.getKeyValue(Activity_Main.this, DatabaseHelper.Keys.USER_EMAIL));
                ((TextView) navigationViewHeaderView.findViewById(R.id.tv_mobile_no)).setText(DbOperations.getKeyValue(Activity_Main.this, DatabaseHelper.Keys.USER_MOBILE));
            } catch (Exception e) {
            }
        }
    }

    private void showLogoutDialog() {
        if (!isDestroyed() || !isFinishing()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
            builder.setTitle(getResources().getString(R.string.logout_title));

            builder.setMessage(getResources().getString(R.string.logout_message));

            builder.setPositiveButton(getString(R.string.confirm), (dialog, which) -> {
                dialog.dismiss();
                logoutApi();

            });
            builder.setNegativeButton(getString(R.string.cancel), (dialog, which) -> {
                dialog.dismiss();
            });

            AlertDialog dialog1 = builder.create();
            dialog1.show();
        }
    }

    private void logoutApi() {
        DbOperations.logout(Activity_Main.this);
        Intent i = new Intent(Activity_Main.this, Activity_Login.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
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