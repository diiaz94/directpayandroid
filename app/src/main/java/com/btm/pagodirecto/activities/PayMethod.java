package com.btm.pagodirecto.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.btm.pagodirecto.R;
import com.btm.pagodirecto.activities.baseActivities.BaseActivity;
import com.btm.pagodirecto.dto.User;
import com.btm.pagodirecto.util.Constants;
import com.btm.pagodirecto.util.Util;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class PayMethod extends BaseActivity {

    @Bind(R.id.btn_pay_p2p)
    Button btnPayP2p;

    @Bind(R.id.btn_pay)
    Button btnPayCard;

    @Bind(R.id.card_tab)
    LinearLayout cardTab;

    @Bind(R.id.p2p_tab)
    LinearLayout p2pTab;

    @Bind(R.id.card_method)
    LinearLayout cardMethodContainer;

    @Bind(R.id.p2p_method)
    LinearLayout p2pMetodContainer;

    @Bind(R.id.footer_text)
    TextView textFooter;

    @Bind(R.id.btn_back)
    Button btnBack;

    @Bind(R.id.image_profile)
    CircleImageView imageProfile;

    @Bind(R.id.user_name)
    TextView userName;

    @Bind(R.id.tdcName)
    TextView tdcName;

    private String mPayType;
    private User mUserSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_method);
        ButterKnife.bind(this);
        hideSoftKeyboard();

        tdcName.setText(Util.getFromSharedPreferences(Constants.TAG_USER_NAME));
        mPayType = getIntent().getStringExtra(Constants.TAG_PAY_TYPE);
        if(mPayType.equalsIgnoreCase("user")){
            Gson gson = new Gson();
            mUserSelected= new User();
            mUserSelected = gson.fromJson(getIntent().getStringExtra(Constants.TAG_USER_OBJECT), User.class);
            userName.setText(mUserSelected.getName());
            GlideUrl glideUrl = new GlideUrl(mUserSelected.getPhoto_url(), new LazyHeaders.Builder()
                        .build());
            Glide.with(this).load(glideUrl).into(imageProfile).onLoadFailed(this.getResources().getDrawable(R.drawable.logo));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Util.setActivity(this);
    }

    @OnClick(R.id.btn_pay_p2p)
    public void payP2PAction(){
        Util.goToActivitySlide(
                Util.getActivity(),
                PinActivity.class);
    }

    @OnClick(R.id.btn_pay)
    public void payCardAction(){
        Util.goToActivitySlide(
                Util.getActivity(),
                PinActivity.class);
    }

    @OnClick(R.id.card_tab)
    public void showCardMethodContainer(){
        final int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            cardTab.setBackgroundDrawable( getResources().getDrawable(R.drawable.tabs_select_state) );
            p2pTab.setBackgroundDrawable(null);
        } else {
            cardTab.setBackground( getResources().getDrawable(R.drawable.tabs_select_state));
            p2pTab.setBackground(null);
        }
        p2pMetodContainer.setVisibility(View.GONE);
        cardMethodContainer.setVisibility(View.VISIBLE);
        textFooter.setText("Guardar Método de Pago");

    }

    @OnClick(R.id.p2p_tab)
    public void showP2PMethodContainer(){

        if(mPayType.equalsIgnoreCase("user")){
            final int sdk = android.os.Build.VERSION.SDK_INT;
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                p2pTab.setBackgroundDrawable( getResources().getDrawable(R.drawable.tabs_select_state) );
                cardTab.setBackgroundDrawable(null);
            } else {
                p2pTab.setBackground( getResources().getDrawable(R.drawable.tabs_select_state));
                cardTab.setBackground(null);
            }
            cardMethodContainer.setVisibility(View.GONE);
            p2pMetodContainer.setVisibility(View.VISIBLE);
            textFooter.setText("Teléfono relacionado a registro de usuario");
        }else{
            Util.showMessage("Opción deshabilitada");
        }
    }

    public void hideSoftKeyboard() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @OnClick(R.id.btn_back)
    public void goToBack(){
        this.finish();
    }

}
