package com.otl.tarangplus.viewholders;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.otl.tarangplus.R;
import com.otl.tarangplus.Utils.Constants;
import com.otl.tarangplus.Utils.Helper;
import com.otl.tarangplus.Utils.SpacesItemDecoration;
import com.otl.tarangplus.Utils.ThumnailFetcher;
import com.otl.tarangplus.customeUI.MyTextView;
import com.otl.tarangplus.model.CatalogListItem;
import com.otl.tarangplus.model.Item;
import com.otl.tarangplus.model.Thumbnails;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SmallLayoutViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.titleText)
    TextView titleText;
    @BindView(R.id.parentPanel)
    CardView parentPanel;
    @BindView(R.id.premium)
    ImageView premiem;
    @BindView(R.id.live_tag)
    ImageView liveTag;

/*    @BindView(R.id.title)
    MyTextView title;
    @BindView(R.id.description)
    MyTextView description;*/

    public SmallLayoutViewHolder(final View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        parentPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                CatalogListItem clickedItem = (CatalogListItem) itemView.getTag();www
//                Helper.moveToPageBasedOnTheme((Activity) itemView.getContext(), clickedItem);
                if (itemView.getTag() instanceof CatalogListItem) {
                    CatalogListItem clickedItem = (CatalogListItem) itemView.getTag();
                    Helper.moveToPageBasedOnTheme((AppCompatActivity) itemView.getContext(), clickedItem);
                } else {
                    Item clickedItem = (Item) itemView.getTag();
                    listener.onItemClick(clickedItem);
                }
            }
        });

    }

    private ItemClickListener listener;

    public void setOnItemClickListener(ItemClickListener listener) {
        this.listener = listener;
    }

    public interface ItemClickListener {
        void onItemClick(Item item);
    }

    public void updateUI(CatalogListItem listItem, String layoutType) {
        if (listItem != null) {
            if (listItem.getAccessControl() != null && listItem.getAccessControl().getIsFree()) {
                boolean premiumTag = listItem.getAccessControl().isPremiumTag();
                if (!premiumTag) {
                    premiem.setVisibility(View.GONE);
                }
            } else {
                if (listItem.getAccessControl() != null) {
                    boolean premiumTag = listItem.getAccessControl().isPremiumTag();
                    if (premiumTag) {
                        premiem.setVisibility(View.VISIBLE);
                    }
                }
            }


            if (listItem.getTheme().equalsIgnoreCase(Constants.LIVE))
                liveTag.setVisibility(View.VISIBLE);
            else
                liveTag.setVisibility(View.GONE);


            Thumbnails thumbnails = listItem.getThumbnails();
            String imageUrl = ThumnailFetcher.fetchAppropriateThumbnail(thumbnails, layoutType);
            if (!TextUtils.isEmpty(imageUrl)) {
                Picasso.get().load(imageUrl).placeholder(R.drawable.place_holder_16x9).into(this.image);
            } else {
                Picasso.get().load(R.drawable.place_holder_16x9).placeholder(R.drawable.place_holder_16x9).into(this.image);
            }
            this.titleText.setText(listItem.getTitle());

//            title.setText(listItem.getTitle());
//            description.setText(listItem.getItemCaption());

        }
    }

    public void updateUI(Item listItem) {
        if (listItem != null) {
            if (listItem.getAccessControl() != null && listItem.getAccessControl().getIsFree()) {

                premiem.setVisibility(View.GONE);

            } else {
                assert listItem.getAccessControl() != null;
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


            Thumbnails thumbnails = listItem.getThumbnails();
            String imageUrl = ThumnailFetcher.fetchAppropriateThumbnail(thumbnails, Constants.T_16_9_SMALL_META_LAYOUT);
            if (!TextUtils.isEmpty(imageUrl)) {
                Picasso.get().load(imageUrl).placeholder(R.drawable.place_holder_16x9).into(this.image);
            } else {
                Picasso.get().load(R.drawable.place_holder_16x9).into(this.image);
            }

            if (!TextUtils.isEmpty(listItem.getTitle()))
                this.titleText.setText(listItem.getTitle());
        }
//        title.setText(listItem.getTitle());
//        description.setText(listItem.getItemCaption());
    }


    public void setInitialPadding(int padding) {
        parentPanel.setLayoutParams(SpacesItemDecoration.addLeftMargin(padding));
    }
}
