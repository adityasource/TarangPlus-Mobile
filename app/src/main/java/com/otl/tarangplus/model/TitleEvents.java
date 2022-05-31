package com.otl.tarangplus.model;

public class TitleEvents {

    private String CategoryName;
    private String CategoryID;
    private String SubCategoryName;
    private String SubCategoryId;
    private String TitleName;
    private String ContentType;
    private String TitleImage;
    private String TitleID;
    private String CurrentTime;
    private String Duration;
    private String PercentComplete;

    public TitleEvents(String categoryName, String categoryID, String subCategoryName, String subCategoryId, String titleName, String contentType, String titleImage, String titleID) {
        CategoryName = categoryName;
        CategoryID = categoryID;
        SubCategoryName = subCategoryName;
        SubCategoryId = subCategoryId;
        TitleName = titleName;
        ContentType = contentType;
        TitleImage = titleImage;
        TitleID = titleID;
    }

    public TitleEvents(String categoryName, String categoryID, String subCategoryName, String subCategoryId, String titleName, String contentType, String titleImage, String titleID, String currentTime, String duration, String percentComplete) {
        CategoryName = categoryName;
        CategoryID = categoryID;
        SubCategoryName = subCategoryName;
        SubCategoryId = subCategoryId;
        TitleName = titleName;
        ContentType = contentType;
        TitleImage = titleImage;
        TitleID = titleID;
        CurrentTime = currentTime;
        Duration = duration;
        PercentComplete = percentComplete;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public String getCategoryID() {
        return CategoryID;
    }

    public void setCategoryID(String categoryID) {
        CategoryID = categoryID;
    }

    public String getSubCategoryName() {
        return SubCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        SubCategoryName = subCategoryName;
    }

    public String getSubCategoryId() {
        return SubCategoryId;
    }

    public void setSubCategoryId(String subCategoryId) {
        SubCategoryId = subCategoryId;
    }

    public String getTitleName() {
        return TitleName;
    }

    public void setTitleName(String titleName) {
        TitleName = titleName;
    }

    public String getContentType() {
        return ContentType;
    }

    public void setContentType(String contentType) {
        ContentType = contentType;
    }

    public String getTitleImage() {
        return TitleImage;
    }

    public void setTitleImage(String titleImage) {
        TitleImage = titleImage;
    }

    public String getTitleID() {
        return TitleID;
    }

    public void setTitleID(String titleID) {
        TitleID = titleID;
    }

    public String getCurrentTime() {
        return CurrentTime;
    }

    public void setCurrentTime(String currentTime) {
        CurrentTime = currentTime;
    }

    public String getDuration() {
        return Duration;
    }

    public void setDuration(String duration) {
        Duration = duration;
    }

    public String getPercentComplete() {
        return PercentComplete;
    }

    public void setPercentComplete(String percentComplete) {
        PercentComplete = percentComplete;
    }
}
