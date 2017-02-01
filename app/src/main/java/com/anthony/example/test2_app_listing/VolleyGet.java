package com.anthony.example.test2_app_listing;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

/**
 * Created by anthony on 1/2/2017.
 */

public class VolleyGet {
    private static final String TAG = "VolleyPost";
    public static StringRequest appListRequest(String url, final ResultListener resultListener){
        StringRequest jsonObjRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        resultListener.successOrFail(true, response);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                resultListener.successOrFail(false, error.toString());
            }
        });
        return jsonObjRequest;
    }

    public static StringRequest appRatingRequest(String appId, final ResultListener resultListener){
        String url = "https://itunes.apple.com/hk/lookup?id=" + appId + "&lang=zh";
        StringRequest jsonObjRequest = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        resultListener.successOrFail(true, response);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                resultListener.successOrFail(false, error.toString());
            }
        });
        return jsonObjRequest;
    }

    public interface ResultListener{
        public void successOrFail(boolean isSuccess, String response);
    }
}
