package com.otl.tarangplus.viewholders;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.otl.tarangplus.R;
import com.otl.tarangplus.Utils.Constants;
import com.otl.tarangplus.Utils.Helper;
import com.otl.tarangplus.Utils.SpacesItemDecoration;
import com.otl.tarangplus.Utils.ThumnailFetcher;
import com.otl.tarangplus.model.CatalogListItem;
import com.otl.tarangplus.model.Thumbnails;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BigLayoutViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.parentPanel)
    CardView parentPanel;
    @BindView(R.id.premium)
    ImageView premiem;
    @BindView(R.id.live_tag)
    ImageView liveTag;


    public BigLayoutViewHolder(final View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        parentPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CatalogListItem clickedItem = (CatalogListItem) itemView.getTag();
                Helper.moveToPageBasedOnTheme((AppCompatActivity) itemView.getContext(), clickedItem);
            }
        });

    }

    public void updateUI(CatalogListItem listItem, String layoutType) {
        if (listItem != null) {
            Thumbnails thumbnails = listItem.getThumbnails();
            String imageUrl = ThumnailFetcher.fetchAppropriateThumbnail(thumbnails, layoutType);

            if (listItem.getAccessControl() != null) {
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
            }


            if (listItem.getTheme().equalsIgnoreCase(Constants.LIVE))
                liveTag.setVisibility(View.VISIBLE);
            else
                liveTag.setVisibility(View.GONE);

            if (!TextUtils.isEmpty(imageUrl)) {
                Picasso.get().load(imageUrl).placeholder(R.drawable.place_holder_16x9).into(this.image);
            } else {
                Picasso.get().load(R.color.colorAccent).into(this.image);
            }
        }
    }

    public void setInitialPadding(int padding) {
        parentPanel.setLayoutParams(SpacesItemDecoration.addLeftMargin(padding));
    }
}
