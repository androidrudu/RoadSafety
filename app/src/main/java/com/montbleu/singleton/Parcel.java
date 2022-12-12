package com.montbleu.singleton;

import org.json.JSONObject;

import java.io.Serializable;

public class Parcel implements Serializable {
    private JSONObject obj;

    public Parcel(JSONObject obj) {
        this.obj = obj;
    }

    public JSONObject getObj() {
        return obj;
    }}