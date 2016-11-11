//package com.roxwin.imagefilter.widgets;
//
//import android.content.Context;
//import android.content.res.TypedArray;
//import android.util.AttributeSet;
//import android.widget.TextView;
//
//import com.roxwin.imagefilter.utils.TypefaceUtils;
//
//public class AviaryTextView extends TextView {
//    public AviaryTextView(Context context) {
//        super(context);
//    }
//
//    public AviaryTextView(Context context, AttributeSet attrs) {
//        this(context, attrs, C0242R.attr.aviaryDefaultTextStyle);
//    }
//
//    public AviaryTextView(Context context, AttributeSet attrs, int defStyle) {
//        super(context, attrs, defStyle);
//        if (!isInEditMode()) {
//            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, C0242R.styleable.AviaryTextView, defStyle, 0);
//            setTypeface(a.getString(C0242R.styleable.AviaryTextView_aviary_typeface));
//            a.recycle();
//        }
//    }
//
//    public void setTypeface(String name) {
//        if (name != null) {
//            try {
//                setTypeface(TypefaceUtils.createFromAsset(getContext().getAssets(), name));
//            } catch (Throwable th) {
//            }
//        }
//    }
//
//    public void setTextAppearance(Context context, int resid) {
//        super.setTextAppearance(context, resid);
//    }
//}
