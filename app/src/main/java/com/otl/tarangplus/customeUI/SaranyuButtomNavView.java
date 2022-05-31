package com.otl.tarangplus.customeUI;

import android.content.Context;
import android.graphics.Typeface;
import androidx.appcompat.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.otl.tarangplus.Analytics.Analytics;
import com.otl.tarangplus.R;
import com.otl.tarangplus.Utils.Constants;
import com.otl.tarangplus.Utils.PreferenceHandler;
import com.otl.tarangplus.model.AppEvents;

public class SaranyuButtomNavView extends RelativeLayout implements View.OnClickListener {
    private AppCompatImageView mNavImgHome, mNavImgSearch, mNavImgTv, mNavImgMe;
    private static BottomItemClickEvent mListener;
    //private TextView mNavTxtHome, mNavTxtSearch, mNavTxtTv, mNavTxtMe;
    private Typeface semiBold, bold;
    private View mNavLytHome, mNavLytSearch, mNavLytTv, mNavLytMe;
    private static final String HOME = "HOME", SEARCH = "SEARCH", TV = "TV", ME = "ME";
    int dim = (int) getResources().getDimension(R.dimen.px_5);
    private AppEvents appEvents;
    private Analytics mAnalyticsEvent = Analytics.getInstance(getContext());

    public SaranyuButtomNavView(Context context) {
        super(context);
        init(context);
    }

    public SaranyuButtomNavView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SaranyuButtomNavView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setSelectedUI(String selectedTab) {
        switch (selectedTab) {
            case HOME:
                setHomeTabUI(dim);
                break;
            case SEARCH:
                setSearchTabUI(dim);
                break;
            case TV:
                setTVTabUI(dim);
                break;
            case ME:
                setMeTabUI(dim);
                break;
        }

    }

    public interface BottomItemClickEvent {
        void clickedItem(View view);
    }

    // Assign the listener implementing events interface that will receive the events
    public void setCustomObjectListener(BottomItemClickEvent listener) {
        mListener = (BottomItemClickEvent) listener;
    }

    public void init(Context context) {
        LayoutInflater mInflater = LayoutInflater.from(context);
        View v = mInflater.inflate(R.layout.bottom_navigation, this, true);

        mNavImgHome = v.findViewById(R.id.nav_img_home);
        mNavImgSearch = v.findViewById(R.id.nav_img_search);
        mNavImgTv = v.findViewById(R.id.nav_img_tv);
        mNavImgMe = v.findViewById(R.id.nav_img_me);

//        mNavTxtHome = v.findViewById(R.id.nav_txt_home);
//        mNavTxtSearch = v.findViewById(R.id.nav_txt_search);
//        mNavTxtTv = v.findViewById(R.id.nav_txt_tv);
//        mNavTxtMe = v.findViewById(R.id.nav_txt_me);

        mNavLytHome = v.findViewById(R.id.home_btn);
        mNavLytSearch = v.findViewById(R.id.home_search);
        mNavLytTv = v.findViewById(R.id.home_tv);
        mNavLytMe = v.findViewById(R.id.home_me);

        mNavLytHome.setOnClickListener(this);
        mNavLytSearch.setOnClickListener(this);
        mNavLytTv.setOnClickListener(this);
        mNavLytMe.setOnClickListener(this);

        semiBold = Typeface.createFromAsset(getContext().getAssets(), "fonts/" + getResources().getString(R.string.font_sans_semi_bold));
        bold = Typeface.createFromAsset(getContext().getAssets(), "fonts/" + getResources().getString(R.string.font_sans_extra_bold));


    }

    @Override
    public void onClick(View v) {
        mListener.clickedItem(v);
        switch (v.getId()) {
            case R.id.home_btn:
                setHomeTabUI(dim);
                appEvents = new AppEvents(1, "", "Home Screen", Constants.AN_APP_TYPE, "", Constants.PAGE_VISIT, "Home Screen",
                        PreferenceHandler.getUserState(getContext()), PreferenceHandler.getUserPeriod(getContext()),
                        PreferenceHandler.getUserPlanType(getContext()), PreferenceHandler.getUserId(getContext()));
                mAnalyticsEvent.logAppEvents(appEvents);
                break;

            case R.id.home_search:
                setSearchTabUI(dim);
                break;

            case R.id.home_tv:
                setTVTabUI(dim);
                break;

            case R.id.home_me:
                setMeTabUI(dim);
                break;

        }

    }

    private void setHomeTabUI(int dim) {
       // mNavImgHome.setPadding(0, 0, 0, 0);
        mNavImgSearch.setPadding(dim, dim, dim, dim);
        mNavImgTv.setPadding(dim, dim, dim, dim);
        mNavImgMe.setPadding(dim, dim, dim, dim);

        mNavImgHome.setImageResource(R.drawable.home_selected);
        mNavImgSearch.setImageResource(R.drawable.search_deselect);
        mNavImgTv.setImageResource(R.drawable.live_tv_deselect);
        mNavImgMe.setImageResource(R.drawable.more_deselect);
//
//        mNavTxtHome.setTypeface(bold);
//        mNavTxtSearch.setTypeface(semiBold);
//        mNavTxtTv.setTypeface(semiBold);
//        mNavTxtMe.setTypeface(semiBold);
//
//        mNavTxtHome.setTextColor(getResources().getColor(R.color.black));
//        mNavTxtSearch.setTextColor(getResources().getColor(R.color.txt_grey));
//        mNavTxtTv.setTextColor(getResources().getColor(R.color.txt_grey));
//        mNavTxtMe.setTextColor(getResources().getColor(R.color.txt_grey));

        mNavLytHome.setEnabled(true);
        mNavLytSearch.setEnabled(true);
        mNavLytTv.setEnabled(true);
        mNavLytMe.setEnabled(true);
    }


    private void setSearchTabUI(int dim) {
        mNavImgHome.setPadding(dim, dim, dim, dim);
        //mNavImgSearch.setPadding(0, 0, 0, 0);
        mNavImgTv.setPadding(dim, dim, dim, dim);
        mNavImgMe.setPadding(dim, dim, dim, dim);

        mNavImgHome.setImageResource(R.drawable.home_deselected);
        mNavImgSearch.setImageResource(R.drawable.search_select);
        mNavImgTv.setImageResource(R.drawable.live_tv_deselect);
        mNavImgMe.setImageResource(R.drawable.more_deselect);

//        mNavTxtHome.setTypeface(semiBold);
//        mNavTxtSearch.setTypeface(bold);
//        mNavTxtTv.setTypeface(semiBold);
//        mNavTxtMe.setTypeface(semiBold);
//
//        mNavTxtHome.setTextColor(getResources().getColor(R.color.txt_grey));
//        mNavTxtSearch.setTextColor(getResources().getColor(R.color.black));
//        mNavTxtTv.setTextColor(getResources().getColor(R.color.txt_grey));
//        mNavTxtMe.setTextColor(getResources().getColor(R.color.txt_grey));

        mNavLytHome.setEnabled(true);
        mNavLytSearch.setEnabled(false);
        mNavLytTv.setEnabled(true);
        mNavLytMe.setEnabled(true);
    }

    private void setTVTabUI(int dim) {
        mNavImgHome.setPadding(dim, dim, dim, dim);
        mNavImgSearch.setPadding(dim, dim, dim, dim);
        //mNavImgTv.setPadding(0, 0, 0, 0);
        mNavImgMe.setPadding(dim, dim, dim, dim);

        mNavImgHome.setImageResource(R.drawable.home_deselected);
        mNavImgSearch.setImageResource(R.drawable.search_deselect);
        mNavImgTv.setImageResource(R.drawable.live_tv_select);
        mNavImgMe.setImageResource(R.drawable.more_deselect);

//        mNavTxtHome.setTypeface(semiBold);
//        mNavTxtSearch.setTypeface(semiBold);
//        mNavTxtTv.setTypeface(bold);
//        mNavTxtMe.setTypeface(semiBold);
//
//        mNavTxtHome.setTextColor(getResources().getColor(R.color.txt_grey));
//        mNavTxtSearch.setTextColor(getResources().getColor(R.color.txt_grey));
//        mNavTxtTv.setTextColor(getResources().getColor(R.color.black));
//        mNavTxtMe.setTextColor(getResources().getColor(R.color.txt_grey));

        mNavLytHome.setEnabled(true);
        mNavLytSearch.setEnabled(true);
        mNavLytTv.setEnabled(false);
        mNavLytMe.setEnabled(true);
    }

    private void setMeTabUI(int dim) {
        mNavImgHome.setPadding(dim, dim, dim, dim);
        mNavImgSearch.setPadding(dim, dim, dim, dim);
        mNavImgTv.setPadding(dim, dim, dim, dim);
        //mNavImgMe.setPadding(0, 0, 0, 0);

        mNavImgHome.setImageResource(R.drawable.home_deselected);
        mNavImgSearch.setImageResource(R.drawable.search_deselect);
        mNavImgTv.setImageResource(R.drawable.live_tv_deselect);
        mNavImgMe.setImageResource(R.drawable.more_select);

//        mNavTxtHome.setTypeface(semiBold);
//        mNavTxtSearch.setTypeface(semiBold);
//        mNavTxtTv.setTypeface(semiBold);
//        mNavTxtMe.setTypeface(bold);
//
//        mNavTxtHome.setTextColor(getResources().getColor(R.color.txt_grey));
//        mNavTxtSearch.setTextColor(getResources().getColor(R.color.txt_grey));
//        mNavTxtTv.setTextColor(getResources().getColor(R.color.txt_grey));
//        mNavTxtMe.setTextColor(getResources().getColor(R.color.black));

        mNavLytHome.setEnabled(true);
        mNavLytSearch.setEnabled(true);
        mNavLytTv.setEnabled(true);
        mNavLytMe.setEnabled(false);
    }
}
