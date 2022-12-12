package com.montbleu.model;

public class GetModuleResponse
{
    private String companyId;

    private String divisionId;

    private String id;

    private String moduleId;

    private String roleModuleName;

    private String type;

    private String[] roleModulePrivileges;

    private String status;

    public String getCompanyId ()
    {
        return companyId;
    }

    public void setCompanyId (String companyId)
    {
        this.companyId = companyId;
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

    public String getModuleId ()
    {
        return moduleId;
    }

    public void setModuleId (String moduleId)
    {
        this.moduleId = moduleId;
    }

    public String getRoleModuleName ()
    {
        return roleModuleName;
    }

    public void setRoleModuleName (String roleModuleName)
    {
        this.roleModuleName = roleModuleName;
    }

    public String getType ()
    {
        return type;
    }

    public void setType (String type)
    {
        this.type = type;
    }

    public String[] getRoleModulePrivileges ()
    {
        return roleModulePrivileges;
    }

    public void setRoleModulePrivileges (String[] roleModulePrivileges)
    {
        this.roleModulePrivileges = roleModulePrivileges;
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
        return "ClassPojo [companyId = "+companyId+", divisionId = "+divisionId+", id = "+id+", moduleId = "+moduleId+", roleModuleName = "+roleModuleName+", type = "+type+", roleModulePrivileges = "+roleModulePrivileges+", status = "+status+"]";
    }
}

