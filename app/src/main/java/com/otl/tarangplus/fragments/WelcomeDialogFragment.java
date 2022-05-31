package com.otl.tarangplus.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.otl.tarangplus.customeUI.GradientTextView;
import com.otl.tarangplus.customeUI.MyTextView;
import com.otl.tarangplus.MainActivity;
import com.otl.tarangplus.R;
import com.otl.tarangplus.Utils.Helper;
import com.otl.tarangplus.Utils.PreferenceHandler;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WelcomeDialogFragment extends DialogFragment {
    public static final String TAG = WelcomeDialogFragment.class.getClass().getName();

    View mRootView;
    @BindView(R.id.name)
    MyTextView mName;
    @BindView(R.id.subscribe_now)
    GradientTextView mSubScribeNow;
    @BindView(R.id.no_thanks)
    MyTextView mNoThanks;
    @BindView(R.id.txt_des)
    MyTextView mDecText;

    public WelcomeDialogFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.transaction_welcome_item, container, false);
        ButterKnife.bind(this, mRootView);

        mName.setText(getString(R.string.welcome) + " " + PreferenceHandler.getCurrentProfileName(getActivity()) + "!");
        PreferenceHandler.setIsLoggedIn(getActivity(), true);
        mDecText.setText("" +PreferenceHandler.getTextForPackDetails(getActivity()));

        mSubScribeNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SubscriptionWebViewFragment subscriptionWebViewFragment = new SubscriptionWebViewFragment();
//                Bundle bundle = new Bundle();
//                bundle.putString(Constants.WHO_INITIATED_THIS, TAG);
//                bundle.putString(Constants.FROM_WHERE, MePageFragment.TAG);
//                bundle.putBoolean(Constants.IS_LOGGED_IN, true);
//                subscriptionWebViewFragment.setArguments(bundle);
//                Helper.addFragmentForDetailsScreen(getActivity(), subscriptionWebViewFragment, SubscriptionWebViewFragment.TAG);
//                dismiss();
                if (getActivity() instanceof MainActivity) {
                    dismiss();
                    Helper.addFragment(getActivity(), new HomePageFragment(), HomePageFragment.TAG);
                } else {
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }

            }
        });

        mNoThanks.setOnClickListener(view -> {
            if (getActivity() instanceof MainActivity) {
                dismiss();
                Helper.addFragment(getActivity(), new HomePageFragment(), HomePageFragment.TAG);
            } else {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }

        });

        return mRootView;
    }

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

    @Override
    public void onDismiss(DialogInterface dialog) {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            getActivity().finish();
            super.onDismiss(dialog);
        }
    }
}
