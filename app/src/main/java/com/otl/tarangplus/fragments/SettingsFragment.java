package com.otl.tarangplus.fragments;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.otl.tarangplus.Analytics.AnalyticsProvider;
import com.otl.tarangplus.Analytics.WebEngageAnalytics;
import com.otl.tarangplus.customeUI.GradientTextView;
import com.otl.tarangplus.customeUI.MyTextView;
import com.otl.tarangplus.model.PageEvents;
import com.otl.tarangplus.viewModel.SettingsViewModel;
import com.otl.tarangplus.R;
import com.otl.tarangplus.Utils.Constants;
import com.otl.tarangplus.Utils.Helper;
import com.otl.tarangplus.Utils.PreferenceHandler;
import com.otl.tarangplus.rest.Resource;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.view.View.GONE;

public class SettingsFragment extends Fragment {

    public static String TAG = SettingsFragment.class.getSimpleName();
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
    @BindView(R.id.streaming)
    GradientTextView mStreaming;
    @BindView(R.id.registered_status)
    GradientTextView mRegisteredStatus;
    @BindView(R.id.notification)
    Switch mNotification;
    @BindView(R.id.clear_watch_history)
    GradientTextView mClearWatchHistory;
    @BindView(R.id.parental_control)
    Switch mParentalControl;
    Unbinder unbinder;
    @BindView(R.id.change_pin)
    GradientTextView changePin;
    @BindView(R.id.change_pin_divider)
    View changePinDivider;
    @BindView(R.id.parental_view)
    RelativeLayout mParentalView;

    private SettingsViewModel settingsViewModel;
    private View mRootView;

    private WebEngageAnalytics webEngageAnalytics;
    private PageEvents pageEvents;


    public SettingsFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_settings, container, false);
        settingsViewModel = ViewModelProviders.of(this).get(SettingsViewModel.class);
        unbinder = ButterKnife.bind(this, mRootView);
        webEngageAnalytics = new WebEngageAnalytics();
        mRegisteredStatus.setText("Registered Devices");
        return mRootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fireScreenView();
        mHeader.setText(R.string.settings);
        mParentalControl.setChecked(PreferenceHandler.getIsParentControlEnabled(getActivity()));
        mNotification.setChecked(PreferenceHandler.getNotificationEnabled(getActivity()));
        mNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    PreferenceHandler.setNotificationEnabled(getActivity(), true);
                } else {
                    PreferenceHandler.setNotificationEnabled(getActivity(), false);
                }
            }
        });
        boolean enabled = PreferenceHandler.getIsParentControlEnabled(getActivity());
        if (enabled) {
            changePin.setVisibility(View.VISIBLE);
            changePinDivider.setVisibility(View.VISIBLE);
        } else {
            changePin.setVisibility(GONE);
            changePinDivider.setVisibility(GONE);
        }


        mParentalControl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                ParentalControlFragment fragment = new ParentalControlFragment();
                Bundle bundle = new Bundle();
//                bundle.putString(Constants.FROM_PAGE, TAG);
//                bundle.putString(Constants.WHO_INITIATED_THIS, TAG);
                bundle.putBoolean(Constants.IS_PARENTAL_CONTROL_ENABLED, b);
                fragment.setArguments(bundle);
                Helper.addFragmentForDetailsScreen(getActivity(), fragment, ParentalControlFragment.TAG);
            }
        });
        changePin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean b = PreferenceHandler.getIsParentControlEnabled(getActivity());
                if (b) {
                    ParentalControlFragment fragment = new ParentalControlFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.FROM_PAGE, TAG);
                    bundle.putString(Constants.WHO_INITIATED_THIS, TAG);
                    fragment.setArguments(bundle);
                    Helper.addFragmentForDetailsScreen(getActivity(), fragment, ParentalControlFragment.TAG);
                }
            }
        });
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        mClose.setVisibility(GONE);

        boolean loggedIn = PreferenceHandler.isLoggedIn(getActivity());
        boolean kidsProfileActive = PreferenceHandler.isKidsProfileActive(getActivity());
        if (!loggedIn || kidsProfileActive) {
            mParentalView.setVisibility(GONE);
            changePin.setVisibility(GONE);
        }

       setParentalControlUI();
    }

    public void setParentalControlUI() {
        boolean isParentControlEnabled = PreferenceHandler.getIsParentControlEnabled(getActivity());
        if (isParentControlEnabled) {
            mParentalControl.setChecked(true);
        } else {
            mParentalControl.setChecked(false);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.category_back_img, R.id.category_grad_back, R.id.back, R.id.header, R.id.close, R.id.toolbar, R.id.app_bar_layout, R.id.streaming, R.id.registered_status, R.id.clear_watch_history})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.category_back_img:
                break;
            case R.id.category_grad_back:
                break;
            case R.id.back:
                break;
            case R.id.header:
                break;
            case R.id.close:
                break;
            case R.id.toolbar:
                break;
            case R.id.app_bar_layout:
                break;
            case R.id.streaming:
                break;
            case R.id.registered_status:
                RegisteredDevicesWebViewFragment registeredDevicesWebViewFragment = new RegisteredDevicesWebViewFragment();
                Bundle bundle = new Bundle();
                bundle.putString(Constants.FROM_WHERE, SettingsFragment.TAG);
                registeredDevicesWebViewFragment.setArguments(bundle);
                Helper.addFragmentForDetailsScreen(getActivity(), registeredDevicesWebViewFragment, RegisteredDevicesWebViewFragment.TAG);

                break;
            case R.id.clear_watch_history:
                clearWatchHistory();
                break;
        }
    }

    private void clearWatchHistory() {

        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.watch_later_layout);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        GradientTextView cancel = (GradientTextView) dialog.findViewById(R.id.cancel);
        GradientTextView confirm = (GradientTextView) dialog.findViewById(R.id.confirm);
        MyTextView title = (MyTextView) dialog.findViewById(R.id.title);
        title.setVisibility(View.VISIBLE);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearWatchHistoryApi();
                dialog.cancel();
            }
        });

    }

    private void clearWatchHistoryApi() {

        String sessionId = PreferenceHandler.getSessionId(getActivity());
        settingsViewModel.clearWatchHistory(sessionId).observe(this, new Observer<Resource>() {
            @Override
            public void onChanged(@Nullable Resource resource) {
                assert resource != null;
                switch (resource.status) {
                    case LOADING:
                        //need to show progress bar
                        Helper.showProgressDialog(getActivity());
                        break;
                    case SUCCESS:
                        Helper.dismissProgressDialog();
                        Helper.showToast(getActivity(), getString(R.string.watch_history_message), R.drawable.ic_check);
                        if (getActivity().getSupportFragmentManager() != null) {
                            getActivity().getSupportFragmentManager().popBackStackImmediate();

                        } else if (getActivity().getFragmentManager() != null) {
                            getActivity().getSupportFragmentManager().popBackStackImmediate();
                        }
                        break;

                    case ERROR:
                        //need to show error messages.
                        Helper.dismissProgressDialog();
                        break;
                }
            }

        });

    }

    public void fireScreenView() {
        AnalyticsProvider mAnalytics = new AnalyticsProvider(getContext());
        mAnalytics.fireScreenView(AnalyticsProvider.Screens.Settings);
        pageEvents = new PageEvents(Constants.Settings);
        webEngageAnalytics.screenViewedEvent(pageEvents);
    }
}
