package com.montbleu.Utils;


import com.montbleu.interfaces.Preference_Keys;

public class Preference_Details {
    private static final PreferenceManager mPrefsMgr = PreferenceManager.getInstance();

    public static String getLoginSessID() {
        return mPrefsMgr.getString(Preference_Keys.LOginKeys.LoginSessID, Preference_Keys.CommonKeys.EMPTY).trim();
    }

    public static String getLoginResObj() {
        return mPrefsMgr.getString(Preference_Keys.LOginKeys.LoginResObj, Preference_Keys.CommonKeys.EMPTY).trim();
    }

    public static Boolean getLoginStatus() {
        return mPrefsMgr.getBoolean(Preference_Keys.LOginKeys.LoginStatus,false);
    }

    public static String getCountryID(){
        return mPrefsMgr.getString(Preference_Keys.CommonKeys.CountryID, Preference_Keys.CommonKeys.EMPTY).trim();
    }

    public static String getContinentID(){
        return mPrefsMgr.getString(Preference_Keys.CommonKeys.ContinentID, Preference_Keys.CommonKeys.EMPTY).trim();
    }

    public static String getContinentArray(){
        return mPrefsMgr.getString(Preference_Keys.CommonKeys.ContinentArray, Preference_Keys.CommonKeys.EMPTY).trim();
    }

    public static String getCountryArray(){
        return mPrefsMgr.getString(Preference_Keys.CommonKeys.CountryArray, Preference_Keys.CommonKeys.EMPTY).trim();
    }

    public static String getCompanyID(){
        return mPrefsMgr.getString(Preference_Keys.LOginKeys.companyID, Preference_Keys.CommonKeys.EMPTY).trim();
    }

    public static String getDivisionID(){
        return mPrefsMgr.getString(Preference_Keys.LOginKeys.divisionID, Preference_Keys.CommonKeys.EMPTY).trim();
    }

    public static String getModuleId(){
        return mPrefsMgr.getString(Preference_Keys.LOginKeys.moduleID, Preference_Keys.CommonKeys.EMPTY).trim();
    }

    public static String getUserID(){
        return mPrefsMgr.getString(Preference_Keys.LOginKeys.userID, Preference_Keys.CommonKeys.EMPTY).trim();
    }

    public static String getPortionID(){
        return mPrefsMgr.getString(Preference_Keys.LOginKeys.portionID, Preference_Keys.CommonKeys.EMPTY).trim();
    }

    public static String getPropertyID(){
        return mPrefsMgr.getString(Preference_Keys.LOginKeys.propertyID, Preference_Keys.CommonKeys.EMPTY).trim();
    }

    public static String getSectID(){
        return mPrefsMgr.getString(Preference_Keys.LOginKeys.sectID, Preference_Keys.CommonKeys.EMPTY).trim();
    }

    public static String getUserName(){
        return mPrefsMgr.getString(Preference_Keys.LOginKeys.userName, Preference_Keys.CommonKeys.EMPTY).trim();
    }
    public static String getEmail(){
        return mPrefsMgr.getString(Preference_Keys.LOginKeys.EmailID, Preference_Keys.CommonKeys.EMPTY).trim();
    }

    public static String getLoginType(){
        return mPrefsMgr.getString(Preference_Keys.LOginKeys.LoginType, Preference_Keys.CommonKeys.EMPTY).trim();
    }

    public static String getLoginUserStatus(){
        return mPrefsMgr.getString(Preference_Keys.LOginKeys.userStatus, Preference_Keys.CommonKeys.EMPTY).trim();
    }
    public static String getFrstName(){
        return mPrefsMgr.getString(Preference_Keys.LOginKeys.FrstName, Preference_Keys.CommonKeys.EMPTY).trim();
    }
    public static String getPWD(){
        return mPrefsMgr.getString(Preference_Keys.LOginKeys.pwd, Preference_Keys.CommonKeys.EMPTY).trim();
    }
    public static Boolean getVoice(){
        return mPrefsMgr.getBoolean(Preference_Keys.LOginKeys.voice,Preference_Keys.CommonKeys.defStatus);
    }
    public static Boolean getBeep(){
        return mPrefsMgr.getBoolean(Preference_Keys.LOginKeys.beep,Preference_Keys.CommonKeys.defStatus);
    }
    public static Boolean getSpeedLimit(){
        return mPrefsMgr.getBoolean(Preference_Keys.LOginKeys.speedlimit,Preference_Keys.CommonKeys.defStatus);
    }
    public static Integer getCoPilotVal(){
        return mPrefsMgr.getInt(Preference_Keys.LOginKeys.coipilot,Preference_Keys.CommonKeys.Co_pilot);
    }
    public static Boolean getStartOnbg(){
        return mPrefsMgr.getBoolean(Preference_Keys.LOginKeys.startonBg,true);
    }
    public static Boolean getAppNoti(){
        return mPrefsMgr.getBoolean(Preference_Keys.LOginKeys.Appnotif,Preference_Keys.CommonKeys.defStatus);
    }
    public static Long getsessTime(){
        return mPrefsMgr.getLong(Preference_Keys.LOginKeys.sessionTime,0L);
    }
    public static String getDeviceId(){
        return mPrefsMgr.getString(Preference_Keys.LOginKeys.devicveID,"");
    }
    public static String getMapCode(){
        return mPrefsMgr.getString(Preference_Keys.LOginKeys.mapCode,"002");
    }
    public static String getPreviousMapCode(){
        return mPrefsMgr.getString(Preference_Keys.LOginKeys.previousmapCode,"002");
    }
    public static String getWifiSSID(){
        return mPrefsMgr.getString(Preference_Keys.LOginKeys.wifi_ssid,"");
    }
    public static String getWifiPwd(){
        return mPrefsMgr.getString(Preference_Keys.LOginKeys.wifi_pwd,"");
    }
    public static Integer getWifiGprs(){
        return mPrefsMgr.getInt(Preference_Keys.LOginKeys.wifi_gprs,2);
    }
    public static Boolean getGPSLoc(){
        return mPrefsMgr.getBoolean(Preference_Keys.LOginKeys.gpsLoc,false);
    }
    public static Boolean getBgStart(){
        return mPrefsMgr.getBoolean(Preference_Keys.LOginKeys.BgStart,false);
    }
    public static String getRegDeviceId(){
        return mPrefsMgr.getString(Preference_Keys.LOginKeys.RegDeviceId,"");
    }
}
