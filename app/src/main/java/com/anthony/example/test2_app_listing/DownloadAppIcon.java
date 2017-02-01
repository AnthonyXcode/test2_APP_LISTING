package com.anthony.example.test2_app_listing;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.IOException;

/**
 * Created by anthony on 1/2/2017.
 */

public class DownloadAppIcon extends AsyncTask<Void, Void, Bitmap> {
    ImageView imageView;
    Context context;
    boolean isOdd;
    String url;
    ImageUtility imageUtility;
    private String TAG = "DownloadUserIcon";
    public DownloadAppIcon(ImageView imageView, Context context, boolean isOdd, String url, ImageUtility imageUtility) {
        this.imageView = imageView;
        this.context = context;
        this.isOdd = isOdd;
        this.url = url;
        this.imageUtility = imageUtility;
    }

    @Override
    protected void onPreExecute() {
        imageView.setImageBitmap(null);
        super.onPreExecute();
    }

    @Override
    protected Bitmap doInBackground(Void... voids) {
        Log.i(TAG, "doInBackground: " + url);
        try {
            return Picasso.with(context).load(url).get();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (bitmap != null){
            if (isOdd){
                bitmap = imageUtility.createRoundedRectBitmap(bitmap, 10, 10, 10, 10);
                imageUtility.bitmapToMemoryCache(url, bitmap);
                imageView.setImageBitmap(bitmap);
            }else {
                float radius = bitmap.getHeight()/2;
                bitmap = imageUtility.createRoundedRectBitmap(bitmap, radius, radius, radius, radius);
                imageUtility.bitmapToMemoryCache(url, bitmap);
                imageView.setImageBitmap(bitmap);
            }
        }
    }
}
