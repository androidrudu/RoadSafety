package com.montbleu.model;

public class GetStartEditResponse{
    public String getId() {
        return this.id; }
    public void setId(String id) {
        this.id = id; }
    String id;
    public String getCompanyId() {
        return this.companyId; }
    public void setCompanyId(String companyId) {
        this.companyId = companyId; }
    String companyId;
    public String getCountryId() {
        return this.countryId; }
    public void setCountryId(String countryId) {
        this.countryId = countryId; }
    String countryId;
    public String getFirstName() {
        return this.firstName; }
    public void setFirstName(String firstName) {
        this.firstName = firstName; }
    String firstName;
    public String getLastName() {
        return this.lastName; }
    public void setLastName(String lastName) {
        this.lastName = lastName; }
    String lastName;
    public String getEmail() {
        return this.email; }
    public void setEmail(String email) {
        this.email = email; }
    String email;
    public String getUsername() {
        return this.username; }
    public void setUsername(String username) {
        this.username = username; }
    String username;
    public String getEmailVerified() {
        return this.emailVerified; }
    public void setEmailVerified(String emailVerified) {
        this.emailVerified = emailVerified; }
    String emailVerified;
    public String getUsernameVerified() {
        return this.usernameVerified; }
    public void setUsernameVerified(String usernameVerified) {
        this.usernameVerified = usernameVerified; }
    String usernameVerified;
    public UserPrimaryDetail getUserPrimaryDetail() {
        return this.userPrimaryDetail; }
    public void setUserPrimaryDetail(UserPrimaryDetail userPrimaryDetail) {
        this.userPrimaryDetail = userPrimaryDetail; }
    UserPrimaryDetail userPrimaryDetail;
    public UserField getUserField() {
        return this.userField; }
    public void setUserField(UserField userField) {
        this.userField = userField; }
    UserField userField;
    public String getStatus() {
        return this.status; }
    public void setStatus(String status) {
        this.status = status; }
    String status;
    public String getType() {
        return this.type; }
    public void setType(String type) {
        this.type = type; }
    String type;

    public class UserField{
        public String getAge() {
            return this.age; }
        public void setAge(String age) {
            this.age = age; }
        String age;
        public String getEmergencyContactNumber() {
            return this.emergencyContactNumber; }
        public void setEmergencyContactNumber(String emergencyContactNumber) {
            this.emergencyContactNumber = emergencyContactNumber; }
        String emergencyContactNumber;
        public String getDrivingLicenseIssueDate() {
            return this.drivingLicenseIssueDate; }
        public void setDrivingLicenseIssueDate(String drivingLicenseIssueDate) {
            this.drivingLicenseIssueDate = drivingLicenseIssueDate; }
        String drivingLicenseIssueDate;
    }

    public class UserPrimaryDetail{
        public String getPrimaryDivisionId() {
            return this.primaryDivisionId; }
        public void setPrimaryDivisionId(String primaryDivisionId) {
            this.primaryDivisionId = primaryDivisionId; }
        String primaryDivisionId;
        public String getPrimaryModuleId() {
            return this.primaryModuleId; }
        public void setPrimaryModuleId(String primaryModuleId) {
            this.primaryModuleId = primaryModuleId; }
        String primaryModuleId;
    }

}
