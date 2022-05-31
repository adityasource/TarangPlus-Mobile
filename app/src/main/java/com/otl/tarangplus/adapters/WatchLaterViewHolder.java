package com.otl.tarangplus.adapters;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.otl.tarangplus.Utils.Helper;
import com.otl.tarangplus.Utils.ThumnailFetcher;
import com.otl.tarangplus.model.CatalogListItem;
import com.otl.tarangplus.model.Item;
import com.otl.tarangplus.model.Thumbnails;
import com.otl.tarangplus.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WatchLaterViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.image)
    public ImageView image;
    @BindView(R.id.titleText)
    TextView titleText;
    @BindView(R.id.episode)
    TextView episode;
    @BindView(R.id.delete)
    ImageView delete;
    @BindView(R.id.premium)
    ImageView premium;

    @BindView(R.id.parentPanel)
    public CardView parentPanel;

    public WatchLaterViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        parentPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Object tag = itemView.getTag();
                if (tag instanceof Item) {
                    Item clickedItem = (Item) itemView.getTag();
                    Helper.moveToPageBasedOnTheme((AppCompatActivity) itemView.getContext(), clickedItem, "");
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Object tag = itemView.getTag();
                if (tag instanceof Item) {
                    if (listener != null) {
                        listener.onDeleteClick((Item) tag);
                    }
                }
            }
        });
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(Item item);
    }

    private OnDeleteClickListener listener;

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.listener = listener;
    }

    public void updateUI(CatalogListItem listItem, String viewType) {
        if (listItem != null) {
            Thumbnails thumbnails = listItem.getThumbnails();
            String imageUrl = ThumnailFetcher.fetchAppropriateThumbnail(thumbnails, viewType);
            if (!TextUtils.isEmpty(imageUrl)) {
                Picasso.get().load(imageUrl)
                        .placeholder(R.drawable.place_holder_16x9)
                        .error(R.drawable.place_holder_16x9)
                        .into(this.image);
            } else {
                Picasso.get().load(R.drawable.place_holder_16x9)
                        .error(R.drawable.place_holder_16x9)
                        .into(this.image);
            }

            String title = listItem.getTitle();
            if (!TextUtils.isEmpty(title)) {
                titleText.setText(title);
            }


            if (!TextUtils.isEmpty(listItem.getItemCaption())) {
                episode.setText(listItem.getItemCaption());
            }
        }
    }

    public void updateUI(Item listItem, String viewType) {
        if (listItem != null) {
            Thumbnails thumbnails = listItem.getThumbnails();
            String imageUrl = ThumnailFetcher.fetchAppropriateThumbnail(thumbnails, viewType);
            if (!TextUtils.isEmpty(imageUrl.trim())) {
                Picasso.get().load(imageUrl)
                        .placeholder(R.drawable.place_holder_16x9)
                        .error(R.drawable.place_holder_16x9)
                        .into(this.image);
            } else {
                Picasso.get().load(R.drawable.place_holder_16x9)
                        .error(R.drawable.place_holder_16x9)
                        .into(this.image);
            }

            String title = listItem.getTitle();
            if (!TextUtils.isEmpty(title)) {
                titleText.setText(title);
            }

            if (!TextUtils.isEmpty(listItem.getItemCaption())) {
                episode.setText(listItem.getItemCaption());
            }

            if(listItem.getAccessControl().isPremiumTag() && !listItem.getAccessControl().isFree()){
                premium.setVisibility(View.VISIBLE);
            }else {
                premium.setVisibility(View.GONE);
            }
        }
    }


}
