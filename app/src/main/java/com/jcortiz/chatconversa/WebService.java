package com.jcortiz.chatconversa;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface WebService {

    @FormUrlEncoded
    @POST("create")
    Call<RespuestaRegistroWS> register(
            @Field("name") String nombre,
            @Field("lastname") String apellido,
            @Field("run") String run,
            @Field("username") String username,
            @Field("email") String email,
            @Field("password") String password,
            @Field("token_enterprise") String tokenEnterprise
    );
}
