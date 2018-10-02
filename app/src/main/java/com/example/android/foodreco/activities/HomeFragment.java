package com.example.android.foodreco.activities;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.foodreco.R;
import com.example.android.foodreco.Utils;
import com.example.android.foodreco.adapter.CollectionsAdapter;
import com.example.android.foodreco.network.ApiClient;
import com.example.android.foodreco.network.ServerApi;
import com.example.android.foodreco.network.response.Collections;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    @BindView(R.id.recyclerview_collections)
    RecyclerView mRecyclerViewCollections;

    //private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        /*Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);

        getCollectinsApi();
        mRecyclerViewCollections.setHasFixedSize(true);
        mRecyclerViewCollections.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    /*
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    */

    private void getCollectinsApi() {
        Utils.showProgressDialog(getActivity(), false);

        ServerApi serverApi = ApiClient.getApiClient();
        Call<List<Collections>> call = serverApi.getCollections();

        call.enqueue(new Callback<List<Collections>>() {
            @Override
            public void onResponse(Call<List<Collections>> call, Response<List<Collections>> response) {
                if(response.isSuccessful()) {
                    List<Collections> collectionsList = response.body();
                    Log.d("poo","onResponse success:  " + collectionsList.size());

                    CollectionsAdapter collectionsAdapter = new CollectionsAdapter(getActivity(), collectionsList);
                    mRecyclerViewCollections.setAdapter(collectionsAdapter);
                    Utils.hideProgressDialog();

                } else {
                    Log.d("poo","onResponse error:  ");
                }
            }

            @Override
            public void onFailure(Call<List<Collections>> call, Throwable t) {
                Log.e("poo","onFailure  " + t);
                Utils.hideProgressDialog();
            }
        });

    }
}
