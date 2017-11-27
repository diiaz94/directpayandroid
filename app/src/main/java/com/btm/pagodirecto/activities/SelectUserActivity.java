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
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.btm.pagodirecto.R;
import com.btm.pagodirecto.activities.baseActivities.BaseActivity;
import com.btm.pagodirecto.activities.baseActivities.BeaconScanner;
import com.btm.pagodirecto.adapters.ReceiptsRecyclerViewAdapter;
import com.btm.pagodirecto.custom.CustomResponse;
import com.btm.pagodirecto.custom.CustomRetrofitCallback;

import com.btm.pagodirecto.adapters.UsersRecyclerViewAdapter;
import com.btm.pagodirecto.custom.SocketHandle;
import com.btm.pagodirecto.domain.beacons.RegisteredBeacons;
import com.btm.pagodirecto.dto.Receipt;
import com.btm.pagodirecto.dto.User;
import com.btm.pagodirecto.responses.ResponseReceipts;
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
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

@EActivity
public class SelectUserActivity extends BeaconScanner {

    @Bind(R.id.users_grid)
    RecyclerView usersGrid;

    @Bind(R.id.pending_list)
    RecyclerView pendingList;

    @Bind(R.id.sell_tab)
    LinearLayout sellTab;

    @Bind(R.id.pending_tab)
    LinearLayout pendingTab;

    @Bind(R.id.btn_back)
    Button btnBack;

    @Bind(R.id.pending_items_count_container)
    LinearLayout pendingItemsCountContainer;

    @Bind(R.id.pending_items_count)
    TextView pendingItemsCount;
    // Permisos que debe autorizar el usuario
    // TODO: Deben mejorarse, sobre todo el de BT, quizas crear una clase aparte de manejo de permisos
    private final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private final int PERMISSION_REQUEST_ENABLE_BT = 2;
    private final String ERROR_SERVICE_LOG = "Error Service: ";
    private ArrayList<User> users = new ArrayList<User>();
    private ArrayList<Receipt> mPendings = new ArrayList<Receipt>();
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
                if (!ifExists(user)){
                    users.add(users.size()-(users.size()>0?1:0),user);
                    usersGrid.setAdapter(new UsersRecyclerViewAdapter(getApplicationContext(),users,new UsersRecyclerViewAdapter.OnItemClickListener() {
                        @Override public synchronized void onItemClick(int i,int type) {
                            Log.d("BEACON_FLAG", "onItemClick:: ");

                            Gson g = new Gson();
                            Util.saveInSharedPreferences("USER_SELL_NAME",mPendings.get(i).getName());
                            Intent intent = new Intent(Util.getActivity(), SellActivity.class);
                            intent.putExtra(Constants.TAG_USER_OBJECT,g.toJson(mPendings.get(i)));
                            intent.putExtra("CART_MODE","SELL");
                            Util.getActivity().startActivity(intent);
                            Util.getActivity().overridePendingTransition(R.anim.slide_from_right,R.anim.slide_to_left);

                            /*Util.goToActivitySlide(
                                    Util.getActivity(),
                                    SellActivity.class);
                            */
                        }
                    }));
                }
            }

            if (action.equalsIgnoreCase(Constants.REMOVE_USER)){
                //REMOVE USER IN LIST
                if (users.size() > 0) {
                    users.remove(this.getIndexFromUser(user));
                    usersGrid.setAdapter(new UsersRecyclerViewAdapter(getApplicationContext(),users,new UsersRecyclerViewAdapter.OnItemClickListener() {
                        @Override public synchronized void onItemClick(int i,int type) {

                        }
                    }));
                }
            }

        }

        private int getIndexFromUser(User user) {
            for (int i = 0; i < users.size(); i++){
                if (users.get(i).getId().equals(user.getId())){
                    return i;
                }
            }
            return 0;
        }

        private boolean ifExists(User user){
            for (int i = 0; i < users.size(); i++){
                if (users.get(i).getId().equals(user.getId())){
                    return true;
                }
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user);
        ButterKnife.bind(this);

        usersGrid.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        pendingList.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));

        JSONObject json = new JSONObject();
        try {
            json.put("beacon", "1");
            json.put("user", Util.getFromSharedPreferences("user_id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        SocketHandle.emitEvent("enter region", json);

        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.ADD_USER);
        filter.addAction(Constants.REMOVE_USER);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                filter);

        loadPendingUsers();
        hideSoftKeyboard();

    }

    @Override
    protected void onResume(){
        super.onResume();
        Util.setActivity(this);

        /*if (Util.getFromSharedPreferences("user_role").equals("customer")) {
            Util.setThreadPolicy();
            requestBluetoothPermission();
            requestLocationAccessPermission();
            BeaconUtil.setRegisteredBeacons();
            BeaconUtil.getRegisteredBeacons();              // Download list of registered listPromotion

        }*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

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
    private void loadPendingUsers() {

        Map<String,String> map = new HashMap<>();
        map.put("user_id", Util.getFromSharedPreferences(Constants.TAG_USER_ID));

        ServiceGenerator.getService(ApiService.class)
                .receipts(map)
                .enqueue(new CustomRetrofitCallback<CustomResponse<ResponseReceipts>>() {

                    @Override
                    public void handleSuccess(Object response) {
                        ResponseReceipts responseReceipts = (ResponseReceipts) response;
                        mPendings = responseReceipts.getReceipts();
                        pendingItemsCount.setText(String.valueOf(mPendings.size()));
                        pendingItemsCountContainer.setVisibility(View.VISIBLE);

                        int viewType = Util.getFromSharedPreferences(Constants.TAG_USER_ROLE).equalsIgnoreCase("customer")? 0:1;
                        pendingList.setAdapter(new ReceiptsRecyclerViewAdapter(getApplicationContext(),mPendings,viewType,new ReceiptsRecyclerViewAdapter.OnItemClickListener() {
                           @Override public synchronized void onItemClick(int i,int type) {
                               Log.d("FLAG", "onItemClick: "+i+" type: "+type);

                               Gson g = new Gson();
                               Util.saveInSharedPreferences("USER_SELL_NAME",mPendings.get(i).getName());
                               Intent intent = new Intent(Util.getActivity(), SellActivity.class);
                               mPendings.get(i).set_id(mPendings.get(i).get_user_id());
                               intent.putExtra(Constants.TAG_USER_OBJECT,g.toJson(mPendings.get(i)));
                               intent.putExtra("CART_MODE","PENDING");
                               Util.getActivity().startActivity(intent);
                               Util.getActivity().overridePendingTransition(R.anim.slide_from_right,R.anim.slide_to_left);

                               /*Util.goToActivitySlide(
                                       Util.getActivity(),
                                       SellActivity.class);
                               */
                           }
                       }));
                    }

                    @Override
                    public void handleResponseError(Response response) {

                    }

                    @Override
                    public void handleFailError(Call<CustomResponse<ResponseReceipts>> call, Throwable t) {

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
        this.finish();
    }

    @OnClick(R.id.sell_tab)
    public void showUserGrid(){
        final int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            sellTab.setBackgroundDrawable( getResources().getDrawable(R.drawable.tabs_select_state) );
            pendingTab.setBackgroundDrawable(null);
        } else {
            sellTab.setBackground( getResources().getDrawable(R.drawable.tabs_select_state));
            pendingTab.setBackground(null);
        }
        pendingList.setVisibility(View.GONE);
        usersGrid.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.pending_tab)
    public void showPendingList(){
        final int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            pendingTab.setBackgroundDrawable( getResources().getDrawable(R.drawable.tabs_select_state) );
            sellTab.setBackgroundDrawable(null);
        } else {
            pendingTab.setBackground( getResources().getDrawable(R.drawable.tabs_select_state));
            sellTab.setBackground(null);
        }
        usersGrid.setVisibility(View.GONE);
        pendingList.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.btn_back)
    public void actionBack(){
        this.finish();
    }

}
