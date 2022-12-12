package com.montbleu.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserFeedbackPOJO {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("companyId")
    @Expose
    private String companyId;
    @SerializedName("divisionId")
    @Expose
    private String divisionId;
    @SerializedName("moduleId")
    @Expose
    private String moduleId;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("userFeedbackField")
    @Expose
    private UserFeedbackField userFeedbackField;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("type")
    @Expose
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(String divisionId) {
        this.divisionId = divisionId;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UserFeedbackField getUserFeedbackField() {
        return userFeedbackField;
    }

    public void setUserFeedbackField(UserFeedbackField userFeedbackField) {
        this.userFeedbackField = userFeedbackField;
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

    public static class UserFeedbackField {

        @SerializedName("description")
        @Expose
        private String description;
        private String rateValue;

        public String getRateValue ()
        {
            return rateValue;
        }

        public void setRateValue (String rateValue)
        {
            this.rateValue = rateValue;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
