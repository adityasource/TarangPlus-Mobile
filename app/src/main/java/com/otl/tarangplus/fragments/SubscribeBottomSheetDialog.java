package com.otl.tarangplus.fragments;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.otl.tarangplus.customeUI.GradientTextView;
import com.otl.tarangplus.customeUI.MyTextView;
import com.otl.tarangplus.R;
import com.otl.tarangplus.Utils.Constants;
import com.otl.tarangplus.Utils.Helper;
import com.otl.tarangplus.Utils.PreferenceHandler;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SubscribeBottomSheetDialog extends DialogFragment {

    private ViewHolder holder;
    private boolean isAccessLoginReq = true;
    private boolean loginstatus;

    public void updateStatus(boolean loginstatus) {
        this.loginstatus = loginstatus;
    }
    public SubscribeBottomSheetDialog() {

    }
    @Override
    public void onStart() {
        super.onStart();

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout((6 * width)/7,  LinearLayout.LayoutParams.WRAP_CONTENT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.bottom_sheet, container, false);
        holder = new ViewHolder(inflate);

        if (getArguments() != null)
            isAccessLoginReq = getArguments().getBoolean(Constants.ACCESS_CONTROL_IS_LOGIN_REG);

        updateUI();
        return inflate;
    }

    private void updateUI() {
        holder.popup_description.setText(getString(R.string.subscription_message));
       // holder.popup_title.setText(getString(R.string.subscribe_now));
      //  holder.popup_positive_button.setText(R.string.proceed);
        holder.popup_negetive_button.setText(R.string.cancel);
//        holder.otherMessage.setText(R.string.other_message);
        boolean loggedIn = PreferenceHandler.isLoggedIn(getActivity());

        if(loginstatus) {
            holder.popup_title.setText(R.string.subscription_header);
            holder.popup_description.setText(R.string.sub_now_text);
            holder.popup_positive_button.setText(R.string.subscribe_now_caps);
        }else {
            holder.popup_title.setText(R.string.login);
            holder.popup_description.setText(R.string.subscribe_now_text);
            holder.popup_positive_button.setText(R.string.login);
        }

       // holder.popup_description.setVisibility(View.GONE);
        holder.mAlreadyParent.setVisibility(View.GONE);
        holder.otherMessage.setVisibility(View.GONE);
        if (loggedIn) {
            holder.loginbut.setVisibility(View.GONE);
            holder.popup_positive_button.setVisibility(View.VISIBLE);
        }else{
            holder.loginbut.setVisibility(View.VISIBLE);
            holder.popup_positive_button.setVisibility(View.GONE);
        }
        holder.otherMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Helper.resetToSensorOrientationWithoutDelay(getActivity());
                listener.onLoginClicked();
                dismiss();
            }
        });
        holder.popup_positive_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Helper.resetToSensorOrientationWithoutDelay(getActivity());
                dismiss();
                if (listener != null)
                listener.onSubscribeClick();
            }
        });
        holder.popup_negetive_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Helper.resetToSensorOrientationWithoutDelay(getActivity());
                dismiss();
                if (listener != null)
                    listener.onCancelClick();
            }
        });
        holder.loginbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Helper.resetToSensorOrientationWithoutDelay(getActivity());
                dismiss();
                if (listener != null)
                    listener.onClickLogin();

            }
        });
    }

    static class ViewHolder {
        @BindView(R.id.popup_title)
        MyTextView popup_title;
        @BindView(R.id.popup_description)
        MyTextView popup_description;
        @BindView(R.id.popup_negetive_button)
        MyTextView popup_negetive_button;
        @BindView(R.id.popup_positive_button)
        MyTextView popup_positive_button;
        @BindView(R.id.bottom_sheet)
        LinearLayout bottom_sheet;
        @BindView(R.id.other_message)
        MyTextView otherMessage;
        @BindView(R.id.already_parent)
        RelativeLayout mAlreadyParent;
        @BindView(R.id.down_already_text)
        MyTextView mDownAlready;
        @BindView(R.id.loginbut)
        MyTextView loginbut;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public interface BottomSheetClickListener {
        void onLoginClicked();

        void onSubscribeClick();

        void onCancelClick();

        void onClickLogin();
    }

    private BottomSheetClickListener listener;

    public void setBottomSheetClickListener(BottomSheetClickListener listener) {
        this.listener = listener;
    }


}
