package com.otl.tarangplus.fragments;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.otl.tarangplus.BroadcastReciver.NetworkChangeReceiver;
import com.otl.tarangplus.customeUI.GradientTextView;
import com.otl.tarangplus.R;
import com.otl.tarangplus.Utils.Helper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InternetUpdateFragment extends Fragment {

    @BindView(R.id.no_internet_image)
    AppCompatImageView no_internet_image;
    @BindView(R.id.try_again_btn)
    GradientTextView try_again_btn;
    @BindView(R.id.no_int_container)
    LinearLayout no_int_container;

    public static String TAG = InternetUpdateFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.error_no_internet, container, false);
        ButterKnife.bind(this, view);

        if (Helper.isKeyboardVisible(getActivity())){
            Helper.dismissKeyboard(getActivity());
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick(R.id.try_again_btn)
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.try_again_btn) {
            boolean connected = NetworkChangeReceiver.isNetworkConnected(getActivity());
            if (connected) {
                Helper.removeCurrentFragment(getActivity(),TAG);
            }
        }
    }
}
