package com.montbleu.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;




public class UserMapSettingReq {

    @SerializedName("companyId")
    @Expose
    private String companyId;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("userPreferenceField")
    @Expose
    private UserPreferenceField userPreferenceField;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("type")
    @Expose
    private String type;
    private String divisionId;
    private String moduleId;

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(String divisionId) {
        this.divisionId = divisionId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserPreferenceField getUserPreferenceField() {
        return userPreferenceField;
    }

    public void setUserPreferenceField(UserPreferenceField userPreferenceField) {
        this.userPreferenceField = userPreferenceField;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static class UserPreferenceField {

        @SerializedName("continentId")
        @Expose
        private String continentId;
        @SerializedName("countryId")
        @Expose
        private String countryId;

        public String getContinentId() {
            return continentId;
        }

        public void setContinentId(String continentId) {
            this.continentId = continentId;
        }

        public String getCountryId() {
            return countryId;
        }

        public void setCountryId(String countryId) {
            this.countryId = countryId;
        }

    }


}

