package com.otl.tarangplus.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.otl.tarangplus.Utils.Constants;
import com.otl.tarangplus.model.CatalogListItem;
import com.otl.tarangplus.viewholders.BannerViewHolder;
import com.otl.tarangplus.viewholders.BigLayoutViewHolder;
import com.otl.tarangplus.viewholders.BigMetaLayoutViewHolder;
import com.otl.tarangplus.viewholders.ContinueWatchingViewHolder;
import com.otl.tarangplus.viewholders.LiveBannerViewHolder;
import com.otl.tarangplus.viewholders.MovieLayoutViewHolder;
import com.otl.tarangplus.viewholders.MovieStaticViewHolder;
import com.otl.tarangplus.viewholders.PlainLayoutViewHolder;
import com.otl.tarangplus.viewholders.PlayLayoutViewHolder;
import com.otl.tarangplus.viewholders.SmallLayoutViewHolder;
import com.otl.tarangplus.viewholders.SubscriptionViewHolder;
import com.otl.tarangplus.R;

import java.util.List;

public class HomeRecyclerViewAdapter extends RecyclerView.Adapter {

    private Context context;
    private CatalogListItem mItems;


    public HomeRecyclerViewAdapter(Context context, CatalogListItem item) {
        this.context = context;
        mItems = item;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch (viewType) {

            case T_CONTINUE_WATCHING_LAYOUT:
                View continueWatching = LayoutInflater.from(context).inflate(R.layout.t_continue_watching_item, parent, false);
                return new ContinueWatchingViewHolder(continueWatching);

            case T_2_3_MOVIE_LAYOUT:
                View movieLayout = LayoutInflater.from(context).inflate(R.layout.t_2_3_movie_static_item_meta, parent, false);
                return new MovieLayoutViewHolder(movieLayout);

            case T_16_9_BIG_LAYOUT:
                View bigLayout = LayoutInflater.from(context).inflate(R.layout.t_16_9_big, parent, false);
                return new BigLayoutViewHolder(bigLayout);

            case T_16_9_BIG_META_LAYOUT:
                View bigMetaLayout = LayoutInflater.from(context).inflate(R.layout.t_16_9_big_meta, parent, false);
                return new BigMetaLayoutViewHolder(bigMetaLayout);

            case T_16_9_SMALL_LAYOUT:
                View smallLayout = LayoutInflater.from(context).inflate(R.layout.t_16_9_small, parent, false);
                return new SmallLayoutViewHolder(smallLayout);

            case T_16_9_SMALL_META_LAYOUT:
                View smallMetaLayout = LayoutInflater.from(context).inflate(R.layout.t_16_9_small_meta, parent, false);
                return new SmallLayoutViewHolder(smallMetaLayout);

            case T_1_1_PLAIN_LAYOUT:
                View tPlainLayout = LayoutInflater.from(context).inflate(R.layout.t_1_1_album_meta, parent, false);
                return new PlainLayoutViewHolder(tPlainLayout);

            case T_1_1_ALBUM_LAYOUT:
                View inflate = LayoutInflater.from(context).inflate(R.layout.t_1_1_album, parent, false);
                return new PlayLayoutViewHolder(inflate);

            case T_16_9_BANNER:
                View bannerView = LayoutInflater.from(context).inflate(R.layout.t_16_9_banner, parent, false);
                return new BannerViewHolder(bannerView);

            case T_16_9_LIVEBANNER:
                View liveView = LayoutInflater.from(context).inflate(R.layout.t_16_9_small, parent, false);
                return new LiveBannerViewHolder(liveView);

            case T_2_3_MOVIE_STATIC_LAYOUT:
                View staticLayout = LayoutInflater.from(context).inflate(R.layout.t_2_3_movie_static_item, parent, false);
                return new MovieStaticViewHolder(staticLayout);

            case T_SUBSCRIPTION:
                View subscriptionLayout = LayoutInflater.from(context).inflate(R.layout.subscription_layout, parent, false);
                return new SubscriptionViewHolder(subscriptionLayout);

            default:
                View bigMetaLayout2 = LayoutInflater.from(context).inflate(R.layout.t_16_9_big_meta, parent, false);
                return new BigMetaLayoutViewHolder(bigMetaLayout2);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        List<CatalogListItem> listItems = mItems.getCatalogListItems();
        if (listItems != null && listItems.size() > 0) {
            CatalogListItem listItem = listItems.get(position);
            if (viewType == T_CONTINUE_WATCHING_LAYOUT) {
                ContinueWatchingViewHolder viewHolder = (ContinueWatchingViewHolder) holder;
                viewHolder.itemView.setTag(listItem);
                viewHolder.updateUI(listItem, mItems.getLayoutType());
            } else if (viewType == T_2_3_MOVIE_LAYOUT) {
                MovieLayoutViewHolder viewHolder = (MovieLayoutViewHolder) holder;
                viewHolder.itemView.setTag(listItem);
                viewHolder.updateUI(listItem, mItems.getLayoutType()/*Constants.T_2_3_MOVIE*/);
            } else if (viewType == T_16_9_BIG_META_LAYOUT) {
                BigMetaLayoutViewHolder viewHolder = (BigMetaLayoutViewHolder) holder;
                viewHolder.itemView.setTag(listItem);
                viewHolder.updateUI(listItem, mItems.getLayoutType());
            } else if (viewType == T_16_9_BIG_LAYOUT) {
                BigLayoutViewHolder viewHolder = (BigLayoutViewHolder) holder;
                viewHolder.itemView.setTag(listItem);
                viewHolder.updateUI(listItem, mItems.getLayoutType());
            } else if (viewType == T_16_9_SMALL_LAYOUT) {
                SmallLayoutViewHolder viewHolder = (SmallLayoutViewHolder) holder;
                viewHolder.itemView.setTag(listItem);
                viewHolder.updateUI(listItem, mItems.getLayoutType());
            } else if (viewType == T_1_1_PLAIN_LAYOUT) {
                PlainLayoutViewHolder viewHolder = (PlainLayoutViewHolder) holder;
                viewHolder.itemView.setTag(listItem);
                viewHolder.updateUI(listItem, mItems.getLayoutType());
            } else if (viewType == T_1_1_ALBUM_LAYOUT) {
                PlayLayoutViewHolder viewHolder = (PlayLayoutViewHolder) holder;
                viewHolder.itemView.setTag(listItem);
                viewHolder.updateUI(listItem, mItems.getLayoutType());
            } /*else if (viewType == T_16_9_EPG_LAYOUT) {
                EpgLayoutViewHolder viewHolder = (EpgLayoutViewHolder) holder;
                viewHolder.itemView.setTag(listItem);
                viewHolder.updateUI(listItem, mItems.getLayoutType());
            } */ else if (viewType == T_16_9_BANNER) {
                BannerViewHolder viewHolder = (BannerViewHolder) holder;
                viewHolder.itemView.setTag(listItem);
                viewHolder.updateUI(listItem, mItems.getLayoutType());
            } else if (viewType == T_2_3_MOVIE_STATIC_LAYOUT) {
                MovieStaticViewHolder viewHolder = (MovieStaticViewHolder) holder;
                viewHolder.itemView.setTag(listItem);
                viewHolder.updateUI(listItem, mItems.getLayoutType());
            } else if (viewType == T_16_9_SMALL_META_LAYOUT) {
                SmallLayoutViewHolder viewHolder = (SmallLayoutViewHolder) holder;
                viewHolder.itemView.setTag(listItem);
                viewHolder.updateUI(listItem, mItems.getLayoutType());
            }else if (viewType == T_16_9_LIVEBANNER) {
                LiveBannerViewHolder viewHolder = (LiveBannerViewHolder) holder;
                viewHolder.itemView.setTag(listItem);
                viewHolder.updateUI(listItem, mItems.getLayoutType());
            } else if (viewType == T_SUBSCRIPTION) {
                SubscriptionViewHolder viewHolder = (SubscriptionViewHolder) holder;
                viewHolder.itemView.setTag(listItem);
                viewHolder.updateUI(listItem, mItems.getLayoutType());
                if (position == 0)
                    viewHolder.setInitialPadding((int) context.getResources().getDimension(R.dimen.px_16));
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mItems != null && mItems.getCatalogListItems() != null)
            return mItems.getCatalogListItems().size();
        else
            return 0;
    }

    @Override
    public int getItemViewType(int position) {
        String layoutType = mItems.getLayoutType();
        if (Constants.T_CONTINUE_WATCHING.equalsIgnoreCase(layoutType)) {
            return T_CONTINUE_WATCHING_LAYOUT;
        }else if (Constants.T_2_3_BIG_META.equalsIgnoreCase(layoutType)) {
            return T_2_3_MOVIE_LAYOUT;
        }  else if (Constants.T_2_3_MOVIE.equalsIgnoreCase(layoutType)) {
            return T_2_3_MOVIE_LAYOUT;
        } else if (Constants.T_16_9_BIG.equalsIgnoreCase(layoutType)) {
            return T_16_9_BIG_LAYOUT;
        } else if (Constants.T_16_9_BIG_META.equalsIgnoreCase(layoutType)) {
            return T_16_9_BIG_META_LAYOUT;
        } else if (Constants.T_16_9_SMALL.equalsIgnoreCase(layoutType)) {
            return T_16_9_SMALL_LAYOUT;
        } else if (Constants.T_1_1_PLAIN.equalsIgnoreCase(layoutType) || Constants.T_1_1_ALBUM_META.equalsIgnoreCase(layoutType)) {
            return T_1_1_PLAIN_LAYOUT;
        } else if (Constants.T_1_1_ALBUM.equalsIgnoreCase(layoutType)) {
            return T_1_1_ALBUM_LAYOUT;
        } else if (Constants.T_16_9_EPG.equalsIgnoreCase(layoutType)) {
            return T_16_9_EPG_LAYOUT;
        } else if (Constants.T_16_9_BANNER.equalsIgnoreCase(layoutType)) {
            return T_16_9_BANNER;
        } else if (Constants.T_2_3_MOVIE_STATIC.equalsIgnoreCase(layoutType)) {
            return T_2_3_MOVIE_STATIC_LAYOUT;
        } else if (Constants.T_16_9_SMALL_META_LAYOUT.equalsIgnoreCase(layoutType) || "shows".equalsIgnoreCase(layoutType)) {
            return T_16_9_SMALL_META_LAYOUT;
        } else if (Constants.T_SUBSCRIPTION.equalsIgnoreCase(layoutType)) {
            return T_SUBSCRIPTION;
        }else if (Constants.T_16_9_LIVEBANNER.equalsIgnoreCase(layoutType)) {
            return T_16_9_LIVEBANNER;
        } else {
            return T_16_9_BIG_META_LAYOUT;
        }
    }

    private final static int T_CONTINUE_WATCHING_LAYOUT = 1;
    private final static int T_2_3_MOVIE_LAYOUT = 2;
    private final static int T_16_9_BIG_LAYOUT = 3;
    private final static int T_16_9_BIG_META_LAYOUT = 4;
    private final static int T_16_9_SMALL_LAYOUT = 5;
    private final static int T_1_1_PLAIN_LAYOUT = 6;
    private final static int T_SUBSCRIPTION = 12;
    private final static int T_1_1_ALBUM_LAYOUT = 7;
    private final static int T_16_9_EPG_LAYOUT = 8;
    private final static int T_16_9_BANNER = 9;
    private final static int T_2_3_MOVIE_STATIC_LAYOUT = 10;
    private final static int T_16_9_SMALL_META_LAYOUT = 11;
    private final static int T_16_9_LIVEBANNER = 13;

}

