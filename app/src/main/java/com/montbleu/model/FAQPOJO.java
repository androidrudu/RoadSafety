package com.montbleu.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FAQPOJO {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("companyId")
    @Expose
    private String companyId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("companyPreferenceField")
    @Expose
    private CompanyPreferenceField companyPreferenceField;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("type")
    @Expose
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CompanyPreferenceField getCompanyPreferenceField() {
        return companyPreferenceField;
    }

    public void setCompanyPreferenceField(CompanyPreferenceField companyPreferenceField) {
        this.companyPreferenceField = companyPreferenceField;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public class CompanyPreferenceField {

        @SerializedName("lines")
        @Expose
        private List<Object> lines = null;

        public List<Object> getLines() {
            return lines;
        }

        public void setLines(List<Object> lines) {
            this.lines = lines;
        }
    }
}
