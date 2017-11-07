package com.btm.pagodirecto.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.btm.pagodirecto.PinActivity;
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

    @Bind(R.id.btn_sell)
    Button btnSell;

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

    @OnClick(R.id.btn_sell)
    public void goToSell(){
        /*Util.goToActivitySlide(
            this,
            SelectUserActivity_.class,
            Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK
        );*/
        Util.goToActivitySlide(
                this,
                SelectUserActivity_.class,
                Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    @OnClick(R.id.btn_shop)
    public void goToCommerce(){
        Util.goToActivitySlide(
                this,
                CommerceActivity.class,
                Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    public void setUserAttributes(){
        userName.setText(Util.getFromSharedPreferences("user_name"));

        GlideUrl glideUrl = new GlideUrl(Util.getFromSharedPreferences("user_url_image"), new LazyHeaders.Builder()
                .build());

        Glide.with(Util.getContext()).load(glideUrl).into(userImage);
    }

    @Override
    public void onBackPressed() {
        // code here to show dialog
        Util.goToActivitySlideBack(
                Util.getActivity(),
                MainActivity.class,
                Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK
        );
    }
}
