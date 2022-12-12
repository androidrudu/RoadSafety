package com.montbleu.singleton;

import android.app.AlertDialog;
import android.app.Application;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.montbleu.Utils.Constants;
import com.montbleu.Utils.PreferenceManager;
import com.montbleu.Utils.Util_functions;
import com.montbleu.interfaces.OnDialogButtonClickListener;
import com.montbleu.roadsafety.R;
import com.montbleu.roadsafety.activity.MorepageActvity;
import com.nexyad.jndksafetynex.JNDKSafetyNex;

import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;



/*@ReportsCrashes(mailTo = "manojmca15@gmail.com", customReportContent = {
        ReportField.APP_VERSION_CODE, ReportField.APP_VERSION_NAME,
        ReportField.ANDROID_VERSION, ReportField.PHONE_MODEL,
        ReportField.CUSTOM_DATA, ReportField.STACK_TRACE, ReportField.LOGCAT},
        mode = ReportingInteractionMode.TOAST, resToastText = R.string.crash_toast_text)*/
public class Road_Application extends Application {

    static com.montbleu.singleton.Road_Application instance;

    //Background Data
    private Timer mActivityTransitionTimer;
    private TimerTask mActivityTransitionTimerTask;
    public boolean wasInBackground;
    private final long MAX_ACTIVITY_TRANSITION_TIME_MS = 60000;
    @Override
    public void onCreate() {
        super.onCreate();
        ACRA.init(this);
        PreferenceManager.init(getApplicationContext());
        FirebaseApp.initializeApp(this);
        //getDeviceID();
        //  getUniTime();

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable e) {
                // Get the stack trace.
                new Thread() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        Toast.makeText(getApplicationContext(), "rudu Application Crashed. Kindly Report Us Through Mail", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                }.start();

                try
                {
                    Thread.sleep(2000); // Let the Toast display before app will get shutdown
                }
                catch (InterruptedException e2)
                {
                    // Ignored.
                }
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Stack trace", sw.toString());
                clipboard.setPrimaryClip(clip);
                Intent intent = new Intent (Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra (Intent.EXTRA_EMAIL, new String[] {"mbtappuser@gmail.com"});
                intent.putExtra (Intent.EXTRA_SUBJECT, "rudu Application Crash Report :");
                intent.putExtra (Intent.EXTRA_TEXT,  sw.toString());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // required when starting from Application
                startActivity(intent);

                /* // Add it to the clip board and close the app

                 */
                System.exit(1);
            }
        });


    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    private void createDB() {


    }

    private void getUniTime() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        long timeInMili = calendar .getTimeInMillis();
        Toast.makeText(getApplicationContext(),"Time & Date :"+convertDate(String.valueOf(timeInMili),"dd/MM/yyyy hh:mm aa"), Toast.LENGTH_SHORT).show();
    }
    public static String convertDate(String dateInMilliseconds,String dateFormat) {
        return DateFormat.format(dateFormat, Long.parseLong(dateInMilliseconds)).toString();
    }

    private void getDeviceID() {
        try {
            JNDKSafetyNex mJniFunction = JNDKSafetyNex.GetInstance(this);
            Constants.M_ANDROID_ID = mJniFunction.GetDeviceId();
        }catch (Exception e){
            e.printStackTrace();
        }

        // Toast.makeText(getApplicationContext(), "DeviceID"+android_id, Toast.LENGTH_SHORT).show();
    }

    private void baseConvert() {
        String str = "ARAlnubkJ5Teq/hE/OvVeXb0cDf/QgJN81alxUNRSWfyFhUifNFpLSbCt/Cf7BWksELqzov6meoHO0f5l02zZU793FoakZ6TPq77lxUsgX4V";

        byte[] dataa = Base64.decode(str, Base64.DEFAULT);
        String decoded = null;
        try {
            decoded = new String(dataa, "Windows-1252");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Toast.makeText(this, "Decoded Value :"+decoded, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void attachBaseContext(Context base) {

        super.attachBaseContext(base);
    }
    public static Road_Application getInstance() {
        return instance;
    }


    public void startActivityTransitionTimer() {
        this.mActivityTransitionTimer = new Timer();
        this.mActivityTransitionTimerTask = new TimerTask() {
            public void run() {
                Road_Application.this.wasInBackground = true;
            }
        };

        this.mActivityTransitionTimer.schedule(mActivityTransitionTimerTask,
                MAX_ACTIVITY_TRANSITION_TIME_MS);
    }

    public void stopActivityTransitionTimer() {
        if (this.mActivityTransitionTimerTask != null) {
            this.mActivityTransitionTimerTask.cancel();
        }

        if (this.mActivityTransitionTimer != null) {
            this.mActivityTransitionTimer.cancel();
        }

        this.wasInBackground = false;
    }



}
