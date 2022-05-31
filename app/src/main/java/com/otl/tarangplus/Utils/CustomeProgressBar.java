package com.otl.tarangplus.Utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;
import com.github.ybq.android.spinkit.style.MultiplePulse;
import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.otl.tarangplus.customeUI.PlayGifView;
import com.otl.tarangplus.R;

public class CustomeProgressBar {

    PlayGifView mLoadingImaeView;
    RelativeLayout backgroundLayout;
    Activity activity;
    View view;
    boolean isCancelled = true;
    boolean cancelable = false;
    private final Object lock = new Object();


    public CustomeProgressBar(Activity activity) {

        if (activity == null)
            return;

        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.custom_default_progress_dialog, null);
//        mLoadingImaeView = (PlayGifView) view.findViewById(R.id.loading_logo);
        backgroundLayout = (RelativeLayout) view.findViewById(R.id.root_layout);

        SpinKitView spinKitView = (SpinKitView) view.findViewById(R.id.spin_kit);

        ThreeBounce sprite = new ThreeBounce();
        sprite.setBounds(0, 0, 50, 50);
        sprite.setColor(activity.getResources().getColor(R.color.colorPrimaryDark));
        spinKitView.setIndeterminateDrawable(sprite);

//        mLoadingImaeView.setImageResource(R.drawable.loader_small);
        activity.addContentView(view, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        view.setVisibility(View.GONE);
        this.activity = activity;
//        lock = new Object();

        backgroundLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Remove once fixed
                //cancel();
            }
        });
    }

    public void setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
    }

    public void show() {
        try {
            synchronized (lock) {
                cancelable = false;
                isCancelled = false;
                view.setVisibility(View.VISIBLE);
                view.bringToFront();
            }
        } catch (Exception e) {

        }

    }

    public void dismiss() {
        cancel();
    }

    public boolean isShowing() {
        return view.getVisibility() == View.VISIBLE;
    }


    public void cancel() {
        try {
            synchronized (lock) {
                view.setVisibility(View.GONE);

                if (onCancelListener != null)
                    onCancelListener.onCancel(CustomeProgressBar.this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public OnCancelListener onCancelListener;

    public interface OnCancelListener {
        void onCancel(CustomeProgressBar dialog);
    }

    public void setOnCancelListener(OnCancelListener listener) {
        this.onCancelListener = listener;
    }


}
