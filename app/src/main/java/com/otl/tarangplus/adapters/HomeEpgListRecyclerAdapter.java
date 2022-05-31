package com.otl.tarangplus.adapters;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.otl.tarangplus.Utils.Constants;
import com.otl.tarangplus.Utils.Helper;
import com.otl.tarangplus.Utils.ThumnailFetcher;
import com.otl.tarangplus.customeUI.MyTextView;
import com.otl.tarangplus.fragments.LiveTvDetailsFragment;
import com.otl.tarangplus.model.CatalogListItem;
import com.otl.tarangplus.model.Program;
import com.otl.tarangplus.model.Thumbnails;
import com.otl.tarangplus.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeEpgListRecyclerAdapter extends RecyclerView.Adapter {

    private Activity context;
    private List<Program> itemList;
    private CatalogListItem item;

    public HomeEpgListRecyclerAdapter(Activity context, CatalogListItem listItem) {
        this.item = listItem;
        this.context = context;
        itemList = listItem.getPrograms();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View epgLayout = LayoutInflater.from(context).inflate(R.layout.t_16_9_epg_layout, parent, false);
        return new ViewHolder(epgLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        if (position == 0)
            viewHolder.premium.setVisibility(View.VISIBLE);
        else
            viewHolder.premium.setVisibility(View.GONE);

        if (position == 1) {
            viewHolder.mImageMask.setVisibility(View.VISIBLE);
            viewHolder.mImageMask.setImageResource(R.drawable.playing_next_dots);
        } else if (position == 0) {
            viewHolder.mImageMask.setVisibility(View.GONE);
        } else {
            viewHolder.mImageMask.setVisibility(View.VISIBLE);
           // viewHolder.mImageMask.setImageResource(R.drawable.upcoming_dots);
        }


        Program listItem = itemList.get(position);
        viewHolder.itemView.setTag(listItem);
        Thumbnails thumbnails = listItem.getThumbnails();
//        viewHolder.premium.setVisibility(View.GONE);
        String imageUrl = ThumnailFetcher.fetchAppropriateThumbnail(thumbnails, Constants.T_16_9_SMALL);
        if (!TextUtils.isEmpty(imageUrl)) {
            Picasso.get().load(imageUrl).placeholder(R.drawable.place_holder_16x9).into(viewHolder.image);
        } else {
            Picasso.get().load(R.color.colorAccent).into(viewHolder.image);
        }
        String title = listItem.getTitle();
        String estimatedTime = Constants.getEstimatedTime(listItem.getStartTime(), listItem.getEndTime());
        viewHolder.titleText.setText(title);

        if (!TextUtils.isEmpty(listItem.getItemCaption())) {
            viewHolder.episodeMetaData.setText(listItem.getItemCaption());
            viewHolder.episodeMetaData.setVisibility(View.VISIBLE);
        } else if (!TextUtils.isEmpty(estimatedTime)) {
            viewHolder.episodeMetaData.setText(estimatedTime);
            viewHolder.episodeMetaData.setVisibility(View.VISIBLE);
        } else {
            viewHolder.episodeMetaData.setText("");
            viewHolder.episodeMetaData.setVisibility(View.GONE);
        }


        viewHolder.parentPanel.setOnClickListener(view -> {
            if (position == 0) {
                LiveTvDetailsFragment fragment = new LiveTvDetailsFragment();
                Bundle bundle = new Bundle();
                bundle.putString(Constants.CATALOG_ID, item.getCatalogID());
                bundle.putString(Constants.CONTENT_ID, item.getContentID());
                bundle.putString(Constants.THEME, item.getTheme());
                bundle.putString(Constants.LAYOUT_TYPE_SELECTED, item.getCatalogObject().getLayoutType());
                bundle.putString(Constants.LAYOUT_SCHEME, item.getCatalogObject().getLayoutScheme());
                fragment.setArguments(bundle);
                Helper.addFragmentForDetailsScreen(context, fragment, LiveTvDetailsFragment.TAG);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (itemList != null) {
            return itemList.size();
        }
        return 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.play_icon)
        ImageView playIcon;
        @BindView(R.id.lyt)
        RelativeLayout lyt;
        @BindView(R.id.titleText)
        MyTextView titleText;
        @BindView(R.id.episode_meta_data)
        MyTextView episodeMetaData;
        @BindView(R.id.premium)
        ImageView premium;
        @BindView(R.id.parentPanel)
        CardView parentPanel;
        @BindView(R.id.image_mask)
        ImageView mImageMask;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
