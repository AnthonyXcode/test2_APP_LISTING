package com.anthony.example.test2_app_listing;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by anthony on 1/2/2017.
 */

public class GrossingAdaper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    LayoutInflater layoutInflater;
    Context context;
    ImageUtility imageUtility;
    ArrayList<ListingItem> items;

    public GrossingAdaper(Context context, ArrayList<ListingItem> items) {
        this.context = context;
        this.items = items;
        imageUtility = new ImageUtility();
        imageUtility.setupCache();
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GrossingHolder(layoutInflater.inflate(R.layout.layout_item1_grossing, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ListingItem item = items.get(position);
        ((GrossingHolder)holder).grossingNameTxt.setText(item.getName());
        ((GrossingHolder)holder).grossingTypeTxt.setText(item.getType());
        setupIcon((GrossingHolder) holder, item);
    }

    private void setupIcon(GrossingHolder holder, ListingItem item){
        Bitmap appIconBitmap = imageUtility.getBitmapFromMemCache(item.getIconLink());
        if (appIconBitmap == null){
            DownloadAppIcon downloadUserIcon = new DownloadAppIcon(holder.grossingImg, context, true, item.getIconLink(), imageUtility);
            downloadUserIcon.execute();
        }else {
            holder.grossingImg.setImageBitmap(appIconBitmap);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private class GrossingHolder extends RecyclerView.ViewHolder{
        ImageView grossingImg;
        TextView grossingNameTxt;
        TextView grossingTypeTxt;
        public GrossingHolder(View itemView) {
            super(itemView);
            grossingImg = (ImageView)itemView.findViewById(R.id.grossingImg);
            grossingNameTxt = (TextView)itemView.findViewById(R.id.grossingNameTxt);
            grossingTypeTxt = (TextView)itemView.findViewById(R.id.grossingTypeTxt);
        }
    }
}
