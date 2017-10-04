package com.btm.pagodirecto.activities.baseActivities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Edwin alvarado on 9/20/17.
 * Copyright © 2017 Edwin Alvarado. All rights reserved.
 */

public class BaseActivity extends AppCompatActivity {

    protected ProgressDialog pd = null;

    public ProgressDialog getPd() {
        return pd;
    }
    public void setPd(ProgressDialog pd) {
        this.pd = pd;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pd = new ProgressDialog(this);


    }





}
