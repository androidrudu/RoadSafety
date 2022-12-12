package com.montbleu.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RiskAlertPOJO {
    @SerializedName("subCategory")
    @Expose
    private String subCategory;
    @SerializedName("riskTotalValue")
    @Expose
    private String riskTotalValue;
    @SerializedName("riskValueList")
    @Expose
    private List<Integer> riskValueList = null;

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getRiskTotalValue() {
        return riskTotalValue;
    }

    public void setRiskTotalValue(String riskTotalValue) {
        this.riskTotalValue = riskTotalValue;
    }

    public List<Integer> getRiskValueList() {
        return riskValueList;
    }

    public void setRiskValueList(List<Integer> riskValueList) {
        this.riskValueList = riskValueList;
    }
}
