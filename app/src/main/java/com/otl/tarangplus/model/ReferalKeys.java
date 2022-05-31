package com.otl.tarangplus.model;

import java.io.Serializable;
import java.util.Objects;

public class ReferalKeys implements Serializable {

    private String pk_campaign;
    private String pk_kwd;
    private String pk_source;
    private String pk_medium;
    private String pk_content;
    private boolean isSendToOTT;

    public String getPk_campaign() {
        return pk_campaign;
    }

    public void setPk_campaign(String pk_campaign) {
        this.pk_campaign = pk_campaign;
    }

    public String getPk_kwd() {
        return pk_kwd;
    }

    public void setPk_kwd(String pk_kwd) {
        this.pk_kwd = pk_kwd;
    }

    public String getPk_source() {
        return pk_source;
    }

    public void setPk_source(String pk_source) {
        this.pk_source = pk_source;
    }

    public String getPk_medium() {
        return pk_medium;
    }

    public void setPk_medium(String pk_medium) {
        this.pk_medium = pk_medium;
    }

    public String getPk_content() {
        return pk_content;
    }

    public void setPk_content(String pk_content) {
        this.pk_content = pk_content;
    }

    public boolean isSendToOTT() {
        return isSendToOTT;
    }

    public void setSendToOTT(boolean sendToOTT) {
        isSendToOTT = sendToOTT;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReferalKeys that = (ReferalKeys) o;
        return isSendToOTT == that.isSendToOTT &&
                Objects.equals(pk_campaign, that.pk_campaign) &&
                Objects.equals(pk_kwd, that.pk_kwd) &&
                Objects.equals(pk_source, that.pk_source) &&
                Objects.equals(pk_medium, that.pk_medium) &&
                Objects.equals(pk_content, that.pk_content);
    }

    @Override
    public int hashCode() {

        return Objects.hash(pk_campaign, pk_kwd, pk_source, pk_medium, pk_content, isSendToOTT);
    }

    @Override
    public String toString() {
        return "ReferalKeys{" +
                "pk_campaign='" + pk_campaign + '\'' +
                ", pk_kwd='" + pk_kwd + '\'' +
                ", pk_source='" + pk_source + '\'' +
                ", pk_medium='" + pk_medium + '\'' +
                ", pk_content='" + pk_content + '\'' +
                ", isSendToOTT=" + isSendToOTT +
                '}';
    }
}
