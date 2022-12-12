package com.montbleu.roadsafety.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.montbleu.Retrofit.RestClient;
import com.montbleu.Retrofit.RestMethods;
import com.montbleu.Utils.Constants;
import com.montbleu.Utils.PreferenceManager;
import com.montbleu.Utils.Preference_Details;
import com.montbleu.Utils.ProgressDialog;
import com.montbleu.Utils.Util_functions;
import com.montbleu.adapter.CustomAdapter;
import com.montbleu.interfaces.OnDialogButtonClickListener;
import com.montbleu.interfaces.Preference_Keys;
import com.montbleu.model.LogoutResponse;
import com.montbleu.roadsafety.R;
import com.montbleu.roadsafety.fragment.DashboardFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MorepageActvity extends Fragment implements View.OnClickListener, OnDialogButtonClickListener {
    ListView list;
    String[] profile_text = {"My Profile", "My Rides", "Settings", "Language", "Log Out", "About", "Terms & Conditions"};
    Integer[] img = {R.drawable.name_ic, R.drawable.myrides_ic, R.drawable.settings_ic, R.drawable.langicon_ic, R.drawable.logouticon_ic, R.drawable.abouticon_ic, R.drawable.termsicon_ic};

    RestMethods restMethods;
    private PreferenceManager mPrefernce_Manager = PreferenceManager.getInstance();
    Boolean mstatusRedirect = false;
    Boolean mconnectStatus = false;
    private long mLastClickTime = 0;
    ImageView back_more_btn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_morepage_actvity, container, false);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        ProgressDialog.setLottieProgressDialog(getActivity(), Constants.lottiePath);
        back_more_btn = view.findViewById(R.id.back_profile_btn);
        back_more_btn.setOnClickListener(this);
        if (Constants.img_profile) {
            try {
                Constants.img_profile = false;
                //profile_image.setImageURI(Uri.parse(Constants.BASE_URL+Constants.file+"?referredBy="+Constants.referredBy+"&sessionId="+Preference_Details.getLoginSessID()+"&companyId="+Constants.companyID+"&userId="+Preference_Details.getUserID()+"&type="+Constants.type_profile));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Constants.alertcount = true;
        CustomAdapter adapter = new CustomAdapter(getActivity(), profile_text, img);
        list = view.findViewById(R.id.listview_profile);
        list.setAdapter(adapter);

        list.setOnItemClickListener((arg0, arg1, position, arg3) -> {
            if (position == 0) {
                Intent profileActivity = new Intent(getActivity(), ProfileScreenActivity.class);
                startActivity(profileActivity);
            } else if (position == 1) {
                //myride
                Intent myIntent = new Intent(getActivity(), MyRidesPageActivity.class);
                startActivity(myIntent);
                MyRidesPageActivity.myRidesArrayList.clear();
                MyRidesPageActivity.offset = 0;
                MyRidesPageActivity.limit = 20;
                MyRidesPageActivity.limitLong = 30;
            } else if (position == 2) {
                Intent myIntent = new Intent(getActivity(), RoadSettingsPageActivity.class);
                startActivity(myIntent);
            } else if (position == 3) {
                //language
                Intent langIntent = new Intent(getActivity(), languageActivity.class);
                startActivity(langIntent);
            } else if (position == 4) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1500) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                mconnectStatus = true;
                Util_functions.AlertDialog(getActivity(), "Do you want Logout?", "Yes", "No", MorepageActvity.this);
            } else if (position == 5) {
                //about
                Intent myIntent = new Intent(getActivity(), RoadAboutPageActivity.class);
                startActivity(myIntent);
            } else if (position == 6) {
                //terms
                Intent myIntent = new Intent(getActivity(), RoadTermsPageActivity.class);
                startActivity(myIntent);
            }

        });
        return view;
    }

/*public void showDialog() {
        CharSequence[] items = {"MBT OBD","ELM OBD"};
        AlertDialog alert;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setSingleChoiceItems(items, -1,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which==0) {
                            Intent MBTActivity = new Intent(getActivity(), com.montbleu.OBD.activity.MBTActivity.class);
                            startActivity(MBTActivity);
                        } else {
                            Intent myIntent = new Intent(getActivity(), MainActivity.class);
                            startActivity(myIntent);
                        }
                        Util_functions.Show_Toast(getActivity(),items[which]+" Selected",true);
                        dialog.dismiss();
                    }

                });
        alert = builder.create();
        alert.setTitle("OBD DEVICE");
        alert.show();
    }*/


    private void onLogout() {
        ProgressDialog.setLottieProgressDialog(getActivity(), Constants.lottiePath);
        ProgressDialog.showProgress();
        restMethods = RestClient.buildHTTPClient();
        restMethods.getLogoutResponse(Constants.referredBy, Preference_Details.getLoginSessID(), Constants.companyID).enqueue(new Callback<LogoutResponse>() {
            @Override
            public void onResponse(@NonNull Call<LogoutResponse> call, @NonNull Response<LogoutResponse> response) {
                Log.e("status", "" + response.body());
                try {
                    if (response.code() == 202) {
                        //S001-success, E003 - profile not exist
                        mPrefernce_Manager.setBoolean(Preference_Keys.LOginKeys.LoginStatus, false);
                        Intent myIntent = new Intent(getActivity(), com.montbleu.roadsafety.activity.MainActivity.class);
                        startActivity(myIntent);
                        ProgressDialog.dismissProgress();
                        //Util_functions.Show_Toast(getContext(),getString(R.string.txt_logout_success),true);
                        MyRidesPageActivity.myRidesArrayList.clear();
                        DashboardFragment.scoreArrayList.clear();
                        MyRidesPageActivity.offset = 0;
                        MyRidesPageActivity.limit = 20;
                        MyRidesPageActivity.limitLong = 30;
                    } else if (response.code() == 412) {
                        try {
                            String Error = response.errorBody().string();
                            JSONObject obj = new JSONObject(Error);
                            JSONArray jsonArray1 = obj.getJSONArray("messages");
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                JSONObject object1 = jsonArray1.getJSONObject(0);
                                Util_functions.ShowValidateAlertError(getActivity(), object1.getString("message"), "OK");
                            }
                            //Util_functions.Show_Toast(getApplicationContext(),getString(R.string.resp_empty),true);
                            ProgressDialog.dismissProgress();
                        } catch (Exception e) {
                            e.printStackTrace();
                            ProgressDialog.dismissProgress();

                        }

                    } else {
                        String Error = response.errorBody().string();
                        JSONObject obj = new JSONObject(Error);
                        String text = obj.getString("message");
                        if (text.toLowerCase().contains("please login again") || text.contains("You don't have permission. Please contact Admin")) {
                            mstatusRedirect = true;
                        }
                        Util_functions.ShowAlertError(getActivity(), text, "OK", MorepageActvity.this);
                        // Util_functions.Show_Toast(getContext(),getString(R.string.txt_error),true);
                        ProgressDialog.dismissProgress();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ProgressDialog.dismissProgress();
                }
            }

            @Override
            public void onFailure(@NonNull Call<LogoutResponse> call, @NonNull Throwable t) {
                if (t.toString().contains("Unable to resolve host")) {
                    Util_functions.ShowValidateAlertError(getActivity(), getString(R.string.check_internet), "OK");
                }
                Util_functions.ShowAlertError(getActivity(), t.getMessage(), "OK", MorepageActvity.this);
                t.printStackTrace();
                ProgressDialog.dismissProgress();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_profile_btn:
                getActivity().finish();
                break;
        }
    }


    @Override
    public void onPositiveButtonClicked(AlertDialog alertDialog) {

        if (!mconnectStatus.booleanValue()) {
            ProgressDialog.dismissProgress();
        } else {
            if (mstatusRedirect) {
                moveToLogin();
            } else {
                mconnectStatus = false;
                if (Util_functions.ConnectivityCheck(getActivity(), MorepageActvity.this).booleanValue()) {
                    // mconnectStatus = true;
                    onLogout();
                }

            }
        }


    }

    @Override
    public void onNegativeButtonClicked() {

    }

    public void moveToLogin() {
        mPrefernce_Manager.setBoolean(Preference_Keys.LOginKeys.LoginStatus, false);
        MyRidesPageActivity.myRidesArrayList.clear();
        DashboardFragment.scoreArrayList.clear();
        MyRidesPageActivity.offset = 0;
        MyRidesPageActivity.limit = 20;
        MyRidesPageActivity.limitLong = 30;
        Intent loginScreen = new Intent(getActivity(), LoginPageActivity.class);
        startActivity(loginScreen);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Constants.img_profile) {
            try {
                Constants.img_profile = false;
                //profile_image.setImageURI(Uri.parse(Constants.BASE_URL+Constants.file+"?referredBy="+Constants.referredBy+"&sessionId="+Preference_Details.getLoginSessID()+"&companyId="+Constants.companyID+"&userId="+Preference_Details.getUserID()+"&type="+Constants.type_profile));
            } catch (Exception e) {
                //   profile.setBackground(getDrawable(R.drawable.profile_man));
                e.printStackTrace();
            }
        }
    }


}