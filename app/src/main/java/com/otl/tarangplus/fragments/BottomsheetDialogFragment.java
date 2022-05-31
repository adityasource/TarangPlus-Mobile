package com.otl.tarangplus.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.otl.tarangplus.MainActivity;
import com.otl.tarangplus.R;
import com.otl.tarangplus.Utils.Constants;
import com.otl.tarangplus.Utils.Helper;
import com.otl.tarangplus.Utils.PreferenceHandler;
import com.otl.tarangplus.adapters.PlaylistAdapter;
import com.otl.tarangplus.customeUI.GradientTextView;
import com.otl.tarangplus.customeUI.MyEditText;
import com.otl.tarangplus.customeUI.MyTextView;
import com.otl.tarangplus.model.Data;
import com.otl.tarangplus.model.DataError;
import com.otl.tarangplus.model.ListItemRequest;
import com.otl.tarangplus.model.PlayList;
import com.otl.tarangplus.model.PlayListDataResponse;
import com.otl.tarangplus.model.PlayListItem;
import com.otl.tarangplus.model.PlayListResponse;
import com.otl.tarangplus.model.PlaylistAddItem;
import com.otl.tarangplus.model.PlaylistType;
import com.otl.tarangplus.model.UserInfo;
import com.otl.tarangplus.rest.ApiService;
import com.otl.tarangplus.rest.RestClient;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class BottomsheetDialogFragment extends DialogFragment {

    @BindView(R.id.playlist_recyclerview)
    RecyclerView playlistRecyclerview;

    @BindView(R.id.add_to_playlist)
    MyTextView addToPlaylist;

    @BindView(R.id.empty_text)
    TextView emptyText;
    @BindView(R.id.playlist_done)
    MyTextView playlistDone;

    private Dialog dialog;
    private ApiService mApiService;
    private Unbinder unbinder;
    private PlaylistAdapter playlistAdapter;
    public static final String TAG = HomePageFragment.class.getName();
    private PlayListItem playListItemToSend;
    private String contentId;
    private String catalogId;
    private String category;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        playlistDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PlaylistAddItem playlistAddItem = new PlaylistAddItem();
                ListItemRequest listItemRequest = new ListItemRequest();
                listItemRequest.setCatalogId(catalogId);
                listItemRequest.setContentId(contentId);
                listItemRequest.setAuthToken(Constants.API_KEY);
                listItemRequest.setRegion(PreferenceHandler.getRegion(getActivity()));
                listItemRequest.setUser_id(PreferenceHandler.getSessionId(getActivity()));
                int counterMesure = 0;
                List<PlayListItem> listItemsTemp = new ArrayList<>();
                listItemsTemp.addAll(Constants.PLAYLISTITEMSFORCURRENTITEM);
                for (PlayListItem playListItemTemp : listItemsTemp) {
                    if (playListItemTemp.isSelected()) {
                        playListItemTemp.setPreviouslySelected(true);
                        playListItemToSend = playListItemTemp;
                    }
                    counterMesure = counterMesure + 1;
                }
                if (counterMesure > 1) {
                    Log.d("counterMesure: ", counterMesure + "");
                }
                playlistAddItem.setListItemRequest(listItemRequest);
                if (listItemsTemp.size() == 0) {
                    Helper.showToast(getActivity(), "Playlist is empty! Create a playlist", Toast.LENGTH_SHORT);
                    dismiss();
                    return;
                }

                if (playListItemToSend == null) {
                    Helper.showToast(getActivity(), "Please select at least one list", Toast.LENGTH_SHORT);
                    return;
                }

                mApiService.addtoPlaylistHere(PreferenceHandler.getSessionId(getActivity()), playListItemToSend.getPlaylistId(), playlistAddItem)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<JsonObject>() {
                            @Override
                            public void call(JsonObject playListResponse) {
                                Helper.showToast(getActivity(), "Added to list", Toast.LENGTH_SHORT);
                                dismiss();
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                //show toast
                                Helper.showToast(getActivity(), "Something went wrong", Toast.LENGTH_SHORT);
                                dismiss();
                            }
                        });

            }
        });

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.playlist_bottomsheet, container, false);
        unbinder = ButterKnife.bind(this, view);
        RestClient mRestClient = new RestClient(getActivity());
        mApiService = mRestClient.getApiService();

        addToPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoginConfirmationPopUp();
            }
        });
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        playlistRecyclerview.setLayoutManager(manager);
        contentId = getArguments().getString("contentId");
        catalogId = getArguments().getString("catalogId");
        category = getArguments().getString("category");
        apicall();
        if (Constants.PLAYLISTITEMSFORCURRENTITEM == null || Constants.PLAYLISTITEMSFORCURRENTITEM.size() <= 0) {
            emptyText.setVisibility(View.VISIBLE);
           playlistAdapter = new PlaylistAdapter(getActivity(), new ArrayList<PlayListItem>());
            playlistRecyclerview.setAdapter(playlistAdapter);
            playlistRecyclerview.setVisibility(View.GONE);
        } else {
            emptyText.setVisibility(View.GONE);
            playlistAdapter = new PlaylistAdapter(getActivity(), Constants.PLAYLISTITEMSFORCURRENTITEM);
            playlistRecyclerview.setAdapter(playlistAdapter);
            playlistRecyclerview.setVisibility(View.VISIBLE);
        }

        return view;
    }

    private void showLoginConfirmationPopUp() {

        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.create_playlist_popup);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        GradientTextView cancel = (GradientTextView) dialog.findViewById(R.id.cancel);
        GradientTextView confirm = (GradientTextView) dialog.findViewById(R.id.confirm);
        MyEditText warningMessage = (MyEditText) dialog.findViewById(R.id.playlistname);


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String playlistname = warningMessage.getText().toString().trim();
                if (!TextUtils.isEmpty(playlistname) || !playlistname.equals("")) {
                    PlayListItem playListItem = new PlayListItem();
                    playListItem.setName(playlistname);
                    playListItem.setType("");
                    PlaylistType playlistType = new PlaylistType();
                    playlistType.setRegion(PreferenceHandler.getRegion(getActivity()));
                    playlistType.setAuthToken(Constants.API_KEY);
                    playlistType.setPlaylist(playListItem);


                    mApiService.createPlayList(PreferenceHandler.getSessionId(getActivity()), playlistType)
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<PlayListDataResponse>() {
                        @Override
                        public void call(PlayListDataResponse playListResponse) {

                            if (playListResponse != null) {
                                //set recycler view data of bottom sheet
                                Toast.makeText(getActivity(), "Created", Toast.LENGTH_SHORT).show();
                                checkPlayList();
                                if (dialog != null)
                                    dialog.dismiss();
                            }

                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            //show toast
                            String errorMessage = Constants.getErrorMessage(throwable).getError().getMessage();
                            if (!TextUtils.isEmpty(errorMessage) || !errorMessage.equals(" ")) {
                                Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "Oops! something went wrong.", Toast.LENGTH_SHORT).show();
                            }
                            //  Toast.makeText(getActivity(), "Cant add playlist with the same name", Toast.LENGTH_SHORT).show();
                            if (dialog != null)
                                dialog.dismiss();
                        }
                    });
                }else{
                    Toast.makeText(getActivity(), "Cant create Playlist with empty name.", Toast.LENGTH_SHORT).show();
                }
                dialog.cancel();
            }
        });

    }

    boolean statusformatch = false;
    List<PlayListItem> temPLayList = new ArrayList<>();

    private void checkPlayList() {

        if (PreferenceHandler.isLoggedIn(getActivity())) {
            temPLayList = Helper.doDeepCopy(Constants.PLAYLISTITEMSFORCURRENTITEM);
            mApiService.getAllPlaylist(PreferenceHandler.getSessionId(getActivity()), 0, 20).subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<PlayListDataResponse>() {
                @Override
                public void call(PlayListDataResponse playListResponse) {
                    emptyText.setVisibility(View.GONE);
                    Constants.addToLocalList(playListResponse.getPlayListResponse().getPlaylistItems());
                    List<PlayListItem> tempList = new ArrayList<>();
                    tempList = Helper.doDeepCopy(Constants.PLAYLISTITEMS);
                    List<PlayListItem> tempList2 = new ArrayList<>();
                    for (PlayListItem playListItemTemp : tempList) {
                        statusformatch = false;
                        tempList2 = Helper.doDeepCopy(Constants.PLAYLISTITEMSFORCURRENTITEM);

                        for (PlayListItem playListItemTemp2 : tempList2) {
                            if (playListItemTemp.getName().equalsIgnoreCase(playListItemTemp2.getName())) {
                                statusformatch = true;
                            }
                        }
                        if (!statusformatch) {
                            temPLayList.add(playListItemTemp);
                        }
                    }
                    emptyText.setVisibility(View.GONE);
                    playlistRecyclerview.setVisibility(View.VISIBLE);
                    playlistAdapter.updateList(temPLayList);
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    //show toast
                    Log.e("ERROR IN GETTING PLAYLIST","YES");
                }
            });

        }
    }

    @Override
    public void onDestroy() {
        for (PlayListItem playListItemTemp : Constants.PLAYLISTITEMSFORCURRENTITEM) {
            playListItemTemp.setSelected(false);
        }
        super.onDestroy();

    }

    private void apicall() {
        mApiService.getAllPlayListDetails(PreferenceHandler.getSessionId(getActivity()), catalogId, contentId, category)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<PlayListResponse>() {
                    @Override
                    public void call(PlayListResponse playListResponse) {
                        Constants.PLAYLISTITEMSFORCURRENTITEM.clear();
                        Constants.PLAYLISTITEMSFORCURRENTITEM = Helper.doDeepCopy(Constants.PLAYLISTITEMS);
                        if (playListResponse != null) {
                            Data response = playListResponse.getPlayListResponse();
                            if (response != null) {
                                List<PlayList> playLists = response.getPlayLists();
                                if (playLists != null && playLists.size() > 0) {
                                    Helper helper = new Helper(getActivity());
                                    Constants.PLAYLISTITEMSFORCURRENTITEM.clear();
                                    Constants.PLAYLISTITEMSFORCURRENTITEM = Helper.doDeepCopy(helper.handleCustomLists(playLists));
                                    playlistAdapter.updateList(Helper.doDeepCopy(Constants.PLAYLISTITEMSFORCURRENTITEM));
                                }else {
                                    playlistAdapter.updateList(Helper.doDeepCopy(Constants.PLAYLISTITEMSFORCURRENTITEM));
                                }
                            }
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e("ERROR IN GETTING PLAYLIST","YES");
                    }
                });
    }

}
