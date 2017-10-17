package com.btm.pagodirecto.activities;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.btm.pagodirecto.R;
import com.btm.pagodirecto.activities.baseActivities.BaseActivity;
import com.btm.pagodirecto.custom.CustomResponse;
import com.btm.pagodirecto.custom.CustomRetrofitCallback;
import com.btm.pagodirecto.adapters.UsersRecyclerViewAdapter;
import com.btm.pagodirecto.dto.User;
import com.btm.pagodirecto.responses.ResponseUsers;
import com.btm.pagodirecto.services.ApiService;
import com.btm.pagodirecto.services.ServiceGenerator;
import com.btm.pagodirecto.util.Util;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

public class SelectUserActivity extends BaseActivity {

    @Bind(R.id.grid)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user);
        Util.setActivity(this);
        ButterKnife.bind(this);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
    loadUsers();
    }

    private void loadUsers() {
      ServiceGenerator.getService(ApiService.class)
                .users()
                .enqueue(new CustomRetrofitCallback<CustomResponse<ResponseUsers>>() {

                    @Override
                    public void handleSuccess(Object response) {
                        ResponseUsers responseUsers = (ResponseUsers) response;
                        ArrayList<User> users = responseUsers.getUsers();

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

}
