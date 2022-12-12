package com.montbleu.model;


public class UserFeedbackReq
{
    private String companyId;

    private String divisionId;


    public String getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(String divisionId) {
        this.divisionId = divisionId;
    }
    private String moduleId;
    public String getModuleID() {
        return moduleId;
    }

    public void setModuleID(String moduleID) {
        this.moduleId = moduleID;
    }

    private String description;

    private String id;

    private String type;

    private String userId;

    private String status;

    public UserFeedbackField getUserFeedbackField() {
        return userFeedbackField;
    }

    public void setUserFeedbackField(UserFeedbackField userFeedbackField) {
        this.userFeedbackField = userFeedbackField;
    }

    private UserFeedbackField userFeedbackField;



    public String getCompanyId ()
    {
        return companyId;
    }

    public void setCompanyId (String companyId)
    {
        this.companyId = companyId;
    }

    public String getDescription ()
    {
        return description;
    }

    public void setDescription (String description)
    {
        this.description = description;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
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

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }


    public static class UserFeedbackField
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


    @Override
    public String toString()
    {
        return "ClassPojo [companyId = "+companyId+", description = "+description+", id = "+id+", type = "+type+", userId = "+userId+", status = "+status+"]";
    }
}

