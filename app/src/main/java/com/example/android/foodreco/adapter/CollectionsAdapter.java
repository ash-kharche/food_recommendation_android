package com.example.android.foodreco.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.foodreco.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CollectionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "CollectionsAdapter";
    Context context;
    List<com.example.android.foodreco.network.response.Collections> collectionsList;

    public CollectionsAdapter(Context c, List<com.example.android.foodreco.network.response.Collections> list) {
        context = c;
        collectionsList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cell_collection_item, viewGroup, false);
        return new CollectionsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {
        try {
            final CollectionsViewHolder holder = (CollectionsViewHolder) viewHolder;
            final com.example.android.foodreco.network.response.Collections collectionsObject = collectionsList.get(position);
            holder.tvText.setText(collectionsObject.getCollectionName());

        } catch (Exception e) {
            Log.e(TAG, "onBindViewHolder", e);
        }
    }

    @Override
    public int getItemCount() {
        return collectionsList.size();
    }

    public class CollectionsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_image)
        ImageView ivImage;
        @BindView(R.id.tv_text)
        TextView tvText;

        public CollectionsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
