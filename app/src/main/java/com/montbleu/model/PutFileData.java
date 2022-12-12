package com.montbleu.model;


public class PutFileData {
    private String category;
    private String companyId;
    private String countryId;
    private String email;
    private String emailVerified;
    private String firstName;
    private String id;
    private String lastName;
    private String profilePicturePath;
    private String status;
    private String timeZoneId;
    private String type;
    UserField UserField;
    UserSessionField UserSessionField;
    private String username;
    private String usernameVerified;


    // Getter Methods

    public String getCategory() {
        return category;
    }

    public String getCompanyId() {
        return companyId;
    }

    public String getCountryId() {
        return countryId;
    }

    public String getEmail() {
        return email;
    }

    public String getEmailVerified() {
        return emailVerified;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getId() {
        return id;
    }

    public String getLastName() {
        return lastName;
    }

    public String getProfilePicturePath() {
        return profilePicturePath;
    }

    public String getStatus() {
        return status;
    }

    public String getTimeZoneId() {
        return timeZoneId;
    }

    public String getType() {
        return type;
    }

    public UserField getUserField() {
        return UserField;
    }

    public UserSessionField getUserSessionField() {
        return UserSessionField;
    }

    public String getUsername() {
        return username;
    }

    public String getUsernameVerified() {
        return usernameVerified;
    }

    // Setter Methods

    public void setCategory(String category) {
        this.category = category;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setEmailVerified(String emailVerified) {
        this.emailVerified = emailVerified;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setProfilePicturePath(String profilePicturePath) {
        this.profilePicturePath = profilePicturePath;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTimeZoneId(String timeZoneId) {
        this.timeZoneId = timeZoneId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUserField(UserField userField) {
        this.UserField = userField;
    }

    public void setUserSessionField(UserSessionField userSessionField) {
        this.UserSessionField = userSessionField;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUsernameVerified(String usernameVerified) {
        this.usernameVerified = usernameVerified;
    }
}
class UserSessionField {
    private String deviceModelName;
    private String deviceStatus;
    private String deviceType;
    private String deviceUniqueId;
    private String deviceVersionNumber;


    // Getter Methods

    public String getDeviceModelName() {
        return deviceModelName;
    }

    public String getDeviceStatus() {
        return deviceStatus;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public String getDeviceUniqueId() {
        return deviceUniqueId;
    }

    public String getDeviceVersionNumber() {
        return deviceVersionNumber;
    }

    // Setter Methods

    public void setDeviceModelName(String deviceModelName) {
        this.deviceModelName = deviceModelName;
    }

    public void setDeviceStatus(String deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public void setDeviceUniqueId(String deviceUniqueId) {
        this.deviceUniqueId = deviceUniqueId;
    }

    public void setDeviceVersionNumber(String deviceVersionNumber) {
        this.deviceVersionNumber = deviceVersionNumber;
    }
}
 class UserField {
    private String age;
    private String drivingLicenseIssueDate;
    private String emergencyContactNumber;


    // Getter Methods

    public String getAge() {
        return age;
    }

    public String getDrivingLicenseIssueDate() {
        return drivingLicenseIssueDate;
    }

    public String getEmergencyContactNumber() {
        return emergencyContactNumber;
    }

    // Setter Methods

    public void setAge(String age) {
        this.age = age;
    }

    public void setDrivingLicenseIssueDate(String drivingLicenseIssueDate) {
        this.drivingLicenseIssueDate = drivingLicenseIssueDate;
    }

    public void setEmergencyContactNumber(String emergencyContactNumber) {
        this.emergencyContactNumber = emergencyContactNumber;
    }
}