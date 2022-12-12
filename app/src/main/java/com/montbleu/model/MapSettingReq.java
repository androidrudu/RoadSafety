package com.montbleu.model;


public class MapSettingReq
{
    private String companyId;

    private String id;

    private String name;

    private String status;

    private String type;

    private String userId;

    private UserPreferenceField userPreferenceField;

    public void setCompanyId(String companyId){
        this.companyId = companyId;
    }
    public String getCompanyId(){
        return this.companyId;
    }
    public void setId(String id){
        this.id = id;
    }
    public String getId(){
        return this.id;
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

    public class UserPreferenceField
    {
        private String appNotification;

        private String backgroundStartup;

        private String beep;

        private String coPliotMode;

        private String continentId;

        private String countryId;

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
        public void setContinentId(String continentId){
            this.continentId = continentId;
        }
        public String getContinentId(){
            return this.continentId;
        }
        public void setCountryId(String countryId){
            this.countryId = countryId;
        }
        public String getCountryId(){
            return this.countryId;
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