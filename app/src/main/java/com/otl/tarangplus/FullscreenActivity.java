package com.otl.tarangplus;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.otl.tarangplus.Utils.Constants;
import com.otl.tarangplus.Utils.Helper;
import com.otl.tarangplus.fragments.TrailerDialogFragment;

import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {
    FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);
        frameLayout = findViewById(R.id.container);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        TrailerDialogFragment trailerDialogFragment = new TrailerDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("contentId", getIntent().getStringExtra("contentId"));
        bundle.putString("catalogId", getIntent().getStringExtra("catalogId"));
        bundle.getString(Constants.TITLE, getIntent().getStringExtra(Constants.TITLE));
        bundle.putString(Constants.PLAY_URL, getIntent().getStringExtra(Constants.PLAY_URL));
        trailerDialogFragment.setArguments(bundle);
        Helper.addFragmentForDetailsScreen(FullscreenActivity.this, trailerDialogFragment, TrailerDialogFragment.TAG);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }
}