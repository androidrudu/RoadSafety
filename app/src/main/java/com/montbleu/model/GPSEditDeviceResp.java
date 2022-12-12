package com.montbleu.model;


public class GPSEditDeviceResp
{
    private DeviceDataField deviceDataField;

    private String portionId;

    private String sectionId;

    private String type;

    private String userId;

    private String companyId;

    private String uniqueCode;

    private String deviceName;

    private String dayPercentage;

    private String divisionId;

    private String id;

    private String category;

    private String moduleId;

    private String propertyId;

    private String status;

    private DeviceDataField1 deviceDataField1;

    public String getDayPercentage() {
        return dayPercentage;
    }

    public void setDayPercentage(String dayPercentage) {
        this.dayPercentage = dayPercentage;
    }

    public DeviceDataField1 getDeviceDataField1 ()
    {
        return deviceDataField1;
    }

    public void setDeviceDataField1 (DeviceDataField1 deviceDataField1)
    {
        this.deviceDataField1 = deviceDataField1;
    }

    public DeviceDataField getDeviceField ()
    {
        return deviceDataField;
    }

    public void setDeviceField (DeviceDataField deviceField)
    {
        this.deviceDataField = deviceField;
    }

    public String getPortionId ()
    {
        return portionId;
    }

    public void setPortionId (String portionId)
    {
        this.portionId = portionId;
    }

    public String getSectionId ()
    {
        return sectionId;
    }

    public void setSectionId (String sectionId)
    {
        this.sectionId = sectionId;
    }

    public String getType ()
    {
        return type;
    }

    public void setType (String type)
    {
        this.type = type;
    }

    public String getUserId ()
    {
        return userId;
    }

    public void setUserId (String userId)
    {
        this.userId = userId;
    }

    public String getCompanyId ()
    {
        return companyId;
    }

    public void setCompanyId (String companyId)
    {
        this.companyId = companyId;
    }

    public String getUniqueCode ()
    {
        return uniqueCode;
    }

    public void setUniqueCode (String uniqueCode)
    {
        this.uniqueCode = uniqueCode;
    }

    public String getDeviceName ()
    {
        return deviceName;
    }

    public void setDeviceName (String name)
    {
        this.deviceName = name;
    }

    public String getDivisionId ()
    {
        return divisionId;
    }

    public void setDivisionId (String divisionId)
    {
        this.divisionId = divisionId;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getCategory ()
    {
        return category;
    }

    public void setCategory (String category)
    {
        this.category = category;
    }

    public String getModuleId ()
    {
        return moduleId;
    }

    public void setModuleId (String moduleId)
    {
        this.moduleId = moduleId;
    }

    public String getPropertyId ()
    {
        return propertyId;
    }

    public void setPropertyId (String propertyId)
    {
        this.propertyId = propertyId;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [deviceField = "+deviceDataField+", portionId = "+portionId+", sectionId = "+sectionId+", type = "+type+", userId = "+userId+", companyId = "+companyId+", uniqueCode = "+uniqueCode+", name = "+deviceName+", divisionId = "+divisionId+", id = "+id+", category = "+category+", moduleId = "+moduleId+", propertyId = "+propertyId+", status = "+status+"]";
    }


    public class DeviceDataField
    {
        private String zipCode;

        private String travelTime;

        private String kiloMeter;

        private String latitude;

        private String accelerY;

        private String alertKiloMeter;

        private String accelerZ;

        private String rideTime;

        private String speed;

        private String rideDate;

        private String accelerX;

        private String gpsCount;

        private String alert;

        private String selfConfidence;

        private String totalKiloMeter;

        private String anticipation;

        private String senRotate;

        private String drivingScore;

        private String risk;

        private String alertId;

        private String drivingSkill;

        private String alertTime;

        private String alertValue;

        private String longitude;

        private String urbanPercent;

        private String ruralPercent;

        private String highwayPercent;

        private String deviceMode;



        private String deviceName;

        public String getDeviceName() {
            return deviceName;
        }

        public void setDeviceName(String deviceName) {
            this.deviceName = deviceName;
        }

        public String getDeviceMode() {
            return deviceMode;
        }

        public void setDeviceMode(String deviceMode) {
            this.deviceMode = deviceMode;
        }

        public String getUrbanPercent() {
            return urbanPercent;
        }

        public void setUrbanPercent(String urbanPercent) {
            this.urbanPercent = urbanPercent;
        }

        public String getRuralPercent() {
            return ruralPercent;
        }

        public void setRuralPercent(String ruralPercent) {
            this.ruralPercent = ruralPercent;
        }

        public String getHighwayPercent() {
            return highwayPercent;
        }

        public void setHighwayPercent(String highwayPercent) {
            this.highwayPercent = highwayPercent;
        }

        public String getZipCode ()
        {
            return zipCode;
        }

        public void setZipCode (String zipCode)
        {
            this.zipCode = zipCode;
        }

        public String getTravelTime ()
        {
            return travelTime;
        }

        public void setTravelTime (String travelTime)
        {
            this.travelTime = travelTime;
        }

        public String getKiloMeter ()
        {
            return kiloMeter;
        }

        public void setKiloMeter (String kiloMeter)
        {
            this.kiloMeter = kiloMeter;
        }

        public String getLatitude ()
        {
            return latitude;
        }

        public void setLatitude (String latitude)
        {
            this.latitude = latitude;
        }

        public String getAccelerY ()
        {
            return accelerY;
        }

        public void setAccelerY (String accelerY)
        {
            this.accelerY = accelerY;
        }

        public String getAlertKiloMeter ()
        {
            return alertKiloMeter;
        }

        public void setAlertKiloMeter (String alertKiloMeter)
        {
            this.alertKiloMeter = alertKiloMeter;
        }

        public String getAccelerZ ()
        {
            return accelerZ;
        }

        public void setAccelerZ (String accelerZ)
        {
            this.accelerZ = accelerZ;
        }

        public String getRideTime ()
        {
            return rideTime;
        }

        public void setRideTime (String rideTime)
        {
            this.rideTime = rideTime;
        }

        public String getSpeed ()
        {
            return speed;
        }

        public void setSpeed (String speed)
        {
            this.speed = speed;
        }

        public String getRideDate ()
        {
            return rideDate;
        }

        public void setRideDate (String rideDate)
        {
            this.rideDate = rideDate;
        }

        public String getAccelerX ()
        {
            return accelerX;
        }

        public void setAccelerX (String accelerX)
        {
            this.accelerX = accelerX;
        }

        public String getGpsCount ()
        {
            return gpsCount;
        }

        public void setGpsCount (String gpsCount)
        {
            this.gpsCount = gpsCount;
        }

        public String getAlert ()
        {
            return alert;
        }

        public void setAlert (String alert)
        {
            this.alert = alert;
        }

        public String getSelfConfidence ()
        {
            return selfConfidence;
        }

        public void setSelfConfidence (String selfConfidence)
        {
            this.selfConfidence = selfConfidence;
        }

        public String getTotalKiloMeter ()
        {
            return totalKiloMeter;
        }

        public void setTotalKiloMeter (String totalKiloMeter)
        {
            this.totalKiloMeter = totalKiloMeter;
        }

        public String getAnticipation ()
        {
            return anticipation;
        }

        public void setAnticipation (String anticipation)
        {
            this.anticipation = anticipation;
        }

        public String getSenRotate ()
        {
            return senRotate;
        }

        public void setSenRotate (String senRotate)
        {
            this.senRotate = senRotate;
        }

        public String getDrivingScore ()
        {
            return drivingScore;
        }

        public void setDrivingScore (String drivingScore)
        {
            this.drivingScore = drivingScore;
        }

        public String getRisk ()
        {
            return risk;
        }

        public void setRisk (String risk)
        {
            this.risk = risk;
        }

        public String getAlertId ()
        {
            return alertId;
        }

        public void setAlertId (String alertId)
        {
            this.alertId = alertId;
        }

        public String getDrivingSkill ()
        {
            return drivingSkill;
        }

        public void setDrivingSkill (String drivingSkill)
        {
            this.drivingSkill = drivingSkill;
        }

        public String getAlertTime ()
        {
            return alertTime;
        }

        public void setAlertTime (String alertTime)
        {
            this.alertTime = alertTime;
        }

        public String getAlertValue ()
        {
            return alertValue;
        }

        public void setAlertValue (String alertValue)
        {
            this.alertValue = alertValue;
        }

        public String getLongitude ()
        {
            return longitude;
        }

        public void setLongitude (String longitude)
        {
            this.longitude = longitude;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [zipCode = "+zipCode+", travelTime = "+travelTime+", kiloMeter = "+kiloMeter+", latitude = "+latitude+", accelerY = "+accelerY+", alertKiloMeter = "+alertKiloMeter+", accelerZ = "+accelerZ+", rideTime = "+rideTime+", speed = "+speed+", rideDate = "+rideDate+", accelerX = "+accelerX+", gpsCount = "+gpsCount+", alert = "+alert+", selfConfidence = "+selfConfidence+", totalKiloMeter = "+totalKiloMeter+", anticipation = "+anticipation+", senRotate = "+senRotate+", drivingScore = "+drivingScore+", risk = "+risk+", alertId = "+alertId+", drivingSkill = "+drivingSkill+", alertTime = "+alertTime+", alertValue = "+alertValue+", longitude = "+longitude+"]";
        }
    }

    public class DeviceField
    {
        private String description;

        public String getDescription ()
        {
            return description;
        }

        public void setDescription (String description)
        {
            this.description = description;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [description = "+description+"]";
        }
    }
    public class DeviceDataField1
    {
        private String drivingStyle;

        private String trafficScore;

        private String riskStyle;

        private String driverState;

        private String transportMode;

        public String getDrivingStyle ()
        {
            return drivingStyle;
        }

        public void setDrivingStyle (String drivingStyle)
        {
            this.drivingStyle = drivingStyle;
        }

        public String getTrafficScore ()
        {
            return trafficScore;
        }

        public void setTrafficScore (String trafficScore)
        {
            this.trafficScore = trafficScore;
        }

        public String getRiskStyle ()
        {
            return riskStyle;
        }

        public void setRiskStyle (String riskStyle)
        {
            this.riskStyle = riskStyle;
        }

        public String getDriverState ()
        {
            return driverState;
        }

        public void setDriverState (String driverState)
        {
            this.driverState = driverState;
        }

        public String getTransportMode ()
        {
            return transportMode;
        }

        public void setTransportMode (String transportMode)
        {
            this.transportMode = transportMode;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [drivingStyle = "+drivingStyle+", trafficScore = "+trafficScore+", riskStyle = "+riskStyle+", driverState = "+driverState+", transportMode = "+transportMode+"]";
        }
    }


}