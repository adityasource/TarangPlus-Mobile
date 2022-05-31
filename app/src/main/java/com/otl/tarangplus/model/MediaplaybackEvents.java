package com.otl.tarangplus.model;

/*event_value(e_v)
application(app)
event_time(e_tm)
event_type(e_t)
media_language(m_l)
media_content_type(m_c_t)
media_genre(m_g)
video_id(v_id)
title_category(t_cat)
total_duration(t_dur)
title(title)
user_state(u_st)
user_period(u_p)
user_plan_type(u_p_r)
user_id(u_id)
show_title(s_t)
content_owner(c_o)
list_name(l_name)*/

public class MediaplaybackEvents {



    public int e_v;
    public String app;
    public double  e_tm;
    public String e_t;
    public String m_l;
    public String m_c_t;
    public String m_g;
    public String v_id;
    public String t_cat;
    public double t_dur;
    public String title;
    public String u_st;
    public String u_p;
    public String u_p_r;
    public String u_id;
    public String s_t;
    public String c_o;
    public String l_name;

    public MediaplaybackEvents(int e_v, String app, double e_tm, String e_t, String m_l, String m_c_t, String m_g, String v_id, String t_cat, double t_dur, String title, String u_st, String u_p, String u_p_r, String u_id, String s_t, String c_o, String l_name) {
        this.e_v = e_v;
        this.app = app;
        this.e_tm = e_tm;
        this.e_t = e_t;
        this.m_l = m_l;
        this.m_c_t = m_c_t;
        this.m_g = m_g;
        this.v_id = v_id;
        this.t_cat = t_cat;
        this.t_dur = t_dur;
        this.title = title;
        this.u_st = u_st;
        this.u_p = u_p;
        this.u_p_r = u_p_r;
        this.u_id = u_id;
        this.s_t = s_t;
        this.c_o = c_o;
        this.l_name = l_name;
    }

    public int getE_v() {
        return e_v;
    }

    public void setE_v(int e_v) {
        this.e_v = e_v;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public double getE_tm() {
        return e_tm;
    }

    public void setE_tm(double e_tm) {
        this.e_tm = e_tm;
    }

    public String getE_t() {
        return e_t;
    }

    public void setE_t(String e_t) {
        this.e_t = e_t;
    }

    public String getM_l() {
        return m_l;
    }

    public void setM_l(String m_l) {
        this.m_l = m_l;
    }

    public String getM_c_t() {
        return m_c_t;
    }

    public void setM_c_t(String m_c_t) {
        this.m_c_t = m_c_t;
    }

    public String getM_g() {
        return m_g;
    }

    public void setM_g(String m_g) {
        this.m_g = m_g;
    }

    public String getV_id() {
        return v_id;
    }

    public void setV_id(String v_id) {
        this.v_id = v_id;
    }

    public String getT_cat() {
        return t_cat;
    }

    public void setT_cat(String t_cat) {
        this.t_cat = t_cat;
    }

    public double getT_dur() {
        return t_dur;
    }

    public void setT_dur(double t_dur) {
        this.t_dur = t_dur;
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

    public String getS_t() {
        return s_t;
    }

    public void setS_t(String s_t) {
        this.s_t = s_t;
    }

    public String getC_o() {
        return c_o;
    }

    public void setC_o(String c_o) {
        this.c_o = c_o;
    }

    public String getL_name() {
        return l_name;
    }

    public void setL_name(String l_name) {
        this.l_name = l_name;
    }


}
