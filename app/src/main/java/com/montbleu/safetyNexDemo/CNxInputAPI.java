package com.montbleu.safetyNexDemo;

public class CNxInputAPI {
public	float mTime;
	public	float mAccelX;
	public	float mAccelY;
	public	float mAccelZ;
	public	float mLat;
	public	float mLon;
	public	float mSpeed;
	public	float mCap;
	public	float mTimeDiffGPS;

	public boolean ParseData(String prmLine) {
		boolean isData = false;
		if(prmLine != null) {
			String line[] = prmLine.split(";");
			if(line.length > 3) {
				try {
                	mAccelX = Float.parseFloat(line[0]);
					mAccelY = Float.parseFloat(line[1]);
					mAccelZ = Float.parseFloat(line[2]);
					mLat = Float.parseFloat(line[3]);
					mLon = Float.parseFloat(line[4]);
					mSpeed = Float.parseFloat(line[5]);
					mTimeDiffGPS = Float.parseFloat(line[6]);
					mCap = Float.parseFloat(line[7]);
					isData = true;
				} catch (java.lang.NumberFormatException e) {
					e.printStackTrace();
				}
			}
		}
		return isData;
	}
}

/*
public class CNxInputAPI {
	public float mTime;
	public	float mAccelX;
	public	float mAccelY;
	public	float mAccelZ;
	public	float mLat;
	public	float mLon;
	public	float mSpeed;
	public	float mCap;
	public	float mTimeDiffGPS;
	public boolean ParseData(String prmLine) {
		boolean isData = false;
		if(prmLine != null) {
			String line[] = prmLine.split(";");
			if(line.length > 14) {
				try {
					mTime = Float.parseFloat(line[0]);
					mAccelX = Float.parseFloat(line[1]);
					mAccelY = Float.parseFloat(line[2]);
					mAccelZ = Float.parseFloat(line[3]);
					mLat = Float.parseFloat(line[10]);
					mLon = Float.parseFloat(line[11]);
					mCap = Float.parseFloat(line[12]);
					mSpeed = Float.parseFloat(line[13]);
					mTimeDiffGPS = Float.parseFloat(line[14]);
					isData = true;
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}
		}
		return isData;
	}
} */


