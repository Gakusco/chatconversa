package com.jcortiz.chatconversa;

import com.jcortiz.chatconversa.respuestasWS.OkRequestWS;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface WebService {

    @FormUrlEncoded
    @POST("create")
    Call<OkRequestWS> register(
            @Field("name") String nombre,
            @Field("lastname") String apellido,
            @Field("run") String run,
            @Field("username") String username,
            @Field("email") String email,
            @Field("password") String password,
            @Field("token_enterprise") String tokenEnterprise
    );

    @FormUrlEncoded
    @POST("login")
    Call<OkRequestWS> login(
            @Field("username") String nombre,
            @Field("password") String password,
            @Field("device_id") String deviceId
    );

    @FormUrlEncoded
    @POST("logout")
    Call<OkRequestWS> logout(
            @Header("Authorization") String token,
            @Field("user_id") String userId,
            @Field("username") String username
    );

    @Multipart
    @POST("load/image")
    Call<OkRequestWS> cargarImagenDeUsario(
            @Header("Authorization") String token,
            @Part MultipartBody.Part file,
            //@Part("user_image") RequestBody nombre,
            @Part("user_id") RequestBody userId,
            @Part("username") RequestBody username
    );

}
