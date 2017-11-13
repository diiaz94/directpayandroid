package com.btm.pagodirecto.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.btm.pagodirecto.R;
import com.btm.pagodirecto.activities.baseActivities.BaseActivity;
import com.btm.pagodirecto.adapters.LoginUserRecyclerViewAdapter;
import com.btm.pagodirecto.adapters.UsersRecyclerViewAdapter;
import com.btm.pagodirecto.custom.CustomResponse;
import com.btm.pagodirecto.custom.CustomRetrofitCallback;
import com.btm.pagodirecto.dto.User;
import com.btm.pagodirecto.responses.ResponseUsers;
import com.btm.pagodirecto.services.ApiService;
import com.btm.pagodirecto.services.ServiceGenerator;
import com.btm.pagodirecto.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Edwin alvarado on 9/20/17.
 * Copyright © 2017 Edwin Alvarado. All rights reserved.
 */

public class MainActivity extends BaseActivity {

    @Bind(R.id.grid)
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Util.setActivity(this);
        ButterKnife.bind(this);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        loadUsers();
    }

    private void loadUsers() {
        Map<String,String> map = new HashMap<>();
        ServiceGenerator.getService(ApiService.class)
                .users(map)
                .enqueue(new CustomRetrofitCallback<CustomResponse<ResponseUsers>>() {

                    @Override
                    public void handleSuccess(Object response) {
                        ResponseUsers responseUsers = (ResponseUsers) response;
                        ArrayList<User> users = responseUsers.getUsers();

                        recyclerView.setAdapter(new LoginUserRecyclerViewAdapter(getApplicationContext(),users));
                    }

                    @Override
                    public void handleResponseError(Response response) {

                    }

                    @Override
                    public void handleFailError(Call<CustomResponse<ResponseUsers>> call, Throwable t) {

                    }
                });
    }
}
