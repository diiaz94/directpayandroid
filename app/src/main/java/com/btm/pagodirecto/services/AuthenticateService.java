package com.btm.pagodirecto.services;


import com.btm.pagodirecto.domain.account.Authenticate;
import com.btm.pagodirecto.domain.account.AuthenticateParameters;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Edwin alvarado on 9/20/17.
 * Copyright © 2017 Edwin Alvarado. All rights reserved.
 */

public interface AuthenticateService {
    String BASE_URL = "/v1/";

    @Headers({"User-Agent: itertest/0.0.1", "Accept: application/json", "Content-Type: application/x-www-form-urlencoded"})
    @POST(BASE_URL+"user/account/signin")
    public Call<Authenticate> signIn(@Body AuthenticateParameters param);

    /*@Headers("Accept: application/json")
    @GET("/api/account")
    public Call<UserProfile> getUserProfile();

    @Headers("Content-Type: application/json")
    @POST(BASE_URL+"register")
    public Call<ResponseBody> register(@Body RegisterParameters param);

    @Headers({"Content-Type: text/plain","Accept: text/plain"})
    @POST(BASE_URL+"account/reset_password/init")
    public Call<ResponseBody> resetPassword(@Body RequestBody email);*/
}
