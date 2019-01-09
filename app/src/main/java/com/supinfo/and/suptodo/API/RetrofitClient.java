package com.supinfo.and.suptodo.API;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class RetrofitClient {

    private static Retrofit retrofit = null;

    private static final String BASE_URL = "http://supinfo.steve-colinet.fr/suptodo/";

    static UserService getUserService(){
        return RetrofitClient.getClient().create(UserService.class);
    }

    private static Retrofit getClient(){
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(RetrofitClient.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }


}
