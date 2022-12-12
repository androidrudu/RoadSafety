package com.montbleu.safetyNexDemo;

import android.util.Log;

import com.montbleu.Utils.Constants;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CNxDemoData {
	
	private String TAG = "CNxDemoData";
	private int mCount;
	private String mOutputFile;
	public String Accx;
	public String Accy;
	public String Accz;
	public String Latt;
	public String Long;
	public String Speed;

	public String getmCurrentDegree() {
		return mCurrentDegree;
	}

	public void setmCurrentDegree(String mCurrentDegree) {
		this.mCurrentDegree = mCurrentDegree;
	}

	public String getmInterval() {
		return mInterval;
	}

	public void setmInterval(String mInterval) {
		this.mInterval = mInterval;
	}

	public String mInterval;
	public String mCurrentDegree;
	public String allData;
	public String mData[];
	public String getAccx() {
		return Accx;
	}

	public void setAccx(String accx) {
		Accx = accx;
	}

	public String getAccy() {
		return Accy;
	}

	public void setAccy(String accy) {
		Accy = accy;
	}

	public String getAccz() {
		return Accz;
	}

	public void setAccz(String accz) {
		Accz = accz;
	}

	public String getLatt() {
		return Latt;
	}

	public void setLatt(String latt) {
		Latt = latt;
	}

	public String getLong() {
		return Long;
	}

	public void setLong(String aLong) {
		Long = aLong;
	}

	public String getSpeed() {
		return Speed;
	}

	public void setSpeed(String speed) {
		Speed = speed;
	}

	public CNxDemoData(String accx, String accy, String accz, String latt, String aLong, String speed,String mInterval,String mCurrentDegree ) {
		Accx = accx;
		Accy = accy;
		Accz = accz;
		Latt = latt;
		Long = aLong;
		Speed = speed;
		this.mInterval = mInterval;
		this.mCurrentDegree = mCurrentDegree;
		allData = Accx + ";" + Accy + ";" + Accz + ";" + Latt + ";" + Long + ";" + Speed + ";"+ this.mInterval + ";"+ this.mCurrentDegree + ";";
		mData = new String[]{
				"Time;Ax       ;Ay     ;Az     ;Gx;Gy;Gz;Mx;My;Mz;lat   ;lon    ;Direction(degres);Speed (Km/h);TimeDiff;SafetyState;Risk;NxAlertTS;NxAlert;NxAlertID;EHorizonLength;MapMatchPosLat;MapMatchPosLon;MapMatchConf;MapMatchCountry;TimeStampReplay",
				allData,
		};
		Constants.sCount++;
	}


	public CNxDemoData(String prmInputFile, String prmOutputFile) {
		mCount = 0;
		LoadData(prmInputFile);
		CreateFileForWriting(prmOutputFile);
	}
	
	private void LoadData(String prmInputFile) {
		if(prmInputFile != null) {
			File InputFile = new File(prmInputFile);
			if(InputFile.exists()) {
				FileReader InputReader;
				try {
					InputReader = new FileReader(InputFile);
					BufferedReader BIR = new BufferedReader(InputReader); 
					String str = BIR.readLine(); 
					int Count=0;
					if(str != null) {
						do {
							Count++;
							mDataList.add(str);
							str = BIR.readLine();
						} while(str != null);
					}
					BIR.close();
					InputReader.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public String ReadNextData() {
		mCount++;
 		return GetData(mCount);
 	}
 	private String GetData(int prmIndex) {
		if(prmIndex < GetLength())
			if(mDataList != null)
				return mDataList.get(prmIndex);
			else
				return mData[prmIndex];
		else
			return null;
	}
	
	public int GetLength() {
		if(mDataList != null)
			return mDataList.size();
		else
			return mData.length;
	}
 	public boolean isEOF() {
		return(mCount >= (GetLength() - 1));
	}
 	private void CreateFileForWriting(String prmOutputFile) {
		mOutputFile = prmOutputFile;
		if(mOutputFile != null) {
			File OutputFile = new File(mOutputFile);
			File TempDirP = OutputFile.getParentFile();
			if (TempDirP!=null) {
				boolean isParentCreated = TempDirP.mkdirs();
			}
			if(OutputFile.exists()) { //Always write a new file
				OutputFile.delete();
			}
			try {
				OutputFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
 	public void WriteData(String prmData) {
		if(mOutputFile != null) {
			File OutputFile = new File(mOutputFile);
			if(OutputFile.exists()) {
				FileWriter OutputWriter;
				try {
					OutputWriter = new FileWriter(OutputFile, true);
					BufferedWriter BIR = new BufferedWriter(OutputWriter);
					BIR.append(prmData);
					BIR.newLine();
					BIR.close();
					OutputWriter.close();
				} catch (IOException e) {
					Log.v(TAG, "Exception raised"); 
					e.printStackTrace();
				}
			} else {
				Log.v(TAG, "File doesn't exist");  
			}
		} else {
			Log.v(TAG, "Output is null");  			
		}
	}
	private ArrayList<String> mDataList = null;

/*	private String mData[] = {
			"Time;Ax       ;Ay     ;Az     ;Gx;Gy;Gz;Mx;My;Mz;lat   ;lon    ;Direction(degres);Speed (Km/h);TimeDiff;SafetyState;Risk;NxAlertTS;NxAlert;NxAlertID;EHorizonLength;MapMatchPosLat;MapMatchPosLon;MapMatchConf;MapMatchCountry;TimeStampReplay",
			"0.0 ;-0.571017;8.62152;3.89177;0 ;0 ;0 ;0 ;0 ;0 ;48.901;2.06621;264.583          ;38          ;0.0     ;2          ;-100;0        ;-1     ;0        ;3             ;0.0           ;0.0;-1.0;null;1569230433826",
			"93.0;0.11133;9.23084;4.3371;0;0;0;0;0;0;48.901;2.06621;264.583;38;0.093;2;-100;0;-1;0;3;0.0;0.0;-1.0;null;1569230433928",
			"144.0;-0.932542;8.35816;2.28766;0;0;0;0;0;0;48.901;2.06621;264.583;38;0.144;2;-100;0;-1;0;3;0.0;0.0;-1.0;null;1569230433981",
			"195.0;-0.464475;9.29908;2.85269;0;0;0;0;0;0;48.901;2.06621;264.583;38;0.195;2;-100;0;-1;0;3;48.90100875;2.06620713814341;0.8072748096167385;null;1569230434035",
			"246.0;-0.457293;9.59835;2.93769;0;0;0;0;0;0;48.901;2.06621;264.583;38;0.246;2;-100;0;-1;0;2090;48.90100769182494;2.0661802103735694;0.8072748096167385;FRA;1569230434102",
			"298.0;-1.14084;9.15543;3.16035;0;0;0;0;0;0;48.901;2.06621;264.583;38;0.298;1;0;0;-1;0;2090;48.901006969251;2.066161822768886;0.8072748096167385;FRA;1569230434158",
			"349.0;0.134075;9.13268;3.25611;0;0;0;0;0;0;48.901;2.06621;264.583;38;0.349;1;0;0;-1;0;2090;48.90100645654807;2.0661487758288595;0.8072748096167385;FRA;1569230434212",
			"399.0;0.253785;9.64504;4.61243;0;0;0;0;0;0;48.901;2.06621;264.583;38;0.399;1;0;0;-1;0;2090;48.901006163531925;2.066141319339265;0.8072748096167385;FRA;1569230434269",
			"451.0;-0.509965;8.14388;3.60088;0;0;0;0;0;0;48.901;2.06621;264.583;38;0.451;1;0;0;-1;0;2090;48.90100586923523;2.0661338302629724;0.8072748096167385;FRA;1569230434325",
			"501.0;-0.326809;8.65265;4.88297;0;0;0;0;0;0;48.901;2.06621;264.583;38;0.501;1;0;0;-1;0;2090;48.901005573977564;2.066126316732712;0.8072748096167385;FRA;1569230434379",
			"553.0;0.0514753;8.74602;4.04979;0;0;0;0;0;0;48.901;2.06621;264.583;38;0.553;1;0;0;-1;0;2090;48.90100498148288;2.0661112393023418;0.8072748096167385;FRA;1569230434435",
			"604.0;-0.673968;9.2895;5.80115;0;0;0;0;0;0;48.901;2.06621;264.583;38;0.604;1;0;0;-1;0;2090;48.901004684963254;2.066103693658413;0.8072748096167385;FRA;1569230434489",
			"656.0;-0.407014;8.65624;4.09887;0;0;0;0;0;0;48.901;2.06621;264.583;38;0.656;1;0;0;-1;0;2090;48.90100438844367;2.0660961480156366;0.8072748096167385;FRA;1569230434544",
			"709.0;-0.191536;9.14585;4.51786;0;0;0;0;0;0;48.901;2.06621;264.583;38;0.709;1;0;0;-1;0;2090;48.90100409192647;2.066088602433335;0.8072748096167385;FRA;1569230434601",
			"762.0;-0.317232;8.48266;4.69383;0;0;0;0;0;0;48.901;2.06621;264.583;38;0.762;1;0;0;-1;0;2090;48.90100379540647;2.0660810567798937;0.8072748096167385;FRA;1569230434658",
			 "815.0;-0.306458;9.33739;2.94726;0;0;0;0;0;0;48.901;2.06621;264.583;38;0.815;1;0;0;-1;0;2090;48.901003498888045;2.06607351116657;0.8072748096167385;FRA;1569230434715",
			"869.0;-0.778116;9.59955;4.20781;0;0;0;0;0;0;48.901;2.06621;264.583;38;0.869;1;0;0;-1;0;2090;48.90100320236972;2.0660659655558784;0.8072748096167385;FRA;1569230434773",
			"923.0;0.11971;9.37809;4.36224;0;0;0;0;0;0;48.901;2.06607;265.193;39;0.0;1;0;0;-1;0;2090;48.90100260933125;2.0660508742878587;0.8072748096167385;FRA;1569230434833",
			"1015.0;-0.659603;8.37731;5.05416;0;0;0;0;0;0;48.901;2.06607;265.193;39;0.092;1;7;0;-1;0;2090;48.901002312812174;2.066043328657672;0.8072748096167385;FRA;1569230434930",
			"1089.0;-1.01155;7.98107;2.81678;0;0;0;0;0;0;48.901;2.06607;265.193;39;0.166;1;7;0;-1;0;2090;48.90100171977509;2.0660282374248626;0.8072748096167385;FRA;1569230435008",
			"1151.0;0.211887;8.84179;5.01825;0;0;0;0;0;0;48.901;2.06607;265.193;39;0.228;1;6;0;-1;0;2090;48.901001423255735;2.066020691787942;0.8072748096167385;FRA;1569230435074",
			"1209.0;0.0335188;9.37809;3.97797;0;0;0;0;0;0;48.901;2.06607;265.193;39;0.286;1;7;0;-1;0;2090;48.90100083021607;2.066005600489712;0.8072748096167385;FRA;1569230435136",
			"1267.0;-0.877475;8.98185;4.41251;0;0;0;0;0;0;48.901;2.06607;265.193;39;0.344;1;8;0;-1;0;2090;48.901000527770314;2.0659979040414043;0.8072748096167385;FRA;1569230435196",
			"1328.0;0.782904;9.15303;5.11401;0;0;0;0;0;0;48.901;2.06607;265.193;39;0.405;1;9;0;-1;0;2090;48.90100023125042;2.0659903583906827;0.8072748096167385;FRA;1569230435261",
			"1409.0;0.565032;9.5373;3.36146;0;0;0;0;0;0;48.901;2.06607;265.193;39;0.486;1;12;0;-1;0;2090;48.900999638212944;2.065975267148059;0.8072748096167385;FRA;1569230435363",
			"1480.0;-0.214281;8.88608;3.61524;0;0;0;0;0;0;48.901;2.06607;265.193;39;0.557;1;13;0;-1;0;2090;48.900999045175126;2.0659601758968105;0.8072748096167385;FRA;1569230435438",
			"1560.0;-1.01155;8.09958;3.553;0;0;0;0;0;0;48.901;2.06607;265.193;39;0.637;1;16;0;-1;0;2090;48.90099874865427;2.065952630221558;0.8072748096167385;FRA;1569230435525",
			"1618.0;-0.48722;8.29232;4.7501;0;0;0;0;0;0;48.901;2.06607;265.193;39;0.695;1;14;0;-1;0;2090;48.90099815561435;2.0659375389166033;0.8072748096167385;FRA;1569230435588",
			"1676.0;-0.869095;8.44076;6.01663;0;0;0;0;0;0;48.901;2.06607;265.193;39;0.753;1;16;0;-1;0;2090;48.90099785908711;2.0659299930790187;0.8072748096167385;FRA;1569230435655",

};*/

}
