package com.otl.tarangplus.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.otl.tarangplus.model.Item;
import com.otl.tarangplus.viewholders.EpisodeItemViewHolder;
import com.otl.tarangplus.R;

import java.util.ArrayList;
import java.util.List;

public class EpisodesAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<Item> itemList;

    public EpisodesAdapter(Context context) {
        this.context = context;
        itemList = new ArrayList<>();
    }

    public void updateItems(List<Item> items) {
        this.itemList = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View movieLayout = LayoutInflater.from(context).inflate(R.layout.t_16_9_small_title_meta, parent, false);
        return new EpisodeItemViewHolder(movieLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        EpisodeItemViewHolder viewHolder = (EpisodeItemViewHolder) holder;
        final Item listItem = itemList.get(position);

        viewHolder.updateUI(listItem);
//        if (position == 0) {
//            viewHolder.setInitialPadding((int) context.getResources().getDimension(R.dimen.px_16));
//        }
        viewHolder.itemView.setTag(listItem);
        viewHolder.setOnItemClickListener(new EpisodeItemViewHolder.ItemClickListener() {
            @Override
            public void onItemClick(Item item) {
                if (listener != null) {
                    listener.onItemClick(item);
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


    @Override
    public int getItemCount() {
        if (itemList != null) {
            return itemList.size();
        }
        return 0;
    }
}
