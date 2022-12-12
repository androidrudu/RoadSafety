package com.montbleu.interfaces;

public interface Preference_Keys {

    interface CommonKeys
    {
        String EMPTY = " ";
        Boolean defStatus = true;
        String ContinentID ="continentID";
        String CountryID ="CountryID";
        String ContinentArray ="continentArray";
        String CountryArray ="countryArray";
        Integer Co_pilot = 70;
    }

    interface Faq_Keys
    {
        String mFaq_Response="faqresponse";
    }

    interface Shop_hours_Keys
    {
        String ShopHours = "ShopHours";
    }

    interface LOginKeys {
        String LoginSessID = "sessionID";
        String userName = "userName";
        String LoginResObj = "LoginResObj";
        String LoginStatus = "LoginStatus";
        String companyID = "companyID";
        String divisionID = "divisionID";
        String moduleID ="moduleId";
        String userID = "userID";
        String portionID = "PortionID";
        String propertyID = "PropertyID";
        String sectID ="SectionID";
        String LoginType="loginType";
        String userStatus="userStatus";
        String EmailID="emailID";
        String FrstName="Name";
        String BgStart = "BgStart";
        String pwd="PWD";
        String voice="Voice";
        String beep="beep";
        String speedlimit="speedlimit";
        String startonBg="startonBg";
        String Appnotif="Appnotif";
        String gpsLoc="gpsLoc";
        String coipilot="coipilot";
        String sessionTime="sessionTime";
        String devicveID="devicveID";
        String mapCode="mapCode";
        String previousmapCode="previousmapCode";
        String wifi_ssid="wifi_ssid";
        String wifi_pwd="wifi_pwd";
        String RegDeviceId="RegDeviceId";
        String wifi_gprs="wifi_gprs";
    }
}
