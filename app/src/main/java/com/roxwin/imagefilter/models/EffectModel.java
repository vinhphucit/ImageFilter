package com.roxwin.imagefilter.models;

import android.graphics.Bitmap;

import com.roxwin.imagefilter.filters.insta.InstaFilter;
import com.roxwin.imagefilter.filters.util.GPUImageFilterTools;

/**
 * Created by PhucTV on 4/19/16.
 */
public class EffectModel {
    private String effectName;
    private Bitmap effectPreview;
    private InstaFilter filterType;
    private boolean selected;
    public EffectModel(String effectName, Bitmap effectPreview, InstaFilter filterType) {
        this.effectName = effectName;
        this.effectPreview = effectPreview;
        this.filterType = filterType;
    }
    public boolean isSelected() {
        return selected;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getEffectName() {
        return effectName;
    }

    public void setEffectName(String effectName) {
        this.effectName = effectName;
    }

    public Bitmap getEffectPreview() {
        return effectPreview;
    }

    public InstaFilter getFilterType() {
        return filterType;
    }

    public void setFilterType(InstaFilter filterType) {
        this.filterType = filterType;
    }

    public void setEffectPreview(Bitmap effectPreview) {
        this.effectPreview = effectPreview;
    }
}
