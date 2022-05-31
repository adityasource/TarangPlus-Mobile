package com.otl.tarangplus.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.gson.JsonObject;
import com.otl.tarangplus.customeUI.GradientTextView;
import com.otl.tarangplus.customeUI.MyTextView;
import com.otl.tarangplus.model.DataError;
import com.otl.tarangplus.model.Profile;
import com.otl.tarangplus.model.ProfileData;
import com.otl.tarangplus.model.UpdateProfileRequest;
import com.otl.tarangplus.MainActivity;
import com.otl.tarangplus.R;
import com.otl.tarangplus.Utils.Constants;
import com.otl.tarangplus.Utils.Helper;
import com.otl.tarangplus.Utils.PreferenceHandler;
import com.otl.tarangplus.Utils.SpacesItemDecoration;
import com.otl.tarangplus.adapters.ManageProfileAdapter;
import com.otl.tarangplus.rest.ApiService;
import com.otl.tarangplus.rest.RestClient;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class WhoIsWatchingFragment extends Fragment {
    View mRootView;
    public static final String TAG = WhoIsWatchingFragment.class.getClass().getName();

    @BindView(R.id.who_is_watching_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.header)
    MyTextView mHeaderLayout;
    @BindView(R.id.back)
    AppCompatImageView mBackBtn;
    @BindView(R.id.close)
    AppCompatImageView mCloseBtn;
    @BindView(R.id.proceed)
    GradientTextView mProceed;
    private ManageProfileAdapter profileAdapter;
    Profile firstPersonProfile;

    @BindView(R.id.members_container)
    RelativeLayout mTextAddLayout;

    boolean mIsFromLogin;
    private ApiService mApiService;


    public WhoIsWatchingFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.who_is_watching, container, false);
        ButterKnife.bind(this, mRootView);
        mIsFromLogin = getArguments() == null ? false : getArguments().getBoolean(Constants.IS_FROM_LOGIN, false);
        mApiService = new RestClient(getContext()).getApiService();
        if (mIsFromLogin && !Constants.REGION.equalsIgnoreCase("IN")) {
            showDialog();
        }

        mCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Helper.removeCurrentFragment(getActivity(), TAG);
            }
        });

        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Helper.removeCurrentFragment(getActivity(),TAG);*/
                getActivity().onBackPressed();
            }
        });
        setUpRecycleView();
        if (mIsFromLogin) {
            mHeaderLayout.setText(R.string.who_is_watching_header);
            mBackBtn.setVisibility(View.GONE);
            mProceed.setVisibility(View.VISIBLE);
        } else {
            mHeaderLayout.setText(R.string.manage_profiles);
            mTextAddLayout.setVisibility(View.GONE);
            mBackBtn.setVisibility(View.VISIBLE);
            mCloseBtn.setVisibility(View.GONE);
            mProceed.setVisibility(View.GONE);
        }
        Helper.showProgressDialog(getActivity());
        mProceed.setVisibility(View.GONE);

        //getAllProfiles();

        mCloseBtn.setOnClickListener(view -> Helper.removeCurrentFragment(getActivity(), TAG));

        mProceed.setOnClickListener(view -> {
            UpdateProfileRequest updateProfileRequest = new UpdateProfileRequest();
            updateProfileRequest.setAuthToken(Constants.API_KEY);
            Profile profile = new Profile();
            profile.setProfileId(firstPersonProfile.getProfileId());
            updateProfileRequest.setUserProfile(profile);

            Helper.showProgressDialog(getActivity());
            mApiService.assignProfile(PreferenceHandler.getSessionId(getActivity()), updateProfileRequest)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<JsonObject>() {
                        @Override
                        public void call(JsonObject jsonObject) {
                            PreferenceHandler.setCurrentProfileName(getActivity(), firstPersonProfile.getFirstname());
                            PreferenceHandler.setCurrentProfileID(getActivity(), firstPersonProfile.getProfileId());
                            Helper.dismissProgressDialog();
                            new WelcomeDialogFragment().show(getFragmentManager(), WelcomeDialogFragment.TAG);
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            throwable.printStackTrace();
                            Helper.dismissProgressDialog();
                        }
                    });

        });
        return mRootView;
    }

    public void getAllProfiles() {
        mApiService.getAllUsers(PreferenceHandler.getSessionId(getActivity()))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ProfileData>() {
                    @Override
                    public void call(ProfileData profileData) {
                        if (profileData.getData().getProfiles() != null && profileData.getData().getProfiles().size() > 0) {
                            firstPersonProfile = profileData.getData().getProfiles().get(0);
                        }
                        profileAdapter.updateItems(profileData.getData().getProfiles());
                        Helper.dismissProgressDialog();
                        if (mIsFromLogin)
                            mProceed.setVisibility(View.VISIBLE);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        DataError errorRes = Constants.getErrorMessage(throwable);
                        String errorMessage = errorRes.getError().getMessage();
                        int errorCode = errorRes.getError().getCode();
                        if (getActivity() != null)
                            if (errorCode == 1016 && ((retrofit2.adapter.rxjava.HttpException) throwable).code() == 422) {
                            Helper.clearLoginDetails(getActivity());
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(new Intent(intent));
                            getActivity().finish();
                            Helper.showToast(getActivity(), errorMessage, R.drawable.ic_error_icon);
                            // ViewModelProviders.of(WhoIsWatchingFragment.this).get(SearchPageViewModel.class).deleteAllSearHistory();
                            Helper.deleteSearchHistory(getActivity());
                        }
                        Helper.dismissProgressDialog();
                        throwable.printStackTrace();
                    }
                });
    }

    private void setUpRecycleView() {
        final GridLayoutManager linearLayoutManager = new GridLayoutManager(getActivity(), 2);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView.addItemDecoration(new SpacesItemDecoration((int) getResources().getDimension(R.dimen.px_40), 0, 0, 0));
        profileAdapter = new ManageProfileAdapter(getActivity(), new ArrayList<Profile>(), mIsFromLogin, false);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(profileAdapter);
        mRecyclerView.setLayoutManager(linearLayoutManager);
    }


    private void showDialog() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(R.layout.welcome_your_email_registered);
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        final ImageView closeOtpButton = (ImageView) dialog.getWindow().findViewById(R.id.cross);

        closeOtpButton.setOnClickListener(view -> dialog.dismiss());
    }


    @Override
    public void onResume() {
        super.onResume();
        getAllProfiles();
    }
}
