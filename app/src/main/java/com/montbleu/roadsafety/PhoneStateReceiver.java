package com.montbleu.roadsafety;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.montbleu.Utils.Constants;

public class PhoneStateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            System.out.println("Receiver start");
            Bundle bundle = intent.getExtras();
            Intent smsIntent = new Intent("phone");
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            if(state.equals(TelephonyManager.EXTRA_STATE_RINGING)){
                Constants.ring_count =12;
                Constants.ring_status=true;
            }
            if ((state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK))){
                Constants.ring_count=13;
                Constants.ring_status_miss=true;
            }
            if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)){
                Constants.ring_count=14;
            }

            LocalBroadcastManager.getInstance(context).sendBroadcast(smsIntent);

        }

        catch (Exception e){
            e.printStackTrace();
        }

    }
}
