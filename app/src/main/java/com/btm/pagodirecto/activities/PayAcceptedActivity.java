package com.btm.pagodirecto.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.btm.pagodirecto.R;
import com.btm.pagodirecto.activities.baseActivities.BaseActivity;
import com.btm.pagodirecto.util.Util;

import org.w3c.dom.Text;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PayAcceptedActivity extends BaseActivity {

    @Bind(R.id.btn_go_home)
    Button btnGoHome;

    @Bind(R.id.notification_title)
    TextView notificacionTitle;

    @Bind(R.id.notification_description)
    TextView notificationDescription;

    @Bind(R.id.terms)
    TextView terms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_accepted);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Util.setActivity(this);

        if (Util.getFromSharedPreferences("FROM").equals("SEND_PAY")){
            notificacionTitle.setText("Notificacion de envio");
            notificationDescription.setText("Se ha enviado su pedido al comercio");
            terms.setVisibility(View.GONE);
        }

    }

    @OnClick(R.id.btn_go_home)
    public void goHome(){
        Util.goToActivitySlide(
                Util.getActivity(),
                MainActivity.class
        );
    }
}

