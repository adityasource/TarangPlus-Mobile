package com.otl.tarangplus.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.JsonObject;
import com.otl.tarangplus.MainActivity;
import com.otl.tarangplus.OnBoardingActivity;
import com.otl.tarangplus.R;
import com.otl.tarangplus.SplashScreenActivity;
import com.otl.tarangplus.Utils.Constants;
import com.otl.tarangplus.Utils.Helper;
import com.otl.tarangplus.Utils.PreferenceHandler;
import com.otl.tarangplus.Utils.SpacesItemDecoration;
import com.otl.tarangplus.customeUI.GradientTextView;
import com.otl.tarangplus.customeUI.MyTextView;
import com.otl.tarangplus.fragments.EditProfilesFragment;
import com.otl.tarangplus.fragments.HomePageFragment;
import com.otl.tarangplus.fragments.LoginFragment;
import com.otl.tarangplus.fragments.OtpScreen;
import com.otl.tarangplus.fragments.VerifyPinFragment;
import com.otl.tarangplus.fragments.WelcomeDialogFragment;
import com.otl.tarangplus.model.Data;
import com.otl.tarangplus.model.DataError;
import com.otl.tarangplus.model.ListResonse;
import com.otl.tarangplus.model.Profile;
import com.otl.tarangplus.model.ProfileData;
import com.otl.tarangplus.model.UpdateProfileRequest;
import com.otl.tarangplus.rest.ApiService;
import com.otl.tarangplus.rest.RestClient;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class WhoIsWatchingRegistrationDialog extends DialogFragment {

    public static final String TAG = WhoIsWatchingRegistrationDialog.class.getSimpleName();
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
    @BindView(R.id.text_who_is_watching)
    MyTextView textWhoIsWatching;
    @BindView(R.id.members_container)
    RelativeLayout membersContainer;
    @BindView(R.id.who_is_watching_list)
    RecyclerView whoIsWatchingList;
    @BindView(R.id.proceed)
    GradientTextView proceed;

    private ApiService mApiService;
    private List<Profile> mListItems;
    private MyWhoIsWatchingAdapter adapter;
    private Profile selectedProfile;
    private boolean isAddProfileSelected = false;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.who_is_watching, container, false);
        ButterKnife.bind(this, inflate);
        return inflate;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        header.setText(getString(R.string.who_is_watching_header));
        mApiService = new RestClient(getActivity()).getApiService();
        close.setVisibility(View.INVISIBLE);
        back.setVisibility(View.INVISIBLE);

        if (getArguments() != null) {
            String from_tag = getArguments().getString(Constants.FROM_PAGE);
            if (from_tag != null && from_tag.equalsIgnoreCase(SplashScreenActivity.TAG) ||
                    from_tag.equalsIgnoreCase(LoginFragment.TAG)) {
                proceed.setVisibility(View.GONE);
            } else {
                proceed.setVisibility(View.VISIBLE);
            }
        }


        getProfiles();
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListItems != null && mListItems.size() > 0) {
                    selectedProfile = mListItems.get(0);
                    PreferenceHandler.setCurrentProfileName(getActivity(), selectedProfile.getFirstname());
                    PreferenceHandler.setCurrentProfileID(getActivity(), selectedProfile.getProfileId());

                    WelcomeDialogFragment welcomeDialogFragment = new WelcomeDialogFragment();
                    welcomeDialogFragment.show(getFragmentManager(), WelcomeDialogFragment.TAG);

                }
            }
        });
    }

    private void setUpRecyclerView() {
        final GridLayoutManager linearLayoutManager = new GridLayoutManager(getActivity(), 2);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        whoIsWatchingList.setNestedScrollingEnabled(false);
        whoIsWatchingList.setHasFixedSize(true);
        whoIsWatchingList.setAdapter(adapter);
        whoIsWatchingList.addItemDecoration(new SpacesItemDecoration((int) getResources().getDimension(R.dimen.px_40), 0, 0, 0));
        whoIsWatchingList.setLayoutManager(linearLayoutManager);
    }

    private void getProfiles() {
        mListItems = new ArrayList<>();
        adapter = new MyWhoIsWatchingAdapter();
        setUpRecyclerView();
        Helper.showProgressDialog(getActivity());
        mApiService.getAllUsers(PreferenceHandler.getSessionId(getActivity()))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ProfileData>() {
                    @Override
                    public void call(ProfileData profileData) {
                        Helper.dismissProgressDialog();
                        mListItems = profileData.getData().getProfiles();
                        Profile profile = new Profile();
                        profile.setFirstname("New Profile");
                        mListItems.add(mListItems.size(), profile);
                        adapter.notifyDataSetChanged();
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
                            if (errorCode == 1016 && ((retrofit2.adapter.rxjava.HttpException) throwable).code() == 422) {
                            Helper.clearLoginDetails(getActivity());
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(new Intent(intent));
                            getActivity().finish();
                            Helper.showToast(getActivity(), errorMessage, R.drawable.ic_error_icon);
                            //ViewModelProviders.of(getActivity()).get(SearchPageViewModel.class).deleteAllSearHistory();
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
                        dismiss();
                        PreferenceHandler.setCurrentProfileName(getActivity(), profile.getFirstname());
                        PreferenceHandler.setCurrentProfileID(getActivity(), profile.getProfileId());
                        if (pf.isChild()) {
                            PreferenceHandler.setIsKidProfile(getActivity(), true);
                        } else {
                            PreferenceHandler.setIsKidProfile(getActivity(), false);
                        }
                        Helper.dismissProgressDialog();
                        if (getActivity() instanceof OnBoardingActivity)
                            getActivity().finish();
                        else
                            Helper.addWithoutBackStack(getActivity(), new HomePageFragment(), HomePageFragment.TAG);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        dismiss();
                        Helper.dismissProgressDialog();
                    }
                });
    }

    private class MyWhoIsWatchingAdapter extends RecyclerView.Adapter {

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View inflate = LayoutInflater.from(getActivity())
                    .inflate(R.layout.my_who_is_watching_item, parent, false);
            return new ViewHolder(inflate);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            ViewHolder mWhoIsWatchingItemViewHolder = (ViewHolder) holder;
            final Profile item = mListItems.get(position);
            Bundle arguments = getArguments();
            
            if (item.getFirstname().equalsIgnoreCase("new profile")) {
                mWhoIsWatchingItemViewHolder.mNameImageText.setText("+");
                mWhoIsWatchingItemViewHolder.mNameImageText.setTextColor(ContextCompat.getColor(getActivity(), R.color.profile_txt_grey));
                mWhoIsWatchingItemViewHolder.mNameImageText.setBackground(getActivity().getDrawable(R.drawable.place_holder_white_circle));
                mWhoIsWatchingItemViewHolder.mName.setText(item.getFirstname());
                mWhoIsWatchingItemViewHolder.mNameImageText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        isAddProfileSelected = true;
                        EditProfilesFragment fragment = new EditProfilesFragment();
                        Helper.addFragment(getActivity(), fragment, EditProfilesFragment.TAG);
                    }
                });
                if (arguments != null && (!TextUtils.isEmpty(arguments.getString(Constants.FROM_PAGE)) && !arguments.getString(Constants.FROM_PAGE).equalsIgnoreCase(OtpScreen.TAG))) {
                    mWhoIsWatchingItemViewHolder.mFullView.setVisibility(View.GONE);
                }
            } else {

                if (item.isChild()) {
                    mWhoIsWatchingItemViewHolder.mNameImageText.setBackground(getActivity().getDrawable(R.drawable.place_holder_kids_profile));
                } else {
                    mWhoIsWatchingItemViewHolder.mNameImageText.setBackground(getActivity().getDrawable(R.drawable.place_holder_circle));
                }
                mWhoIsWatchingItemViewHolder.mNameImageText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        As its registration fragment not needed
                    }
                });
                setTextAccordingToName(mWhoIsWatchingItemViewHolder, item);
            }
        }

        private void getUserDetails(Profile profile) {
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
//                                    VerifyPinDialog dialog = new VerifyPinDialog();
                                    VerifyPinFragment dialog = new VerifyPinFragment();
                                    Bundle bundle = new Bundle();
                                    bundle.putString(Constants.WHO_INITIATED_THIS, WhoIsWatchingRegistrationDialog.TAG);
                                    bundle.putString(Constants.PARENTAL_PIN, dataData.getParentalPin());
                                    bundle.putParcelable(Constants.SELECTED_PROFILE, profile);
                                    dialog.setArguments(bundle);
//                                    dialog.show(getFragmentManager(), VerifyPinDialog.TAG);
                                    Helper.addFragment(getActivity(), dialog, VerifyPinFragment.TAG);

                                    //dismiss();
                                } else {
                                    changeUser(profile);
                                }
                            } else {
                                changeUser(profile);
                            }

                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            throwable.printStackTrace();
                            Helper.dismissProgressDialog();
                        }
                    });
        }

        private void setTextAccordingToName(ViewHolder holder, Profile profile) {
            if (profile.getFirstname() != null && profile.getLastname() != null && profile.getLastname().trim().length() > 0) {
                holder.mName.setText(profile.getFirstname() + " " + profile.getLastname());
                holder.mNameImageText.setText(profile.getFirstname().substring(0, 1) + profile.getLastname().substring(0, 1));
            } else if (profile.getFirstname() != null) {
                holder.mName.setText(profile.getFirstname());
                if (profile.getFirstname().split(" ").length > 1) {
                    String[] splitName = profile.getFirstname().split(" ");
                    holder.mNameImageText.setText(splitName[0].substring(0, 1) + splitName[1].substring(0, 1));
                } else {
                    holder.mNameImageText.setText(profile.getFirstname().substring(0, 1));
                }
            } else if (profile.getLastname() != null) {
                holder.mName.setText(profile.getLastname());
                holder.mNameImageText.setText(profile.getLastname().substring(0, 1));
            }

        }

        @Override
        public int getItemCount() {
            if (mListItems != null) {
                return mListItems.size();
            }
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.name_image_text)
        MyTextView mNameImageText;
        @BindView(R.id.name)
        MyTextView mName;
        @BindView(R.id.parent_view)
        RelativeLayout mParentView;
        @BindView(R.id.full_view)
        RelativeLayout mFullView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            String fromPage = arguments.getString(Constants.FROM_PAGE);
            if (!TextUtils.isEmpty(fromPage) && fromPage.equalsIgnoreCase(OtpScreen.TAG)) {
                //do not do anything
            } else {
                FragmentActivity activity = getActivity();
                if (activity != null && isAddProfileSelected) {
                    getActivity().finish();
                    super.onDismiss(dialog);
                }
            }
        } else {
            FragmentActivity activity = getActivity();
            if (activity != null && isAddProfileSelected) {
                getActivity().finish();
                super.onDismiss(dialog);
            }
        }
    }
}
