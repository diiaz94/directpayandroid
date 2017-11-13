package com.btm.pagodirecto.services;

import com.btm.pagodirecto.custom.CustomResponse;
import com.btm.pagodirecto.responses.ResponseProducts;
import com.btm.pagodirecto.responses.ResponseReceipts;
import com.btm.pagodirecto.responses.ResponseUsers;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.QueryMap;

/**
 * Created by Pedro on 3/10/2017.
 */

public interface ApiService {

    String BASE_URL = "/v1";

    @GET(BASE_URL+"/users")
    public Call<CustomResponse<ResponseUsers>> users(@QueryMap Map<String,String> params);

    @GET(BASE_URL+"/products")
    public Call<CustomResponse<ResponseProducts>> products();

    @GET(BASE_URL+"/receipts")
    public Call<CustomResponse<ResponseReceipts>> receipts(@QueryMap Map<String,String> params);
}
