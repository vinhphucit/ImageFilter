package com.roxwin.imagefilter.utils;

import android.content.res.AssetManager;
import android.graphics.Typeface;

import java.lang.ref.SoftReference;
import java.util.HashMap;

public final class TypefaceUtils {
    private static final HashMap<String, SoftReference<Typeface>> S_TYPE_CACHE;

    static {
        S_TYPE_CACHE = new HashMap();
    }

    private TypefaceUtils() {
    }

    public static Typeface createFromAsset(AssetManager assets, String fontname) {
        SoftReference<Typeface> cachedFont = getFromCache(fontname);
        if (cachedFont != null && cachedFont.get() != null) {
            return (Typeface) cachedFont.get();
        }
        Typeface result = Typeface.createFromAsset(assets, fontname);
        putIntoCache(fontname, result);
        return result;
    }

    private static SoftReference<Typeface> getFromCache(String fontname) {
        SoftReference<Typeface> softReference;
        synchronized (S_TYPE_CACHE) {
            softReference = (SoftReference) S_TYPE_CACHE.get(fontname);
        }
        return softReference;
    }

    private static void putIntoCache(String fontname, Typeface font) {
        synchronized (S_TYPE_CACHE) {
            S_TYPE_CACHE.put(fontname, new SoftReference(font));
        }
    }
}
