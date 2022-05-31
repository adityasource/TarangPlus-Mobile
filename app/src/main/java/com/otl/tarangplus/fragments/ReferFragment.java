package com.otl.tarangplus.fragments;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.otl.tarangplus.customeUI.GradientTextView;
import com.otl.tarangplus.customeUI.MyTextView;
import com.otl.tarangplus.viewModel.SettingsViewModel;
import com.otl.tarangplus.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ReferFragment extends Fragment {

    public static String TAG = ReferFragment.class.getSimpleName();
    @BindView(R.id.category_back_img)
    ImageView mCategoryBackImg;
    @BindView(R.id.category_grad_back)
    TextView mCategoryGradBack;
    @BindView(R.id.back)
    ImageView mBack;
    @BindView(R.id.header)
    MyTextView mHeader;
    @BindView(R.id.close)
    AppCompatImageView mClose;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.invite)
    GradientTextView mInvite;
    Unbinder unbinder;


    private SettingsViewModel settingsViewModel;
    private View mRootView;


    public ReferFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_refer, container, false);
        settingsViewModel = ViewModelProviders.of(this).get(SettingsViewModel.class);
        unbinder = ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mHeader.setText(R.string.refer_a_freind);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity().getSupportFragmentManager() != null) {
                    getActivity().getSupportFragmentManager().popBackStackImmediate();

                } else if (getActivity().getFragmentManager() != null) {
                    getActivity().getSupportFragmentManager().popBackStackImmediate();
                }
            }
        });
        mClose.setVisibility(View.GONE);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.back, R.id.header, R.id.close, R.id.invite})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                if (getActivity().getSupportFragmentManager() != null) {
                    getActivity().getSupportFragmentManager().popBackStackImmediate();

                } else if (getActivity().getFragmentManager() != null) {
                    getActivity().getSupportFragmentManager().popBackStackImmediate();
                }
                break;
            case R.id.header:
                break;
            case R.id.close:
                break;
            case R.id.invite:
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/html");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml("TarangPlus me link here"));
                startActivity(Intent.createChooser(sharingIntent,"Share to your friend by using these"));
                break;
        }
    }
}
