package com.roxwin.imagefilter.utils;

import android.text.TextUtils;
import android.widget.ImageView;

import com.roxwin.imagefilter.AppApplication;
import com.squareup.picasso.Picasso;

/**
 * Created by PhucTV on 7/17/15.
 */
public class PicassoUtils {
    public static void setImage(String picUrl, ImageView view, int defaultIcon) {
        Picasso.with(AppApplication.getContext()).load(TextUtils.isEmpty(picUrl)?null:picUrl).placeholder(defaultIcon).error(AppApplication.getContext().getResources().getDrawable(defaultIcon)).into(view);
    }
}
