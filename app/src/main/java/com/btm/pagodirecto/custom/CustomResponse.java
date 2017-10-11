package com.btm.pagodirecto.custom;

import com.btm.pagodirecto.responses.ErrorResponse;

/**
 * Created by Pedro on 4/10/2017.
 */
public class CustomResponse<T>  {
    boolean success;
    T response;
    ErrorResponse error;

    public boolean isSuccessfull(){
        return success;
    }

    public T getResponse() {
        return response;
    }
}
