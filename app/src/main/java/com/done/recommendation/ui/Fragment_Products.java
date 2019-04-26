package com.done.recommendation.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.done.recommendation.R;
import com.done.recommendation.adapter.Adapter_Products;
import com.done.recommendation.database.DbOperations;
import com.done.recommendation.network.response.Collections;
import com.done.recommendation.network.response.Product;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Fragment_Products extends Fragment {

    private static String POSITION = "pos";
    private static String COLLECTION = "collection";
    private static String COLLECTION_ID = "collection_id";
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private Adapter_Products mProductAdapter;

    LinearLayoutManager layoutManager;
    private Collections collection;

    private Activity mActivity;

    public static Fragment_Products newInstance(int position, int collectionId, Collections collection) {
        Bundle bundle = new Bundle();
        bundle.putInt(POSITION, position);
        bundle.putInt(COLLECTION_ID, collectionId);
        bundle.putSerializable(COLLECTION, collection);
        Fragment_Products fragment = new Fragment_Products();
        fragment.setArguments(bundle);
        return fragment;
    }

    public Fragment_Products() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (Activity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        collection = (Collections) getArguments().getSerializable(COLLECTION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_products, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        init();
    }

    private void init() {
        layoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setVisibility(View.VISIBLE);

        List<Product> productList = DbOperations.getProductsOfCollection(mActivity, collection.getCollectionId());
        mProductAdapter = new Adapter_Products(mActivity, productList, Adapter_Products.TYPE_PRODUCT);
        mRecyclerView.setAdapter(mProductAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (null == mRecyclerView.getLayoutManager()) {
            layoutManager = new LinearLayoutManager(mActivity);
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setHasFixedSize(true);
        }
        updateAdapters();
    }

    private void updateAdapters() {
        if (null != mProductAdapter) {
            mProductAdapter.notifyDataSetChanged();
        }
    }
}

