package com.supinfo.and.suptodo.API;

public class ApiUtil {

    public static final String BASE_URL = "http://supinfo.steve-colinet.fr/suptodo/";

    public static UserService getUserService(){
        return RetrofitClient.getClient(BASE_URL).create(UserService.class);
    }
}
