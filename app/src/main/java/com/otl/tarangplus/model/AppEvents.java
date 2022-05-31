package com.otl.tarangplus.model;

public class AppEvents {

/*  event_value(e_v)..
    type(typ)..
    visit(vst)..
    application(app)..
    search_query(s_q)..
    event_type(e_t)..
    title(title)..
    user_state(u_st)..
    user_period(u_p)..
    user_plan_type(u_p_r)..
    user_id(u_id)..*/
    public int e_v;
    public String typ;
    public String vst;
    public String app;
    public String s_q;
    public String e_t;
    public String title;
    public String u_st;
    public String u_p;
    public String u_p_r;
    public String u_id;

    public int getE_v() {
        return e_v;
    }

    public void setE_v(int e_v) {
        this.e_v = e_v;
    }

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    public String getVst() {
        return vst;
    }

    public void setVst(String vst) {
        this.vst = vst;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getS_q() {
        return s_q;
    }

    public void setS_q(String s_q) {
        this.s_q = s_q;
    }

    public String getE_t() {
        return e_t;
    }

    public void setE_t(String e_t) {
        this.e_t = e_t;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getU_st() {
        return u_st;
    }

    public void setU_st(String u_st) {
        this.u_st = u_st;
    }

    public String getU_p() {
        return u_p;
    }

    public void setU_p(String u_p) {
        this.u_p = u_p;
    }

    public String getU_p_r() {
        return u_p_r;
    }

    public void setU_p_r(String u_p_r) {
        this.u_p_r = u_p_r;
    }

    public String getU_id() {
        return u_id;
    }

    public void setU_id(String u_id) {
        this.u_id = u_id;
    }

    public AppEvents(int e_v, String typ, String vst, String app, String s_q, String e_t,
                     String title, String u_st, String u_p, String u_p_r, String u_id) {
        this.e_v = e_v; //event_value
        this.typ = typ; // type
        this.vst = vst; //visit
        this.app = app; //android
        this.s_q = s_q; //search query
        this.e_t = e_t; //event_type
        this.title = title; //item title
        this.u_st = u_st; // user info
        this.u_p = u_p;
        this.u_p_r = u_p_r;
        this.u_id = u_id;
    }

    @Override
    public String toString() {
        return "AppEvents{" +
                "e_v=" + e_v +
                ", typ='" + typ + '\'' +
                ", vst='" + vst + '\'' +
                ", app='" + app + '\'' +
                ", s_q='" + s_q + '\'' +
                ", e_t='" + e_t + '\'' +
                ", title='" + title + '\'' +
                ", u_st='" + u_st + '\'' +
                ", u_p='" + u_p + '\'' +
                ", u_p_r='" + u_p_r + '\'' +
                ", u_id='" + u_id + '\'' +
                '}';
    }
}

