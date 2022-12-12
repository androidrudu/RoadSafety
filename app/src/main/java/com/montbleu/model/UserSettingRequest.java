package com.montbleu.model;

public class UserSettingRequest
{
    private String companyId;

    private String name;

    private String status;

    private String type;

    private String userId;

    private String divisionId;
    private String moduleId;

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


    private String id;

    private UserPreferenceField userPreferenceField;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCompanyId(String companyId){
        this.companyId = companyId;
    }
    public String getCompanyId(){
        return this.companyId;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void setStatus(String status){
        this.status = status;
    }
    public String getStatus(){
        return this.status;
    }
    public void setType(String type){
        this.type = type;
    }
    public String getType(){
        return this.type;
    }
    public void setUserId(String userId){
        this.userId = userId;
    }
    public String getUserId(){
        return this.userId;
    }
    public void setUserPreferenceField(UserPreferenceField userPreferenceField){
        this.userPreferenceField = userPreferenceField;
    }
    public UserPreferenceField getUserPreferenceField(){
        return this.userPreferenceField;
    }

    public static class UserPreferenceField
    {
        private String appNotification;

        private String backgroundStartup;

        private String beep;

        private String coPliotMode;

        private String speedLimitBeep;

        private String voiceAlert;

        public void setAppNotification(String appNotification){
            this.appNotification = appNotification;
        }
        public String getAppNotification(){
            return this.appNotification;
        }
        public void setBackgroundStartup(String backgroundStartup){
            this.backgroundStartup = backgroundStartup;
        }
        public String getBackgroundStartup(){
            return this.backgroundStartup;
        }
        public void setBeep(String beep){
            this.beep = beep;
        }
        public String getBeep(){
            return this.beep;
        }
        public void setCoPliotMode(String coPliotMode){
            this.coPliotMode = coPliotMode;
        }
        public String getCoPliotMode(){
            return this.coPliotMode;
        }
        public void setSpeedLimitBeep(String speedLimitBeep){
            this.speedLimitBeep = speedLimitBeep;
        }
        public String getSpeedLimitBeep(){
            return this.speedLimitBeep;
        }
        public void setVoiceAlert(String voiceAlert){
            this.voiceAlert = voiceAlert;
        }
        public String getVoiceAlert(){
            return this.voiceAlert;
        }
    }

}

