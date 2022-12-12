package com.montbleu.model;


public class MapUpdateResponse
{
    private String companyId;

    private String code;

    private String systemCode;

    private String name;

    private ModulePreferenceField modulePreferenceField;

    private String id;

    private String divisionId;

    private String moduleId;

    private String category;

    private String type;

    private String status;

    public String getCompanyId ()
    {
        return companyId;
    }

    public void setCompanyId (String companyId)
    {
        this.companyId = companyId;
    }

    public String getCode ()
    {
        return code;
    }

    public void setCode (String code)
    {
        this.code = code;
    }

    public String getSystemCode ()
    {
        return systemCode;
    }

    public void setSystemCode (String systemCode)
    {
        this.systemCode = systemCode;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public ModulePreferenceField getModulePreferenceField ()
    {
        return modulePreferenceField;
    }

    public void setModulePreferenceField (ModulePreferenceField modulePreferenceField)
    {
        this.modulePreferenceField = modulePreferenceField;
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

    public String getType ()
    {
        return type;
    }

    public void setType (String type)
    {
        this.type = type;
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
        return "ClassPojo [companyId = "+companyId+", code = "+code+", systemCode = "+systemCode+", name = "+name+", modulePreferenceField = "+modulePreferenceField+", id = "+id+", divisionId = "+divisionId+", moduleId = "+moduleId+", category = "+category+", type = "+type+", status = "+status+"]";
    }
    public class ModulePreferenceField
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