package com.otl.tarangplus.model;

public class PageEvents {
    private String pageName;

    public PageEvents(String page_Name) {
        pageName = page_Name;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }
}
