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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.btm.pagodirecto.R;
import com.btm.pagodirecto.activities.baseActivities.BaseActivity;
import com.btm.pagodirecto.activities.baseActivities.BeaconScanner;
import com.btm.pagodirecto.custom.CustomResponse;
import com.btm.pagodirecto.custom.CustomRetrofitCallback;
import com.btm.pagodirecto.adapters.GridRecyclerViewAdapter;
import com.btm.pagodirecto.domain.beacons.RegisteredBeacons;
import com.btm.pagodirecto.dto.User;
import com.btm.pagodirecto.responses.ResponseUsers;
import com.btm.pagodirecto.services.ApiService;
import com.btm.pagodirecto.services.ServiceGenerator;
import com.btm.pagodirecto.util.BeaconUtil;
import com.btm.pagodirecto.util.Util;

import org.altbeacon.beacon.Beacon;
import org.androidannotations.annotations.EActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

@EActivity
public class SelectUserActivity extends BeaconScanner {

    @Bind(R.id.grid)
    RecyclerView recyclerView;

    // Permisos que debe autorizar el usuario
    // TODO: Deben mejorarse, sobre todo el de BT, quizas crear una clase aparte de manejo de permisos
    private final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private final int PERMISSION_REQUEST_ENABLE_BT = 2;
    private final String ERROR_SERVICE_LOG = "Error Service: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user);
        Util.setActivity(this);
        ButterKnife.bind(this);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        loadUsers();
        hideSoftKeyboard();
    }

    @Override
    protected void onResume(){
        super.onResume();

        Util.setThreadPolicy();
        requestBluetoothPermission();
        requestLocationAccessPermission();
        BeaconUtil.setRegisteredBeacons();
        BeaconUtil.getRegisteredBeacons();                 // Download list of registered listPromotion

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


    private void loadUsers() {
      ServiceGenerator.getService(ApiService.class)
                .users()
                .enqueue(new CustomRetrofitCallback<CustomResponse<ResponseUsers>>() {

                    @Override
                    public void handleSuccess(Object response) {
                        ResponseUsers responseUsers = (ResponseUsers) response;
                        ArrayList<User> users = responseUsers.getUsers();

                       recyclerView.setAdapter(new GridRecyclerViewAdapter(getApplicationContext(),users));
                    }

                    @Override
                    public void handleResponseError(Response response) {

                    }

                    @Override
                    public void handleFailError(Call<CustomResponse<ResponseUsers>> call, Throwable t) {

                    }
                });
    }

    /**
     * Hides the soft keyboard
     */
    public void hideSoftKeyboard() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * Shows the soft keyboard
     */
    public void showSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        view.requestFocus();
        inputMethodManager.showSoftInput(view, 0);
    }

}
