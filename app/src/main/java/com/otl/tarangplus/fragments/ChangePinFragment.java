package com.otl.tarangplus.fragments;

import android.app.Service;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.otl.tarangplus.customeUI.GradientTextView;
import com.otl.tarangplus.customeUI.MyTextView;
import com.otl.tarangplus.model.SignInRequest;
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
import butterknife.Unbinder;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ChangePinFragment extends Fragment {

    public static final String TAG = ChangePinFragment.class.getSimpleName();
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
    @BindView(R.id.message)
    GradientTextView message;
    @BindView(R.id.otp_text_input)
    LinePinField mOtpInputText;
    @BindView(R.id.four_digit_container)
    LinearLayout fourDigitContainer;
    @BindView(R.id.continue_to_parent)
    GradientTextView continueToParent;
    @BindView(R.id.forgot_pin)
    GradientTextView forgotPin;
    Unbinder unbinder;

    private ApiService mApiService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.create_pin_fragment, container, false);
        unbinder = ButterKnife.bind(this, inflate);
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

        header.setText("Change PIN");
        message.setText(getString(R.string.create_new_pin));
        message.setVisibility(View.VISIBLE);
        continueToParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setParentalControl();
            }
        });
        mOtpInputText.setOnTextCompleteListener(enteredText -> true);

    }

    public void setParentalControl() {
        String finalPin = getFinalPin();
        if (TextUtils.isEmpty(finalPin) || finalPin.length() < 4) {
            Helper.showToast(getActivity(), "Please enter 4 digit pin", R.drawable.ic_cross);
            return;
        }
        SignInRequest request = new SignInRequest();
        User user = new User();
        user.setParentalControl(true + "");
        user.setParentalPin(finalPin);
        request.setUser(user);
        request.setAuthToken(Constants.API_KEY);
        Helper.showProgressDialog(getActivity());
        Helper.dismissKeyboard(getActivity());
        mApiService.setParentalControl(request, PreferenceHandler.getSessionId(getActivity()))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(jsonObject -> {
                    Helper.showToast(getActivity(), jsonObject.get("data").getAsJsonObject().get("message").toString(), R.drawable.ic_check);
                    PreferenceHandler.setParentEnabled(getActivity(), true);
                    SettingsFragment fragment = new SettingsFragment();
                    Helper.addWithoutBackStackAndKeepHome(getActivity(), fragment, SettingsFragment.TAG,1);
                    Helper.dismissProgressDialog();
                    Helper.dismissKeyboard(getActivity());
                }, throwable -> {
                    throwable.printStackTrace();
                    Helper.dismissKeyboard(getActivity());
                    Helper.dismissProgressDialog();
                    Helper.showToast(getActivity(), throwable.getMessage(), R.drawable.ic_cross);
                });
    }

    private String getFinalPin() {
        String finalFin = mOtpInputText.getText().toString();
        return finalFin;
    }
    public static void setFocus(EditText editText) {
        if (editText == null)
            return;

        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
    }

    public void showSoftKeyboard(EditText editText) {
        if (editText == null)
            return;

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, 0);
    }
}
