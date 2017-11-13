package com.btm.pagodirecto.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.btm.pagodirecto.R;
import com.btm.pagodirecto.activities.baseActivities.BaseActivity;
import com.btm.pagodirecto.util.Util;

import butterknife.Bind;
import butterknife.OnClick;

public class PayAccepted extends BaseActivity {

    @Bind(R.id.btn_go_home)
    Button btnGoHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_accepted);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Util.setActivity(this);
    }

    @OnClick(R.id.btn_go_home)
    public void goHome(){
        Util.goToActivitySlide(
                Util.getActivity(),
                HomeActivity.class,
                Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
    }
}

