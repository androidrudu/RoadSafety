package com.montbleu.model;

import java.security.PublicKey;

public class ScoreListModel {
    public String TotTime;
    public String totkms;
    public String driveScore;
    public String date_time;
    public String id;
    public String anticipate;
    public String checkDash;
    public String name;
    public long  date;
    public String highwayPercent;
    public String urbanPercent;
    public String ruralPercent;
    public String dayPercentage;

    public String getDayPercentage() {
        return dayPercentage;
    }

    public void setDayPercentage(String dayPercentage) {
        this.dayPercentage = dayPercentage;
    }

    public String getHighwayPercent() {
        return highwayPercent;
    }

    public void setHighwayPercent(String highwayPercent) {
        this.highwayPercent = highwayPercent;
    }

    public String getUrbanPercent() {
        return urbanPercent;
    }

    public void setUrbanPercent(String urbanPercent) {
        this.urbanPercent = urbanPercent;
    }

    public String getRuralPercent() {
        return ruralPercent;
    }

    public void setRuralPercent(String ruralPercent) {
        this.ruralPercent = ruralPercent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getCheckDash() {
        return checkDash;
    }

    public void setCheckDash(String checkDash) {
        this.checkDash = checkDash;
    }

    public String getAnticipate() {
        return anticipate;
    }

    public void setAnticipate(String anticipate) {
        this.anticipate = anticipate;
    }

    public String getSelfConfident() {
        return selfConfident;
    }

    public void setSelfConfident(String selfConfident) {
        this.selfConfident = selfConfident;
    }

    public String getDrivingSkill() {
        return drivingSkill;
    }

    public void setDrivingSkill(String drivingSkill) {
        this.drivingSkill = drivingSkill;
    }

    public String selfConfident;
    public String drivingSkill;

    public String getDeviceMode() {
        return deviceMode;
    }

    public void setDeviceMode(String deviceMode) {
        this.deviceMode = deviceMode;
    }

    public String deviceMode;

    public String getTotTime() {
        return TotTime;
    }

    public void setTotTime(String totTime) {
        TotTime = totTime;
    }

    public String getTotkms() {
        return totkms;
    }

    public void setTotkms(String totkms) {
        this.totkms = totkms;
    }

    public String getDriveScore() {
        return driveScore;
    }

    public void setDriveScore(String driveScore) {
        this.driveScore = driveScore;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ScoreListModel(String TotTime, String totkms, String driveScore, String date_time, String id,String anticipate,String selfConfident,String drivingSkill,String name,String checkDash,  String highwayPercent,String urbanPercent, String ruralPercent, String dayPercentage,String deviceMode) {
        this.TotTime = TotTime;
        this.driveScore = driveScore;
        this.id = id;
        this.totkms = totkms;
        this.date_time = date_time;
        this.anticipate = anticipate;
        this.selfConfident = selfConfident;
        this.drivingSkill = drivingSkill;
        this.checkDash=checkDash;
        this.name=name;
        this.highwayPercent = highwayPercent;
        this.ruralPercent = ruralPercent;
        this.urbanPercent = urbanPercent;
        this.dayPercentage = dayPercentage;
        this.deviceMode = deviceMode;
    }
    public ScoreListModel(Long date){
       this.date=date;
    }

}
