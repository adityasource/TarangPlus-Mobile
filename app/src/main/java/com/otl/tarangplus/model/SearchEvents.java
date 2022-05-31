package com.otl.tarangplus.model;

public class SearchEvents {
    private String SearchQuery;

    public SearchEvents(String searchQuery) {
        SearchQuery = searchQuery;
    }

    public String getSearchQuery() {
        return SearchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        SearchQuery = searchQuery;
    }
}
