package com.montbleu.model;

import java.util.ArrayList;



public class LoginResponse
{

    private UserSession userSession;
    private User user;
    private String divisionId;
    private String moduleID;

    public String getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(String divisionId) {
        this.divisionId = divisionId;
    }

    public String getModuleID() {
        return moduleID;
    }

    public void setModuleID(String moduleID) {
        this.moduleID = moduleID;
    }

    public UserSession getUserSession ()
    {
        return userSession;
    }

    public void setUserSession (UserSession userSession)
    {
        this.userSession = userSession;
    }

    public User getUser ()
    {
        return user;
    }

    public void setUser (User user)
    {
        this.user = user;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [userSession = "+userSession+", user = "+user+"]";
    }





    public class UserSession
    {
        private String id;

        private UserSessionField userSessionField;

        private String username;

        public String getId ()
        {
            return id;
        }

        public void setId (String id)
        {
            this.id = id;
        }

        public UserSessionField getUserSessionField ()
        {
            return userSessionField;
        }

        public void setUserSessionField (UserSessionField userSessionField)
        {
            this.userSessionField = userSessionField;
        }

        public String getUsername ()
        {
            return username;
        }

        public void setUsername (String username)
        {
            this.username = username;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [id = "+id+", userSessionField = "+userSessionField+", username = "+username+"]";
        }
    }


    public class UserSessionField
    {
        private String deviceType;

        private String deviceMACAddress;

        private String deviceToken;

        public String getDeviceType ()
        {
            return deviceType;
        }

        public void setDeviceType (String deviceType)
        {
            this.deviceType = deviceType;
        }

        public String getDeviceMACAddress ()
        {
            return deviceMACAddress;
        }

        public void setDeviceMACAddress (String deviceMACAddress)
        {
            this.deviceMACAddress = deviceMACAddress;
        }

        public String getDeviceToken ()
        {
            return deviceToken;
        }

        public void setDeviceToken (String deviceToken)
        {
            this.deviceToken = deviceToken;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [deviceType = "+deviceType+", deviceMACAddress = "+deviceMACAddress+", deviceToken = "+deviceToken+"]";
        }
    }

    public class User
    {
        private String firstName;

        private String lastName;

        private String emailVerified;

        private String companyId;

        private UserField userField;

        private String id;

        private UserPrimaryDetail userPrimaryDetail;

        private String type;

        private String email;

        private String usernameVerified;

        private String username;

        private String status;

        public String getFirstName ()
        {
            return firstName;
        }

        public void setFirstName (String firstName)
        {
            this.firstName = firstName;
        }

        public String getLastName ()
        {
            return lastName;
        }

        public void setLastName (String lastName)
        {
            this.lastName = lastName;
        }

        public String getEmailVerified ()
        {
            return emailVerified;
        }

        public void setEmailVerified (String emailVerified)
        {
            this.emailVerified = emailVerified;
        }

        public String getCompanyId ()
        {
            return companyId;
        }

        public void setCompanyId (String companyId)
        {
            this.companyId = companyId;
        }

        public UserField getUserField ()
        {
            return userField;
        }

        public void setUserField (UserField userField)
        {
            this.userField = userField;
        }

        public String getId ()
        {
            return id;
        }

        public void setId (String id)
        {
            this.id = id;
        }

        public UserPrimaryDetail getUserPrimaryDetail ()
        {
            return userPrimaryDetail;
        }

        public void setUserPrimaryDetail (UserPrimaryDetail userPrimaryDetail)
        {
            this.userPrimaryDetail = userPrimaryDetail;
        }

        public String getType ()
        {
            return type;
        }

        public void setType (String type)
        {
            this.type = type;
        }

        public String getEmail ()
        {
            return email;
        }

        public void setEmail (String email)
        {
            this.email = email;
        }

        public String getUsernameVerified ()
        {
            return usernameVerified;
        }

        public void setUsernameVerified (String usernameVerified)
        {
            this.usernameVerified = usernameVerified;
        }

        public String getUsername ()
        {
            return username;
        }

        public void setUsername (String username)
        {
            this.username = username;
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
            return "ClassPojo [firstName = "+firstName+", lastName = "+lastName+", emailVerified = "+emailVerified+", companyId = "+companyId+", userField = "+userField+", id = "+id+", userPrimaryDetail = "+userPrimaryDetail+", type = "+type+", email = "+email+", usernameVerified = "+usernameVerified+", username = "+username+", status = "+status+"]";
        }
    }


    public class UserField
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



    public class UserPrimaryDetail
    {
        private String primaryModuleId;

        private String primaryDivisionId;

        public String getPrimaryModuleId ()
        {
            return primaryModuleId;
        }

        public void setPrimaryModuleId (String primaryModuleId)
        {
            this.primaryModuleId = primaryModuleId;
        }

        public String getPrimaryDivisionId ()
        {
            return primaryDivisionId;
        }

        public void setPrimaryDivisionId (String primaryDivisionId)
        {
            this.primaryDivisionId = primaryDivisionId;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [primaryModuleId = "+primaryModuleId+", primaryDivisionId = "+primaryDivisionId+"]";
        }
    }





}

