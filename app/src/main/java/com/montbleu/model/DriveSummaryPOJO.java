package com.montbleu.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DriveSummaryPOJO {

    @SerializedName("companyId")
    @Expose
    private String companyId;
    @SerializedName("drivenKiloMeter")
    @Expose
    private String drivenKiloMeter;
    @SerializedName("rideCount")
    @Expose
    private String rideCount;
    @SerializedName("drivenHour")
    @Expose
    private String drivenHour;
    @SerializedName("divisionId")
    @Expose
    private String divisionId;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("lastRideDateTime")
    @Expose
    private String lastRideDateTime;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getDrivenKiloMeter() {
        return drivenKiloMeter;
    }

    public void setDrivenKiloMeter(String drivenKiloMeter) {
        this.drivenKiloMeter = drivenKiloMeter;
    }

    public String getRideCount() {
        return rideCount;
    }

    public void setRideCount(String rideCount) {
        this.rideCount = rideCount;
    }

    public String getDrivenHour() {
        return drivenHour;
    }

    public void setDrivenHour(String drivenHour) {
        this.drivenHour = drivenHour;
    }

    public String getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(String divisionId) {
        this.divisionId = divisionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLastRideDateTime() {
        return lastRideDateTime;
    }

    public void setLastRideDateTime(String lastRideDateTime) {
        this.lastRideDateTime = lastRideDateTime;
    }

}
