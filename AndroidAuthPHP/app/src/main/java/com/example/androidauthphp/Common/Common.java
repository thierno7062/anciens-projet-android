package com.example.androidauthphp.Common;

import com.example.androidauthphp.Remote.IMyAPI;
import com.example.androidauthphp.Remote.RetrofitClient;

import retrofit2.Retrofit;

public class Common {

    public static final String BASE_URL ="http://192.168.1.4/myapi/";

    public static IMyAPI getAPI()
    {
        return RetrofitClient.getClient(BASE_URL).create(IMyAPI.class);
    }
}
