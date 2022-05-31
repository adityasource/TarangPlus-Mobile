package com.otl.tarangplus.fragments;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;
import com.otl.tarangplus.Analytics.Analytics;
import com.otl.tarangplus.customeUI.GradientTextView;
import com.otl.tarangplus.customeUI.MyEditText;
import com.otl.tarangplus.customeUI.MyTextView;
import com.otl.tarangplus.model.AppEvents;
import com.otl.tarangplus.model.User;
import com.otl.tarangplus.viewModel.SettingsViewModel;
import com.otl.tarangplus.R;
import com.otl.tarangplus.Utils.Constants;
import com.otl.tarangplus.Utils.Helper;
import com.otl.tarangplus.Utils.PreferenceHandler;
import com.otl.tarangplus.rest.ApiService;
import com.otl.tarangplus.rest.PasswordChangeRequest;
import com.otl.tarangplus.rest.RestClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class ChangePasswordFragment extends Fragment {

    public static String TAG = ChangePasswordFragment.class.getSimpleName();
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
    @BindView(R.id.old_password)
    MyEditText mOldPassword;
    @BindView(R.id.old_password_input)
    TextInputLayout mOldPasswordInput;
    @BindView(R.id.new_password)
    MyEditText mNewPassword;
    @BindView(R.id.new_password_input)
    TextInputLayout mNewPasswordInput;
    @BindView(R.id.confirm_new_password)
    MyEditText mConfirmNewPassword;
    @BindView(R.id.confirm_password_input)
    TextInputLayout mConfirmPasswordInput;
    @BindView(R.id.confirm)
    GradientTextView mConfirm;
    Unbinder unbinder;
    private ApiService mApiService;
    private AppEvents appEvents;
    private Analytics mAnalyticsEvent;

    private SettingsViewModel settingsViewModel;
    private View mRootView;


    public ChangePasswordFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_change_password, container, false);
        settingsViewModel = ViewModelProviders.of(this).get(SettingsViewModel.class);
        unbinder = ButterKnife.bind(this, mRootView);

        RestClient mRestClient = new RestClient(getActivity());
        mApiService = mRestClient.getApiService();
        mAnalyticsEvent = Analytics.getInstance(getContext());
        appEvents = new AppEvents(1, "Change Password Screen", "", Constants.AN_APP_TYPE, "", Constants.PAGE_VISIT, "Change Password Screen",
                "", PreferenceHandler.getUserPeriod(getContext()), PreferenceHandler.getUserPlanType(getContext()), PreferenceHandler.getUserId(getContext()));
        mAnalyticsEvent.logAppEvents(appEvents);
        return mRootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mHeader.setText(R.string.change_password);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Helper.dismissKeyboard(getActivity());
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

    @OnClick(R.id.confirm)
    public void onViewClicked() {

        Helper.dismissKeyboard(getActivity());

        String sessionId = PreferenceHandler.getSessionId(getActivity());
        String region = PreferenceHandler.getRegion(getActivity());
        String authToken = Constants.API_KEY;
        User user = new User();
        PasswordChangeRequest request = new PasswordChangeRequest();

        if (mOldPassword.getText() == null || TextUtils.isEmpty(mOldPassword.getText().toString())) {
            mOldPasswordInput.setError(getString(R.string.is_field_empty));
            return;
        }

        if (mOldPassword.getText().toString().trim().length() < 6) {
            mOldPasswordInput.setError(getString(R.string.invalid_password));
            return;
        }

        if (mNewPassword.getText() == null || TextUtils.isEmpty(mNewPassword.getText().toString())) {
            mNewPasswordInput.setError(getString(R.string.is_field_empty));
            return;
        }

        if (mNewPassword.getText().toString().trim().length() < 6) {
            mNewPasswordInput.setError(getString(R.string.password_short));
            return;
        }

        if (mConfirmNewPassword.getText() == null || TextUtils.isEmpty(mConfirmNewPassword.getText().toString())) {
            mConfirmPasswordInput.setError(getString(R.string.is_field_empty));
            return;
        }

        if (mConfirm.getText().toString().trim().length() < 0) {
            mConfirmPasswordInput.setError(getString(R.string.password_short));
            return;
        }

        if (!mNewPassword.getText().toString().trim().equals(mConfirmNewPassword.getText().toString().trim())) {
            mConfirmPasswordInput.setError(getString(R.string.password_do_not_match));
            return;
        }

        user.setCurrentPassword(mOldPassword.getText().toString().trim());
        user.setPassword(mNewPassword.getText().toString().trim());
        user.setConfirmPassword(mNewPassword.getText().toString().trim());
        user.setRegion(region);

        request.setUser(user);
        request.setAuthToken(authToken);


        mApiService.changePassword(sessionId, request)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject object) {
                        if (object != null) {
                            Helper.dismissProgressDialog();
                            Helper.showToast(getActivity(), getString(R.string.password_change_success), R.drawable.ic_check);
                            if (getActivity().getSupportFragmentManager() != null) {
                                getActivity().getSupportFragmentManager().popBackStackImmediate();

                            } else if (getActivity().getFragmentManager() != null) {
                                getActivity().getSupportFragmentManager().popBackStackImmediate();
                            }
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        //need to show error messages.
                        String errorMessage = Constants.getErrorMessage(throwable).getError().getMessage();
                        Helper.showToast(getActivity(), errorMessage, R.drawable.ic_error_icon);
                        Helper.dismissProgressDialog();


                    }
                });

        /*settingsViewModel.changePassword(sessionId, request).observe(this, new Observer<Resource>() {
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
                        Helper.showToast(getActivity(), getString(R.string.password_change_success), R.drawable.ic_check);
                        if (getActivity().getSupportFragmentManager() != null) {
                            getActivity().getSupportFragmentManager().popBackStackImmediate();

                        } else if (getActivity().getFragmentManager() != null) {
                            getActivity().getSupportFragmentManager().popBackStackImmediate();
                        }
                        break;

                    case ERROR:
                        //need to show error messages.
                        Helper.dismissProgressDialog();
                        String errorMessage = resource.message;
                        Helper.showToast(getActivity(), errorMessage, R.drawable.ic_error_icon);
                        Helper.dismissProgressDialog();
                        break;
                }
            }
        });*/
    }
}
