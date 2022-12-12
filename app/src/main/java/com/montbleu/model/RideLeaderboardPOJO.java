package com.montbleu.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RideLeaderboardPOJO {

    @SerializedName("dayPercentage")
    @Expose
    private String dayPercentage;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("startDateTime")
    @Expose
    private String startDateTime;
    @SerializedName("createdAtToTimeZone")
    @Expose
    private String createdAtToTimeZone;
    @SerializedName("alertDataCount")
    @Expose
    private String alertDataCount;
    @SerializedName("endDateTime")
    @Expose
    private String endDateTime;
    @SerializedName("deviceId")
    @Expose
    private String deviceId;
    @SerializedName("deviceName")
    @Expose
    private String deviceName;
    @SerializedName("deviceDataList")
    @Expose
    private List<DeviceData> deviceDataList = null;

    public String getDayPercentage() {
        return dayPercentage;
    }

    public void setDayPercentage(String dayPercentage) {
        this.dayPercentage = dayPercentage;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    public String getCreatedAtToTimeZone() {
        return createdAtToTimeZone;
    }

    public void setCreatedAtToTimeZone(String createdAtToTimeZone) {
        this.createdAtToTimeZone = createdAtToTimeZone;
    }

    public String getAlertDataCount() {
        return alertDataCount;
    }

    public void setAlertDataCount(String alertDataCount) {
        this.alertDataCount = alertDataCount;
    }

    public String getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        this.endDateTime = endDateTime;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public List<DeviceData> getDeviceDataList() {
        return deviceDataList;
    }

    public void setDeviceDataList(List<DeviceData> deviceDataList) {
        this.deviceDataList = deviceDataList;
    }

    public class DeviceData {

        @SerializedName("subCategory")
        @Expose
        private String subCategory;
        @SerializedName("category")
        @Expose
        private String category;
        @SerializedName("createdAt")
        @Expose
        private String createdAt;
        @SerializedName("createdAtToTimeZone")
        @Expose
        private String createdAtToTimeZone;
        @SerializedName("deviceDataField")
        @Expose
        private DeviceDataField deviceDataField;

        public String getSubCategory() {
            return subCategory;
        }

        public void setSubCategory(String subCategory) {
            this.subCategory = subCategory;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getCreatedAtToTimeZone() {
            return createdAtToTimeZone;
        }

        public void setCreatedAtToTimeZone(String createdAtToTimeZone) {
            this.createdAtToTimeZone = createdAtToTimeZone;
        }

        public DeviceDataField getDeviceDataField() {
            return deviceDataField;
        }

        public void setDeviceDataField(DeviceDataField deviceDataField) {
            this.deviceDataField = deviceDataField;
        }

    }

    public class DeviceDataField {

        @SerializedName("speed")
        @Expose
        private String speed;
        @SerializedName("risk")
        @Expose
        private String risk;
        @SerializedName("alert")
        @Expose
        private String alert;
        @SerializedName("latitude")
        @Expose
        private String latitude;
        @SerializedName("longitude")
        @Expose
        private String longitude;
        @SerializedName("kiloMeter")
        @Expose
        private String kiloMeter;
        @SerializedName("drivingScore")
        @Expose
        private String drivingScore;
        @SerializedName("anticipation")
        @Expose
        private String anticipation;
        @SerializedName("drivingSkill")
        @Expose
        private String drivingSkill;
        @SerializedName("selfConfidence")
        @Expose
        private String selfConfidence;
        @SerializedName("totalKiloMeter")
        @Expose
        private String totalKiloMeter;
        @SerializedName("travelTime")
        @Expose
        private String travelTime;
        @SerializedName("gpsCount")
        @Expose
        private String gpsCount;
        @SerializedName("accelerX")
        @Expose
        private String accelerX;
        @SerializedName("accelerY")
        @Expose
        private String accelerY;
        @SerializedName("accelerZ")
        @Expose
        private String accelerZ;
        @SerializedName("senRotate")
        @Expose
        private String senRotate;
        @SerializedName("zipCode")
        @Expose
        private String zipCode;
        @SerializedName("alertId")
        @Expose
        private String alertId;
        @SerializedName("alertValue")
        @Expose
        private String alertValue;
        @SerializedName("alertTime")
        @Expose
        private String alertTime;
        @SerializedName("rideDate")
        @Expose
        private String rideDate;
        @SerializedName("rideTime")
        @Expose
        private String rideTime;
        @SerializedName("alertKiloMeter")
        @Expose
        private String alertKiloMeter;

        public String getSpeed() {
            return speed;
        }

        public void setSpeed(String speed) {
            this.speed = speed;
        }

        public String getRisk() {
            return risk;
        }

        public void setRisk(String risk) {
            this.risk = risk;
        }

        public String getAlert() {
            return alert;
        }

        public void setAlert(String alert) {
            this.alert = alert;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getKiloMeter() {
            return kiloMeter;
        }

        public void setKiloMeter(String kiloMeter) {
            this.kiloMeter = kiloMeter;
        }

        public String getDrivingScore() {
            return drivingScore;
        }

        public void setDrivingScore(String drivingScore) {
            this.drivingScore = drivingScore;
        }

        public String getAnticipation() {
            return anticipation;
        }

        public void setAnticipation(String anticipation) {
            this.anticipation = anticipation;
        }

        public String getDrivingSkill() {
            return drivingSkill;
        }

        public void setDrivingSkill(String drivingSkill) {
            this.drivingSkill = drivingSkill;
        }

        public String getSelfConfidence() {
            return selfConfidence;
        }

        public void setSelfConfidence(String selfConfidence) {
            this.selfConfidence = selfConfidence;
        }

        public String getTotalKiloMeter() {
            return totalKiloMeter;
        }

        public void setTotalKiloMeter(String totalKiloMeter) {
            this.totalKiloMeter = totalKiloMeter;
        }

        public String getTravelTime() {
            return travelTime;
        }

        public void setTravelTime(String travelTime) {
            this.travelTime = travelTime;
        }

        public String getGpsCount() {
            return gpsCount;
        }

        public void setGpsCount(String gpsCount) {
            this.gpsCount = gpsCount;
        }

        public String getAccelerX() {
            return accelerX;
        }

        public void setAccelerX(String accelerX) {
            this.accelerX = accelerX;
        }

        public String getAccelerY() {
            return accelerY;
        }

        public void setAccelerY(String accelerY) {
            this.accelerY = accelerY;
        }

        public String getAccelerZ() {
            return accelerZ;
        }

        public void setAccelerZ(String accelerZ) {
            this.accelerZ = accelerZ;
        }

        public String getSenRotate() {
            return senRotate;
        }

        public void setSenRotate(String senRotate) {
            this.senRotate = senRotate;
        }

        public String getZipCode() {
            return zipCode;
        }

        public void setZipCode(String zipCode) {
            this.zipCode = zipCode;
        }

        public String getAlertId() {
            return alertId;
        }

        public void setAlertId(String alertId) {
            this.alertId = alertId;
        }

        public String getAlertValue() {
            return alertValue;
        }

        public void setAlertValue(String alertValue) {
            this.alertValue = alertValue;
        }

        public String getAlertTime() {
            return alertTime;
        }

        public void setAlertTime(String alertTime) {
            this.alertTime = alertTime;
        }

        public String getRideDate() {
            return rideDate;
        }

        public void setRideDate(String rideDate) {
            this.rideDate = rideDate;
        }

        public String getRideTime() {
            return rideTime;
        }

        public void setRideTime(String rideTime) {
            this.rideTime = rideTime;
        }

        public String getAlertKiloMeter() {
            return alertKiloMeter;
        }

        public void setAlertKiloMeter(String alertKiloMeter) {
            this.alertKiloMeter = alertKiloMeter;
        }

    }
}
