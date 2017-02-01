package com.anthony.example.test2_app_listing;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by anthony on 1/2/2017.
 */

public class ListingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<ListingItem> top100Items;
    ArrayList<ListingItem> grossing10Items;
    LayoutInflater layoutInflater;
    private final static int TYPE_GROSSING = 0;
    private final static int TYPE_TOP = 1;
    private String TAG = "ListingAdapter";
    private ImageUtility imageUtility;
    private Context context;
    private int itemCount;

    public ListingAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        grossing10Items = new ArrayList<>();
        top100Items = new ArrayList<>();
        imageUtility = new ImageUtility();
        imageUtility.setupCache();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_GROSSING){
            GrossingHolder holder = new GrossingHolder(layoutInflater.inflate(R.layout.container_grossing, null));
            return holder;
        }else if (viewType == TYPE_TOP){
            Top100Holder holder = new Top100Holder(layoutInflater.inflate(R.layout.layout_item2_listing_top, null));
            return holder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof GrossingHolder){
            setupGrossingLayout((GrossingHolder) holder);
        }else if (holder instanceof Top100Holder){
            int itemPosition;
            if (grossing10Items.size() == 0){
                itemPosition = position;
            }else {
                itemPosition = position - 1;
            }
            setupTop100Layout((Top100Holder) holder,itemPosition);
        }
    }

    @Override
    public int getItemCount() {
        return setItemCountm();
    }

    @Override
    public int getItemViewType(int position) {
        if (grossing10Items.size() == 0){
            return TYPE_TOP;
        }else {
            if (position == 0) {
                return TYPE_GROSSING;
            } else {
                return TYPE_TOP;
            }
        }
    }

    private int setItemCountm(){
        if (grossing10Items.size() == 0){
            itemCount = top100Items.size();
        }else {
            itemCount = top100Items.size() + 1;
        }
        return itemCount;
    }

    GrossingAdaper grossingAdapter;
    LinearLayoutManager layoutManager;
    boolean grossingInitialized = false;
    private void setupGrossingLayout(GrossingHolder holder){
        if (grossing10Items.size() != 0) {
            if (!grossingInitialized) {
                grossingInitialized = true;
                if (grossingAdapter == null) grossingAdapter = new GrossingAdaper(context, grossing10Items);
                if (layoutManager == null) layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                holder.container.setVisibility(View.VISIBLE);
                holder.grossingRecylerView.setLayoutManager(layoutManager);
                holder.grossingRecylerView.setAdapter(grossingAdapter);
            }
        }else {
            grossingInitialized = false;
            holder.container.setVisibility(View.GONE);
        }
    }

    private void setupTop100Layout(Top100Holder holder, int position){
        ListingItem item = top100Items.get(position);
        holder.topNumbTxt.setText(String.valueOf(position + 1));
        holder.topAppNameTxt.setText(item.getName());
        holder.topTypeTxt.setText(item.getType());
        setStar(holder, item.getAverageUserRating());
        holder.ratingNumbTxt.setText(" (" + String.valueOf(item.getUserRatingCount()) + ")");
        setAPPImg(holder, item, position);
    }

    private void setAPPImg(Top100Holder holder, ListingItem item, int position){
        Bitmap appIconBitmap = imageUtility.getBitmapFromMemCache(item.getIconLink());
        if (appIconBitmap == null){
            boolean isOdd;
            if (position % 2 != 0){
                isOdd = false;
            }else {
                isOdd = true;
            }
            DownloadAppIcon downloadUserIcon = new DownloadAppIcon(holder.topImg, context, isOdd, item.getIconLink(), imageUtility);
            downloadUserIcon.execute();
        }else {
            holder.topImg.setImageBitmap(appIconBitmap);
        }
    }

    private void setStar(Top100Holder holder, double rating){
        holder.starImg1.setSelected(false);
        holder.starImg1.setPressed(false);
        holder.starImg2.setSelected(false);
        holder.starImg2.setPressed(false);
        holder.starImg3.setSelected(false);
        holder.starImg3.setPressed(false);
        holder.starImg4.setSelected(false);
        holder.starImg4.setPressed(false);
        holder.starImg5.setSelected(false);
        holder.starImg5.setPressed(false);
        if (rating >= 0.5)holder.starImg1.setPressed(true);
        if (rating >= 1.0) holder.starImg1.setSelected(true);
        if (rating >= 1.5) holder.starImg2.setPressed(true);
        if (rating >= 2.0) holder.starImg2.setSelected(true);
        if (rating >= 2.5) holder.starImg3.setPressed(true);
        if (rating >= 3.0) holder.starImg3.setSelected(true);
        if (rating >= 3.5) holder.starImg4.setPressed(true);
        if (rating >= 4.0) holder.starImg4.setSelected(true);
        if (rating >= 4.5) holder.starImg5.setPressed(true);
        if (rating >= 5.0) holder.starImg5.setSelected(true);
    }

    public void addAllGrossing(ArrayList<ListingItem> items){
        grossing10Items.addAll(items);
        notifyDataSetChanged();
    }

    public void addItem(ListingItem item){
        top100Items.add(item);
        notifyDataSetChanged();
    }

    public void updateTop00ForSearch(ArrayList<ListingItem> topItems){
        top100Items.clear();
        top100Items.addAll(topItems);
        notifyDataSetChanged();

    }

    public void updateGrossingForSearch(ArrayList<ListingItem> grossingItems){
        grossing10Items.clear();
        grossing10Items.addAll(grossingItems);
        notifyDataSetChanged();
        grossingAdapter.notifyDataSetChanged();
    }

    private class GrossingHolder extends RecyclerView.ViewHolder{
        private RecyclerView grossingRecylerView;
        View container;
        public GrossingHolder(View itemView) {
            super(itemView);
            grossingRecylerView = (RecyclerView)itemView.findViewById(R.id.grossingRecylerView);
            container = itemView;
        }
    }

    private class Top100Holder extends RecyclerView.ViewHolder{
        private TextView topNumbTxt;
        private ImageView topImg;
        private TextView topAppNameTxt;
        private TextView topTypeTxt;
        private ImageView starImg1;
        private ImageView starImg2;
        private ImageView starImg3;
        private ImageView starImg4;
        private ImageView starImg5;
        private TextView ratingNumbTxt;
        public Top100Holder(View itemView) {
            super(itemView);
            topNumbTxt = (TextView)itemView.findViewById(R.id.topNumbTxt);
            topImg = (ImageView)itemView.findViewById(R.id.topImg);
            topAppNameTxt = (TextView)itemView.findViewById(R.id.topAppNameTxt);
            topTypeTxt = (TextView)itemView.findViewById(R.id.topTypeTxt);
            starImg1 = (ImageView)itemView.findViewById(R.id.starImg1);
            starImg2 = (ImageView)itemView.findViewById(R.id.starImg2);
            starImg3 = (ImageView)itemView.findViewById(R.id.starImg3);
            starImg4 = (ImageView)itemView.findViewById(R.id.starImg4);
            starImg5 = (ImageView)itemView.findViewById(R.id.starImg5);
            ratingNumbTxt = (TextView)itemView.findViewById(R.id.ratingNumbTxt);
        }
    }
}
