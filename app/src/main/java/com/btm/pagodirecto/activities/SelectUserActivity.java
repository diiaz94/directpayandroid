package com.btm.pagodirecto.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
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

import com.btm.pagodirecto.adapters.UsersRecyclerViewAdapter;
import com.btm.pagodirecto.custom.SocketHandle;
import com.btm.pagodirecto.domain.beacons.RegisteredBeacons;
import com.btm.pagodirecto.dto.User;
import com.btm.pagodirecto.responses.ResponseUsers;
import com.btm.pagodirecto.services.ApiService;
import com.btm.pagodirecto.services.ServiceGenerator;
import com.btm.pagodirecto.util.BeaconUtil;
import com.btm.pagodirecto.util.Constants;
import com.btm.pagodirecto.util.Util;
import com.google.gson.Gson;

import org.altbeacon.beacon.Beacon;
import org.androidannotations.annotations.EActivity;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private ArrayList<User> users = new ArrayList<User>();
    public Boolean enterRegion = false;

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public  void onReceive(Context context, final Intent intent) {
            // Get extra data included in the Intent

            String action = intent.getAction();
            String usr = intent.getStringExtra("user");
            Gson gson = new Gson();
            User user = gson.fromJson(usr, User.class);

            if(action.equalsIgnoreCase(Constants.ADD_USER)){
                //ADD USER IN LIST
                users.add(users.size(),user);
                recyclerView.setAdapter(new UsersRecyclerViewAdapter(getApplicationContext(),users));

            }

            if(action.equalsIgnoreCase(Constants.REMOVE_USER)){
                //REMOVE USER IN LIST
                users.remove(this.getIndexFromUser(user));
                recyclerView.setAdapter(new UsersRecyclerViewAdapter(getApplicationContext(),users));
            }

        }

        private int getIndexFromUser(User user) {

            return 0;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user);
        Util.setActivity(this);
        ButterKnife.bind(this);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));

        if (Util.getFromSharedPreferences("user_role").equals("customer")){loadUsers();}

        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.ADD_USER);
        filter.addAction(Constants.REMOVE_USER);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                filter);

        hideSoftKeyboard();


    }

    @Override
    protected void onResume(){
        super.onResume();

        if (Util.getFromSharedPreferences("user_role").equals("customer")) {
            Util.setThreadPolicy();
            requestBluetoothPermission();
            requestLocationAccessPermission();
            BeaconUtil.setRegisteredBeacons();
            BeaconUtil.getRegisteredBeacons();              // Download list of registered listPromotion

            JSONObject json = new JSONObject();
            try {
                json.put("beacon", "1");
                json.put("user", Util.getFromSharedPreferences("user_id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            this.enterRegion = true;
            SocketHandle.emitEvent("enter region", json);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        JSONObject json = new JSONObject();
        try {
            json.put("beacon","1");
            json.put("user",Util.getFromSharedPreferences("user_id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.enterRegion = false;
        SocketHandle.emitEvent("exit region", json);
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

        Map<String,String> map = new HashMap<>();
        map.put("role", Util.getFromSharedPreferences("user_role"));
        map.put("user_id", Util.getFromSharedPreferences("user_id"));

        ServiceGenerator.getService(ApiService.class)
                .users(map)
                .enqueue(new CustomRetrofitCallback<CustomResponse<ResponseUsers>>() {

                    @Override
                    public void handleSuccess(Object response) {
                        ResponseUsers responseUsers = (ResponseUsers) response;
                        users = responseUsers.getUsers();

                       recyclerView.setAdapter(new UsersRecyclerViewAdapter(getApplicationContext(),users));
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

    @Override
    public void onBackPressed() {
        // code here to show dialog
        Util.goToActivitySlideBack(
                Util.getActivity(),
                HomeActivity.class,
                Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK
        );
    }

}
