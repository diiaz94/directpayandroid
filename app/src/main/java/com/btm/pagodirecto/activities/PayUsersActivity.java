package com.btm.pagodirecto.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.btm.pagodirecto.R;
import com.btm.pagodirecto.activities.baseActivities.BaseActivity;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class PayUsersActivity extends BaseActivity {

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
    LinearLayout pendingitemsCountContainer;

    @Bind(R.id.pending_items_count)
    TextView pendingitemsCount;

    private ArrayList<User> mUsers = new ArrayList<User>();
    private ArrayList<Receipt> mPendings = new ArrayList<Receipt>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_users);
        ButterKnife.bind(this);
        Util.setActivity(this);

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
        map.put("status", "pending");
        //map.put("user_id", Util.getFromSharedPreferences("user_id"));
        final BaseActivity act = this;
        ServiceGenerator.getService(ApiService.class)
                .receipts(map)
                .enqueue(new CustomRetrofitCallback<CustomResponse<ResponseReceipts>>() {

                    @Override
                    public void handleSuccess(Object response) {
                        ResponseReceipts responseReceipts = (ResponseReceipts) response;
                        mPendings = responseReceipts.getReceipts();
                        if(mPendings.size()>0){
                            pendingitemsCount.setText(String.valueOf(mPendings.size()));
                            pendingitemsCountContainer.setVisibility(View.VISIBLE);
                        }

                        pendingList.setAdapter(new ReceiptsRecyclerViewAdapter(getApplicationContext(),mPendings,new ReceiptsRecyclerViewAdapter.OnItemClickListener() {
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

    @OnClick(R.id.users_tab)
    public void showUserGrid(){
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
    }

    public void hideSoftKeyboard() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @OnClick(R.id.btn_back)
    public void goToBack(){
        this.finish();
    }
}
