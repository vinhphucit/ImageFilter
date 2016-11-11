package com.roxwin.imagefilter.bases;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.roxwin.imagefilter.bus.BusCenter;
import com.roxwin.imagefilter.bus.SubscribleEvent;
import com.roxwin.imagefilter.utils.Toaster;
import com.squareup.otto.Subscribe;

import butterknife.ButterKnife;

/**
 * Created by PhucTV on 4/18/16.
 */
public abstract class BaseFragment extends Fragment {
    private static final String TAG = BaseFragment.class.getName();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutResource(), container, false);
        ButterKnife.inject(this, view);
        updateFollowingViewBinding();
        return view;
    }

    protected void updateFollowingViewBinding() {
        Log.v(TAG, "updateFollowingViewBinding : After views created");
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    public void onResume() {
        super.onResume();

    }

    @Override
    public void onStart() {
        super.onStart();
        BusCenter.getInstance().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        BusCenter.getInstance().unregister(this);
    }

    @Subscribe
    public void onNotify(SubscribleEvent event) {
        Log.d(TAG, "onOttoNotify event:" + event.event);
    }

    protected abstract String getSubclassName();

    public abstract int getLayoutResource();

    protected ActionBar getActionBar() {
        Activity localActivity = getActivity();
        if ((localActivity instanceof AppCompatActivity)) {
            return ((AppCompatActivity) localActivity).getSupportActionBar();
        }
        return null;
    }

    protected void popBackStackByFragmentTag(String fragmentTag) {
        Log.d(getSubclassName(), "popNowPlayingFragment called");
        ((BaseFragmentResponder) getActivity()).popBackStackByFragmentTag(fragmentTag);
    }

    protected void popFragment(BaseFragment fragment) {
        Log.d(getSubclassName(), (new StringBuilder()).append("popFragment called with tag: ").append(getSubclassName()).toString());
        BaseFragmentResponder basefragmentresponder = (BaseFragmentResponder) getActivity();
        String subclassName = getSubclassName();
        StringBuilder stringbuilder = (new StringBuilder()).append("popFragment -- baseFramentResponder == null? ");
        boolean isBaseFragmentNull;
        if (basefragmentresponder == null) {
            isBaseFragmentNull = true;
        } else {
            isBaseFragmentNull = false;
        }
        Log.d(subclassName, stringbuilder.append(isBaseFragmentNull).toString());
        if (basefragmentresponder != null) {
            basefragmentresponder.popFragment(fragment);
        }
    }

    public void showFragment(BaseFragment fragment) {
        Log.d(getSubclassName(), (new StringBuilder()).append("showFragment called with tag: ").append(getSubclassName()).toString());
        ((BaseFragmentResponder) getActivity()).showFragment(fragment, fragment.getSubclassName());
    }

    public void showToast(int i) {
        if (!isAdded()) {
            Log.w(TAG, "showToast - Fragment is not added; aborting showing Toast");
        } else {
            showToast(getResources().getString(i));
        }
    }

    public void showToast(String text) {
        showToast(text, Toaster.Duration.LONG);
    }

    protected void showToast(String text, Toaster.Duration duration) {
        if (getActivity() == null) {
            Log.e(TAG, "showToast - getActivity() is null; aborting showing Toast");
        } else {
            Toaster.showToast(getActivity(), text, duration);
        }
    }

    public static abstract interface BaseFragmentResponder {
        public abstract void showFragment(BaseFragment fragment, String fragmentTag);

        public abstract void popFragment(BaseFragment fragment);

        public abstract void popBackStackByFragmentTag(String fragmentTag);
    }
}
