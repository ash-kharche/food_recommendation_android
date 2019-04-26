package com.done.recommendation.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.done.recommendation.R;
import com.done.recommendation.network.response.Collections;
import com.done.recommendation.ui.Activity_Main;
import com.done.recommendation.ui.Fragment_Collection;
import com.done.recommendation.ui.Fragment_Main;
import com.done.recommendation.utils.widgets.SqaureImageView;

import java.util.List;

public class Adapter_Collections extends RecyclerView.Adapter<Adapter_Collections.ViewHolder> {

    private Context context;
    private List<Collections> collectionsList;

    public void updateContents(List<Collections> contents) {
        this.collectionsList = contents;
    }

    public Adapter_Collections(Context context, List<Collections> collectionsList) {
        this.collectionsList = collectionsList;
        /*if(collectionsList != null) {
            Log.d(Constants.TAG, "Adapter_collections: collectionsList:  " + collectionsList.size());
        }*/
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_grid_collection, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Collections collection = collectionsList.get(position);

        Glide.with(context).load(collection.getImageUrl())
                .centerCrop()
                .placeholder(R.drawable.menu_default)
                .error(R.drawable.menu_default)
                .into(holder.squareCollectionImage);

        holder.collectionName.setText(collection.getCollectionName());
        holder.cell.setOnClickListener(view -> {
            ((Activity_Main) context).getSupportFragmentManager().beginTransaction().replace(R.id.am_fragmentContainer, Fragment_Collection.getInstance(position), Fragment_Collection.TAG).addToBackStack(Fragment_Main.class.getName()).commit();

        });
    }

    @Override
    public int getItemCount() {
        if (collectionsList != null) {
            return collectionsList.size();
        }
        return -1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView collectionName;
        public SqaureImageView squareCollectionImage;
        public View cell;

        public ViewHolder(View itemView) {
            super(itemView);
            collectionName = itemView.findViewById(R.id.cgc_tv_collectionName);
            squareCollectionImage = itemView.findViewById(R.id.cgc_iv_collectionImage);
            cell = itemView;
        }
    }

}