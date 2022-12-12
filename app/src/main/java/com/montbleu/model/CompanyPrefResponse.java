package com.montbleu.model;


import java.util.ArrayList;

public class CompanyPrefResponse {


        private String companyId;

        private String name;

        private String id;

        private String type;

        private CompanyPreferenceField companyPreferenceField;

        private String status;

        public String getCompanyId ()
        {
            return companyId;
        }

        public void setCompanyId (String companyId)
        {
            this.companyId = companyId;
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

        public String getType ()
        {
            return type;
        }

        public void setType (String type)
        {
            this.type = type;
        }

        public CompanyPreferenceField getCompanyPreferenceField ()
        {
            return companyPreferenceField;
        }

        public void setCompanyPreferenceField (CompanyPreferenceField companyPreferenceField)
        {
            this.companyPreferenceField = companyPreferenceField;
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
            return "ClassPojo [companyId = "+companyId+", name = "+name+", id = "+id+", type = "+type+", companyPreferenceField = "+companyPreferenceField+", status = "+status+"]";
        }




    public class CompanyPreferenceField
    {
        private ArrayList<String> lines;

        public ArrayList<String> getLines ()
        {
            return lines;
        }

        public void setLines (ArrayList<String> lines)
        {
            this.lines = lines;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [lines = "+lines+"]";
        }
    }



}
