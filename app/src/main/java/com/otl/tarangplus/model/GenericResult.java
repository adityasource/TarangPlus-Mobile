package com.otl.tarangplus.model;

public class GenericResult {

    private ShowDetailsResponse showDetailsResponse;
    private ListResonse listResonse;
    private ListResonse recommendedList;
    private SingleEpisode singleEpisode;
    private boolean isSeasonAvailable;

    public ListResonse getRecommendedList() {
        return recommendedList;
    }

    public void setRecommendedList(ListResonse recommendedList) {
        this.recommendedList = recommendedList;
    }

    public ShowDetailsResponse getShowDetailsResponse() {
        return showDetailsResponse;
    }

    public void setShowDetailsResponse(ShowDetailsResponse showDetailsResponse) {
        this.showDetailsResponse = showDetailsResponse;
    }

    public ListResonse getListResonse() {
        return listResonse;
    }

    public void setListResonse(ListResonse listResonse) {
        this.listResonse = listResonse;
    }

    public boolean isSeasonAvailable() {
        return isSeasonAvailable;
    }

    public void setSeasonAvailable(boolean seasonAvailable) {
        isSeasonAvailable = seasonAvailable;
    }

    public SingleEpisode getSingleEpisode() {
        return singleEpisode;
    }

    public void setSingleEpisode(SingleEpisode singleEpisode) {
        this.singleEpisode = singleEpisode;
    }
}
