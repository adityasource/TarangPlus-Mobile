package com.otl.tarangplus.adapters;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
import com.otl.tarangplus.model.Thumbnails;
import com.otl.tarangplus.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LinearTvAdapter extends RecyclerView.Adapter {

    private Activity context;
    private List<CatalogListItem> itemList;
    private String LAYOUT_TYPE = Constants.T_16_9_SMALL_META_LAYOUT;

    public LinearTvAdapter(Activity context) {
        this.context = context;
        itemList = new ArrayList<>();
    }

    public void setTvItems(List<CatalogListItem> items) {
        itemList = items;
        notifyDataSetChanged();
    }

    public void appendSetTvItems(List<CatalogListItem> items) {
        itemList.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.tv_item, parent, false);
        return setUpSizeTwoItems(view, 2);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        LinearTvItemViewHolder viewHolder = (LinearTvItemViewHolder) holder;
        CatalogListItem item = itemList.get(position);
        if (item != null) {
            String title = item.getTitle();
            Thumbnails thumbnails = item.getThumbnails();
            String url = ThumnailFetcher.fetchAppropriateThumbnail(thumbnails, LAYOUT_TYPE);
            Picasso.get().load(url).placeholder(R.drawable.place_holder_16x9).error(R.drawable.place_holder_16x9).into(viewHolder.image);
            viewHolder.titleText.setText(title);
            viewHolder.titleText.setVisibility(View.GONE);


            if (item.getAccessControl() != null && item.getAccessControl().getIsFree()) {
                if (viewHolder.premium != null)
                    viewHolder.premium.setVisibility(View.GONE);
            } else {
                if (item.getAccessControl() != null) {
                    boolean premiumTag = item.getAccessControl().isPremiumTag();
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
                if (!TextUtils.isEmpty(item.getTheme()) && item.getTheme().equalsIgnoreCase("live"))
                    viewHolder.liveTag.setVisibility(View.VISIBLE);
                else
                    viewHolder.liveTag.setVisibility(View.GONE);


            viewHolder.parentPanel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Fragment currentFragment = Helper.getCurrentFragment(context);
                    if (currentFragment != null && currentFragment instanceof LiveTvDetailsFragment) {
                        if (listener != null) {
                            listener.onItemClick(item);
                        }
                    } else {
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
                }
            });
        }
    }

    private ItemClickListener listener;

    public void setItemClickListener(ItemClickListener listener) {
        this.listener = listener;
    }

    public interface ItemClickListener {
        void onItemClick(CatalogListItem item);
    }

    @Override
    public int getItemCount() {
        if (itemList != null) {
            return itemList.size();
        }
        return 0;
    }

    static class LinearTvItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.play_icon)
        ImageView play_icon;
        @BindView(R.id.lyt)
        RelativeLayout lyt;
        @BindView(R.id.titleText)
        MyTextView titleText;
        @BindView(R.id.premium)
        ImageView premium;
        @BindView(R.id.parentPanel)
        CardView parentPanel;
        @BindView(R.id.live_tag)
        ImageView liveTag;


        public LinearTvItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private LinearTvItemViewHolder setUpSizeTwoItems(View view, int spanCount) {
        int displayWidth = Constants.getDeviceWidth(context);
        displayWidth = displayWidth - (int) context.getResources().getDimension(R.dimen.px_38);
        int itemSize = displayWidth / spanCount;
        LinearTvItemViewHolder holder = new LinearTvItemViewHolder(view);
        ViewGroup.LayoutParams layoutParams = holder.image.getLayoutParams();
        layoutParams.width = itemSize;
        holder.image.setLayoutParams(layoutParams);
        holder.image.requestLayout();
        return holder;
    }
}
