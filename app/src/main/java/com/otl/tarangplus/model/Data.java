
package com.otl.tarangplus.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class Data implements Parcelable {

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
    public Boolean getNewUser() {
        return newUser;
    }

    public void setNewUser(Boolean newUser) {
        this.newUser = newUser;
    }

    @SerializedName("new_user")
    @Expose
    private Boolean newUser;
    @SerializedName("user_info")
    @Expose
    private UserInfo userInfo;

    @SerializedName("genres")
    @Expose
    private List<String> genres;

    @SerializedName("playlists")
    @Expose
    private List<PlayList> playLists;

    public List<PlayList> getPlayLists() {
        return playLists;
    }

    @SerializedName("subs_categories")
    public List<String> subCategories;

    @SerializedName("subcategories")
    public List<Data> subCategoriesList;

    @SerializedName("associated_videos")
    public Boolean associatedVideos;

    @SerializedName("play_type")
    public Boolean playType;

    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("state")
    @Expose
    private String state;

    @SerializedName("country")
    @Expose
    private String country;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Boolean isAssociatedVideos() {
        return associatedVideos;
    }

    public void setAssociatedVideos(Boolean associatedVideos) {
        this.associatedVideos = associatedVideos;
    }

    public Boolean isPlayType() {
        return playType;
    }

    public void setPlayType(Boolean playType) {
        this.playType = playType;
    }

    public List<String> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<String> subCategories) {
        this.subCategories = subCategories;
    }

    public void setPlayLists(List<PlayList> playLists) {
        this.playLists = playLists;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }



    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    @SerializedName("language")
    @Expose
    private String language;

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @SerializedName("access_tag")
    @Expose
    private String accessTag;
    @SerializedName("ad_banner_ids")
    @Expose
    private List<Object> adBannerIds = null;
    @SerializedName("additional_keys")
    @Expose
    private List<Object> additionalKeys = null;
    @SerializedName("catalog_list_items")
    @Expose
    private List<CatalogListItem> catalogListItems = null;
    @SerializedName("category")
    @Expose
    private Boolean category;
    @SerializedName("cms_list_type")
    @Expose
    private String cmsListType;
    @SerializedName("display_count")
    @Expose
    private Object displayCount;
    @SerializedName("display_title")
    @Expose
    private String displayTitle;
    @SerializedName("duration_string")
    @Expose
    private String durationString;
    @SerializedName("filter_type")
    @Expose
    private String filterType;
    @SerializedName("friendly_id")
    @Expose
    private String friendlyId;
    @SerializedName("home_link")
    @Expose
    private String homeLink;
    @SerializedName("layout_structure")
    @Expose
    private String layoutStructure;
    @SerializedName("layout_scheme")
    @Expose
    private String layoutScheme;
    @SerializedName("layout_type")
    @Expose
    private String layoutType;
    @SerializedName("list_id")
    @Expose
    private String listId;
    @SerializedName("list_order")
    @Expose
    private Object listOrder;
    @SerializedName("list_type")
    @Expose
    private String listType;
    @SerializedName("logo")
    @Expose
    private String logo;
    @SerializedName("menu_link")
    @Expose
    private String menuLink;
    @SerializedName("ml_description")
    @Expose
    private List<Object> mlDescription = null;
    @SerializedName("ml_genres")
    @Expose
    private List<Object> mlGenres = null;
    @SerializedName("ml_language")
    @Expose
    private List<Object> mlLanguage = null;
    @SerializedName("publish_at_date")
    @Expose
    private String publishAtDate;
    @SerializedName("query_filter_types")
    @Expose
    private List<Object> queryFilterTypes = null;
    @SerializedName("region")
    @Expose
    private List<Object> region = null;
    @SerializedName("regions")
    @Expose
    private List<Object> regions = null;
    @SerializedName("secondary_layer")
    @Expose
    private List<Object> secondaryLayer = null;
    @SerializedName("smart_url")
    @Expose
    private String smartUrl;
    @SerializedName("play_url")
    @Expose
    private PlayUrl playUrl;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("tags")
    @Expose
    private List<String> tags = null;
    @SerializedName("thumbnails")
    @Expose
    private Thumbnails thumbnails;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("version")
    @Expose
    private String version;
    @SerializedName("theme")
    @Expose
    private String theme;
    @SerializedName("ad_unit_id")
    @Expose
    private String adUnitId;
    @SerializedName("ad_url")
    @Expose
    private String adUrl;
    @SerializedName("total_items_count")
    @Expose
    private Integer totalItemsCount;
    @SerializedName("platform_note")
    @Expose
    private String platformNote;
    @SerializedName("platform_code")
    @Expose
    private Integer platformCode;
    @SerializedName("content_id")
    @Expose
    private String contentId;
    @SerializedName("text_message")
    @Expose
    private String textMessage;
    @SerializedName("app_code")
    @Expose
    private Integer appCode;
    @SerializedName("params_hash")
    @Expose
    private ParamsHash paramsHash;
    @SerializedName("params_hash2")
    @Expose
    private ParamsHash2 paramsHash2;
    @SerializedName("app_version")
    @Expose
    private AppVersion appVersion;
    @SerializedName("order")
    @Expose
    private Integer order;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("catalog_id")
    @Expose
    private String catalogId;
    @SerializedName("service")
    @Expose
    private Object service;
    @SerializedName("items")
    @Expose
    private List<Item> items = null;
    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("description")
    @Expose
    private String description;
    @Expose
    @SerializedName("short_description")
    private String shortDescription;

    @SerializedName("profiles")
    @Expose
    private List<Profile> profiles = null;
    @SerializedName("auth_token")
    @Expose
    private String mAuthToken;
    @SerializedName("item_caption")
    @Expose
    private String itemCaption;
    @SerializedName("email_id")
    @Expose
    private String emailId;
    @SerializedName("mobile_number")
    @Expose
    private String mobileNumber;
    @SerializedName("session")
    @Expose
    private String session;
    @SerializedName("login_type")
    @Expose
    private String loginType;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("firstname")
    @Expose
    private String firstname;
    @SerializedName("lastname")
    @Expose
    private String lastname;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("profile_pic")
    @Expose
    private String profilePic;
    @SerializedName("first_time_login")
    @Expose
    private String firstTimeLogin;
    @SerializedName("add_profile")
    @Expose
    private Boolean addProfile;
    @SerializedName("item_additional_data")
    @Expose
    private Map<String, ArrayList> itemAdditionalData;

    @SerializedName("access_control")
    @Expose
    private AccessControl accessControl;

    @SerializedName("messages")
    @Expose
    private List<Message> messages = null;
    @SerializedName("message")
    @Expose
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @SerializedName("catalog_object")
    @Expose
    private CatalogObject catalogObject;
    @SerializedName("session_id")
    @Expose
    private String sessionId;
    @SerializedName("profile_obj")
    @Expose
    private ProfileObj profileObj;
    @SerializedName("channel_logo")
    @Expose
    private ChannelLogo channelLogo;
    @Expose
    @SerializedName("catalog_name")
    private String catalogName;
    @SerializedName("age")
    @Expose
    private String age;
    @SerializedName("dob")
    @Expose
    private String dob;

    @SerializedName("share_url")
    @Expose
    private String shareUrl;

    @SerializedName("parental_control")
    @Expose
    private String parentalControl;
    @SerializedName("parental_pin")
    @Expose
    private String parentalPin;
    @SerializedName("user_email_id")
    @Expose
    private String userEmialId;

    @SerializedName("ext_account_email_id")
    @Expose
    private String ext_account_email_id;

    @SerializedName("birthdate")
    @Expose
    private String birthDate;
    @SerializedName("preview")
    @Expose
    private Preview preview;
    @SerializedName("is_subscribed")
    @Expose
    private boolean isSubscribed;

    @SerializedName("active_plans")
    @Expose
    public List<String> activePlans;

    @SerializedName("inactive_plans")
    @Expose
    public List<String> inActivePlans;
    @SerializedName("plan_category_type")
    @Expose
    private String planCategoryType;
    @SerializedName("show_id")
    @Expose
    private String showID;
    @SerializedName("is_device_limit_status")
    @Expose
    private boolean deviceLimitFlag;
    @SerializedName("campaign_data")
    @Expose
    private CampaignData campaignData;
    @SerializedName("primary_id")
    @Expose
    private String primaryId;

    @SerializedName("play_url_type")
    @Expose
    private String playUrlType;

    @SerializedName("channel_name")
    @Expose
    private String channelName;

    @SerializedName("media_type")
    @Expose
    private String mediaType;


    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getPlayUrlType() {
        return playUrlType;
    }

    public void setPlayUrlType(String playUrlType) {
        this.playUrlType = playUrlType;
    }

    public boolean isDeviceLimitFlag() {
        return deviceLimitFlag;
    }

    public void setDeviceLimitFlag(boolean deviceLimitFlag) {
        this.deviceLimitFlag = deviceLimitFlag;
    }

    public CampaignData getCampaignData() {
        return campaignData;
    }

    public void setCampaignData(CampaignData campaignData) {
        this.campaignData = campaignData;
    }

    public String getShowID() {
        return showID;
    }

    public void setShowID(String showID) {
        this.showID = showID;
    }

    public String getPlanCategoryType() {
        return planCategoryType;
    }

    public void setPlanCategoryType(String planCategoryType) {
        this.planCategoryType = planCategoryType;
    }

    public List<String> getActivePlans() {
        return activePlans;
    }

    public void setActivePlans(List<String> activePlans) {
        this.activePlans = activePlans;
    }

    public List<String> getInActivePlans() {
        return inActivePlans;
    }

    public void setInActivePlans(List<String> inActivePlans) {
        this.inActivePlans = inActivePlans;
    }

    public boolean isSubscribed() {
        return isSubscribed;
    }

    public void setSubscribed(boolean subscribed) {
        isSubscribed = subscribed;
    }

    public Preview getPreview() {
        return preview;
    }

    public void setPreview(Preview preview) {
        this.preview = preview;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getUserEmialId() {
        return userEmialId;
    }

    public void setUserEmialId(String userEmialId) {
        this.userEmialId = userEmialId;
    }

    public String getParentalControl() {
        return parentalControl;
    }

    public void setParentalControl(String parentalControl) {
        this.parentalControl = parentalControl;
    }

    public String getParentalPin() {
        return parentalPin;
    }

    public void setParentalPin(String parentalPin) {
        this.parentalPin = parentalPin;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getCatalogName() {
        return catalogName;
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }

    public static Creator<Data> getCREATOR() {
        return CREATOR;
    }

    public void setItemAdditionalData(Map<String, ArrayList> itemAdditionalData) {
        this.itemAdditionalData = itemAdditionalData;
    }

    public ProfileObj getProfileObj() {
        return profileObj;
    }

    public void setProfileObj(ProfileObj profileObj) {
        this.profileObj = profileObj;
    }

    public ChannelLogo getChannelLogo() {
        return channelLogo;
    }

    public void setChannelLogo(ChannelLogo channelLogo) {
        this.channelLogo = channelLogo;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public CatalogObject getCatalogObject() {
        return catalogObject;
    }

    public void setCatalogObject(CatalogObject catalogObject) {
        this.catalogObject = catalogObject;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }


    public AccessControl getAccessControl() {
        return accessControl;
    }

    public void setAccessControl(AccessControl accessControl) {
        this.accessControl = accessControl;
    }

    public Map<String, ArrayList> getItemAdditionalData() {
        return itemAdditionalData;
    }

    public void setItemAdditionalData(TreeMap<String, ArrayList> itemAdditionalData) {
        this.itemAdditionalData = itemAdditionalData;
    }


    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getFirstTimeLogin() {
        return firstTimeLogin;
    }

    public void setFirstTimeLogin(String firstTimeLogin) {
        this.firstTimeLogin = firstTimeLogin;
    }

    public Boolean getAddProfile() {
        return addProfile;
    }

    public void setAddProfile(Boolean addProfile) {
        this.addProfile = addProfile;
    }

    public String getItemCaption() {
        return itemCaption;
    }

    public void setItemCaption(String itemCaption) {
        this.itemCaption = itemCaption;
    }

    public String getmAuthToken() {
        return mAuthToken;
    }

    public void setmAuthToken(String mAuthToken) {
        this.mAuthToken = mAuthToken;
    }

    public List<Profile> getProfiles() {
        return profiles;
    }

    public void setProfiles(List<Profile> profiles) {
        this.profiles = profiles;
    }

    public String getPlatformNote() {
        return platformNote;
    }

    public void setPlatformNote(String platformNote) {
        this.platformNote = platformNote;
    }

    public Integer getPlatformCode() {
        return platformCode;
    }

    public void setPlatformCode(Integer platformCode) {
        this.platformCode = platformCode;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    public Integer getAppCode() {
        return appCode;
    }

    public void setAppCode(Integer appCode) {
        this.appCode = appCode;
    }

    public ParamsHash getParamsHash() {
        return paramsHash;
    }

    public void setParamsHash(ParamsHash paramsHash) {
        this.paramsHash = paramsHash;
    }

    public ParamsHash2 getParamsHash2() {
        return paramsHash2;
    }

    public void setParamsHash2(ParamsHash2 paramsHash2) {
        this.paramsHash2 = paramsHash2;
    }

    public AppVersion getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(AppVersion appVersion) {
        this.appVersion = appVersion;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(String catalogId) {
        this.catalogId = catalogId;
    }

    public Object getService() {
        return service;
    }

    public void setService(Object service) {
        this.service = service;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }


    public String getAccessTag() {
        return accessTag;
    }

    public void setAccessTag(String accessTag) {
        this.accessTag = accessTag;
    }

    public List<Object> getAdBannerIds() {
        return adBannerIds;
    }

    public void setAdBannerIds(List<Object> adBannerIds) {
        this.adBannerIds = adBannerIds;
    }

    public List<Object> getAdditionalKeys() {
        return additionalKeys;
    }

    public void setAdditionalKeys(List<Object> additionalKeys) {
        this.additionalKeys = additionalKeys;
    }

    public List<CatalogListItem> getCatalogListItems() {
        return catalogListItems;
    }

    public void setCatalogListItems(List<CatalogListItem> catalogListItems) {
        this.catalogListItems = catalogListItems;
    }

    public Boolean getCategory() {
        return category;
    }

    public void setCategory(Boolean category) {
        this.category = category;
    }

    public String getCmsListType() {
        return cmsListType;
    }

    public void setCmsListType(String cmsListType) {
        this.cmsListType = cmsListType;
    }

    public Object getDisplayCount() {
        return displayCount;
    }

    public void setDisplayCount(Object displayCount) {
        this.displayCount = displayCount;
    }

    public String getDisplayTitle() {
        return displayTitle;
    }

    public void setDisplayTitle(String displayTitle) {
        this.displayTitle = displayTitle;
    }

    public String getDurationString() {
        return durationString;
    }

    public void setDurationString(String durationString) {
        this.durationString = durationString;
    }

    public String getFilterType() {
        return filterType;
    }

    public void setFilterType(String filterType) {
        this.filterType = filterType;
    }

    public String getFriendlyId() {
        return friendlyId;
    }

    public void setFriendlyId(String friendlyId) {
        this.friendlyId = friendlyId;
    }

    public String getHomeLink() {
        return homeLink;
    }

    public void setHomeLink(String homeLink) {
        this.homeLink = homeLink;
    }

    public String getLayoutStructure() {
        return layoutStructure;
    }

    public void setLayoutStructure(String layoutStructure) {
        this.layoutStructure = layoutStructure;
    }

    public String getLayoutScheme() {
        return layoutScheme;
    }

    public void setLayoutScheme(String layoutScheme) {
        this.layoutScheme = layoutScheme;
    }

    public String getLayoutType() {
        return layoutType;
    }

    public void setLayoutType(String layoutType) {
        this.layoutType = layoutType;
    }

    public String getListId() {
        return listId;
    }

    public void setListId(String listId) {
        this.listId = listId;
    }

    public Object getListOrder() {
        return listOrder;
    }

    public void setListOrder(Object listOrder) {
        this.listOrder = listOrder;
    }

    public String getListType() {
        return listType;
    }

    public void setListType(String listType) {
        this.listType = listType;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getMenuLink() {
        return menuLink;
    }

    public void setMenuLink(String menuLink) {
        this.menuLink = menuLink;
    }

    public List<Object> getMlDescription() {
        return mlDescription;
    }

    public void setMlDescription(List<Object> mlDescription) {
        this.mlDescription = mlDescription;
    }

    public List<Object> getMlGenres() {
        return mlGenres;
    }

    public void setMlGenres(List<Object> mlGenres) {
        this.mlGenres = mlGenres;
    }

    public List<Object> getMlLanguage() {
        return mlLanguage;
    }

    public void setMlLanguage(List<Object> mlLanguage) {
        this.mlLanguage = mlLanguage;
    }

    public String getPublishAtDate() {
        return publishAtDate;
    }

    public void setPublishAtDate(String publishAtDate) {
        this.publishAtDate = publishAtDate;
    }

    public List<Object> getQueryFilterTypes() {
        return queryFilterTypes;
    }

    public void setQueryFilterTypes(List<Object> queryFilterTypes) {
        this.queryFilterTypes = queryFilterTypes;
    }

    public List<Object> getRegion() {
        return region;
    }

    public void setRegion(List<Object> region) {
        this.region = region;
    }

    public List<Object> getRegions() {
        return regions;
    }

    public void setRegions(List<Object> regions) {
        this.regions = regions;
    }

    public List<Object> getSecondaryLayer() {
        return secondaryLayer;
    }

    public void setSecondaryLayer(List<Object> secondaryLayer) {
        this.secondaryLayer = secondaryLayer;
    }

    public String getSmartUrl() {
        return smartUrl;
    }

    public void setSmartUrl(String smartUrl) {
        this.smartUrl = smartUrl;
    }

    public PlayUrl getPlayUrl() {
        return playUrl;
    }

    public void setPlayUrl(PlayUrl playUrl) {
        this.playUrl = playUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Thumbnails getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(Thumbnails thumbnails) {
        this.thumbnails = thumbnails;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getAdUnitId() {
        return adUnitId;
    }

    public void setAdUnitId(String adUnitId) {
        this.adUnitId = adUnitId;
    }

    public String getAdUrl() {
        return adUrl;
    }

    public void setAdUrl(String adUrl) {
        this.adUrl = adUrl;
    }

    public Integer getTotalItemsCount() {
        return totalItemsCount;
    }

    public void setTotalItemsCount(Integer totalItemsCount) {
        this.totalItemsCount = totalItemsCount;
    }

    public String getPrimaryId() {
        return primaryId;
    }

    public void setPrimaryId(String primaryId) {
        this.primaryId = primaryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Data data = (Data) o;
        return Objects.equals(accessTag, data.accessTag) &&
                Objects.equals(adBannerIds, data.adBannerIds) &&
                Objects.equals(additionalKeys, data.additionalKeys) &&
                Objects.equals(catalogListItems, data.catalogListItems) &&
                Objects.equals(category, data.category) &&
                Objects.equals(cmsListType, data.cmsListType) &&
                Objects.equals(displayCount, data.displayCount) &&
                Objects.equals(displayTitle, data.displayTitle) &&
                Objects.equals(durationString, data.durationString) &&
                Objects.equals(filterType, data.filterType) &&
                Objects.equals(friendlyId, data.friendlyId) &&
                Objects.equals(homeLink, data.homeLink) &&
                Objects.equals(layoutStructure, data.layoutStructure) &&
                Objects.equals(layoutScheme, data.layoutScheme) &&
                Objects.equals(layoutType, data.layoutType) &&
                Objects.equals(listId, data.listId) &&
                Objects.equals(listOrder, data.listOrder) &&
                Objects.equals(listType, data.listType) &&
                Objects.equals(logo, data.logo) &&
                Objects.equals(menuLink, data.menuLink) &&
                Objects.equals(mlDescription, data.mlDescription) &&
                Objects.equals(mlGenres, data.mlGenres) &&
                Objects.equals(mlLanguage, data.mlLanguage) &&
                Objects.equals(publishAtDate, data.publishAtDate) &&
                Objects.equals(queryFilterTypes, data.queryFilterTypes) &&
                Objects.equals(region, data.region) &&
                Objects.equals(regions, data.regions) &&
                Objects.equals(secondaryLayer, data.secondaryLayer) &&
                Objects.equals(smartUrl, data.smartUrl) &&
                Objects.equals(status, data.status) &&
                Objects.equals(tags, data.tags) &&
                Objects.equals(thumbnails, data.thumbnails) &&
                Objects.equals(title, data.title) &&
                Objects.equals(version, data.version) &&
                Objects.equals(theme, data.theme) &&
                Objects.equals(adUnitId, data.adUnitId) &&
                Objects.equals(adUrl, data.adUrl) &&
                Objects.equals(channelName, data.channelName) &&
                Objects.equals(playUrlType, data.playUrlType) &&
                Objects.equals(ext_account_email_id, data.ext_account_email_id) &&
                Objects.equals(totalItemsCount, data.totalItemsCount);
    }

    @Override
    public int hashCode() {

        return Objects.hash(accessTag, adBannerIds, additionalKeys, catalogListItems, category, cmsListType, displayCount, displayTitle, durationString, filterType, friendlyId, homeLink, layoutStructure, layoutScheme, layoutType, listId, listOrder, listType, logo, menuLink, mlDescription, mlGenres, mlLanguage, publishAtDate, queryFilterTypes, region, regions, secondaryLayer, smartUrl, status, tags, thumbnails, title, version, theme, adUnitId, adUrl, totalItemsCount, ext_account_email_id, channelName, playUrlType);
    }

    @Override
    public String toString() {
        return "Data{" +
                "accessTag='" + accessTag + '\'' +
                "channelName='" + channelName + '\'' +
                "playUrlType='" + playUrlType + '\'' +
                ", adBannerIds=" + adBannerIds +
                ", additionalKeys=" + additionalKeys +
                ", catalogListItems=" + catalogListItems +
                ", category=" + category +
                ", cmsListType='" + cmsListType + '\'' +
                ", displayCount=" + displayCount +
                ", displayTitle='" + displayTitle + '\'' +
                ", durationString='" + durationString + '\'' +
                ", filterType='" + filterType + '\'' +
                ", friendlyId='" + friendlyId + '\'' +
                ", homeLink='" + homeLink + '\'' +
                ", layoutStructure='" + layoutStructure + '\'' +
                ", layoutScheme='" + layoutScheme + '\'' +
                ", layoutType='" + layoutType + '\'' +
                ", listId='" + listId + '\'' +
                ", listOrder=" + listOrder +
                ", listType='" + listType + '\'' +
                ", logo='" + logo + '\'' +
                ", menuLink='" + menuLink + '\'' +
                ", mlDescription=" + mlDescription +
                ", mlGenres=" + mlGenres +
                ", mlLanguage=" + mlLanguage +
                ", publishAtDate='" + publishAtDate + '\'' +
                ", queryFilterTypes=" + queryFilterTypes +
                ", region=" + region +
                ", regions=" + regions +
                ", secondaryLayer=" + secondaryLayer +
                ", smartUrl='" + smartUrl + '\'' +
                ", status='" + status + '\'' +
                ", tags=" + tags +
                ", thumbnails=" + thumbnails +
                ", title='" + title + '\'' +
                ", version='" + version + '\'' +
                ", theme='" + theme + '\'' +
                ", adUnitId='" + adUnitId + '\'' +
                ", adUrl='" + adUrl + '\'' +
                ", address='" + address + '\'' +
                ", country='" + country + '\'' +
                ", state='" + state + '\'' +
                ", ext_account_email_id='" + ext_account_email_id + '\'' +
                ", totalItemsCount=" + totalItemsCount +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeByte((byte) (newUser == null ? 0 : newUser ? 1 : 2));
        dest.writeStringList(genres);
        dest.writeStringList(subCategories);
        dest.writeTypedList(subCategoriesList);
        dest.writeByte((byte) (associatedVideos == null ? 0 : associatedVideos ? 1 : 2));
        dest.writeByte((byte) (playType == null ? 0 : playType ? 1 : 2));
        dest.writeString(url);
        dest.writeString(address);
        dest.writeString(state);
        dest.writeString(country);
        dest.writeString(language);
        dest.writeString(accessTag);
        dest.writeTypedList(catalogListItems);
        dest.writeByte((byte) (category == null ? 0 : category ? 1 : 2));
        dest.writeString(cmsListType);
        dest.writeString(displayTitle);
        dest.writeString(durationString);
        dest.writeString(filterType);
        dest.writeString(friendlyId);
        dest.writeString(homeLink);
        dest.writeString(layoutStructure);
        dest.writeString(layoutScheme);
        dest.writeString(layoutType);
        dest.writeString(listId);
        dest.writeString(listType);
        dest.writeString(logo);
        dest.writeString(menuLink);
        dest.writeString(publishAtDate);
        dest.writeString(smartUrl);
        dest.writeString(status);
        dest.writeStringList(tags);
        dest.writeString(title);
        dest.writeString(version);
        dest.writeString(theme);
        dest.writeString(adUnitId);
        dest.writeString(adUrl);
        if (totalItemsCount == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(totalItemsCount);
        }
        dest.writeString(platformNote);
        if (platformCode == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(platformCode);
        }
        dest.writeString(contentId);
        dest.writeString(textMessage);
        if (appCode == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(appCode);
        }
        if (order == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(order);
        }
        dest.writeString(type);
        dest.writeString(catalogId);
        dest.writeTypedList(items);
        if (count == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(count);
        }
        dest.writeString(description);
        dest.writeString(shortDescription);
        dest.writeTypedList(profiles);
        dest.writeString(mAuthToken);
        dest.writeString(itemCaption);
        dest.writeString(emailId);
        dest.writeString(mobileNumber);
        dest.writeString(session);
        dest.writeString(loginType);
        dest.writeString(userId);
        dest.writeString(firstname);
        dest.writeString(lastname);
        dest.writeString(name);
        dest.writeString(profilePic);
        dest.writeString(firstTimeLogin);
        dest.writeByte((byte) (addProfile == null ? 0 : addProfile ? 1 : 2));
        dest.writeString(message);
        dest.writeString(sessionId);
        dest.writeParcelable(channelLogo, flags);
        dest.writeString(catalogName);
        dest.writeString(age);
        dest.writeString(dob);
        dest.writeString(shareUrl);
        dest.writeString(parentalControl);
        dest.writeString(parentalPin);
        dest.writeString(userEmialId);
        dest.writeString(ext_account_email_id);
        dest.writeString(birthDate);
        dest.writeByte((byte) (isSubscribed ? 1 : 0));
        dest.writeStringList(activePlans);
        dest.writeStringList(inActivePlans);
        dest.writeString(planCategoryType);
        dest.writeString(showID);
        dest.writeByte((byte) (deviceLimitFlag ? 1 : 0));
        dest.writeString(primaryId);
        dest.writeString(playUrlType);
        dest.writeString(channelName);
        dest.writeString(mediaType);
    }


    protected Data(Parcel in) {
        byte tmpNewUser = in.readByte();
        newUser = tmpNewUser == 0 ? null : tmpNewUser == 1;
        genres = in.createStringArrayList();
        subCategories = in.createStringArrayList();
        subCategoriesList = in.createTypedArrayList(Data.CREATOR);
        byte tmpAssociatedVideos = in.readByte();
        associatedVideos = tmpAssociatedVideos == 0 ? null : tmpAssociatedVideos == 1;
        byte tmpPlayType = in.readByte();
        playType = tmpPlayType == 0 ? null : tmpPlayType == 1;
        url = in.readString();
        address = in.readString();
        state = in.readString();
        country = in.readString();
        language = in.readString();
        accessTag = in.readString();
        catalogListItems = in.createTypedArrayList(CatalogListItem.CREATOR);
        byte tmpCategory = in.readByte();
        category = tmpCategory == 0 ? null : tmpCategory == 1;
        cmsListType = in.readString();
        displayTitle = in.readString();
        durationString = in.readString();
        filterType = in.readString();
        friendlyId = in.readString();
        homeLink = in.readString();
        layoutStructure = in.readString();
        layoutScheme = in.readString();
        layoutType = in.readString();
        listId = in.readString();
        listType = in.readString();
        logo = in.readString();
        menuLink = in.readString();
        publishAtDate = in.readString();
        smartUrl = in.readString();
        status = in.readString();
        tags = in.createStringArrayList();
        title = in.readString();
        version = in.readString();
        theme = in.readString();
        adUnitId = in.readString();
        adUrl = in.readString();
        if (in.readByte() == 0) {
            totalItemsCount = null;
        } else {
            totalItemsCount = in.readInt();
        }
        platformNote = in.readString();
        if (in.readByte() == 0) {
            platformCode = null;
        } else {
            platformCode = in.readInt();
        }
        contentId = in.readString();
        textMessage = in.readString();
        if (in.readByte() == 0) {
            appCode = null;
        } else {
            appCode = in.readInt();
        }
        if (in.readByte() == 0) {
            order = null;
        } else {
            order = in.readInt();
        }
        type = in.readString();
        catalogId = in.readString();
        items = in.createTypedArrayList(Item.CREATOR);
        if (in.readByte() == 0) {
            count = null;
        } else {
            count = in.readInt();
        }
        description = in.readString();
        shortDescription = in.readString();
        profiles = in.createTypedArrayList(Profile.CREATOR);
        mAuthToken = in.readString();
        itemCaption = in.readString();
        emailId = in.readString();
        mobileNumber = in.readString();
        session = in.readString();
        loginType = in.readString();
        userId = in.readString();
        firstname = in.readString();
        lastname = in.readString();
        name = in.readString();
        profilePic = in.readString();
        firstTimeLogin = in.readString();
        byte tmpAddProfile = in.readByte();
        addProfile = tmpAddProfile == 0 ? null : tmpAddProfile == 1;
        message = in.readString();
        sessionId = in.readString();
        channelLogo = in.readParcelable(ChannelLogo.class.getClassLoader());
        catalogName = in.readString();
        age = in.readString();
        dob = in.readString();
        shareUrl = in.readString();
        parentalControl = in.readString();
        parentalPin = in.readString();
        userEmialId = in.readString();
        ext_account_email_id = in.readString();
        birthDate = in.readString();
        isSubscribed = in.readByte() != 0;
        activePlans = in.createStringArrayList();
        inActivePlans = in.createStringArrayList();
        planCategoryType = in.readString();
        showID = in.readString();
        deviceLimitFlag = in.readByte() != 0;
        primaryId = in.readString();
        playUrlType = in.readString();
        channelName = in.readString();
        mediaType = in.readString();
    }

    public static final Creator<Data> CREATOR = new Creator<Data>() {
        @Override
        public Data createFromParcel(Parcel in) {
            return new Data(in);
        }

        @Override
        public Data[] newArray(int size) {
            return new Data[size];
        }
    };

    public List<Data> getSubCategoriesList() {
        return subCategoriesList;
    }

    public void setSubCategoriesList(List<Data> subCategoriesList) {
        this.subCategoriesList = subCategoriesList;
    }

    public String getExt_account_email_id() {
        return ext_account_email_id;
    }

    public void setExt_account_email_id(String ext_account_email_id) {
        this.ext_account_email_id = ext_account_email_id;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }
}
