
package com.otl.tarangplus.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ItemAdditionalData {

    @SerializedName("Starring")
    @Expose
    private List<String> starring = null;
    @SerializedName("Directed By")
    @Expose
    private List<String> directedBy = null;
    @SerializedName("Producer")
    @Expose
    private List<String> producer = null;

    public List<String> getStarring() {
        return starring;
    }

    public void setStarring(List<String> starring) {
        this.starring = starring;
    }

    public List<String> getDirectedBy() {
        return directedBy;
    }

    public void setDirectedBy(List<String> directedBy) {
        this.directedBy = directedBy;
    }

    public List<String> getProducer() {
        return producer;
    }

    public void setProducer(List<String> producer) {
        this.producer = producer;
    }

}