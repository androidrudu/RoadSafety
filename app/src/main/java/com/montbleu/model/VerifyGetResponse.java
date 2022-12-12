package com.montbleu.model;



public class VerifyGetResponse
        {
private String id;

private String status;

private String type;

private UserVerificationField userVerificationField;

private String username;

private String verificationCode;

public void setId(String id){
        this.id = id;
        }
public String getId(){
        return this.id;
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
public void setUserVerificationField(UserVerificationField userVerificationField){
        this.userVerificationField = userVerificationField;
        }
public UserVerificationField getUserVerificationField(){
        return this.userVerificationField;
        }
public void setUsername(String username){
        this.username = username;
        }
public String getUsername(){
        return this.username;
        }
public void setVerificationCode(String verificationCode){
        this.verificationCode = verificationCode;
        }
public String getVerificationCode(){
        return this.verificationCode;
        }
            public class UserVerificationField
            {
                private String password;

                public void setPassword(String password){
                    this.password = password;
                }
                public String getPassword(){
                    return this.password;
                }
            }

        }



