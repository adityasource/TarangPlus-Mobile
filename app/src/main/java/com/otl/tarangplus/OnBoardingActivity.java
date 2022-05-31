package com.otl.tarangplus;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;

import com.otl.tarangplus.BroadcastReciver.NetworkChangeReceiver;
import com.otl.tarangplus.Utils.Constants;
import com.otl.tarangplus.Utils.Helper;
import com.otl.tarangplus.dialogs.WhoIsWatchingDialog;
import com.otl.tarangplus.fragments.EditProfilesFragment;
import com.otl.tarangplus.fragments.InternetUpdateFragment;
import com.otl.tarangplus.fragments.LoginFragment;
import com.otl.tarangplus.fragments.RegisterFragment;
import com.otl.tarangplus.fragments.RegisteredDevicesWebViewFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OnBoardingActivity extends AppCompatActivity implements NetworkChangeReceiver.NetworkChangeListener {

    private static final String TAG = OnBoardingActivity.class.getSimpleName();

    @BindView(R.id.container)
    FrameLayout container;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        ButterKnife.bind(this);

        int requestCode = 0;
        if (getIntent().getExtras() != null)
            requestCode = getIntent().getExtras().getInt("requestCode");

        Bundle bundle = new Bundle();

        if (requestCode == Constants.ACTION_SIGNUP_CLICKED) {
            bundle.putString(Constants.FROM_IN_APP, Constants.FROM_IN_APP);
        }

        try{
            if (getIntent() != null && getIntent().getStringExtra(Constants.FROM_PAGE) != null &&
                    getIntent().getStringExtra(Constants.FROM_PAGE).equalsIgnoreCase(RegisterFragment.TAG)) {
                RegisterFragment fragment = new RegisterFragment();
                bundle.putString(Constants.FROM_PAGE, getIntent().getStringExtra(Constants.FROM_PAGE));
                fragment.setArguments(bundle);
                Helper.addWithoutBackStack(OnBoardingActivity.this, fragment, RegisterFragment.TAG);
                return;
            }
        }catch (Exception e){

        }


        LoginFragment fragment = new LoginFragment();
        bundle.putString(Constants.FROM_PAGE, getIntent().getStringExtra(Constants.FROM_PAGE));
        fragment.setArguments(bundle);
        Helper.addWithoutBackStack(OnBoardingActivity.this, fragment, LoginFragment.TAG);


        receiver = new NetworkChangeReceiver();
        receiver.setNetworkChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            unregisterReceiver(receiver);
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }

    private NetworkChangeReceiver receiver;
    private InternetUpdateFragment framgment;
    private boolean isDiconnected = false;

    @Override
    public void onNetworkDisconnected() {

        framgment = new InternetUpdateFragment();
        if (!isDiconnected) {
            Helper.addFragment(OnBoardingActivity.this, framgment, InternetUpdateFragment.TAG);
            isDiconnected = true;
        }
    }

    @Override
    public void onNetworkConnected() {
        if (isDiconnected) {
            onBackPressed();
            //Helper.getInstance(this).removeCurrentFragment(InternetUpdateFragment.TAG);
            isDiconnected = false;
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                           // | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            //| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            //| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            );
        }
        Helper.setLightStatusBar(this);
        //Helper.clearLightStatusBar(this);
    }

    @Override
    public void onBackPressed() {

        Fragment currentFragment = Helper.getCurrentFragment(OnBoardingActivity.this);
        if (currentFragment != null) {
            if (currentFragment instanceof EditProfilesFragment) {
                super.onBackPressed();
                Fragment currentFragmentTemp = Helper.getCurrentFragment(OnBoardingActivity.this);
                if (currentFragmentTemp instanceof WhoIsWatchingDialog) {
                    ((WhoIsWatchingDialog) currentFragmentTemp).getProfiles();
                }
            } else if (currentFragment instanceof RegisteredDevicesWebViewFragment) {
                super.onBackPressed();
                Fragment settingFrag = Helper.getCurrentFragment(OnBoardingActivity.this);
                if (settingFrag instanceof LoginFragment) {
                    ((LoginFragment) settingFrag).logout();
                }

            } else {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        Helper.setLightStatusBar(this);
        Helper.dismissProgressDialog();
        super.onDestroy();
    }
}
