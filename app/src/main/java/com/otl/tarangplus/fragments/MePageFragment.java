package com.otl.tarangplus.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.facebook.login.LoginManager;
import com.github.angads25.toggle.interfaces.OnToggledListener;
import com.github.angads25.toggle.model.ToggleableView;
import com.github.angads25.toggle.widget.LabeledSwitch;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.JsonObject;
//import com.onesignal.OneSignal;
import com.otl.tarangplus.Analytics.Analytics;
import com.otl.tarangplus.Analytics.AnalyticsProvider;
import com.otl.tarangplus.Analytics.WebEngageAnalytics;
import com.otl.tarangplus.MainActivity;
import com.otl.tarangplus.OnBoardingActivity;
import com.otl.tarangplus.R;
import com.otl.tarangplus.SplashScreenActivity;
import com.otl.tarangplus.Utils.Constants;
import com.otl.tarangplus.Utils.Helper;
import com.otl.tarangplus.Utils.PreferenceHandler;
import com.otl.tarangplus.adapters.WhoIsWatchingAdapter;
import com.otl.tarangplus.customeUI.GradientTextView;
import com.otl.tarangplus.customeUI.MyTextView;
import com.otl.tarangplus.model.AppEvents;
import com.otl.tarangplus.model.Data;
import com.otl.tarangplus.model.DataError;
import com.otl.tarangplus.model.ListResonse;
import com.otl.tarangplus.model.PageEvents;
import com.otl.tarangplus.model.Profile;
import com.otl.tarangplus.model.ProfileData;
import com.otl.tarangplus.model.UpdateProfileRequest;
import com.otl.tarangplus.model.UserProfileData;
import com.otl.tarangplus.rest.ApiService;
import com.otl.tarangplus.rest.RestClient;
import com.otl.tarangplus.rest.SignOutDetails;
import com.webengage.sdk.android.User;
import com.webengage.sdk.android.WebEngage;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MePageFragment extends Fragment implements WhoIsWatchingAdapter.selectProfileClickListner {


    View mRootView;
    @BindView(R.id.login)
    GradientTextView mLogin;
    @BindView(R.id.view_plans_without_logged_in)
    GradientTextView mViewPlan;
    @BindView(R.id.help)
    GradientTextView mHelp;
    @BindView(R.id.terms_of_use)
    GradientTextView mTermsOfUse;
    @BindView(R.id.privacy_policies)
    GradientTextView mPrivacyPolicy;
    @BindView(R.id.contact_us)
    GradientTextView mContactUs;

    @BindView(R.id.terms_of_logged_in_use)
    GradientTextView mTermsOfUseLoggedIn;
    @BindView(R.id.privacy_logged_in_policies)
    GradientTextView mPrivacyPolicyLoggedIn;
    @BindView(R.id.contact_logged_in_us)
    GradientTextView mContactUsLoggedIn;

    @BindView(R.id.logged_in_container)
    LinearLayout loggedInContainer;
    @BindView(R.id.non_logged_in_container)
    LinearLayout non_logged_in_container;
    @BindView(R.id.account_deails)
    GradientTextView account_deails;
    @BindView(R.id.watch_list)
    GradientTextView watch_list;
    @BindView(R.id.favourite)
    GradientTextView favourite;
    @BindView(R.id.reminders)
    GradientTextView reminders;
    @BindView(R.id.view_plas_with_logged_in)
    GradientTextView view_plas_with_logged_in;
    @BindView(R.id.my_devices_with_logged_in)
    GradientTextView my_devices_with_logged_in;
    @BindView(R.id.manage_payments)
    GradientTextView manage_payments;
    @BindView(R.id.notifications)
    GradientTextView notifications;
    //    @BindView(R.id.action_settings)
//    GradientTextView action_settings;
    @BindView(R.id.refer_freind)
    GradientTextView refer_freind;
    @BindView(R.id.help_with_logged_in)
    GradientTextView help_with_logged_in;
//    @BindView(R.id.who_is_watching_list)
//    RecyclerView who_is_watching_list;

    @BindView(R.id.app_version_txt)
    MyTextView mAppVersion;
    @BindView(R.id.register)
    GradientTextView mRegister;

    @BindView(R.id.register_trail_text)
    GradientTextView mRegisterTrailText;

    @BindView(R.id.devider_1)
    View devider1;
    @BindView(R.id.devider_2)
    View devider2;
    @BindView(R.id.devider_3)
    View devider3;
    //    @BindView(R.id.devider_4)
//    View devider4;
    @BindView(R.id.divider5)
    View devider5;

    @BindView(R.id.view_plan_image_login)
    ImageView mViewPlansImage;
    @BindView(R.id.contact_us_image_login)
    ImageView mContactUSImage;

    @BindView(R.id.view_plans_login)
    RelativeLayout view_plans_login;

    @BindView(R.id.playlist)
    RelativeLayout playlist;

    //    private WhoIsWatchingAdapter mWhoIsWatchingAdapter;
    public static final String TAG = MePageFragment.class.getSimpleName();

    /*@BindView(R.id.notification_logged_in)
    GradientTextView notificationSwitchLoggedin;*/
    /*    @BindView(R.id.notification_without_logged_in)
    GradientTextView notification_switch;*/

    @BindView(R.id.loggedin_switch)
    LabeledSwitch languageSwitchLoggedin;
    @BindView(R.id.language_switch)
    LabeledSwitch language_switch;

    @BindView(R.id.notification_switch_not)
    LabeledSwitch notification_switch_not;
    @BindView(R.id.notification_switch_loggedin)
    LabeledSwitch notification_switch_loggedin;


    @BindView(R.id.clear_watch_history_without_logged_in)
    GradientTextView clearWatchHistoryWithoutLoggedIn;
    private ApiService mApiService;
    private Dialog dialog;
    private WebEngageAnalytics webEngageAnalytics;
    private PageEvents pageEvents;
    private AppEvents appEvents;
    private Analytics mAnalyticsEvent;
    public MePageFragment() {

    }

    public Dialog getLogoutPopUp() {
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.me_fragment, container, false);
        ButterKnife.bind(this, mRootView);
        webEngageAnalytics = new WebEngageAnalytics();
        mLogin.setOnClickListener(view ->
                startActivityForResult((new Intent(getActivity(), OnBoardingActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK)), Constants.ACTION_ME_LOGIN));
        String currentProfileID = PreferenceHandler.getCurrentProfileID(getActivity());
        mApiService = new RestClient(getContext()).getApiService();
        //getParentalControlData();
        mAnalyticsEvent = Analytics.getInstance(getContext());
        if (PreferenceHandler.getIsTrailAvaliable(getActivity())) {
            mRegisterTrailText.setText(PreferenceHandler.getStartTrailMessage(getActivity()));
        }
        appEvents = new AppEvents(1, "", "Me Screen", Constants.AN_APP_TYPE, "", Constants.PAGE_VISIT, "Me Screen",
                PreferenceHandler.getUserState(getContext()), PreferenceHandler.getUserPeriod(getContext()),
                PreferenceHandler.getUserPlanType(getContext()), PreferenceHandler.getUserId(getContext()));
        mAnalyticsEvent.logAppEvents(appEvents);

        return mRootView;
    }

    public void getParentalControlData() {
        if (PreferenceHandler.isLoggedIn(getActivity()) &&
                PreferenceHandler.getSessionId(getActivity()) != null) {
            mApiService.getAllUsers(PreferenceHandler.getSessionId(getActivity()))
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(profileData -> {
                if (profileData != null && profileData.getData() != null) {
                    UserProfileData profileDataData = profileData.getData();
                    String control = profileDataData.getParentalControl();
                    if (control.equalsIgnoreCase("true")) {
                        PreferenceHandler.setParentEnabled(getActivity(), true);
                    } else if (control.equalsIgnoreCase("false")) {
                        PreferenceHandler.setParentEnabled(getActivity(), false);
                    }
                }
            }, throwable -> {
                throwable.printStackTrace();
            });
        }
    }


    public void setDataAccordingly() {

        if (PreferenceHandler.isLoggedIn(getActivity())) {

            loggedInContainer.setVisibility(View.VISIBLE);
            non_logged_in_container.setVisibility(View.GONE);
            //setUpRecycleView();

            if (PreferenceHandler.getSessionId(getActivity()) != null) {
                //getAllUsers();
            }
        } else {
            loggedInContainer.setVisibility(View.GONE);
//            who_is_watching_list.setVisibility(View.GONE);
//            manage_accounts.setVisibility(View.GONE);
            non_logged_in_container.setVisibility(View.VISIBLE);
        }


        if (PreferenceHandler.isKidsProfileActive(Objects.requireNonNull(getActivity()))) {
            // action_settings.setVisibility(View.GONE);
            view_plas_with_logged_in.setVisibility(View.GONE);
            account_deails.setVisibility(View.GONE);
            devider1.setVisibility(View.GONE);
            devider2.setVisibility(View.VISIBLE);
            devider3.setVisibility(View.GONE);
            //devider4.setVisibility(View.GONE);
            devider5.setVisibility(View.GONE);
            mContactUsLoggedIn.setVisibility(View.GONE);
            mContactUSImage.setVisibility(View.GONE);
            mViewPlansImage.setVisibility(View.GONE);

        } else {
            //action_settings.setVisibility(View.VISIBLE);
            account_deails.setVisibility(View.VISIBLE);
            devider1.setVisibility(View.VISIBLE);
//            view_plas_with_logged_in.setVisibility(View.VISIBLE);
//            account_deails.setVisibility(View.VISIBLE);
//            devider1.setVisibility(View.VISIBLE);
            devider2.setVisibility(View.VISIBLE);
//            devider3.setVisibility(View.VISIBLE);
            //devider4.setVisibility(View.VISIBLE);
        }

    }

    public void getAllUsers() {
        Helper.showProgressDialog(getActivity());
        mApiService.getAllUsers(PreferenceHandler.getSessionId(getActivity()))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ProfileData>() {
                    @Override
                    public void call(ProfileData profileData) {
                        if (profileData != null) {
                            updateDefaultUserUI(profileData);
                            //mWhoIsWatchingAdapter.updateItems(profileData.getData().getProfiles());
//                            who_is_watching_list.setVisibility(View.VISIBLE);
//                            manage_accounts.setVisibility(View.VISIBLE);
                        } else {
//                            who_is_watching_list.setVisibility(View.GONE);
//                            manage_accounts.setVisibility(View.GONE);
                        }

                        Helper.dismissProgressDialog();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        Helper.dismissProgressDialog();
//                        who_is_watching_list.setVisibility(View.GONE);
//                        manage_accounts.setVisibility(View.GONE);

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

    private void updateDefaultUserUI(ProfileData profileData) {
        if (profileData.getData() != null && profileData.getData().getProfiles() != null && profileData.getData().getProfiles().size() > 0) {
            for (Profile profileData1 : profileData.getData().getProfiles()) {
                String currentProfileID = PreferenceHandler.getCurrentProfileID(getActivity());
                if (TextUtils.isEmpty(currentProfileID) || profileData1.getProfileId().equalsIgnoreCase(currentProfileID) && profileData1.isDefaultProfile()) {
                    account_deails.setVisibility(View.VISIBLE);
                    devider1.setVisibility(View.VISIBLE);
                    view_plas_with_logged_in.setVisibility(View.VISIBLE);
                    mViewPlansImage.setVisibility(View.VISIBLE);

                    devider3.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) getActivity()).setSelectedNavUI(Constants.TABS.ME);
        fireScreenView();

        mAppVersion.setText(Constants.getAppVersion());
        if (PreferenceHandler.getNotificationOn(getActivity()).equals(Constants.YES) ||
                PreferenceHandler.getNotificationOn(getActivity()).equals("")) {
            notification_switch_not.setOn(true);
            notification_switch_loggedin.setOn(true);
        } else {
            notification_switch_not.setOn(false);
            notification_switch_loggedin.setOn(false);
        }
        if (PreferenceHandler.getLang(getActivity()).equals(Constants.ENGLISH) ||
                PreferenceHandler.getLang(getActivity()).equals("")) {
            language_switch.setOn(false);
            languageSwitchLoggedin.setOn(false);
        } else {
            language_switch.setOn(true);
            languageSwitchLoggedin.setOn(true);
        }
        setDataAccordingly();

        notification_switch_not.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(ToggleableView toggleableView, boolean isOn) {

                if (isOn) {
                    PreferenceHandler.setNotificationOn(getActivity(), Constants.YES);
//                    OneSignal.setSubscription(true);
                    FirebaseMessaging.getInstance().subscribeToTopic("tarangplus")
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    Log.d(TAG, "Ho gaya");
                                }
                            });
                } else {
                    PreferenceHandler.setNotificationOn(getActivity(), Constants.NO);
//                    OneSignal.setSubscription(false);
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("tarangplus")
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    Log.d(TAG, "Ho gaya");
                                }
                            });

                }

            }
        });
        notification_switch_loggedin.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(ToggleableView toggleableView, boolean isOn) {

                if (isOn) {
                    PreferenceHandler.setNotificationOn(getActivity(), Constants.YES);
//                    OneSignal.setSubscription(true);
                    FirebaseMessaging.getInstance().subscribeToTopic("tarangplus");
                } else {
                    PreferenceHandler.setNotificationOn(getActivity(), Constants.NO);
//                    OneSignal.setSubscription(false);
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("tarangplus");
                }

            }
        });


//        manage_accounts.setOnClickListener(view1 -> {
//            boolean kidsProfileActive = PreferenceHandler.isKidsProfileActive(getActivity());
//            if (kidsProfileActive) {
//                Helper.showToast(getActivity(), getString(R.string.kids_profile_prohibited), R.drawable.ic_error_icon);
//                return;
//            }
//            WhoIsWatchingFragment fragment = new WhoIsWatchingFragment();
//            Bundle bundle = new Bundle();
//            bundle.putBoolean(Constants.IS_FROM_LOGIN, false);
//            fragment.setArguments(bundle);
//            Helper.addFragment(getActivity(), fragment, WhoIsWatchingFragment.TAG);
//        });
        //who_is_watching_list.addItemDecoration(new SpacesItemDecoration(0, 0, 0, (int) getResources().getDimension(R.dimen.px_20)));

    }

    @OnClick({R.id.view_plans_without_logged_in, R.id.help,
            R.id.terms_of_use, R.id.privacy_policies, R.id.contact_us, R.id.terms_of_logged_in_use,
            R.id.privacy_logged_in_policies, R.id.contact_logged_in_us, R.id.account_deails, R.id.watch_list,
            R.id.reminders, R.id.view_plas_with_logged_in, R.id.manage_payments, R.id.notifications,
            /*R.id.action_settings,*/ R.id.refer_freind, R.id.help_with_logged_in, R.id.log_out, R.id.register,
            R.id.notification_without_logged_in, R.id.language_switch, R.id.notification_logged_in,
            R.id.loggedin_switch, R.id.clear_watch_history_without_logged_in, R.id.favourite, R.id.playlist, R.id.my_devices_with_logged_in})

    public void onViewClicked(View view) {
        int id = view.getId();
        view.setClickable(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setClickable(true);
            }
        }, 500);
        if (id == R.id.view_plans_without_logged_in) {
            SubscriptionWebViewFragment subscriptionWebViewFragment = new SubscriptionWebViewFragment();
            Bundle bundle = new Bundle();
            bundle.putString(Constants.FROM_WHERE, MePageFragment.TAG);
            bundle.putBoolean(Constants.IS_LOGGED_IN, false);
            subscriptionWebViewFragment.setArguments(bundle);

            appEvents = new AppEvents(1, Constants.LINK_VIEW_PLAN, "", Constants.AN_APP_TYPE, "", Constants.CLICK, "",
                    PreferenceHandler.getUserState(getContext()), PreferenceHandler.getUserPeriod(getContext()), PreferenceHandler.getUserPlanType(getContext()), PreferenceHandler.getUserId(getContext()));
            mAnalyticsEvent.logAppEvents(appEvents);

            AnalyticsProvider analyticsProvider = new AnalyticsProvider(getActivity());
            analyticsProvider.fireNetCoreViewPlanClick();
            // analyticsProvider.branchViewPlans(getActivity());

            Helper.addFragmentForDetailsScreen(getActivity(), subscriptionWebViewFragment, SubscriptionWebViewFragment.TAG);

        } else if (id == R.id.help || id == R.id.help_with_logged_in) {
            HelpFragment helpFragment = new HelpFragment();
            Helper.addFragment(getActivity(), helpFragment, HelpFragment.TAG);
            appEvents = new AppEvents(1, "FAQ", "", Constants.AN_APP_TYPE, "", Constants.PAGE_VISIT, "FAQ",
                    "", PreferenceHandler.getUserPeriod(getContext()), PreferenceHandler.getUserPlanType(getContext()), PreferenceHandler.getUserId(getContext()));
            mAnalyticsEvent.logAppEvents(appEvents);
        } else if (id == R.id.terms_of_use || id == R.id.terms_of_logged_in_use) {
            TermsOfUserFragment fragment = new TermsOfUserFragment();
            Bundle bundle = new Bundle();
            bundle.putString(Constants.TYPE, Constants.Type.TERMES_OF_USE);
            fragment.setArguments(bundle);
            appEvents = new AppEvents(1, "Terms of use", "", Constants.AN_APP_TYPE, "", Constants.PAGE_VISIT, "Terms of use",
                    "", PreferenceHandler.getUserPeriod(getContext()), PreferenceHandler.getUserPlanType(getContext()), PreferenceHandler.getUserId(getContext()));
            mAnalyticsEvent.logAppEvents(appEvents);
            Helper.addFragmentForDetailsScreen(getActivity(), fragment, TermsOfUserFragment.TAG);
        } else if (id == R.id.privacy_policies || id == R.id.privacy_logged_in_policies) {
            TermsOfUserFragment fragment = new TermsOfUserFragment();
            Bundle bundle = new Bundle();
            bundle.putString(Constants.TYPE, Constants.Type.PRIVACY_POLICY);
            fragment.setArguments(bundle);
            appEvents = new AppEvents(1,  "Privacy policy", "", Constants.AN_APP_TYPE, "", Constants.PAGE_VISIT,  "Privacy policy",
                    "", PreferenceHandler.getUserPeriod(getContext()), PreferenceHandler.getUserPlanType(getContext()), PreferenceHandler.getUserId(getContext()));
            mAnalyticsEvent.logAppEvents(appEvents);
            Helper.addFragmentForDetailsScreen(getActivity(), fragment, TermsOfUserFragment.TAG);
        } else if (id == R.id.contact_us || id == R.id.contact_logged_in_us) {
            TermsOfUserFragment fragment = new TermsOfUserFragment();
            Bundle bundle = new Bundle();
            bundle.putString(Constants.TYPE, Constants.Type.CONTACT_US);
            fragment.setArguments(bundle);
            appEvents = new AppEvents(1,  "Contact us", "", Constants.AN_APP_TYPE, "", Constants.PAGE_VISIT,  "Contact us",
                    "", PreferenceHandler.getUserPeriod(getContext()), PreferenceHandler.getUserPlanType(getContext()), PreferenceHandler.getUserId(getContext()));
            mAnalyticsEvent.logAppEvents(appEvents);
            Helper.addFragmentForDetailsScreen(getActivity(), fragment, TermsOfUserFragment.TAG);
        } else if (id == R.id.account_deails) {

            /**
             * Account details fragment
             *
             */
            if (!PreferenceHandler.isKidsProfileActive(getActivity())) {
                AccountDetailsFragment accountDetailsFragment = new AccountDetailsFragment();
                Helper.addFragment(getActivity(), accountDetailsFragment, AccountDetailsFragment.TAG);
            }
        } else if (id == R.id.watch_list) {
            Fragment fragment_watch = new WatchListFragment();
            Bundle bundle = new Bundle();
            bundle.putString(Constants.WHAT, "watchlater");
            fragment_watch.setArguments(bundle);
            appEvents = new AppEvents(1,  "Watch Later Screen", "", Constants.AN_APP_TYPE, "", Constants.PAGE_VISIT,  "Watch Later Screen",
                    "", PreferenceHandler.getUserPeriod(getContext()), PreferenceHandler.getUserPlanType(getContext()), PreferenceHandler.getUserId(getContext()));
            mAnalyticsEvent.logAppEvents(appEvents);
            Helper.addFragment(getActivity(), fragment_watch, WatchListFragment.TAG);
        }
        else if (id == R.id.my_devices_with_logged_in) {
            RegisteredDevicesWebViewFragment registeredDevicesWebViewFragment = new RegisteredDevicesWebViewFragment();
            Bundle bundle = new Bundle();
            bundle.putString(Constants.FROM_WHERE, LoginFragment.TAG);
            if (getArguments() != null)
                bundle.putString(Constants.FROM_PAGE, getArguments().getString(Constants.FROM_PAGE));
            registeredDevicesWebViewFragment.setArguments(bundle);
            Helper.addFragment(getActivity(), registeredDevicesWebViewFragment, RegisteredDevicesWebViewFragment.TAG);

        }else if (id == R.id.favourite) {
            Fragment fragment_watch = new WatchListFragment();
            Bundle bundle = new Bundle();
            bundle.putString(Constants.WHAT, "favourite");
            fragment_watch.setArguments(bundle);
            Helper.addFragment(getActivity(), fragment_watch, WatchListFragment.TAG);
        } else if (id == R.id.playlist) {
            Fragment playlistFragment = new PlaylistFragment();
            Bundle bundle = new Bundle();
            bundle.putString(Constants.WHAT, "playlist");
            playlistFragment.setArguments(bundle);
            Helper.addFragment(getActivity(), playlistFragment, PlaylistFragment.TAG);
        } else if (id == R.id.reminders) {

        } else if (id == R.id.view_plas_with_logged_in) {

         /*   if(PreferenceHandler.getFacebook(getActivity()).equalsIgnoreCase("facebook")) {
                Helper.showToast(getActivity(), "Please login with email/mobile to Subscribe.", R.drawable.ic_error_icon);
                return;
            }*/

            SubscriptionWebViewFragment subscriptionWebViewFragment = new SubscriptionWebViewFragment();
            Bundle bundle = new Bundle();
            bundle.putString(Constants.FROM_WHERE, MePageFragment.TAG);
            bundle.putBoolean(Constants.IS_LOGGED_IN, true);
            subscriptionWebViewFragment.setArguments(bundle);
            AnalyticsProvider analyticsProvider = new AnalyticsProvider(getActivity());
            analyticsProvider.fireNetCoreViewPlanClick();
            // analyticsProvider.branchViewPlans(getActivity());
            appEvents = new AppEvents(1, Constants.LINK_VIEW_PLAN, "", Constants.AN_APP_TYPE, "", Constants.CLICK, "",
                    PreferenceHandler.getUserState(getContext()), PreferenceHandler.getUserPeriod(getContext()), PreferenceHandler.getUserPlanType(getContext()), PreferenceHandler.getUserId(getContext()));
            mAnalyticsEvent.logAppEvents(appEvents);
            Helper.addFragmentForDetailsScreen(getActivity(), subscriptionWebViewFragment, SubscriptionWebViewFragment.TAG);
        } /*else if (id == R.id.manage_payments) {
            SubscriptionWebViewFragment subscriptionWebViewFragment = new SubscriptionWebViewFragment();
            Bundle bundle = new Bundle();
            bundle.putString(Constants.FROM_WHERE, MePageFragment.TAG);
            bundle.putBoolean(Constants.IS_LOGGED_IN, true);
            subscriptionWebViewFragment.setArguments(bundle);
             Helper.addFragmentForDetailsScreen(getActivity(),subscriptionWebViewFragment, SubscriptionWebViewFragment.TAG);
        }*/ else if (id == R.id.notifications) {

        } /*else if (id == R.id.action_settings) {
         *//**
         * For Setting fragment - Parental control, clear watch history, etc.
         *//*
            SettingsFragment settingsFragment = new SettingsFragment();
            Helper.addFragment(getActivity(), settingsFragment, SettingsFragment.TAG);

        }*/
        else if (id == R.id.refer_freind) {
            /**
             * For Refer fragment - Sharing a link.
             */
            ReferFragment referFragment = new ReferFragment();
            //Helper.addFragment(getActivity(), referFragment, ReferFragment.TAG);

        } else if (id == R.id.help_with_logged_in) {
            /**
             * For Help fragment - Sharing a link.
             */
            HelpFragment helpFragment = new HelpFragment();
            // Helper.addFragment(getActivity(), helpFragment, HelpFragment.TAG);

        } else if (id == R.id.log_out) {
            /*PreferenceHandler.clearAll(getActivity());
            setDataAccordingly();
            ViewModelProviders.of(this).get(SearchPageViewModel.class).deleteAllSearHistory();*/
            showLoginConfirmationPopUp();
        } else if (id == R.id.register) {
            Intent intent = new Intent(getActivity(), OnBoardingActivity.class);
            intent.putExtra(Constants.FROM_PAGE, RegisterFragment.TAG);
            startActivityForResult(intent.
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK), Constants.ACTION_ME_LOGIN);
        } /*else if (id == R.id.notification_without_logged_in || id == R.id.notification_logged_in) {
         *//* *//*
            try {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        } */ else if (id == R.id.language_switch || id == R.id.loggedin_switch) {

            language_switch.setOnToggledListener(new OnToggledListener() {
                @Override
                public void onSwitched(ToggleableView toggleableView, boolean isOn) {
                    showLanguageConfirmationPopUp(isOn);

                }
            });
            languageSwitchLoggedin.setOnToggledListener(new OnToggledListener() {
                @Override
                public void onSwitched(ToggleableView toggleableView, boolean isOn) {
                    showLanguageConfirmationPopUp(isOn);

                }
            });


        } else if (id == R.id.clear_watch_history_without_logged_in) {
            clearWatchHistory();
        }
    }

    private void showLanguageConfirmationPopUp(final boolean isOn) {

        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.watch_later_layout);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        MyTextView title = (MyTextView) dialog.findViewById(R.id.title);
        GradientTextView cancel = (GradientTextView) dialog.findViewById(R.id.cancel);
        GradientTextView confirm = (GradientTextView) dialog.findViewById(R.id.confirm);
        MyTextView warningMessage = (MyTextView) dialog.findViewById(R.id.warning);
        String lang = PreferenceHandler.getLang(getActivity());
        String currentLang = "";
        if (!TextUtils.isEmpty(lang) && lang.equals(Constants.ODIA)) { // other language
            currentLang = "English";
        } else { // English
            currentLang = "Odia";
        }
        warningMessage.setText("Are you sure you want to change app language to " + currentLang + ".");
        title.setText("Change Language");
        cancel.setText("No");
        confirm.setText("Yes");
        title.setVisibility(View.VISIBLE);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = PreferenceHandler.getLang(getActivity());
                if (str.equals(Constants.ODIA)) {

                    language_switch.setOn(true);
                    languageSwitchLoggedin.setOn(true);
                    //PreferenceHandler.setLang(getActivity(),Constants.NO);
                } else {
                    language_switch.setOn(false);
                    languageSwitchLoggedin.setOn(false);

                    //PreferenceHandler.setLang(getActivity(),Constants.YES);

                }
                dialog.cancel();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String lang = PreferenceHandler.getLang(getActivity());
                String currentLang = "";
                if (!TextUtils.isEmpty(lang) && lang.equals(Constants.ODIA)) { // other language
                    currentLang = "En";
                } else { // English
                    currentLang = "Od";
                }
                if (currentLang.equals("En")) {
                    PreferenceHandler.setLang(getActivity(), Constants.ENGLISH);
                    webEngageAnalytics.languageChosen(Constants.ENGLISH);
                } else {
                    PreferenceHandler.setLang(getActivity(), Constants.ODIA);
                    webEngageAnalytics.languageChosen(Constants.ODIA);
                }
                dialog.cancel();

                Intent intent2 = new Intent();
                intent2.setAction(Constants.LANGUAGECHANGES);
                getActivity().sendBroadcast(intent2);
                try {
                    MainActivity ma = (MainActivity) getActivity();
                    ma.onBackPressed();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                //  startActivity(intent);
            }
        });

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
                /*todo Search history*/
                //Helper.deleteSearchHistory(getActivity());
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
        mApiService.signOut(sessionId, outDetails)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject object) {
                        Helper.dismissProgressDialog();
                        if (object != null) {
                            JsonObject element = object.getAsJsonObject("data");
                            String jsonMessage = element.getAsJsonObject().get("message").getAsString();
                            Helper.showToast(getActivity(), jsonMessage, R.drawable.ic_check);
                            Helper.clearLoginDetails(getActivity());
                            String type = PreferenceHandler.getLoggedInType(getContext());
                            if(type.equals("msisdn")){
                                type = Constants.MOBILE;
                            }
//                            setDataAccordingly();
                            try {
                                LoginManager.getInstance().logOut();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                            /*Intent intent = new Intent(getActivity(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            getActivity().finish();*/
                            webEngageAnalytics.logout(Constants.LOGOUT);

                            appEvents = new AppEvents(1,type, "", Constants.AN_APP_TYPE, "", Constants.LOGOUT, "",
                                    "", PreferenceHandler.getUserPeriod(getContext()), PreferenceHandler.getUserPlanType(getContext()), PreferenceHandler.getUserId(getContext()));
                            mAnalyticsEvent.logAppEvents(appEvents);

                            User userWeb = WebEngage.get().user();
                            if(userWeb!=null){
                                userWeb.logout();
                            }

                            goToLoginPage();
                            AnalyticsProvider analyticsProvider = new AnalyticsProvider(getActivity());
                            String analyticsUserId = PreferenceHandler.getAnalyticsUserId(getActivity());
                            analyticsProvider.resetUserId(analyticsUserId, getActivity());

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

    private void goToLoginPage() {
        Intent intent = new Intent(getActivity(), OnBoardingActivity.class);
        intent.putExtra(Constants.FROM_PAGE, MePageFragment.TAG);
        getActivity().startActivityForResult(intent, Constants.ACTION_LOGIN_CLICKED);
        getActivity().finish();
    }

    private void setUpRecycleView() {
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        // mWhoIsWatchingAdapter = new WhoIsWatchingAdapter(getActivity(), new ArrayList<Profile>(), false, true);
//        who_is_watching_list.setHasFixedSize(true);
//        who_is_watching_list.setItemViewCacheSize(mWhoIsWatchingAdapter.getItemCount());
//        who_is_watching_list.setAdapter(mWhoIsWatchingAdapter);
//        who_is_watching_list.setLayoutManager(linearLayoutManager);
        // mWhoIsWatchingAdapter.setOnProfileClickListner(this);
    }

    @Override
    public void onProfileSelected(Profile profile) {
        String currentProfileID = PreferenceHandler.getCurrentProfileID(getActivity());
        if (currentProfileID.equalsIgnoreCase(profile.getProfileId())) {
            Helper.showToast(getActivity(), "Already in same profile", R.drawable.ic_cross);
            return;
        }
        Helper.showProgressDialog(getActivity());
        mApiService.getAccountDetails(PreferenceHandler.getSessionId(getActivity()))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ListResonse>() {
                    @Override
                    public void call(ListResonse data) {
                        PreferenceHandler.isProfileChanged(getActivity(), true);
                        Helper.dismissProgressDialog();
                        Data dataData = data.getData();
                        String control = dataData.getParentalControl();
                        if (!TextUtils.isEmpty(control) && control.equalsIgnoreCase("true")) {
                            boolean isKidsProfActive = profile.isChild();
                            if (!isKidsProfActive) {
                                VerifyPinFragment fragment = new VerifyPinFragment();
                                Bundle bundle = new Bundle();
                                bundle.putString(Constants.PARENTAL_PIN, dataData.getParentalPin());
                                bundle.putParcelable(Constants.SELECTED_PROFILE, profile);
                                fragment.setArguments(bundle);
                                Helper.addFragment(getActivity(), fragment, VerifyPinFragment.TAG);
                            } else {
                                //changeUser(profile);
                            }
                        } else {
                            //changeUser(profile);
                        }

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        Helper.dismissProgressDialog();

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
                        PreferenceHandler.setCurrentProfileName(getActivity(), profile.getFirstname());
                        PreferenceHandler.setCurrentProfileID(getActivity(), profile.getProfileId());
                        if (pf.isChild()) {
                            PreferenceHandler.setIsKidProfile(getActivity(), true);
                        } else {
                            PreferenceHandler.setIsKidProfile(getActivity(), false);
                        }

                        Helper.dismissProgressDialog();
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        getActivity().finish();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        Helper.dismissProgressDialog();
                    }
                });


    }

    public void fireScreenView() {
        AnalyticsProvider mAnalytics = new AnalyticsProvider(getContext());
        mAnalytics.fireScreenView(AnalyticsProvider.Screens.ME);
        pageEvents = new PageEvents(Constants.ME);
        webEngageAnalytics.screenViewedEvent(pageEvents);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
        Helper.showProgressDialog(getActivity());
        String sessionId = PreferenceHandler.getSessionId(getActivity());
        mApiService.clearWatchHistory(sessionId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject object) {
                        if (object != null) {
                            Helper.dismissProgressDialog();
                            Helper.showToast(getActivity(), getString(R.string.watch_history_message), R.drawable.ic_check);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        //need to show error messages.
                        Helper.dismissProgressDialog();
                    }
                });
    }
}
