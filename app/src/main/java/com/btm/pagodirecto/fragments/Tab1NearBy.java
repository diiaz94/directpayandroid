package com.btm.pagodirecto.fragments;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.btm.pagodirecto.R;
import com.btm.pagodirecto.activities.CommerceProductsActivity;
import com.btm.pagodirecto.adapters.CommerceRecyclerViewAdapter;
import com.btm.pagodirecto.custom.CustomResponse;
import com.btm.pagodirecto.custom.CustomRetrofitCallback;
import com.btm.pagodirecto.dto.Commerce;
import com.btm.pagodirecto.responses.ResponseCommerces;
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
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by edwinalvarado on 11/6/17.
 */

public class Tab1NearBy extends Fragment {

    @Bind(R.id.list)
    RecyclerView recyclerView;
    private ArrayList<Commerce> mCommerces;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_commerce_tab1, container, false);
        ButterKnife.bind(this,v);

        recyclerView.setLayoutManager(new GridLayoutManager(Util.getContext(), 1));
        loadCommerces();

        return v;
    }

    private void loadCommerces2() {

        ArrayList<Commerce> commerces = new ArrayList<Commerce>();

        //Add comerces to an array
        Commerce commerce1 = new Commerce("0001","Birras Bar", "https://image.ibb.co/hf2rvw/image_3.png", "Birras Bar", "Cerveza tradicional", "Active", "1",false);
        Commerce commerce2 = new Commerce("0002","Avila Burger", "https://image.ibb.co/dCmUhb/image_1.png", "Avila Burger", "Hamburguesas y mas", "Active", "5", false);
        Commerce commerce3 = new Commerce("0003","Cafe Habu", "https://image.ibb.co/cVUAoG/image_2.png", "Cafe Habu", "Cafe&lunch bar", "Active", "10", false);

        commerces.add(commerce1);
        commerces.add(commerce2);
        commerces.add(commerce3);

        //recyclerView.setAdapter(new CommerceRecyclerViewAdapter(getContext(),commerces));
    }


    private void loadCommerces() {

        Map<String,String> map = new HashMap<>();
        map.put("role", "commerce");
        map.put("user_id", Util.getFromSharedPreferences("user_id"));
        ServiceGenerator.getService(ApiService.class)
                .commerces(map)
                .enqueue(new CustomRetrofitCallback<CustomResponse<ResponseCommerces>>() {

                    @Override
                    public void handleSuccess(Object response) {
                        ResponseCommerces responseCommerces = (ResponseCommerces) response;
                        mCommerces = responseCommerces.getCommerces();

                        recyclerView.setAdapter(new CommerceRecyclerViewAdapter(getActivity().getApplicationContext(),mCommerces,new CommerceRecyclerViewAdapter.OnItemClickListener() {
                            @Override public synchronized void onItemClick(int i,int type) {
                                switch (type){
                                    case 0:
                                        Gson g = new Gson();
                                        Util.saveInSharedPreferences("COMMERCE_NAME",mCommerces.get(i).getName());
                                        Intent intent = new Intent(getActivity(),CommerceProductsActivity.class);
                                        intent.putExtra(Constants.TAG_USER_OBJECT,g.toJson(mCommerces.get(i)));
                                        getActivity().startActivity(intent);
                                        getActivity().overridePendingTransition(R.anim.slide_from_right,R.anim.slide_to_left);
                                        break;

                                }
                            }
                        }));
                    }

                    @Override
                    public void handleResponseError(Response response) {

                    }

                    @Override
                    public void handleFailError(Call<CustomResponse<ResponseCommerces>> call, Throwable t) {

                    }
                });
    }
}
