package com.otl.tarangplus.fragments;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.os.Bundle;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;
import com.otl.tarangplus.Database.LayoutDbScheme;
import com.otl.tarangplus.R;
import com.otl.tarangplus.Utils.Constants;
import com.otl.tarangplus.Utils.Helper;
import com.otl.tarangplus.Utils.KeyboardUtils;
import com.otl.tarangplus.Utils.PreferenceHandler;
import com.otl.tarangplus.customeUI.GradientTextView;
import com.otl.tarangplus.customeUI.MyEditText;
import com.otl.tarangplus.customeUI.MyTextView;
import com.otl.tarangplus.model.SignInRequest;
import com.otl.tarangplus.model.User;
import com.otl.tarangplus.rest.ApiService;
import com.otl.tarangplus.rest.RestClient;
import com.otl.tarangplus.viewModel.LoginRegistrationViewModel;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class ForgotPasswordFragment extends Fragment {

    public static final String TAG = ForgotPasswordFragment.class.getSimpleName();
    @BindView(R.id.back)
    AppCompatImageView back;
    @BindView(R.id.header)
    MyTextView header;
    @BindView(R.id.close)
    AppCompatImageView close;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.app_bar_layout)
    AppBarLayout app_bar_layout;
    @BindView(R.id.words)
    MyTextView words;
    @BindView(R.id.mobile_number)
    MyEditText mobile_number;
    @BindView(R.id.mobile_number_text_input)
    TextInputLayout mobile_number_text_input;
    @BindView(R.id.login_container)
    RelativeLayout login_container;
    @BindView(R.id.confirm)
    GradientTextView confirm;
    @BindView(R.id.error_message)
    MyTextView mErrorMessage;

    //        Top Bar UI
    @BindView(R.id.category_back_img)
    ImageView mTopbarImage;
    @BindView(R.id.category_grad_back)
    TextView mGradientBackground;

    LoginRegistrationViewModel loginRegistrationViewModel;
    @BindView(R.id.email)
    MyTextView email;
    @BindView(R.id.mobile)
    MyTextView mobile;


    @BindView(R.id.email_id)
    MyEditText emailId;
    @BindView(R.id.email_id_text_input)
    TextInputLayout emailIdTextInput;

    @BindView(R.id.relative_layout2)
    RelativeLayout relative_layout2;

    private ApiService mApiService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.forgot_password, container, false);
        ButterKnife.bind(this, inflate);
        header.setText(getString(R.string.forgot_password));
        mApiService = new RestClient(getActivity()).getApiService();
        close.setVisibility(View.GONE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        setTopbarUI(Constants.getSchemeColor("All"));
        KeyboardUtils.addKeyboardToggleListener(getActivity(), new KeyboardUtils.SoftKeyboardToggleListener() {
            @Override
            public void onToggleSoftKeyboard(boolean isVisible) {
                if (isVisible) {
                    Helper.fullScreenView(getActivity());
                } else {
                    if (Helper.isKeyboardVisible(getActivity())) {
                        Helper.dismissKeyboardWithoutFlags(getActivity());
                    }
                }
            }
        });
        return inflate;
    }

    private boolean loginIdChoiceEmail;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String region = PreferenceHandler.getRegion(getActivity());
        loginRegistrationViewModel = ViewModelProviders.of(this).get(LoginRegistrationViewModel.class);
        mobile.performClick();
        loginIdChoiceEmail = false;
        mobile_number.setInputType(InputType.TYPE_CLASS_PHONE);
        if(region.equalsIgnoreCase("IN"))
            words.setText(getString(R.string.forgot_sample_text));
        else
            words.setText(getString(R.string.enter_shemaroo_word_email_id));
        final String prefix = "+91 ";
        mobile_number.setText(prefix);
        Selection.setSelection(mobile_number.getText(), mobile_number.getText().length());

        emailIdTextInput.setHint(getString(R.string.email_id));
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginIdChoiceEmail = true;
                emailIdTextInput.setVisibility(View.VISIBLE);
                email.setTextColor(getResources().getColor(R.color.otv_orange));
                mobile.setTextColor(getResources().getColor(R.color.text_gray));
                email.setBackground(getActivity().getDrawable(R.drawable.rounded_orange));
                mobile.setBackground(null);
               // mobile_number.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS);
                //mobile_number_text_input.setHint(getString(R.string.email_id));
                emailId.setText("");
                mobile_number.setText("");
                emailIdTextInput.setHint(getString(R.string.email_id));
                emailId.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS);
                mErrorMessage.setVisibility(View.GONE);
                words.setText(getString(R.string.enter_shemaroo_word_email_id));
                mobile_number_text_input.setVisibility(View.GONE);
            }
        });
        mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginIdChoiceEmail = false;
                mobile_number_text_input.setVisibility(View.VISIBLE);
                emailIdTextInput.setVisibility(View.GONE);
                mobile.setTextColor(getResources().getColor(R.color.otv_orange));
                email.setTextColor(getResources().getColor(R.color.text_gray));
                email.setBackground(null);
                emailId.setText("");
                mobile_number.setText("");
                mErrorMessage.setVisibility(View.GONE);
                mobile.setBackground(getActivity().getDrawable(R.drawable.rounded_orange));
                mobile_number.setInputType(InputType.TYPE_CLASS_PHONE);
                words.setText(getString(R.string.forgot_sample_text));
                final String prefix = "+91 ";
                mobile_number.setText(prefix);
                Selection.setSelection(mobile_number.getText(), mobile_number.getText().length());

            }
        });
        if (!region.equalsIgnoreCase("IN")) {

            relative_layout2.setVisibility(View.GONE);
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


        } else {
            mobile.setVisibility(View.VISIBLE);
            email.setVisibility(View.VISIBLE);
            if (loginIdChoiceEmail) {
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
                emailIdTextInput.setVisibility(View.GONE);
                mobile_number_text_input.setVisibility(View.VISIBLE);
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
                        if (!editable.toString().startsWith("+91 ")) {
                            mobile_number.setText("+91 ");
                            Selection.setSelection(mobile_number.getText(), mobile_number.getText().length());
                        }
                    }
                });

            }


        }

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Helper.dismissKeyboard(getActivity());
                confirm();
            }
        });

    }

    public void confirm() {

        String mobileText = mobile_number.getText().toString();
        String emailText = emailId.getText().toString();

        if(loginIdChoiceEmail && TextUtils.isEmpty(emailText)){
            emailIdTextInput.setError(getString(R.string.is_field_empty));
            return;
        }else if(!loginIdChoiceEmail && TextUtils.isEmpty(mobileText)){
            mobile_number_text_input.setError(getString(R.string.is_field_empty));
            return;
        }


            String region = PreferenceHandler.getRegion(getActivity());
            SignInRequest request = new SignInRequest();
            final User user = new User();
            request.setAuthToken(Constants.API_KEY);

            if (region.equalsIgnoreCase("IN")) {

                if (loginIdChoiceEmail) {
                    if (TextUtils.isEmpty(emailText)) {
                        Helper.showToast(getActivity(), getString(R.string.is_field_empty), R.drawable.ic_cross);
                        return;
                    }

                    user.setType("email");
                    String indianMobileNumber = emailText;
                    user.setuId(indianMobileNumber);
                    request.setUser(user);
                } else {
                    if (mobileText.trim().length() == 3) {
                        mobile_number_text_input.setError(getString(R.string.is_field_empty));
                        return;
                    }

                    if (mobileText.trim().length() != 14) {
                        mobile_number_text_input.setError(getString(R.string.invalid_mobile_number));
                        return;
                    }

                    user.setType("msisdn");
                    String indianMobileNumber = mobileText.trim().replace("+", "");
                    user.setuId(indianMobileNumber.replaceAll(" ", ""));
                    request.setUser(user);
                }

            } else {

                if (TextUtils.isEmpty(emailText)) {
                    Helper.showToast(getActivity(), getString(R.string.is_field_empty), R.drawable.ic_cross);
                    return;
                }

                user.setType("email");
                String indianMobileNumber = emailText;
                user.setuId(indianMobileNumber);
                request.setUser(user);
            }
            Helper.showProgressDialog(getActivity());
            mApiService.forgotPassword(request)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.newThread())
                    .subscribe(new Action1<JsonObject>() {
                        @Override
                        public void call(JsonObject jsonObject) {
                            Helper.dismissProgressDialog();
                            OtpScreen otpScreen = new OtpScreen();
                            Bundle bundle = new Bundle();
                            Helper.dismissKeyboard(getActivity());
                            if (region.equalsIgnoreCase("IN")) {
                                if (loginIdChoiceEmail) {
                                    LoginFragment fragment = new LoginFragment();
                                    bundle.putString(Constants.TYPE, "email");
                                    bundle.putString(Constants.FROM_PAGE, ForgotPasswordFragment.TAG);
                                    fragment.setArguments(bundle);
                                    Helper.addFragment(getActivity(), fragment, OtpScreen.TAG);
                                    Helper.showToast(getActivity(), getString(R.string.enter_otp_sent_to_email), R.drawable.ic_check);
                                } else {
                                    String uid = request.getUser().getuId();
                                    bundle.putString(Constants.TYPE, "msisdn");
                                    bundle.putString(Constants.FROM_PAGE, ForgotPasswordFragment.TAG);
                                    bundle.putString(Constants.USER_ID, uid.replace(" ", ""));
                                    otpScreen.setArguments(bundle);
                                    Helper.addFragment(getActivity(), otpScreen, OtpScreen.TAG);
                                    Helper.showToast(getActivity(), getString(R.string.enter_otp_sent_to_mobile_resend), R.drawable.ic_check);

                                }

                            } else {
                                LoginFragment fragment = new LoginFragment();
                                bundle.putString(Constants.TYPE, "email");
                                bundle.putString(Constants.FROM_PAGE, ForgotPasswordFragment.TAG);
                                fragment.setArguments(bundle);
                                Helper.addFragment(getActivity(), fragment, LoginFragment.TAG);
                                Helper.showToast(getActivity(), getString(R.string.enter_otp_sent_to_email), R.drawable.ic_check);
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            //ResponseBody responseBody = ((HttpException) throwable).response().errorBody();
                            //String errorMessage = Constants.getErrorMessage(responseBody);
                            throwable.printStackTrace();
                            String errorMessage = Constants.getErrorMessage(throwable).getError().getMessage();
                            mErrorMessage.setVisibility(View.VISIBLE);
                            mErrorMessage.setText(errorMessage);
                            Helper.dismissProgressDialog();
                            Helper.dismissKeyboard(getActivity());
                        }
                    });

    }

    private void setTopbarUI(LayoutDbScheme layoutDbScheme) {
        if (layoutDbScheme != null) {
            if (!TextUtils.isEmpty(layoutDbScheme.getImageUrl())) {
                Picasso.get().load(layoutDbScheme.getImageUrl()).into(mTopbarImage);
                mGradientBackground.setBackground(Constants.getbackgroundGradient(layoutDbScheme));
            } else {
                Picasso.get().load(R.color.white).into(mTopbarImage);
                mGradientBackground.setBackground(Constants.getbackgroundGradient(layoutDbScheme));
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
