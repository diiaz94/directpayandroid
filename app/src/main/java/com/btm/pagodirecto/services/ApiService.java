package com.btm.pagodirecto.services;

import com.btm.pagodirecto.dto.User;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;

/**
 * Created by Pedro on 3/10/2017.
 */

public interface ApiService {

    String BASE_URL = "/v1/";

    @Headers({"User-Agent: itertest/0.0.1", "Accept: application/json", "Content-Type: application/x-www-form-urlencoded"})
    @GET(BASE_URL+"users")
    public Call<ArrayList<User>> users();
}
