package com.montbleu.Retrofit;

import android.media.Image;

import com.montbleu.Utils.Constants;
import com.montbleu.model.PutFileData;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface FileDownloadClient
{
    @GET(Constants.multipartFile)
    Call<ResponseBody> downloadFile(@Header("referredBy") String referredBy, @Header("sessionId") String sessionId, @Query("companyId") String companyId, @Query("userId") String userId, @Query("type") String type);

    @Multipart
    @PUT(Constants.file)
    Call<ResponseBody> GetImageFile(@Header("referredBy") String referredBy, @Header("sessionId") String sessionId, @Query("companyId") String companyId, @Query("type") String type, @Query("userId") String userId,@Part MultipartBody.Part file);
    //ANDROID, 62f1f43e00c4f246396a0cbf, comp00000000000000000001, 622aea0200c4f20bae0f29d3, USER_PROFILE_PICTURE, okhttp3.MultipartBody$Part@2e1e39e

}
