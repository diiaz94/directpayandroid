package com.btm.pagodirecto.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.btm.pagodirecto.R;
import com.btm.pagodirecto.activities.baseActivities.BaseActivity;
import com.btm.pagodirecto.util.Util;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends BaseActivity {

    @Bind(R.id.btn_commerces)
    Button btnCommerces;

    @Bind(R.id.btn_pay)
    Button btnPay;

    @Bind(R.id.user_name)
    TextView userName;

    @Bind(R.id.image_profile)
    CircleImageView userImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        this.setUserAttributes();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Util.setActivity(this);
    }

    @OnClick(R.id.btn_pay)
    public void goToPay(){
        Util.goToActivitySlide(
                this,
                PayUsers.class);
    }

    @OnClick(R.id.btn_commerces)
    public void goToCommerce(){
        Util.goToActivitySlide(
                this,
                CommerceActivity.class);
    }

    public void setUserAttributes(){
        userName.setText(Util.getFromSharedPreferences("user_name"));

        GlideUrl glideUrl = new GlideUrl(Util.getFromSharedPreferences("user_url_image"), new LazyHeaders.Builder()
                .build());

        Glide.with(Util.getContext()).load(glideUrl).into(userImage);
    }

}
