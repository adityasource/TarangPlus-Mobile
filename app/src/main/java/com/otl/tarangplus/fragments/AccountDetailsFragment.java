package com.otl.tarangplus.fragments;

import android.app.Dialog;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.otl.tarangplus.Analytics.Analytics;
import com.otl.tarangplus.Analytics.AnalyticsProvider;
import com.otl.tarangplus.Analytics.WebEngageAnalytics;
import com.otl.tarangplus.MainActivity;
import com.otl.tarangplus.R;
import com.otl.tarangplus.Utils.Constants;
import com.otl.tarangplus.Utils.Helper;
import com.otl.tarangplus.Utils.PreferenceHandler;
import com.otl.tarangplus.customeUI.GradientTextView;
import com.otl.tarangplus.customeUI.MyEditText;
import com.otl.tarangplus.customeUI.MyTextView;
import com.otl.tarangplus.model.AppEvents;
import com.otl.tarangplus.model.Data;
import com.otl.tarangplus.model.DataError;
import com.otl.tarangplus.model.ListResonse;
import com.otl.tarangplus.model.PageEvents;
import com.otl.tarangplus.rest.RestClient;
import com.otl.tarangplus.rest.SignOutDetails;
import com.otl.tarangplus.viewModel.AccountDetailsViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class AccountDetailsFragment extends Fragment {
    View mRootView;
    public static final String TAG = AccountDetailsFragment.class.getClass().getName();
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
    @BindView(R.id.update_link)
    RelativeLayout mUpdateLink;
    @BindView(R.id.name_label)
    MyTextView mNameLabel;
    @BindView(R.id.name)
    MyTextView mName;
    @BindView(R.id.mobile_label)
    MyTextView mMobileLabel;
    @BindView(R.id.mobile)
    MyTextView mMobile;
    @BindView(R.id.country)
    MyTextView mcountry;
    @BindView(R.id.state_id)
    MyTextView mstate_id;
    @BindView(R.id.email_id_label)
    MyTextView mEmailIdLabel;
    @BindView(R.id.email_id)
    MyTextView mEmailId;
    @BindView(R.id.birth_date_label)
    MyTextView mBirthDateLabel;
    @BindView(R.id.birth_date)
    MyTextView mBirthDate;
    @BindView(R.id.other_details)
    LinearLayout mOtherDetails;
    @BindView(R.id.security_details)
    RelativeLayout mSecurityDetails;
    @BindView(R.id.password)
    MyEditText mPassword;
    @BindView(R.id.password_layout)
    LinearLayout mPasswordLayout;
    @BindView(R.id.change)
    GradientTextView mChange;
    @BindView(R.id.password_label)
    MyTextView mPasswordLabel;
    @BindView(R.id.log_out)
    GradientTextView mLogOut;
    @BindView(R.id.address_id)
    MyTextView addressId;


    private AccountDetailsViewModel mAccountsViewModels;
    private Bundle mBundle;
    private RestClient mApiServices;
    private Dialog dialog;

    WebEngageAnalytics webEngageAnalytics;
    PageEvents pageEvents;

    private AppEvents appEvents;
    private Analytics mAnalyticsEvent;

    public AccountDetailsFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_account_details, container, false);
        mAccountsViewModels = ViewModelProviders.of(this).get(AccountDetailsViewModel.class);
        mApiServices = new RestClient(getActivity());
        webEngageAnalytics = new WebEngageAnalytics();

        ButterKnife.bind(this, mRootView);
        mClose.setVisibility(View.GONE);
        mName.setClickable(false);
        mName.setFocusable(false);
        mMobile.setFocusable(false);
        mMobileLabel.setClickable(false);
        mEmailId.setFocusable(false);
        mEmailId.setClickable(false);
        mBirthDate.setFocusable(false);
        mBirthDate.setClickable(false);
        mPassword.setFocusable(false);
        mPassword.setClickable(false);
        mBundle = new Bundle();
        mAnalyticsEvent = Analytics.getInstance(getContext());
        appEvents = new AppEvents(1, "Account Details Screen", "", Constants.AN_APP_TYPE, "", Constants.PAGE_VISIT, "Account Details Screen",
                "", PreferenceHandler.getUserPeriod(getContext()), PreferenceHandler.getUserPlanType(getContext()), PreferenceHandler.getUserId(getContext()));
        mAnalyticsEvent.logAppEvents(appEvents);

        return mRootView;
    }


    @OnClick({R.id.log_out, R.id.back, R.id.close, R.id.name, R.id.email_id, R.id.birth_date, R.id.change, R.id.update_text})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.back:
                if (getActivity().getSupportFragmentManager() != null) {
                    getActivity().getSupportFragmentManager().popBackStackImmediate();

                } else if (getActivity().getFragmentManager() != null) {
                    getActivity().getSupportFragmentManager().popBackStackImmediate();
                }
                break;
            case R.id.close:
                break;
            case R.id.name:
                break;
            case R.id.email_id:
                break;
            case R.id.birth_date:
                break;
            case R.id.update_text:
                UpdatePersonalFragment updatePersonalFragment = new UpdatePersonalFragment();
                if (mBundle != null) {
                    updatePersonalFragment.setArguments(mBundle);
                }
                Helper.addFragment(getActivity(), updatePersonalFragment, UpdatePersonalFragment.TAG);
                break;
            case R.id.change:
                ChangePasswordFragment changePasswordFragment = new ChangePasswordFragment();
                Helper.addFragment(getActivity(), changePasswordFragment, ChangePasswordFragment.TAG);
                break;
            case R.id.log_out:
               /* PreferenceHandler.clearAll(getActivity());
                setDataAccordingly();*/
                showLoginConfirmationPopUp();
                //Branch.getInstance().logout();
                break;
        }
    }

    private void showLoginConfirmationPopUp() {

        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.watch_later_layout);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        GradientTextView cancel = (GradientTextView) dialog.findViewById(R.id.cancel);
        GradientTextView confirm = (GradientTextView) dialog.findViewById(R.id.confirm);
        MyTextView warningMessage = (MyTextView) dialog.findViewById(R.id.warning);
        MyTextView title = (MyTextView) dialog.findViewById(R.id.title);
        title.setVisibility(View.VISIBLE);
        title.setText("Logout");
        warningMessage.setText("Are you sure you want to logout?");
        cancel.setText("No");
        confirm.setText("Yes");

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Helper.deleteSearchHistory(getActivity());
                checkAndLogout();
                dialog.cancel();
            }
        });

    }

    private void checkAndLogout() {
        Helper.showProgressDialog(getActivity());
        SignOutDetails outDetails = new SignOutDetails();
        outDetails.setAuthToken(Constants.API_KEY);
        String sessionId = PreferenceHandler.getSessionId(getActivity());

        mApiServices.getApiService().signOut(sessionId, outDetails)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject object) {
                        if (object != null) {
                            Helper.updateCleverTapDetails(getActivity());
                            JsonElement element = object.get("message");
                            String msg = element.getAsString();
                            Helper.showToast(getActivity(), msg, R.drawable.ic_check);
                            Helper.clearLoginDetails(getActivity());

//                            setDataAccordingly();
                            try {
                                LoginManager.getInstance().logOut();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            getActivity().finish();

                            AnalyticsProvider provider = new AnalyticsProvider(getActivity());
                            String analyticsUserId = PreferenceHandler.getAnalyticsUserId(getActivity());
                            provider.resetUserId(analyticsUserId, getActivity());
                            // TODO: 22/01/19
                        }

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Helper.dismissProgressDialog();
                        throwable.printStackTrace();
                        Helper.clearLoginDetails(getActivity());

//                        setDataAccordingly();
//                        ViewModelProviders.of(getActivity()).get(SearchPageViewModel.class).deleteAllSearHistory();
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });
    }


    @Override
    public void onResume() {
        super.onResume();
        String sessionId = PreferenceHandler.getSessionId(getActivity());

        Helper.showProgressDialog(getActivity());
        mApiServices.getApiService().getAccountDetails(sessionId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ListResonse>() {
                    @Override
                    public void call(ListResonse resonse) {

                        if (resonse != null) {
                            Helper.dismissProgressDialog();
                            updateDetails(resonse);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Helper.dismissProgressDialog();
                        throwable.printStackTrace();
                        DataError errorRes = Constants.getErrorMessage(throwable);
                        String errorMessage = errorRes.getError().getMessage();
                        int errorCode = errorRes.getError().getCode();

                        if (getActivity() != null)
                            if (errorCode == 1016 && ((HttpException) throwable).code() == 422) {
                                Helper.clearLoginDetails(getActivity());
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(new Intent(intent));
                                getActivity().finish();
                                Helper.showToast(getActivity(), errorMessage, R.drawable.ic_error_icon);
                                Helper.deleteSearchHistory(getActivity());
                            }
                    }
                });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mHeader.setText(getResources().getString(R.string.account_details));
        String strFB = PreferenceHandler.getFacebook(getActivity());
        if (!TextUtils.isEmpty(strFB) && strFB.equals("facebook")) {
            mSecurityDetails.setVisibility(View.GONE);
            mPasswordLayout.setVisibility(View.GONE);
        }

        fireScreenView();
    }

    private void updateDetails(ListResonse resonse) {

        Data data = resonse.getData();
        if (data != null) {

            boolean subscribed = data.isSubscribed();
            PreferenceHandler.setIsSubscribed(getActivity(), subscribed);

            if (data.getActivePlans() != null) {
                List<String> activePlans = data.getActivePlans();
                PreferenceHandler.udpateActivePlans(activePlans);
            }
            if (data.getInActivePlans() != null) {
                List<String> inActivePlans = data.getInActivePlans();
                PreferenceHandler.udpateInActivePlans(inActivePlans);
            }

            String firstname = data.getFirstname() + data.getLastname();

            if (firstname != null && !TextUtils.isEmpty(firstname)) {
                mName.setText(firstname);
                mBundle.putString(Constants.PROFILE_NAME, firstname);
            } else {
                mName.setText("-");
                mBundle.putString(Constants.PROFILE_NAME, "-");
            }

            String mobileNumber = data.getMobileNumber();
            if (mobileNumber != null && !TextUtils.isEmpty(mobileNumber)) {
                mMobile.setText(mobileNumber);
                mBundle.putString(Constants.MOBILE, mobileNumber);
            } else {
                mMobile.setText("-");
                mBundle.putString(Constants.MOBILE, "-");
            }

            String address = data.getAddress();
            if (address != null && !TextUtils.isEmpty(address)) {
                addressId.setText(address);
                mBundle.putString(Constants.ADDRESS, address);
            } else {
                addressId.setText("-");
                mBundle.putString(Constants.ADDRESS, address);
            }

            String country = data.getCountry();
            if (!TextUtils.isEmpty(country)) {
                mcountry.setText(country);
                mBundle.putString(Constants.COUNTRY_KEY, country);
            } else {
                mcountry.setText(Constants.COUNTRY);
                mBundle.putString(Constants.COUNTRY_KEY, Constants.COUNTRY);
            }

            String state = data.getState();
            if (!TextUtils.isEmpty(state)) {
                mstate_id.setText(state);
                mBundle.putString(Constants.STATE_KEY, state);
            } else {
                mstate_id.setText(Constants.STATE);
                mBundle.putString(Constants.STATE_KEY, Constants.STATE);
            }

            String emailId = data.getUserEmialId();
            if (TextUtils.isEmpty(emailId)) {
                emailId = data.getExt_account_email_id();
                if (!TextUtils.isEmpty(emailId)) {
                    mEmailId.setText(emailId);
                    PreferenceHandler.setUserEMailId(getActivity(), emailId);
                } else
                    mEmailId.setText("-");
            } else {
                mEmailId.setText(emailId);
                PreferenceHandler.setUserEMailId(getActivity(), emailId);
            }
            String loginType = data.getLoginType();
            if (!TextUtils.isEmpty(loginType) && loginType.equalsIgnoreCase("MSISDN")) {
                mobileNumber = data.getPrimaryId();
                if (!TextUtils.isEmpty(mobileNumber)) {
                    mMobile.setText(mobileNumber);
                    mBundle.putString(Constants.MOBILE, mobileNumber);
                } else {
                    mMobile.setText("-");
                }

                emailId = data.getUserEmialId();
                if (!TextUtils.isEmpty(emailId))
                    mBundle.putString(Constants.USERMAIL, emailId);
                else
                    mBundle.putString(Constants.USERMAIL, data.getExt_account_email_id());

                if (TextUtils.isEmpty(emailId)) {
                    emailId = data.getExt_account_email_id();
                    if (!TextUtils.isEmpty(emailId)) {
                        mEmailId.setText(emailId);
                        PreferenceHandler.setUserEMailId(getActivity(), emailId);
                    } else {
                        mEmailId.setText("-");
                    }
                } else {
                    mEmailId.setText(emailId);
                    PreferenceHandler.setUserEMailId(getActivity(), emailId);
                }
            } else if (!TextUtils.isEmpty(loginType) && (loginType.equalsIgnoreCase("facebook") ||
                    loginType.equalsIgnoreCase("external"))) {
                emailId = data.getUserEmialId();
                if (!TextUtils.isEmpty(emailId))
                    mBundle.putString(Constants.USERMAIL, emailId);
                else
                    mBundle.putString(Constants.USERMAIL, data.getExt_account_email_id());

                if (TextUtils.isEmpty(emailId)) {
                    emailId = data.getExt_account_email_id();
                    if (!TextUtils.isEmpty(emailId)) {
                        mEmailId.setText(emailId);
                        PreferenceHandler.setUserEMailId(getActivity(), emailId);
                    } else {
                        mEmailId.setText("-");
                    }
                } else {
                    mEmailId.setText(emailId);
                    PreferenceHandler.setUserEMailId(getActivity(), emailId);
                }

            } else {
                emailId = data.getPrimaryId();
                if (!TextUtils.isEmpty(emailId))
                    mBundle.putString(Constants.USERMAIL, emailId);
                else
                    mBundle.putString(Constants.USERMAIL, data.getExt_account_email_id());
                mEmailId.setText(emailId);
                PreferenceHandler.setUserEMailId(getActivity(), emailId);
            }

            String dob = data.getBirthDate();
            if (dob != null && !TextUtils.isEmpty(dob)) {
                //"birthdate": "2018-06-19"
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                try {
                    Date parse = format.parse(dob);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    String date = dateFormat.format(parse);
                    mBirthDate.setText(date);
                    mBundle.putString(Constants.DOB, date);
                } catch (ParseException e) {
                    e.printStackTrace();
                    mBirthDate.setText("-");
                    mBundle.putString(Constants.DOB, "-");
                }

            } else {
                mBirthDate.setText("-");
                mBundle.putString(Constants.DOB, "-");
            }
            /**
             * Yet to add password. Cannot get it from data.
             */

            String region = data.getLoginType();
            if (!region.equalsIgnoreCase("MSISDN")) {
                mEmailId.setClickable(false);
                mEmailId.setFocusable(false);
                mMobile.setFocusable(true);
                mMobile.setClickable(true);
            } else {
                mEmailId.setClickable(true);
                mEmailId.setFocusable(true);
                mMobile.setFocusable(false);
                mMobile.setClickable(false);
            }
        }
    }

    public Dialog getLogoutPopUp() {
        return dialog;
    }

    private void updateCleverTapDetails(Context mContext) {
//        if (mContext != null) {
//            HashMap<String, Object> profileUpdate = new HashMap<String, Object>();
//            // String
//            if (PreferenceHandler.isLoggedIn(mContext)) {
//                profileUpdate.put("Identity", PreferenceHandler.getAnalyticsUserId(mContext));                    // String or number   get User ID
//                String activePlans = PreferenceHandler.getActivePlans();
//                String inActivePlans = PreferenceHandler.getInActivePlans();
//
//                if (!TextUtils.isEmpty(activePlans)) {
//                    String[] split = activePlans.split(",");
//                    profileUpdate.put("active_plans", split);
//                }
//
//                if (!TextUtils.isEmpty(inActivePlans)) {
//                    String[] split = inActivePlans.split(",");
//                    profileUpdate.put("inactive_plans", split);
//                }
//
//                profileUpdate.put("is_subscribed", PreferenceHandler.getIsSubscribed(mContext));
//            }
//            profileUpdate.put("is_loggedIn", false);
//
//            if (PreferenceHandler.getNotificationEnabled(mContext))
//                profileUpdate.put("MSG-push", true);                        // Enable push notifications_new
//            else
//                profileUpdate.put("MSG-push", false);                        // Enable push notifications_new
//
//            AnalyticsProvider.sendProfileDataToCleverTap(profileUpdate);
//        }
    }

    public void fireScreenView() {
        AnalyticsProvider mAnalytics = new AnalyticsProvider(getContext());
        mAnalytics.fireScreenView(AnalyticsProvider.Screens.UserDetails);
        pageEvents = new PageEvents(Constants.HomeScreen);
        webEngageAnalytics.screenViewedEvent(pageEvents);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}
