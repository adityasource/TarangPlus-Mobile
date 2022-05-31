package com.otl.tarangplus.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;
import com.otl.tarangplus.customeUI.GradientTextView;
import com.otl.tarangplus.customeUI.MyEditText;
import com.otl.tarangplus.customeUI.MyTextView;
import com.otl.tarangplus.model.SignInRequest;
import com.otl.tarangplus.model.User;
import com.otl.tarangplus.R;
import com.otl.tarangplus.Utils.Constants;
import com.otl.tarangplus.Utils.Helper;
import com.otl.tarangplus.Utils.PreferenceHandler;
import com.otl.tarangplus.rest.ApiService;
import com.otl.tarangplus.rest.RestClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class PINForgotPasswordFrag extends Fragment {

    public static final String TAG = PINForgotPasswordFrag.class.getSimpleName();
    @BindView(R.id.category_back_img)
    android.widget.ImageView categoryBackImg;
    @BindView(R.id.category_grad_back)
    android.widget.TextView categoryGradBack;
    @BindView(R.id.back)
    android.widget.ImageView back;
    @BindView(R.id.header)
    MyTextView header;
    @BindView(R.id.close)
    androidx.appcompat.widget.AppCompatImageView close;
    @BindView(R.id.toolbar)
    androidx.appcompat.widget.Toolbar toolbar;
    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;
    @BindView(R.id.password_text_input)
    TextInputLayout passwordTextInput;
    @BindView(R.id.forgot_pwd)
    GradientTextView forgotPwd;

    @BindView(R.id.continue_to_parent)
    GradientTextView continueToParent;

    @BindView(R.id.me_edit_txt)
    MyEditText meEditText;
    @BindView(R.id.message)
    GradientTextView message;

    private ApiService apiService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.parental_change_password, container, false);
        ButterKnife.bind(this, inflate);
        return inflate;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        header.setText("Forgot PIN");
        apiService = new RestClient(getActivity()).getApiService();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        close.setVisibility(View.GONE);
        meEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                passwordTextInput.setError("");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        continueToParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToResetPinFragment();
            }
        });
    }

    private void moveToResetPinFragment() {
        String password = meEditText.getText().toString();
        if (TextUtils.isEmpty(password)) {
            passwordTextInput.setError(getString(R.string.password_empty));
            return;
        }
        if (password.trim().length() < 6) {
            passwordTextInput.setError(getString(R.string.password_short));
            return;
        }
        Helper.showProgressDialog(getActivity());
        Helper.dismissKeyboard(getActivity());
        SignInRequest request = new SignInRequest();
        User user = new User();
        user.setPassword(password);
        request.setUser(user);
        request.setAuthToken(Constants.API_KEY);

//        apiService.verifyPassowrd(PreferenceHandler.getSessionId(getActivity()), password)     // deprecated now its post call
        apiService.verifyPassowrd(request, PreferenceHandler.getSessionId(getActivity()))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject jsonObject) {
                        Helper.dismissProgressDialog();
                        VerifyPinFragment fragment = new VerifyPinFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString(Constants.FROM_PAGE, TAG);


                        if (getArguments() != null) {
                            String whoInitiatedFlow = getArguments().getString(Constants.WHO_INITIATED_THIS);
                            if (!TextUtils.isEmpty(whoInitiatedFlow))
                                bundle.putString(Constants.WHO_INITIATED_THIS, whoInitiatedFlow);
                        }

                        fragment.setArguments(bundle);
                        Helper.addFragment(getActivity(), fragment, VerifyPinFragment.TAG);
//                        Helper.addWithoutBackStack(getActivity(), fragment, VerifyPinFragment.TAG);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        ResponseBody responseBody = ((retrofit2.adapter.rxjava.HttpException) throwable).response().errorBody();
                        String errorMessage = Constants.getErrorMessage(responseBody);
                        Helper.showToast(getActivity(), errorMessage, R.drawable.ic_cross);
                        throwable.printStackTrace();
                        Helper.dismissProgressDialog();
                    }
                });
    }
}
