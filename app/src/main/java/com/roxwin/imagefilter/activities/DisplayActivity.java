package com.roxwin.imagefilter.activities;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.roxwin.imagefilter.Constant;
import com.roxwin.imagefilter.R;
import com.roxwin.imagefilter.adapters.EffectListAdapter;
import com.roxwin.imagefilter.bases.BaseActivity;
import com.roxwin.imagefilter.filters.FilterHelper;
import com.roxwin.imagefilter.filters.GPUImageFilter;
import com.roxwin.imagefilter.filters.GPUImageView;
import com.roxwin.imagefilter.filters.insta.InstaFilter;
import com.roxwin.imagefilter.filters.util.GPUImageFilterTools;
import com.roxwin.imagefilter.listeners.EffectListener;
import com.roxwin.imagefilter.models.EffectModel;
import com.roxwin.imagefilter.utils.CameraUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;

public class DisplayActivity extends BaseActivity {

    public static byte[] imageToShow;
    @InjectView(R.id.ivCaptureImage)
    ImageView ivCaptureImage;

    @InjectView(R.id.rvEffectImages)
    RecyclerView rvEffectImages;


    EffectListAdapter mAdapter;
    List<EffectModel> mEffects = new ArrayList<>();
    LinearLayoutManager mLayoutManager;

    private GPUImageView mGPUImageView;
    private GPUImageFilter mFilter;
    private GPUImageFilterTools.FilterAdjuster mFilterAdjuster;

    File fileImageSaved;

    private Bitmap orgBitmap;
    private Bitmap effectBitmap;
    private Bitmap smallEffectBitmap;

    private final int[] TITLES = {
            R.string.filter_normal,
            R.string.filter_amaro,
            R.string.filter_rise,
            R.string.filter_hudson,
            R.string.filter_xproii,
            R.string.filter_sierra,
            R.string.filter_lomo,
            R.string.filter_earlybird,
            R.string.filter_sutro,
            R.string.filter_toaster,
            R.string.filter_brannan,
            R.string.filter_inkwell,
            R.string.filter_walden,
            R.string.filter_hefe,
            R.string.filter_valencia,
            R.string.filter_nashville,
            R.string.filter_1977,
            R.string.filter_lordkelvin
    };

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_display;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void updateFollowingViewBinding() {
        super.updateFollowingViewBinding();
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        mGPUImageView = new GPUImageView(this);
        mGPUImageView.setImage(smallEffectBitmap);
        if (imageToShow == null) {
            finish();
        } else {
            BitmapFactory.Options opts = new BitmapFactory.Options();

            opts.inPurgeable = true;
            opts.inInputShareable = true;
            opts.inMutable = false;
            opts.inSampleSize = 2;
            orgBitmap = BitmapFactory.decodeByteArray(imageToShow,
                    0,
                    imageToShow.length,
                    opts);


            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            orgBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            //save image to footie folder
            fileImageSaved = CameraUtils.getOutputMediaFile();
            try {
                fileImageSaved.createNewFile();
                FileOutputStream fos = new FileOutputStream(fileImageSaved);
                orgBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


            BitmapFactory.Options smallOpts = new BitmapFactory.Options();
            smallOpts.inSampleSize = 3;
            smallEffectBitmap = BitmapFactory.decodeByteArray(byteArray,
                    0,
                    byteArray.length,
                    smallOpts);


            //make image square
            if (smallEffectBitmap.getWidth() >= smallEffectBitmap.getHeight()) {

                smallEffectBitmap = Bitmap.createBitmap(
                        smallEffectBitmap,
                        smallEffectBitmap.getWidth() / 2 - smallEffectBitmap.getHeight() / 2,
                        0,
                        smallEffectBitmap.getHeight(),
                        smallEffectBitmap.getHeight()
                );

            } else {

                smallEffectBitmap = Bitmap.createBitmap(
                        smallEffectBitmap,
                        0,
                        smallEffectBitmap.getHeight() / 2 - smallEffectBitmap.getWidth() / 2,
                        smallEffectBitmap.getWidth(),
                        smallEffectBitmap.getWidth()
                );
            }

            effectBitmap = putOverlay(orgBitmap, R.drawable.watermark);
            ivCaptureImage.setImageBitmap(effectBitmap);

            mGPUImageView = new GPUImageView(this);
            mGPUImageView.setImage(effectBitmap);

            imageToShow = null;

            //setup recycle view
            rvEffectImages.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            rvEffectImages.setLayoutManager(mLayoutManager);

            GPUImageView ivSmallCaptureImage = new GPUImageView(this);
            ivSmallCaptureImage.setImage(smallEffectBitmap);

            for (int i = 0; i < TITLES.length; i++) {
                InstaFilter filter = FilterHelper.getFilter(this, i);

                ivSmallCaptureImage.setFilter(filter);
                smallEffectBitmap = ivSmallCaptureImage.getGPUImage().getBitmapWithFilterApplied();
                EffectModel model = new EffectModel(getString(TITLES[i]), smallEffectBitmap, filter);
                if (i == 0)
                    model.setSelected(true);
                mEffects.add(model);
            }


            mAdapter = new EffectListAdapter(mEffects, new EffectListener() {
                @Override
                public void onItemClick(int position, EffectModel trip) {
                    mAdapter.setSelected(position);
                    switchFilterTo(position);
//                    effectBitmap = putOverlay(EffectImageUtils.applyStyle(bmImage, position + 1), R.drawable.watermark);
//                    ivCaptureImage.setImageBitmap(effectBitmap);
                }

            });
            rvEffectImages.setAdapter(mAdapter);
        }
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    public Bitmap putOverlay(Bitmap bitmap, int watermarkDrawble) {

        android.graphics.Bitmap.Config bitmapConfig =
                bitmap.getConfig();
        if (bitmapConfig == null) {
            bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
        }
        bitmap = bitmap.copy(bitmapConfig, true);
        Resources res = getResources();
        Bitmap bmWatermark = BitmapFactory.decodeResource(res, watermarkDrawble);

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);

        int x = (bitmap.getWidth() - bmWatermark.getWidth()) / 2;
        int y = bitmap.getHeight() - bmWatermark.getHeight();

        canvas.drawBitmap(bmWatermark, x, y, paint);

        return bitmap;
    }

    public Bitmap drawWatermark(Context gContext,
                                Bitmap bitmap,
                                String gText) {

        android.graphics.Bitmap.Config bitmapConfig =
                bitmap.getConfig();
        // set default bitmap config if none
        Resources resources = gContext.getResources();
        float scale = resources.getDisplayMetrics().density;

        if (bitmapConfig == null) {
            bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
        }
        // resource bitmaps are imutable,
        // so we need to convert it to mutable one
        bitmap = bitmap.copy(bitmapConfig, true);

        Canvas canvas = new Canvas(bitmap);
        // new antialised Paint
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // text color - #3D3D3D
        paint.setColor(Color.WHITE);
        // text size in pixels
        paint.setTextSize((int) (14 * scale));
        // text shadow
        paint.setShadowLayer(2f, 3f, 3f, Color.DKGRAY);

        // draw text to the Canvas center
        Rect bounds = new Rect();
        paint.getTextBounds(gText, 0, gText.length(), bounds);
        int x = (bitmap.getWidth() - bounds.width()) / 2;
        int y = bitmap.getHeight() - bounds.height();

        canvas.drawText(gText, x, y, paint);

        return bitmap;
    }

    private void switchFilterTo(final int position) {

        InstaFilter filter = FilterHelper.getFilter(this, position);

        mGPUImageView.setFilter(filter);
        ivCaptureImage.setImageBitmap(mGPUImageView.getGPUImage().getBitmapWithFilterApplied());
//        ivCaptureImage = ivSmallCaptureImage.getGPUImage().getBitmapWithFilterApplied();
//        if (mFilter == null
//                || (filter != null && !mFilter.getClass().equals(filter.getClass()))) {
//
//            mFilter = filter;
//            mGPUImageView.setFilter(mFilter);
//            mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(mFilter);
//
//            ivCaptureImage.setImageBitmap(mGPUImageView.getGPUImage().getBitmapWithFilterApplied());

//            if (mFilterAdjuster != null) {
//                mFilterAdjuster.adjust(50);
//            }
//            ivCaptureImage.requestRender();
//        }
    }

    @OnClick(R.id.btnContact)
    void onContactButtonClick() {
        try {
            Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constant.TEXT_WEBSITE));
            startActivity(myIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No application can handle this request."
                    + " Please install a webbrowser", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @OnClick(R.id.btnSave)
    void onSaveImage() {
        try {
            if (fileImageSaved == null || !fileImageSaved.exists())
                fileImageSaved.createNewFile();
            Bitmap bitmap = ((BitmapDrawable) ivCaptureImage.getDrawable()).getBitmap();
            CameraUtils.insertImage(getContentResolver(), bitmap, "", "");

            FileOutputStream fos = new FileOutputStream(fileImageSaved);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//            mGPUImageView.getGPUImage().getBitmapWithFilterApplied().compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.btnShare)
    void onShareEffectImage() {
        try {
            if (fileImageSaved == null || !fileImageSaved.exists())
                fileImageSaved.createNewFile();
            Bitmap bitmap = ((BitmapDrawable) ivCaptureImage.getDrawable()).getBitmap();
            CameraUtils.insertImage(getContentResolver(), bitmap, "", "");

            FileOutputStream fos = new FileOutputStream(fileImageSaved);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//            mGPUImageView.getGPUImage().getBitmapWithFilterApplied().compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Uri uriFromPath = Uri.fromFile(new File(fileImageSaved.getPath()));
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uriFromPath);
        shareIntent.setType("image/jpeg");
        startActivity(Intent.createChooser(shareIntent, "Share images to.."));
    }
}
