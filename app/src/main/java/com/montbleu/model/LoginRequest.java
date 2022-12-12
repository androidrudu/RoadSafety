package com.montbleu.model;

public class LoginRequest {
    private String password;

    private String id;

    private String companyId;

    private UserSessionField userSessionField;

    private String username;

    public String getPassword ()
    {
        return password;
    }

    public void setPassword (String password)
    {
        this.password = password;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    private String category;
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public UserSessionField getUserSessionField ()
    {
        return userSessionField;
    }

    public void setUserSessionField (UserSessionField userSessionField)
    {
        this.userSessionField = userSessionField;
    }

    public String getUsername ()
    {
        return username;
    }

    public void setUsername (String username)
    {
        this.username = username;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [password = "+password+", id = "+id+", userSessionField = "+userSessionField+", username = "+username+"]";
    }

    public static class UserSessionField
    {
        private String deviceType;

        private String deviceMACAddress;

        private String deviceToken;

        private String deviceUniqueId;

        private String deviceVersionNumber;

        private String deviceModelName;

        private String deviceStatus;

        public String getDeviceOrderId() {
            return deviceOrderId;
        }

        public void setDeviceOrderId(String deviceOrderId) {
            this.deviceOrderId = deviceOrderId;
        }

        private String deviceOrderId;

        public String getDeviceUniqueId() {
            return deviceUniqueId;
        }

        public void setDeviceUniqueId(String deviceUniqueId) {
            this.deviceUniqueId = deviceUniqueId;
        }

        public String getDeviceVersionNumber() {
            return deviceVersionNumber;
        }

        public void setDeviceVersionNumber(String deviceVersionNumber) {
            this.deviceVersionNumber = deviceVersionNumber;
        }

        public String getDeviceModelName() {
            return deviceModelName;
        }

        public void setDeviceModelName(String deviceModelName) {
            this.deviceModelName = deviceModelName;
        }

        public String getDeviceStatus() {
            return deviceStatus;
        }

        public void setDeviceStatus(String deviceStatus) {
            this.deviceStatus = deviceStatus;
        }

        public String getDeviceType ()
        {
            return deviceType;
        }

        public void setDeviceType (String deviceType)
        {
            this.deviceType = deviceType;
        }

        public String getDeviceMACAddress ()
        {
            return deviceMACAddress;
        }

        public void setDeviceMACAddress (String deviceMACAddress)
        {
            this.deviceMACAddress = deviceMACAddress;
        }

        public String getDeviceToken ()
        {
            return deviceToken;
        }

        public void setDeviceToken (String deviceToken)
        {
            this.deviceToken = deviceToken;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [deviceType = "+deviceType+", deviceMACAddress = "+deviceMACAddress+", deviceToken = "+deviceToken+"]";
        }
    }




}


