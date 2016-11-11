package com.roxwin.imagefilter.activities;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.roxwin.imagefilter.R;
import com.roxwin.imagefilter.bases.BaseActivity;
import com.roxwin.imagefilter.fragments.FrontCameraFragment;

public class CameraActivity extends BaseActivity {
    FrontCameraFragment frag = null;
    private static final String TAG = CameraActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_camera;
    }

    @Override
    protected void updateFollowingViewBinding() {
        super.updateFollowingViewBinding();
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        frag =
                (FrontCameraFragment) getFragmentManager().findFragmentById(R.id.camera_preview);
    }


    public void takePicture(View v) {
        frag.takeSimplePicture();
    }
}
