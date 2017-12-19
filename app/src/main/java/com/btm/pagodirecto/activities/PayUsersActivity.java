package com.btm.pagodirecto.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.btm.pagodirecto.R;
import com.btm.pagodirecto.activities.baseActivities.BaseActivity;
import com.btm.pagodirecto.activities.baseActivities.BeaconScanner;
import com.btm.pagodirecto.adapters.ReceiptsRecyclerViewAdapter;
import com.btm.pagodirecto.adapters.UsersRecyclerViewAdapter;
import com.btm.pagodirecto.custom.CustomResponse;
import com.btm.pagodirecto.custom.CustomRetrofitCallback;
import com.btm.pagodirecto.dto.Receipt;
import com.btm.pagodirecto.dto.User;
import com.btm.pagodirecto.responses.ResponseReceipts;
import com.btm.pagodirecto.responses.ResponseUsers;
import com.btm.pagodirecto.services.ApiService;
import com.btm.pagodirecto.services.ServiceGenerator;
import com.btm.pagodirecto.util.Constants;
import com.btm.pagodirecto.util.Util;
import com.google.gson.Gson;

import org.androidannotations.annotations.EActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import retrofit2.Call;
import retrofit2.Response;

@EActivity
public class PayUsersActivity extends BeaconScanner {

    @Bind(R.id.btn_back)
    Button btnBack;

    @Bind(R.id.users_grid)
    RecyclerView usersGrid;

    @Bind(R.id.pending_list)
    RecyclerView pendingList;

    @Bind(R.id.users_tab)
    LinearLayout usersTab;

    @Bind(R.id.pending_tab)
    LinearLayout pendingTab;

    @Bind(R.id.pending_items_count_container)
    LinearLayout pendingItemsCountContainer;

    @Bind(R.id.pending_items_count)
    TextView pendingItemsCount;

    private ArrayList<User> mUsers = new ArrayList<User>();
    private ArrayList<Receipt> mPendings = new ArrayList<Receipt>();

    private static ArrayList<String> USERS = new ArrayList<String>();
    private static ArrayList<String> PENDINGS = new ArrayList<String>();

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public  void onReceive(Context context, final Intent intent) {
            // Get extra data included in the Intent

            String action = intent.getAction();

            if (action.equalsIgnoreCase(Constants.NEW_RECEIPT)){
                //Update list
                String userId = intent.getStringExtra("user");
                loadPendings();
            }

        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_users);
        ButterKnife.bind(this);
        Util.setActivity(this);

        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.NEW_RECEIPT);

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                filter);

        usersGrid.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        pendingList.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));

        loadUsers();
        loadPendings();
        hideSoftKeyboard();

    }


    @Override
    protected void onResume() {
        super.onResume();
        Util.setActivity(this);
    }

    private void loadUsers() {

        Map<String,String> map = new HashMap<>();
        map.put("role", Util.getFromSharedPreferences("user_role"));
        map.put("user_id", Util.getFromSharedPreferences("user_id"));
        final BaseActivity act = this;
        ServiceGenerator.getService(ApiService.class)
                .users(map)
                .enqueue(new CustomRetrofitCallback<CustomResponse<ResponseUsers>>() {

                    @Override
                    public void handleSuccess(Object response) {
                        ResponseUsers responseUsers = (ResponseUsers) response;
                        mUsers = responseUsers.getUsers();
                        fillUsersList();

                        usersGrid.setAdapter(new UsersRecyclerViewAdapter(getApplicationContext(),mUsers,new UsersRecyclerViewAdapter.OnItemClickListener() {
                            @Override public synchronized void onItemClick(int i,int type) {
                                switch (type){
                                    case 0:
                                        Gson g = new Gson();
                                        Intent intent = new Intent(act,PayActivity.class);
                                        intent.putExtra(Constants.TAG_USER_OBJECT,g.toJson(mUsers.get(i)));
                                        act.startActivity(intent);
                                        act.overridePendingTransition(R.anim.slide_from_right,R.anim.slide_to_left);
                                        break;
                                }
                            }
                        }));
                    }

                    @Override
                    public void handleResponseError(Response response) {

                    }

                    @Override
                    public void handleFailError(Call<CustomResponse<ResponseUsers>> call, Throwable t) {

                    }
                });
    }

    private void loadPendings() {
        Map<String,String> map = new HashMap<>();
        map.put("user_id", Util.getFromSharedPreferences("user_id"));
        final BaseActivity act = this;
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
                                switch (type){
                                    case 0:
                                        Gson g = new Gson();
                                        Intent intent = new Intent(act,PayResumeActivity.class);
                                        intent.putExtra(Constants.TAG_RECEIPT_OBJECT,g.toJson(mPendings.get(i)));
                                        act.startActivity(intent);
                                        act.overridePendingTransition(R.anim.slide_from_right,R.anim.slide_to_left);
                                        break;
                                }
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

    private void fillUsersList() {
        USERS.clear();
        for(int i = 0; i < mUsers.size(); i++){
            USERS.add(i,mUsers.get(i).getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, USERS);
        AutoCompleteTextView textView = (AutoCompleteTextView)
                findViewById(R.id.users_list);
        textView.setAdapter(adapter);

    }

    private void fillPendingList() {
        USERS.clear();
        for(int i = 0; i < mPendings.size(); i++){
            PENDINGS.add(i,mPendings.get(i).getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, PENDINGS);
        AutoCompleteTextView textView = (AutoCompleteTextView)
                findViewById(R.id.users_list);
        textView.setAdapter(adapter);

    }

    @OnClick(R.id.users_tab)
    public void showUserGrid() {
        final int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            usersTab.setBackgroundDrawable( getResources().getDrawable(R.drawable.tabs_select_state) );
            pendingTab.setBackgroundDrawable(null);
        } else {
            usersTab.setBackground( getResources().getDrawable(R.drawable.tabs_select_state));
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
            usersTab.setBackgroundDrawable(null);
        } else {
            pendingTab.setBackground( getResources().getDrawable(R.drawable.tabs_select_state));
            usersTab.setBackground(null);
        }
        usersGrid.setVisibility(View.GONE);
        pendingList.setVisibility(View.VISIBLE);
        fillPendingList();
    }

    public void hideSoftKeyboard() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @OnTextChanged(R.id.users_list)
    public void onTextChangeUserList(){

    }

    @OnClick(R.id.btn_back)
    public void goToBack(){
        this.finish();
    }
}
