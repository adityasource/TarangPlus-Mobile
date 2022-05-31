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
import com.otl.tarangplus.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListAdapter extends RecyclerView.Adapter {

    private Activity context;
    private List<CatalogListItem> mItems;
    private String mLayoutType;
    int placeholder = R.drawable.place_holder_16x9;

    public ListAdapter(Activity context, String layoutTheam) {
        this.context = context;
        this.mLayoutType = layoutTheam;
        mItems = new ArrayList<>();
    }

    public void updateListItems(List<CatalogListItem> items) {
        this.mItems = items;
        notifyDataSetChanged();
    }

    public void appendUpdatedList(List<CatalogListItem> items) {
        this.mItems.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if (!TextUtils.isEmpty(mLayoutType)) {
            if (mLayoutType.equalsIgnoreCase("t_2_3_movie") || mLayoutType.equalsIgnoreCase("movies")) {
                view = LayoutInflater.from(context).inflate(R.layout.t_2_3_movie_static_item_meta, parent, false);
                placeholder = R.drawable.place_holder_2x3;
                return setUpSizeThreeItems(view, 3);
            } else if (mLayoutType.equalsIgnoreCase("t_2_3_big_meta") || mLayoutType.equalsIgnoreCase("movies")) {
                view = LayoutInflater.from(context).inflate(R.layout.t_2_3_movie_static_item, parent, false);
                placeholder = R.drawable.place_holder_2x3;
                return setUpSizeThreeItems(view, 3);
            }else if (mLayoutType.equalsIgnoreCase("shows") || mLayoutType.equalsIgnoreCase("show")) {
                view = LayoutInflater.from(context).inflate(R.layout.t_16_9_small_new, parent, false);
                placeholder = R.drawable.place_holder_16x9;
                return setUpSizeTwoItems(view, 2);
            } else if (mLayoutType.equalsIgnoreCase("episodes")) {
                view = LayoutInflater.from(context).inflate(R.layout.t_16_9_small_new, parent, false);
                placeholder = R.drawable.place_holder_16x9;
                return setUpSizeTwoItems(view, 2);
            }/* else if (mLayoutType.equalsIgnoreCase("album") || mLayoutType.equalsIgnoreCase("songs")) {
                view = LayoutInflater.from(context).inflate(R.layout.t_16_9_big_meta, parent, false);
                placeholder = R.drawable.place_holder_1x1;
                return setUpSizeTwoItems(view, 2);
            } */ else {
                view = LayoutInflater.from(context).inflate(R.layout.t_16_9_big_meta, parent, false);
                placeholder = R.drawable.place_holder_16x9;
                return setUpSizeTwoItems(view, 2);
            }

        }
        return null;
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
        @BindView(R.id.live_tag)
        ImageView liveTag;
        @Nullable
        @BindView(R.id.play_icon)
        ImageView playIcon;
        @Nullable
        @BindView(R.id.titleText)
        MyTextView titleText;
        @Nullable
        @BindView(R.id.episode_meta_data)
        TextView episodeMetaData;

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

    private ListViewHolder setUpSizeThreeItems(View view, int spanCount) {
        int displayWidth = Constants.getDeviceWidth(context);
        displayWidth = displayWidth - (int) context.getResources().getDimension(R.dimen.px_10);
        int itemSize = displayWidth / spanCount;
        ListViewHolder holder = new ListViewHolder(view);
        ViewGroup.LayoutParams layoutParams = holder.image.getLayoutParams();
        layoutParams.width = itemSize;
        holder.image.setLayoutParams(layoutParams);
        holder.image.requestLayout();
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        ListViewHolder mListViewHolder = (ListViewHolder) holder;
        List<CatalogListItem> listItems = mItems;
        final CatalogListItem listItem = listItems.get(position);
        mListViewHolder.parentPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Helper.moveToPageBasedOnTheme(context, listItem);
            }
        });
        setUpData(listItem, holder);
    }
    private static Map<String, Integer> get16By9Height(Activity activity, int dimension) {
        Map<String, Integer> map = new HashMap<>();
        if (activity != null) {
            int width = Constants.getDeviceWidth(activity) - dimension;
            int newWidth = width / 2;
            map.put("WIDTH", newWidth);
            int heightnew = (newWidth * 9) / 16;

            map.put("HEIGHT", heightnew);
            return map;
        } else {
            map.put("HEIGHT", (int) (activity.getResources().getDimension(R.dimen.px_200)));
            return map;
        }

    }
    private void setUpData(CatalogListItem listItem, RecyclerView.ViewHolder holder) {
        ListViewHolder viewHolder = (ListViewHolder) holder;
        String url = "";
        if (!TextUtils.isEmpty(mLayoutType) && mLayoutType.equalsIgnoreCase("movies") ||
                "t_2_3_big_meta".equalsIgnoreCase(mLayoutType)) {

            ViewGroup.LayoutParams layoutparams = ((ListViewHolder) holder).rela_list.getLayoutParams();
            layoutparams.width = Helper.get2By3Width(context, ((int) (context.getResources().getDimension(R.dimen.px_35)))).get("WIDTH");
            ((ListViewHolder) holder).rela_list.setLayoutParams(layoutparams);

            viewHolder.titleText.setVisibility(View.VISIBLE);
            viewHolder.description.setVisibility(View.VISIBLE);
            viewHolder.description.setText(listItem.getItemCaption());
            url = ThumnailFetcher.fetchAppropriateThumbnail(listItem.getThumbnails(), Constants.T_2_3_MOVIE_STATIC);
            Picasso.get().load(url).placeholder(placeholder).into(viewHolder.image);
        } else {
            url = ThumnailFetcher.fetchAppropriateThumbnail(listItem.getThumbnails(), Constants.T_16_9_SMALL);
            viewHolder.titleText.setVisibility(View.VISIBLE);

            ViewGroup.LayoutParams layoutparams = ((ListViewHolder) holder).rela_list.getLayoutParams();
            ViewGroup.LayoutParams layoutparamsPanel =  ((ListViewHolder) holder).parentPanel.getLayoutParams();

            Map<String, Integer> map = get16By9Height(context, ((int) (context.getResources().getDimension(R.dimen.px_38))));
            layoutparams.width  = map .get("WIDTH");
            ((ListViewHolder) holder).rela_list.setLayoutParams(layoutparams);
            layoutparamsPanel.height = map .get("HEIGHT");
            ((ListViewHolder) holder).parentPanel.setLayoutParams(layoutparamsPanel);
        }


        if (!TextUtils.isEmpty(url)) {
            Picasso.get().load(url).placeholder(placeholder).into(viewHolder.image);
        } else {
            Picasso.get().load(R.color.dark_grey).into(viewHolder.image);
        }


        if (viewHolder.titleText != null) {
            viewHolder.titleText.setText(listItem.getTitle());
        }
        if (viewHolder.episodeMetaData != null) {
            viewHolder.episodeMetaData.setText("EP 001 | 60m");
        }
        if (!TextUtils.isEmpty(mLayoutType)) {
            if (!mLayoutType.equalsIgnoreCase("episodes")) {
                if (viewHolder.playIcon != null) {
                    viewHolder.playIcon.setVisibility(View.GONE);
                }
            }
        }

        if (viewHolder.premium != null)
            if (listItem.getAccessControl() != null && listItem.getAccessControl().getIsFree()) {
                viewHolder.premium.setVisibility(View.GONE);
            } else {
                if (listItem.getAccessControl() != null) {
                    boolean premiumTag = listItem.getAccessControl().isPremiumTag();
                    if (premiumTag) {
                        viewHolder.premium.setVisibility(View.VISIBLE);
                    } else
                        viewHolder.premium.setVisibility(View.GONE);
                }

            }

        if (viewHolder.liveTag != null)
            if (listItem.getTheme().equalsIgnoreCase(Constants.LIVE))
                viewHolder.liveTag.setVisibility(View.VISIBLE);
            else
                viewHolder.liveTag.setVisibility(View.GONE);

        if (viewHolder.description != null)
            viewHolder.description.setText(listItem.getItemCaption());
    }

    @Override
    public int getItemCount() {
        if (mItems != null && mItems.size() > 0) {
            return mItems.size();
        }
        return 0;
    }
}
