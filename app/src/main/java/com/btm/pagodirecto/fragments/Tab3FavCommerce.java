package com.btm.pagodirecto.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.btm.pagodirecto.R;
import com.btm.pagodirecto.adapters.CommerceRecyclerViewAdapter;
import com.btm.pagodirecto.dto.Commerce;
import com.btm.pagodirecto.util.Util;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by edwinalvarado on 11/6/17.
 */

public class Tab3FavCommerce extends Fragment {

    @Bind(R.id.list)
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_commerce_tab1, container, false);
        ButterKnife.bind(this,v);

        recyclerView.setLayoutManager(new GridLayoutManager(Util.getContext(), 1));
        loadCommerces();

        return v;
    }

    private void loadCommerces() {

        ArrayList<Commerce> commerces = new ArrayList<Commerce>();

        //Add comerces to an array
        Commerce commerce1 = new Commerce("0001","Lodge Restaurant", "https://image.ibb.co/hf2rvw/image_3.png", "Lodge Restaurant", "Comida Meditarranea", "Active", "1",false);
        Commerce commerce2 = new Commerce("0002","Pizzeria Venezia", "https://image.ibb.co/dCmUhb/image_1.png", "Pizzeria Venezia", "Comida italiana", "Active", "5", false);
        Commerce commerce3 = new Commerce("0003","Cafe Habu", "https://image.ibb.co/cVUAoG/image_2.png", "Cafe Habu", "Cafe&lunch bar", "Active", "10", false);

        commerces.add(commerce1);
        commerces.add(commerce2);
        commerces.add(commerce3);

        recyclerView.setAdapter(new CommerceRecyclerViewAdapter(getContext(),commerces));
    }
}
