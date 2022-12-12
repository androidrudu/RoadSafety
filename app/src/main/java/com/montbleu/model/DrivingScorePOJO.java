package com.montbleu.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DrivingScorePOJO {

    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("drivingScoreList")
    @Expose
    private List<DrivingScore> drivingScoreList = null;
    @SerializedName("drivingScoreCount")
    @Expose
    private String drivingScoreCount;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("username")
    @Expose
    private String username;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public List<DrivingScore> getDrivingScoreList() {
        return drivingScoreList;
    }

    public void setDrivingScoreList(List<DrivingScore> drivingScoreList) {
        this.drivingScoreList = drivingScoreList;
    }

    public String getDrivingScoreCount() {
        return drivingScoreCount;
    }

    public void setDrivingScoreCount(String drivingScoreCount) {
        this.drivingScoreCount = drivingScoreCount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public class DrivingScore {

        @SerializedName("drivingScore")
        @Expose
        private Integer drivingScore;
        @SerializedName("rideName")
        @Expose
        private String rideName;

        public Integer getDrivingScore() {
            return drivingScore;
        }

        public void setDrivingScore(Integer drivingScore) {
            this.drivingScore = drivingScore;
        }

        public String getRideName() {
            return rideName;
        }

        public void setRideName(String rideName) {
            this.rideName = rideName;
        }

    }
}