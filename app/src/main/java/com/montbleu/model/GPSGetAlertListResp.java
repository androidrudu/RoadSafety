package com.montbleu.model;

import java.util.ArrayList;

public class GPSGetAlertListResp {
    private DeviceField deviceField;

    private String portionId;



    private String sectionId;

    private String type;

    private String userId;

    private String companyId;

    private String uniqueCode;

    private String name;

    private String divisionId;

    private String id;

    private String category;





    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }
    private String subCategory;

    private String moduleId;

    private ArrayList<DeviceDataList> deviceDataList;

    private String propertyId;

    private String status;

    public DeviceField getDeviceField ()
    {
        return deviceField;
    }

    public void setDeviceField (DeviceField deviceField)
    {
        this.deviceField = deviceField;
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

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
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

    public ArrayList<DeviceDataList> getDeviceDataList ()
    {
        return deviceDataList;
    }

    public void setDeviceDataList (ArrayList<DeviceDataList> deviceDataList)
    {
        this.deviceDataList = deviceDataList;
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
        return "ClassPojo [deviceField = "+deviceField+", portionId = "+portionId+", sectionId = "+sectionId+", type = "+type+", userId = "+userId+", companyId = "+companyId+", uniqueCode = "+uniqueCode+", name = "+name+", divisionId = "+divisionId+", id = "+id+", category = "+category+", moduleId = "+moduleId+", deviceDataList = "+deviceDataList+", propertyId = "+propertyId+", status = "+status+"]";
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

    public class DeviceDataLiveField
    {
        private String pm21;

        private String pm22;

        private String pm01;

        private String pm23;

        private String pm02;

        private String pm24;

        private String pm20;

        private String pm18;

        private String pm19;

        private String pm14;

        private String pm15;

        private String pm16;

        private String pm17;

        private String pm10;

        private String pm11;

        private String pm12;

        private String pm13;

        private String pm30;

        private String pm07;

        private String pm29;

        private String pm08;

        private String pm09;

        private String pm03;

        private String pm25;

        private String pm04;

        private String pm26;

        private String pm05;

        private String pm27;

        private String pm06;

        private String pm28;

        public String getPm21 ()
        {
            return pm21;
        }

        public void setPm21 (String pm21)
        {
            this.pm21 = pm21;
        }

        public String getPm22 ()
        {
            return pm22;
        }

        public void setPm22 (String pm22)
        {
            this.pm22 = pm22;
        }

        public String getPm01 ()
        {
            return pm01;
        }

        public void setPm01 (String pm01)
        {
            this.pm01 = pm01;
        }

        public String getPm23 ()
        {
            return pm23;
        }

        public void setPm23 (String pm23)
        {
            this.pm23 = pm23;
        }

        public String getPm02 ()
        {
            return pm02;
        }

        public void setPm02 (String pm02)
        {
            this.pm02 = pm02;
        }

        public String getPm24 ()
        {
            return pm24;
        }

        public void setPm24 (String pm24)
        {
            this.pm24 = pm24;
        }

        public String getPm20 ()
        {
            return pm20;
        }

        public void setPm20 (String pm20)
        {
            this.pm20 = pm20;
        }

        public String getPm18 ()
        {
            return pm18;
        }

        public void setPm18 (String pm18)
        {
            this.pm18 = pm18;
        }

        public String getPm19 ()
        {
            return pm19;
        }

        public void setPm19 (String pm19)
        {
            this.pm19 = pm19;
        }

        public String getPm14 ()
        {
            return pm14;
        }

        public void setPm14 (String pm14)
        {
            this.pm14 = pm14;
        }

        public String getPm15 ()
        {
            return pm15;
        }

        public void setPm15 (String pm15)
        {
            this.pm15 = pm15;
        }

        public String getPm16 ()
        {
            return pm16;
        }

        public void setPm16 (String pm16)
        {
            this.pm16 = pm16;
        }

        public String getPm17 ()
        {
            return pm17;
        }

        public void setPm17 (String pm17)
        {
            this.pm17 = pm17;
        }

        public String getPm10 ()
        {
            return pm10;
        }

        public void setPm10 (String pm10)
        {
            this.pm10 = pm10;
        }

        public String getPm11 ()
        {
            return pm11;
        }

        public void setPm11 (String pm11)
        {
            this.pm11 = pm11;
        }

        public String getPm12 ()
        {
            return pm12;
        }

        public void setPm12 (String pm12)
        {
            this.pm12 = pm12;
        }

        public String getPm13 ()
        {
            return pm13;
        }

        public void setPm13 (String pm13)
        {
            this.pm13 = pm13;
        }

        public String getPm30 ()
        {
            return pm30;
        }

        public void setPm30 (String pm30)
        {
            this.pm30 = pm30;
        }

        public String getPm07 ()
        {
            return pm07;
        }

        public void setPm07 (String pm07)
        {
            this.pm07 = pm07;
        }

        public String getPm29 ()
        {
            return pm29;
        }

        public void setPm29 (String pm29)
        {
            this.pm29 = pm29;
        }

        public String getPm08 ()
        {
            return pm08;
        }

        public void setPm08 (String pm08)
        {
            this.pm08 = pm08;
        }

        public String getPm09 ()
        {
            return pm09;
        }

        public void setPm09 (String pm09)
        {
            this.pm09 = pm09;
        }

        public String getPm03 ()
        {
            return pm03;
        }

        public void setPm03 (String pm03)
        {
            this.pm03 = pm03;
        }

        public String getPm25 ()
        {
            return pm25;
        }

        public void setPm25 (String pm25)
        {
            this.pm25 = pm25;
        }

        public String getPm04 ()
        {
            return pm04;
        }

        public void setPm04 (String pm04)
        {
            this.pm04 = pm04;
        }

        public String getPm26 ()
        {
            return pm26;
        }

        public void setPm26 (String pm26)
        {
            this.pm26 = pm26;
        }

        public String getPm05 ()
        {
            return pm05;
        }

        public void setPm05 (String pm05)
        {
            this.pm05 = pm05;
        }

        public String getPm27 ()
        {
            return pm27;
        }

        public void setPm27 (String pm27)
        {
            this.pm27 = pm27;
        }

        public String getPm06 ()
        {
            return pm06;
        }

        public void setPm06 (String pm06)
        {
            this.pm06 = pm06;
        }

        public String getPm28 ()
        {
            return pm28;
        }

        public void setPm28 (String pm28)
        {
            this.pm28 = pm28;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [pm21 = "+pm21+", pm22 = "+pm22+", pm01 = "+pm01+", pm23 = "+pm23+", pm02 = "+pm02+", pm24 = "+pm24+", pm20 = "+pm20+", pm18 = "+pm18+", pm19 = "+pm19+", pm14 = "+pm14+", pm15 = "+pm15+", pm16 = "+pm16+", pm17 = "+pm17+", pm10 = "+pm10+", pm11 = "+pm11+", pm12 = "+pm12+", pm13 = "+pm13+", pm30 = "+pm30+", pm07 = "+pm07+", pm29 = "+pm29+", pm08 = "+pm08+", pm09 = "+pm09+", pm03 = "+pm03+", pm25 = "+pm25+", pm04 = "+pm04+", pm26 = "+pm26+", pm05 = "+pm05+", pm27 = "+pm27+", pm06 = "+pm06+", pm28 = "+pm28+"]";
        }
    }


    public class DeviceDataList
    {
        private String subCategory;

        private String portionId;

        private String sectionId;

        private String type;

        private String deviceId;

        private String userId;

        private DeviceDataErrorField deviceDataErrorField;

        private String companyId;

        private DeviceDataLiveField deviceDataLiveField;

        private String noOfRecords;

        private String divisionId;

        private String id;

        private String category;

        private DeviceDataField deviceDataField;

        private String moduleId;

        private String propertyId;

        private String status;
        private String createdAtToTimeZone;

        private DeviceDataField1 deviceDataField1;

        public DeviceDataField1 getDeviceDataField1 ()
        {
            return deviceDataField1;
        }

        public void setDeviceDataField1 (DeviceDataField1 deviceDataField1)
        {
            this.deviceDataField1 = deviceDataField1;
        }

        public String getCreatedAtToTimeZone() {
            return createdAtToTimeZone;
        }

        public void setCreatedAtToTimeZone(String createdAtToTimeZone) {
            this.createdAtToTimeZone = createdAtToTimeZone;
        }

        public String getSubCategory ()
        {
            return subCategory;
        }

        public void setSubCategory (String subCategory)
        {
            this.subCategory = subCategory;
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

        public String getDeviceId ()
        {
            return deviceId;
        }

        public void setDeviceId (String deviceId)
        {
            this.deviceId = deviceId;
        }

        public String getUserId ()
        {
            return userId;
        }

        public void setUserId (String userId)
        {
            this.userId = userId;
        }

        public DeviceDataErrorField getDeviceDataErrorField ()
        {
            return deviceDataErrorField;
        }

        public void setDeviceDataErrorField (DeviceDataErrorField deviceDataErrorField)
        {
            this.deviceDataErrorField = deviceDataErrorField;
        }

        public String getCompanyId ()
        {
            return companyId;
        }

        public void setCompanyId (String companyId)
        {
            this.companyId = companyId;
        }

        public DeviceDataLiveField getDeviceDataLiveField ()
        {
            return deviceDataLiveField;
        }

        public void setDeviceDataLiveField (DeviceDataLiveField deviceDataLiveField)
        {
            this.deviceDataLiveField = deviceDataLiveField;
        }

        public String getNoOfRecords ()
        {
            return noOfRecords;
        }

        public void setNoOfRecords (String noOfRecords)
        {
            this.noOfRecords = noOfRecords;
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
            return "ClassPojo [subCategory = "+subCategory+", portionId = "+portionId+", sectionId = "+sectionId+", type = "+type+", deviceId = "+deviceId+", userId = "+userId+", deviceDataErrorField = "+deviceDataErrorField+", companyId = "+companyId+", deviceDataLiveField = "+deviceDataLiveField+", noOfRecords = "+noOfRecords+", divisionId = "+divisionId+", id = "+id+", category = "+category+", deviceDataField = "+deviceDataField+", moduleId = "+moduleId+", propertyId = "+propertyId+", status = "+status+"]";
        }
    }



    public class DeviceDataField
    {
        private String subCategory;

        public String getSubCategory() {
            return subCategory;
        }

        public void setSubCategory(String subCategory) {
            this.subCategory = subCategory;
        }

        private String zipCode;

        private String travelTime;

        private String kiloMeter;

        private String latitude;

        private String accelerY;

        private String alertKiloMeter;

        private String accelerZ;

        private String rideTime;

        private String rideDate;

        private String speed;

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

        private String alertTime;

        private String drivingSkill;

        private String alertValue;

        private String longitude;

        public String previousSpeed;

        public String previousTravelTime;

        public String urbanPercent;

        public String ruralPercent;

        public String highwayPercent;

        public String gyroscopeX;

        public String gyroscopeY;

        public String gyroscopeZ;

        public String magnetometerX;

        public String magnetometerY;

        public String magnetometerZ;

        public String alertName;

        public String ehorizonLength;

        public String engineState;

        public String gpsTimeDiff;

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

        public String getMagnetometerX() {
            return magnetometerX;
        }

        public void setMagnetometerX(String magnetometerX) {
            this.magnetometerX = magnetometerX;
        }

        public String getMagnetometerY() {
            return magnetometerY;
        }

        public void setMagnetometerY(String magnetometerY) {
            this.magnetometerY = magnetometerY;
        }

        public String getMagnetometerZ() {
            return magnetometerZ;
        }

        public void setMagnetometerZ(String magnetometerZ) {
            this.magnetometerZ = magnetometerZ;
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

        public String getRideDate ()
        {
            return rideDate;
        }

        public void setRideDate (String rideDate)
        {
            this.rideDate = rideDate;
        }

        public String getSpeed ()
        {
            return speed;
        }

        public void setSpeed (String speed)
        {
            this.speed = speed;
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

        public String getAlertTime ()
        {
            return alertTime;
        }

        public void setAlertTime (String alertTime)
        {
            this.alertTime = alertTime;
        }

        public String getDrivingSkill ()
        {
            return drivingSkill;
        }

        public void setDrivingSkill (String drivingSkill)
        {
            this.drivingSkill = drivingSkill;
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
            return "ClassPojo [zipCode = "+zipCode+", travelTime = "+travelTime+", kiloMeter = "+kiloMeter+", latitude = "+latitude+", accelerY = "+accelerY+", alertKiloMeter = "+alertKiloMeter+", accelerZ = "+accelerZ+", rideTime = "+rideTime+", rideDate = "+rideDate+", speed = "+speed+", accelerX = "+accelerX+", gpsCount = "+gpsCount+", alert = "+alert+", selfConfidence = "+selfConfidence+", totalKiloMeter = "+totalKiloMeter+", anticipation = "+anticipation+", senRotate = "+senRotate+", drivingScore = "+drivingScore+", risk = "+risk+", alertId = "+alertId+", alertTime = "+alertTime+", drivingSkill = "+drivingSkill+", alertValue = "+alertValue+", longitude = "+longitude+"]";
        }
    }

    public class DeviceDataErrorField
    {
        private String[] codes;

        public String[] getCodes ()
        {
            return codes;
        }

        public void setCodes (String[] codes)
        {
            this.codes = codes;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [codes = "+codes+"]";
        }
    }


}
