package com.montbleu.model;

public class Risk_alert_model {

   String speed;
   String zipcode;
   String km_distance;
   String time_hh_mm;
   String alert_val;
   String risk;

    public String getRisk() {
        return risk;
    }

    public void setRisk(String risk) {
        this.risk = risk;
    }

    String category;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPs_sl_val() {
        return ps_sl_val;
    }

    public void setPs_sl_val(String ps_sl_val) {
        this.ps_sl_val = ps_sl_val;
    }

    String ps_sl_val;

    public Risk_alert_model(String speed, String zipcode, String km_distance, String time_hh_mm, String alert_val, String category, String lat, String lng,String ps_sl_val,String risk) {
        this.speed = speed;
        this.risk = risk;
        this.zipcode = zipcode;
        this.km_distance = km_distance;
        this.time_hh_mm = time_hh_mm;
        this.alert_val = alert_val;
        this.category = category;
        this.lat = lat;
        this.lng = lng;
        this.ps_sl_val = ps_sl_val;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    String lat;
   String lng;

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getKm_distance() {
        return km_distance;
    }

    public void setKm_distance(String km_distance) {
        this.km_distance = km_distance;
    }

    public String getTime_hh_mm() {
        return time_hh_mm;
    }

    public void setTime_hh_mm(String time_hh_mm) {
        this.time_hh_mm = time_hh_mm;
    }

    public String getAlert_val() {
        return alert_val;
    }

    public void setAlert_val(String alert_val) {
        this.alert_val = alert_val;
    }

    public String getcategory() {
        return category;
    }

    public void setcategory(String category) {
        this.category = category;
    }
}
