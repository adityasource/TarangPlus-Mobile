package com.otl.tarangplus.viewholders;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.otl.tarangplus.R;
import com.otl.tarangplus.Utils.Constants;
import com.otl.tarangplus.Utils.Helper;
import com.otl.tarangplus.Utils.ThumnailFetcher;
import com.otl.tarangplus.fragments.MovieDetailsFragment;
import com.otl.tarangplus.fragments.ShowDetailsPageFragment;
import com.otl.tarangplus.model.CatalogListItem;
import com.otl.tarangplus.model.Thumbnails;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BannerViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.premium)
    ImageView premiem;
    @BindView(R.id.live_tag)
    ImageView liveTag;
    @BindView(R.id.play_icon)
    ImageView play_icon;
    @BindView(R.id.parentPanel)
    CardView parentPanel;


    public BannerViewHolder(final View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        parentPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CatalogListItem clickedItem = (CatalogListItem) itemView.getTag();
                if (clickedItem != null && clickedItem.getTheme().equalsIgnoreCase("show")) {
                    ShowDetailsPageFragment fragment = new ShowDetailsPageFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.CONTENT_ID, clickedItem.getContentID());
                    bundle.putString(Constants.CATALOG_ID, clickedItem.getCatalogID());
                    bundle.putString(Constants.FRIENDLY_ID, clickedItem.getFriendlyId());
                    bundle.putString(Constants.DISPLAY_TITLE, clickedItem.getTitle());
                    fragment.setArguments(bundle);
                    Helper.addFragment((AppCompatActivity) itemView.getContext(), fragment, ShowDetailsPageFragment.TAG);
                } else if (clickedItem != null && clickedItem.getTheme().equalsIgnoreCase("movie")) {
                    MovieDetailsFragment fragment = new MovieDetailsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.CONTENT_ID, clickedItem.getContentID());
                    bundle.putString(Constants.CATALOG_ID, clickedItem.getCatalogID());
                    fragment.setArguments(bundle);
                    Helper.addFragment((AppCompatActivity) itemView.getContext(), fragment, MovieDetailsFragment.TAG);
                }
            }
        });
    }

    public void updateUI(CatalogListItem listItem, String layoutType) {
        if (listItem != null) {
            Thumbnails thumbnails = listItem.getThumbnails();
            assert listItem.getAccessControl() != null;
            if (listItem.getAccessControl() != null && listItem.getAccessControl().getIsFree()) {
                premiem.setVisibility(View.GONE);
            } else {
                boolean premiumTag = listItem.getAccessControl().isPremiumTag();
                if (premiumTag) {
                    premiem.setVisibility(View.VISIBLE);
                } else {
                    premiem.setVisibility(View.GONE);
                }
            }


            if (listItem.getTheme().equalsIgnoreCase(Constants.LIVE))
                liveTag.setVisibility(View.VISIBLE);
            else
                liveTag.setVisibility(View.GONE);


            String imageUrl = ThumnailFetcher.fetchAppropriateThumbnail(thumbnails, layoutType);
            if (!TextUtils.isEmpty(imageUrl)) {
                Picasso.get().load(imageUrl).placeholder(R.drawable.place_holder_16x9).into(this.image);
            } else {
                Picasso.get().load(R.color.colorAccent).into(this.image);
            }
        }
    }
}
