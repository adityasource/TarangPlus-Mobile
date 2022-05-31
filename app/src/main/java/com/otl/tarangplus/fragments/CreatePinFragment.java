package com.otl.tarangplus.fragments;

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
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.JsonObject;
import com.otl.tarangplus.customeUI.GradientTextView;
import com.otl.tarangplus.customeUI.MyTextView;
import com.otl.tarangplus.model.Profile;
import com.otl.tarangplus.model.SignInRequest;
import com.otl.tarangplus.model.UpdateProfileRequest;
import com.otl.tarangplus.model.User;
import com.poovam.pinedittextfield.LinePinField;
import com.otl.tarangplus.R;
import com.otl.tarangplus.Utils.Constants;
import com.otl.tarangplus.Utils.Helper;
import com.otl.tarangplus.Utils.KeyboardUtils;
import com.otl.tarangplus.Utils.PreferenceHandler;
import com.otl.tarangplus.rest.ApiService;
import com.otl.tarangplus.rest.RestClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class CreatePinFragment extends Fragment {

    public static final String TAG = CreatePinFragment.class.getSimpleName();
    @BindView(R.id.category_back_img)
    ImageView categoryBackImg;
    @BindView(R.id.category_grad_back)
    TextView categoryGradBack;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.header)
    MyTextView header;
    @BindView(R.id.close)
    AppCompatImageView close;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;
    @BindView(R.id.otp_text_input)
    LinePinField mOtpInputText;
    @BindView(R.id.four_digit_container)
    LinearLayout fourDigitContainer;
    @BindView(R.id.continue_to_parent)
    GradientTextView continueToParent;
    @BindView(R.id.message)
    GradientTextView message;
    @BindView(R.id.forgot_pin)
    GradientTextView forgotPin;

    private ApiService mApiService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.create_pin_fragment, container, false);
        ButterKnife.bind(this, inflate);
        mApiService = new RestClient(getActivity()).getApiService();
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
        return inflate;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        close.setVisibility(View.GONE);

        header.setText("Create PIN");
        message.setVisibility(View.VISIBLE);
        message.setText(getString(R.string.create_new_pin));
        continueToParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setParentalControl();
            }
        });
        mOtpInputText.setOnTextCompleteListener(enteredText -> true);
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
                        Helper.addWithoutBackStack(getActivity(), new HomePageFragment(), HomePageFragment.TAG);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Helper.dismissProgressDialog();
                        throwable.printStackTrace();
                    }
                });
    }

    private void setParentalControl() {
        Bundle arguments = getArguments();
        boolean isParentalControlEnabled = arguments.getBoolean(Constants.IS_PARENTAL_CONTROL_ENABLED);
        String finalPin = getFinalPin();
        if (TextUtils.isEmpty(finalPin) || finalPin.length() < 4) {
            Helper.showToast(getActivity(), "Please enter 4 digit pin", R.drawable.ic_cross);
            return;
        }
        SignInRequest request = new SignInRequest();
        User user = new User();
        user.setParentalControl(isParentalControlEnabled + "");
        user.setParentalPin(finalPin);
        request.setUser(user);
        request.setAuthToken(Constants.API_KEY);
        Helper.showProgressDialog(getActivity());
        Helper.dismissKeyboard(getActivity());
        mApiService.setParentalControl(request, PreferenceHandler.getSessionId(getActivity()))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject jsonObject) {
                        Helper.showToast(getActivity(), jsonObject.get("data").getAsJsonObject().get("message").toString(), R.drawable.ic_check);
                        PreferenceHandler.setParentEnabled(getActivity(), isParentalControlEnabled);
                        SettingsFragment fragment = new SettingsFragment();
                        Helper.addWithoutBackStackAndKeepHome(getActivity(), fragment, SettingsFragment.TAG,1);
                        Helper.dismissProgressDialog();
                        Helper.dismissKeyboard(getActivity());
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        Helper.dismissKeyboard(getActivity());
                        Helper.dismissProgressDialog();
                        Helper.showToast(getActivity(), throwable.getMessage(), R.drawable.ic_cross);
                    }
                });
    }

    private String getFinalPin() {
        String finalFin = mOtpInputText.getText().toString();
        return finalFin;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
