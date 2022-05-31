package com.otl.tarangplus.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.GradientDrawable;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Patterns;
import android.view.Display;
import android.view.Window;

import com.google.gson.Gson;
import com.otl.tarangplus.BuildConfig;
import com.otl.tarangplus.Database.LayoutDbScheme;
import com.otl.tarangplus.R;
import com.otl.tarangplus.customeUI.MyTextView;
import com.otl.tarangplus.model.AccessControl;
import com.otl.tarangplus.model.DataError;
import com.otl.tarangplus.model.PlayListItem;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;

public class Constants {
    public static final String IS_DEFAULT_PROFILE = "is_default_profile";
    public static final String ITEMS = "myItems";
    public static final int API_ITEMS_COUNT = 500; // No of items asked in API response
    public static final int ADS_WAITTIME = 15000; //milliSec
    public static final long HEARTBEAT_RATE = 20000;
    public static final String MOBILE = "mobile";
    public static final String USERMAIL = "mail";
    public static final String DOB = "dob";
    public static final String OTP = "otp";
    public static final String IS_NOTIFICATION_ENABLED = "notification enabled";
    public static final String IS_PARENTAL_CONTROL_ENABLED = "isParentalControlEnabled";
    public static final String SELECTED_PROFILE = "profileSelected";
    public static final String PARENTAL_PIN = "parentalPin";
    public static final int PAGE_LIST_LIMIT = 20; //Set by server to be 20 for each index number
    public static final String SELECTED_GENRE = "selectedGenre";
    public static final String ALL_EPISODES = "allEpisodes";
    public static final String PLAIN_CATEGORY_TYPE = "plainCategoryType";
    public static final String IS_PROFILE_SELECTED = "profile selected";
    public static final String WATCH_LATER_TXT = "Watch Later";
    public static final String WATCH_HISTORY_TXT = "Watch History";
    public static final String ACCESS_CONTROL_IS_LOGIN_REG = "ACCESS_CONTROL_IS_LOGIN_REG";
    public static final String AN_APP_TYPE = "android";
    public static final String WHO_INITIATED_THIS = "to_determine_which_flow_is_initiated";
    public static final String SVOD_ACTIVE = "svod active";
    public static final String ANALYTICS_ID = "analytics id";
    public static final String USER_PERIOD = "user period";
    public static final String USER_PACK_NAME = "user plan name";
    public static final String USER_PLAN_TYPE = "user plan type";
    public static final String IS_SUBSCRIBED = "is subscribed";
    public static final String FAQ_URL = "faqUrl";
    public static final String HELP_URL = "helpUrl";
    public static final String PRIVACY_POLICY = "privacyPOlicy";
    public static final String TERMS_AND_CONDITION = "termsAndCondition";
    public static final String CONTACT_US = "contactUs";
    public static final String MEDIA_ACTIVE_INTERVAL = "media_active_interval";
    public static final String USER_NAME = "userName";
    public static final String PROFILE_LIMIT = "profileLimit";
    public static final String FROM_NOTIFICATION = "fromNotification";
    public static final String IS_CLEVER_TAP_ENABLED = "isCelverTapEnabled";
    public static final String SUB_CATEGORY_LIST = "subCategoryList";
    public static final String SUB_ACTIVE_PLANS = "subActivePlans";
    public static final String SUB_INACTIVE_PLANS = "subInActivePlans";
    public static final String LIVE = "live";
    public static final String DEVICE_TYPE = "android";
    public static final String DEVICE_NAME = android.os.Build.MANUFACTURER + " " + android.os.Build.MODEL;
    public static final String DATA_SENT_TO_OTT = "data_sent_to_ott";
    public static final String FROM_IN_APP = "FROM_IN_APP";
    public static final String DEVICE_ID = "device_id";
    public static final String EMAIL_ID = "email_id";
    public static final String EMAIL = "email";
    public static final String CARRYING_PAGE = "carrying_page";
    public static final String IS_FROM_DETAIL_PAGE = "is_from_details_page";
    public static final String EXTRA_LINK = "extra_link";
    public static final String ENGLISH = "en";
    public static final String OTHER = "od";
    public static final String ODIA = "od";
    public static final String WATCHLATER = "watchlater";
    public static final String FAVOURITE = "favourite";
    public static final String PLAYLIST = "playlist";

    //Webengage
    public static final String YOUR_WEBENGAGE_LICENSE_CODE = "";

    public static final String PAGE_NAME = "Page_Name";
    public static final String NAME = "Name";
    public static final String PHONE = "Phone";
    public static final String EMAILID = "Email_ID";
    public static final String USER_ADDRESS = "Address";
    public static final String USER_STATE = "State";
    public static final String USER_COUNTRY = "Country";
    public static final String DATE_OF_BIRTH = "DOB";
    public static final String CATEGORY_NAME = "Category_Name";
    public static final String CATEGORY_ID = "Category_ID";
    public static final String SUB_CATEGORY_NAME = "Sub_Category_Name";
    public static final String SUB_CATEGORY_ID = "Sub_Category_ID";
    public static final String BANNER_NAME = "Banner_Name";
    public static final String BANNER_ID = "Banner_ID";
    public static final String POSITION = "Position";
    public static final String SEARCH_QUERY = "Search_Query";

    public static final String TITLE_NAME = "Title_Name";
    public static final String CONTENT_TYPE = "Content_Type";
    public static final String TITLE_IMAGE = "Title_Image";
    public static final String TITLE_ID = "Title_ID";

    public static final String CURRENT_TIME = "Current_time";
    public static final String DURATION = "Duration";
    public static final String PERCENT_COMPLETE = "Percent_Complete";

    public static final String LANGUAGE = "Language";
    public static final String LOGOUT = "Logout";

    public static final String PLAN_NAME = "PlanName";
    public static final String PLAN_PRICE = "PlanPrice";
    public static final String STATUS = "Status";

    public static final String HomeScreen = "HomeScreen";
    public static final String LoginScreen = "LoginScreen";
    public static final String ShowDetailsScreen = "ShowDetailsScreen";
    public static final String EpisodeDetailsScreen = "EpisodeDetailsScreen";
    public static final String MovieDetailsScreen = "MovieDetailsScreen";
    public static final String TrailerDetailsScreen = "TrailerDetailsScreen";
    public static final String LiveTvScreen = "LiveTvScreen";
    public static final String Search = "Search";
    public static final String TV = "TV";
    public static final String ME = "ME";
    public static final String ViewPlans = "ViewPlans";
    public static final String PlayList = "PlayList";
    public static final String Login = "Login";
    public static final String Registration = "Registration";
    public static final String Settings = "Settings";
    public static final String UserDetails = "UserDetails";
    public static final String EditUserDetails = "EditUserDetails";
    public static final String WatchList = "WatchList";

    public static final String WHAT = "what";
    public static final String LANGUAGECHANGES = "com.otl.tarangplus.language_change";
    public static final String GOOGLEADID = "ca-app-pub-3940256099942544/1033173712";
    //Test : ca-app-pub-3940256099942544/1033173712
    //prod : ca-app-pub-1011955008088827~5947291329
    public static final String GOOGLE = "Google";
    public static final String FACEBOOK = "Facebook";
    public static final String LOGIN_TYPE = "login_type";
    public static final String FACEBOOK_LOGIN = "facebook";
    public static final String PLAY_URL = "play_url";

    public static String IP = "";
    public static int CURRENT_BITRATE = 0;
    public static final String FROM_WHERE = "fromwhere";
    public static String REGION = "";
    public static final String REGION_TAG = "region";
    //public static final String API_KEY = "Ts4XpMvGsB2SW7NZsWc3";
    public static final String API_KEY = "Unxs6bxjw2xab7Tsw64F";

    public static final String FIRST_INSTALLED = "first_installed";

    public static final String YES = "yes";
    public static final String NO = "no";

    /*  t_16_9_banner
        t_16_9_livebanner
        t_16_9_big_meta
        t_2_3_big_meta
     */

    public static final String T_16_9_LIVEBANNER = "t_16_9_livebanner";
    public static final String T_2_3_BIG_META = "t_2_3_big_meta";

    public static final String T_CONTINUE_WATCHING = "t_continue_watching";
    public static final String T_2_3_MOVIE = "t_2_3_movie";
    public static final String T_2_3_small = "t_2_3_small";

    public static final String T_16_9_BIG = "t_16_9_big";
    public static final String T_16_9_BIG_RIGHT_META = "t_16_9_right_meta";
    public static final String T_16_9_BIG_WITHOUGHT_META = "t_16_9_big_without_meta";

    public static final String T_16_9_BIG_META = "t_16_9_big_meta";
    public static final String T_16_9_SMALL = "t_16_9_small";

    public static final String T_1_1_PLAIN = "t_1_1_plain";
    public static final String T_1_1_ALBUM = "t_1_1_album";
    public static final String T_16_9_EPG = "t_16_9_epg";
    public static final String T_COMB_16_9 = "t_comb_16_9_list";
    public static final String T_COMB_16_9_IMAGE = "t_comb_16_9_image";
    public static final String T_COMB_1_1_LIST = "t_comb_1_1_list";
    public static final String T_COMB_1_1_IMAGE = "t_comb_1_1_image";
    public static final String T_16_9_BANNER = "t_16_9_banner";
    public static final String T_2_3_MOVIE_STATIC = "t_2_3_movie_static";
    public static final String HOME_LINK = "homeLink";
    public static final String DISPLAY_TITLE = "displayTitle";
    public static final String FRIENDLY_ID = "friendly_id";
    public static final String CONTENT_ID = "item_id";
    public static final String CATALOG_ID = "catalog_id";
    public static final String LAYOUT_TYPE_SELECTED = "layoutTypeSelected";
    public static final String THEME = "theme";
    public static final String PROFILE_NAME = "profile_name";
    public static final String PROFILE_ID = "profile_id";
    public static final String IS_KID_PROF = "kid_profile";
    public final static String T_16_9_SMALL_META_LAYOUT = "t_16_9_small_meta";
    public final static String T_1_1_ALBUM_META = "t_1_1_album_meta";
    public final static String T_SUBSCRIPTION = "t_subscription";
    public static final String IS_EPISODE = "is_episode";
    public static final String PLAYER_AUTH_TOKEN = "ywVXaTzycwZ8agEs3ujx";
    public static final String ASCENDING_ORDER = "asc";
    public static final String DECENDINGDING_ORDER = "desc";
    public static final String IS_FROM_LOGIN = "is_from_login";
    public static final String WATCH_LATER = "watchlater";
    public static final String DEEP_LINK_URL = "deeplinkUrl";
    public static final long ANALYTICS_INTERVAL = 10000; // 10 secs for analytics

    public static final int ACTION_LOGIN_CLICKED = 100;
    public static final int ACTION_SIGNUP_CLICKED = 102;
    public static final int ACTION_ME_LOGIN = 101;
    public static final String FROM_PAGE = "from";
    public static final String USER_ID = "user_id";
    public static final String TYPE = "type";
    public static final String INDIA = "IN";
    public static final String USA = "US";
    public static final String FREE = "free";
    public static final String PAID = "paid";
    public static String CUR_SESSION_ID = "";
    public static List<PlayListItem> PLAYLISTITEMS = new ArrayList<>(); // Main
    public static List<PlayListItem> PLAYLISTITEMSFORCURRENTITEM = new ArrayList<>(); // current item
    public static void addToLocalList(List<PlayListItem> playListItemList){
        PLAYLISTITEMS.clear();
        if(playListItemList != null){



            for (PlayListItem playListItem: playListItemList){

                if(playListItem.getName().equalsIgnoreCase("Favourite") ||
                        playListItem.getName().equalsIgnoreCase("Watch Later") ||
                        playListItem.getName().equalsIgnoreCase("Watch History") ){

                }else {
                    PLAYLISTITEMS.add(playListItem);
                }

            }

        }

    }


    private static int[] colors = new int[2];
    public static final String LAYOUT_SCHEME = "layout_scheme";
    public static final String IS_LOGGED_IN = "isLoggedIn";
    public static final String SHOW_ID = "showId";
    public static final String IS_KID_PROFILE_SELECTED = "kid_profile";
    public static String PAYMENTURL = "";
    public static String USER_LOGIN_ID = "";
    public static String SESSION_ID = "";

    public static String STATE = "";
    public static String CITY = "";
    public static String COUNTRY = "";

    public static String STATE_KEY = "state";
    public static String CITY_KEY = "city";
    public static String COUNTRY_KEY = "country";
    public static final String ADDRESS = "address";

    public static String SETPACKDETAILSTEXT = "pack_details_text";
    public static String REGISTERED_DEVICES_URL = "";
    public static Gson gson = new Gson();
    public static boolean IS_APP_LAUNCH_EVENT_FIRED = false;
    public static final String NON_MEDIA = "non-media";
    public static final String LINK_VIEW_PLAN = "view_plans";
    public static final String LINK_REGISTER = "register";
    public static final String LINK_LOGIN = "login";
    public static final String STATIC_BANNER = "static_banner";
    public static final String PREMIER = "premier";
    public static final String START_TRAIL_MESSAGE = "start_trail_message";
    public static ArrayList<String> tabsPresent = new ArrayList<String>();
    public static boolean FULLSCREENFLAG;

    public static final String EVENT_VALUE = "e_v1" ;
    public static final String MEDIA_EVENT_NAME = "media_playback";
    public static final String APP_EVENTS = "app_events";
    public static final int DEFAULT_EVENT_VALUE = 1;
    public static final String EVENT_TYPE_VALUE = "";
    public static final String EVENT_TYPE_PLAY ="play" ;
    public static final String EVENT_TYPE_PLAY_COMPLETE ="play_complete" ;
    public static final String EVENT_TYPE_BUFFER ="buffer" ;
    public static final String EVENT_TYPE_PLAYBACK_TIME ="playback_time" ;

    /*event_value(e_v)
  application(app)
  event_time(e_tm)
  event_type(e_t)
  media_language(m_l)
  media_content_type(m_c_t)
  media_genre(m_g)
  video_id(v_id)
  title_category(t_cat)
  total_duration(t_dur)
  title(title)
  user_state(u_st)
  user_period(u_p)
  user_plan_type(u_p_r)
  user_id(u_id)
  show_title(s_t)
  content_owner(c_o)
  list_name(l_name)*/
    public static final String APP_CAMPAIGN_EVENT="app_campaign_event";
    public static final String UTM_SOURCE = "utm_source";
    public static final String UTM_MEDIUM = "utm_medium";
    public static final String UTM_CONTENT = "utm_content";
    public static final String UTM_CAMPAIGN = "utm_campaign";
    public static final String UTM_TERM   = "utm_term";
    public static final String APPLICATION = "app";
    public static final String EVENT_TIME = "e_tm";
    public static final String EVENT_TYPE = "e_t";
    public static final String MEDIA_LANGUAGE = "m_l";
    public static final String MEDIA_CONTENT_TYPE = "m_c_t";
    public static final String MEDIA_GENRE = "m_g";
    public static final String VIDEO_ID ="v_id";
    public static final String TITLE_CATEGORY = "t_cat";
    public static final String TOTAL_DURATION = "t_dur";
    public static final String TITLE = "title";
    public static final String USER_STATE_F = "u_st";
    public static final String USERS_PERIOD = "u_p";
    public static final String USERS_PLAN_TYPE = "u_p_r";
    public static final String USERS_ID = "u_id";
    public static final String SHOW_TITLE = "s_t";
    public static final String CONTENT_OWNER = "c_o";
    public static final String LIST_NAME ="l_name" ;
    public static final String CONTENT_OWNER_VALUE = "tarangplus";
    public static final String CLICK = "click";
    public static final String DETAILS_PAGE ="details_page" ;
    public static final String MOVIE_DETAILS_PAGE ="movie_details_page" ;
    public static final String VISIT = "vst";
    public static final String SEARCH_QUERY_F = "s_q";
    public static final String PAYMENT_EVENT = "payment_events";
    public static final String SHARE = "share";
    public static final String HOME_PAGE ="home_page" ;
    public static final String ME_PAGE = "me_page";
    public static final String USER_STATE_VALUE = "";
    public static final String ACTIVITY = "mainActivity";
    public static final String SEARCH = "search";
    public static final String PAGE_VISIT = "page_visit";
    public static final String SHOW_DEATILS = "show_details_page";
    public static final String LISTING_PAGE ="listing_page" ;
    public static final String CONTINUE_WATCHING_PAGE ="continue_watching_page";
    public static final String SHARE_URL = "share_url";
    public static final String ABOUT_US = "about_us";
    public static final String ORDER_ID = "o_id";
    public static final String SESSION_ID_PAMENT = "s_id" ;
    public static final String ANALYTICS_ID_PAYMENT = "a_id";
    public static final String PRICE = "pri";
    public static final String PAYMENT_TYPE = "p_typ";
    public static final String CURRENCY = "cur";
    public static final String PLAN_TYPE = "pln_name";
    public static final String KEYWORDS = "keywords";
    public static final String USER_ID_payment = "u_id";

    private static String TAG = Constants.class.getSimpleName();

    public static void setTextWithGradient(Activity activity, MyTextView myTextView, int startColor, int endcolor) {

        Shader myShader = new LinearGradient(
                0, 0, 0, 80,
                ContextCompat.getColor(activity, startColor), ContextCompat.getColor(activity, endcolor),
                Shader.TileMode.CLAMP);
        myTextView.getPaint().setShader(myShader);
    }

    public static int getDeviceWidth(Context context) {
        if (context != null) {
            DisplayMetrics matrix = context.getResources().getDisplayMetrics();
            return matrix.widthPixels;
        } else
            return 0;
    }

    public static void donoWhyButNeeded(Activity context) {
        /**
         * If this method is called all is good.. else ur in deep s***
         * */

      /*  if (context != null) {
            Display display = context.getWindowManager().getDefaultDisplay();
            DisplayMetrics matrix = context.getResources().getDisplayMetrics();

            Point size = new Point();
            display.getRealSize(size);
            display.getRealMetrics(matrix);
            int width = size.x;
            int height = size.y;

            width = matrix.widthPixels;
            height = matrix.heightPixels;
        }*/
    }

    public static int getDeviceHeight(Context context) {
        if (context != null) {
            DisplayMetrics matrix = context.getResources().getDisplayMetrics();
            return matrix.heightPixels;
        } else
            return 0;
    }

    public static List<LayoutDbScheme> layoutDbScheme;

    public static LayoutDbScheme getSchemeColor(String val) {
        val = val.replace("-", "_");
        if (layoutDbScheme != null)
            for (int i = 0; i < layoutDbScheme.size(); i++) {
                if (layoutDbScheme.get(i).getScheme().equalsIgnoreCase(val)) {
                    return layoutDbScheme.get(i);
                }
            }
        return null;
    }


    public static GradientDrawable getbackgroundGradient(LayoutDbScheme schemeval) {
        colors[0] = Color.parseColor("#E6" + schemeval.getStartColor());
        colors[1] = Color.parseColor("#E6" + schemeval.getEndColor());
        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.BL_TR, colors);
        gd.setGradientType(GradientDrawable.LINEAR_GRADIENT);
//        gd.setCornerRadius(0f);

        return gd;
    }

    public static String getAppVersion() {
        return "Version: " + BuildConfig.VERSION_NAME;
    }

    public static String getEstimatedTime(String startTime, String stopTime) {
        //2018-10-12 20:00:00 UTC
        //yyyy-MM-dd HH:mm:ss
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        String finalOutput = "";
        try {
            Date start = format.parse(startTime);
            Date end = format.parse(stopTime);
            SimpleDateFormat simpleDateFormat;
            if (Constants.REGION.equalsIgnoreCase("IN"))
                simpleDateFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
            else if (Constants.REGION.equalsIgnoreCase("US"))
                simpleDateFormat = new SimpleDateFormat("h:mm a", Locale.US);
            else
                simpleDateFormat = new SimpleDateFormat("h:mm a");

            String s1 = simpleDateFormat.format(start);
            String s2 = simpleDateFormat.format(end);
            finalOutput = s1 + " - " + s2;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return finalOutput;
    }

    public static String removeExtraWhiteSpace(String firstname) {
        if (firstname == null || TextUtils.isEmpty(firstname)) {
            return firstname;
        }
        StringBuilder builder = new StringBuilder(firstname);
        for (int i = 0; i < builder.length(); i++) {
            if (builder.length() == (i + 1)) {
                break;
            }
            if (builder.charAt(i) == ' ' && builder.charAt(i + 1) == ' ') {
                builder.deleteCharAt(i);
            }
        }
        return new String(builder).trim();
    }


    public class TABS {
        public static final String HOME = "HOME", SEARCH = "SEARCH", TV = "TV", ME = "ME";
    }

    public static int getStatusBarHight(Activity context) {
        Rect rectangle = new Rect();
        Window window = context.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
        int statusBarHeight = rectangle.top;
        int contentViewTop =
                window.findViewById(Window.ID_ANDROID_CONTENT).getTop();
        int titleBarHeight = contentViewTop - statusBarHeight;
        return (statusBarHeight * 2);
    }

    public static String getErrorMessage(ResponseBody responseBody) {
        try {
            JSONObject jsonObject = new JSONObject(responseBody.string());
            return ((JSONObject) jsonObject.get("error")).get("message").toString();
        } catch (Exception e) {
            return e.getMessage();
        }
    }


    public static DataError getErrorMessage(Throwable throwable) {
        try {
            ResponseBody responseBody = ((retrofit2.adapter.rxjava.HttpException) throwable).response().errorBody();


            String res = null;
            if (responseBody != null) {
                res = new JSONObject(responseBody.string()).toString();
            }

            if (TextUtils.isEmpty(res))
                res = "{\"error\":{\"code\":\"0\",\"message\":\" \"}}";
            return gson.fromJson(res, DataError.class);
        } catch (Exception e) {
            String res = "{\"error\":{\"code\":\"0\",\"message\":\" \"}}";
            return gson.fromJson(res, DataError.class);
        }

//        try {
//            ResponseBody responseBody = ((retrofit2.adapter.rxjava.HttpException) throwable).response().errorBody();
//            JSONObject jsonObject = new JSONObject(responseBody.string());
//            return ((JSONObject) jsonObject.get("error")).get("message").toString();
//        } catch (Exception e) {
//            return e.getMessage();
//        }
    }

    public static int getErrorCode(Throwable throwable) {
        try {
            ResponseBody responseBody = ((retrofit2.adapter.rxjava.HttpException) throwable).response().errorBody();
            JSONObject jsonObject = new JSONObject(responseBody.string());
            String code = (String) ((JSONObject) jsonObject.get("error")).get("code");
            return Integer.valueOf(code);
        } catch (Exception e) {
            return 500;
        }
    }

    public static int getNavigationHight(Context context) {
        if (context != null) {
            Resources resources = context.getResources();
            if (hasNavBar(resources)) {
                int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
                if (resourceId > 0) {
                    return resources.getDimensionPixelSize(resourceId);
                }
            }
            return 0;
        } else
            return 0;
    }

    private static boolean hasNavBar(Resources resources) {
        int id = resources.getIdentifier("config_showNavigationBar", "bool", "android");
        return id > 0 && resources.getBoolean(id);
    }

    public static Fragment getCurrentFragment(AppCompatActivity mActivity) {
        return mActivity.getSupportFragmentManager().findFragmentById(R.id.container);
    }

    public static boolean isEmailValied(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static class Type {

        public static final String TERMES_OF_USE = "termsOfUse";

        public static final String PRIVACY_POLICY = "privacyPolicy";

        public static final String CONTACT_US = "contactUs";

        public static final String DEEP_LINK_URL = "deeplinkUrl";
    }

    public static long parseTimeToMillis(String pos) {
        try {
            String[] data = pos.split(":");

            int hours = Integer.parseInt(data[0]);
            int minutes = Integer.parseInt(data[1]);
            int seconds = Integer.parseInt(data[2]);

            long time = seconds + 60 * minutes + 3600 * hours;

            return TimeUnit.MILLISECONDS.convert(time, TimeUnit.SECONDS);
        } catch (Exception e) {
            return 0;
        }

    }


    public static String getPreRoleAd(AccessControl accessControl) {
        if (accessControl.getPreRoleSettings() != null &&
                !TextUtils.isEmpty(accessControl.getPreRoleSettings().getAdsUrl()))
            return accessControl.getPreRoleSettings().getAdsUrl();
        return "";
    }

    public static String getPostRoleAd(AccessControl accessControl) {
        if (accessControl.getPostRoleSettings() != null &&
                !TextUtils.isEmpty(accessControl.getPostRoleSettings().getAdsUrl()))
            return accessControl.getPostRoleSettings().getAdsUrl();
//        return "https://pubads.g.doubleclick.net/gampad/ads?sz=640x480&iu=/124319096/external/ad_rule_samples&ciu_szs=300x250&ad_rule=1&impl=s&gdfp_req=1&env=vp&output=vmap&unviewed_position_start=1&cust_params=deployment%3Ddevsite%26sample_ar%3Dpremidpost&cmsid=496&vid=short_onecue&correlator=";
        return "";
    }

    public static String getMidRoleAd(AccessControl accessControl) {
        if (accessControl.getMidRoleSettings() != null &&
                !TextUtils.isEmpty(accessControl.getMidRoleSettings().getAdsUrl()))
            return accessControl.getMidRoleSettings().getAdsUrl();
//        return "https://pubads.g.doubleclick.net/gampad/ads?sz=640x480&iu=/124319096/external/ad_rule_samples&ciu_szs=300x250&ad_rule=1&impl=s&gdfp_req=1&env=vp&output=vmap&unviewed_position_start=1&cust_params=deployment%3Ddevsite%26sample_ar%3Dpremidpost&cmsid=496&vid=short_onecue&correlator=";
        return "";
    }

    public static ArrayList<Integer> getMidRoleAdPos(AccessControl accessControl) {
        if (accessControl.getMidRoleSettings() != null &&
                accessControl.getMidRoleSettings().getMidrollPositions() != null && !TextUtils.isEmpty(accessControl.getMidRoleSettings().getAdsUrl()))  // if midroll ad is empty we should not set intervals as it will show up in UI
            return accessControl.getMidRoleSettings().getMidrollPositions();
        return new ArrayList<>();
    }


    public static class CleverTapParams {
        public static final String IDENTITY = "Identity";
        public static final String ACTIVE_PLANS = "active_plans";
        public static final String INACTIVE_PLANS = "inactive_plans";
        public static final String IS_SUBSCRIBED = "is_subscribed";
        public static final String IS_LOGGEDIN = "is_loggedIn";
        public static final String COUNTRY_NAME = "country";
        public static final String STATE_NAME = "state";
        public static final String CITY_NAME = "city";
        public static final String APP_VERSION = "app_version";
        public static final String EMAIL_ID = "email_id";
        public static final String USER_PERIOD = "user_period";
        public static final String MOBILE_NO = "mobile_no";
        public static final String FIRST_NAME = "first_name";
        public static final String USER_PLAN_TYPE = "user_plan_type";

    }


}
