package com.montbleu.model;

public class GPSEditDeviceReq
{
    private String companyId;

    private String portionId;

    private String divisionId;

    private String sectionId;

    private String category;

    private String subType;

    private DeviceDataField1 deviceDataField1;

    public DeviceDataField1 getDeviceDataField1 ()
    {
        return deviceDataField1;
    }

    public void setDeviceDataField1 (DeviceDataField1 deviceDataField1)
    {
        this.deviceDataField1 = deviceDataField1;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    private String subCategory;

    private DeviceDataField deviceDataField;

    private String moduleId;

    private String type;

    private String userId;

    private String propertyId;

    private String deviceId;

    private String status;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompanyId ()
    {
        return companyId;
    }

    public void setCompanyId (String companyId)
    {
        this.companyId = companyId;
    }

    public String getPortionId ()
    {
        return portionId;
    }

    public void setPortionId (String portionId)
    {
        this.portionId = portionId;
    }

    public String getDivisionId ()
    {
        return divisionId;
    }

    public void setDivisionId (String divisionId)
    {
        this.divisionId = divisionId;
    }

    public String getSectionId ()
    {
        return sectionId;
    }

    public void setSectionId (String sectionId)
    {
        this.sectionId = sectionId;
    }

    public String getCategory ()
    {
        return category;
    }

    public void setCategory (String category)
    {
        this.category = category;
    }



    public DeviceDataField getDeviceDataField ()
    {
        return deviceDataField;
    }

    public void setDeviceDataField (DeviceDataField deviceDataField)
    {
        this.deviceDataField = deviceDataField;
    }

    public String getModuleId ()
    {
        return moduleId;
    }

    public void setModuleId (String moduleId)
    {
        this.moduleId = moduleId;
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

    public String getPropertyId ()
    {
        return propertyId;
    }

    public void setPropertyId (String propertyId)
    {
        this.propertyId = propertyId;
    }

    public String getDeviceId ()
    {
        return deviceId;
    }

    public void setDeviceId (String deviceId)
    {
        this.deviceId = deviceId;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }


    public static class DeviceDataField1
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



    public static class DeviceDataField
    {

        private String deviceMode;

        private String gpsCount;

        private String accelerX;

        private String accelerY;

        private String accelerZ;

        private String senRotate;

        private String zipCode;

        private String alertId;

        private String alertValue;

        private String alertTime;

        private String rideDate;

        private String rideTime;

        private String gyroscopeX;

        private String gyroscopeY;

        private String gyroscopeZ;

        private String magnatometerX;

        private String magnatometerY;

        private String magnatometerZ;

        private String alertName;

        private String ehorizonLength;

        private String engineState;

        private String gpsTimeDiff;

        private String previousSpeed;

        private String previousTravelTime;

        public String getDeviceMode() {
            return deviceMode;
        }

        public void setDeviceMode(String deviceMode) {
            this.deviceMode = deviceMode;
        }

        public String getPreviousSpeed() {
            return previousSpeed;
        }

        public void setPreviousSpeed(String previousSpeed) {
            this.previousSpeed = previousSpeed;
        }

        public String getPreviousTravelTime() {
            return previousTravelTime;
        }

        public void setPreviousTravelTime(String previousTravelTime) {
            this.previousTravelTime = previousTravelTime;
        }

        public String getGyroscopeX() {
            return gyroscopeX;
        }

        public void setGyroscopeX(String gyroscopeX) {
            this.gyroscopeX = gyroscopeX;
        }

        public String getGyroscopeY() {
            return gyroscopeY;
        }

        public void setGyroscopeY(String gyroscopeY) {
            this.gyroscopeY = gyroscopeY;
        }

        public String getGyroscopeZ() {
            return gyroscopeZ;
        }

        public void setGyroscopeZ(String gyroscopeZ) {
            this.gyroscopeZ = gyroscopeZ;
        }

        public String getMagnatometerX() {
            return magnatometerX;
        }

        public void setMagnatometerX(String magnatometerX) {
            this.magnatometerX = magnatometerX;
        }

        public String getMagnatometerY() {
            return magnatometerY;
        }

        public void setMagnatometerY(String magnatometerY) {
            this.magnatometerY = magnatometerY;
        }

        public String getMagnatometerZ() {
            return magnatometerZ;
        }

        public void setMagnatometerZ(String magnatometerZ) {
            this.magnatometerZ = magnatometerZ;
        }

        public String getAlertName() {
            return alertName;
        }

        public void setAlertName(String alertName) {
            this.alertName = alertName;
        }

        public String getEhorizonLength() {
            return ehorizonLength;
        }

        public void setEhorizonLength(String ehorizonLength) {
            this.ehorizonLength = ehorizonLength;
        }

        public String getEngineState() {
            return engineState;
        }

        public void setEngineState(String engineState) {
            this.engineState = engineState;
        }

        public String getGpsTimeDiff() {
            return gpsTimeDiff;
        }

        public void setGpsTimeDiff(String gpsTimeDiff) {
            this.gpsTimeDiff = gpsTimeDiff;
        }

        public String getAlertKiloMeter() {
            return alertKiloMeter;
        }

        public void setAlertKiloMeter(String alertKiloMeter) {
            this.alertKiloMeter = alertKiloMeter;
        }

        private String alertKiloMeter;

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

        private String travelTime;

        private String alert;

        private String kiloMeter;

        private String selfConfidence;

        private String totalKiloMeter;

        private String anticipation;

        private String latitude;

        private String drivingScore;

        private String risk;

        private String drivingSkill;

        private String speed;

        private String longitude;

        private String urbanPercent;

        private String ruralPercent;

        private String highwayPercent;

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


        public String getTravelTime ()
        {
            return travelTime;
        }

        public void setTravelTime (String travelTime)
        {
            this.travelTime = travelTime;
        }

        public String getAlert ()
        {
            return alert;
        }

        public void setAlert (String alert)
        {
            this.alert = alert;
        }

        public String getKiloMeter ()
        {
            return kiloMeter;
        }

        public void setKiloMeter (String kiloMeter)
        {
            this.kiloMeter = kiloMeter;
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

        public String getLatitude ()
        {
            return latitude;
        }

        public void setLatitude (String latitude)
        {
            this.latitude = latitude;
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

        public String getDrivingSkill ()
        {
            return drivingSkill;
        }

        public void setDrivingSkill (String drivingSkill)
        {
            this.drivingSkill = drivingSkill;
        }

        public String getSpeed ()
        {
            return speed;
        }

        public void setSpeed (String speed)
        {
            this.speed = speed;
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
            return "ClassPojo [travelTime = "+travelTime+", alert = "+alert+", kiloMeter = "+kiloMeter+", selfConfidence = "+selfConfidence+", totalKiloMeter = "+totalKiloMeter+", anticipation = "+anticipation+", latitude = "+latitude+", drivingScore = "+drivingScore+", risk = "+risk+", drivingSkill = "+drivingSkill+", speed = "+speed+", longitude = "+longitude+"]";
        }
    }
}


