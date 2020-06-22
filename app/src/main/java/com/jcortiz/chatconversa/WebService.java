package com.jcortiz.chatconversa;

import com.jcortiz.chatconversa.respuestasWS.OkRequestWS;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

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
}
