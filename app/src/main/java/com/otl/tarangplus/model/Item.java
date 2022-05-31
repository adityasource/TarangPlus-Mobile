
package com.otl.tarangplus.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class Item implements Parcelable {


    @SerializedName("channel_name")
    @Expose
    private String channelName;

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("content_id")
    @Expose
    private String contentId;
    @SerializedName("thumbnails")
    @Expose
    private Thumbnails thumbnails;
    @SerializedName("genres")
    @Expose
    private List<String> genres = null;
    @SerializedName("display_genres")
    @Expose
    private List<String> displayGenres = null;
    @SerializedName("language")
    @Expose
    private String language;
    @SerializedName("catalog_id")
    @Expose
    private String catalogId;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("keywords")
    @Expose
    private String keywords;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("catalog_object")
    @Expose
    private CatalogObject catalogObject;
    @SerializedName("episode_count")
    @Expose
    private Integer episodeCount;
    @SerializedName("no_of_user_rated")
    @Expose
    private Integer noOfUserRated;
    @SerializedName("average_user_rating")
    @Expose
    private String averageUserRating;
    @SerializedName("channel_object")
    @Expose
    private ChannelObject channelObject;
    @SerializedName("friendly_id")
    @Expose
    private String friendlyId;
    @SerializedName("show_object")
    @Expose
    private ShowObject showObject;
    @SerializedName("theme")
    @Expose
    private String theme;
    @SerializedName("catalog_name")
    @Expose
    private String catalogName;
    @SerializedName("short_description")
    @Expose
    private Object shortDescription;
    @SerializedName("episode_flag")
    @Expose
    private String episodeFlag;
    @SerializedName("subcategory_flag")
    @Expose
    private String subcategoryFlag;
    @SerializedName("rating")
    @Expose
    private Object rating;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("published_at")
    @Expose
    private Object publishedAt;
    @SerializedName("display_title")
    @Expose
    private String displayTitle;
    @SerializedName("item_caption")
    @Expose
    private String itemCaption;
    @SerializedName("item_additional_data")
    @Expose
    private TreeMap<String, ArrayList> itemAdditionalData;
    @SerializedName("smart_url")
    @Expose
    private String smartUrl;
    @SerializedName("play_url")
    @Expose
    private PlayUrl playUrl;
    @SerializedName("access_control")
    @Expose
    private AccessControl accessControl;
    @SerializedName("show_theme_id")
    @Expose
    private String showThemeId;
    @SerializedName("duration_string")
    @Expose
    private String durationString;

    @SerializedName("episode_number")
    @Expose
    private String episodeNumber;

    @SerializedName("start_time")
    @Expose
    private String startTime;

    @SerializedName("stop_time")
    @Expose
    private String stopTime;

    @SerializedName("layout_scheme")
    @Expose
    private String layoutScheme;

    @SerializedName("share_url")
    @Expose
    private String shareUrl;

    @SerializedName("listitem_id")
    @Expose
    private String listItemId;

    @SerializedName("total_percentage")
    @Expose
    private int totalPercentage;
    @SerializedName("preview")
    @Expose
    private Preview preview;
    @SerializedName("show_name")
    @Expose
    private String showName;

    @SerializedName("play_url_type")
    @Expose
    private String playUrlType;
    @SerializedName("media_type")
    @Expose
    private String mediaType;

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }



    private boolean displayInList;

    public String getPlayUrlType() {
        return playUrlType;
    }

    public void setPlayUrlType(String playUrlType) {
        this.playUrlType = playUrlType;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public Preview getPreview() {
        return preview;
    }

    public void setPreview(Preview preview) {
        this.preview = preview;
    }

    public int getTotalPercentage() {
        return totalPercentage;
    }

    public void setTotalPercentage(int totalPercentage) {
        this.totalPercentage = totalPercentage;
    }

    public String getListItemId() {
        return listItemId;
    }

    public void setListItemId(String listItemId) {
        this.listItemId = listItemId;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public String getLayoutScheme() {
        return layoutScheme;
    }

    public void setLayoutScheme(String layoutScheme) {
        this.layoutScheme = layoutScheme;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStopTime() {
        return stopTime;
    }

    public void setStopTime(String stopTime) {
        this.stopTime = stopTime;
    }

    public String getEpisodeNumber() {
        return episodeNumber;
    }

    public void setEpisodeNumber(String episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    public String getDurationString() {
        return durationString;
    }

    public void setDurationString(String durationString) {
        this.durationString = durationString;
    }
    public static Creator<Item> getCREATOR() {
        return CREATOR;
    }

    public Item() {

    }

    protected Item(Parcel in) {
        title = in.readString();
        contentId = in.readString();
        channelName = in.readString();
        genres = in.createStringArrayList();
        displayGenres = in.createStringArrayList();
        language = in.readString();
        catalogId = in.readString();
        id = in.readString();
        description = in.readString();
        keywords = in.readString();
        category = in.readString();
        if (in.readByte() == 0) {
            episodeCount = null;
        } else {
            episodeCount = in.readInt();
        }
        if (in.readByte() == 0) {
            noOfUserRated = null;
        } else {
            noOfUserRated = in.readInt();
        }
        averageUserRating = in.readString();
        friendlyId = in.readString();
        theme = in.readString();
        catalogName = in.readString();
        episodeFlag = in.readString();
        subcategoryFlag = in.readString();
        createdAt = in.readString();
        displayTitle = in.readString();
        itemCaption = in.readString();
        smartUrl = in.readString();
        showThemeId = in.readString();
        shareUrl = in.readString();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    public String getShowThemeId() {
        return showThemeId;
    }

    public void setShowThemeId(String showThemeId) {
        this.showThemeId = showThemeId;
    }

    public AccessControl getAccessControl() {
        return accessControl;
    }

    public void setAccessControl(AccessControl accessControl) {
        this.accessControl = accessControl;
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

    public TreeMap<String, ArrayList> getItemAdditionalData() {
        return itemAdditionalData;
    }

    public void setItemAdditionalData(TreeMap<String, ArrayList> itemAdditionalData) {
        this.itemAdditionalData = itemAdditionalData;
    }


    public String getItemCaption() {
        return itemCaption;
    }

    public void setItemCaption(String itemCaption) {
        this.itemCaption = itemCaption;
    }

    public String getDisplayTitle() {
        return displayTitle;
    }

    public void setDisplayTitle(String displayTitle) {
        this.displayTitle = displayTitle;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public Thumbnails getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(Thumbnails thumbnails) {
        this.thumbnails = thumbnails;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public List<String> getDisplayGenres() {
        return displayGenres;
    }

    public void setDisplayGenres(List<String> displayGenres) {
        this.displayGenres = displayGenres;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(String catalogId) {
        this.catalogId = catalogId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public CatalogObject getCatalogObject() {
        return catalogObject;
    }

    public void setCatalogObject(CatalogObject catalogObject) {
        this.catalogObject = catalogObject;
    }

    public Integer getEpisodeCount() {
        return episodeCount;
    }

    public void setEpisodeCount(Integer episodeCount) {
        this.episodeCount = episodeCount;
    }

    public Integer getNoOfUserRated() {
        return noOfUserRated;
    }

    public void setNoOfUserRated(Integer noOfUserRated) {
        this.noOfUserRated = noOfUserRated;
    }

    public String getAverageUserRating() {
        return averageUserRating;
    }

    public void setAverageUserRating(String averageUserRating) {
        this.averageUserRating = averageUserRating;
    }

    public ChannelObject getChannelObject() {
        return channelObject;
    }

    public void setChannelObject(ChannelObject channelObject) {
        this.channelObject = channelObject;
    }

    public boolean isDisplayInList() {
        return displayInList;
    }

    public void setDisplayInList(boolean displayInList) {
        this.displayInList = displayInList;
    }

    public String getFriendlyId() {
        return friendlyId;
    }

    public void setFriendlyId(String friendlyId) {
        this.friendlyId = friendlyId;
    }

    public ShowObject getShowObject() {
        return showObject;
    }

    public void setShowObject(ShowObject showObject) {
        this.showObject = showObject;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getCatalogName() {
        return catalogName;
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }

    public Object getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(Object shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getEpisodeFlag() {
        return episodeFlag;
    }

    public void setEpisodeFlag(String episodeFlag) {
        this.episodeFlag = episodeFlag;
    }

    public String getSubcategoryFlag() {
        return subcategoryFlag;
    }

    public void setSubcategoryFlag(String subcategoryFlag) {
        this.subcategoryFlag = subcategoryFlag;
    }

    public Object getRating() {
        return rating;
    }

    public void setRating(Object rating) {
        this.rating = rating;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Object getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Object publishedAt) {
        this.publishedAt = publishedAt;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(contentId);
        parcel.writeStringList(genres);
        parcel.writeStringList(displayGenres);
        parcel.writeString(language);
        parcel.writeString(catalogId);
        parcel.writeString(id);
        parcel.writeString(description);
        parcel.writeString(channelName);
        parcel.writeString(keywords);
        parcel.writeString(category);
        if (episodeCount == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(episodeCount);
        }
        if (noOfUserRated == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(noOfUserRated);
        }
        parcel.writeString(averageUserRating);
        parcel.writeString(friendlyId);
        parcel.writeString(theme);
        parcel.writeString(catalogName);
        parcel.writeString(episodeFlag);
        parcel.writeString(subcategoryFlag);
        parcel.writeString(createdAt);
        parcel.writeString(displayTitle);
        parcel.writeString(itemCaption);
        parcel.writeString(smartUrl);
        parcel.writeString(showThemeId);
        parcel.writeString(shareUrl);
    }
}
