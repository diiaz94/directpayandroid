package com.btm.pagodirecto;

import android.app.Application;

import com.btm.pagodirecto.custom.SocketHandle;

/**
 * Created by Pedro on 31/10/2017.
 */

public class PagoDirecto extends Application {
    private static final String TAG = "PagoDirecto";

    @Override
    public void onCreate(){
        super.onCreate();
        SocketHandle.init(this);
    }
}
