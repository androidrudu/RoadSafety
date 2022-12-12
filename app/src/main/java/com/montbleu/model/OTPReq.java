package com.montbleu.model;

public class OTPReq {
    private String id;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    private String companyId;
    private String status;
    private String type;
    private String username;
    private String verificationCode;

    public UserVerificationField getUserVerificationField() {
        return userVerificationField;
    }

    public void setUserVerificationField(UserVerificationField userVerificationField) {
        this.userVerificationField = userVerificationField;
    }

    private UserVerificationField userVerificationField;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }



    public static class UserVerificationField{

        String mobilePin;
        String password;

        public String getMobilePin() {
            return mobilePin;
        }

        public void setMobilePin(String mobilePin) {
            this.mobilePin = mobilePin;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}

