package com.montbleu.Retrofit;

import android.media.Image;

import com.montbleu.Utils.Constants;
import com.montbleu.model.CompanyPrefResponse;
import com.montbleu.model.ContinentResponse;
import com.montbleu.model.CountryResponse;
import com.montbleu.model.DashboardResModel;
import com.montbleu.model.DriveSummaryPOJO;
import com.montbleu.model.DrivingScorePOJO;
import com.montbleu.model.FAQPOJO;
import com.montbleu.model.GPSDeviceReq;
import com.montbleu.model.GPSDeviceResp;
import com.montbleu.model.GPSEditDeviceReq;
import com.montbleu.model.GPSEditDeviceResp;
import com.montbleu.model.GPSGETDeviceResp;
import com.montbleu.model.GPSGetAlertListResp;
import com.montbleu.model.GetModuleResponse;
import com.montbleu.model.GetPortionResponse;
import com.montbleu.model.GetStartEditResponse;
import com.montbleu.model.GetStartPersEditResp;
import com.montbleu.model.LicenseUpdateReq;
import com.montbleu.model.LoginRequest;
import com.montbleu.model.LoginResponse;
import com.montbleu.model.LogoutResponse;
import com.montbleu.model.MapSettingReq;
import com.montbleu.model.MapSettingResponse;
import com.montbleu.model.MapUpdateResponse;
import com.montbleu.model.OTPReq;
import com.montbleu.model.OTPVerification;
import com.montbleu.model.ProfileEditResponse;
import com.montbleu.model.ProfileGetResponse;
import com.montbleu.model.RegisterReq;
import com.montbleu.model.RegisterResponse;
import com.montbleu.model.ReportScreenResponse;
import com.montbleu.model.RideLeaderboardPOJO;
import com.montbleu.model.RiskAlertPOJO;
import com.montbleu.model.SettingEditResponse;
import com.montbleu.model.SettingGetGenResponse;
import com.montbleu.model.UserDeviceDataModalResp;
import com.montbleu.model.UserDeviceResponse;
import com.montbleu.model.UserFeedbackPOJO;
import com.montbleu.model.UserFeedbackReq;
import com.montbleu.model.UserMapSettingReq;
import com.montbleu.model.UserSettingGETResponse;
import com.montbleu.model.UserSettingRequest;
import com.montbleu.model.UserSettingResponse;
import com.montbleu.model.VerifyGetResponse;
import com.montbleu.roadsafety.AboutUsResponse;

import java.io.StringReader;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by Umair Adil on 10/12/2016.
 */

public interface RestMethods
{

    @GET(Constants.dashboard)
    Call<List<DashboardResModel>> getDashboardResponse(@Header("referredBy") String referredBy, @Header("sessionId") String sessionId,@Query("companyId") String  companyId,@Query("userId") String userId,@Query("type") String type,@Query("dashboardType") String dashboardType, @Query("offset") int offset, @Query("limit") int limit);

    @GET(Constants.companyPreference)
    Call<List<CompanyPrefResponse>> getCompanyPreference(@Header("referredBy") String referredBy, @Header("sessionId") String sessionId,@Query("companyId") String  companyId,@Query("type") String type, @Query("offset") int offset, @Query("limit") int limit);

    @GET(Constants.companyPreference)
    Call<List<AboutUsResponse>> getAboutUs(@Header("referredBy") String referredBy, @Header("sessionId") String sessionId,@Query("companyId") String  companyId,@Query("type") String type, @Query("offset") int offset, @Query("limit") int limit);

    //LOgin-Post Logout - DELETE
    @POST(Constants.userSession)
    Call<LoginResponse>  getLoginResponse(@Header("referredBy") String  referredBy,@Header("sessionId") String sessionId, @Body LoginRequest loginRequest);

    @DELETE(Constants.userSession)
    Call<LogoutResponse>  getLogoutResponse(@Header("referredBy") String  referredBy, @Header("sessionId") String sessionId,@Header("companyId") String companyId);

    @GET(Constants.continent)
    Call<List<ContinentResponse>>  getContinentResponse(@Header("referredBy") String  referredBy, @Header("sessionId") String sessionId,@Query("companyId") String companyId,@Query("applicationType") String applicationType);

    @GET(Constants.country)
    Call<List<CountryResponse>>  getCountryResponse(@Header("referredBy") String  referredBy, @Header("sessionId") String sessionId,@Query("companyId") String companyId,@Query("continentId") String ContinentID,@Query("applicationType") String applicationType);

    @POST(Constants.userFeedback)
    Call<ReportScreenResponse>  ReportScreenCall(@Header("referredBy") String  referredBy, @Header("sessionId") String sessionId,@Body UserFeedbackReq userFeedbackReq);

    //General Setings - POST,PUT,GET
    @POST(Constants.register)
    Call<RegisterResponse>  registerCall(@Header("referredBy") String  referredBy, @Header("sessionId") String sessionId, @Body RegisterReq registerReq);

    @PUT(Constants.register)
    Call<GetStartEditResponse> EditGetStartResponse(@Header("referredBy") String  referredBy, @Header("sessionId") String sessionId, @Body RegisterReq registerReq);

    @PUT(Constants.register)
    Call<ProfileEditResponse> profileEditResponse(@Header("referredBy") String  referredBy, @Header("sessionId") String sessionId, @Body RegisterReq registerReq);

    @PUT(Constants.register)
    Call<SettingEditResponse> EditSettingResponse(@Header("referredBy") String  referredBy, @Header("sessionId") String sessionId, @Body RegisterReq registerReq);

    @GET(Constants.register)
    Call<List<GetStartPersEditResp>> registerGetStartResponse(@Header("referredBy") String referredBy, @Header("sessionId") String sessionId, @Query("companyId") String  companyId, @Query("type") String type, @Query("id") String id, @Query("sortOrder") String sortOrder, @Query("offset") int offset, @Query("limit") int limit);

    @GET(Constants.register)
    Call<List<ProfileEditResponse>> profileGetResponse(@Header("referredBy") String referredBy, @Header("sessionId") String sessionId, @Query("companyId") String  companyId, @Query("type") String type, @Query("id") String id, @Query("sortOrder") String sortOrder, @Query("offset") int offset, @Query("limit") int limit);

    @GET(Constants.register)
    Call<List<SettingGetGenResponse>> settingGetResponse(@Header("referredBy") String referredBy, @Header("sessionId") String sessionId, @Query("companyId") String  companyId, @Query("type") String type, @Query("id") String id, @Query("sortOrder") String sortOrder, @Query("offset") int offset, @Query("limit") int limit);

    //OTP - POST,GET
    @POST(Constants.otpverify)
    Call<OTPVerification>  getOTPVerifyCall(@Header("referredBy") String  referredBy, @Header("sessionId") String sessionId, @Body OTPReq otpReq);

    @PUT(Constants.otpverify)
    Call<OTPVerification>  putOTPVerifyCall(@Header("referredBy") String  referredBy, @Header("sessionId") String sessionId, @Body OTPReq otpReq);

    @GET(Constants.otpverify)
    Call<List<VerifyGetResponse>> verifyGetResponse(@Header("referredBy") String referredBy,@Header("sessionId") String sessionId,@Query("companyId") String  companyId,@Query("id") String ID, @Query("username") String  userName, @Query("verificationCode") String verificationCode, @Query("status") String status, @Query("type") String type,@Query("sortOrder") String sortOrder, @Query("offset") int offset, @Query("limit") int limit);

   //AppSettings - POST,PUT,GET
    @POST(Constants.userPreference)
    Call<UserSettingResponse>  userSettingPostResp(@Header("referredBy") String  referredBy, @Header("sessionId") String sessionId, @Body UserSettingRequest settingRequestReq);

    @PUT(Constants.userPreference)
    Call<UserSettingResponse>  userSettingPUTResp(@Header("referredBy") String  referredBy, @Header("sessionId") String sessionId, @Body UserSettingRequest settingRequestReq);

    @GET(Constants.userPreference)
    Call<List<UserSettingGETResponse>> userSettingGetResponse(@Header("referredBy") String referredBy, @Header("sessionId") String sessionId,@Query("companyId") String  companyId,@Query("userId") String userId, @Query("type") String type, @Query("sortOrder") String sortOrder, @Query("offset") int offset, @Query("limit") int limit);

    //MapSetting
    @POST(Constants.userPreference)
    Call<MapSettingResponse>  userMapSettingPostResp(@Header("referredBy") String  referredBy, @Header("sessionId") String sessionId, @Body UserMapSettingReq settingRequestReq);

   //LIVE_DATA-GET
    @GET(Constants.userDeviceData)
    Call<List<UserDeviceDataModalResp>> userDeviceDataGetResp(@Header("referredBy") String referredBy, @Header("sessionId") String sessionId, @Query("companyId") String  companyId, @Query("moduleId") String  moduleId, @Query("divisionId") String  divisionId, @Query("portionId") String portionId, @Query("userId") String userId, @Query("type") String type,@Query("category") String category, @Query("sortOrder") String sortOrder, @Query("offset") int offset, @Query("limit") int limit);

    @GET(Constants.userdevice)
    Call<List<UserDeviceResponse>> userDeviceResponse(@Header("referredBy") String referredBy, @Header("sessionId") String sessionId, @Query("companyId") String companyId,@Query("userId") String userId,@Query("type") String type, @Query("sortBy") String sortBy, @Query("sortOrder") String sortOrder, @Query("offset") int offset, @Query("limit") int limit);

    //MODULE
    @GET(Constants.module)
    Call<List<GetModuleResponse>> moduleGetResp(@Header("referredBy") String referredBy, @Header("sessionId") String sessionId,@Query("userId") String userId, @Query("companyId") String  companyId,@Query("type") String type);

    //PORTION
    @GET(Constants.portion)
    Call<List<GetPortionResponse>> portionGetResp(@Header("referredBy") String referredBy, @Header("sessionId") String sessionId,@Query("userId") String userId, @Query("companyId") String companyId, @Query("type") String type);

    //GPS Data
    @POST(Constants.userdevice)
    Call<GPSDeviceResp>  GPSDeviceResponse(@Header("referredBy") String  referredBy, @Header("sessionId") String sessionId, @Body GPSDeviceReq gpsDeviceReq);

    @POST(Constants.userDeviceData)
    Call<GPSEditDeviceResp>  GPSPostDeviceDataResponse(@Header("referredBy") String  referredBy, @Header("sessionId") String sessionId, @Body GPSEditDeviceReq gpsEditDeviceReq);

    //Dashboard and MyRides List

    @GET(Constants.userQuery)
    Call<List<GPSGETDeviceResp>>  GETGPSDeviceResponse(@Header("referredBy") String  referredBy, @Header("sessionId") String sessionId, @Query("companyId") String  companyId, @Query("userId") String userId, @Query("type") String type, @Query("sortBy") String sortBy, @Query("sortOrder") String sortOrder, @Query("offset") int offset, @Query("limit") int limit, @Query("queryType") String queryType, @Query("queryFields") String deviceDataCategory);

    //graphList

    @GET(Constants.userQuery)
    Call<List<GPSGetAlertListResp>>  GETgraphList(@Header("referredBy") String  referredBy, @Header("sessionId") String sessionId, @Query("companyId") String  companyId, @Query("userId") String userId,@Query("deviceId") String rideID,@Query("type") String type,@Query("queryType") String queryType, @Query("queryFields") String deviceDataCategory);

    //alertlist & count
    @GET(Constants.userQuery)
    Call<List<GPSGetAlertListResp>>  GETGPSGETAlertResponse(@Header("referredBy") String  referredBy, @Header("sessionId") String sessionId, @Query("companyId") String  companyId, @Query("userId") String userId,@Query("deviceId") String rideID,@Query("type") String type,@Query("queryType") String queryType, @Query("queryFields") String deviceDataCategory);

    @GET(Constants.userQuery)
    Call<List<GPSGetAlertListResp>>  GETAlertCountResponse(@Header("referredBy") String  referredBy, @Header("sessionId") String sessionId, @Query("companyId") String  companyId, @Query("userId") String userId,@Query("deviceId") String rideID,@Query("type") String type,@Query("queryType") String queryType);

    //MULTIPARTFILE
    @GET(Constants.multipartFile)
    Call<ResponseBody> downloadFile(@Header("referredBy") String referredBy, @Header("sessionId") String sessionId, @Query("companyId") String companyId, @Query("userId") String userId, @Query("type") String type);

    //MapUpdate - Module Preference
    @GET(Constants.modulePreference)
    Call<List<MapUpdateResponse>> MapUpdateReq(@Header("referredBy") String referredBy, @Header("sessionId") String sessionId, @Query("companyId") String companyId, @Query("divisionId") String divisionId, @Query("moduleId") String moduleId, @Query("type") String type, @Query("sortBy") String sortBy, @Query("sortOrder") String sortOrder, @Query("offset") String offset, @Query("limit") String limit);

    //LicenseUppdate - User Preference
    @POST(Constants.userPreference)
    Call<UserSettingResponse>  LicenseUpdateReq(@Header("referredBy") String  referredBy, @Header("sessionId") String sessionId, @Body LicenseUpdateReq licenseUpdateReq);

    /*v2-starts*/

    @GET("dashboard")
    Call<List<DriveSummaryPOJO>> getDashboardValues(@Header("referredBy") String referredBy,
                                                    @Header("sessionId") String sessionId,
                                                    @QueryMap Map<String, String> parameters);

    @GET("query")
    Call<List<RideLeaderboardPOJO>> getQuery(@Header("referredBy") String referredBy,
                                             @Header("sessionId") String sessionId,
                                             @QueryMap Map<String, String> parameters);

    @GET("dashboard")
    Call<List<DrivingScorePOJO>> getDashboard(@Header("referredBy") String referredBy,
                                              @Header("sessionId") String sessionId,
                                              @QueryMap Map<String, String> parameters);

    @GET("dashboard")
    Call<List<RiskAlertPOJO>> getRiskAlert(@Header("referredBy") String referredBy,
                                           @Header("sessionId") String sessionId,
                                           @QueryMap Map<String, String> parameters);

    @POST("userFeedback")
    Call<UserFeedbackPOJO> postUserFeedback(@Header("referredBy") String referredBy,
                                            @Header("sessionId") String sessionId,
                                            @Body UserFeedbackPOJO parameters);

    @GET("companyPreference")
    Call<List<FAQPOJO>> getFAQ(@Header("referredBy") String referredBy,
                               @Header("sessionId") String sessionId,
                               @QueryMap Map<String, String> parameters);

    /*v2-ends*/

}