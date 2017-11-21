package com.btm.pagodirecto.activities.baseActivities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.btm.pagodirecto.R;
import com.btm.pagodirecto.util.BeaconUtil;
import com.btm.pagodirecto.util.Constants;
import com.btm.pagodirecto.util.Util;

/**
 * Created by Edwin alvarado on 9/20/17.
 * Copyright © 2017 Edwin Alvarado. All rights reserved.
 */

public class BaseActivity extends AppCompatActivity {

    protected ProgressDialog pd = null;

    // Permisos que debe autorizar el usuario
    // TODO: Deben mejorarse, sobre todo el de BT, quizas crear una clase aparte de manejo de permisos
    private final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private final int PERMISSION_REQUEST_ENABLE_BT = 2;
    private final String ERROR_SERVICE_LOG = "Error Service: ";

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

    @Override
    protected void onResume() {
        super.onResume();

       /* if (Util.getFromSharedPreferences(Constants.TAG_USER_ROLE).equals("customer")) {
            Util.setThreadPolicy();
            requestBluetoothPermission();
            requestLocationAccessPermission();
            BeaconUtil.setRegisteredBeacons();
            BeaconUtil.getRegisteredBeacons();              // Download list of registered listPromotion

        }*/
    }

    //Buetooth permission and activate
    private void requestBluetoothPermission(){
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter!=null&&!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, PERMISSION_REQUEST_ENABLE_BT);
        }
    }

    //TODO: Manage all permissions in a separated class
    // AltBeacon Library needs to request Location Access Permission
    @TargetApi(Build.VERSION_CODES.M)
    private void requestLocationAccessPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);builder.setTitle("This app needs location access");
                builder.setMessage("Please grant location access so this app can detect listPromotions.");
                builder.setPositiveButton(android.R.string.ok, null);


                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog){
                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);}});
                builder.show();
            }
        }
    }

    /**
     * Overrides the pending Activity transition by performing the "Enter" animation.
     */
    protected void overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    /**
     * Overrides the pending Activity transition by performing the "Exit" animation.
     */
    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }






}
