package com.otl.tarangplus.fragments;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.app.Application;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.otl.tarangplus.Analytics.Analytics;
import com.otl.tarangplus.Analytics.AnalyticsProvider;
import com.otl.tarangplus.Analytics.WebEngageAnalytics;
import com.otl.tarangplus.Database.LayoutDbScheme;
import com.otl.tarangplus.MainActivity;
import com.otl.tarangplus.MyApplication;
import com.otl.tarangplus.R;
import com.otl.tarangplus.SplashScreenActivity;
import com.otl.tarangplus.Utils.Constants;
import com.otl.tarangplus.Utils.Helper;
import com.otl.tarangplus.Utils.KeyboardUtils;
import com.otl.tarangplus.Utils.PreferenceHandler;
import com.otl.tarangplus.customeUI.GradientTextView;
import com.otl.tarangplus.customeUI.MyEditText;
import com.otl.tarangplus.customeUI.MyTextView;
import com.otl.tarangplus.model.AppEvents;
import com.otl.tarangplus.model.CampaignData;
import com.otl.tarangplus.model.DeviceData;
import com.otl.tarangplus.model.ListResonse;
import com.otl.tarangplus.model.LoggedInData;
import com.otl.tarangplus.model.LoginEvents;
import com.otl.tarangplus.model.PageEvents;
import com.otl.tarangplus.model.PlayListDataResponse;
import com.otl.tarangplus.model.PlayListResponse;
import com.otl.tarangplus.model.SignInRequest;
import com.otl.tarangplus.model.User;
import com.otl.tarangplus.model.UserEvents;
import com.otl.tarangplus.model.UserInfo;
import com.otl.tarangplus.rest.ApiService;
import com.otl.tarangplus.rest.RestClient;
import com.otl.tarangplus.viewModel.LoginRegistrationViewModel;
import com.webengage.sdk.android.WebEngage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.concurrent.Executor;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

//import static com.facebook.appevents.UserDataStore.EMAIL;

public class LoginFragment extends Fragment {

    public static final String TAG = LoginFragment.class.getSimpleName();
    private static final int RC_SIGN_IN = 0;
    private Dialog dialog;
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
    @BindView(R.id.mobile_number)
    MyEditText mobile_number;
    @BindView(R.id.mobile_number_text_input)
    TextInputLayout mobile_number_text_input;

    @BindView(R.id.password)
    MyEditText password;
    @BindView(R.id.password_text_input)
    TextInputLayout password_text_input;

    @BindView(R.id.login_btn)
    GradientTextView login_btn;
    @BindView(R.id.forgot_pwd)
    GradientTextView forgot_pwd;
    @BindView(R.id.register)
    GradientTextView register;
    @BindView(R.id.parentPanel)
    RelativeLayout parentPanel;
    @BindView(R.id.error_message)
    MyTextView mErrorMessage;

    //        Top Bar UI
   /* @BindView(R.id.category_back_img)
    ImageView mTopbarImage;*/

    @BindView(R.id.centericon)
    ImageView centericon;

    @BindView(R.id.category_grad_back)
    TextView mGradientBackground;
    AnalyticsProvider mAnalytics;
    @BindView(R.id.email)
    MyTextView email;
    @BindView(R.id.mobile)
    MyTextView mobile;
    @BindView(R.id.email_id)
    MyEditText email_Id;
    @BindView(R.id.email_id_text_input)
    TextInputLayout emailIdTextInput;
    @BindView(R.id.dummy_view)
    EditText dummy_view;
    @BindView(R.id.google)
    ImageView google;
    /*@BindView(R.id.login_button)
    LoginButton loginButton;*/
 /*   @BindView(R.id.facebookclick)
    ImageView facebookclick;*/

    @BindView(R.id.relative_layout2)
    RelativeLayout relative_layout2;
    @BindView(R.id.facebook)
    ImageView facebook;
    @BindView(R.id.login_button)
    LoginButton loginButton;
    @BindView(R.id.login)
    MyTextView login;


    private LoginRegistrationViewModel loginRegistrationViewModel;
    private ApiService mApiService;
    private boolean loginIdChoiceEmail;
    private CallbackManager callbackManager;
    private String fbUserId = "", fbUserName = "", fbGender = "", fbEmail = "";
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInAccount account;
    String personName;
    String personGivenName;
    String personFamilyName;
    String personEmail;
    String personId;
    Uri personPhoto;
    //   private AppEvents appEvents;

    WebEngageAnalytics webEngageAnalytics;
    LoginEvents loginEvents;
    private PageEvents pageEvents;
    private AppEvents appEvents;
    private com.otl.tarangplus.Analytics.Analytics mAnalyticsEvent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_screen, container, false);
        ButterKnife.bind(this, view);
        mApiService = new RestClient(getContext()).getApiService();
        webEngageAnalytics = new WebEngageAnalytics();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        FacebookSdk.sdkInitialize(getApplicationContext());
        mAnalytics = new AnalyticsProvider(getContext());
        header.setText(getString(R.string.login));
        back.setVisibility(View.VISIBLE);
        close.setVisibility(View.GONE);
        mAnalyticsEvent = Analytics.getInstance(getContext());
        header.setVisibility(View.GONE);
        centericon.setVisibility(View.VISIBLE);
        KeyboardUtils.addKeyboardToggleListener(getActivity(), isVisible -> {
            if (isVisible) {
                Helper.fullScreenView(getActivity());
            } else {
                if (Helper.isKeyboardVisible(getActivity())) {
                    Helper.dismissKeyboard(getActivity());
                }
            }
        });


        setUpDynamicValidationForViews();
        setTopbarUI(Constants.getSchemeColor("All"));
        callbackManager = CallbackManager.Factory.create();

        loginButton.setPermissions(Arrays.asList("public_profile, email"));
        loginButton.setFragment(this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);
        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGoogleSignInClient.signOut();
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        appEvents = new AppEvents(1, "Login Screen", "", Constants.AN_APP_TYPE, "", Constants.PAGE_VISIT, "Login Screen",
                "", PreferenceHandler.getUserPeriod(getContext()), PreferenceHandler.getUserPlanType(getContext()), PreferenceHandler.getUserId(getContext()));
        mAnalyticsEvent.logAppEvents(appEvents);

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.performClick();
            }
        });
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                System.out.println("onSuccess");
                String accessToken = loginResult.getAccessToken().getToken();

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {

                        if (object != null) {
                            Log.i("LoginActivity", response.toString());

                            try {

                                fbUserId = object.getString("id");
                                fbUserName = object.getString("name");
                                fbEmail = object.getString("email");

                                sendFBDetailsToServer("facebook");


                            } catch (Exception e) {
                                e.printStackTrace();
                                if (e.getMessage().equalsIgnoreCase("No value for email")) {
                                    fbEmail = "";
                                    try {
                                        fbUserName = object.getString("name");
                                        fbUserId = object.getString("id");
                                    } catch (JSONException e1) {
                                        e1.printStackTrace();
                                    }
                                    sendFBDetailsToServer("facebook");
                                }
                            }
                        }
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.i("LoginActivity", "");
            }

            @Override
            public void onError(FacebookException error) {
                Log.i("LoginActivity", "");
            }
        });
        return view;
    }

    private void sendFBDetailsToServer(final String provider) {
        User user = new User();
        user.setRegion(PreferenceHandler.getRegion(getActivity()));
        user.setEmailId(fbEmail);
        user.setUserId(fbUserId);
        user.setProvider(provider);
        user.setFirstName(fbUserName);

        SignInRequest request = new SignInRequest();
        request.setAuthToken(Constants.API_KEY);
        request.setUser(user);
        Helper.showProgressDialog(getActivity());
        mApiService.fbLogin(request)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<LoggedInData>() {
                    @Override
                    public void call(LoggedInData signInFBRequest) {
                        Helper.deleteSearchHistory(getActivity());
                        Helper.dismissKeyboard(getActivity());
                        if ((LoggedInData) signInFBRequest != null) {
                            PreferenceHandler.setFacebook(getActivity(), "facebook");
                            continueLogin((LoggedInData) signInFBRequest);
                            appEvents = new AppEvents(1, Constants.FACEBOOK, "", Constants.AN_APP_TYPE, "", Constants.LINK_LOGIN, Constants.LINK_LOGIN,
                                    "", PreferenceHandler.getUserPeriod(getContext()), PreferenceHandler.getUserPlanType(getContext()), PreferenceHandler.getUserId(getContext()));
                            Log.d("MobileLoginAppevent", appEvents.toString());
                        }
                        mAnalyticsEvent.logAppEvents(appEvents);
                        Helper.dismissProgressDialog();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        Helper.dismissProgressDialog();
                        String errorMessage = Constants.getErrorMessage(throwable).getError().getMessage();
                        mErrorMessage.setVisibility(View.VISIBLE);
                        if (!TextUtils.isEmpty(errorMessage) || !errorMessage.equals(" ")) {
                            mErrorMessage.setText(errorMessage);
                        } else {
                            mErrorMessage.setText("Something went wrong, try after sometime");
                        }
                    }
                });

    }

    private void sendGoogleDetailsToServer(final String provider) {
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());
        if (acct != null) {

            personName = acct.getDisplayName();
            personGivenName = acct.getGivenName();
            personFamilyName = acct.getFamilyName();
            personEmail = acct.getEmail();
            personId = acct.getId();
            personPhoto = acct.getPhotoUrl();
        }
        User user = new User();
        user.setRegion(PreferenceHandler.getRegion(getActivity()));
        user.setExtAccountEmailId(personEmail);
        user.setUserId(personId);
        user.setProvider(provider);
        user.setFirstName(personName);

        SignInRequest request = new SignInRequest();
        request.setAuthToken(Constants.API_KEY);
        request.setUser(user);
        Helper.showProgressDialog(getActivity());
        mApiService.fbLogin(request)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<LoggedInData>() {
                    @Override
                    public void call(LoggedInData signInFBRequest) {
                        Helper.deleteSearchHistory(getActivity());
                        Helper.dismissKeyboard(getActivity());
                        if ((LoggedInData) signInFBRequest != null) {
                            PreferenceHandler.setGoogle(getActivity(), "google");
                            //  continueLogin((LoggedInData) signInFBRequest);
                        }

                        appEvents = new AppEvents(1, Constants.GOOGLE, "", Constants.AN_APP_TYPE, "", Constants.LINK_LOGIN, Constants.LINK_LOGIN,
                                "", PreferenceHandler.getUserPeriod(getContext()), PreferenceHandler.getUserPlanType(getContext()), PreferenceHandler.getUserId(getContext()));
                        Log.d("MobileLoginAppevent", appEvents.toString());

                        mAnalyticsEvent.logAppEvents(appEvents);
                        continueLogin((LoggedInData) signInFBRequest);

                        Helper.dismissProgressDialog();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        Helper.dismissProgressDialog();
                        String errorMessage = Constants.getErrorMessage(throwable).getError().getMessage();
                        mErrorMessage.setVisibility(View.VISIBLE);
                        if (!TextUtils.isEmpty(errorMessage) || !errorMessage.equals(" ")) {
                            mErrorMessage.setText(errorMessage);
                        } else {
                            mErrorMessage.setText("Something went wrong, try after sometime");
                        }
                    }
                });

    }

    private void setUpDynamicValidationForViews() {
        String region = PreferenceHandler.getRegion(getActivity());
        if (!TextUtils.isEmpty(region)) {
            if (region.equalsIgnoreCase("IN")) {
                loginIdChoiceEmail = false;
                email.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        emailIdTextInput.setVisibility(View.VISIBLE);
                        mobile_number_text_input.setVisibility(View.GONE);
                        email.setTextColor(getResources().getColor(R.color.otv_orange));
                        mobile.setTextColor(getResources().getColor(R.color.text_gray));
                        loginIdChoiceEmail = true;
                        mobile_number_text_input.setHint(getResources().getString(R.string.email_id));
                        email.setBackground(getActivity().getDrawable(R.drawable.rounded_orange));
                        mobile.setBackground(null);

                        /*clear other screen*/
                        dummy_view.requestFocus();
                        email_Id.setText("");
                        password.setText("");
                        if (Helper.isKeyboardVisible(getActivity())) {
                            Helper.dismissKeyboardWithoutFlags(getActivity());
                        }

                        emailIdTextInput.setError(null);
                        emailIdTextInput.setErrorEnabled(false);
                        password_text_input.setError(null);
                        password_text_input.setErrorEnabled(false);
                        mErrorMessage.setVisibility(View.GONE);

                        email_Id.addTextChangedListener(new TextWatcher() {
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
                                    email_Id.setText(result);
                                    email_Id.setSelection(result.length());
                                    // alert the user
                                }
                            }
                        });
                    }
                });
                mobile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mobile_number.setInputType(InputType.TYPE_CLASS_NUMBER);
                        mobile_number_text_input.setHint(getString(R.string.mobile_number));
                        emailIdTextInput.setVisibility(View.GONE);
                        loginIdChoiceEmail = false;
                        email.setBackground(null);
                        mobile.setBackground(getActivity().getDrawable(R.drawable.rounded_orange));

                        /*clear other screen*/
                        dummy_view.requestFocus();
                        password.setText("");
                        mobile_number.setText("");
                        if (Helper.isKeyboardVisible(getActivity())) {
                            Helper.dismissKeyboardWithoutFlags(getActivity());
                        }


                        mobile_number_text_input.setError(null);
                        mobile_number_text_input.setErrorEnabled(false);
                        password_text_input.setError(null);
                        password_text_input.setErrorEnabled(false);
                        mErrorMessage.setVisibility(View.GONE);


                        mobile_number_text_input.setVisibility(View.VISIBLE);
                        mobile.setTextColor(getResources().getColor(R.color.otv_orange));
                        email.setTextColor(getResources().getColor(R.color.text_gray));
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
                    }
                });
                mobile_number.setOnFocusChangeListener((view, b) -> {

                    if (Helper.getCurrentFragment(getActivity()) instanceof RegisterFragment)
                        return;

                    if (b) {
                        new Handler().postDelayed(() -> {
                            if (TextUtils.isEmpty(mobile_number.getText())) {
                                final String prefix = "+91 ";
                                mobile_number.setText(prefix);
                                Selection.setSelection(mobile_number.getText(), mobile_number.getText().length());
                            }
                        }, 200);
                    } else {
                        if (mobile_number.getText().toString().equalsIgnoreCase("+91 "))
                            mobile_number.setText("");
                    }


                    if (b)
                        return;

                    String indianMobileNumber = mobile_number.getText().toString();

                    if (indianMobileNumber.trim().length() == 3) {
                        mobile_number_text_input.setError(getString(R.string.is_field_empty));
                    } else if (indianMobileNumber.trim().length() != 14) {
                        mobile_number_text_input.setError(getString(R.string.please_enter_valid_number));
                    }
                });

                mobile.setVisibility(View.GONE);
                email.setVisibility(View.GONE);

                emailIdTextInput.setVisibility(View.GONE);
                mobile_number_text_input.setVisibility(View.VISIBLE);

                password.setOnFocusChangeListener((view, b) -> {
                    String indianPassword = password.getText().toString();

                    if (b)
                        return;


                    if (TextUtils.isEmpty(indianPassword)) {
                        password_text_input.setError(getString(R.string.is_field_empty));
                    } else if (indianPassword.trim().length() < 6) {
                        password_text_input.setError(getString(R.string.password_short));
                    }
                });


                email_Id.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean b) {

                        if (b)
                            return;

                        String emaiId = email_Id.getText().toString();
                        if (TextUtils.isEmpty(emaiId)) {
                            emailIdTextInput.setError(getString(R.string.is_field_empty));
                        } else if (!Constants.isEmailValied(emaiId)) {
                            emailIdTextInput.setError(getString(R.string.invalid_email_id));
                        }

                    }

                });

            } else {
                relative_layout2.setVisibility(View.GONE);
                email.performClick();
                loginIdChoiceEmail = true;
                emailIdTextInput.setVisibility(View.VISIBLE);
                mobile_number_text_input.setVisibility(View.GONE);
                email_Id.addTextChangedListener(new TextWatcher() {
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
                            email_Id.setText(result);
                            email_Id.setSelection(result.length());
                            // alert the user
                        }
                    }
                });

            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fireScreenView();
        String region = PreferenceHandler.getRegion(getActivity());
        loginRegistrationViewModel = ViewModelProviders.of(this).get(LoginRegistrationViewModel.class);

        if (region.equalsIgnoreCase("IN")) {
            mobile.setVisibility(View.VISIBLE);
            email.setVisibility(View.VISIBLE);
            mobile.performClick();
            loginIdChoiceEmail = false;
            mobile_number.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    mobile_number_text_input.setError(null);
                    mobile_number_text_input.setErrorEnabled(false);

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

            password.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    password_text_input.setError(null);
                    password_text_input.setErrorEnabled(false);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

        } else if (!region.equalsIgnoreCase("IN")) {
            mobile.setVisibility(View.GONE);
            email.setVisibility(View.GONE);
            loginIdChoiceEmail = true;
            email_Id.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    emailIdTextInput.setError(null);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

//            other_contry_password.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                    other_contry_password_text_input.setError(null);
//                    other_contry_password_text_input.setErrorEnabled(false);
//                }
//
//                @Override
//                public void afterTextChanged(Editable editable) {
//
//                }
//            });

        } else {
            Helper.showToast(getActivity(), getResources().getString(R.string.no_country_dec), R.drawable.ic_error_icon);
        }


        Bundle arguments = getArguments();
        if (arguments != null) {
            String fromPage = arguments.getString(Constants.FROM_IN_APP);
            if (!TextUtils.isEmpty(fromPage) && fromPage.equalsIgnoreCase(Constants.FROM_IN_APP)) {
                RegisterFragment fragment = new RegisterFragment();
                Helper.addFragment(getActivity(), fragment, RegisterFragment.TAG);

            }
        }
    }

    private void checkCredentials() {
        String region = Constants.REGION;
        if (!region.equalsIgnoreCase("") && region.equalsIgnoreCase("IN")) {
            if (loginIdChoiceEmail) {
                String emailId = email_Id.getText().toString();
                String indianPassword = password.getText().toString();
                // String otherPassword = other_contry_password.getText().toString();

                if (TextUtils.isEmpty(emailId)) {
                    emailIdTextInput.setError(getString(R.string.invalid_email));
                    return;
                }

                if (!Constants.isEmailValied(emailId)) {
                    emailIdTextInput.setError(getString(R.string.invalid_email));
                    return;
                }
                if (TextUtils.isEmpty(indianPassword)) {
                    password_text_input.setError(getString(R.string.is_field_empty));
                    return;
                }
                fireLoginApi(region);
            } else {
                String indianMobileNumber = mobile_number.getText().toString();
                String indianPassword = password.getText().toString();

                if (indianMobileNumber.trim().length() == 3) {
                    mobile_number_text_input.setError(getString(R.string.is_field_empty));
                    return;
                }

                if (indianMobileNumber.trim().length() != 14) {
                    mobile_number_text_input.setError(getString(R.string.please_enter_valid_number));
                    return;
                }

                if (TextUtils.isEmpty(indianPassword)) {
                    password_text_input.setError(getString(R.string.is_field_empty));
                    return;
                }
                fireLoginApi(region);
            }

        } else if (!region.equalsIgnoreCase("") && !region.equalsIgnoreCase("IN")) {

            String emailId = email_Id.getText().toString();

            if (TextUtils.isEmpty(emailId)) {
                emailIdTextInput.setError(getString(R.string.invalid_email));
                return;
            }

            if (!Constants.isEmailValied(emailId)) {
                emailIdTextInput.setError(getString(R.string.invalid_email));
                return;
            }

            fireLoginApi(region);
        } else {
            // TODO: 09/10/18 Handle this
            Helper.showToast(getActivity(), getResources().getString(R.string.no_country_dec), R.drawable.ic_error_icon);
        }
    }

    private void fireLoginApi(String region) {
        Helper.dismissKeyboard(getActivity());
        login_btn.setEnabled(false);
        SignInRequest request = new SignInRequest();
        final User user = new User();
        request.setAuthToken(Constants.API_KEY);
        if (region.equalsIgnoreCase("IN")) {
            if (loginIdChoiceEmail) {
                String passwordd = password.getText().toString();
                user.setEmailId(email_Id.getText().toString());
                user.setPassword(passwordd);
                //user.setCurrentSignInIp(PreferenceHandler.getIp(getActivity()));
                user.setRegion(region);
                user.setType("email");
                PreferenceHandler.setLoggedInType(getActivity(), "email");
            } else {
                String indianMobileNumber = mobile_number.getText().toString().trim().replace("+", "");
                String indianPassword = password.getText().toString();
                user.setEmailId(indianMobileNumber.replaceAll(" ", ""));
                user.setPassword(indianPassword);
                user.setRegion(region);
                //user.setCurrentSignInIp(PreferenceHandler.getIp(getActivity()));
                user.setType("msisdn");
                PreferenceHandler.setLoggedInType(getActivity(), "msisdn");
            }
        } else if (!region.equalsIgnoreCase("IN")) {
            String passwordd = password.getText().toString();
            user.setEmailId(email_Id.getText().toString());
            user.setPassword(passwordd);
            //user.setCurrentSignInIp(PreferenceHandler.getIp(getActivity()));
            user.setRegion(region);
            user.setType("email");
            PreferenceHandler.setLoggedInType(getActivity(), "email");
        } else {
            // TODO: 09/10/18 Handle this
            Helper.showToast(getActivity(), getResources().getString(R.string.no_country_dec), R.drawable.ic_error_icon);
        }

        user.setDeviceData(new DeviceData(Constants.DEVICE_NAME, Constants.DEVICE_TYPE));
        request.setUser(user);
        Helper.showProgressDialog(getActivity());
        mApiService.login(request)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(loggedInData -> {
                    enableLoginButton();
                    Helper.deleteSearchHistory(getActivity());
                    Helper.dismissKeyboard(getActivity());
                    if ((LoggedInData) loggedInData != null) {
                        continueLogin((LoggedInData) loggedInData);
                    }

                    Helper.dismissProgressDialog();
                }, throwable -> {
                    throwable.printStackTrace();
                    enableLoginButton();
                    Helper.dismissProgressDialog();
                    String errorMessage = Constants.getErrorMessage(throwable).getError().getMessage();
                    mErrorMessage.setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(errorMessage) || !errorMessage.equals(" ")) {
                        mErrorMessage.setText(errorMessage);
                    } else {
                        mErrorMessage.setText("Something went wrong, try after sometime");
                    }
                });


       /* loginRegistrationViewModel.login(request).observe(this, new Observer<Resource>() {
            @Override
            public void onChanged(@Nullable Resource resource) {
                Resource.Status status = resource.status;
                if (status == Resource.Status.SUCCESS) {
                    Helper.deleteSearchHistory(getActivity());
                    Helper.dismissKeyboard(getActivity());
                    LoggedInData object = (LoggedInData) resource.data;
                    if (object != null) {
                        continueLogin(object);
                    }
                    Helper.dismissProgressDialog();
                }
                if (status == Resource.Status.LOADING) {
                    Helper.showProgressDialog(getActivity());
                }
                if (status == Resource.Status.ERROR) {
                    Helper.dismissProgressDialog();
                    String errorMessage = resource.message;
                    mErrorMessage.setVisibility(View.VISIBLE);
                    mErrorMessage.setText(errorMessage);
                }
            }
        });*/
    }

    private void enableLoginButton() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                login_btn.setEnabled(true);
            }
        }, 1000);
    }

    private void getCampaginParameters() {
        boolean loggedIn = PreferenceHandler.isLoggedIn(getActivity());
        if (!loggedIn) {
            return;
        }
        mApiService.getAccountDetails(PreferenceHandler.getSessionId(getActivity()))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ListResonse>() {
                    @Override
                    public void call(ListResonse resonse) {
                        if (resonse != null) {
                            CampaignData campaignData = resonse.getData().getCampaignData();
                            /*if (campaignData != null) {
                                PreferenceHandler.setReferalDetails(getActivity(), campaignData);
                            }*/
                            if (resonse.getData().getFirstname() != null) {
                                PreferenceHandler.setUserName(MyApplication.getInstance(), resonse.getData().getFirstname());
                            }

                            if (resonse.getData() != null && resonse.getData().getEmailId() != null) {
                                PreferenceHandler.setUserEMailId(getActivity(), resonse.getData().getEmailId());
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

    private void checkPlayList() {

        if (PreferenceHandler.isLoggedIn(getActivity())) {

            mApiService.getAllPlaylist(PreferenceHandler.getSessionId(getActivity()), 0, 20).subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<PlayListDataResponse>() {
                @Override
                public void call(PlayListDataResponse playListResponse) {

                    Log.d(TAG, "checkPlayList : " + "");
                    Constants.addToLocalList(playListResponse.getPlayListResponse().getPlaylistItems());
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    //show toast
                    Log.e("ERROR IN GETTING PLAYLIST", "YES");
                }
            });

        }

    }

    private void continueLogin(LoggedInData object) {
        PreferenceHandler.setSessionId(getActivity(), object.getData().getSession());
        PreferenceHandler.setIsLoggedIn(getActivity(), true);
        getCampaginParameters();
        checkPlayList();
        Gson gson = new Gson();
        com.webengage.sdk.android.User user = WebEngage.get().user();
        Log.d("somesessiondata", gson.toJson(object));
//
//        String parentalControl = object.getData().getParentalControl();
//        if (parentalControl.equalsIgnoreCase("TRUE")) {
//            PreferenceHandler.setParentEnabled(getActivity(), true);
//        } else {
//            PreferenceHandler.setParentEnabled(getActivity(), false);
//        }
        //For Payments
        if(object.getData().getLoginType()!=null && object.getData().getLoginType().equalsIgnoreCase("email")){
            appEvents = new AppEvents(1, Constants.EMAIL, "", Constants.AN_APP_TYPE, "", Constants.LINK_LOGIN, Constants.LINK_LOGIN,
                    PreferenceHandler.getUserState(getContext()), PreferenceHandler.getUserPeriod(getContext()), PreferenceHandler.getUserPlanType(getContext()), PreferenceHandler.getUserId(getContext()));
            mAnalyticsEvent.logAppEvents(appEvents);
        }else if(object.getData().getLoginType()!=null && object.getData().getLoginType().equalsIgnoreCase("msisdn")){
            appEvents = new AppEvents(1, Constants.MOBILE, "", Constants.AN_APP_TYPE, "", Constants.LINK_LOGIN, Constants.LINK_LOGIN,
                    PreferenceHandler.getUserState(getContext()), PreferenceHandler.getUserPeriod(getContext()), PreferenceHandler.getUserPlanType(getContext()), PreferenceHandler.getUserId(getContext()));
            mAnalyticsEvent.logAppEvents(appEvents);
        }else{

        }
        Constants.SESSION_ID = object.getData().getSession();
        if (object.getData().getEmailId() != null && !object.getData().getEmailId().equals("")) {
            Constants.USER_LOGIN_ID = object.getData().getEmailId();
            PreferenceHandler.setUserLoginId(getActivity(), object.getData().getEmailId());
            loginEvents = new LoginEvents(object.getData().getEmailId(), "", Constants.EMAIL);
            user.setEmail(object.getData().getEmailId());
            user.setPhoneNumber("");
        } else if (!TextUtils.isEmpty(object.getData().getMobileNumber())) {
            Constants.USER_LOGIN_ID = object.getData().getMobileNumber();
            PreferenceHandler.setUserLoginId(getActivity(), object.getData().getMobileNumber());
            loginEvents = new LoginEvents("", object.getData().getMobileNumber(), Constants.MOBILE);
            user.setPhoneNumber("+" + object.getData().getMobileNumber());
        } else if (!TextUtils.isEmpty(object.getData().getExt_account_email_id())) {
            Constants.USER_LOGIN_ID = object.getData().getExt_account_email_id();
            PreferenceHandler.setUserLoginId(getActivity(), object.getData().getExt_account_email_id());
            loginEvents = new LoginEvents(object.getData().getExt_account_email_id(), "", Constants.GOOGLE);
            user.setEmail(object.getData().getExt_account_email_id());
            user.setPhoneNumber("");
        } else {
            Constants.USER_LOGIN_ID = fbUserId;
            PreferenceHandler.setUserLoginId(getActivity(), fbUserId);
            loginEvents = new LoginEvents("", "", Constants.FACEBOOK);
        }


        String emailId = object.getData().getEmailId() != null ? object.getData().getEmailId() : "";
        String mobileNumber = object.getData().getMobileNumber() != null ? object.getData().getMobileNumber() : "";

        /*    loginEvents = new LoginEvents(emailId,mobileNumber);*/

        webEngageAnalytics.loginEvent(loginEvents);
        if (user != null && object.getData().getUserId() != null) {
            user.login(object.getData().getUserId());
        }

        String analyticsUserId = object.getData().getUserId();
        if (analyticsUserId != null && !TextUtils.isEmpty(analyticsUserId)) {
            PreferenceHandler.setAnalyticsUserId(getActivity(), analyticsUserId);
        }


        // TODO: 04/04/19 show registered device flow
/*        if (object.getData().isDeviceLimitFlag()){
            showDeviceLimitPopUp();
        } else {*/
//        if (parentalControl.equalsIgnoreCase("TRUE")) {
//            WhoIsWatchingDialog dialog = new WhoIsWatchingDialog();
//            Bundle bundle = new Bundle();
//            bundle.putString(Constants.FROM_PAGE, LoginFragment.class.getSimpleName());
//            dialog.setArguments(bundle);
////            dialog.show(getActivity().getSupportFragmentManager(), WhoIsWatchingDialog.TAG);
//            Helper.addFragment(getActivity(), dialog, WhoIsWatchingDialog.TAG);
//
//        } else {
        Intent data = new Intent();
        String text = Constants.ACTION_ME_LOGIN + "";
        data.setData(Uri.parse(text));
        getActivity().setResult(Constants.ACTION_ME_LOGIN, data);
        Bundle arguments = getArguments();
        if (arguments != null) {
            String fromPage = arguments.getString(Constants.FROM_PAGE);
            if (!TextUtils.isEmpty(fromPage)) {

                if (fromPage.equals(SplashScreenActivity.TAG) || fromPage.equals(MePageFragment.TAG)) {
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    getActivity().startActivity(intent);
                    getActivity().finish();
                    return;
                }

                boolean b = fromPage.equalsIgnoreCase(MovieDetailsFragment.TAG) || fromPage.equalsIgnoreCase(ShowDetailsPageFragment.TAG)
                        || fromPage.equalsIgnoreCase(HomePageFragment.TAG)
                        || fromPage.equalsIgnoreCase(MePageFragment.TAG);
                if (b) {
                    getActivity().finish();
                }

                if(fromPage.equalsIgnoreCase(PasswordResetFragment.TAG) || fromPage.equalsIgnoreCase(ForgotPasswordFragment.TAG)){
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    getActivity().startActivity(intent);
                    getActivity().finish();
                }
            } else {
                getActivity().setResult(Constants.ACTION_ME_LOGIN);
                getActivity().finish();
            }
        } else{
            getActivity().finish();

        }
        // }
        //}
    }


    //    Registered device
    private void showDeviceLimitPopUp() {

        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.watch_later_layout);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        GradientTextView cancel = (GradientTextView) dialog.findViewById(R.id.cancel);
        GradientTextView confirm = (GradientTextView) dialog.findViewById(R.id.confirm);
        MyTextView warningMessage = (MyTextView) dialog.findViewById(R.id.warning);
        MyTextView title = (MyTextView) dialog.findViewById(R.id.title);
        title.setVisibility(View.VISIBLE);
        title.setText(R.string.device_limit_reached);
        warningMessage.setText(R.string.device_limit_msg);
        cancel.setText(R.string.cancel);
        confirm.setText(R.string.manage_devices);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Helper.checkAndLogout(getActivity(), mApiService);
                dialog.cancel();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                goToLoginPage();
                dialog.cancel();

                mobile_number.setText("");
                password.setText("");

                RegisteredDevicesWebViewFragment registeredDevicesWebViewFragment = new RegisteredDevicesWebViewFragment();
                Bundle bundle = new Bundle();
                bundle.putString(Constants.FROM_WHERE, LoginFragment.TAG);
                if (getArguments() != null)
                    bundle.putString(Constants.FROM_PAGE, getArguments().getString(Constants.FROM_PAGE));
                registeredDevicesWebViewFragment.setArguments(bundle);
                Helper.addFragmentForDetailsScreen(getActivity(), registeredDevicesWebViewFragment, RegisteredDevicesWebViewFragment.TAG);

            }
        });

    }


    @OnClick({R.id.back, R.id.close, R.id.login_btn, R.id.register, R.id.forgot_pwd})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.back) {
            Helper.removeCurrentFragment(getActivity(), TAG);
            getActivity().finish();
        } else if (id == R.id.back) {
            Helper.removeCurrentFragment(getActivity(), TAG);
            getActivity().finish();
        } else if (id == R.id.login_btn) {
            checkCredentials();
        } else if (id == R.id.register) {
            Helper.dismissKeyboard(getActivity());
            RegisterFragment fragment = new RegisterFragment();
            Bundle bundle = new Bundle();
            try {
                bundle.putString(Constants.FROM_PAGE, getArguments().getString(Constants.FROM_PAGE));
            } catch (Exception ex) {
                ex.printStackTrace();
                bundle.putString(Constants.FROM_PAGE, LoginFragment.TAG);
            }
            fragment.setArguments(bundle);
            Helper.addFragment(getActivity(), fragment, RegisterFragment.TAG);
        } else if (id == R.id.forgot_pwd) {
            if (mAnalytics != null) {
                //mAnalytics.branchForgotPassword(getActivity());
            }
            ForgotPasswordFragment fragment = new ForgotPasswordFragment();
            Helper.addFragment(getActivity(), fragment, ForgotPasswordFragment.TAG);
        }
    }


    private void showForgotPasswordDialog() {
        ForgotPasswordFragment fragment = new ForgotPasswordFragment();
        Helper.addFragment(getActivity(), fragment, ForgotPasswordFragment.TAG);
    }

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
        mAnalytics.fireScreenView(AnalyticsProvider.Screens.Login);
        pageEvents = new PageEvents(Constants.LoginScreen);
        webEngageAnalytics.screenViewedEvent(pageEvents);
    }


    public void logout() {
        if (mApiService != null && getActivity() != null)
            Helper.checkAndLogout(getActivity(), mApiService);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            account = completedTask.getResult(ApiException.class);
            if (account != null) {
                Helper.showToast(getActivity(), "SignIn successful", R.drawable.ic_check);
                sendGoogleDetailsToServer("google");
                //  Log.d("GoogleLoginAppevent",appEvents.toString());
            }
            // Signed in successfully, show authenticated UI.
            // updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            Helper.showToast(getActivity(), "signInResult:failed code=" + e.getStatusCode(), R.drawable.ic_error_icon);

            //  updateUI(null);
        }
    }

    private void setUserPreferenceHandler() {
        mApiService.account(PreferenceHandler.getSessionId(getActivity()))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<PlayListResponse>() {
                    @Override
                    public void call(PlayListResponse playListResponse) {
                        if (playListResponse != null && playListResponse.getPlayListResponse() != null && playListResponse.getPlayListResponse().getUserInfo() != null) {
                            //Data data =  playListResponse.getPlayListResponse();
                            UserInfo userInfo = playListResponse.getPlayListResponse().getUserInfo();
                            if (playListResponse.getPlayListResponse().getUserInfo().getUserPackName() != null) {
                                PreferenceHandler.setUserPackName(getContext(), playListResponse.getPlayListResponse().getUserInfo().getUserPackName());
                                Log.d("UserData PackName", PreferenceHandler.getPackName(getContext()));
                            }
                            if (playListResponse.getPlayListResponse().getUserId() != null) {
                                PreferenceHandler.setUserLoginId(getContext(), playListResponse.getPlayListResponse().getUserId());
                            }
                            if (playListResponse.getPlayListResponse().getUserInfo().getUserPlanType() != null) {
                                PreferenceHandler.setUserPlanType(getContext(), playListResponse.getPlayListResponse().getUserInfo().getUserPlanType());
                                Log.d("UserData PlanType", PreferenceHandler.getPackName(getContext()));
                            }
                            if (playListResponse.getPlayListResponse().getUserInfo().getUserState() != null) {
                                PreferenceHandler.setUserState(getContext(), playListResponse.getPlayListResponse().getUserInfo().getUserState());
                                Log.d("UserData UserState", PreferenceHandler.getPackName(getContext()));
                            }
                            if (playListResponse.getPlayListResponse().getUserInfo().getUserPeriod() != null) {
                                PreferenceHandler.setUserPeriod(getContext(), playListResponse.getPlayListResponse().getUserInfo().getUserPeriod());
                                Log.d("UserData UserPeriod", PreferenceHandler.getPackName(getContext()));
                            }
                            if (playListResponse.getPlayListResponse().getUserInfo().getAnalyticsUserId() != null) {
                                PreferenceHandler.setAnalyticsUserId(getContext(), playListResponse.getPlayListResponse().getUserInfo().getAnalyticsUserId());
                                Log.d("UserData AnalyticsUserId", PreferenceHandler.getPackName(getContext()));
                            }
                            Log.e("onsetUserPreferenceHandler", "success");
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Helper.dismissProgressDialog();
                        Log.e("onsetUserPreferenceHandler", "failure");
                        throwable.printStackTrace();
                    }
                });

    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener((Executor) this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
    }
}
