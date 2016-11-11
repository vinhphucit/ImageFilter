package com.roxwin.imagefilter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.roxwin.imagefilter.R;
import com.roxwin.imagefilter.bases.BaseActivity;

public class StartupActivity extends BaseActivity {
    private static final int SPLASH_SCREEN_SHOW_TIME_MS = 4000;


    //    @InjectView(R.id.tvSplashWebsite)
//    TextView tvSplashWebsite;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_startup;
    }

    @Override
    protected void onResume() {
        super.onResume();
        goToFirstActivity();
    }

    @Override
    protected void updateFollowingViewBinding() {
        super.updateFollowingViewBinding();
//        tvSplashWebsite.setText(Constant.TEXT_WEBSITE);
    }

    private void goToFirstActivity() {
        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(StartupActivity.this, LandingActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_SCREEN_SHOW_TIME_MS);
    }
}
