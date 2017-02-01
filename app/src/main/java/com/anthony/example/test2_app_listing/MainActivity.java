package com.anthony.example.test2_app_listing;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView listingRecyclerView;
    private RequestQueue requestQueue;
    private String TAG = "MainActivity";
    private ArrayList<ListingItem> top100Items;
    private ArrayList<ListingItem> grossingItems;
    private ListingAdapter adapter;
    private EditText appSearchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupLayout();
        setupClick();
        setupTool();
        topAppRequest();
        grossingAppRequest();
    }

    private void setupLayout(){
        listingRecyclerView = (RecyclerView)findViewById(R.id.listingRecyclerView);
        appSearchEditText = (EditText)findViewById(R.id.appSearchEditText); 
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        adapter = new ListingAdapter(this);
        listingRecyclerView.setLayoutManager(layoutManager);
        listingRecyclerView.setAdapter(adapter);
    }
    
    private void setupClick(){
        appSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchApp(s.toString());
            }
        });
    }

    private void searchApp(final String appName){
        ArrayList<ListingItem> preparedGrossingItems = new ArrayList<>();
        for (ListingItem item: grossingItems){
            if (item.getName().toLowerCase().contains(appName.toLowerCase())){
                preparedGrossingItems.add(item);
            }
        }
        adapter.updateGrossingForSearch(preparedGrossingItems);

        ArrayList<ListingItem> prepared100Items = new ArrayList<>();
        for (ListingItem item:top100Items){
            if (item.getName().toLowerCase().contains(appName.toLowerCase())){
                prepared100Items.add(item);
            }
        }
        adapter.updateTop00ForSearch(prepared100Items);

    }

    private void setupTool(){
        top100Items = new ArrayList<>();
        grossingItems = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);
    }

    private void topAppRequest(){
        requestQueue.add(VolleyGet.appListRequest("https://itunes.apple.com/hk/rss/topfreeapplications/limit=100/lang=zh/json", new VolleyGet.ResultListener(){
            @Override
            public void successOrFail(boolean isSuccess, String response) {
                if (isSuccess){
                    try {
                        JSONObject responseJSON = new JSONObject(response);
                        JSONObject feedJSON = responseJSON.getJSONObject("feed");
                        JSONArray entryJSONArray = feedJSON.getJSONArray("entry");
                        for(int i = 0, p = entryJSONArray.length(); i < p ; i++){
                            ListingItem item = new ListingItem();
                            item.setNumber(i);
                            item.setName(entryJSONArray.getJSONObject(i).getJSONObject("im:name").getString("label"));
                            item.setType(entryJSONArray.getJSONObject(i).getJSONObject("category").getJSONObject("attributes").getString("label"));
                            item.setIconLink(entryJSONArray.getJSONObject(i).getJSONArray("im:image").getJSONObject(1).getString("label"));
                            item.setId(entryJSONArray.getJSONObject(i).getJSONObject("id").getJSONObject("attributes").getString("im:id"));
                            top100Items.add(item);
                            appDetailRequest(item, i);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(MainActivity.this, "Please make sure that your device connected network", Toast.LENGTH_SHORT).show();
                }
            }
        }));
    }

    private void appDetailRequest(final ListingItem item, final int position){
        requestQueue.add(VolleyGet.appRatingRequest(item.getId(), new VolleyGet.ResultListener() {
            @Override
            public void successOrFail(boolean isSuccess, String response) {
                if (isSuccess){
                    try {
                        JSONObject responseJSON = new JSONObject(response);
                        JSONObject result = responseJSON.getJSONArray("results").getJSONObject(0);
                        double averageUserRating = result.getDouble("averageUserRating");
                        int userRatingCount = result.getInt("userRatingCount");
                        item.setAverageUserRating(averageUserRating);
                        item.setUserRatingCount(userRatingCount);
                        adapter.addItem(item);
                    } catch (JSONException e) {
                        adapter.addItem(item);
                    }
                }else {

                }
            }
        }));
    }

    private void grossingAppRequest(){
        requestQueue.add(VolleyGet.appListRequest("https://itunes.apple.com/hk/rss/topgrossingapplications/limit=10/lang=zh/json", new VolleyGet.ResultListener(){
            @Override
            public void successOrFail(boolean isSuccess, String response) {
                if (isSuccess){
                    try {
                        JSONObject responseJSON = new JSONObject(response);
                        JSONObject feedJSON = responseJSON.getJSONObject("feed");
                        JSONArray entryJSONArray = feedJSON.getJSONArray("entry");
                        for(int i = 0, p = entryJSONArray.length(); i < p ; i++){
                            ListingItem item = new ListingItem();
                            item.setNumber(i);
                            item.setName(entryJSONArray.getJSONObject(i).getJSONObject("im:name").getString("label"));
                            item.setType(entryJSONArray.getJSONObject(i).getJSONObject("category").getJSONObject("attributes").getString("label"));
                            item.setIconLink(entryJSONArray.getJSONObject(i).getJSONArray("im:image").getJSONObject(1).getString("label"));
                            item.setId(entryJSONArray.getJSONObject(i).getJSONObject("id").getJSONObject("attributes").getString("im:id"));
                            grossingItems.add(item);
                        }
                        adapter.addAllGrossing(grossingItems);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(MainActivity.this, "Please make sure that your device connected network", Toast.LENGTH_SHORT).show();
                }
            }
        }));
    }
}
