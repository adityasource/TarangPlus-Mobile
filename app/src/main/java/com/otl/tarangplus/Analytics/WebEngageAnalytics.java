package com.otl.tarangplus.Analytics;

import com.otl.tarangplus.Utils.Constants;
import com.otl.tarangplus.model.CategoryEvents;
import com.otl.tarangplus.model.LoggedInData;
import com.otl.tarangplus.model.LoginEvents;
import com.otl.tarangplus.model.PageEvents;
import com.otl.tarangplus.model.PlansEvents;
import com.otl.tarangplus.model.PromoEvents;
import com.otl.tarangplus.model.SearchEvents;
import com.otl.tarangplus.model.TitleEvents;
import com.otl.tarangplus.model.UserEvents;
import com.webengage.sdk.android.Analytics;
import com.webengage.sdk.android.WebEngage;

import java.util.HashMap;
import java.util.Map;


public class WebEngageAnalytics {

    Analytics webAnalytics = WebEngage.get().analytics();


    public void homeEvent(PageEvents pageEvents){
        Map<String, Object> pageAttributes = new HashMap<>();
        pageAttributes.put(Constants.PAGE_NAME, pageEvents.getPageName());
        webAnalytics.track("Home Page",pageAttributes);
    }

    public void registeredEvent(UserEvents userEvents) {
        Map<String, Object> registerAttributes = new HashMap<>();
        registerAttributes.put(Constants.NAME, userEvents.getName());
        registerAttributes.put(Constants.PHONE, userEvents.getPhone());
        webAnalytics.track("User Registered",registerAttributes);
    }

    public void loginEvent(LoginEvents loginEvents) {
        Map<String, Object> loginAttributes = new HashMap<>();
        loginAttributes.put(Constants.EMAILID, loginEvents.getEmail_ID());
        loginAttributes.put(Constants.PHONE, loginEvents.getPhone());
        loginAttributes.put(Constants.LOGIN_TYPE, loginEvents.getType());
        webAnalytics.track("Login",loginAttributes);
    }

    public void promoClickEvent(PromoEvents promoEvents) {
        Map<String, Object> promoAttributes = new HashMap<>();
        promoAttributes.put(Constants.BANNER_NAME, promoEvents.getBanner_Name());
        promoAttributes.put(Constants.BANNER_ID, promoEvents.getBanner_ID());
        promoAttributes.put(Constants.POSITION, promoEvents.getPosition());
        webAnalytics.track("Promo Click",promoAttributes);
    }

    public void screenViewedEvent(PageEvents pageEvents) {
        Map<String, Object> screenAttributes = new HashMap<>();
        screenAttributes.put(Constants.PAGE_NAME, pageEvents.getPageName());
        webAnalytics.track("Screen Viewed",screenAttributes);
    }


    public void categoryEvent(CategoryEvents categoryEvents){
        Map<String, Object> categoryAttributes = new HashMap<>();
        categoryAttributes.put(Constants.CATEGORY_NAME, categoryEvents.getCategoryName());
        categoryAttributes.put(Constants.CATEGORY_ID, categoryEvents.getCategoryID());
        webAnalytics.track("Category Viewed", categoryAttributes);
    }

    public void subCategoryEvent(CategoryEvents categoryEvents){
        Map<String, Object> categoryAttributes = new HashMap<>();
        categoryAttributes.put(Constants.CATEGORY_NAME, categoryEvents.getCategoryName());
        categoryAttributes.put(Constants.CATEGORY_ID, categoryEvents.getCategoryID());
        categoryAttributes.put(Constants.SUB_CATEGORY_NAME, categoryEvents.getSub_Category_Name());
        categoryAttributes.put(Constants.SUB_CATEGORY_ID, categoryEvents.getSub_Category_ID());
        webAnalytics.track("Sub-Category Viewed", categoryAttributes);
    }

    public void titleViewedEvent(TitleEvents titleEvents){
        Map<String, Object> titleAttributes = new HashMap<>();
        titleAttributes.put(Constants.CATEGORY_NAME, titleEvents.getCategoryName());
        titleAttributes.put(Constants.CATEGORY_ID, titleEvents.getCategoryID());
        titleAttributes.put(Constants.SUB_CATEGORY_NAME, titleEvents.getCategoryName());
        titleAttributes.put(Constants.SUB_CATEGORY_ID, titleEvents.getCategoryID());
        titleAttributes.put(Constants.TITLE_NAME, titleEvents.getTitleName());
        titleAttributes.put(Constants.CONTENT_TYPE, titleEvents.getContentType());
        titleAttributes.put(Constants.TITLE_IMAGE, titleEvents.getTitleImage());
        titleAttributes.put(Constants.TITLE_ID, titleEvents.getTitleID());
        webAnalytics.track("Title Viewed", titleAttributes);
    }

    public void titlePlayedEvent(TitleEvents titleEvents){
        Map<String, Object> titleAttributes = new HashMap<>();
        titleAttributes.put(Constants.CATEGORY_NAME, titleEvents.getCategoryName());
        titleAttributes.put(Constants.CATEGORY_ID, titleEvents.getCategoryID());
        titleAttributes.put(Constants.SUB_CATEGORY_NAME, titleEvents.getCategoryName());
        titleAttributes.put(Constants.SUB_CATEGORY_ID, titleEvents.getCategoryID());
        titleAttributes.put(Constants.TITLE_NAME, titleEvents.getTitleName());
        titleAttributes.put(Constants.CONTENT_TYPE, titleEvents.getContentType());
        titleAttributes.put(Constants.TITLE_IMAGE, titleEvents.getTitleImage());
        titleAttributes.put(Constants.TITLE_ID, titleEvents.getTitleID());
        webAnalytics.track("Title Played", titleAttributes);
    }

    public void titleStartedEvent(TitleEvents titleEvents){
        Map<String, Object> titleAttributes = new HashMap<>();
        titleAttributes.put(Constants.CATEGORY_NAME, titleEvents.getCategoryName());
        titleAttributes.put(Constants.CATEGORY_ID, titleEvents.getCategoryID());
        titleAttributes.put(Constants.SUB_CATEGORY_NAME, titleEvents.getCategoryName());
        titleAttributes.put(Constants.SUB_CATEGORY_ID, titleEvents.getCategoryID());
        titleAttributes.put(Constants.TITLE_NAME, titleEvents.getTitleName());
        titleAttributes.put(Constants.CONTENT_TYPE, titleEvents.getContentType());
        titleAttributes.put(Constants.TITLE_IMAGE, titleEvents.getTitleImage());
        titleAttributes.put(Constants.TITLE_ID, titleEvents.getTitleID());
        webAnalytics.track("Title Started", titleAttributes);
    }


    public void titleProgressEvent(TitleEvents titleEvents){
        Map<String, Object> titleAttributes = new HashMap<>();
        titleAttributes.put(Constants.CATEGORY_NAME, titleEvents.getCategoryName());
        titleAttributes.put(Constants.CATEGORY_ID, titleEvents.getCategoryID());
        titleAttributes.put(Constants.SUB_CATEGORY_NAME, titleEvents.getCategoryName());
        titleAttributes.put(Constants.SUB_CATEGORY_ID, titleEvents.getCategoryID());
        titleAttributes.put(Constants.TITLE_NAME, titleEvents.getTitleName());
        titleAttributes.put(Constants.CONTENT_TYPE, titleEvents.getContentType());
        titleAttributes.put(Constants.TITLE_IMAGE, titleEvents.getTitleImage());
        titleAttributes.put(Constants.TITLE_ID, titleEvents.getTitleID());
        titleAttributes.put(Constants.CURRENT_TIME, titleEvents.getCurrentTime());
        titleAttributes.put(Constants.DURATION, titleEvents.getDuration());
        titleAttributes.put(Constants.PERCENT_COMPLETE, titleEvents.getPercentComplete());
        webAnalytics.track("Title Progress", titleAttributes);
    }

    public void titleCompletedEvent(TitleEvents titleEvents){
        Map<String, Object> titleAttributes = new HashMap<>();
        titleAttributes.put(Constants.CATEGORY_NAME, titleEvents.getCategoryName());
        titleAttributes.put(Constants.CATEGORY_ID, titleEvents.getCategoryID());
        titleAttributes.put(Constants.SUB_CATEGORY_NAME, titleEvents.getCategoryName());
        titleAttributes.put(Constants.SUB_CATEGORY_ID, titleEvents.getCategoryID());
        titleAttributes.put(Constants.TITLE_NAME, titleEvents.getTitleName());
        titleAttributes.put(Constants.CONTENT_TYPE, titleEvents.getContentType());
        titleAttributes.put(Constants.TITLE_IMAGE, titleEvents.getTitleImage());
        titleAttributes.put(Constants.TITLE_ID, titleEvents.getTitleID());
        webAnalytics.track("Title Completed", titleAttributes);
    }


    public void  watchLaterEvent(TitleEvents titleEvents){
        Map<String, Object> titleAttributes = new HashMap<>();
        titleAttributes.put(Constants.CATEGORY_NAME, titleEvents.getCategoryName());
        titleAttributes.put(Constants.CATEGORY_ID, titleEvents.getCategoryID());
        titleAttributes.put(Constants.SUB_CATEGORY_NAME, titleEvents.getCategoryName());
        titleAttributes.put(Constants.SUB_CATEGORY_ID, titleEvents.getCategoryID());
        titleAttributes.put(Constants.TITLE_NAME, titleEvents.getTitleName());
        titleAttributes.put(Constants.CONTENT_TYPE, titleEvents.getContentType());
        titleAttributes.put(Constants.TITLE_IMAGE, titleEvents.getTitleImage());
        titleAttributes.put(Constants.TITLE_ID, titleEvents.getTitleID());
        webAnalytics.track("Added to Watch Later", titleAttributes);
    }

    public void updateAccount(UserEvents userEvents){
        Map<String, Object> userAttributes = new HashMap<>();
        userAttributes.put(Constants.NAME, userEvents.getName());
        userAttributes.put(Constants.PHONE, userEvents.getPhone());
        userAttributes.put(Constants.EMAILID, userEvents.getEmail());
        userAttributes.put(Constants.USER_ADDRESS, userEvents.getAddress());
        userAttributes.put(Constants.USER_STATE, userEvents.getState());
        userAttributes.put(Constants.USER_COUNTRY, userEvents.getCountry());
        userAttributes.put(Constants.DATE_OF_BIRTH, userEvents.getDOB());
        webAnalytics.track("Account Details Updated", userAttributes);

    }

    public void languageChosen(String language){
        Map<String, Object> languageAttributes = new HashMap<>();
        languageAttributes.put(Constants.LANGUAGE,language);
        webAnalytics.track("Language Chosen",languageAttributes);

    }

    public void search(SearchEvents searchEvents){
        Map<String, Object> searchAttributes = new HashMap<>();
        searchAttributes.put(Constants.SEARCH_QUERY,searchEvents.getSearchQuery());
        webAnalytics.track("Search",searchAttributes);

    }

    public void watchlistViewed(PageEvents pageEvents){
        Map<String, Object> pageAttributes = new HashMap<>();
        pageAttributes.put(Constants.PAGE_NAME, pageEvents.getPageName());
        webAnalytics.track("Watchlist Viewed",pageAttributes);
    }

    public void logout(String logout){
        Map<String, Object> logoutAttributes = new HashMap<>();
        logoutAttributes.put(Constants.LOGOUT,logout);
        webAnalytics.track("Logout",logoutAttributes);

    }






}
