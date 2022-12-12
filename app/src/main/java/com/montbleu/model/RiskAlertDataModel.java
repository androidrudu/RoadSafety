package com.montbleu.model;

import java.util.List;


import java.util.List;

public class RiskAlertDataModel {
    int image;
    String title;
    String count;
    String alertId;
    List<Integer> graph;

    public RiskAlertDataModel(String title, String count,int image,String alertId , List<Integer> graph) {
        this.image = image;
        this.title = title;
        this.count = count;
        this.graph = graph;
        this.alertId = alertId;
    }

    public String getAlertId() {
        return alertId;
    }

    public void setAlertId(String alertId) {
        this.alertId = alertId;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public List<Integer> getGraph() {
        return graph;
    }

    public void setGraph(List<Integer> graph) {
        this.graph = graph;
    }
}
