package com.btm.pagodirecto.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.btm.pagodirecto.R;
import com.btm.pagodirecto.activities.baseActivities.BaseActivity;
import com.btm.pagodirecto.activities.baseActivities.BeaconScanner;
import com.btm.pagodirecto.util.BeaconUtil;
import com.btm.pagodirecto.util.Constants;
import com.btm.pagodirecto.util.Util;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends BaseActivity {

    @Bind(R.id.btn_commerces)
    Button btnCommerces;

    @Bind(R.id.btn_pay)
    Button btnPay;

    @Bind(R.id.user_name)
    TextView userName;

    @Bind(R.id.image_profile)
    CircleImageView userImage;

    // Permisos que debe autorizar el usuario
    // TODO: Deben mejorarse, sobre todo el de BT, quizas crear una clase aparte de manejo de permisos
    private final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private final int PERMISSION_REQUEST_ENABLE_BT = 2;
    private final String ERROR_SERVICE_LOG = "Error Service: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        this.setUserAttributes();
        Util.setActivity(this);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Util.getFromSharedPreferences("user_role").equals("customer")) {
            //Util.setThreadPolicy();
            //requestBluetoothPermission();
            //requestLocationAccessPermission();
            //BeaconUtil.setRegisteredBeacons();
            //BeaconUtil.getRegisteredBeacons();              // Download list of registered listPromotion

        }
    }

    @OnClick(R.id.btn_pay)
    public void goToPay(){
        if (Util.getFromSharedPreferences(Constants.TAG_USER_ROLE).toLowerCase().equals(Constants.ROLE_COMMERCE)) {
            Util.goToActivitySlide(
                    this,
                    SelectUserActivity_.class);
        }else{
            Util.goToActivitySlide(
                    this,
                    PayUsers.class);
        }
    }

    @OnClick(R.id.btn_commerces)
    public void goToCommerce(){
        Util.goToActivitySlide(
                this,
                CommerceActivity.class);
    }

    public void setUserAttributes(){
        userName.setText(Util.getFromSharedPreferences(Constants.TAG_USER_NAME));

        GlideUrl glideUrl = new GlideUrl(Util.getFromSharedPreferences(Constants.TAG_USER_URL_IMAGE), new LazyHeaders.Builder()
                .build());

        Glide.with(Util.getContext()).load(glideUrl).into(userImage);

        if (Util.getFromSharedPreferences(Constants.TAG_USER_ROLE).equals(Constants.ROLE_COMMERCE)){
            btnCommerces.setVisibility(View.GONE);
            btnPay.setText("Vender");
        }
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

}
