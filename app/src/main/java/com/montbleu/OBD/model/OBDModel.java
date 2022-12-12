package com.montbleu.OBD.model;

public class OBDModel {
    public String value;
    public String name;
    public String id;
    public Boolean status;
    public Integer mImage;

    public Integer getmImage() {
        return mImage;
    }

    public void setmImage(Integer mImage) {
        this.mImage = mImage;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public OBDModel(String id,String name,String value,Integer mImage,Boolean status) {
        this.value = value;
        this.name = name;
        this.id=id;
        this.status = status;
        this.mImage = mImage;
    }

    public OBDModel(String id,String name,String value,Boolean status) {
        this.value = value;
        this.name = name;
        this.id=id;
        this.status = status;
       // this.mImage = mImage;
    }

}
