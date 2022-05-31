package com.otl.tarangplus.adapters;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.otl.tarangplus.Utils.Constants;
import com.otl.tarangplus.fragments.HomePageItemsFragment;
import com.otl.tarangplus.model.HomeScreenResponse;

public class KidsAdapter extends FragmentStatePagerAdapter {

    private static final String TAG = HomePagerAdapter.class.getName();
    public Context mContext;
    HomeScreenResponse mHomeScreenResponse;
    private TabLayout mTabLayout;


    public KidsAdapter(FragmentManager fm, Context context, HomeScreenResponse dataResponse, TabLayout tabLayout) {
        super(fm);
        mContext = context;
        mHomeScreenResponse = dataResponse;
        mTabLayout = tabLayout;
    }

    @Override
    public Fragment getItem(int position) {
        HomePageItemsFragment customFragment = new HomePageItemsFragment();
        Bundle bundle = new Bundle();

        bundle.putString(Constants.HOME_LINK, "kids");
        bundle.putString(Constants.DISPLAY_TITLE, "kids_profile");
        customFragment.setArguments(bundle);
        customFragment.setOnDisplayClickListener(new HomePageItemsFragment.DisplayTitleClickListener() {
            @Override
            public void onClickDisplayTitle(String title) {
                if (listener != null) {
                    listener.onClickDisplayTitle(title);
                }
            }
        });
        return customFragment;
    }

    public interface DisplayTitleClickListener {
        void onClickDisplayTitle(String title);
    }

    private HomePagerAdapter.DisplayTitleClickListener listener;

    public void setOnDisplayClickListener(HomePagerAdapter.DisplayTitleClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String displayTitle = "Kids";
        return displayTitle;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}

