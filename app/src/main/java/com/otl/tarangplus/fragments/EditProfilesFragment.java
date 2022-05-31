package com.otl.tarangplus.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.AppCompatImageView;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;
import com.otl.tarangplus.Analytics.AnalyticsProvider;
import com.otl.tarangplus.customeUI.GradientTextView;
import com.otl.tarangplus.customeUI.MyEditText;
import com.otl.tarangplus.customeUI.MyTextView;
import com.otl.tarangplus.model.Profile;
import com.otl.tarangplus.model.UpdateProfileRequest;
import com.otl.tarangplus.Database.LayoutDbScheme;
import com.otl.tarangplus.R;
import com.otl.tarangplus.Utils.Constants;
import com.otl.tarangplus.Utils.Helper;
import com.otl.tarangplus.Utils.PreferenceHandler;
import com.otl.tarangplus.rest.ApiService;
import com.otl.tarangplus.rest.RestClient;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class EditProfilesFragment extends Fragment {
    public static final String TAG = EditProfilesFragment.class.getName();

    View mRootView;
    @BindView(R.id.name_image_text)
    MyTextView mImageText;
    @BindView(R.id.profile_name)
    MyEditText mProfileName;
    @BindView(R.id.kid_switch)
    Switch mSwitchIsKid;
    @BindView(R.id.delete_this_profile)
    MyTextView mDeleteThisProfile;
    @BindView(R.id.save)
    GradientTextView mSave;
    @BindView(R.id.add)
    GradientTextView mAdd;
    @BindView(R.id.header)
    MyTextView mHeader;
    @BindView(R.id.close)
    AppCompatImageView mClose;
    @BindView(R.id.back)
    AppCompatImageView mBack;
    boolean setIsKid;
    //        Top Bar UI
    @BindView(R.id.category_back_img)
    ImageView mTopbarImage;
    @BindView(R.id.category_grad_back)
    TextView mGradientBackground;
    @BindView(R.id.kids_container)
    RelativeLayout mKidsLayer;
    private ApiService mApiService;

    @BindView(R.id.mobile_number_text_input)
    TextInputLayout mobileNumberTextInput;

    public EditProfilesFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.edit_user_profile, container, false);
        ButterKnife.bind(this, mRootView);
        mClose.setVisibility(View.GONE);
        setTopbarUI(Constants.getSchemeColor("All"));
        mApiService = new RestClient(getActivity()).getApiService();
        final String profileId = getArguments() == null ? null : getArguments().getString(Constants.PROFILE_ID);
        String name = getArguments() == null ? null : getArguments().getString(Constants.PROFILE_NAME);
        String profileName = "";
        if (name != null && !TextUtils.isEmpty(name)) {
            profileName = name.trim();
        }
        String kidsProfile = getArguments() == null ? null : getArguments().getString(Constants.IS_KID_PROF);
        Boolean isDefaultProfile = getArguments() == null ? null : getArguments().getBoolean(Constants.IS_DEFAULT_PROFILE, false);

        if (TextUtils.isEmpty(profileId)) {
            mDeleteThisProfile.setVisibility(View.GONE);
        } else {
            mDeleteThisProfile.setVisibility(View.VISIBLE);
        }

        if (isDefaultProfile != null && isDefaultProfile) {
            mDeleteThisProfile.setVisibility(View.GONE);
            mKidsLayer.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(profileId) && !TextUtils.isEmpty(profileName)) {
            String init1 = profileName.substring(0, 1);
            mImageText.setText(profileName.substring(0, 1));
            boolean contains = profileName.contains(" ");
            if (contains) {
                int i = profileName.indexOf(" ");
                if (profileName.length() > i) {
                    char init2 = profileName.charAt(i + 1);
                    mImageText.setText(init1 + init2);
                }
            }
            if (kidsProfile != null && kidsProfile.equalsIgnoreCase("true")) {
                setIsKid = true;
                mImageText.setBackground(getActivity().getDrawable(R.drawable.place_holder_kids_profile));
            } else {
                mImageText.setBackground(getActivity().getDrawable(R.drawable.place_holder_circle));
                setIsKid = false;
            }
            mSwitchIsKid.setChecked(setIsKid);
            mProfileName.setText(profileName);
        }

        mBack.setOnClickListener(view -> /*Helper.removeCurrentFragment(getActivity(), TAG)*/getActivity().onBackPressed());


        mSwitchIsKid.setOnCheckedChangeListener((compoundButton, b) -> {
            Helper.dismissKeyboard(getActivity());
            if (b) {
                setIsKid = true;
                mImageText.setBackground(getActivity().getDrawable(R.drawable.place_holder_kids_profile));
            } else {
                setIsKid = false;
                mImageText.setBackground(getActivity().getDrawable(R.drawable.place_holder_circle));
            }
        });


        mProfileName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                mImageText.setBackgroundResource(0);

                if (mSwitchIsKid.isChecked()) {
                    mImageText.setBackground(getActivity().getDrawable(R.drawable.place_holder_kids_profile));
                } else {
                    mImageText.setBackground(getActivity().getDrawable(R.drawable.place_holder_circle));
                }
                setTextAccordingToName(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().startsWith(" ")) {
                    mProfileName.setText("");
                    Selection.setSelection(mProfileName.getText(), mProfileName.getText().length());
                }

                if (editable.toString().endsWith("  ")) {
                    String jj = editable.toString().trim();
                    jj = jj + " ";
                    mProfileName.setText(jj);
                    Selection.setSelection(mProfileName.getText(), mProfileName.getText().length());
                }

            }
        });


        if (!TextUtils.isEmpty(profileId)) {
            mSave.setVisibility(View.VISIBLE);
            mAdd.setVisibility(View.GONE);
            mHeader.setText("Edit Profile");
        } else {
            mSave.setVisibility(View.GONE);
            mAdd.setVisibility(View.VISIBLE);
            mHeader.setText("Add Profile");
        }

        mAdd.setOnClickListener(view -> {

            if (TextUtils.isEmpty(mProfileName.getText().toString().trim())) {
                mobileNumberTextInput.setError(getString(R.string.is_field_empty));
//                Helper.showToast(getActivity(), "Profile name is empty", R.drawable.ic_cross);
                return;
            }

            if (Helper.isKeyboardVisible(getActivity())) {
                Helper.dismissKeyboard(getActivity());
            }

            UpdateProfileRequest updateProfileRequest = new UpdateProfileRequest();
            updateProfileRequest.setAuthToken(Constants.API_KEY);
            Profile profile = new Profile();
            profile.setFirstname(mProfileName.getText().toString());
            profile.setChild(setIsKid);
            updateProfileRequest.setUserProfile(profile);
            Helper.showProgressDialog(getActivity());
            mApiService.createProfile(PreferenceHandler.getSessionId(getActivity()), updateProfileRequest)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<JsonObject>() {
                        @Override
                        public void call(JsonObject profileData) {
                            Helper.dismissProgressDialog();
//                            Helper.showToast(getActivity(), "Profile Created successfully", R.drawable.ic_check);
                            // Helper.addFragment(getActivity(), new MePageFragment(), MePageFragment.TAG);

                            try {
                                AnalyticsProvider analyticsProvider = new AnalyticsProvider(getActivity());
                                if (setIsKid)
                                    analyticsProvider.fireNetCoreProfileUpdate("kids");
                                else {
                                    analyticsProvider.fireNetCoreProfileUpdate("user");
                                }
                            }catch (Exception e){
                            }

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (getActivity() != null)
                                        getActivity().onBackPressed();
                                }
                            }, 100);
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            throwable.printStackTrace();
                            Helper.dismissProgressDialog();
                            Helper.showToast(getActivity(), Constants.getErrorMessage(throwable).getError().getMessage(), R.drawable.ic_error_icon);
//                            Helper.showToast(getActivity(), "Profile creation limit reached", R.drawable.ic_check);
                        }
                    });
        });


        mSave.setOnClickListener(view -> {

            if (TextUtils.isEmpty(mProfileName.getText().toString().trim())) {
//                Helper.showToast(getActivity(), "Profile name must not be blank", R.drawable.ic_cross);
                mobileNumberTextInput.setError(getString(R.string.is_field_empty));
                return;
            }

            UpdateProfileRequest updateProfileRequest = new UpdateProfileRequest();
            updateProfileRequest.setAuthToken(Constants.API_KEY);
            Profile profile = new Profile();
            setName(profile);
            profile.setChild(setIsKid);
            updateProfileRequest.setUserProfile(profile);
            Helper.showProgressDialog(getActivity());
            mApiService.updateUserProfile(PreferenceHandler.getSessionId(getActivity()), profileId, updateProfileRequest)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<JsonObject>() {
                        @Override
                        public void call(JsonObject profileData) {
                            Helper.dismissProgressDialog();
                            try {
                                AnalyticsProvider analyticsProvider = new AnalyticsProvider(getActivity());
                                if (setIsKid)
                                    analyticsProvider.fireNetCoreProfileUpdate("kids");
                                else {
                                    analyticsProvider.fireNetCoreProfileUpdate("user");
                                }
                            }catch (Exception e){
                            }
//                            Helper.showToast(getActivity(), "Profile updated successfully", R.drawable.ic_check);
                            getActivity().onBackPressed();
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            throwable.printStackTrace();
                            Helper.dismissProgressDialog();
                        }
                    });

        });

        mDeleteThisProfile.setOnClickListener(view -> {
            if (TextUtils.isEmpty(profileId)) {
                return;
            }
            String currentProfileID = PreferenceHandler.getCurrentProfileID(getActivity());
            if (currentProfileID.equalsIgnoreCase(profileId)) {
                Helper.showToast(getActivity(), "Selected profiles cannot be deleted. Change profile and try again", R.drawable.ic_error_icon);
                return;
            }

            showConfirmationPopUp(profileId);


        });


        return mRootView;
    }

    private void fireDeleteProfileApi(String profileId) {
        Helper.showProgressDialog(getActivity());
        mApiService.deleteProfile(PreferenceHandler.getSessionId(getActivity()), profileId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject profileData) {
                        Helper.dismissProgressDialog();
//                        Helper.showToast(getActivity(), "Deleted User Profile successfully", R.drawable.ic_check);
                        //Helper.addFragment(getActivity(), new MePageFragment(), MePageFragment.TAG);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (Helper.isKeyboardVisible(getActivity())) {
                                    Helper.dismissKeyboard(getActivity());
                                }
                                getActivity().onBackPressed();
                            }
                        }, 100);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Helper.dismissProgressDialog();
                    }
                });
    }


    private void setTextAccordingToName(String text) {
        if (!TextUtils.isEmpty(text)) {
            if (text.trim().split(" ").length > 1) {
                String[] splitName = text.split(" ");
                if (splitName != null && splitName.length >= 1) {
                    try {
                        mImageText.setText(splitName[0].substring(0, 1) + splitName[1].substring(0, 1));
                    } catch (Exception e) {
                        mImageText.setText(splitName[0].substring(0, 1));
                    }

                }
            } else {
                mImageText.setText(text.substring(0, 1));
            }
        } else {
            mImageText.setText("");
        }
    }

    public void setName(Profile profile) {
        String[] splitName = mProfileName.getText().toString().split(" ");
        if (splitName.length == 1) {
            profile.setFirstname(mProfileName.getText().toString());
        } else if (splitName.length == 2) {
            profile.setFirstname(splitName[0]);
            profile.setLastname(splitName[1]);
        } else if (splitName.length > 2) {
            profile.setFirstname(splitName[0]);
            profile.setLastname(splitName[1]);
        }

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
    public void onDestroy() {
        super.onDestroy();
        Helper.dismissKeyboard(getActivity());
    }

    private void showConfirmationPopUp(String profileId) {

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
        title.setText(R.string.del_profile_title);
        warningMessage.setText(R.string.del_profile_msg);
        cancel.setText(R.string.no);
        confirm.setText(R.string.yes);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                fireDeleteProfileApi(profileId);

            }
        });

    }


}
