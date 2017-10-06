package com.btm.pagodirecto.activities.custom;

import android.util.Log;

import com.btm.pagodirecto.util.Util;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Pedro on 4/10/2017.
 */

public abstract class CustomRetrofitCallback<T> implements Callback<T> {

    private static final String TAG = "CustomRetrofitCallback" ;
    public CustomRetrofitCallback() {

    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        Log.i(TAG,"Response message::"+response.message());
        if (response.isSuccessful()){
            CustomResponse  custom =   (CustomResponse) response.body();

            if(custom.isSuccessfull()) {
                handleSuccess(custom.getResponse());
                return;
            }
            handleResponseError(response);
            Log.v(TAG, "Response not successfully::" + response.errorBody());
        }else{
            Log.v(TAG, "Response not successfully::" + response.errorBody());
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        Util.showMessage("Request Failed, check your internet connection");
        Util.hideDialogMessage();
        handleFailError(call,t);
    }


    public abstract void handleSuccess(Object response);
    public abstract void handleResponseError(Response response);
    public abstract void handleFailError(Call<T> call, Throwable t);



}
