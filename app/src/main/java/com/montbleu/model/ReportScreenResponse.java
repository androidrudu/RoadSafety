package com.montbleu.model;

public class ReportScreenResponse
{
    private String id;

    private String companyId;

    private String userId;

    private String description;

    private UserFeedbackField userFeedbackField;

    private String status;

    private String type;

    public void setId(String id){
        this.id = id;
    }
    public String getId(){
        return this.id;
    }
    public void setCompanyId(String companyId){
        this.companyId = companyId;
    }
    public String getCompanyId(){
        return this.companyId;
    }
    public void setUserId(String userId){
        this.userId = userId;
    }
    public String getUserId(){
        return this.userId;
    }
    public void setDescription(String description){
        this.description = description;
    }
    public String getDescription(){
        return this.description;
    }
    public void setUserFeedbackField(UserFeedbackField userFeedbackField){
        this.userFeedbackField = userFeedbackField;
    }
    public UserFeedbackField getUserFeedbackField(){
        return this.userFeedbackField;
    }
    public void setStatus(String status){
        this.status = status;
    }
    public String getStatus(){
        return this.status;
    }
    public void setType(String type){
        this.type = type;
    }
    public String getType(){
        return this.type;
    }

    public class UserFeedbackField
    {
        private String description;

        public void setDescription(String description){
            this.description = description;
        }
        public String getDescription(){
            return this.description;
        }
    }
}
