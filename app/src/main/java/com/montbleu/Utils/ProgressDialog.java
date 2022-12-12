package com.montbleu.Utils;

import android.app.Activity;
import android.graphics.Color;

import com.montbleu.roadsafety.BeautifulProgressDialog;


public class ProgressDialog {

    String lottie;
    String msg;
   static BeautifulProgressDialog progressDialog;
   public static void setLottieProgressDialog(Activity activity,String lottie_path){
   lottieProg(activity,lottie_path);
   }

    private static void lottieProg(Activity activity,String lottie_path) {
        progressDialog = new BeautifulProgressDialog(activity, BeautifulProgressDialog.withLottie, null);
        progressDialog.setLottieLocation(lottie_path);
        progressDialog.setLottieLoop(true);
        progressDialog.setLayoutColor(Color.TRANSPARENT);
        progressDialog.setLayoutElevation(0);
        progressDialog.setCancelable(true);
    }

    public static void setProgressDialog(Activity activity,String lottie_path,String msg){
        progressDialog = new BeautifulProgressDialog(activity, BeautifulProgressDialog.withLottie, null);
        progressDialog.setLottieLocation(lottie_path);
        progressDialog.setLottieLoop(true);
        progressDialog.setLayoutColor(Color.TRANSPARENT);
        progressDialog.setLayoutElevation(0);
        progressDialog.setCancelable(true);
        progressDialog.setMessage(msg);
    }

    public static void showProgress(){
        if (progressDialog!=null) {
            progressDialog.setCancelable(true);
            progressDialog.show();
        }
    }

    public static void dismissProgress(){
        if(progressDialog!=null){
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }




}
