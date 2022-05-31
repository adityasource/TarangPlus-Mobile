package com.otl.tarangplus.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;
import android.text.TextUtils;

import com.otl.tarangplus.model.Data;
import com.otl.tarangplus.model.Item;
import com.otl.tarangplus.model.ListResonse;
import com.otl.tarangplus.Database.AppDatabase;
import com.otl.tarangplus.Database.PlayListDbScheme;
import com.otl.tarangplus.Utils.Constants;
import com.otl.tarangplus.Utils.PreferenceHandler;
import com.otl.tarangplus.rest.ApiService;
import com.otl.tarangplus.rest.RestClient;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class PlaylistCheck extends Service {

    private static final String TAG = PlaylistCheck.class.getSimpleName();
    private static AppDatabase mDb;
    private static PlaylistCheck instance;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private ApiService apiService;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mDb = AppDatabase.getInstance(this);
        apiService = new RestClient(this).getApiService();
        boolean loggedIn = PreferenceHandler.isLoggedIn(getApplicationContext());
        if (loggedIn) {
            checkForWatchList("");
        }
    }

    private void checkForWatchList(String contentId) {
        apiService.getPlayLists(PreferenceHandler.getSessionId(this),
                Constants.WATCH_LATER,0)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ListResonse>() {
                    @Override
                    public void call(ListResonse resonse) {
                        if (resonse != null) {
                            Data data =
                                    resonse.getData();
                            List<Item> items = data.getItems();
                            insertToDb(items,contentId);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }

    private static Executor executor = Executors.newSingleThreadExecutor();

    private void insertToDb(List<Item> items,String itemContentId) {
        PlayListDbScheme[] array = new PlayListDbScheme[items.size()];
        executor.execute(new Runnable() {
            @Override
            public void run() {
                if (items.size() >0) {
                    for (int i = 0; i < items.size(); i++) {
                        Item item = items.get(i);
                        String title = item.getTitle();
                        String contentId = item.getContentId();
                        String catalogId = item.getCatalogId();
                        String listId = item.getListItemId();
                        PlayListDbScheme scheme = new PlayListDbScheme(contentId, catalogId, title, listId);
                        array[i] = scheme;
                        if (!TextUtils.isEmpty(itemContentId) && itemContentId.equalsIgnoreCase(contentId)) {
                            if (listSelectListener != null) {
                                listSelectListener.onListSelected(listId);
                            }
                        }
                    }
                    mDb.playListDb().insertAll(array);
                }else {
                    mDb.playListDb().deleteAllPlayLists();
                }
            }
        });
    }

    private OnListSelectListener listSelectListener;
    public void setOnListSelectedListener(OnListSelectListener listener) {
        listSelectListener = listener;
    }
    public interface OnListSelectListener {
        void onListSelected(String selectedId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    public void updatePlayList(String contentId) {
        checkForWatchList(contentId);
    }

    public static PlaylistCheck getPlayListInstance() {
        if (instance != null) {
            return instance;
        }
        return null;
    }
}
