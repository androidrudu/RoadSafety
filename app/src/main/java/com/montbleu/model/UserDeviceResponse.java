package com.montbleu.model;

public class UserDeviceResponse
{
    private String companyId;

    private String uniqueCode;

    private String name;

    private String id;

    private String divisionId;

    private String moduleId;

    private String type;

    private String userId;

    private UserDeviceField userDeviceField;

    private String status;

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

    public UserDeviceField getUserDeviceField ()
    {
        return userDeviceField;
    }

    public void setUserDeviceField (UserDeviceField userDeviceField)
    {
        this.userDeviceField = userDeviceField;
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
        return "ClassPojo [companyId = "+companyId+", uniqueCode = "+uniqueCode+", name = "+name+", id = "+id+", divisionId = "+divisionId+", moduleId = "+moduleId+", type = "+type+", userId = "+userId+", userDeviceField = "+userDeviceField+", status = "+status+"]";
    }


    public class UserDeviceField
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