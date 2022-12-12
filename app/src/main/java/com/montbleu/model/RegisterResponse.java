package com.montbleu.model;

public class RegisterResponse
{
    private String id;

    private String companyId;

    private String countryId;

    private String firstName;

    private String lastName;

    private String email;

    private String username;

    private String emailVerified;

    private String usernameVerified;

    private UserPrimaryDetail userPrimaryDetail;

    private UserField userField;

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
    public void setCountryId(String countryId){
        this.countryId = countryId;
    }
    public String getCountryId(){
        return this.countryId;
    }
    public void setFirstName(String firstName){
        this.firstName = firstName;
    }
    public String getFirstName(){
        return this.firstName;
    }
    public void setLastName(String lastName){
        this.lastName = lastName;
    }
    public String getLastName(){
        return this.lastName;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public String getEmail(){
        return this.email;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public String getUsername(){
        return this.username;
    }
    public void setEmailVerified(String emailVerified){
        this.emailVerified = emailVerified;
    }
    public String getEmailVerified(){
        return this.emailVerified;
    }
    public void setUsernameVerified(String usernameVerified){
        this.usernameVerified = usernameVerified;
    }
    public String getUsernameVerified(){
        return this.usernameVerified;
    }
    public void setUserPrimaryDetail(UserPrimaryDetail userPrimaryDetail){
        this.userPrimaryDetail = userPrimaryDetail;
    }
    public UserPrimaryDetail getUserPrimaryDetail(){
        return this.userPrimaryDetail;
    }
    public void setUserField(UserField userField){
        this.userField = userField;
    }
    public UserField getUserField(){
        return this.userField;
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

    public class UserField
    {
        private String age;

        private String emergencyContactNumber;

        private String drivingLicenseIssueDate;

        public void setAge(String age){
            this.age = age;
        }
        public String getAge(){
            return this.age;
        }
        public void setEmergencyContactNumber(String emergencyContactNumber){
            this.emergencyContactNumber = emergencyContactNumber;
        }
        public String getEmergencyContactNumber(){
            return this.emergencyContactNumber;
        }
        public void setDrivingLicenseIssueDate(String drivingLicenseIssueDate){
            this.drivingLicenseIssueDate = drivingLicenseIssueDate;
        }
        public String getDrivingLicenseIssueDate(){
            return this.drivingLicenseIssueDate;
        }
    }

    public class UserPrimaryDetail
    {
        private String primaryDivisionId;

        private String primaryModuleId;

        public void setPrimaryDivisionId(String primaryDivisionId){
            this.primaryDivisionId = primaryDivisionId;
        }
        public String getPrimaryDivisionId(){
            return this.primaryDivisionId;
        }
        public void setPrimaryModuleId(String primaryModuleId){
            this.primaryModuleId = primaryModuleId;
        }
        public String getPrimaryModuleId(){
            return this.primaryModuleId;
        }
    }


}

