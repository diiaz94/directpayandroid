package com.btm.pagodirecto.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.btm.pagodirecto.R;
import com.btm.pagodirecto.transforms.RoundedCornersTransformation;
import com.btm.pagodirecto.util.Util;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class ProductDetailActivity extends AppCompatActivity {

    @Bind(R.id.product_name)
    TextView productName;

    @Bind(R.id.product_description)
    TextView productDescription;

    @Bind(R.id.product_image)
    ImageView productImage;

    @Bind(R.id.product_price)
    TextView productPrice;

    @Bind(R.id.btn_back)
    Button btnBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        ButterKnife.bind(this);
        setProductAttributes();
    }

    public void setProductAttributes(){
        productName.setText(Util.getFromSharedPreferences("product_name"));
        productDescription.setText(Util.getFromSharedPreferences("product_description"));
        productPrice.setText(Util.getFromSharedPreferences("product_price"));

        GlideUrl glideUrl = new GlideUrl(Util.getFromSharedPreferences("product_url_image"), new LazyHeaders.Builder()
                .build());

        Glide.with(Util.getContext()).load(glideUrl).into(productImage);
    }

    @OnClick(R.id.btn_back)
    public void goToSell(){
        // code here to show dialog
        Util.goToActivitySlideBack(
                Util.getActivity(),
                SellActivity.class,
                Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK
        );
    }

    @Override
    public void onBackPressed() {
        // code here to show dialog
        Util.goToActivitySlideBack(
                Util.getActivity(),
                SellActivity.class,
                Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK
        );
    }
}
