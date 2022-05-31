package com.otl.tarangplus.adapters;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.otl.tarangplus.Utils.Constants;
import com.otl.tarangplus.Utils.Helper;
import com.otl.tarangplus.Utils.ThumnailFetcher;
import com.otl.tarangplus.customeUI.MyTextView;
import com.otl.tarangplus.model.CatalogListItem;
import com.otl.tarangplus.model.Item;
import com.otl.tarangplus.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MoreListingAdapter extends RecyclerView.Adapter {

    private String mLayoutType;
    private List<Item> mItems;
    private List<CatalogListItem> catalogListItems;
    private Activity context;
    private int placeholder = R.drawable.place_holder_16x9;

    public MoreListingAdapter(Activity context, String layoutType) {
        if (!TextUtils.isEmpty(layoutType))
            this.mLayoutType = layoutType;
        else
            this.mLayoutType = "show";  // Fallback should be fixed some day ;-)
        mItems = new ArrayList<>();
        catalogListItems = new ArrayList<>();
        this.context = context;
    }

    public void updateListItems(List<Item> items) {
        this.mItems = items;
        notifyDataSetChanged();
    }

    public void appendUpdateListItems(List<Item> items) {
        this.mItems.addAll(items);
        notifyDataSetChanged();
    }

    public void updateListItems(List<CatalogListItem> catalogListItemList, String from) {
        //catalogListItems = catalogListItemList;
        catalogListItems.addAll(catalogListItemList);
        notifyDataSetChanged();
    }

    private ListViewHolder setUpSizeThreeItems(View view, int spanCount) {
        int displayWidth = Constants.getDeviceWidth(context);
        displayWidth = displayWidth - (int) context.getResources().getDimension(R.dimen.px_48);
        int itemSize = displayWidth / spanCount;
        ListViewHolder holder = new ListViewHolder(view);
        ViewGroup.LayoutParams layoutParams = holder.image.getLayoutParams();
        layoutParams.width = itemSize;
        holder.image.setLayoutParams(layoutParams);
        holder.image.requestLayout();
        return holder;
    }

    private ListViewHolder setUpSizeTwoItems(View view, int spanCount) {
        int displayWidth = Constants.getDeviceWidth(context);
        displayWidth = displayWidth - (int) context.getResources().getDimension(R.dimen.px_38);
        int itemSize = displayWidth / spanCount;
        ListViewHolder holder = new ListViewHolder(view);
        ViewGroup.LayoutParams layoutParams = holder.image.getLayoutParams();
        layoutParams.width = itemSize;
        holder.image.setLayoutParams(layoutParams);
        holder.image.requestLayout();
        return holder;
    }

    class ListViewHolder extends RecyclerView.ViewHolder {

        @Nullable
        @BindView(R.id.parentPanel)
        CardView parentPanel;

        @Nullable
        @BindView(R.id.image)
        ImageView image;

        @Nullable
        @BindView(R.id.premium)
        ImageView premium;
        @Nullable
        @BindView(R.id.play_icon)
        ImageView playIcon;
        @Nullable
        @BindView(R.id.titleText)
        MyTextView titleText;
        @Nullable
        @BindView(R.id.episode_meta_data)
        TextView episodeMetaData;
        @Nullable
        @BindView(R.id.live_tag)
        ImageView liveTag;

        @BindView(R.id.description)
        MyTextView description;
        @Nullable
        @BindView(R.id.rela_list)
        RelativeLayout rela_list;
        
        public ListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if (!TextUtils.isEmpty(mLayoutType)) {
            if (mLayoutType.equalsIgnoreCase("t_2_3_movie") || mLayoutType.equalsIgnoreCase("movies") ||
                    mLayoutType.equalsIgnoreCase("movie")) {
                view = LayoutInflater.from(context).inflate(R.layout.t_2_3_movie_static_item, parent, false);
                placeholder = R.drawable.place_holder_2x3;
                return setUpSizeThreeItems(view, 3);
            } else if (mLayoutType.equalsIgnoreCase("shows") || mLayoutType.equalsIgnoreCase("show")) {
                view = LayoutInflater.from(context).inflate(R.layout.t_16_9_small_new, parent, false);
                placeholder = R.drawable.place_holder_16x9;
                return setUpSizeTwoItems(view, 2);
            } else if (mLayoutType.equalsIgnoreCase("episodes") ||
                    mLayoutType.equalsIgnoreCase("episode") ||
                    mLayoutType.equalsIgnoreCase("show_episode")) {
                view = LayoutInflater.from(context).inflate(R.layout.t_16_9_small_new, parent, false);
                placeholder = R.drawable.place_holder_16x9;
                return setUpSizeTwoItems(view, 2);
            } else if (mLayoutType.equalsIgnoreCase("album") || mLayoutType.equalsIgnoreCase("songs")) {
                view = LayoutInflater.from(context).inflate(R.layout.t_1_1_album_meta, parent, false);
                placeholder = R.drawable.place_holder_1x1;
                return setUpSizeTwoItems(view, 2);
            } else if (mLayoutType.equalsIgnoreCase("live")) {
                view = LayoutInflater.from(context).inflate(R.layout.tv_item, parent, false);
                placeholder = R.drawable.place_holder_1x1;
                return setUpSizeTwoItems(view, 2);
            } else if (mLayoutType.equalsIgnoreCase("linear")) {
                view = LayoutInflater.from(context).inflate(R.layout.tv_item, parent, false);
                placeholder = R.drawable.place_holder_1x1;
                return setUpSizeTwoItems(view, 2);
            } else {
                view = LayoutInflater.from(context).inflate(R.layout.t_16_9_small_new, parent, false);
                placeholder = R.drawable.place_holder_16x9;
                return setUpSizeTwoItems(view, 2);
            }

        } else {
            view = LayoutInflater.from(context).inflate(R.layout.t_16_9_small_new, parent, false);
            placeholder = R.drawable.place_holder_16x9;
            return setUpSizeTwoItems(view, 2);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        ListViewHolder mListViewHolder = (ListViewHolder) holder;
        if (mItems != null && mItems.size() > 0) {
            List<Item> listItems = mItems;
            final Item listItem = listItems.get(position);
            mListViewHolder.parentPanel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Helper.moveToPageBasedOnTheme(context, listItem, "");
                }
            });
            setUpData(listItem, holder);
        } else if (catalogListItems != null && catalogListItems.size() > 0) {
            List<CatalogListItem> listItems = catalogListItems;
            final CatalogListItem listItem = listItems.get(position);
            mListViewHolder.parentPanel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Helper.moveToPageBasedOnTheme(context, listItem);
                }
            });
            setUpData(listItem, holder, "");
        }
    }
    private static Map<String, Integer> get16By9Height(Activity activity, int dimension) {
        Map<String, Integer> map = new HashMap<>();
        if (activity != null) {
            int width = Constants.getDeviceWidth(activity) - dimension;
            int newWidth = width / 2;
            map.put("WIDTH",  newWidth);
            int heightnew = (newWidth * 9) / 16;

            map.put("HEIGHT", heightnew);
            return map;
        } else {
            map.put("HEIGHT", (int) (activity.getResources().getDimension(R.dimen.px_200)));
            return map;
        }

    }
    private void setUpData(Item listItem, RecyclerView.ViewHolder holder) {
        ListViewHolder viewHolder = (ListViewHolder) holder;
        String url = "";
        if (!TextUtils.isEmpty(mLayoutType) &&
                mLayoutType.equalsIgnoreCase("movies") ||
                mLayoutType.equalsIgnoreCase("movie") ||
                mLayoutType.equalsIgnoreCase("t_2_3_movie")) {
            ViewGroup.LayoutParams layoutparams = ((ListViewHolder) holder).rela_list.getLayoutParams();
            layoutparams.width = Helper.get2By3Width(context, ((int) (context.getResources().getDimension(R.dimen.px_35)))).get("WIDTH");
            ((ListViewHolder) holder).rela_list.setLayoutParams(layoutparams);

            url = ThumnailFetcher.fetchAppropriateThumbnail(listItem.getThumbnails(), Constants.T_2_3_MOVIE_STATIC);

            viewHolder.titleText.setText(listItem.getTitle());
            viewHolder.description.setText(listItem.getItemCaption());
        } else {
            url = ThumnailFetcher.fetchAppropriateThumbnail(listItem.getThumbnails(), Constants.T_16_9_SMALL);
            if (viewHolder.titleText != null)
                viewHolder.titleText.setVisibility(View.VISIBLE);

            if (viewHolder.description != null) {
                viewHolder.description.setVisibility(View.VISIBLE);
                viewHolder.description.setText(listItem.getItemCaption());
            }

            ViewGroup.LayoutParams layoutparams = ((ListViewHolder) holder).rela_list.getLayoutParams();
            ViewGroup.LayoutParams layoutparamsPanel = ((ListViewHolder) holder).parentPanel.getLayoutParams();

            Map<String, Integer> map = get16By9Height(context, ((int) (context.getResources().getDimension(R.dimen.px_38))));
            //layoutparams.height = map .get("HEIGHT");
            layoutparams.width  = map .get("WIDTH");
            ((ListViewHolder) holder).rela_list.setLayoutParams(layoutparams);
            layoutparamsPanel.height = map .get("HEIGHT");
            ((ListViewHolder) holder).parentPanel.setLayoutParams(layoutparamsPanel);
        }


        if (!TextUtils.isEmpty(url)) {
            Picasso.get().load(url).placeholder(placeholder).into(viewHolder.image);
        }


        if (viewHolder.titleText != null) {
            viewHolder.titleText.setText(listItem.getTitle());
        }
        if (viewHolder.episodeMetaData != null && TextUtils.isEmpty(listItem.getItemCaption())) {
            viewHolder.episodeMetaData.setText(listItem.getItemCaption());
        }

        if (!TextUtils.isEmpty(mLayoutType)) {
            if (!mLayoutType.equalsIgnoreCase("episodes") ||
                    !mLayoutType.equalsIgnoreCase("episode") ||
                    !mLayoutType.equalsIgnoreCase("show_episode")) {
                if (viewHolder.playIcon != null) {
                    viewHolder.playIcon.setVisibility(View.GONE);
                }
                if (viewHolder.episodeMetaData != null)
                    viewHolder.episodeMetaData.setVisibility(View.VISIBLE);
            }
        }


        //        Setting Linear channel UI here , we should only show 16:9 icon
        if (!TextUtils.isEmpty(mLayoutType) && mLayoutType.equalsIgnoreCase("linear")) {
            if (viewHolder.titleText != null)
                viewHolder.titleText.setVisibility(View.GONE);
            if (viewHolder.episodeMetaData != null)
                viewHolder.episodeMetaData.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(listItem.getTheme()) && listItem.getTheme().equalsIgnoreCase("linear")) {
            if (viewHolder.titleText != null)
                viewHolder.titleText.setVisibility(View.GONE);
            if (viewHolder.episodeMetaData != null)
                viewHolder.episodeMetaData.setVisibility(View.GONE);
        }


//        Setting Premium,Live Tag's Here
        if (listItem.getAccessControl() != null && listItem.getAccessControl().getIsFree()) {
            if (viewHolder.premium != null) {
                boolean premiumTag = listItem.getAccessControl().isPremiumTag();
                if (!premiumTag) {
                    viewHolder.premium.setVisibility(View.GONE);
                }
            }
//            if (!TextUtils.isEmpty(listItem.getTheme()) && listItem.getTheme().equalsIgnoreCase("live")) {
//                Picasso.get().load(R.drawable.live_rec).into(viewHolder.premium);
//                boolean premiumTag = listItem.getAccessControl().isPremiumTag();
//                if (premiumTag) {
//                    viewHolder.premium.setVisibility(View.VISIBLE);
//                }
//            }
        } else {
            if (viewHolder.premium != null)
                if (listItem.getAccessControl() != null) {
                    boolean premiumTag = listItem.getAccessControl().isPremiumTag();
                    if (premiumTag) {
                        viewHolder.premium.setVisibility(View.VISIBLE);
                    } else
                        viewHolder.premium.setVisibility(View.GONE);
                }
        }

        if (viewHolder.liveTag != null)
            if (!TextUtils.isEmpty(listItem.getTheme()) && listItem.getTheme().equalsIgnoreCase("live"))
                viewHolder.liveTag.setVisibility(View.VISIBLE);
            else
                viewHolder.liveTag.setVisibility(View.GONE);

    }

    private void setUpData(CatalogListItem listItem, RecyclerView.ViewHolder holder, String status) {
        ListViewHolder viewHolder = (ListViewHolder) holder;
        String url = "";
        if (!TextUtils.isEmpty(mLayoutType) &&
                mLayoutType.equalsIgnoreCase("movies") ||
                mLayoutType.equalsIgnoreCase("movie") ||
                mLayoutType.equalsIgnoreCase("t_2_3_movie")) {

            url = ThumnailFetcher.fetchAppropriateThumbnail(listItem.getThumbnails(), Constants.T_2_3_MOVIE_STATIC);
            if (viewHolder.titleText != null) {

                viewHolder.titleText.setVisibility(View.GONE);

            }

        } else {
            url = ThumnailFetcher.fetchAppropriateThumbnail(listItem.getThumbnails(), Constants.T_16_9_SMALL);
            if (viewHolder.titleText != null)
                viewHolder.titleText.setVisibility(View.VISIBLE);
        }


        if (!TextUtils.isEmpty(url)) {
            Picasso.get().load(url).placeholder(placeholder).into(viewHolder.image);
        }


        if (viewHolder.titleText != null) {
            viewHolder.titleText.setText(listItem.getTitle());
        }
        if (viewHolder.episodeMetaData != null && TextUtils.isEmpty(listItem.getItemCaption())) {
            viewHolder.episodeMetaData.setText(listItem.getItemCaption());
        }

        if (!TextUtils.isEmpty(mLayoutType)) {
            if (!mLayoutType.equalsIgnoreCase("episodes") ||
                    !mLayoutType.equalsIgnoreCase("episode") ||
                    !mLayoutType.equalsIgnoreCase("show_episode")) {
                if (viewHolder.playIcon != null) {
                    viewHolder.playIcon.setVisibility(View.GONE);
                }
                if (viewHolder.episodeMetaData != null)
                    viewHolder.episodeMetaData.setVisibility(View.VISIBLE);
            }
        }


        //        Setting Linear channel UI here , we should only show 16:9 icon
        if (!TextUtils.isEmpty(mLayoutType) && mLayoutType.equalsIgnoreCase("linear")) {
            if (viewHolder.titleText != null)
                viewHolder.titleText.setVisibility(View.GONE);
            if (viewHolder.episodeMetaData != null)
                viewHolder.episodeMetaData.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(listItem.getTheme()) && listItem.getTheme().equalsIgnoreCase("linear")) {
            if (viewHolder.titleText != null)
                viewHolder.titleText.setVisibility(View.GONE);
            if (viewHolder.episodeMetaData != null)
                viewHolder.episodeMetaData.setVisibility(View.GONE);
        }


//        Setting Premium,Live Tag's Here
        if (listItem.getAccessControl() != null && listItem.getAccessControl().getIsFree()) {
            if (viewHolder.premium != null)
                viewHolder.premium.setVisibility(View.GONE);

        } else {
            if (listItem.getAccessControl() != null) {
                boolean premiumTag = listItem.getAccessControl().isPremiumTag();
                if (viewHolder.premium != null)
                    if (premiumTag) {
                        viewHolder.premium.setVisibility(View.VISIBLE);
                        Picasso.get().load(R.drawable.premium_rec).into(viewHolder.premium);
                    } else {
                        viewHolder.premium.setVisibility(View.GONE);
                    }
            }
        }

        if (viewHolder.liveTag != null)
            if (!TextUtils.isEmpty(listItem.getTheme()) && listItem.getTheme().equalsIgnoreCase("live"))
                viewHolder.liveTag.setVisibility(View.VISIBLE);
            else
                viewHolder.liveTag.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        if (mItems != null && mItems.size() > 0) {
            return mItems.size();
        } else if (catalogListItems != null) {
            return catalogListItems.size();
        }
        return 0;
    }
}
