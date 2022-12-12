package com.montbleu.model;



public class ContinentResponse
{
    private String isoCode2;
    private String name;
    private String id;
    private String applicationType;
    public String getApplicationType() {
        return applicationType;
    }
    public void setApplicationType(String applicationType) {
        this.applicationType = applicationType;
    }
    public String getIsoCode2 ()
    {
        return isoCode2;
    }
    public void setIsoCode2 (String isoCode2)
    {
        this.isoCode2 = isoCode2;
    }
    public String getName ()
    {
        return name;
    }
    public void setName (String name)
    {
        this.name = name;
    }
    public String getId ()
    {
        return id;
    }
    public void setId (String id)
    {
        this.id = id;
    }
    @Override
    public String toString()
    {
        return "ClassPojo [isoCode2 = "+isoCode2+", name = "+name+", id = "+id+",applicationType="+applicationType+"]";
    }
}
