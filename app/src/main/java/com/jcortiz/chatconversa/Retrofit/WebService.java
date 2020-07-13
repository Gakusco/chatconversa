package com.jcortiz.chatconversa.Retrofit;

import com.jcortiz.chatconversa.Retrofit.respuestasWS.MensajeWS;
import com.jcortiz.chatconversa.Retrofit.respuestasWS.OkRequestWS;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface WebService {

    @FormUrlEncoded
    @POST("user/create")
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
    @POST("user/login")
    Call<OkRequestWS> login(
            @Field("username") String nombre,
            @Field("password") String password,
            @Field("device_id") String deviceId
    );

    @FormUrlEncoded
    @POST("user/logout")
    Call<OkRequestWS> logout(
            @Header("Authorization") String token,
            @Field("user_id") String userId,
            @Field("username") String username
    );

    @Multipart
    @POST("user/load/image")
    Call<OkRequestWS> cargarImagenDeUsario(
            @Header("Authorization") String token,
            @Part MultipartBody.Part file,
            @Part("user_id") RequestBody userId,
            @Part("username") RequestBody username
    );

    @FormUrlEncoded
    @POST("message/get")
    Call<MensajeWS> obtenerMensajes(
            @Header("Authorization") String token,
            @Field("user_id") String userId,
            @Field("username") String username
    );
}
