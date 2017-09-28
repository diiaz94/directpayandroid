package com.berlinendeavours.iter.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;

import com.berlinendeavours.iter.R;
import com.berlinendeavours.iter.activities.baseActivities.BaseActivity;
import com.berlinendeavours.iter.util.Util;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by Edwin alvarado on 9/20/17.
 * Copyright © 2017 Edwin Alvarado. All rights reserved.
 */

public class SplashActivity extends BaseActivity {

    private final int SPLASH_DISPLAY_LENGTH = 2000;
    private Activity activity;

    private final int interval = 5000; // 2 Second
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Util.setActivity(this);

        handler.postAtTime(runnable, System.currentTimeMillis()+interval);
        handler.postDelayed(runnable, interval);
    }

    /*private void checkSession() {
        // Validate Token (Authenticate GET call in AccountService API)
        ServiceGenerator.getService(AccountService.class)
            .authenticateToken()
            .enqueue(new AuthenticateTokenCallback() {
                @Override
                public void handleSuccess(Response response) {
                    String username = "";
                    ResponseBody user = (ResponseBody) response.body();
                    try {
                        username = user.string();
                    } catch (IOException e) {
                    }
                    // Username not empty indicates a valid token returned by authenticateToken()
                    if(!username.isEmpty() && username.equals(Util.getUser())) {
                        Util.goToActivity(
                                Util.getActivity(),
                                MainActivity.class,
                                Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK
                        );
                    }
                    else{
                        Util.showMessage("Not logged in");
                        Util.goToActivity(
                                Util.getActivity(),
                                .class,
                                Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK
                        );
                    }
                }
                @Override
                public void handleError(Response response) {
                }

            }); // Obtiene la lista de beacons registrados y manejo de callbacks);
    }*/

    private Runnable runnable = new Runnable(){
        public void run() {
            //Open activity with the app state Home - Wizzard - Start
            Util.goToActivityFade(
                    Util.getActivity(),
                    MainActivity.class,
                    Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK
            );
        }
    };
}
