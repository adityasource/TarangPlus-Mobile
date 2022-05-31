package com.otl.tarangplus.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.otl.tarangplus.MainActivity;
import com.otl.tarangplus.R;
import com.otl.tarangplus.Utils.Constants;
import com.otl.tarangplus.Utils.Helper;
import com.otl.tarangplus.Utils.PreferenceHandler;
import com.otl.tarangplus.adapters.PlaylistAdapter;
import com.otl.tarangplus.adapters.PlaylistMePageAdaptor;
import com.otl.tarangplus.customeUI.GradientTextView;
import com.otl.tarangplus.customeUI.MyTextView;
import com.otl.tarangplus.model.Data;
import com.otl.tarangplus.model.PlayListDataResponse;
import com.otl.tarangplus.model.PlayListItem;
import com.otl.tarangplus.rest.ApiService;
import com.otl.tarangplus.rest.RestClient;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class PlaylistFragment extends Fragment {
    public static final String TAG = PlaylistFragment.class.getSimpleName();

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.header)
    MyTextView header;
    @BindView(R.id.close)
    AppCompatImageView close;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.browse_shemaroo)
    GradientTextView browseShemaroo;
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

    private ApiService mApiService;
    private String what;
    private String where;
    private PlaylistMePageAdaptor playlistMePageAdaptor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.playlist, container, false);
        ButterKnife.bind(this, inflate);
        mApiService = new RestClient(getContext()).getApiService();
        what = getArguments().getString(Constants.WHAT);
            header.setText(R.string.Playlist);
            where = Constants.PLAYLIST;
        checkPlayList();
        close.setVisibility(View.GONE);
        return inflate;
    }

    @OnClick({R.id.back, R.id.close, R.id.browse_shemaroo})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.back) {
            getActivity().onBackPressed();
        } else if (id == R.id.close) {
            getActivity().onBackPressed();
        } else if (id == R.id.browse_shemaroo) {
            //Helper.addWithoutBackStackAndKeepHome(getActivity(), new HomePageFragment(), HomePageFragment.TAG, 2);
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

            /*getActivity().onBackPressed();
            getActivity().onBackPressed();*/
        }
    }

    private void checkPlayList() {

        if(PreferenceHandler.isLoggedIn(getActivity())){

            mApiService.getAllPlaylist(PreferenceHandler.getSessionId(getActivity()),0, 20)  .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<PlayListDataResponse>() {
                @Override
                public void call(PlayListDataResponse playListResponse) {


                    Log.d(TAG, "checkPlayList : "+"");
                   Constants.addToLocalList(playListResponse.getPlayListResponse().getPlaylistItems());
                    if(Constants.PLAYLISTITEMS.size() > 0 ){
                        noWatchlistContainer.setVisibility(View.GONE);
                    }else{
                        watchDescrText.setVisibility(View.GONE);
                        favDescrText.setVisibility(View.GONE);
                        playlistText.setVisibility(View.VISIBLE);
                        playlist_recyclerview.setVisibility(View.GONE);
                        noWatchlistContainer.setVisibility(View.VISIBLE);
                    }
                    LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                    playlist_recyclerview.setLayoutManager(manager);
                    List<PlayListItem> playListItem = new ArrayList<>();
                    playListItem = Helper.doDeepCopy(Constants.PLAYLISTITEMS);
                    playlistMePageAdaptor = new PlaylistMePageAdaptor(getActivity(), listener, playListItem);
                    playlist_recyclerview.setAdapter(playlistMePageAdaptor);
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    //show toast
                    Log.e("ERROR IN GETTING PLAYLIST","YES");
                    noWatchlistContainer.setVisibility(View.VISIBLE);
                }
            });

        }
    }
    PlaylistMePageAdaptor.UpdateEmptyListener listener = new PlaylistMePageAdaptor.UpdateEmptyListener(){
        @Override
        public void onAllDelete() {
            if(Constants.PLAYLISTITEMS.size() > 0 ){
                noWatchlistContainer.setVisibility(View.GONE);
            }else{
                watchDescrText.setVisibility(View.GONE);
                favDescrText.setVisibility(View.GONE);
                playlistText.setVisibility(View.VISIBLE);
                playlist_recyclerview.setVisibility(View.GONE);
                noWatchlistContainer.setVisibility(View.VISIBLE);
            }
        }
    };
}
