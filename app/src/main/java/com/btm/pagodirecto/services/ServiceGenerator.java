package com.btm.pagodirecto.services;


import com.btm.pagodirecto.interceptor.HttpInterceptor;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 *
 * The ServiceGenerator Class allows programmer to manage APIRest
 * communication in a comfortable way. Each new API needs a new
 * ServiceGenerator, but the same generator can manage multiple uses
 * of the same API. Example: This generator manage the GSG API, but
 * inside it you can manage API for beacons or API for account management.
 * It is based on RETROFIT Library and it work with HTTP interceptors and
 * Callbacks. It was designed to generate only one instace per API category
 * (For each category the method create is invoked once.
 *
 * TODO: IS IT THREAD-SAFE?
 * /**
 * Created by Edwin alvarado on 9/20/17.
 * Copyright © 2017 Edwin Alvarado. All rights reserved.
 */

public class ServiceGenerator {

    //Test
    private static final String API_BASE_URL = "https://pagodirectoapi.herokuapp.com";

    // Interceptor for logging
    private  static final HttpLoggingInterceptor logginInterceptor = new HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY);

    // Build HTTP connection and indicate the interceptor
    private static final OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
            .addInterceptor(new HttpInterceptor())
            .addInterceptor(logginInterceptor);

    private static final Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());

    private static final Retrofit retrofit = builder
            .client(httpClient.build())
            .build();

    private static final AuthenticateService ACCOUNT_SERVICE =  retrofit.create(AuthenticateService.class);
    private static final ApiService API_SERVICE =  retrofit.create(ApiService.class);

    public ServiceGenerator(){}

    //All services need to be declare here
    public static <S> S getService(Class<S> serviceClass) {

        if(serviceClass == AuthenticateService.class){
            return (S) ACCOUNT_SERVICE;
        }

        if(serviceClass == ApiService.class){
            return (S) API_SERVICE;
        }

        return null;
    }

}
