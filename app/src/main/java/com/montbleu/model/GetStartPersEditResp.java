package com.montbleu.model;




public class GetStartPersEditResp {

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(String emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getUsernameVerified() {
        return usernameVerified;
    }

    public void setUsernameVerified(String usernameVerified) {
        this.usernameVerified = usernameVerified;
    }

    public UserPrimaryDetail getUserPrimaryDetail() {
        return userPrimaryDetail;
    }

    public void setUserPrimaryDetail(UserPrimaryDetail userPrimaryDetail) {
        this.userPrimaryDetail = userPrimaryDetail;
    }

    public UserField getUserField() {
        return userField;
    }

    public void setUserField(UserField userField) {
        this.userField = userField;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public class UserPrimaryDetail {

        private String primaryDivisionId;
        private String primaryModuleId;

        public String getPrimaryDivisionId() {
            return primaryDivisionId;
        }

        public void setPrimaryDivisionId(String primaryDivisionId) {
            this.primaryDivisionId = primaryDivisionId;
        }

        public String getPrimaryModuleId() {
            return primaryModuleId;
        }

        public void setPrimaryModuleId(String primaryModuleId) {
            this.primaryModuleId = primaryModuleId;
        }



    }


    public class UserField {

        private String age;
        private String emergencyContactNumber;
        private String drivingLicenseIssueDate;

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getEmergencyContactNumber() {
            return emergencyContactNumber;
        }

        public void setEmergencyContactNumber(String emergencyContactNumber) {
            this.emergencyContactNumber = emergencyContactNumber;
        }

        public String getDrivingLicenseIssueDate() {
            return drivingLicenseIssueDate;
        }

        public void setDrivingLicenseIssueDate(String drivingLicenseIssueDate) {
            this.drivingLicenseIssueDate = drivingLicenseIssueDate;
        }

    }


}




