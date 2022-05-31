package com.otl.tarangplus.viewholders;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.otl.tarangplus.customeUI.MyTextView;
import com.otl.tarangplus.R;
import com.otl.tarangplus.Utils.Constants;
import com.otl.tarangplus.Utils.Helper;
import com.otl.tarangplus.Utils.SpacesItemDecoration;
import com.otl.tarangplus.Utils.ThumnailFetcher;
import com.otl.tarangplus.model.CatalogListItem;
import com.otl.tarangplus.model.Item;
import com.otl.tarangplus.model.Thumbnails;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EpisodeItemViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.parentPanel)
    CardView parentPanel;
    @BindView(R.id.episode_meta_data)
    MyTextView episodeMetaData;
    @BindView(R.id.titleText)
    MyTextView titleText;
    @BindView(R.id.premium)
    ImageView premiem;

    public EpisodeItemViewHolder(final View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        parentPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

    public void updateUI(Item listItem) {
        if (listItem != null) {
            Thumbnails thumbnails = listItem.getThumbnails();
            String imageUrl = ThumnailFetcher.fetchAppropriateThumbnail(thumbnails, Constants.T_16_9_SMALL);
            if (!TextUtils.isEmpty(imageUrl)) {
                Picasso.get().load(imageUrl).placeholder(R.drawable.place_holder_16x9).into(this.image);
            } else {
                Picasso.get().load(R.color.colorAccent).into(this.image);
            }
            String title = listItem.getTitle();
            String displayTitle = listItem.getDisplayTitle();
            if (TextUtils.isEmpty(title)) {
                this.titleText.setVisibility(View.GONE);
            } else {
                this.titleText.setVisibility(View.VISIBLE);
                this.titleText.setText(title);
            }
            if (listItem != null && !TextUtils.isEmpty(listItem.getItemCaption()))
                this.episodeMetaData.setText(listItem.getItemCaption());

/*            // TODO: 28/09/18 to set episode_number
            String durationString = listItem.getDurationString();
            String episodeNumber = listItem.getEpisodeNumber();
            if (!TextUtils.isEmpty(durationString)) {
                //00:00:00
                String result = getDurationSeprateInHoursAndMins(durationString);
                if (!TextUtils.isEmpty(episodeNumber)) {
                    this.episodeMetaData.setText();
                }else {
                    this.episodeMetaData.setText(result);
                }
            }*/


            if (listItem.getAccessControl() != null && listItem.getAccessControl().getIsFree()) {
                this.premiem.setVisibility(View.GONE);
            } else {
                if (listItem.getAccessControl() != null) {
                    boolean premiumTag = listItem.getAccessControl().isPremiumTag();
                    if (premiumTag) {
                        this.premiem.setVisibility(View.VISIBLE);
                    } else {
                        this.premiem.setVisibility(View.GONE);
                    }
                }
            }
        }
    }


    private static String getDurationSeprateInHoursAndMins(String inputDuration) {
        String duration = null;
        if (TextUtils.isEmpty(inputDuration))
            return null;

        String[] inputDurationArray = inputDuration.split(":");
        if (inputDurationArray[0].equalsIgnoreCase("00")) {
            duration = inputDurationArray[1] + " min";
        } else if (inputDurationArray[1].equalsIgnoreCase("00")) {
            duration = inputDurationArray[0] + " hr";
        } else {
            duration = inputDurationArray[0] + " hr, " + inputDurationArray[1] + " min";
        }

        return duration;
    }

    public void setInitialPadding(int padding) {
        parentPanel.setLayoutParams(SpacesItemDecoration.addLeftMargin(padding));
    }
}
