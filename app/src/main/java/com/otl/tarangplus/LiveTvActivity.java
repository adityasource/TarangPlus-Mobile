package com.otl.tarangplus;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.exoplayer2.ui.PlayerView;
import com.vidgyor.livemidroll.vidgyorPlayerManager.PlayerManager;
import com.vidgyor.networkcheck.VidgyorNetworkManager;

import static com.vidgyor.livemidroll.Util.getDisplayMetrics;

public class LiveTvActivity extends AppCompatActivity {
    private static final String TAG = LiveTvActivity.class.getSimpleName() + "PD--";

    private PlayerView playerView;
    private PlayerManager player;
    private boolean isInLandscape = false;
    private ConstraintLayout parentPlayerView;
    private boolean isPlayerReleased;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_live_tv);

        String channelName = "tarangtv";

//Note: In case you want to call Odisha Tv channel, then use below code:
        //  String channelName = "odishatv";

       // playerView = findViewById(R.id.vid_player_view);
        player = new PlayerManager(LiveTvActivity.this, playerView, channelName);
        parentPlayerView = findViewById(R.id.constraintLayout);

        ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) parentPlayerView.getLayoutParams();
        lp.height = (int) (getDisplayMetrics(this).heightPixels * 0.32);
        parentPlayerView.setLayoutParams(lp);
        parentPlayerView.setMaxHeight((int) (getDisplayMetrics(this).heightPixels * 0.32));

        if (player != null) {
            player.isInLandscape(false, false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (player != null)
            player.resumePlayer();
    }

    @Override
        protected void onPause() {
        super.onPause();
        if (player != null)
            player.pausePlayer();
    }


    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        try {
            isPlayerReleased = true;
            if (player != null) {
                player.release();
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                finishAndRemoveTask();
            }
            VidgyorNetworkManager.from(this).stop();
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onDestroy();

    }


    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed");
        try {
            if (player != null && isInLandscape) {
                player.closeFullScreenMode(this, playerView, playerView.getOverlayFrameLayout());
                isInLandscape = false;

            } else {
                isPlayerReleased = true;
                if (player != null) {
                    player.release();
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAndRemoveTask();
                }
                VidgyorNetworkManager.from(this).stop();
                super.onBackPressed();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ConstraintLayout.LayoutParams landscapeParams = (ConstraintLayout.LayoutParams) parentPlayerView.getLayoutParams();

            landscapeParams.height = (int) (getDisplayMetrics(this).heightPixels);
            parentPlayerView.setLayoutParams(landscapeParams);
            parentPlayerView.setMaxHeight(getDisplayMetrics(this).heightPixels);

            hideSystemUI();
            isInLandscape = true;


            if (player != null) {
                player.isInLandscape(true, false);
            }


        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            ConstraintLayout.LayoutParams portraitParams = (ConstraintLayout.LayoutParams) parentPlayerView.getLayoutParams();
            portraitParams.height = (int) (getDisplayMetrics(this).heightPixels * 0.32);
            parentPlayerView.setLayoutParams(portraitParams);
            parentPlayerView.setMaxHeight((int) (getDisplayMetrics(this).heightPixels * 0.32));
            showSystemUI();
            if (player != null) {
                player.isInLandscape(false, false);
            }
            isInLandscape = false;
        }
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        //  | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }

}


