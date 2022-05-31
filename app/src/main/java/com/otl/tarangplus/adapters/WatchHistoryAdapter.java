package com.otl.tarangplus.adapters;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonObject;
import com.otl.tarangplus.Analytics.AnalyticsProvider;
import com.otl.tarangplus.Utils.Constants;
import com.otl.tarangplus.Utils.Helper;
import com.otl.tarangplus.Utils.PreferenceHandler;
import com.otl.tarangplus.model.Item;
import com.otl.tarangplus.R;
import com.otl.tarangplus.rest.ApiService;
import com.otl.tarangplus.rest.RestClient;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class WatchHistoryAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<Item> items;
    private String type;
    private String playListId;

    public WatchHistoryAdapter(Context context, UpdateEmptyListener listener, String where) {
        this.context = context;
        items = new ArrayList<>();
        this.listener = listener;
        this.type = where;
    }

    public void updateData(List<Item> items, String where) {
        this.items.addAll(items);
        notifyDataSetChanged();
        this.type = where;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View continueWatching = LayoutInflater.from(context).inflate(R.layout.watch_later_item, parent, false);
        return setUpSize(continueWatching, 2);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        WatchLaterViewHolder viewHolder = (WatchLaterViewHolder) holder;
        Item listItem = items.get(position);
        viewHolder.itemView.setTag(listItem);
        viewHolder.updateUI(listItem, Constants.T_CONTINUE_WATCHING);
        viewHolder.setOnDeleteClickListener(new WatchLaterViewHolder.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(Item item) {
                deleteApi(item);
            }
        });
    }

    private ApiService mApiService;

    private void deleteApi(Item item) {
        if(!TextUtils.isEmpty(playListId)){
            type = playListId;
        }
        mApiService = new RestClient(context).getApiService();

        mApiService.removeWatchLaterItem(PreferenceHandler.getSessionId(context),type
                , item.getListItemId())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject jsonObject) {
                        if(type.equalsIgnoreCase(Constants.WATCH_LATER))
                            Helper.showToast((Activity) context, context.getString(R.string.deleted_watch_history_item), R.drawable.ic_check);
                        else
                            Helper.showToast((Activity) context, context.getString(R.string.deleted_favourite_history_item), R.drawable.ic_check);

                        items.remove(item);
                        if (items.size() == 0) {
                            if (listener != null) {
                                listener.onAllDelete();
                            }
                        }
                        notifyDataSetChanged();
                        try{
                            AnalyticsProvider analyticsProvider = new AnalyticsProvider(context);
                            analyticsProvider.reportRemoveFromWatchList(item);
                        }catch (Exception e){

                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }

    private WatchLaterViewHolder setUpSize(View view, int spanCount) {
        int displayWidth = Constants.getDeviceWidth(context);
        displayWidth = displayWidth - (int) context.getResources().getDimension(R.dimen.px_48);
        int itemSize = displayWidth / spanCount;

        WatchLaterViewHolder holder = new WatchLaterViewHolder(view);
        ViewGroup.LayoutParams layoutParams = holder.parentPanel.getLayoutParams();
        layoutParams.width = itemSize;
        holder.parentPanel.setLayoutParams(layoutParams);
        holder.parentPanel.requestLayout();
        return holder;
    }

    public interface UpdateEmptyListener {
        void onAllDelete();
    }

    private UpdateEmptyListener listener;

 /*   public void setOnUpdateEmptyListener(UpdateEmptyListener listener) {
        Log.d("Listner", "listner");
        this.listener = listener;
    }
*/
    @Override
    public int getItemCount() {
        return items.size();
    }
}
