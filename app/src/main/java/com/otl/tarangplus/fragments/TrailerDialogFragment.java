package com.otl.tarangplus.fragments;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.gms.cast.framework.CastContext;
import com.otl.tarangplus.MainActivity;
import com.otl.tarangplus.R;
import com.otl.tarangplus.Utils.Constants;
import com.otl.tarangplus.Utils.Helper;
import com.otl.tarangplus.Utils.PreferenceHandler;
import com.saranyu.ott.instaplaysdk.DRM.InstaDrmContent;
import com.saranyu.ott.instaplaysdk.InstaMediaItem;
import com.saranyu.ott.instaplaysdk.InstaPlayView;
import com.saranyu.ott.instaplaysdk.ads.Adds;
import com.saranyu.ott.instaplaysdk.mediatracks.AudioTrack;
import com.saranyu.ott.instaplaysdk.mediatracks.CaptionTrack;
import com.saranyu.ott.instaplaysdk.mediatracks.VideoTrack;
import com.saranyu.ott.instaplaysdk.smarturl.SmartUrlFetcher2;
import com.saranyu.ott.instaplaysdk.smarturl.SmartUrlResponseV2;

import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.view.View.SYSTEM_UI_FLAG_FULLSCREEN;
import static android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
import static android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TrailerDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrailerDialogFragment extends Fragment {
    public static final String TAG = TrailerDialogFragment.class.getName();
    private Unbinder unbinder;
    String playUrl;
    String title;
    private boolean IS_SUBSCRIBED = true;
    private String adaptiveURL = "";
    private static String analytics_unique_id;
    private static long analytics_time_spent = 0;
    private long CUR_PROGRESS_TIME = 0;
    private String laURL = "";
    private final String drm_scheme = "widevine";
    private static boolean IS_CAST_CONNECTED = false;
    private CastContext mCastContext;
    @BindView(R.id.instaplay)
    InstaPlayView mInstaPlayView;
    @BindView(R.id.play_icon)
    ImageView mPlayIcon;
    @BindView(R.id.image)
    ImageView mImage;
    @BindView(R.id.player_container)
    RelativeLayout mPlayerContainer;
    private EventListener mEventListener;
    private Adds adds;
    private String ads;

    @SuppressLint("SourceLockedOrientationActivity")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.trailer_dialog, container, false);
        unbinder = ButterKnife.bind(this, v);
        playUrl = getArguments().getString(Constants.PLAY_URL);
        IS_SUBSCRIBED = /*getArguments().getBoolean(Constants.IS_SUBSCRIBED)*/true;
        title = getArguments().getString(Constants.TITLE);
        mPlayIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play();
            }
        });
//        mCastContext = CastContext.getSharedInstance((MainActivity) getActivity());
//        if (mCastContext != null)
//            if (mCastContext.getCastState() == 3 || mCastContext.getCastState() == 4) {
//                IS_CAST_CONNECTED = true;
//                if (getActivity() != null)
//                    (getActivity()).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//            } else {
//                IS_CAST_CONNECTED = false;
//                if (getActivity() != null)
//                    (getActivity()).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
//            }
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getSmartURL(playUrl);
    }

    public void getSmartURL(String playUrl) {
        SmartUrlFetcher2 urlFetcher;

        String smartUrl = playUrl;

        Helper.showProgressDialog(getActivity());

        urlFetcher = new SmartUrlFetcher2(smartUrl, "hls", Constants.PLAYER_AUTH_TOKEN);

        urlFetcher.setOnFinishListener(new SmartUrlFetcher2.SmartUrlFetcherFinishListener() {
            @Override
            public void onFinished(SmartUrlResponseV2 videoUrl) {

                if (videoUrl != null) {

                    adaptiveURL = Helper.getPlayBackURL(Constants.REGION, IS_SUBSCRIBED, videoUrl);
//                    Needed for analytics
                    analytics_unique_id = UUID.randomUUID().toString();
                    analytics_time_spent = 0;

                    Helper.dismissProgressDialog();
                    play();
                    //    mInstaPlayView.setFullscreenVisibility(true);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                adaptiveURL = "";
                Helper.dismissProgressDialog();
            }
        });

        urlFetcher.getVideoUrls();
    }

    private void play() {

        String urlType = playUrl;

        if (!mInstaPlayView.isPlayerActive()) {
            if (!TextUtils.isEmpty(adaptiveURL)) {
                setupPlayContent(adaptiveURL);
                mPlayIcon.setVisibility(View.GONE);
                mImage.setVisibility(View.GONE);
                //   viewHolder.mPremium.setVisibility(View.GONE);
                //viewHolder.mCommingSoon.setVisibility(View.GONE);
            }
        } else if (!TextUtils.isEmpty(adaptiveURL)) {
//            Log.e(TAG, "play: " + viewHolder.mInstaPlayView.getDuration());
            mInstaPlayView.seekTo(CUR_PROGRESS_TIME);
            mInstaPlayView.play();
            mPlayIcon.setVisibility(View.GONE);
            mImage.setVisibility(View.GONE);

            // viewHolder.mPremium.setVisibility(View.GONE);
            // viewHolder.mCommingSoon.setVisibility(View.GONE);
        } else {
            Helper.showToast(getActivity(), "Unable to Play! , Please try after some time", R.drawable.ic_error_icon);
        }

    }

    private void setupPlayContent(String adaptiveURL) {
        if (mInstaPlayView != null && mInstaPlayView.isPlaying()) {
            mInstaPlayView.stop();
            mInstaPlayView.release();
        }

        mEventListener = null;
        mEventListener = new EventListener();
        mInstaPlayView.addPlayerEventListener(mEventListener);
        mInstaPlayView.addPlayerUIListener(mEventListener);
        mInstaPlayView.addMediaTrackEventListener(mEventListener);
        mInstaPlayView.addAdEventListener(mEventListener);
        updateOrientationChange();

        InstaMediaItem mMediaItem;
        InstaDrmContent instaDrmContent = new InstaDrmContent(adaptiveURL, title, drm_scheme, laURL,
                PreferenceHandler.getUserId(getActivity()), null, false);

        mMediaItem = new InstaMediaItem(adaptiveURL);


        // mMediaItem.setInstaCastItem(getCastMediaItem(adaptiveURL));
        mInstaPlayView.setMediaItem(mMediaItem);
        mInstaPlayView.AutoPlayEnable(true);   // Added this to fix Ads playback
        mInstaPlayView.prepare();


        mInstaPlayView.setLanguageVisibility(false);
        mInstaPlayView.setCaptionVisibility(true);
        mInstaPlayView.setHDVisibility(false);

        mInstaPlayView.seekTo(CUR_PROGRESS_TIME);
        mInstaPlayView.setSeekBarVisibility(true);
        mInstaPlayView.setCurrentProgVisible(true);
        mInstaPlayView.setForwardTimeVisible(true);
        mInstaPlayView.setRewindVisible(true);
        mInstaPlayView.setDurationTimeVisible(true);
        mInstaPlayView.play();
        mInstaPlayView.setBufferVisibility(true);
        // mInstaPlayView.setVideoQualitySelectionText(fa);
        mInstaPlayView.setQualityVisibility(false);
        mInstaPlayView.setFullscreenVisibility(false);
        mInstaPlayView.setFullScreen(true);

    }

    private class EventListener implements
            InstaPlayView.PlayerEventListener,
            InstaPlayView.PlayerUIListener,
            InstaPlayView.MediaTrackEventListener,
            InstaPlayView.AdPlaybackEventListener {

        @Override
        public void OnError(int i, String s) {
            // mAnalytics.updateAnalytics(MEDIA_TYPE, AnalyticsProvider.EventName.error);
            //removeHeartBeatRunnable();

        }

        @Override
        public void OnCompleted() {

        }

        @Override
        public void OnStateChanged(int i) {

        }

        @Override
        public void OnTimeInfo(long l) {

        }

        @Override
        public void OnPositionChanged(long l, long l1) {

        }

        @Override
        public void OnPlay() {


        }

        @Override
        public void OnPaused() {

        }

        @Override
        public void OnBuffering() {

        }

        @Override
        public void OnReady() {

        }

        @Override
        public void OnCastConnected() {
            IS_CAST_CONNECTED = true;
        }

        @Override
        public void OnCastDisconnected() {

        }

        @Override
        public void OnMuteStateChanged(boolean b) {

        }

        @Override
        public void OnUIPlay() {

        }

        @Override
        public void OnUIPause() {

        }

        @Override
        public void OnUIFullScreen() {
            if (getActivity() != null)
                (getActivity()).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

            Helper.resetToSensorOrientation(getActivity());
        }

        @Override
        public void OnUIReplay() {

        }

        @Override
        public void OnUISeek() {

        }

        @Override
        public void OnUiTouch(boolean b) {

        }

        @Override
        public void OnUIShown() {
            try {

                if (getActivity() instanceof MainActivity) {
                    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        ((MainActivity) getActivity()).toolbar.setVisibility(View.VISIBLE);
                    }

                }

            } catch (Exception e) {
            }
        }

        @Override
        public void OnUIHidden() {

        }

        @Override
        public void OnVideoTracks(List<VideoTrack> list) {

        }

        @Override
        public void OnVideoTrackChanged(VideoTrack videoTrack) {
            Constants.CURRENT_BITRATE = videoTrack.bitrate;
        }

        @Override
        public void OnAudioTracks(List<AudioTrack> list) {

        }

        @Override
        public void OnAudioTrackChanged(AudioTrack audioTrack) {

        }

        @Override
        public void OnCaptionTracks(List<CaptionTrack> list) {

        }

        @Override
        public void OnCaptionTrackChanged(CaptionTrack captionTrack) {

        }

        @Override
        public void OnAdEvent(InstaPlayView.AdPlaybackEvent adPlaybackEvent) {

        }

        @Override
        public void OnAdError(InstaPlayView.AdPlayerErrorCode adPlayerErrorCode, String s) {

        }

        @Override
        public void OnAdProgress(float v, float v1) {
        }
    }

    public void updateOrientationChange() {

        Constants.donoWhyButNeeded(getActivity());
        showFullScreen();

        mInstaPlayView.setFullScreen(true);
        int navigationHight = Constants.getNavigationHight(getActivity());
        int deviceWidth;
        if (navigationHight > 0) {
            deviceWidth = Constants.getDeviceWidth(getActivity());
        } else {
            deviceWidth = Constants.getDeviceWidth(getActivity());
        }
        int deviceHeight = Constants.getDeviceHeight(getActivity());

        ViewGroup.LayoutParams layoutParams = mPlayerContainer.getLayoutParams();
        layoutParams.width = deviceWidth;
        layoutParams.height = deviceHeight;
        mPlayerContainer.setLayoutParams(layoutParams);

    }

    public void showFullScreen() {
        if (getActivity() != null) {
            View decorView = getActivity().getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    SYSTEM_UI_FLAG_LAYOUT_STABLE |
                            SYSTEM_UI_FLAG_FULLSCREEN |
                            SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                            SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                            SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }

    private void removeFullScreen() {

        View decorView = getActivity().getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            if (mInstaPlayView != null && mInstaPlayView.isPlaying())
                mInstaPlayView.pause();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        //removeFullScreen();
        showFullScreen();
//
//        if (!IS_CAST_CONNECTED && mInstaPlayView != null) {
//            mInstaPlayView.stop();
//            mInstaPlayView.release();
//            getActivity().onBackPressed();
//        }
//        try {
//            if (mInstaPlayView != null) {
//                mInstaPlayView.stop();
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (mInstaPlayView != null) {
                mInstaPlayView.stop();
                mInstaPlayView.release();
                //     getActivity().onBackPressed();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        showFullScreen();
    }
}
