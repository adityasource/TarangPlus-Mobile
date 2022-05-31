package com.otl.tarangplus.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.otl.tarangplus.R;
import com.otl.tarangplus.Utils.Constants;
import com.otl.tarangplus.Utils.Helper;
import com.otl.tarangplus.Utils.PreferenceHandler;
import com.otl.tarangplus.customeUI.MyTextView;
import com.otl.tarangplus.fragments.HomePageFragment;
import com.otl.tarangplus.fragments.WatchListFragment;
import com.otl.tarangplus.model.Data;
import com.otl.tarangplus.model.ListResonse;
import com.otl.tarangplus.model.PlayListDataResponse;
import com.otl.tarangplus.model.PlayListItem;
import com.otl.tarangplus.rest.ApiService;
import com.otl.tarangplus.rest.Resource;
import com.otl.tarangplus.rest.RestClient;
import com.otl.tarangplus.viewModel.PlayListViewHolder;
import com.otl.tarangplus.viewModel.PlaylistMePageViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class PlaylistMePageAdaptor extends RecyclerView.Adapter {
    @BindView(R.id.playlist_recyclerview)
    RecyclerView playlist_recyclerview;
    @BindView(R.id.no_watchlist_container)
    RelativeLayout noWatchlistContainer;
    @BindView(R.id.watch_descr_text)
    MyTextView watchDescrText;
    @BindView(R.id.fav_descr_text)
    MyTextView favDescrText;
    @BindView(R.id.playlist_text)
    MyTextView playlistText;

    Context context;
    List<PlayListItem> playListItemList = new ArrayList<>();
    private ApiService mApiService;
    private PlaylistMePageAdaptor playlistMePageAdaptor;
    public static final String TAG = HomePageFragment.class.getName();

    public PlaylistMePageAdaptor(Context context, UpdateEmptyListener listener, List<PlayListItem> playListItemList) {
        this.context = context;
        this.playListItemList = playListItemList;
        this.listener = listener;
    }

    public void updateList(List<PlayListItem> playListItemList){

        this.playListItemList.clear();
        this.playListItemList = playListItemList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View playlistLO = LayoutInflater.from(context).inflate(R.layout.playlist_item, parent, false);
        return new PlaylistMePageViewHolder(playlistLO);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        PlaylistMePageViewHolder viewHolder = (PlaylistMePageViewHolder) holder;


        if (playListItemList != null && playListItemList.size() > 0) {
            PlayListItem singleItem = playListItemList.get(position);

            if (singleItem != null) {
                viewHolder.updateUI(singleItem);
                viewHolder.itemView.setTag(singleItem);

                viewHolder.setOnItemClickListener( new PlaylistMePageViewHolder.ItemClickListener() {
                    @Override
                    public void onItemdeleteClick(PlayListItem item) {
                        Helper.showProgressDialog((Activity) context);
                        mApiService = new RestClient(context).getApiService();
                        mApiService.deletePlaylist( PreferenceHandler.getSessionId(context), item.getPlaylistId())
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Action1<JsonObject>() {
                                    @Override
                                    public void call(JsonObject object) {
                                        updateRecylerview();
                                        Helper.showToast((Activity) context, item.getName()+" is deleted from Playlist succesfully ", R.drawable.ic_check);
                                    }
                                }, new Action1<Throwable>() {
                                    @Override
                                    public void call(Throwable throwable) {
                                        Helper.dismissProgressDialog();
                                    }
                                });
                    }

                    @Override
                    public void onItemClick(PlayListItem item) {
                        Log.d("itemclickedhere", "onItemClick: ");
                        Fragment fragment_watch = new WatchListFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("title", item.getName());
                        bundle.putString(Constants.WHAT, "playlist");
                        bundle.putString("playlistId", item.getPlaylistId());
                        fragment_watch.setArguments(bundle);
                        Helper.addFragment(((Activity) context), fragment_watch, WatchListFragment.TAG);
                     /*   mApiService = new RestClient(context).getApiService();
                        mApiService.getAllItemsPlaylist(PreferenceHandler.getSessionId(context), item.getPlaylistId(),0, 20)
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Action1<ListResonse>() {
                            @Override
                            public void call(ListResonse resonse) {
                                Log.d(TAG, "checkPlayList : "+"");
                                Data data = resonse.getData();

                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                //show toast
                            }
                        });*/

                    }
                });
            }

        }
    }

    @Override
    public int getItemCount() {
        return playListItemList == null ? 0 : playListItemList.size();
    }

    public void updateRecylerview(){

        mApiService.getAllPlaylist(PreferenceHandler.getSessionId(context),0, 20)  .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<PlayListDataResponse>() {
            @Override
            public void call(PlayListDataResponse playListResponse) {

                Log.d(TAG, "checkPlayList : "+"");
                Constants.addToLocalList(playListResponse.getPlayListResponse().getPlaylistItems());
                List<PlayListItem> listItemsTemp = new ArrayList<>();
                listItemsTemp = Helper.doDeepCopy(Constants.PLAYLISTITEMS);
                if (Constants.PLAYLISTITEMS.size() == 0) {
                    if (listener != null) {
                        listener.onAllDelete();
                    }
                }
                Helper.dismissProgressDialog();
                updateList(listItemsTemp);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                //show toast
                Log.e("ERROR IN GETTING PLAYLIST","YES");
                Helper.dismissProgressDialog();
            }
        });
    }

    public interface UpdateEmptyListener {
        void onAllDelete();
    }
    private UpdateEmptyListener listener;
}
