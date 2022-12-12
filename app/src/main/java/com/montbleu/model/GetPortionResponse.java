package com.montbleu.model;


public class GetPortionResponse
{
    private String companyId;

    private String name;

    private String divisionId;

    private String id;

    private PortionField portionField;

    private String sectionId;

    private String category;

    private String moduleId;

    private String type;

    private String propertyId;

    private String userId;

    private String status;

    public String getCompanyId ()
    {
        return companyId;
    }

    public void setCompanyId (String companyId)
    {
        this.companyId = companyId;
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

    public PortionField getPortionField ()
    {
        return portionField;
    }

    public void setPortionField (PortionField portionField)
    {
        this.portionField = portionField;
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

    public String getPropertyId ()
    {
        return propertyId;
    }

    public void setPropertyId (String propertyId)
    {
        this.propertyId = propertyId;
    }

    public String getUserId ()
    {
        return userId;
    }

    public void setUserId (String userId)
    {
        this.userId = userId;
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
        return "ClassPojo [companyId = "+companyId+", name = "+name+", divisionId = "+divisionId+", id = "+id+", portionField = "+portionField+", sectionId = "+sectionId+", category = "+category+", moduleId = "+moduleId+", type = "+type+", propertyId = "+propertyId+", userId = "+userId+", status = "+status+"]";
    }

    public class PortionField
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


