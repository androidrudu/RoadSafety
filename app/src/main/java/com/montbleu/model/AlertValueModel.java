package com.montbleu.model;

public class AlertValueModel {
    public String value;
    public String time;
    public String dist;
    public String zipcode;
    public String speed;


    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getSpeedLimit() {
        return speedLimit;
    }

    public void setSpeedLimit(String speedLimit) {
        this.speedLimit = speedLimit;
    }

    public String speedLimit;


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDist() {
        return dist;
    }

    public void setDist(String dist) {
        this.dist = dist;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public AlertValueModel(String value, String time, String dist, String zipcode,String speedLimit,String speed) {
        this.value = value;
        this.time = time;
        this.dist = dist;
        this.zipcode = zipcode;
        this.speedLimit=speedLimit;
        this.speed=speed;
    }
}
