
package com.otl.tarangplus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CatalogObject {

    @SerializedName("friendly_id")
    @Expose
    private String friendlyId;
    @SerializedName("layout_type")
    @Expose
    private String layoutType;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("layout_scheme")
    @Expose
    private String layoutScheme;
    @SerializedName("plan_category_type")
    @Expose
    private String planCategoryType;

    public String getPlanCategoryType() {
        return planCategoryType;
    }

    public void setPlanCategoryType(String planCategoryType) {
        this.planCategoryType = planCategoryType;
    }

    public String getLayoutScheme() {
        return layoutScheme;
    }

    public void setLayoutScheme(String layoutScheme) {
        this.layoutScheme = layoutScheme;
    }

    public String getFriendlyId() {
        return friendlyId;
    }

    public void setFriendlyId(String friendlyId) {
        this.friendlyId = friendlyId;
    }

    public String getLayoutType() {
        return layoutType;
    }

    public void setLayoutType(String layoutType) {
        this.layoutType = layoutType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
