package com.otl.tarangplus.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.JsonObject;
import com.otl.tarangplus.Analytics.Analytics;
import com.otl.tarangplus.Analytics.AnalyticsProvider;
import com.otl.tarangplus.Analytics.WebEngageAnalytics;
import com.otl.tarangplus.MainActivity;
import com.otl.tarangplus.MyApplication;
import com.otl.tarangplus.customeUI.GradientTextView;
import com.otl.tarangplus.customeUI.MyTextView;
import com.otl.tarangplus.model.AppEvents;
import com.otl.tarangplus.model.CampaginThings;
import com.otl.tarangplus.model.CampaignData;
import com.otl.tarangplus.model.DeviceData;
import com.otl.tarangplus.model.LoggedInData;
import com.otl.tarangplus.model.PlayListDataResponse;
import com.otl.tarangplus.model.Profile;
import com.otl.tarangplus.model.ProfileData;
import com.otl.tarangplus.model.SignInRequest;
import com.otl.tarangplus.model.UpdateProfileRequest;
import com.otl.tarangplus.model.User;
import com.otl.tarangplus.model.UserEvents;
import com.poovam.pinedittextfield.LinePinField;
import com.otl.tarangplus.Database.LayoutDbScheme;
import com.otl.tarangplus.R;
import com.otl.tarangplus.Utils.Constants;
import com.otl.tarangplus.Utils.Helper;
import com.otl.tarangplus.Utils.KeyboardUtils;
import com.otl.tarangplus.Utils.PreferenceHandler;
import com.otl.tarangplus.rest.ApiService;
import com.otl.tarangplus.rest.RestClient;
import com.squareup.picasso.Picasso;
import com.webengage.sdk.android.WebEngage;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class OtpScreen extends Fragment {

    private ViewHolder viewHolder;
    public static String TAG = OtpScreen.class.getSimpleName();
    String userId;
    String type;
    private static final int SMS_READ_REQUEST = 101;
    private static final int SMS_RECIEVE_REQUEST = 102;
    CountDownTimer countDownTimer;
    private ApiService mApiService;
    String mOtpEnterd;
    AnalyticsProvider analyticsProvider;
    WebEngageAnalytics webEngageAnalytics;
    private AppEvents appEvents;
    private Analytics mAnalyticsEvent ;
    UserEvents userEvents;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.otp_verification_layout, container, false);
        viewHolder = new ViewHolder(view);
        mApiService = new RestClient(getContext()).getApiService();
        viewHolder.header.setText(getString(R.string.otp_verification));
        viewHolder.back.setVisibility(View.GONE);
        setOnClickListnersForViews(getActivity());
        setTopbarUI(Constants.getSchemeColor("All"));
        analyticsProvider = new AnalyticsProvider(getActivity());
        mAnalyticsEvent = Analytics.getInstance(getContext());
        webEngageAnalytics = new WebEngageAnalytics();
        countDownTimer = new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {

                long seconds = millisUntilFinished / 1000;
                if (seconds < 10) {
                    viewHolder.mTimer.setText("00:" + "0" + seconds);
                } else {
                    viewHolder.mTimer.setText("00:" + seconds);
                }
            }

            public void onFinish() {
                viewHolder.mTimer.setText("");
                viewHolder.mResend.setVisibility(View.VISIBLE);
            }
        }.start();

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
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, new IntentFilter("otp"));
        return view;
    }

    private void setOnClickListnersForViews(Activity activity) {

        viewHolder.back.setOnClickListener(v -> {
            if (getActivity() != null) {
                if (getActivity().getSupportFragmentManager() != null) {
                    getActivity().getSupportFragmentManager().popBackStackImmediate();

                } else if (getActivity().getFragmentManager() != null) {
                    getActivity().getSupportFragmentManager().popBackStackImmediate();
                }
            }
            /* getActivity().onBackPressed();*/
        });

        viewHolder.close.setOnClickListener(v -> {
            if (getActivity() != null) {
                if (getActivity().getSupportFragmentManager() != null) {
                    getActivity().getSupportFragmentManager().popBackStackImmediate();

                } else if (getActivity().getFragmentManager() != null) {
                    getActivity().getSupportFragmentManager().popBackStackImmediate();
                }
            }
            /*getActivity().onBackPressed();*/
        });
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && !TextUtils.isEmpty(intent.getStringExtra("message"))) {
                String message = intent.getStringExtra("message");
                mOtpEnterd = message;
                viewHolder.mOtpInputText.setText(message);

            }
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiver);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private boolean checkAndRequestPermissions() {
//        boolean isSmsPermissionReadGradnted = PermissionsRequester.requestSMSPermissionIfNeeded(getActivity(), SMS_READ_REQUEST);
//        boolean b = PermissionsRequester.requestSMSRecieverPermission(getActivity(), SMS_RECIEVE_REQUEST);
//        return isSmsPermissionReadGradnted && b;
        return false;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        if (getArguments() != null) {
            userId = getArguments().getString(Constants.USER_ID);
            type = getArguments().getString(Constants.TYPE);
        }


        viewHolder.mResend.setOnClickListener(view1 -> {

            countDownTimer.start();
//                viewHolder.mResend.setVisibility(View.GONE); commented as clients wants it to be present all the time.
            SignInRequest request = new SignInRequest();
            request.setAuthToken(Constants.API_KEY);
            User user = new User();
            user.setEmailId(userId);
            user.setType(type);
            request.setUser(user);
            Helper.showProgressDialog(getActivity());
            mApiService.resendVerificationCode(request)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<JsonObject>() {
                        @Override
                        public void call(JsonObject jsonObject) {

                            Helper.dismissProgressDialog();
                            Helper.showToast(getActivity(), getString(R.string.enter_otp_sent_to_mobile_resend), R.drawable.ic_cross);
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            throwable.printStackTrace();
                            Helper.dismissProgressDialog();
                            viewHolder.mErrorMessage.setVisibility(View.VISIBLE);
                        }
                    });
        });

        viewHolder.mOtpInputText.setOnTextCompleteListener(enteredText -> {

            mOtpEnterd = enteredText;
            return true;
        });


        viewHolder.verify.setOnClickListener(view1 -> {

           /* if(TextUtils.isEmpty(pin1) || TextUtils.isEmpty(pin2) || TextUtils.isEmpty(pin3)|| TextUtils.isEmpty(pin4)) {
                Helper.showToast(getActivity(),"Please enter valid OTP",R.drawable.ic_error_icon);
            }*/
            String finalFin = mOtpEnterd;

            if (viewHolder.mOtpInputText != null && viewHolder.mOtpInputText.getText() != null) {
                finalFin = viewHolder.mOtpInputText.getText().toString();
            }
            if (TextUtils.isEmpty(finalFin)) {
                Helper.showToast(getActivity(), getString(R.string.pin_empty), R.drawable.ic_cross);
                return;
            }
            if (finalFin.length() < 4) {

                Helper.showToast(getActivity(), getString(R.string.wrong_pin), R.drawable.ic_cross);
                return;
            }

            String from = getArguments().getString(Constants.FROM_PAGE);
            String mobileNum = getArguments().getString(Constants.USER_ID);

            if (!TextUtils.isEmpty(from) && from.equalsIgnoreCase(ForgotPasswordFragment.TAG)) {
                checkOtpAgainstValidation(finalFin);
            } else {
                String finalFin1 = finalFin;
                mApiService.checkOtp(finalFin, type, /*Constants.DEVICE_TYPE, Constants.DEVICE_NAME,*/ mobileNum)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<LoggedInData>() {
                            @Override
                            public void call(LoggedInData loggedInData) {
                                Helper.dismissProgressDialog();
                                com.webengage.sdk.android.User user = WebEngage.get().user();
                                PreferenceHandler.setIsLoggedIn(getActivity(), true);
                                PreferenceHandler.setTextForPackDetails(getActivity(), loggedInData.getWelcomeMsg());
                                if (loggedInData.getFreeTrail() != null)
                                    PreferenceHandler.setIsFreeTrail(getActivity(), loggedInData.getFreeTrail());
                                else
                                    PreferenceHandler.setIsFreeTrail(getActivity(), false);

                                try {
                                    PreferenceHandler.setSessionId(getActivity(), loggedInData.getData().getMessages().get(0).getSession());
                                    checkPlayList();
                                    PreferenceHandler.setIsLoggedIn(getActivity(), true);
                                    if (loggedInData.getData() != null && !TextUtils.isEmpty(loggedInData.getData().getSessionId())) {
                                        PreferenceHandler.setSessionId(getActivity(), loggedInData.getData().getSessionId());
                                        checkPlayList();
                                    }
                                    if (loggedInData.getData().getEmailId() != null && !loggedInData.getData().getEmailId().equals("")) {
                                        Constants.USER_LOGIN_ID = loggedInData.getData().getEmailId();
                                        user.setEmail(Constants.USER_LOGIN_ID);
                                        PreferenceHandler.setUserLoginId(getActivity(), loggedInData.getData().getEmailId());
                                    } else {
                                        Constants.USER_LOGIN_ID = loggedInData.getData().getMobileNumber();
                                        user.setPhoneNumber("+" + Constants.USER_LOGIN_ID);
                                        PreferenceHandler.setUserLoginId(getActivity(), loggedInData.getData().getMobileNumber());
                                    }


                                    String analyticsUserId = loggedInData.getData().getUserId();
                                    if (analyticsUserId != null && !TextUtils.isEmpty(analyticsUserId)) {
                                        PreferenceHandler.setAnalyticsUserId(getActivity(), analyticsUserId);
                                        // Branch.getInstance().setIdentity(PreferenceHandler.getAnalyticsUserId(getActivity()));
                                        analyticsProvider.updateUserId(analyticsUserId, from, getActivity());
                                    }
                                    analyticsProvider.completeRegestration(analyticsUserId, "IN");
                                    //analyticsProvider.branchSignUpOtpSuccess(getActivity());

                                } catch (Exception e) {

                                }
                                if (!PreferenceHandler.getDataSendToOTT(getActivity())) {
                                    PreferenceHandler.setDataSendToOTT(getActivity(), true);
                                    try {
                                        if (PreferenceHandler.getReferalDetails(getActivity()) != null) {
                                            CampaignData referalDetails = PreferenceHandler.getReferalDetails(getActivity());
                                            if (referalDetails != null) {
                                                sendCampaginParametersToOTT(referalDetails);
                                            }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                appEvents = new AppEvents(1, Constants.MOBILE, "" ,Constants.AN_APP_TYPE,"",Constants.LINK_REGISTER,"",
                                        "", PreferenceHandler.getUserPeriod(getContext()),PreferenceHandler.getUserPlanType(getContext()),PreferenceHandler.getUserId(getContext()));
                                mAnalyticsEvent.logAppEvents(appEvents);
                                Helper.showToast(getActivity(), getActivity().getResources().getString(R.string.otp_verified), R.drawable.ic_cross);
                                PreferenceHandler.setLoggedInType(getActivity(), type);
                                userEvents = new UserEvents(PreferenceHandler.getUserName((MyApplication.getInstance())), "+" + Constants.USER_LOGIN_ID, "", type);
                                webEngageAnalytics.registeredEvent(userEvents);


                                if (user != null && loggedInData.getData().getUserId() != null) {
                                    user.login(loggedInData.getData().getUserId());
                                }

                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);

                                //getProfiles();

                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                throwable.printStackTrace();

                                String errorMessage = Constants.getErrorMessage(throwable).getError().getMessage();
                                Helper.showToast(getActivity(), errorMessage, R.drawable.ic_cross);
                                Helper.dismissProgressDialog();
                                //analyticsProvider.branchSignUpOtpFailure(finalFin1,mobileNum,getActivity());
                            }
                        });

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

    private void checkOtpAgainstValidation(final String finalFin) {
        SignInRequest request = new SignInRequest();
        //{"auth_token":"tzxN848PaefLW9NA7iDM", "user": {"key":"256321","region":"IN"}}
        request.setAuthToken(Constants.API_KEY);
        User user = new User();
        user.setKey(finalFin);
        user.setRegion(PreferenceHandler.getRegion(getActivity()));
        user.setMobileNumber(userId);
        user.setDeviceData(new DeviceData(Constants.DEVICE_NAME, Constants.DEVICE_TYPE));
        request.setUser(user);
        Helper.showProgressDialog(getActivity());
        mApiService.checkOtpWithPostCall(request)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject jsonObject) {
                        Helper.dismissProgressDialog();
                        PasswordResetFragment fragment = new PasswordResetFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString(Constants.OTP, finalFin);
                        fragment.setArguments(bundle);
                        Helper.addFragment(getActivity(), fragment, PasswordResetFragment.TAG);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        Helper.showToast(getActivity(), Constants.getErrorMessage(throwable).getError().getMessage(), R.drawable.ic_cross);
                        Helper.dismissProgressDialog();
                    }
                });
    }


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

                        SubscriptionWebViewFragment subscriptionWebViewFragment = new SubscriptionWebViewFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString(Constants.WHO_INITIATED_THIS, TAG);
                        bundle.putString(Constants.FROM_WHERE, MePageFragment.TAG);
                        bundle.putBoolean(Constants.IS_LOGGED_IN, true);
                        bundle.putString(Constants.TYPE, type);
                        bundle.putString(Constants.EXTRA_LINK, "&new_user_st=true");
                        subscriptionWebViewFragment.setArguments(bundle);
                        Helper.addFragmentForDetailsScreen(getActivity(), subscriptionWebViewFragment, SubscriptionWebViewFragment.TAG);
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


    static class ViewHolder {
        @BindView(R.id.back)
        ImageView back;
        @BindView(R.id.header)
        MyTextView header;
        @BindView(R.id.close)
        AppCompatImageView close;
        @BindView(R.id.toolbar)
        Toolbar toolbar;
        @BindView(R.id.app_bar_layout)
        AppBarLayout app_bar_layout;
        @BindView(R.id.text_otp)
        MyTextView text_otp;
        @BindView(R.id.four_digit_container)
        LinearLayout four_digit_container;
        @BindView(R.id.text_did_recieve)
        MyTextView text_did_recieve;
        @BindView(R.id.resend)
        MyTextView mResend;
        @BindView(R.id.verify)
        GradientTextView verify;
        @BindView(R.id.error_message)
        MyTextView mErrorMessage;
        @BindView(R.id.timer)
        MyTextView mTimer;
        @BindView(R.id.parent_view)
        RelativeLayout mParentView;
        @BindView(R.id.otp_text_input)
        public LinePinField mOtpInputText;

        //        Top Bar UI
        @BindView(R.id.category_back_img)
        ImageView mTopbarImage;
        @BindView(R.id.category_grad_back)
        TextView mGradientBackground;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);

            mParentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Just to avoid background click
                }
            });


        }
    }

    private void setTopbarUI(LayoutDbScheme layoutDbScheme) {
        if (layoutDbScheme != null) {
            if (!TextUtils.isEmpty(layoutDbScheme.getImageUrl())) {
                Picasso.get().load(layoutDbScheme.getImageUrl()).into(viewHolder.mTopbarImage);
                viewHolder.mGradientBackground.setBackground(Constants.getbackgroundGradient(layoutDbScheme));
            } else {
                Picasso.get().load(R.color.white).into(viewHolder.mTopbarImage);
                viewHolder.mGradientBackground.setBackground(Constants.getbackgroundGradient(layoutDbScheme));
            }
        }
    }

    public void sendCampaginParametersToOTT(CampaignData campaignData) {
        CampaginThings campaginThings = new CampaginThings();
        campaginThings.setAuthToken(Constants.API_KEY);
        campaginThings.setCampaignData(campaignData);
        if (PreferenceHandler.getSessionId(getActivity()) != null)
            mApiService.campaginThings(PreferenceHandler.getSessionId(getActivity()), campaginThings).subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<JsonObject>() {
                @Override
                public void call(JsonObject jsonObject) {

                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {

                }
            });
    }
}
