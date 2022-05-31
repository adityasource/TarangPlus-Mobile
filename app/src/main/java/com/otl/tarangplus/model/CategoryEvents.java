package com.otl.tarangplus.model;

public class CategoryEvents {
    private String CategoryName;
    private String CategoryID;
    private String Sub_Category_Name;
    private String Sub_Category_ID;

    public CategoryEvents(String categoryName, String categoryID) {
        CategoryName = categoryName;
        CategoryID = categoryID;
    }

    public CategoryEvents(String categoryName, String categoryID, String sub_Category_Name, String sub_Category_ID) {
        CategoryName = categoryName;
        CategoryID = categoryID;
        Sub_Category_Name = sub_Category_Name;
        Sub_Category_ID = sub_Category_ID;
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

    public String getSub_Category_Name() {
        return Sub_Category_Name;
    }

    public void setSub_Category_Name(String sub_Category_Name) {
        Sub_Category_Name = sub_Category_Name;
    }

    public String getSub_Category_ID() {
        return Sub_Category_ID;
    }

    public void setSub_Category_ID(String sub_Category_ID) {
        Sub_Category_ID = sub_Category_ID;
    }
}
