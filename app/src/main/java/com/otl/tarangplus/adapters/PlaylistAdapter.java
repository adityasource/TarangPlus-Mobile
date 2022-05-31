package com.otl.tarangplus.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.otl.tarangplus.R;
import com.otl.tarangplus.Utils.Constants;
import com.otl.tarangplus.Utils.Helper;
import com.otl.tarangplus.model.PlayListItem;
import com.otl.tarangplus.viewModel.PlayListViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class PlaylistAdapter extends RecyclerView.Adapter {

    Context context;
    List<PlayListItem> playListItemList = new ArrayList<>();

    public PlaylistAdapter(Context context, List<PlayListItem> playListItemList) {
        this.context = context;
        this.playListItemList = Helper.doDeepCopy(playListItemList);
    }

    public void updateList(List<PlayListItem> playListItemList) {
        this.playListItemList.clear();
        this.playListItemList = Helper.doDeepCopy(playListItemList);
        Constants.PLAYLISTITEMSFORCURRENTITEM = this.playListItemList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View playlistLO = LayoutInflater.from(context).inflate(R.layout.playlist_select, parent, false);
        return new PlayListViewHolder(playlistLO);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        PlayListViewHolder viewHolder = (PlayListViewHolder) holder;


        if (playListItemList != null && playListItemList.size() > 0) {
            PlayListItem singleItem = playListItemList.get(position);

            if (singleItem != null) {
                viewHolder.updateUI(singleItem);
                viewHolder.itemView.setTag(singleItem);

                viewHolder.setOnItemClickListener(new PlayListViewHolder.ItemClickListener() {
                    @Override
                    public void onItemClick(PlayListItem item) {
                        List<PlayListItem> playListItems = new ArrayList<>();
                        playListItems = Helper.doDeepCopy(playListItemList);
                        for (PlayListItem playListItemTemp : playListItems) {
                            if (item.getName().equalsIgnoreCase(playListItemTemp.getName())) {

                            } else {
                                playListItemTemp.setSelected(false);
                            }
                        }
                        updateList(playListItems);
                    }
                });
            }

        }
    }

    @Override
    public int getItemCount() {
        return playListItemList == null ? 0 : playListItemList.size();
    }
}
