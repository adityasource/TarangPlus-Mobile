package com.otl.tarangplus.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.otl.tarangplus.customeUI.GradientTextView;
import com.otl.tarangplus.customeUI.MyEditText;
import com.otl.tarangplus.customeUI.MyTextView;
import com.otl.tarangplus.model.SignInRequest;
import com.otl.tarangplus.model.User;
import com.otl.tarangplus.viewModel.PasswordResetViewModel;
import com.otl.tarangplus.R;
import com.otl.tarangplus.Utils.Constants;
import com.otl.tarangplus.Utils.Helper;
import com.otl.tarangplus.Utils.PreferenceHandler;
import com.otl.tarangplus.rest.Resource;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PasswordResetFragment extends Fragment {

    public static final String TAG = PasswordResetFragment.class.getSimpleName();

    @BindView(R.id.password)
    MyEditText password;
    @BindView(R.id.password_confirm)
    MyEditText confirmPassword;
    @BindView(R.id.reset_btn)
    GradientTextView resetButton;
    @BindView(R.id.password_text_input)
    TextInputLayout password_text_input;
    @BindView(R.id.password_confirm_text_input)
    TextInputLayout password__confirm_text_input;
    @BindView(R.id.header)
    MyTextView mHeader;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.close)
    AppCompatImageView mClose;
    @BindView(R.id.back)
    ImageView mBack;

    private PasswordResetViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.reset_password, container, false);
        ButterKnife.bind(this, inflate);
        return inflate;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(PasswordResetViewModel.class);

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });
        mHeader.setText(R.string.reset_password);
        mClose.setVisibility(View.GONE);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
    }

    private void resetPassword() {
        String password = this.password.getText().toString();

        if (TextUtils.isEmpty(password)) {
            password_text_input.setError(getString(R.string.is_field_empty));
            return;
        }
        String confirmPassword = this.confirmPassword.getText().toString();
        if (TextUtils.isEmpty(confirmPassword)) {
            password__confirm_text_input.setError(getString(R.string.is_field_empty));
            return;
        }

        if (password.trim().length() < 6) {
            password_text_input.setError(getString(R.string.password_short));
            return;
        }
        if (!password.equalsIgnoreCase(confirmPassword)) {
            password__confirm_text_input.setError(getString(R.string.password_confirm_password_not_matching));
            return;
        }
        SignInRequest request = new SignInRequest();
        User user = new User();
        /**
         * {"auth_token":"tzxN848PaefLW9NA7iDM", "user": {"key":"575265","password":"saranyu",
         * "confirm_password":"saranyu","region":"IN","type":"msisdn"}}
         */
        request.setAuthToken(Constants.API_KEY);
        user.setKey(getArguments().getString(Constants.OTP));
        user.setPassword(password);
        user.setConfirmPassword(confirmPassword);
        user.setRegion(PreferenceHandler.getRegion(getActivity()));
        if (PreferenceHandler.getRegion(getActivity()).equalsIgnoreCase("IN")) {
            user.setType("msisdn");
        } else {
            user.setType("email");
        }
        request.setUser(user);

        viewModel.resetPassword(request)
                .observe(getActivity(), new Observer<Resource>() {
                    @Override
                    public void onChanged(@Nullable Resource resource) {
                        Resource.Status status = resource.status;
                        if (status == Resource.Status.SUCCESS) {
                            Helper.dismissProgressDialog();
                            Helper.dismissKeyboard(getActivity());
                            Helper.showToast(getActivity(), "Your password is updated.\nPlease login with your new password.", R.drawable.ic_check);
                            LoginFragment fragment = new LoginFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString(Constants.FROM_PAGE, PasswordResetFragment.TAG);
                            fragment.setArguments(bundle);
                            Helper.addFragment(getActivity(), fragment, LoginFragment.TAG);
                        } else if (status == Resource.Status.ERROR) {
                            Throwable data = (Throwable) resource.data;

                            Helper.dismissKeyboard(getActivity());
                            Helper.showToast(getActivity(), data.getLocalizedMessage(), R.drawable.ic_cross);
                            Helper.dismissProgressDialog();
                        } else if (status == Resource.Status.LOADING) {
                            Helper.showProgressDialog(getActivity());
                        }
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
