package com.montbleu.model;

public class UserSettingGETResponse {

    private String id;
    private String userId;
    private String name;
    private UserPreferenceField userPreferenceField;
    private String status;
    private String type;
    private String divisionId;
    private String moduleId;
    private String companyId;

    public String getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(String divisionId) {
        this.divisionId = divisionId;
    }

    public String getModuleID() {
        return moduleId;
    }

    public void setModuleID(String moduleID) {
        this.moduleId = moduleID;
    }

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



    public class UserPreferenceField {

        private String continentId;
        private String countryId;
        private String appNotification;
        private String backgroundStartup;
        private String speedLimitBeep;
        private String beep;
        private String voiceAlert;
        private String coPliotMode;

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

        public String getAppNotification() {
            return appNotification;
        }

        public void setAppNotification(String appNotification) {
            this.appNotification = appNotification;
        }

        public String getBackgroundStartup() {
            return backgroundStartup;
        }

        public void setBackgroundStartup(String backgroundStartup) {
            this.backgroundStartup = backgroundStartup;
        }

        public String getSpeedLimitBeep() {
            return speedLimitBeep;
        }

        public void setSpeedLimitBeep(String speedLimitBeep) {
            this.speedLimitBeep = speedLimitBeep;
        }

        public String getBeep() {
            return beep;
        }

        public void setBeep(String beep) {
            this.beep = beep;
        }

        public String getVoiceAlert() {
            return voiceAlert;
        }

        public void setVoiceAlert(String voiceAlert) {
            this.voiceAlert = voiceAlert;
        }

        public String getCoPliotMode() {
            return coPliotMode;
        }

        public void setCoPliotMode(String coPliotMode) {
            this.coPliotMode = coPliotMode;
        }



    }



}



