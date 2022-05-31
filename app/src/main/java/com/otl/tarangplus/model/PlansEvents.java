package com.otl.tarangplus.model;

public class PlansEvents {
    private String PlanName;
    private String PlanPrice;
    private String Status;

    public PlansEvents(String planName, String planPrice) {
        PlanName = planName;
        PlanPrice = planPrice;
    }

    public PlansEvents(String planName, String planPrice, String status) {
        PlanName = planName;
        PlanPrice = planPrice;
        Status = status;
    }

    public String getPlanName() {
        return PlanName;
    }

    public void setPlanName(String planName) {
        PlanName = planName;
    }

    public String getPlanPrice() {
        return PlanPrice;
    }

    public void setPlanPrice(String planPrice) {
        PlanPrice = planPrice;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
