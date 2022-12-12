package com.montbleu.model;

public class AlertData {
    String alertName;
    String alertNoOfCount;
    Integer alertTypeImg;
    String alertId;

    public AlertData(String alertName, String alertNoOfCount, Integer alertTypeImg, String alertId) {
        this.alertName = alertName;
        this.alertNoOfCount = alertNoOfCount;
        this.alertTypeImg = alertTypeImg;
        this.alertId = alertId;
    }

    public String getAlertId() {
        return alertId;
    }

    public void setAlertId(String alertId) {
        this.alertId = alertId;
    }

    public String getAlertName() {
        return alertName;
    }

    public void setAlertName(String alertName) {
        this.alertName = alertName;
    }

    public String getAlertNoOfCount() {
        return alertNoOfCount;
    }

    public void setAlertNoOfCount(String alertNoOfCount) {
        this.alertNoOfCount = alertNoOfCount;
    }

    public Integer getAlertTypeImg() {
        return alertTypeImg;
    }

    public void setAlertTypeImg(Integer alertTypeImg) {
        this.alertTypeImg = alertTypeImg;
    }

}
