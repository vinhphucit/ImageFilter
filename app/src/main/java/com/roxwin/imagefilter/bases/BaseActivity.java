package com.roxwin.imagefilter.bases;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.roxwin.imagefilter.R;
import com.roxwin.imagefilter.activities.StartupActivity;
import com.roxwin.imagefilter.bus.BusCenter;
import com.roxwin.imagefilter.utils.Toaster;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.Optional;

/**
 * Created by PhucTV on 4/18/16.
 */
public abstract class BaseActivity extends AppCompatActivity implements BaseFragment.BaseFragmentResponder, ActivityCompat.OnRequestPermissionsResultCallback {
    private static final String TAG = BaseActivity.class.getName();

    private static final int REQUEST_MULTIPLE_PERMISSION = 1;
    private static String[] REQUEST_PERMISSIONS = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private static boolean sResumed = false;

    private Runnable cameraRunnable;
    private Runnable writeStorageRunnable;

    @Optional
    @InjectView(R.id.root)
    View mLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TAG", "onCreate: " + getClass().getName());
        setContentView(getLayoutResource());
        ButterKnife.inject(this);

        updateFollowingViewBinding();

        if ((this instanceof StartupActivity) && !isTaskRoot()) {
            Intent intent = getIntent();
            String intentAction = intent.getAction();
            if (intent.hasCategory("android.intent.category.LAUNCHER") && intentAction != null && intentAction.equals("android.intent.action.MAIN")) {
                finish();
            }
        }


    }

    protected abstract int getLayoutResource();

    protected void updateFollowingViewBinding() {
        Log.v(TAG, "updateFollowingViewBinding : After views created");
    }

    @Override
    protected void onStart() {
        super.onStart();
        BusCenter.getInstance().unregister(this);
        Log.d(TAG, (new StringBuilder()).append("onStart: ").append(getClass().getName()).toString());
    }

    @Override
    protected void onStop() {
        super.onStop();
        BusCenter.getInstance().register(this);
        Log.d(TAG, (new StringBuilder()).append("onStop:").append(getClass().getName()).toString());
    }

    @Override
    protected void onPause() {
        super.onPause();
        sResumed = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        sResumed = true;
    }

    @Override
    protected void onPostResume() {
        Log.d(TAG, (new StringBuilder()).append("onPostResume: ").append(getClass().getName()).toString());
        super.onPostResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void showFragment(BaseFragment fragment, String fragmentTag) {
        Log.w(TAG, "showFragment - DEFAULT implementation called in BaseActivity");
    }

    @Override
    public void popFragment(BaseFragment fragment) {
        Log.w(TAG, "popFragment - DEFAULT implementation called in BaseActivity");
    }

    @Override
    public void popBackStackByFragmentTag(String fragmentTag) {
        Log.w(TAG, "popBackStackByFragmentTag - DEFAULT implementation called in BaseActivity");
    }

    protected void showToast(int resourceText) {
        showToast(getResources().getString(resourceText), Toaster.Duration.LONG);
    }

    protected void showToast(int resourceText, Toaster.Duration duration) {
        showToast(getResources().getString(resourceText), duration);
    }

    protected void showToast(String text) {
        showToast(text, Toaster.Duration.LONG);
    }

    protected void showToast(String text, Toaster.Duration duration) {
        Toaster.showToast(this, text, duration);
    }

    protected boolean checkAndRequestPermissions() {
        int permissionCamera = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);
        int permissionWriteStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionWriteStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (permissionCamera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_MULTIPLE_PERMISSION);
            return false;
        }
        return true;
    }

    /**
     * Called when the 'show camera' button is clicked.
     * Callback is defined in resource layout definition.
     */
    public void showCamera(Runnable callback) {
        Log.i(TAG, "Show contacts button pressed. Checking permissions.");
        cameraRunnable = callback;
        // Verify that all required contact permissions have been granted.
        if (checkAndRequestPermissions()) {
            if (callback != null)
                callback.run();

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.d(TAG, "Permission callback called-------");
        switch (requestCode) {
            case REQUEST_MULTIPLE_PERMISSION: {

                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Log.d(TAG, "sms & location services permission granted");
                        if (cameraRunnable != null) {
                            cameraRunnable.run();
                        }

                        // process the normal flow
                        //else any one or both the permissions are not granted
                    } else {
                        Log.d(TAG, "Some permissions are not granted ask again ");
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            showDialogOK("Camera and Write External Storage Permission required for this app",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    checkAndRequestPermissions();
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    // proceed with logic by disabling the related features or quit the app.
                                                    break;
                                            }
                                        }
                                    });
                        }
                        //permission is denied (and never ask again is  checked)
                        //shouldShowRequestPermissionRationale will return false
                        else {
                            Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_LONG)
                                    .show();
                            //                            //proceed with logic by disabling the related features or quit the app.
                        }
                    }
                }
            }
        }

    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }


}
