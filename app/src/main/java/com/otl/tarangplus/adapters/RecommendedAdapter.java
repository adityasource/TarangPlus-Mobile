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
import android.widget.TextView;

import com.otl.tarangplus.Utils.Constants;
import com.otl.tarangplus.Utils.ThumnailFetcher;
import com.otl.tarangplus.customeUI.MyTextView;
import com.otl.tarangplus.model.Item;
import com.otl.tarangplus.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecommendedAdapter extends RecyclerView.Adapter {

    private Activity context;
    private List<Item> mItems;


    public RecommendedAdapter(Activity context) {
        this.context = context;
        mItems = new ArrayList<>();
    }

    public void updateListItems(List<Item> items) {
        this.mItems = items;
        notifyDataSetChanged();
    }

    public void appendUpdateListItems(List<Item> items) {
        this.mItems.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(context).inflate(R.layout.t_16_9_small_meta, parent, false);
        ListViewHolder holder = new ListViewHolder(view);
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

        public ListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        ListViewHolder mListViewHolder = (ListViewHolder) holder;
        List<Item> listItems = mItems;
        final Item listItem = listItems.get(position);
        mListViewHolder.parentPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onItemClick(listItem);
                }
            }
        });
        setUpData(listItem, holder);
//        if (position == 0)
//            mListViewHolder.parentPanel.setLayoutParams(SpacesItemDecoration.addLeftMargin((int) context.getResources().getDimension(R.dimen.px_16)));

    }

    private ItemClickListener listener;

    public void setOnItemClickListener(ItemClickListener listener) {
        this.listener = listener;
    }

    public interface ItemClickListener {
        void onItemClick(Item item);
    }

    private void setUpData(Item listItem, RecyclerView.ViewHolder holder) {
        ListViewHolder viewHolder = (ListViewHolder) holder;
        String url = "";
        url = ThumnailFetcher.fetchAppropriateThumbnail(listItem.getThumbnails(), Constants.T_16_9_SMALL);
        if (!TextUtils.isEmpty(url)) {
            Picasso.get().load(url).placeholder(R.drawable.place_holder_16x9).into(viewHolder.image);
        } else {
            Picasso.get().load(R.color.dark_grey).into(viewHolder.image);
        }

        if (viewHolder.titleText != null) {
            viewHolder.titleText.setText(listItem.getTitle());
        }


        if (listItem.getAccessControl() != null && listItem.getAccessControl().getIsFree()) {
            if (viewHolder.premium != null)
                viewHolder.premium.setVisibility(View.GONE);

//                if (!TextUtils.isEmpty(item.getTheme()) && item.getTheme().equalsIgnoreCase("live")) {
//                    Picasso.get().load(R.drawable.live_rec).into(viewHolder.premium);
//                    viewHolder.premium.setVisibility(View.VISIBLE);
//                }

        } else {
            assert listItem.getAccessControl() != null;
            boolean premiumTag = listItem.getAccessControl().isPremiumTag();
            if (viewHolder.premium != null)
                if (premiumTag) {
                    viewHolder.premium.setVisibility(View.VISIBLE);
                    Picasso.get().load(R.drawable.premium_rec).into(viewHolder.premium);
                } else {
                    viewHolder.premium.setVisibility(View.GONE);
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
        }
        return 0;
    }
}
