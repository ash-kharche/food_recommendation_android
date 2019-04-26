package com.done.recommendation.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.done.recommendation.R;
import com.done.recommendation.database.DbOperations;
import com.done.recommendation.network.response.Collections;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Fragment_Collection extends Fragment {

    public static final String TAG = "Fragment_Collection";
    private View view;
    private static final String COLLECTION_POSITION = "collection_position";
    private int position;
    @BindView(R.id.fr_vp_collection)
    ViewPager vpCollection;
    @BindView(R.id.fr_tl_collection)
    TabLayout tlCollection;
    private ViewPagerAdapter pagerAdapter;
    private Activity mActivity;


    public Fragment_Collection() {
    }

    public static Fragment_Collection getInstance(int position) {
        Bundle b = new Bundle();
        b.putInt(COLLECTION_POSITION, position);

        Fragment_Collection fragmentCollection = new Fragment_Collection();
        fragmentCollection.setArguments(b);
        return fragmentCollection;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_collection, container, false);
        ButterKnife.bind(Fragment_Collection.this, view);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        position = bundle.getInt(COLLECTION_POSITION);
        setHasOptionsMenu(true);
        mActivity.invalidateOptionsMenu();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (pagerAdapter != null) {
            pagerAdapter.notifyDataSetChanged();
        }

        setupViewPager();
        tlCollection.setupWithViewPager(this.vpCollection);
    }

    private void setupViewPager() {
        ArrayList<Collections> collections = (ArrayList<Collections>) DbOperations.isDatabaseLoaded(getActivity());
        pagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), collections);
        vpCollection.setAdapter(pagerAdapter);
        vpCollection.setCurrentItem(position);

        tlCollection.setTabMode(TabLayout.MODE_SCROLLABLE);
        tlCollection.setupWithViewPager(this.vpCollection);
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private List<Collections> collections;

        public ViewPagerAdapter(FragmentManager manager, ArrayList<Collections> collectionsList) {
            super(manager);
            collections = collectionsList;
        }

        @Override
        public Fragment getItem(int position) {
            Collections dCollection = collections.get(position);
            return Fragment_Products.newInstance(0, dCollection.getCollectionId(), dCollection);
        }

        @Override
        public int getCount() {

            return null == collections ? 0 : collections.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String text = collections.get(position).getCollectionName();
            SpannableString s = new SpannableString(text);
            final StyleSpan boldext = new StyleSpan(android.graphics.Typeface.BOLD);
            s.setSpan(boldext, 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            return s.toString();
            //return text.subSequence(0, text.length());
        }
    }

}
