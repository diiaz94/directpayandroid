package com.btm.pagodirecto.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
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

public class PayActivity extends BaseActivity {

    @Bind(R.id.btn_back)
    Button btnBack;

    @Bind(R.id.user_name)
    TextView userName;

    @Bind(R.id.image_profile)
    CircleImageView userImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        ButterKnife.bind(this);
        setUserAttributes();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Util.setActivity(this);
    }

    private void setUserAttributes() {
        userName.setText(Util.getFromSharedPreferences("pay_entity_name"));
        GlideUrl glideUrl = new GlideUrl(Util.getFromSharedPreferences("pay_image_url"), new LazyHeaders.Builder()
                .build());

        Glide.with(Util.getContext()).load(glideUrl).into(userImage);
    }

    @OnClick(R.id.btn_back)
    public void goToBack(){
        this.finish();
    }
}
