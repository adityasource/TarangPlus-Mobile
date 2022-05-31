
package com.otl.tarangplus.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CatalogListItem implements Parcelable {


    @SerializedName("channel_name")
    @Expose
    private String channelName;

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getPlayUrlType() {
        return playUrlType;
    }

    public void setPlayUrlType(String playUrlType) {
        this.playUrlType = playUrlType;
    }

    @SerializedName("play_url_type")
    @Expose
    private String playUrlType;


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
    @SerializedName("layout_type")
    @Expose
    private String layoutType;
    @SerializedName("list_id")
    @Expose
    private String listId;
    @SerializedName("list_order")
    @Expose
    private Integer listOrder;
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
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("order_no")
    @Expose
    private Integer orderNo;
    @SerializedName("media_record_id")
    @Expose
    private String mediaRecordId;
    @SerializedName("catalog_object")
    @Expose
    private CatalogObject catalogObject;
    @SerializedName("tag")
    @Expose
    private String tag;
    @SerializedName("list_item_object")
    @Expose
    private ListItemObject listItemObject;
    @SerializedName("catalog_id")
    @Expose
    private String catalogID;
    @SerializedName("content_id")
    @Expose
    private String contentID;
    @SerializedName("layout_scheme")
    @Expose
    private String layoutScheme;
    @SerializedName("access_control")
    @Expose
    private AccessControl accessControl;
    @SerializedName("show_theme_id")
    @Expose
    private String showThemeId;
    @SerializedName("item_caption")
    @Expose
    private String itemCaption;
    @SerializedName("programs")
    @Expose
    private List<Program> programs;
    @SerializedName("catalog_name")
    @Expose
    private String catalogName;

    protected CatalogListItem(Parcel in) {
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
        layoutType = in.readString();
        listId = in.readString();
        playUrlType = in.readString();
        channelName = in.readString();
        if (in.readByte() == 0) {
            listOrder = null;
        } else {
            listOrder = in.readInt();
        }
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
        message = in.readString();
        if (in.readByte() == 0) {
            orderNo = null;
        } else {
            orderNo = in.readInt();
        }
        mediaRecordId = in.readString();
        tag = in.readString();
        catalogID = in.readString();
        contentID = in.readString();
        layoutScheme = in.readString();
        showThemeId = in.readString();
        itemCaption = in.readString();
        catalogName = in.readString();
    }

    public static final Creator<CatalogListItem> CREATOR = new Creator<CatalogListItem>() {
        @Override
        public CatalogListItem createFromParcel(Parcel in) {
            return new CatalogListItem(in);
        }

        @Override
        public CatalogListItem[] newArray(int size) {
            return new CatalogListItem[size];
        }
    };

    public List<Program> getPrograms() {
        return programs;
    }

    public void setPrograms(List<Program> programs) {
        this.programs = programs;
    }

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



    public String getLayoutScheme() {
        return layoutScheme;
    }

    public void setLayoutScheme(String layoutScheme) {
        this.layoutScheme = layoutScheme;
    }

    public String getContentID() {
        return contentID;
    }

    public void setContentID(String contentID) {
        this.contentID = contentID;
    }

    public String getCatalogID() {
        return catalogID;
    }

    public void setCatalogID(String catalogID) {
        this.catalogID = catalogID;
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

    public Integer getListOrder() {
        return listOrder;
    }

    public void setListOrder(Integer listOrder) {
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public String getMediaRecordId() {
        return mediaRecordId;
    }

    public void setMediaRecordId(String mediaRecordId) {
        this.mediaRecordId = mediaRecordId;
    }

    public CatalogObject getCatalogObject() {
        return catalogObject;
    }

    public void setCatalogObject(CatalogObject catalogObject) {
        this.catalogObject = catalogObject;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public ListItemObject getListItemObject() {
        return listItemObject;
    }

    public void setListItemObject(ListItemObject listItemObject) {
        this.listItemObject = listItemObject;
    }

    public String getItemCaption() {
        return itemCaption;
    }

    public void setItemCaption(String itemCaption) {
        this.itemCaption = itemCaption;
    }

    public String getCatalogName() {
        return catalogName;
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }

    @Override
    public String toString() {
        return "CatalogListItem{" +
                "accessTag='" + accessTag + '\'' +
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
                ", totalItemsCount=" + totalItemsCount +
                ", message='" + message + '\'' +
                ", orderNo=" + orderNo +
                ", mediaRecordId='" + mediaRecordId + '\'' +
                ", catalogObject=" + catalogObject +
                ", tag='" + tag + '\'' +
                ", listItemObject=" + listItemObject +
                ", itemCaption=" + itemCaption +
                ", channelName=" + channelName +
                ", playUrlType=" + playUrlType +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(accessTag);
        parcel.writeTypedList(catalogListItems);
        parcel.writeByte((byte) (category == null ? 0 : category ? 1 : 2));
        parcel.writeString(cmsListType);
        parcel.writeString(displayTitle);
        parcel.writeString(durationString);
        parcel.writeString(filterType);
        parcel.writeString(friendlyId);
        parcel.writeString(homeLink);
        parcel.writeString(layoutStructure);
        parcel.writeString(layoutType);
        parcel.writeString(listId);
        parcel.writeString(channelName);
        parcel.writeString(playUrlType);
        if (listOrder == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(listOrder);
        }
        parcel.writeString(listType);
        parcel.writeString(logo);
        parcel.writeString(menuLink);
        parcel.writeString(publishAtDate);
        parcel.writeString(smartUrl);
        parcel.writeString(status);
        parcel.writeStringList(tags);
        parcel.writeString(title);
        parcel.writeString(version);
        parcel.writeString(theme);
        parcel.writeString(adUnitId);
        parcel.writeString(adUrl);
        if (totalItemsCount == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(totalItemsCount);
        }
        parcel.writeString(message);
        if (orderNo == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(orderNo);
        }
        parcel.writeString(mediaRecordId);
        parcel.writeString(tag);
        parcel.writeString(catalogID);
        parcel.writeString(contentID);
        parcel.writeString(layoutScheme);
        parcel.writeString(showThemeId);
        parcel.writeString(itemCaption);
        parcel.writeString(catalogName);
    }
}
