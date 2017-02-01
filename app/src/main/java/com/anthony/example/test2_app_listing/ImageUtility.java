package com.anthony.example.test2_app_listing;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.util.LruCache;

/**
 * Created by anthony on 1/2/2017.
 */

public class ImageUtility {
    private LruCache<String, Bitmap> mMemoryCache;

    public void setupCache(){
        final int maxMemory = (int)(Runtime.getRuntime().maxMemory()/1024);
        final int cacheSize = maxMemory / 8;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount() / 1024;
            }
        };
    }

    public void bitmapToMemoryCache(String key, Bitmap bitmap){
        if (getBitmapFromMemCache(key) == null) mMemoryCache.put(key, bitmap);
    }

    public Bitmap getBitmapFromMemCache(String key){
        return mMemoryCache.get(key);
    }

    public static Bitmap createRoundedRectBitmap(Bitmap bitmap,
                                                  float topLeftCorner, float topRightCorner,
                                                  float bottomRightCorner, float bottomLeftCorner) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = Color.WHITE;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        Path path = new Path();
        float[] radii = new float[]{
                topLeftCorner, bottomLeftCorner,
                topRightCorner, topRightCorner,
                bottomRightCorner, bottomRightCorner,
                bottomLeftCorner, bottomLeftCorner
        };

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        path.addRoundRect(rectF, radii, Path.Direction.CW);
        canvas.drawPath(path, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }
}
