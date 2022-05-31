package com.otl.tarangplus.fragments;

import android.app.Dialog;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;
import com.otl.tarangplus.Analytics.Analytics;
import com.otl.tarangplus.Analytics.AnalyticsProvider;
import com.otl.tarangplus.Analytics.WebEngageAnalytics;
import com.otl.tarangplus.Database.LayoutDbScheme;
import com.otl.tarangplus.OnBoardingActivity;
import com.otl.tarangplus.R;
import com.otl.tarangplus.Utils.Constants;
import com.otl.tarangplus.Utils.Helper;
import com.otl.tarangplus.Utils.KeyboardUtils;
import com.otl.tarangplus.Utils.PreferenceHandler;
import com.otl.tarangplus.customeUI.GradientTextView;
import com.otl.tarangplus.customeUI.MyEditText;
import com.otl.tarangplus.customeUI.MyTextView;
import com.otl.tarangplus.model.AppEvents;
import com.otl.tarangplus.model.PageEvents;
import com.otl.tarangplus.model.SignInRequest;
import com.otl.tarangplus.model.SignUpData;
import com.otl.tarangplus.model.User;
import com.otl.tarangplus.model.UserEvents;
import com.otl.tarangplus.rest.ApiService;
import com.otl.tarangplus.rest.RestClient;
import com.otl.tarangplus.viewModel.LoginRegistrationViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RegisterFragment extends Fragment {

    public static final String TAG = RegisterFragment.class.getSimpleName();
    ImageView category_back_img;
    @BindView(R.id.email_id)
    MyEditText emailId;
    @BindView(R.id.email_id_text_input)
    TextInputLayout emailIdTextInput;
    private LoginRegistrationViewModel loginRegistrationViewModel;

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.centericon)
    ImageView centericon;

    @BindView(R.id.header)
    MyTextView header;
    @BindView(R.id.close)
    AppCompatImageView close;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.app_bar_layout)
    AppBarLayout app_bar_layout;
    @BindView(R.id.name)
    MyEditText name;
    @BindView(R.id.name_text_input)
    TextInputLayout name_text_input;

    @BindView(R.id.mobile_number)
    MyEditText mobile_number;

    @BindView(R.id.password)
    MyEditText password;
    @BindView(R.id.password_text_input)
    TextInputLayout password_text_input;

    @BindView(R.id.check_box)
    CheckBox check_box;
    @BindView(R.id.privacy_policies)
    MyTextView privacy_policies;

    @BindView(R.id.register_btn)
    MyTextView register_btn;
    @BindView(R.id.error_message)
    MyTextView mErrorMessage;
    @BindView(R.id.parent_view)
    RelativeLayout mParentView;

    //        Top Bar UI
/*    @BindView(R.id.category_back_img)
    ImageView mTopbarImage;*/
    @BindView(R.id.category_grad_back)
    TextView mGradientBackground;
    @BindView(R.id.terms_of_use)
    MyTextView termsOfUse;

    @BindView(R.id.dummy_view)
    EditText dummy_view;

//    @BindView(R.id.login)
//    GradientTextView mLogin;


    @BindView(R.id.email)
    MyTextView email;
    @BindView(R.id.mobile)
    MyTextView mobile;

    @BindView(R.id.mobile_number_text_input)
    TextInputLayout mobile_number_text_input;

    @BindView(R.id.relative_layout2)
    RelativeLayout relative_layout2;

    private String userId = "";
    private ApiService mApiService;
    public boolean isUserIncompletlyRegistered = true;
    AnalyticsProvider analyticsProvider;
    boolean loginIdChoiceEmail;
    private Dialog dialog;
    private String from_page = "";
    WebEngageAnalytics webEngageAnalytics;
    UserEvents userEvents;
    private PageEvents pageEvents;
    private AppEvents appEvents;
    private Analytics mAnalyticsEvent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.regester_screen, container, false);
        ButterKnife.bind(this, inflate);
        header.setText(getString(R.string.register_header));
        header.setVisibility(View.GONE);
        centericon.setVisibility(View.VISIBLE);
        close.setVisibility(View.GONE);
        webEngageAnalytics = new WebEngageAnalytics();
        mApiService = new RestClient(getContext()).getApiService();
        analyticsProvider = new AnalyticsProvider(getActivity());
        from_page = getArguments().getString(Constants.FROM_PAGE);
        setTopbarUI(Constants.getSchemeColor("All"));
        mAnalyticsEvent = Analytics.getInstance(getContext());
        KeyboardUtils.addKeyboardToggleListener(getActivity(), new KeyboardUtils.SoftKeyboardToggleListener() {
            @Override
            public void onToggleSoftKeyboard(boolean isVisible) {
                if (isVisible) {
                    Helper.fullScreenView(getActivity());
                } else {
                    if (Helper.isKeyboardVisible(getActivity())) {
                        Helper.dismissKeyboard(getActivity());
                    }
                }
            }
        });

        appEvents = new AppEvents(1, "Registration Screen", "", Constants.AN_APP_TYPE, "", Constants.PAGE_VISIT, "Registration Screen",
                "", PreferenceHandler.getUserPeriod(getContext()), PreferenceHandler.getUserPlanType(getContext()), PreferenceHandler.getUserId(getContext()));
        mAnalyticsEvent.logAppEvents(appEvents);

        return inflate;
    }

    @Override
    public void onStop() {
        super.onStop();

        if (isUserIncompletlyRegistered) {
            try {
                if (mobile_number.getText().toString().length() > 0 || password.getText().toString().length() > 0
                        || name.getText().toString().length() > 0) {
                    AnalyticsProvider analyticsProvider = new AnalyticsProvider(getActivity());
                    analyticsProvider.fireNetCoreIncompleteReg();
                    isUserIncompletlyRegistered = false;
                }
            } catch (Exception e) {

            }


        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fireScreenView();
        mParentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                    Just to avoid background click
            }
        });


        loginRegistrationViewModel = ViewModelProviders.of(this).get(LoginRegistrationViewModel.class);

        String region = PreferenceHandler.getRegion(getActivity());
        if (!TextUtils.isEmpty(region) && !region.equalsIgnoreCase("IN")) {
            mobile.setVisibility(View.GONE);
            email.setVisibility(View.GONE);
            relative_layout2.setVisibility(View.GONE);
            email.performClick();
            loginIdChoiceEmail = true;
            emailIdTextInput.setVisibility(View.VISIBLE);
            mobile_number_text_input.setVisibility(View.GONE);
            emailId.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    emailIdTextInput.setError(null);
                    emailIdTextInput.setErrorEnabled(false);
                    mErrorMessage.setVisibility(View.GONE);
                }

                @Override
                public void afterTextChanged(Editable s) {
                    String result = s.toString().replaceAll(" ", "");
                    if (!s.toString().equals(result)) {
                        emailId.setText(result);
                        emailId.setSelection(result.length());
                        // alert the user
                    }
                }
            });

        } else {

            this.mobile_number.setInputType(InputType.TYPE_CLASS_NUMBER);
            this.mobile_number_text_input.setHint(getString(R.string.mobile_number));


            /*todo new changes*/
            //email.setVisibility(View.VISIBLE);
            //mobile.setVisibility(View.VISIBLE);
            mobile.setVisibility(View.GONE);
            email.setVisibility(View.GONE);
            relative_layout2.setVisibility(View.GONE);
            /*changes*/
            emailIdTextInput.setVisibility(View.GONE);
            mobile_number_text_input.setVisibility(View.VISIBLE);
            mobile.setTextColor(getResources().getColor(R.color.otv_orange));
            email.setTextColor(getResources().getColor(R.color.text_gray));
            email.setBackground(null);
            mobile.setBackground(getActivity().getDrawable(R.drawable.rounded_orange));

            /*clear other screen*/
            dummy_view.requestFocus();
            password.setText("");
            emailId.setText("");
            name.setText("");
            if (Helper.isKeyboardVisible(getActivity())) {
                Helper.dismissKeyboardWithoutFlags(getActivity());
            }


            mobile_number_text_input.setError(null);
            mobile_number_text_input.setErrorEnabled(false);
            password_text_input.setError(null);
            password_text_input.setErrorEnabled(false);
            mErrorMessage.setVisibility(View.GONE);
            name_text_input.setError(null);
            name_text_input.setErrorEnabled(false);
            mobile_number_text_input.setHint(getResources().getString(R.string.mobile_number));
            loginIdChoiceEmail = false;
            /*changes*/
            mobile.performClick();
//            loginIdChoiceEmail = true;
            loginIdChoiceEmail = false;
            mobile_number.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    mobile_number_text_input.setError(null);
                    mobile_number_text_input.setErrorEnabled(false);
                    mErrorMessage.setVisibility(View.GONE);
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (!editable.toString().startsWith("+91 ") && editable.toString().startsWith("+91") && mobile_number.hasFocus()) {
                        String str = editable.toString();
                        str = str.replace("+91", "+91 ");
                        mobile_number.setText(str);
                        Selection.setSelection(mobile_number.getText(), mobile_number.getText().length());
                        return;
                    }


                    if (!editable.toString().startsWith("+91 ") && mobile_number.hasFocus()) {
                        mobile_number.setText("+91 ");
                        Selection.setSelection(mobile_number.getText(), mobile_number.getText().length());
                    }
                }
            });
            emailId.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    emailIdTextInput.setError(null);
                    emailIdTextInput.setErrorEnabled(false);
                    mErrorMessage.setVisibility(View.GONE);
                }

                @Override
                public void afterTextChanged(Editable s) {
                    String result = s.toString().replaceAll(" ", "");
                    if (!s.toString().equals(result)) {
                        emailId.setText(result);
                        emailId.setSelection(result.length());
                        // alert the user
                    }
                }
            });
            /*email.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    emailIdTextInput.setVisibility(View.VISIBLE);
                    mobile_number_text_input.setVisibility(View.GONE);
                    email.setTextColor(getResources().getColor(R.color.otv_orange));
                    mobile.setTextColor(getResources().getColor(R.color.text_gray));
                    email.setBackground(getActivity().getDrawable(R.drawable.rounded_orange));
                    mobile.setBackground(null);

                    *//*clear other screen*//*
                    dummy_view.requestFocus();
                    name_text_input.setError(null);
                    name_text_input.setErrorEnabled(false);
                    emailId.setText("");
                    mobile_number.setText("");
                    password.setText("");
                    name.setText("");
                    if (Helper.isKeyboardVisible(getActivity())) {
                        Helper.dismissKeyboardWithoutFlags(getActivity());
                    }

                    emailIdTextInput.setError(null);
                    emailIdTextInput.setErrorEnabled(false);
                    mErrorMessage.setVisibility(View.GONE);
                    name_text_input.setError(null);
                    name_text_input.setErrorEnabled(false);
                    password_text_input.setError(null);
                    password_text_input.setErrorEnabled(false);


                    mobile_number_text_input.setHint(getResources().getString(R.string.email_id));
                    loginIdChoiceEmail = true;
//                    emailId.addTextChangedListener(new TextWatcher() {
//                        @Override
//                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                        }
//
//                        @Override
//                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                            emailIdTextInput.setError(null);
//                            emailIdTextInput.setErrorEnabled(false);
//                            mErrorMessage.setVisibility(View.GONE);
//                        }
//
//                        @Override
//                        public void afterTextChanged(Editable s) {
//                            String result = s.toString().replaceAll(" ", "");
//                            if (!s.toString().equals(result)) {
//                                emailId.setText(result);
//                                emailId.setSelection(result.length());
//                                // alert the user
//                            }
//                        }
//                    });
                }
            });*/
            mobile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    emailIdTextInput.setVisibility(View.GONE);
                    mobile_number_text_input.setVisibility(View.VISIBLE);
                    mobile.setTextColor(getResources().getColor(R.color.otv_orange));
                    email.setTextColor(getResources().getColor(R.color.text_gray));
                    email.setBackground(null);
                    mobile.setBackground(getActivity().getDrawable(R.drawable.rounded_orange));

                    /*clear other screen*/
                    dummy_view.requestFocus();
                    password.setText("");
                    emailId.setText("");
                    name.setText("");
                    if (Helper.isKeyboardVisible(getActivity())) {
                        Helper.dismissKeyboardWithoutFlags(getActivity());
                    }


                    mobile_number_text_input.setError(null);
                    mobile_number_text_input.setErrorEnabled(false);
                    password_text_input.setError(null);
                    password_text_input.setErrorEnabled(false);
                    mErrorMessage.setVisibility(View.GONE);
                    name_text_input.setError(null);
                    name_text_input.setErrorEnabled(false);
                    mobile_number_text_input.setHint(getResources().getString(R.string.mobile_number));
                    loginIdChoiceEmail = false;

                }
            });


        }
//        confirm_pwd.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                confirm_pwd_text_input.setError(null);
//                confirm_pwd_text_input.setErrorEnabled(false);
//                mErrorMessage.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                password_text_input.setError(null);
                password_text_input.setErrorEnabled(false);
                mErrorMessage.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                name_text_input.setError(null);
                name_text_input.setErrorEnabled(false);
                mErrorMessage.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        setUpDynamicValidationForViews();

//        mLogin.setOnClickListener(view1 -> {
//            Helper.dismissKeyboard(getActivity());
//            LoginFragment fragment = new LoginFragment();
//            Helper.addFragment(getActivity(), fragment, LoginFragment.TAG);
//
//        });
    }

    private void setUpDynamicValidationForViews() {
        String region = PreferenceHandler.getRegion(getActivity());
        if (!TextUtils.isEmpty(region)) {


            name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (b)
                        return;

                    String firName = name.getText().toString();

                    if (TextUtils.isEmpty(firName)) {
                        name_text_input.setError(getString(R.string.is_field_empty));

                    } else if (firName.trim().length() == 0) {
                        name_text_input.setError(getString(R.string.is_field_empty));
                    }
                    //mobile_number.setInputType(EditorInfo.IME_ACTION_NEXT);
                }
            });

            emailId.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {

                    if (b)
                        return;

                    String emaiId = emailId.getText().toString();
                    if (TextUtils.isEmpty(emaiId)) {
                        emailIdTextInput.setError(getString(R.string.is_field_empty));
                    } else if (!Constants.isEmailValied(emaiId)) {
                        emailIdTextInput.setError(getString(R.string.invalid_email_id));
                    }

                }

            });
            mobile_number.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    String indianMobileNumber = mobile_number.getText().toString();
                    if (region.equalsIgnoreCase("IN")) {
                        mobile_number.setInputType(InputType.TYPE_CLASS_NUMBER);
                        if (b) {
                            new Handler().postDelayed(() -> {
                                if (TextUtils.isEmpty(mobile_number.getText())) {
                                    final String prefix1 = "+91 ";
                                    mobile_number.setText(prefix1);
                                    Selection.setSelection(mobile_number.getText(), mobile_number.getText().length());
                                }
                            }, 200);

                        } else {
                            if (mobile_number.getText().toString().equalsIgnoreCase("+91 "))
                                mobile_number.setText("");
                        }
                    }


                    if (b)
                        return;

                    if (region.equalsIgnoreCase("IN")) {
                        if (indianMobileNumber.trim().length() == 3) {
                            mobile_number_text_input.setError(getString(R.string.is_field_empty));
                        } else if (indianMobileNumber.trim().length() != 14) {
                            mobile_number_text_input.setError(getString(R.string.please_enter_valid_number));
                        }
                    } else {
                        if (TextUtils.isEmpty(indianMobileNumber)) {
                            mobile_number_text_input.setError(getString(R.string.is_field_empty));
                        } else if (!Constants.isEmailValied(indianMobileNumber)) {
                            mobile_number_text_input.setError(getString(R.string.invalid_email_reg));
                        }
                    }

                }
            });

            password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {

                    if (b)
                        return;

                    String password = RegisterFragment.this.password.getText().toString();
                    if (TextUtils.isEmpty(password)) {
                        password_text_input.setError(getString(R.string.is_field_empty));
                    } else if (password.trim().length() < 6) {
                        password_text_input.setError(getString(R.string.password_short));
                    }
                }
            });

//            confirm_pwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//                @Override
//                public void onFocusChange(View view, boolean b) {
//                    if (b)
//                        return;
//
//                    String confirmPassword = confirm_pwd.getText().toString();
//                    if (!password.getText().toString().equalsIgnoreCase(confirmPassword)) {
//                        confirm_pwd_text_input.setError(getString(R.string.password_confirm_password_not_matching));
//                    } else if (TextUtils.isEmpty(confirmPassword)) {
//                        confirm_pwd_text_input.setError(getString(R.string.is_field_empty));
//                    }
//                }
//            });
        }
    }

    private void doOtpVerificationcall(String otp, String type) {
//
//        loginRegistrationViewModel.doOtpVerification(otp, type)
//                .observe(getActivity(), new Observer<Resource>() {
//                    @Override
//                    public void onChanged(@Nullable Resource resource) {
//                        Resource.Status status = resource.status;
//                        if (status == Resource.Status.SUCCESS) {
//                            JsonObject object = (JsonObject) resource.data;
//                            continueLogin(object);
//                        }
//                        if (status == Resource.Status.LOADING) {
//
//                        }
//                        if (status == Resource.Status.ERROR) {
//
//                        }
//                    }
//                });
    }

    private void continueLogin(JsonObject object) {

    }

    @OnClick({R.id.back, R.id.close, R.id.register_btn, R.id.terms_of_use, R.id.privacy_policies})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.terms_of_use) {
            TermsOfUserFragment fragment = new TermsOfUserFragment();
            Bundle bundle = new Bundle();
            bundle.putString(Constants.TYPE, Constants.Type.TERMES_OF_USE);
            fragment.setArguments(bundle);
            Helper.addFragmentForDetailsScreen(getActivity(), fragment, TermsOfUserFragment.TAG);
        } else if (id == R.id.privacy_policies) {
            TermsOfUserFragment fragment = new TermsOfUserFragment();
            Bundle bundle = new Bundle();
            bundle.putString(Constants.TYPE, Constants.Type.PRIVACY_POLICY);
            fragment.setArguments(bundle);
            Helper.addFragmentForDetailsScreen(getActivity(), fragment, TermsOfUserFragment.TAG);
        } else if (id == R.id.back) {
//            Helper.removeCurrentFragment(getActivity(),TAG);
            if (getActivity() != null)
                getActivity().onBackPressed();
        } else if (id == R.id.close) {
//            Helper.removeCurrentFragment(getActivity(),TAG);
            if (getActivity() != null)
                getActivity().onBackPressed();
        } else if (id == R.id.register_btn) {
            String firName = name.getText().toString();

            if (TextUtils.isEmpty(firName)) {
                name_text_input.setError(getString(R.string.is_field_empty));
                return;
            }


            if (firName.trim().length() == 0) {
                name_text_input.setError(getString(R.string.is_field_empty));
                return;
            }
            if (!loginIdChoiceEmail) { //its mobile login
                userId = mobile_number.getText().toString().trim();

                if (mobile_number.getText().toString().contains("+")) {

                    if (userId.trim().length() == 3) {
                        mobile_number_text_input.setError(getString(R.string.is_field_empty));
                        return;
                    }

                    if (mobile_number.getText().toString().trim().length() != 14) {
                        mobile_number_text_input.setError(getString(R.string.invalid_mobile_number));
                        return;
                    }
                } else {

                    if (TextUtils.isEmpty(userId)) {
                        mobile_number_text_input.setError(getString(R.string.is_field_empty));
                        return;
                    }


                    if (!Constants.isEmailValied(mobile_number.getText().toString())) {
                        mobile_number_text_input.setError(getString(R.string.invalid_email_reg));
                        return;
                    }
                }

                String password = this.password.getText().toString();
                if (TextUtils.isEmpty(password)) {
                    password_text_input.setError(getString(R.string.is_field_empty));
                    return;
                }
//            String confirmPassword = this.confirm_pwd.getText().toString();
//            if (TextUtils.isEmpty(confirmPassword)) {
//                confirm_pwd_text_input.setError(getString(R.string.is_field_empty));
//                return;
//            }

                if (password.trim().length() < 6) {
                    password_text_input.setError(getString(R.string.password_short));
                    return;
                }
//            if (!password.equalsIgnoreCase(confirmPassword)) {
//                confirm_pwd_text_input.setError(getString(R.string.password_confirm_password_not_matching));
//                return;
//            }

                if (!check_box.isChecked()) {
                    Helper.showToast(getActivity(), getString(R.string.please_accept_terms_and_conditions), R.drawable.ic_cross);
                    return;
                }

                //analyticsProvider.branchSignUpInitiated(getActivity());

                Helper.dismissKeyboard(getActivity());

                SignInRequest request = new SignInRequest();
                User user = new User();
                String region = PreferenceHandler.getRegion(getActivity());
                user.setType("msisdn");
                PreferenceHandler.setLoggedInType(getActivity(), "msisdn");

                String mobileNumber = userId.replace("+", "");
                user.setuId(mobileNumber.replace(" ", ""));
//                if (region.equalsIgnoreCase("IN")) {
//                    user.setType("msisdn");
//                    String mobileNumber = userId.replace("+", "");
//                    user.setuId(mobileNumber.replace(" ", ""));
//                } else {
//                    user.setType("email");
//                    user.setEmailId(userId);
//                }
                user.setRegion(region);
                user.setPassword(password);
                user.setmLastName("");
                user.setFirstName(firName);
                //user.setCurrentSignInIp(PreferenceHandler.getIp(getActivity()));
                //user.setDeviceData(new DeviceData(Constants.DEVICE_NAME, Constants.DEVICE_TYPE));

                request.setAuthToken(Constants.API_KEY);
                request.setUser(user);
                fireRegisterApi(request);
            } else {
                userId = emailId.getText().toString().trim();
                if (TextUtils.isEmpty(userId)) {
                    emailIdTextInput.setError(getString(R.string.is_field_empty));
                    return;
                }

                if (!Constants.isEmailValied(emailId.getText().toString())) {
                    emailIdTextInput.setError(getString(R.string.invalid_email_reg));
                    return;
                }


                String password = this.password.getText().toString();
                if (TextUtils.isEmpty(password)) {
                    password_text_input.setError(getString(R.string.is_field_empty));
                    return;
                }
//            String confirmPassword = this.confirm_pwd.getText().toString();
//            if (TextUtils.isEmpty(confirmPassword)) {
//                confirm_pwd_text_input.setError(getString(R.string.is_field_empty));
//                return;
//            }

                if (password.trim().length() < 6) {
                    password_text_input.setError(getString(R.string.password_short));
                    return;
                }
//            if (!password.equalsIgnoreCase(confirmPassword)) {
//                confirm_pwd_text_input.setError(getString(R.string.password_confirm_password_not_matching));
//                return;
//            }

                if (!check_box.isChecked()) {
                    Helper.showToast(getActivity(), getString(R.string.please_accept_terms_and_conditions), R.drawable.ic_cross);
                    return;
                }

                //analyticsProvider.branchSignUpInitiated(getActivity());

                Helper.dismissKeyboard(getActivity());

                SignInRequest request = new SignInRequest();
                User user = new User();
                String region = PreferenceHandler.getRegion(getActivity());
//                user.setType("msisdn");
//                String mobileNumber = userId.replace("+", "");
//                user.setuId(mobileNumber.replace(" ", ""));
//                if (region.equalsIgnoreCase("IN")) {
//                    user.setType("msisdn");
//                    String mobileNumber = userId.replace("+", "");
//                    user.setuId(mobileNumber.replace(" ", ""));
//                } else {
                user.setType("email");
                PreferenceHandler.setLoggedInType(getActivity(), "email");

                user.setEmailId(userId);
//                }
                user.setRegion(region);
                user.setPassword(password);
                user.setmLastName("");
                user.setFirstName(firName);
                //user.setCurrentSignInIp(PreferenceHandler.getIp(getActivity()));
                //user.setDeviceData(new DeviceData(Constants.DEVICE_NAME, Constants.DEVICE_TYPE));

                request.setAuthToken(Constants.API_KEY);
                request.setUser(user);
                fireRegisterApi(request);
            }


        }

    }

    private void fireRegisterApi(SignInRequest request) {

        Helper.showProgressDialog(getActivity());
        mApiService.signUp(request)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(signedUpData -> {
                    Helper.dismissProgressDialog();
                    String region = PreferenceHandler.getRegion(getActivity());

                    SignUpData signUpData = signedUpData;
                    if (signUpData != null && !request.getUser().getType().equalsIgnoreCase("MSISDN")) {
                        showLanguageConfirmationPopUp();
                    }else {
                        userEvents = new UserEvents(signedUpData.getData().getFirstname(),signedUpData.getData().getEmailId());
                        webEngageAnalytics.registeredEvent(userEvents);
                    }

//
//                    String userName = signedUpData.getData().getFirstname() != null ? signedUpData.getData().getFirstname() : "";
//                    String phone = signedUpData.getData().getEmailId() != null ? signedUpData.getData().getEmailId() : "";
//
//                    userEvents = new UserEvents(userName,phone);
//                    webEngageAnalytics.registeredEvent(userEvents);


                    isUserIncompletlyRegistered = false;



                    if (region.equalsIgnoreCase("IN") && request.getUser().getType().equalsIgnoreCase("MSISDN")) {
                        OtpScreen otpScreen = new OtpScreen();
                        Bundle bundle = new Bundle();
                        String uid = userId.replace("+", "");
                        bundle.putString(Constants.TYPE, "msisdn");
                        bundle.putString(Constants.USER_ID, uid.replace(" ", ""));
                        bundle.putString(Constants.FROM_PAGE, RegisterFragment.TAG);
                        //analyticsProvider.branchSignUpOtpEntered(getActivity());
                        otpScreen.setArguments(bundle);
                        Helper.addFragment(getActivity(), otpScreen, OtpScreen.TAG);
                    } else {
                        showLanguageConfirmationPopUp();
                       /* String extra = Objects.requireNonNull(getActivity()).getIntent().getStringExtra(Constants.FROM_PAGE);

                        if (!TextUtils.isEmpty(extra) && (extra.equalsIgnoreCase(ShowDetailsPageFragment.TAG)
                                || extra.equalsIgnoreCase(MovieDetailsFragment.TAG))) {
                            getActivity().setResult(Constants.ACTION_ME_LOGIN);
                            getActivity().finish();
                        } else {
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }*/
                    }

                }, throwable -> {
                    Log.e("ERRO: ", throwable.getMessage());
                    Helper.dismissProgressDialog();
                    throwable.printStackTrace();
                    String errorMessage = Constants.getErrorMessage(throwable).getError().getMessage();
                    mErrorMessage.setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(errorMessage) || !errorMessage.equals(" ")) {
                        mErrorMessage.setText(errorMessage);
                    } else {
                        mErrorMessage.setText("Something went wrong, try after sometime");
                    }
                    isUserIncompletlyRegistered = true;
                });

    }

    private void showLanguageConfirmationPopUp() {

        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.watch_later_layout);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        MyTextView title = (MyTextView) dialog.findViewById(R.id.title);
        GradientTextView cancel = (GradientTextView) dialog.findViewById(R.id.cancel);
        GradientTextView confirm = (GradientTextView) dialog.findViewById(R.id.confirm);
        MyTextView warningMessage = (MyTextView) dialog.findViewById(R.id.warning);

        warningMessage.setText("The email has been sent successfully, check your email for verification link");
        title.setText("Registered Successfully!");
        //cancel.setText("No");
        cancel.setVisibility(View.GONE);
        confirm.setText("OK");
        title.setVisibility(View.VISIBLE);
        /*cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = PreferenceHandler.getLang(getActivity());
                dialog.cancel();
            }
        });*/
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                appEvents = new AppEvents(1, Constants.EMAIL, "",Constants.AN_APP_TYPE,"",Constants.LINK_REGISTER,Constants.LINK_REGISTER,
                        "", PreferenceHandler.getUserPeriod(getContext()),PreferenceHandler.getUserPlanType(getContext()),PreferenceHandler.getUserId(getContext()));
                mAnalyticsEvent.logAppEvents(appEvents);
                startActivity((new Intent(getActivity(), OnBoardingActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK)));

            }
        });

    }
/*
    public void getProfiles() {
        Helper.showProgressDialog(getActivity());
        mApiService.getAllUsers(PreferenceHandler.getSessionId(getActivity()))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ProfileData>() {
                    @Override
                    public void call(ProfileData profileData) {

                        Helper.dismissProgressDialog();
                        List<Profile> mListItems = profileData.getData().getProfiles();
                        for (Profile profile : mListItems) {
                            if (profile.isDefaultProfile()) {
                                PreferenceHandler.setCurrentProfileName(getActivity(), profile.getFirstname());
                                PreferenceHandler.setCurrentProfileID(getActivity(), profile.getProfileId());
                                PreferenceHandler.isProfileChanged(getActivity(), true);
                                changeUser(profile);
                            }
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Helper.dismissProgressDialog();
                        throwable.printStackTrace();
                    }
                });
    }

    private void changeUser(Profile pf) {
        UpdateProfileRequest updateProfileRequest = new UpdateProfileRequest();
        updateProfileRequest.setAuthToken(Constants.API_KEY);
        Profile profile = new Profile();
        profile.setProfileId(pf.getProfileId());
        updateProfileRequest.setUserProfile(profile);
        Helper.showProgressDialog(getActivity());

        mApiService.assignProfile(PreferenceHandler.getSessionId(getActivity()), updateProfileRequest)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject jsonObject) {
                        PreferenceHandler.isProfileChanged(getActivity(), true);

                        if (pf.isChild()) {
                            PreferenceHandler.setIsKidProfile(getActivity(), true);
                        } else {
                            PreferenceHandler.setIsKidProfile(getActivity(), false);
                        }

                        Helper.dismissProgressDialog();

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        Helper.dismissProgressDialog();
                    }
                });
    }
*/


    private void setTopbarUI(LayoutDbScheme layoutDbScheme) {
        if (layoutDbScheme != null) {
            if (!TextUtils.isEmpty(layoutDbScheme.getImageUrl())) {
                //Picasso.get().load(layoutDbScheme.getImageUrl()).into(mTopbarImage);
                mGradientBackground.setBackground(Constants.getbackgroundGradient(layoutDbScheme));
            } else {
                //Picasso.get().load(R.color.white).into(mTopbarImage);
                mGradientBackground.setBackground(Constants.getbackgroundGradient(layoutDbScheme));
            }
        }
    }

    public void fireScreenView() {
        AnalyticsProvider mAnalytics = new AnalyticsProvider(getContext());
        mAnalytics.fireScreenView(AnalyticsProvider.Screens.Registration);
        pageEvents = new PageEvents(Constants.Registration);
        webEngageAnalytics.screenViewedEvent(pageEvents);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
