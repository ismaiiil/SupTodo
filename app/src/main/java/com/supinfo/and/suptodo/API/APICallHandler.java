package com.supinfo.and.suptodo.API;

public class APICallHandler {
    public static APICallHandler INSTANCE;

    public APICallHandler getINSTANCE(){
        if (INSTANCE == null) {
            INSTANCE = new APICallHandler();
        }
        return INSTANCE;
    }


}
