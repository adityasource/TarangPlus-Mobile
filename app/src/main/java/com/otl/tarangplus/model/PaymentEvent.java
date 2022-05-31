package com.otl.tarangplus.model;

public class PaymentEvent {
    public String u_id;//user id
    public String o_id;//odered id
    public String s_id;// session id
    public String pri;//price
    public String p_typ;// payment type
    public String cur;//currency
    public String status;//Subsciption status
    public String pln_name;//plan name

    public PaymentEvent(String u_id, String o_id, String s_id, String pri, String p_typ, String cur, String status, String pln_name) {
        this.u_id = u_id;
        this.o_id = o_id;
        this.s_id = s_id;
        this.pri = pri;
        this.p_typ = p_typ;
        this.cur = cur;
        this.status = status;
        this.pln_name = pln_name;
    }

    @Override
    public String toString() {
        return "PaymentEvent{" +
                "u_id='" + u_id + '\'' +
                ", o_id='" + o_id + '\'' +
                ", s_id='" + s_id + '\'' +
                ", pri='" + pri + '\'' +
                ", p_typ='" + p_typ + '\'' +
                ", cur='" + cur + '\'' +
                ", status='" + status + '\'' +
                ", pln_name='" + pln_name + '\'' +
                '}';
    }

    public String getU_id() {
        return u_id;
    }

    public void setU_id(String u_id) {
        this.u_id = u_id;
    }

    public String getO_id() {
        return o_id;
    }

    public void setO_id(String o_id) {
        this.o_id = o_id;
    }

    public String getS_id() {
        return s_id;
    }

    public void setS_id(String s_id) {
        this.s_id = s_id;
    }

    public String getPri() {
        return pri;
    }

    public void setPri(String pri) {
        this.pri = pri;
    }

    public String getP_typ() {
        return p_typ;
    }

    public void setP_typ(String p_typ) {
        this.p_typ = p_typ;
    }

    public String getCur() {
        return cur;
    }

    public void setCur(String cur) {
        this.cur = cur;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPln_name() {
        return pln_name;
    }

    public void setPln_name(String pln_name) {
        this.pln_name = pln_name;
    }
}
