package com.jcortiz.chatconversa.Retrofit;

import retrofit2.converter.gson.GsonConverterFactory;



public class WSClient {
    private static WSClient instance;
    private WebService servicio;
    private retrofit2.Retrofit retrofit;

    private WSClient() {
        retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl("http://chat-conversa.unnamed-chile.com/ws/user/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        servicio = retrofit.create(WebService.class);
    }

    public static WSClient getInstance() {
        if (instance == null) instance = new WSClient();
        return instance;
    }

    public WebService getWebService(){return servicio;}

    public retrofit2.Retrofit getRetrofit() {return retrofit;}

}
