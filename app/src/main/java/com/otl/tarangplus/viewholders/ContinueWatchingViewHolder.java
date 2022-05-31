package com.otl.tarangplus.viewholders;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.otl.tarangplus.R;
import com.otl.tarangplus.Utils.Helper;
import com.otl.tarangplus.Utils.SpacesItemDecoration;
import com.otl.tarangplus.Utils.ThumnailFetcher;
import com.otl.tarangplus.fragments.ContinueWatchingFragment;
import com.otl.tarangplus.model.CatalogListItem;
import com.otl.tarangplus.model.Item;
import com.otl.tarangplus.model.Thumbnails;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContinueWatchingViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.image)
    public ImageView image;
    @BindView(R.id.titleText)
    TextView titleText;
    @BindView(R.id.episode)
    TextView episode;
    @BindView(R.id.delete)
    public ImageView delete;
    @BindView(R.id.parentPanel)
    public CardView parentPanel;
    @BindView(R.id.item_progress_bar)
    ProgressBar mProgressBar;

    private ItemClickListener listener;

    public void setOnItemClickListener(ItemClickListener listener) {
        this.listener = listener;
    }

    public interface ItemClickListener {
        void onItemClick(Item item);
    }

    public ContinueWatchingViewHolder(final View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        parentPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Item clickedItem = (Item) itemView.getTag();
                Helper.moveToPageBasedOnTheme((AppCompatActivity) itemView.getContext(), clickedItem,ContinueWatchingFragment.TAG);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Item clickedItem = (Item) itemView.getTag();
                listener.onItemClick(clickedItem);
            }
        });
    }

    public void updateUI(CatalogListItem listItem, String viewType) {
        if (listItem != null) {
            Thumbnails thumbnails = listItem.getThumbnails();
            String imageUrl = ThumnailFetcher.fetchAppropriateThumbnail(thumbnails, viewType);
            if (!TextUtils.isEmpty(imageUrl)) {
                Picasso.get().load(imageUrl).placeholder(R.drawable.place_holder_16x9).error(R.drawable.place_holder_16x9).into(this.image);
            } else {
                Picasso.get().load(R.drawable.place_holder_16x9).placeholder(R.drawable.place_holder_16x9).error(R.drawable.place_holder_16x9).into(this.image);
            }

            String title = listItem.getTitle();
            if (!TextUtils.isEmpty(title)) {
                titleText.setText(title);
            }


        }
    }

    public void updateUI(Item listItem, String viewType) {
        if (listItem != null) {
            Thumbnails thumbnails = listItem.getThumbnails();
            String imageUrl = ThumnailFetcher.fetchAppropriateThumbnail(thumbnails, viewType);
            if (!TextUtils.isEmpty(imageUrl)) {
                Picasso.get().load(imageUrl).placeholder(R.drawable.place_holder_16x9).error(R.drawable.place_holder_16x9).into(this.image);
            } else {
                Picasso.get().load(R.drawable.place_holder_16x9).placeholder(R.drawable.place_holder_16x9).error(R.drawable.place_holder_16x9).into(this.image);
            }


            if (!TextUtils.isEmpty(listItem.getTitle())) {
                titleText.setText(listItem.getTitle());
            }

            if (!TextUtils.isEmpty(listItem.getItemCaption())) {
                episode.setText(listItem.getItemCaption());
            }

            if (listItem.getTotalPercentage() > 0)
                mProgressBar.setProgress(listItem.getTotalPercentage());
            else
                mProgressBar.setProgress(1);
        }
    }

    public void setInitialPadding(int padding) {
        parentPanel.setLayoutParams(SpacesItemDecoration.addLeftMargin(padding));
    }
}
