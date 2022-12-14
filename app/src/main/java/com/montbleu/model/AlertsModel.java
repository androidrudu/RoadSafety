package com.montbleu.model;

public class AlertsModel {
    public String name;
    public String type;
    public String value;
    public Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public AlertsModel() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public AlertsModel(String name, String type, String value ,Integer id) {
        this.name = name;
        this.type = type;
        this.value = value;
        this.id   = id;
    }

}