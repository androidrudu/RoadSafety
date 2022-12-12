package com.montbleu.model;

public class rideListItem {

    private String rides;
    private String riskAlert;
    private String score;

    public rideListItem(String rides, String riskAlert, String score) {
        this.rides = rides;
        this.riskAlert = riskAlert;
        this.score = score;
    }

    public String getRides() {
        return rides;
    }

    public void setRides(String rides) {
        this.rides = rides;
    }

    public String getRiskAlert() {
        return riskAlert;
    }

    public void setRiskAlert(String riskAlert) {
        this.riskAlert = riskAlert;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
