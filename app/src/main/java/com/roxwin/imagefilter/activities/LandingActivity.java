
package com.roxwin.imagefilter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.roxwin.imagefilter.R;
import com.roxwin.imagefilter.bases.BaseActivity;

import butterknife.InjectView;
import butterknife.OnClick;

public class LandingActivity extends BaseActivity {

    @InjectView(R.id.btnContinue)
    Button btnContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_landing;
    }

    @Override
    protected void updateFollowingViewBinding() {
        super.updateFollowingViewBinding();
    }

    @OnClick(R.id.btnContinue)
    void goToNextActivity() {
        showCamera(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(LandingActivity.this, CameraActivity.class);
                startActivity(intent);
                superfinish();
            }
        });

    }
    void superfinish(){
        finish();
    }
}
