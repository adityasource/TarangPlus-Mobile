package com.otl.tarangplus.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.otl.tarangplus.Utils.Constants;
import com.otl.tarangplus.model.Item;
import com.otl.tarangplus.viewholders.ContinueWatchingViewHolder;
import com.otl.tarangplus.R;

import java.util.ArrayList;
import java.util.List;

public class ContinueWatchingAdapter extends RecyclerView.Adapter {

    private final String fromPage;
    private Context context;
    private List<Item> mItems;

    public ContinueWatchingAdapter(Context context, String listing_page) {
        this.context = context;
        mItems = new ArrayList<>();
        fromPage = listing_page;
    }

    public void updateItems(List<Item> items) {
        this.mItems = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View movieLayout;
        if (fromPage.equalsIgnoreCase("listing_page")) {
            movieLayout = LayoutInflater.from(context).inflate(R.layout.t_continue_watching_list_item, parent, false);
            return setUpSize(movieLayout, 2);
        } else {
            movieLayout = LayoutInflater.from(context).inflate(R.layout.t_continue_watching_item, parent, false);
            return new ContinueWatchingViewHolder(movieLayout);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ContinueWatchingViewHolder viewHolder = (ContinueWatchingViewHolder) holder;
        final Item listItem = mItems.get(position);
        viewHolder.updateUI(listItem, Constants.T_16_9_SMALL);

        viewHolder.itemView.setTag(listItem);

        viewHolder.setOnItemClickListener(item -> {
            if (listener != null) {
                listener.onItemClick(item);
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

    @Override
    public int getItemCount() {
        if (mItems != null) {
            return mItems.size();
        }
        return 0;
    }

    private ContinueWatchingViewHolder setUpSize(View view, int spanCount) {
        int displayWidth = Constants.getDeviceWidth(context);
        displayWidth = displayWidth - (int) context.getResources().getDimension(R.dimen.px_48);
        int itemSize = displayWidth / spanCount;

        ContinueWatchingViewHolder holder = new ContinueWatchingViewHolder(view);
        ViewGroup.LayoutParams layoutParams = holder.parentPanel.getLayoutParams();
        layoutParams.width = itemSize;
        holder.parentPanel.setLayoutParams(layoutParams);
        holder.parentPanel.requestLayout();
        return holder;
    }

    public interface UpdateEmptyListener {
        void onAllDelete();
    }

    private UpdateEmptyListener emptyListener;

    public void setOnUpdateEmptyListener(UpdateEmptyListener listener) {
        this.emptyListener = listener;
    }
}
