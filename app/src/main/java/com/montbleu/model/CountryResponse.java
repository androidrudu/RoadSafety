package com.montbleu.model;


public class CountryResponse
{
    private String continentId;

    private String isoCode2;

    private String isoCode3;

    private String name;

    private String id;

    private String dialingCode;

    private String applicationType;

    public String getApplicationType() {
        return applicationType;
    }
    public void setApplicationType(String applicationType) {
        this.applicationType = applicationType;
    }

    public String getContinentId ()
    {
        return continentId;
    }

    public void setContinentId (String continentId)
    {
        this.continentId = continentId;
    }

    public String getIsoCode2 ()
    {
        return isoCode2;
    }

    public void setIsoCode2 (String isoCode2)
    {
        this.isoCode2 = isoCode2;
    }

    public String getIsoCode3 ()
    {
        return isoCode3;
    }

    public void setIsoCode3 (String isoCode3)
    {
        this.isoCode3 = isoCode3;
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

    public String getDialingCode ()
    {
        return dialingCode;
    }

    public void setDialingCode (String dialingCode)
    {
        this.dialingCode = dialingCode;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [continentId = "+continentId+", isoCode2 = "+isoCode2+", isoCode3 = "+isoCode3+", name = "+name+", id = "+id+", dialingCode = "+dialingCode+",applicationType="+applicationType+"]";
    }
}

