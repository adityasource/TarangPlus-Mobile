package com.otl.tarangplus.rest;


import com.google.gson.JsonObject;
import com.otl.tarangplus.model.AddPlayListItems;
import com.otl.tarangplus.model.AppConfigDetails;
import com.otl.tarangplus.model.CampaginThings;
import com.otl.tarangplus.model.ContinueWatchingResponse;
import com.otl.tarangplus.model.HeartBeatRequest;
import com.otl.tarangplus.model.HomeScreenResponse;
import com.otl.tarangplus.model.ListResonse;
import com.otl.tarangplus.model.LoggedInData;
import com.otl.tarangplus.model.PlayListDataResponse;
import com.otl.tarangplus.model.PlayListResponse;
import com.otl.tarangplus.model.PlaylistAddItem;
import com.otl.tarangplus.model.PlaylistType;
import com.otl.tarangplus.model.ProfileData;
import com.otl.tarangplus.model.SearchListItems;
import com.otl.tarangplus.model.ShareRecivedData;
import com.otl.tarangplus.model.ShareRelatedData;
import com.otl.tarangplus.model.ShowDetailsResponse;
import com.otl.tarangplus.model.SignInRequest;
import com.otl.tarangplus.model.SignUpData;
import com.otl.tarangplus.model.SingleEpisode;
import com.otl.tarangplus.model.UpdateProfileRequest;
import com.otl.tarangplus.model.WatchCount;

import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by VPotadar on 28/08/18.
 */

public interface ApiService {
/*http://13.233.16.182/catalog_lists/all-channels?page=0&category=all&auth_token=Unxs6bxjw2xab7Tsw64F&region=IN*/
    @GET("/catalog_lists/{home_link}.gzip")
    Observable<HomeScreenResponse> getHomeScreenDetailsBasedOnHomeLink(@Path("home_link") String homeLink);

    @GET("/catalog_lists/catalog-tabs.gzip")
    Observable<HomeScreenResponse> getHomeScreenTabs();

    @GET("/catalogs/message/items/app-config-params")
    Observable<AppConfigDetails> getConfigParms(@Query("current_version") String current_version);

    @GET("/search.gzip")
    Observable<SearchListItems> searchDataSet(@Query("auth_token") String authId,
                                              @Query("region") String region,
                                              @Query("filters") String filter,
                                              @Query("category") String category,
                                              @Query("q") String q);

    @GET("/catalog_lists/trending-search.gzip")
    Observable<SearchListItems> getTrendingSearchDataSet(/*@Query("filters") String filter, @Query("category") String category*/);


    //Get Details about the Show
    @GET("/catalogs/{catalog_id}/items/{item_id}.gzip")
    Observable<ShowDetailsResponse> getShowDetailsResponse(@Path("catalog_id") String catId, @Path("item_id") String itemId);

    //Get Seasons details
    @GET("/catalogs/shows/subcategories/{subcategory_Id}/episodes.gzip")
    Observable<ListResonse> getItemsInSeasons(@Path("subcategory_Id") String subcategory_Id);

    //single episode
    @GET("/catalogs/shows/subcategories/{subcategory_id}/episodes/{episode_id}.gzip")
    Observable<SingleEpisode> getEpisodesInSeasons(@Path("subcategory_id") String subcategory_id, @Path("episode_id") String episode_Id);

    //Get Seasons details
    @GET("/catalogs/shows/{showId}/episodes/{episode_id}.gzip")
    Observable<ListResonse> getSingleEpisodeWithoutSeason(@Path("showId") String showId, @Path("episode_id") String episodeid);

    //http://18.210.75.7:3000/catalog_lists/bollywood-classic?auth_token=Ts4XpMvGsB2SW7NZsWc3&region=IN
    @GET("/catalog_lists/{home_link_id}")
    Observable<ListResonse> getListingData(@Path("home_link_id") String homeLinkID, @Query("page") int pageSize);

    //http://18.210.75.7:3000/catalog_lists/classic-more-in-comedy?auth_token=Ts4XpMvGsB2SW7NZsWc3&region=IN
    @GET("/catalog_lists/classic-more-in-comedy")
    Observable<ListResonse> getMoreComedy();

    //http://18.210.75.7:3000/catalogs/gujarathi/episodes?region=IN&auth_token=Ts4XpMvGsB2SW7NZsWc3
    @GET("/catalogs/{home_link_id}/episodes")
    Observable<ListResonse> getAllEpisodes(@Path("home_link_id") String homeLinkId);

    //http://18.210.75.7:3000/users/sign_in
    @POST("/users/sign_in")
    Observable<LoggedInData> login(@Body SignInRequest request);

    @POST("/users/external_auth/sign_in")
    Observable<LoggedInData> fbLogin(@Body SignInRequest request);

    //http://18.210.75.7:3000/users/verification/178344?auth_token=Ts4XpMvGsB2SW7NZsWc3&type=msisdn
    @GET("/users/verification/{otp}")
    Observable<LoggedInData> checkOtp(@Path("otp") String otp, @Query("type") String type, /*@Query("device_type") String device_type, @Query("device_name") String device_name,*/ @Query("mobile_number") String mobileNum);

// Email Verification
    @GET("/users/verification/{key}")
    Observable<LoggedInData> verifyEmail(@Path("key") String key);


    //http://18.210.75.7:3000/users/forgot_password
    @POST("/users/forgot_password")
    Observable<JsonObject> forgotPassword(@Body SignInRequest request);

    // http://18.210.75.7:3000/users/b16e4bf2afd8d4ab472adbb48ef1a2d8/profiles?auth_token=Ts4XpMvGsB2SW7NZsWc3
    @GET("/users/{session_id}/profiles")
    Observable<ProfileData> getAllUsers(@Path("session_id") String sessionID);

    @PUT("/users/{session_id}/profiles/{profile_id}")
    Observable<JsonObject> updateUserProfile(@Path("session_id") String sessionID, @Path("profile_id") String profileId, @Body UpdateProfileRequest updateProfileRequest);

    //Create add profiles
    @POST("/users/{session_id}/profiles.gzip")
    Observable<JsonObject> createProfile(@Path("session_id") String sid, @Body UpdateProfileRequest updateProfileRequest);

    //Delete profile
    @DELETE("/users/{session_id}/profiles/{profile_id}")
    Observable<JsonObject> deleteProfile(@Path("session_id") String sid, @Path("profile_id") String profileIdId);

    //http://18.210.75.7:3000/users
    @POST("/users")
    Observable<SignUpData> signUp(@Body SignInRequest request);

//    '{"auth_token":"Ts4XpMvGsB2SW7NZsWc3", "listitem":{"content_id":"5b45ea38c1df415fd200001a","catalog_id":"5b45cc9cc1df416723000055"}}'
//            'http://18.210.75.7:3000/users/b16e4bf2afd8d4ab472adbb48ef1a2d8/playlists/watchlater'

    //http://18.210.75.7:3000/catalogs/bollywood-retro-movies/items?genre=drama&region=IN&auth_token=3zZmzoHg8z6SM3wpDoyw
    @GET("/catalogs/{home_link_id}/items.gzip")
    Observable<ListResonse> getMoreBasedOnGenre(@Path("home_link_id") String homeLinkID, @Query("genre") String genre, @Query("page") int pageSize);

    @POST("/users/{session_id}/playlists/{listname}.gzip")
    Observable<JsonObject> setWatchLater(@Path("session_id") String sid,@Path("listname") String listName, @Body AddPlayListItems items);

    @GET("/catalogs/{catalog_id}/items/{item_id}/episode_list.gzip")
    Observable<ListResonse> getAllEpisodesUnderShow(@Path("catalog_id") String catId, @Path("item_id") String showID, @Query("page") int page, @Query("page_size") int pageSize, @Query("order_by") String orderBy);

    //http://18.210.75.7:3000/users/b16e4bf2afd8d4ab472adbb48ef1a2d8/playlists/watchlater/listitems?auth_token=Ts4XpMvGsB2SW7NZsWc3&region=IN
    @GET("/users/{session_id}/playlists/{play_list_id}/listitems")
    Observable<ListResonse> getPlayLists(@Path("session_id") String userId, @Path("play_list_id") String playListID, @Query("page") int itemSize);


    //curl -i -H "Content-Type: application/json" -H "Accept: application/json" -X POST -d '{"auth_token":"Ts4XpMvGsB2SW7NZsWc3", "user_profile":{"profile_id": "58827375"}}'
    //http://18.210.75.7:3000/users/b16e4bf2afd8d4ab472adbb48ef1a2d8/assign_profile

    @POST("/users/{session_id}/assign_profile.gzip")
    Observable<JsonObject> assignProfile(@Path("session_id") String sid, @Body UpdateProfileRequest updateProfileRequest);

    // '{"auth_token":"Ts4XpMvGsB2SW7NZsWc3", "user":{"email_id":"919740876169","type":"msisdn"}}'
    //        'http://18.210.75.7:8080/users/resend_verification_link'


    @POST("/users/resend_verification_link.gzip")
    Observable<JsonObject> resendVerificationCode(@Body SignInRequest request);

    @GET("/regions/autodetect/ip.gzip")
    Observable<JsonObject> getRegions();

    /**
     * @param ma_id
     * @param ma_ti
     * @param ma_pn
     * @param ma_mt
     * @param ma_re
     * @param ma_st
     * @param ma_ps
     * @param ma_le
     * @param ma_ttp
     * @param ma_w
     * @param ma_h
     * @param ma_fs
     * @param url
     * @param idsite
     * @param rec    With extra unknown params
     * @return
     */
    /*r=430832,h=12,m=12,s=13,_id=83f6ee534556fc3f,_idts=1538557466,_idvc=7,_idn=0,_refts=0,_viewts=1538717389,cs=windows-1252,send_image=1,pdf=1,qt=0,realp=0,wma=0,dir=0,fla=0,java=0,gears=0,ag=0,cookie=1,res=1600x900,gt_ms=102,pv_id=4ETZwn*/
   /* @POST("/piwik/piwik.php")
    Observable<JsonObject> updateMediaAnalytics(@Query("ma_id") String ma_id, @Query("ma_ti") String ma_ti, @Query("ma_pn") String ma_pn, @Query("ma_mt") String ma_mt, @Query("ma_re") String ma_re, @Query("ma_st") String ma_st, @Query("ma_ps") String ma_ps, @Query("ma_le") String ma_le, @Query("ma_ttp") String ma_ttp, @Query("ma_w") String ma_w, @Query("ma_h") String ma_h, @Query("ma_fs") String ma_fs, @Query("url") String url, @Query("idsite") String idsite, @Query("rec") String rec, @Query("r") String r, @Query("h") String h, @Query("m") String m, @Query("s") String s, @Query("_id") String _id, @Query("_idts") String _idts, @Query("_idvc") String _idvc, @Query("_idn") String _idn, @Query("_refts") String _refts, @Query("_viewts") String _viewts, @Query("cs") String cs, @Query("send_image") String send_image, @Query("pdf") String pdf, @Query("qt") String qt, @Query("realp") String realp, @Query("wma") String wma, @Query("dir") String dir, @Query("fla") String fla, @Query("java") String java, @Query("gears") String gears, @Query("ag") String ag, @Query("cookie") String cookie, @Query("res") String res, @Query("gt_ms") String gt_ms, @Query("pv_id") String pv_id, @Query("_cvar") Cvar customVar, @Query("urlref") String urlref,@Query("dimension1") String dimension1,@Query("dimension2") String dimension2,@Query("dimension3") String dimension3,@Query("dimension4") String dimension4,@Query("dimension5") String dimension5,@Query("dimension6") String dimension6,@Query("dimension7") String dimension7,@Query("dimension8") String dimension8,@Query("dimension9") String dimension9,@Query("dimension10") String dimension10,@Query("dimension11") String dimension11,@Query("dimension12") String dimension12,@Query("dimension13") String dimension13,@Query("dimension14") String dimension14);*/

    //http://18.210.75.7:8080/catalog_lists/all-channels?auth_token=Ts4XpMvGsB2SW7NZsWc3&region=IN&page_size=150
    @GET("/catalog_lists/all-channels")
    Observable<ListResonse> getAllChannelList(@Query("theme") String theme,
                                              @Query("page") int pageSize, @Query("category") String category);

    //http://18.210.75.7:8080/catalogs/sample-test-channel/items/:item_id/programs?region=IN&auth_token=Ts4XpMvGsB2SW7NZsWc3&region=IN&status=any
    @GET("/catalogs/{catalog_id}/items/{item_id}/programs")
    Observable<ListResonse> getNextPrograms(@Path("catalog_id") String catalogId, @Path("item_id") String itemIt, @Query("status") String status);

    //http://18.210.75.7:8080/catalogs/sample-test-channel/items?region=IN&auth_token=Ts4XpMvGsB2SW7NZsWc3&region=IN&status=any&content_category=bhakti
    @GET("/catalogs/{catalog_id}/items")
    Observable<ListResonse> getLiveChannels(@Path("catalog_id") String catalogId, @Query("content_category") String contentCategory, @Query("status") String status, @Query("page") int pageSize);

    @GET("/users/{session_id}/account")
    Observable<ListResonse> getAccountDetails(@Path("session_id") String session_id);

    @PUT("/users/{session_id}/account")
    Observable<ListResonse> updateAccountDetails(@Path("session_id") String session_id, @Body AccountUpdateRequest request);

    @GET("/users/{session_id}/account")
    Observable<PlayListResponse> account(@Path("session_id") String session_id);


    @POST("/users/otp_verification")
    Observable<JsonObject> checkOtpWithPostCall(@Body SignInRequest request);

    //HeartBeat request
    @POST("/users/{user_id}/playlists/watchhistory.gzip")
    Observable<JsonObject> reportHeartBeat(@Path("user_id") String userID, @Body HeartBeatRequest heartBeatRequest);

    //Get Continue watching list
    @GET("/users/{user_id}/playlists/watchhistory/listitems.gzip")
    Observable<ContinueWatchingResponse> getContinueWatchingList(@Path("user_id") String sid, @Query("page_size") int pageSize);


    //Get Continue watching list
    @GET("/users/{user_id}/playlists/watchhistory/listitems.gzip")
    Observable<ContinueWatchingResponse> getContinueWatchingList1(@Path("user_id") String sid, @Query("page") int pageSize);


    //Delete Continue watching item
    @DELETE("/users/{session_id}/playlists/watchhistory/listitems/{item_id}.gzip")
    Observable<JsonObject> removeContinueWatchItem(@Path("session_id") String sid, @Path("item_id") String itemId);

    @POST("/users/reset_password")
    Observable<JsonObject> resetPassword(@Body SignInRequest request);


    @DELETE("/users/{session_id}/playlists/watchhistory/clear_all")
    Observable<JsonObject> clearWatchHistory(@Path("session_id") String sessionId);

    //http://18.210.75.7:8080/users/70d457c3b837e660f28829c0f55e50b1/account
    @POST("/users/{user_id}/account")
    Observable<JsonObject> setParentalControl(@Body SignInRequest request, @Path("user_id") String userID);

    @DELETE("/users/{session_id}/playlists/{listName}/listitems/{listitem_id}.gzip")
    Observable<JsonObject> removeWatchLaterItem(@Path("session_id") String sid, @Path("listName") String listName, @Path("listitem_id") String itemId);

    @POST("/users/{session_id}/change_password")
    Observable<JsonObject> changePassword(@Path("session_id") String sid, @Body PasswordChangeRequest PasswordChangeRequest);


//    Deprecated for now
//    //http://18.210.75.7:8080/users/a394d3715b4e90d7fa08948bdf114565/verify_password?auth_token=Ts4XpMvGsB2SW7NZsWc3&password=welcome
//    @GET("/users/{session_id}/verify_password")
//    Observable<JsonObject> verifyPassowrd(@Path("session_id") String sessionID,@Query("password") String password);


    //http://18.210.75.7:8080/users/a394d3715b4e90d7fa08948bdf114565/verify_password?auth_token=Ts4XpMvGsB2SW7NZsWc3&password=welcome
    @POST("/users/{session_id}/verify_password")
    Observable<JsonObject> verifyPassowrd(@Body SignInRequest request, @Path("session_id") String userID);

    //http://18.210.75.7:8080/users/a394d3715b4e90d7fa08948bdf114565/get_all_details?auth_token=Ts4XpMvGsB2SW7NZsWc3&region=IN&catalog_id=5b8d1076c1df411ca70000c0&content_id=5ba9068dc1df413dae0003bf&category=kids
    @GET("/users/{user_id}/get_all_details")
    Observable<PlayListResponse> getAllPlayListDetails(@Path("user_id") String userId, @Query("catalog_id") String catalogId, @Query("content_id") String contentId, @Query("category") String category);

    @POST("/users/{session_id}/sign_out.gzip")
    Observable<JsonObject> signOut(@Path("session_id") String path, @Body SignOutDetails sDetails);

    @POST("/users/get_share_parameters.gzip")
    Observable<ShareRecivedData> getDetailsFromShareUrl(@Body ShareRelatedData shareRelatedData);

    //http://3.85.70.32:6060/users/c7d7da2e82c2efebfa69d80ef1068ac7/campaign_data
    @POST("/users/{session_id}/campaign_data.gzip")
    Observable<JsonObject> campaginThings(@Path("session_id") String path, @Body CampaginThings campaginThings);

    @GET("/catalogs/{catalog_id}/items/{item_id}/videolists.gzip")
    Observable<ListResonse> getAssociatedVideos(@Path("catalog_id") String catalogId, @Path("item_id") String itemId);

    @POST("/users/{session_id}/playlists")
    Observable<PlayListDataResponse> createPlayList(@Path("session_id") String sessionId, @Body PlaylistType playlistType  );

    @GET("/v2/users/{session_id}/playlists")
    Observable<PlayListDataResponse> getAllPlaylist(@Path("session_id") String sessionId, @Query("page") int page, @Query("page_size") int pagesize);


    @POST("/users/{session_id}/playlists/{playlistId}/listitems")
    Observable<JsonObject> addtoPlaylistHere(@Path("session_id") String sessionId, @Path("playlistId") String playlistId,@Body PlaylistAddItem playlistType  );

    @DELETE("/users/{session_id}/playlists/{playlistId}")
    Observable<JsonObject> deletePlaylist(@Path("session_id") String sessionId,@Path("playlistId") String playlistId );

    @GET("/users/{session_id}/playlists/{playlistId}/listitems")
    Observable<ListResonse> getAllItemsPlaylist(@Path("session_id") String sessionId,@Path("playlistId") String playlistId, @Query("page") int page, @Query("page_size") int pagesize);


    //http://bhima.tarangplus.in:3006/live/watch_count?auth_token=Unxs6bxjw2xab7Tsw64F&region=IN&item_language=en&item_id=5d81cea5babd81783700004b
    @GET("/live/watch_count")
    Observable<WatchCount> getViewCount(@Query("item_id") String itemId);

}

