package com.otl.tarangplus.fragments;

import android.app.DatePickerDialog;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.otl.tarangplus.Analytics.Analytics;
import com.otl.tarangplus.Analytics.AnalyticsProvider;
import com.otl.tarangplus.Analytics.WebEngageAnalytics;
import com.otl.tarangplus.MyApplication;
import com.otl.tarangplus.R;
import com.otl.tarangplus.Utils.Constants;
import com.otl.tarangplus.Utils.Helper;
import com.otl.tarangplus.Utils.JsonStore;
import com.otl.tarangplus.Utils.PreferenceHandler;
import com.otl.tarangplus.customeUI.GradientTextView;
import com.otl.tarangplus.customeUI.MyEditText;
import com.otl.tarangplus.customeUI.MyTextView;
import com.otl.tarangplus.model.AppEvents;
import com.otl.tarangplus.model.CountryCode;
import com.otl.tarangplus.model.Data;
import com.otl.tarangplus.model.ListResonse;
import com.otl.tarangplus.model.PageEvents;
import com.otl.tarangplus.model.UserEvents;
import com.otl.tarangplus.rest.AccountUpdateRequest;
import com.otl.tarangplus.rest.AccountUser;
import com.otl.tarangplus.rest.RestClient;
import com.otl.tarangplus.viewModel.AccountDetailsViewModel;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class UpdatePersonalFragment extends Fragment {
    View mRootView;
    private AppEvents appEvents;
    private Analytics mAnalyticsEvent;
    public static final String TAG = UpdatePersonalFragment.class.getClass().getName();
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
    @BindView(R.id.name)
    MyEditText mName;
    @BindView(R.id.name_text_input)
    TextInputLayout mNameTextInput;
    @BindView(R.id.mobile_number)
    MyEditText mMobileNumber;
    @BindView(R.id.mobile_number_text_input)
    TextInputLayout mMobileNumberTextInput;
    @BindView(R.id.email_id)
    MyEditText mEmailId;
    @BindView(R.id.email_text_input)
    TextInputLayout mEmailTextInput;
    @BindView(R.id.birth_date)
    MyEditText mBirthDate;
    @BindView(R.id.birth_date_layout)
    TextInputLayout mBirthDateLayout;
    @BindView(R.id.confirm)
    GradientTextView mDone;
    @BindView(R.id.other_details)
    LinearLayout mOtherDetails;

    @BindView(R.id.address_txt)
    MyEditText addressTxt;
    @BindView(R.id.city_txt)
    MyEditText cityTxt;
    @BindView(R.id.state_txt)
    MyEditText stateTxt;
    @BindView(R.id.country_text)
    MyEditText country;

    private AccountDetailsViewModel mAccountsViewModels;
    private RestClient mApiServices;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private String type;
    private String dob;

    private WebEngageAnalytics webEngageAnalytics;
    private UserEvents userEvents;
    private PageEvents pageEvents;


    public UpdatePersonalFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_update_personal, container, false);
        ButterKnife.bind(this, mRootView);
        mAccountsViewModels = ViewModelProviders.of(this).get(AccountDetailsViewModel.class);
        mApiServices = new RestClient(getActivity());
        webEngageAnalytics = new WebEngageAnalytics();
        mClose.setVisibility(View.GONE);
        mAnalyticsEvent = Analytics.getInstance(getContext());
        appEvents = new AppEvents(1, "Update Personal Info screen", "", Constants.AN_APP_TYPE, "", Constants.PAGE_VISIT, "Update Personal Info screen",
                "", PreferenceHandler.getUserPeriod(getContext()), PreferenceHandler.getUserPlanType(getContext()), PreferenceHandler.getUserId(getContext()));
        mAnalyticsEvent.logAppEvents(appEvents);
   /*     FirebaseAnalytics a = FirebaseAnalytics.getInstance(getActivity());
        FirebaseAnalytics.UserProperty
        *//*test*//*
        searchMyCountry();*/

        Bundle arguments = getArguments();
        if (arguments != null) {
            String profileNAme = arguments.getString(Constants.PROFILE_NAME);
            if (profileNAme != null && !TextUtils.isEmpty(profileNAme) && !profileNAme.equalsIgnoreCase("-")) {
                mName.setText(profileNAme);
            }

            String phone = arguments.getString(Constants.MOBILE);
            if (phone != null && !TextUtils.isEmpty(phone) && !phone.equalsIgnoreCase("-")) {

                mMobileNumber.setText(phone);

            }

            String mail = arguments.getString(Constants.USERMAIL);
            if (mail != null && !TextUtils.isEmpty(mail) && !mail.equalsIgnoreCase("-")) {
                mEmailId.setText(mail);
            }

            String dob = arguments.getString(Constants.DOB);
            if (dob != null && !TextUtils.isEmpty(dob) && !dob.equalsIgnoreCase("-")) {
                mBirthDate.setText(dob);
            }

            String address = arguments.getString(Constants.ADDRESS);
            if (address != null && !TextUtils.isEmpty(address)) {
                addressTxt.setText(address);
            } else {

            }

            String state = arguments.getString(Constants.STATE_KEY);
            if (state != null && !TextUtils.isEmpty(state)) {
                stateTxt.setText(state);
            } else {
                stateTxt.setText(Constants.STATE);
            }

            String country1 = arguments.getString(Constants.COUNTRY_KEY);
            if (state != null && !TextUtils.isEmpty(state)) {
                country.setText(country1);
            } else {
                country.setText(Constants.COUNTRY);
            }

        }
        return mRootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fireScreenView();
        String region = PreferenceHandler.getRegion(getActivity());
        mHeader.setText(getResources().getString(R.string.update_personal_details));
        type = PreferenceHandler.getLoggedInType(getActivity());
        if (TextUtils.isEmpty(type)) {
            type = PreferenceHandler.getFacebook(getActivity());
        }
        if (!region.equalsIgnoreCase("IN")) {
            if (type.equalsIgnoreCase("facebook")) {

            } else {
                mEmailId.setClickable(false);
                mEmailId.setFocusable(false);
            }
        } else {
            if (!type.equals("") && type.equalsIgnoreCase("email")) {
                mEmailId.setFocusable(false);
                mEmailId.setClickable(false);
            } else if (!type.equals("") && type.equalsIgnoreCase("msisdn")) {
                mMobileNumber.setFocusable(false);
                mMobileNumber.setClickable(false);
            }
        }


        mEmailId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mEmailTextInput.setError("");
            }
        });

        mMobileNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                String indianMobileNumber = mMobileNumber.getText().toString();
                if (region.equalsIgnoreCase("IN")) {
                    mMobileNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
                    if (b) {
                        new Handler().postDelayed(() -> {
                            if (TextUtils.isEmpty(mMobileNumber.getText())) {
                                final String prefix1 = "+91 ";
                                mMobileNumber.setText(prefix1);
                                Selection.setSelection(mMobileNumber.getText(), mMobileNumber.getText().length());
                            }
                        }, 200);

                    } else {
                        if (mMobileNumber.getText().toString().equalsIgnoreCase("+91 "))
                            mMobileNumber.setText("");
                    }
                }


                if (b)
                    return;

                if (region.equalsIgnoreCase("IN")) {
                    if (indianMobileNumber.trim().length() == 3) {
                        mMobileNumberTextInput.setError(getString(R.string.is_field_empty));
                    } else if (indianMobileNumber.trim().length() != 14) {
                        mMobileNumberTextInput.setError(getString(R.string.please_enter_valid_number));
                    }
                } else {
                    if (TextUtils.isEmpty(indianMobileNumber)) {
                        mMobileNumberTextInput.setError(getString(R.string.is_field_empty));
                    } else if (!Constants.isEmailValied(indianMobileNumber)) {
                        mMobileNumberTextInput.setError(getString(R.string.invalid_mobile_number));
                    }
                }

            }
        });

        mMobileNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mMobileNumberTextInput.setError(null);
                mMobileNumberTextInput.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().startsWith("+91 ") && editable.toString().startsWith("+91") && mMobileNumber.hasFocus()) {
                    String str = editable.toString();
                    str = str.replace("+91", "+91 ");
                    mMobileNumber.setText(str);
                    Selection.setSelection(mMobileNumber.getText(), mMobileNumber.getText().length());
                    return;
                }


                if (!editable.toString().startsWith("+91 ") && mMobileNumber.hasFocus()) {
                    mMobileNumber.setText("+91 ");
                    Selection.setSelection(mMobileNumber.getText(), mMobileNumber.getText().length());
                }
            }
        });
    }


    @OnClick({R.id.back, R.id.name, R.id.mobile_number, R.id.email_id, R.id.birth_date, R.id.confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                if (getActivity().getSupportFragmentManager() != null) {
                    getActivity().getSupportFragmentManager().popBackStackImmediate();

                } else if (getActivity().getFragmentManager() != null) {
                    getActivity().getSupportFragmentManager().popBackStackImmediate();
                }
                break;
            case R.id.name:
                break;
            case R.id.mobile_number:
                break;
            case R.id.email_id:
                break;
            case R.id.birth_date:
                showCalendar();
                break;
            case R.id.confirm:
                updateDetails();
        }
    }

    private void updateDetails() {

        if (TextUtils.isEmpty(type) || TextUtils.isEmpty(Constants.REGION)) {
            Helper.showToast(getActivity(), "Oops something went wrong, please reopen the app.", R.drawable.ic_cross);
            return;
        }

        if (TextUtils.isEmpty(mName.getText().toString().trim())) {
            Helper.showToast(getActivity(), "Please enter your name", R.drawable.ic_cross);
            return;
        }
        if (!type.equalsIgnoreCase("msisdn")
                && Constants.REGION.equalsIgnoreCase(Constants.INDIA)
                && !TextUtils.isEmpty(mMobileNumber.getText().toString().trim())
                && !mMobileNumber.getText().toString().trim().equals("+91")
                && mMobileNumber.getText().toString().trim().length() != 14) {
            Helper.showToast(getActivity(), "Please fill correct and complete details", R.drawable.ic_cross);
            return;
        }
        //if (Constants.REGION.equalsIgnoreCase(Constants.INDIA)) {
        if (type.equalsIgnoreCase("msisdn") && mEmailId.getText() != null && !mEmailId.getText().toString().equals("")) {
            if (!Constants.isEmailValied(mEmailId.getText().toString())) {
                mEmailTextInput.setError(getString(R.string.valid_email));
                return;
            }
           if(!TextUtils.isEmpty(type) && type.equalsIgnoreCase("facebook")) {

           }

        }

     /*   } else if (Constants.REGION.equalsIgnoreCase(Constants.USA) && !Constants.isEmailValied(mEmailId.getText().toString())) {
            mEmailTextInput.setError(getString(R.string.invalid_email));
            return;
        }*/


        if (!Constants.REGION.equalsIgnoreCase(Constants.INDIA))
            if (!TextUtils.isEmpty(mMobileNumber.getText().toString()))
                if (mMobileNumber.getText().length() != 14) {
                    mMobileNumber.setError(getString(R.string.please_enter_valid_number));
                    return;
                }


        AccountUpdateRequest accountUpdateRequest = new AccountUpdateRequest();
//        updateProfileRequest.setAuthToken(Constants.API_KEY);
        AccountUser user = new AccountUser();
        user.setFirstname(mName.getText().toString().trim());
        if (!TextUtils.isEmpty(mBirthDate.getText().toString().trim())) {
            try {

                dob = setDate();
                user.setBirthDate(dob);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //
        if (!type.equals("") && type.equalsIgnoreCase("email")) {
            user.setMobile(mMobileNumber.getText().toString().trim());
        } else if (!type.equals("") && type.equalsIgnoreCase("msisdn")) {
            PreferenceHandler.setUserEMailId(getActivity(), mEmailId.getText().toString().trim());
            user.setUserEmailId(mEmailId.getText().toString().trim());
        }else if (!type.equals("") && type.equalsIgnoreCase("facebook")){
            user.setMobile(mMobileNumber.getText().toString().trim());
            PreferenceHandler.setUserEMailId(getActivity(), mEmailId.getText().toString().trim());
            user.setUserEmailId(mEmailId.getText().toString().trim());
        }


        if (!TextUtils.isEmpty(addressTxt.getText() + "")) {
            user.setAddress(addressTxt.getText() + "");
        } else {
            user.setAddress("");
        }
        if (!TextUtils.isEmpty(stateTxt.getText() + "")) {
            user.setState(stateTxt.getText() + "");
        } else {
            user.setState("");
        }
        if (!TextUtils.isEmpty(country.getText() + "")) {
            user.setCountry(country.getText() + "");
        } else {
            user.setCountry("");
        }

        accountUpdateRequest.setUser(user);
        String uName =  mName.getText().toString().trim()!= null ? mName.getText().toString().trim() : "";
        String uPhone =  mMobileNumber.getText().toString().trim()!= null ? mMobileNumber.getText().toString().trim() : "";
        String uMail =  mEmailId.getText().toString().trim()!= null ? mEmailId.getText().toString().trim() : "";
        String uAddress =  addressTxt.getText().toString()!= null ? addressTxt.getText().toString() : "";
        String uState =  stateTxt.getText().toString()!= null ? stateTxt.getText().toString() : "";
        String uCountry =  country.getText().toString()!= null ? country.getText().toString() : "";
        String uDob =  dob!= null ? dob : "";

        userEvents = new UserEvents(uName,uPhone,uMail,uAddress,uState,uCountry,uDob);


        Helper.showProgressDialog(getActivity());
        mApiServices.getApiService().updateAccountDetails(PreferenceHandler.getSessionId(getActivity()), accountUpdateRequest)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ListResonse>() {
                    @Override
                    public void call(ListResonse resonse) {
                        if (resonse != null) {
                            PreferenceHandler.setUserName(MyApplication.getInstance(), user.getFirstname());
                            Data data = resonse.getData();
                            Helper.updateCleverTapDetails(getActivity());
//                            sendDataToCleverTap();
                            if (data != null) {
                                Helper.dismissProgressDialog();
                                Helper.dismissKeyboard(getActivity());
                                String message = data.getMessage();
                                if (message != null && !TextUtils.isEmpty(message)) {
                                    webEngageAnalytics.updateAccount(userEvents);
                                    Helper.showToast(getActivity(), message, R.drawable.ic_check);
                                    if (getActivity().getSupportFragmentManager() != null) {
                                        getActivity().onBackPressed();

                                    } else if (getActivity().getFragmentManager() != null) {
                                        getActivity().onBackPressed();
                                    }
                                }
                            }
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Helper.dismissProgressDialog();
                        String errorMessage = throwable.getLocalizedMessage();
                        Helper.showToast(getActivity(), getActivity().getResources().getString(R.string.error_on_region), R.drawable.ic_check);
                        Helper.dismissProgressDialog();
                    }
                });
    }


    private String setDate() throws Exception {
        String[] split = mBirthDate.getText().toString().trim().split("-");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(split[0]));
        calendar.set(Calendar.MONTH, Integer.parseInt(split[1]) - 1);
        calendar.set(Calendar.YEAR, Integer.parseInt(split[2]));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date time = calendar.getTime();
        return dateFormat.format(time);

    }

    private void showCalendar() {

        Helper.dismissKeyboard(getActivity());
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -18);
        cal.add(Calendar.DATE, -1);
        Date date = cal.getTime();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), R.style.CalenderDialogTheme,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                            int dayOfMonth = datePicker.getDayOfMonth();
                            int year = datePicker.getYear();
                            int month = datePicker.getMonth() + 1;
                            String mm, dd;
                            if (dayOfMonth < 10) {
                                dd = "0" + dayOfMonth;
                            } else {
                                dd = dayOfMonth + "";
                            }
                            if (month < 10) {
                                mm = "0" + month;
                            } else {
                                mm = month + "";
                            }
//                            Log.d(TAG, "onDateSet: " + dd + "-" + mm + "-" + year);
                            mBirthDate.setText(dd + "-" + mm + "-" + year);
                        }
                    }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.getDatePicker().setMaxDate(cal.getTimeInMillis());
            datePickerDialog.show();
        } else {
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), R.style.CalenderDialogTheme,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                            int dayOfMonth = datePicker.getDayOfMonth();
                            int year = datePicker.getYear();
                            int month = datePicker.getMonth() + 1;
                            String mm, dd;
                            if (dayOfMonth < 10) {
                                dd = "0" + dayOfMonth;
                            } else {
                                dd = dayOfMonth + "";
                            }
                            if (month < 10) {
                                mm = "0" + month;
                            } else {
                                mm = month + "";
                            }
//                            Log.d(TAG, "onDateSet: " + dd + "-" + mm + "-" + year);
                            mBirthDate.setText(dd + "-" + mm + "-" + year);
                        }
                    }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
            dialog.getDatePicker().setMaxDate(cal.getTimeInMillis());
            dialog.show();
        }
    }

    private boolean isUser18YearOld(DatePicker view, int year, int month, int day) {
        Calendar userAge = new GregorianCalendar(year, month, day);
        Calendar minAdultAge = new GregorianCalendar();
        minAdultAge.add(Calendar.YEAR, -18);
        if (minAdultAge.before(userAge)) {
            return false;
        }
        return true;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Helper.dismissKeyboard(getActivity());
    }


    public void fireScreenView() {
        AnalyticsProvider mAnalytics = new AnalyticsProvider(getContext());
        mAnalytics.fireScreenView(AnalyticsProvider.Screens.EditUserDetails);
        pageEvents = new PageEvents(Constants.EditUserDetails);
        webEngageAnalytics.screenViewedEvent(pageEvents);
    }

    private void searchMyCountry() {
        String json = JsonStore.json;
        Gson gson = new Gson();
        Type listType = new TypeToken<List<CountryCode>>() {
        }.getType();
        List<CountryCode> countryCode = gson.fromJson(json, listType);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            List<CountryCode> result1 = countryCode.stream()
                    .filter(x -> x.getName().startsWith("In"))
                    .collect(Collectors.toList());

            for (CountryCode code : result1) {
                Log.d("CountryCode", code.getName());
            }

        } else {

        }


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
