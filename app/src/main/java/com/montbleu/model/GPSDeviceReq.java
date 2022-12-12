package com.montbleu.model;


public class GPSDeviceReq {
    private String id;
    private String companyId;
    private String divisionId;
    private String moduleId;
    private String userId;
    private String propertyId;
    private String sectionId;
    private String portionId;
    private String name;
    private String timezoneCode;
    private String locationName;
    private String uniqueCode;
    DeviceField DeviceFieldObject;
    private String category;
    private String status;
    private String type;
    private String subType;



    // Getter Methods


    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }


    public String getId() {
        return id;
    }

    public String getCompanyId() {
        return companyId;
    }

    public String getDivisionId() {
        return divisionId;
    }

    public String getModuleId() {
        return moduleId;
    }

    public String getUserId() {
        return userId;
    }

    public String getPropertyId() {
        return propertyId;
    }

    public String getSectionId() {
        return sectionId;
    }

    public String getPortionId() {
        return portionId;
    }

    public String getName() {
        return name;
    }

    public String getTimezoneCode() {
        return timezoneCode;
    }

    public String getLocationName() {
        return locationName;
    }

    public String getUniqueCode() {
        return uniqueCode;
    }

    public DeviceField getDeviceField() {
        return DeviceFieldObject;
    }

    public String getCategory() {
        return category;
    }

    public String getStatus() {
        return status;
    }

    public String getType() {
        return type;
    }

    // Setter Methods

    public void setId(String id) {
        this.id = id;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public void setDivisionId(String divisionId) {
        this.divisionId = divisionId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public void setPortionId(String portionId) {
        this.portionId = portionId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTimezoneCode(String timezoneCode) {
        this.timezoneCode = timezoneCode;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    public void setDeviceField(DeviceField deviceFieldObject) {
        this.DeviceFieldObject = deviceFieldObject;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static class DeviceField {
        private String description;
        private String city;
        private String state;
        private String country;
        private String zipCode;
        // Getter Methods

        public String getDescription() {
            return description;
        }

        public String getCity() {
            return city;
        }

        public String getState() {
            return state;
        }

        public String getCountry() {
            return country;
        }

        public String getZipCode() {
            return zipCode;
        }

        // Setter Methods

        public void setDescription(String description) {
            this.description = description;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public void setState(String state) {
            this.state = state;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public void setZipCode(String zipCode) {
            this.zipCode = zipCode;
        }
    }

}


