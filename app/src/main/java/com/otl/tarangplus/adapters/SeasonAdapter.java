package com.otl.tarangplus.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.otl.tarangplus.R;
import com.otl.tarangplus.customeUI.MyTextView;
import com.otl.tarangplus.fragments.ShowDetailsPageFragment;
import com.otl.tarangplus.interfaces.SeasonAdapterClickListner;
import com.otl.tarangplus.model.Item;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Aditya on 8/9/2019.
 */
public class SeasonAdapter extends RecyclerView.Adapter<SeasonAdapter.ViewHolder> {

    List<Item> items;
    Context context;
    SeasonAdapterClickListner seasonAdapterClickListner;
    public SeasonAdapter(List<Item> items, Context context, SeasonAdapterClickListner seasonAdapterClickListner) {
        this.items = items;
        this.context = context;
        this.seasonAdapterClickListner = seasonAdapterClickListner;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.season_items, null);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (items != null && items.size() != 0) {
            try {
                holder.title.setText(items.get(position).getTitle());
                holder.description.setText(items.get(position).getDescription());
                Glide.with(context)
                        .load(items.get(position).getThumbnails().getSmall169().getUrl())
                        .into(holder.image);

                holder.linearlayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = holder.getAdapterPosition();
                        Item item = items.get(pos);
                        seasonAdapterClickListner.OnseasonClick(item);
                    }
                });

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }


    @Override
    public int getItemCount() {
        return (null != items ? items.size() : 0);
    }

    public final static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.title)
        MyTextView title;
        @BindView(R.id.description)
        MyTextView description;
        @BindView(R.id.linearlayout)
        LinearLayout linearlayout;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
