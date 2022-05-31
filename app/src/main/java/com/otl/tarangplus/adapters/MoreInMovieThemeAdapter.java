package com.otl.tarangplus.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.otl.tarangplus.model.Item;
import com.otl.tarangplus.viewholders.MovieLayoutViewHolder;
import com.otl.tarangplus.R;

import java.util.ArrayList;
import java.util.List;

public class MoreInMovieThemeAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<Item> mItems;

    public MoreInMovieThemeAdapter(Context context) {
        this.context = context;
        mItems = new ArrayList<>();
    }

    public void updateItems(List<Item> items) {
        this.mItems = items;
        notifyDataSetChanged();
    }

    public void appendUpdateItems(List<Item> items) {

        this.mItems = items;
        notifyDataSetChanged();

    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View movieLayout = LayoutInflater.from(context).inflate(R.layout.t_2_3_movie_static_item_meta, parent, false);
        return new MovieLayoutViewHolder(movieLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MovieLayoutViewHolder viewHolder = (MovieLayoutViewHolder) holder;
        final Item listItem = mItems.get(position);
        viewHolder.updateUI(listItem);
//        if (position == 0) {
//            viewHolder.setInitialPadding((int) context.getResources().getDimension(R.dimen.px_16));
//        }

        viewHolder.itemView.setTag(listItem);
        viewHolder.setOnItemClickListener(new MovieLayoutViewHolder.ItemClickListener() {
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
        if (mItems != null) {
            return mItems.size();
        }
        return 0;
    }
}
