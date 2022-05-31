package com.otl.tarangplus.dialogs;

import androidx.fragment.app.DialogFragment;

public class VerifyPinDialog extends DialogFragment  {

//    public static final String TAG = VerifyPinDialog.class.getSimpleName();
//    @BindView(R.id.category_back_img)
//    ImageView categoryBackImg;
//    @BindView(R.id.category_grad_back)
//    TextView categoryGradBack;
//    @BindView(R.id.back)
//    ImageView back;
//    @BindView(R.id.header)
//    MyTextView header;
//    @BindView(R.id.close)
//    AppCompatImageView close;
//    @BindView(R.id.toolbar)
//    Toolbar toolbar;
//    @BindView(R.id.app_bar_layout)
//    AppBarLayout appBarLayout;
//    @BindView(R.id.message)
//    GradientTextView message;
//    @BindView(R.id.pin1)
//    MyEditText pin1;
//    @BindView(R.id.pin2)
//    MyEditText pin2;
//    @BindView(R.id.pin3)
//    MyEditText pin3;
//    @BindView(R.id.pin4)
//    MyEditText pin4;
//    @BindView(R.id.pin5)
//    MyEditText pin5;
//    @BindView(R.id.four_digit_container)
//    LinearLayout fourDigitContainer;
//    @BindView(R.id.continue_to_parent)
//    GradientTextView continueToParent;
//    @BindView(R.id.forgot_pin)
//    GradientTextView forgotPin;
//    Unbinder unbinder;
//    private ApiService mApiService;
//
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        Dialog dialog = super.onCreateDialog(savedInstanceState);
//        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//        dialog.setCancelable(false);
//        dialog.setCanceledOnTouchOutside(false);
//        return dialog;
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        Dialog dialog = getDialog();
//        if (dialog != null) {
//            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        }
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View inflate = inflater.inflate(R.layout.create_pin_fragment, container, false);
//        unbinder = ButterKnife.bind(this, inflate);
//        close.setVisibility(View.GONE);
//        back.setVisibility(View.GONE);
//        header.setText("Enter PIN");
//        forgotPin.setVisibility(View.GONE);
//        return inflate;
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        mApiService = new RestClient(getActivity()).getApiService();
//        KeyboardUtils.addKeyboardToggleListener(getActivity(), new KeyboardUtils.SoftKeyboardToggleListener() {
//            @Override
//            public void onToggleSoftKeyboard(boolean isVisible) {
//                if (isVisible) {
//                    Helper.fullScreenView(getActivity());
//                } else {
//                    if (Helper.isKeyboardVisible(getActivity())) {
//                        Helper.dismissKeyboard(getActivity());
//                    }
//                }
//            }
//        });
//        setUpPinViews();
//        message.setVisibility(View.GONE);
//        continueToParent.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                verifyPin();
//            }
//        });
//    }
//
//    private void verifyPin() {
//        String parentalPin = getArguments().getString(Constants.PARENTAL_PIN);
//        if (!TextUtils.isEmpty(parentalPin)) {
//            if (parentalPin.equalsIgnoreCase(getFinalPin())) {
//                Parcelable parcelable = getArguments().getParcelable(Constants.SELECTED_PROFILE);
//                assert parcelable != null;
//                changeUser((Profile) parcelable);
//            } else {
//                Helper.showToast(getActivity(), "PIN Entered is Incorrect", R.drawable.ic_cross);
//            }
//        } else {
//            Helper.showToast(getActivity(), "PIN Entered is Incorrect", R.drawable.ic_cross);
//        }
//    }
//
//    private String getFinalPin() {
//        String pin1 = this.pin1.getText().toString();
//        String pin2 = this.pin2.getText().toString();
//        String pin3 = this.pin3.getText().toString();
//        String pin4 = this.pin4.getText().toString();
//        String finalFin = pin1 + pin2 + pin3 + pin4;
//        return finalFin;
//    }
//
//    private void changeUser(Profile pf) {
//        UpdateProfileRequest updateProfileRequest = new UpdateProfileRequest();
//        updateProfileRequest.setAuthToken(Constants.API_KEY);
//        Profile profile = new Profile();
//        profile.setProfileId(pf.getProfileId());
//        updateProfileRequest.setUserProfile(profile);
//        Helper.showProgressDialog(getActivity());
//
//        mApiService.assignProfile(PreferenceHandler.getSessionId(getActivity()), updateProfileRequest)
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<JsonObject>() {
//                    @Override
//                    public void call(JsonObject jsonObject) {
//                        //dismiss();
//                        PreferenceHandler.setCurrentProfileName(getActivity(), profile.getFirstname());
//                        PreferenceHandler.setCurrentProfileID(getActivity(), profile.getProfileId());
//                        if (pf.isChild()) {
//                            PreferenceHandler.setIsKidProfile(getActivity(), true);
//                        } else {
//                            PreferenceHandler.setIsKidProfile(getActivity(), false);
//                        }
//                        Helper.dismissProgressDialog();
//                        Intent intent = new Intent(getActivity(), MainActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        startActivity(new Intent(intent));
//                        getActivity().finish();
//                    }
//                }, new Action1<Throwable>() {
//                    @Override
//                    public void call(Throwable throwable) {
//                        throwable.printStackTrace();
//                        dismiss();
//                        Helper.dismissProgressDialog();
//                    }
//                });
//    }
//
//    private void setUpPinViews() {
//        pin5.addTextChangedListener(new GenericTextWatcher(pin5));
//
//        GenericOnKeyEvent OtpBackKeyListner = new GenericOnKeyEvent();
//
//        pin1.setOnKeyListener(OtpBackKeyListner);
//        pin2.setOnKeyListener(OtpBackKeyListner);
//        pin3.setOnKeyListener(OtpBackKeyListner);
//        pin4.setOnKeyListener(OtpBackKeyListner);
//        //pin5.setOnKeyListener(OtpBackKeyListner);
//
//        pin1.setOnFocusChangeListener(this);
//        pin2.setOnFocusChangeListener(this);
//        pin3.setOnFocusChangeListener(this);
//        pin4.setOnFocusChangeListener(this);
//
//    }
//
//    @Override
//    public void onFocusChange(View v, boolean hasFocus) {
//        final int id = v.getId();
//        switch (id) {
//            case R.id.pin1:
//                if (hasFocus) {
//                    setFocus(pin5);
//                    showSoftKeyboard(pin5);
//                }
//                break;
//
//            case R.id.pin2:
//                if (hasFocus) {
//                    setFocus(pin5);
//                    showSoftKeyboard(pin5);
//                }
//                break;
//
//            case R.id.pin3:
//                if (hasFocus) {
//                    setFocus(pin5);
//                    showSoftKeyboard(pin5);
//                }
//                break;
//
//            case R.id.pin4:
//                if (hasFocus) {
//                    setFocus(pin5);
//                    showSoftKeyboard(pin5);
//                }
//                break;
//            default:
//                break;
//        }
//    }
//
//
//    public class GenericTextWatcher implements TextWatcher {
//        private View view;
//
//        private GenericTextWatcher(View view) {
//            this.view = view;
//        }
//
//        @Override
//        public void afterTextChanged(Editable editable) {
//            // TODO Auto-generated method stub
//            String text = editable.toString();
//
//            if (!text.equals("")) {
//
//                switch (view.getId()) {
//                    case R.id.pin1:
//                        if (text.length() == 1)
//                            pin2.requestFocus();
//                        break;
//                    case R.id.pin2:
//                        if (text.length() == 1)
//                            pin3.requestFocus();
//                        break;
//                    case R.id.pin3:
//                        if (text.length() == 1)
//                            pin4.requestFocus();
//                        break;
//                    case R.id.pin4:
//                        break;
//                }
//            }
//        }
//
//        @Override
//        public void beforeTextChanged(CharSequence s, int start, int before, int count) {
//            // TODO Auto-generated method stub
//        }
//
//        @Override
//        public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
//            // TODO Auto-generated method stub
//            if (s.length() == 0) {
//                pin1.setText("");
//            } else if (s.length() == 1) {
//                pin1.setText(s.charAt(0) + "");
//                pin2.setText("");
//                pin3.setText("");
//                pin4.setText("");
//
//            } else if (s.length() == 2) {
//                pin2.setText(s.charAt(1) + "");
//                pin3.setText("");
//                pin4.setText("");
//
//            } else if (s.length() == 3) {
//                pin3.setText(s.charAt(2) + "");
//                pin4.setText("");
//
//            } else if (s.length() == 4) {
//                //setFocusedPinBackground(mPinFifthDigitEditText);
//                pin4.setText(s.charAt(3) + "");
//                Helper.dismissKeyboard(getActivity());
//            }
//        }
//    }
//
//    public class GenericOnKeyEvent implements View.OnKeyListener {
//
//        @Override
//        public boolean onKey(View v, int keyCode, KeyEvent event) {
//            if (event.getAction() == KeyEvent.ACTION_DOWN) {
//                final int id = v.getId();
//                switch (id) {
//                    case R.id.pin5:
//                        if (keyCode == KeyEvent.KEYCODE_DEL) {
//                            if (pin5.getText().length() == 5)
//                                pin4.setText("");
//                            else if (pin5.getText().length() == 4)
//                                pin3.setText("");
//                            else if (pin5.getText().length() == 3)
//                                pin2.setText("");
//                            else if (pin5.getText().length() == 2)
//                                pin1.setText("");
//
//                            if (pin5.length() > 0)
//                                pin5.setText(pin5.getText().subSequence(0, pin5.length() - 1));
//
//                            return true;
//                        }
//
//                        break;
//
//                    default:
//                        return false;
//                }
//            }
//            return false;
//        }
//    }
//
//    public static void setFocus(EditText editText) {
//        if (editText == null)
//            return;
//
//        editText.setFocusable(true);
//        editText.setFocusableInTouchMode(true);
//        editText.requestFocus();
//    }
//
//    public void showSoftKeyboard(EditText editText) {
//        if (editText == null)
//            return;
//
//        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Service.INPUT_METHOD_SERVICE);
//        imm.showSoftInput(editText, 0);
//    }
//
//    @Override
//    public void onDismiss(DialogInterface dialog) {
//        FragmentActivity activity = getActivity();
//        if (activity != null) {
//            getActivity().finish();
//            super.onDismiss(dialog);
//        }
//    }
}
