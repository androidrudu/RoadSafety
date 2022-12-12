package com.montbleu.model;

public class GPSDeviceResp
{
    private DeviceField deviceField;

    private String portionId;

    private String sectionId;

    private String type;

    private String userId;

    private String companyId;

    private String uniqueCode;

    private String name;

    private String id;

    private String divisionId;

    private String moduleId;

    private String category;

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

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getDivisionId ()
    {
        return divisionId;
    }

    public void setDivisionId (String divisionId)
    {
        this.divisionId = divisionId;
    }

    public String getModuleId ()
    {
        return moduleId;
    }

    public void setModuleId (String moduleId)
    {
        this.moduleId = moduleId;
    }

    public String getCategory ()
    {
        return category;
    }

    public void setCategory (String category)
    {
        this.category = category;
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
        return "ClassPojo [deviceField = "+deviceField+", portionId = "+portionId+", sectionId = "+sectionId+", type = "+type+", userId = "+userId+", companyId = "+companyId+", uniqueCode = "+uniqueCode+", name = "+name+", id = "+id+", divisionId = "+divisionId+", moduleId = "+moduleId+", category = "+category+", propertyId = "+propertyId+", status = "+status+"]";
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



}