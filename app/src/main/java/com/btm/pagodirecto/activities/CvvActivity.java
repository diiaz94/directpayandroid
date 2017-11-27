package com.btm.pagodirecto.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.btm.pagodirecto.R;
import com.btm.pagodirecto.activities.baseActivities.BaseActivity;
import com.btm.pagodirecto.custom.CustomResponse;
import com.btm.pagodirecto.custom.CustomRetrofitCallback;
import com.btm.pagodirecto.dto.Receipt;
import com.btm.pagodirecto.services.ApiService;
import com.btm.pagodirecto.services.ServiceGenerator;
import com.btm.pagodirecto.util.Constants;
import com.btm.pagodirecto.util.Util;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class CvvActivity extends BaseActivity {

    @Bind(R.id.number1)
    LinearLayout number1;
    @Bind(R.id.number2)
    LinearLayout number2;
    @Bind(R.id.number3)
    LinearLayout number3;
    @Bind(R.id.number4)
    LinearLayout number4;
    @Bind(R.id.number5)
    LinearLayout number5;
    @Bind(R.id.number6)
    LinearLayout number6;
    @Bind(R.id.number7)
    LinearLayout number7;
    @Bind(R.id.number8)
    LinearLayout number8;
    @Bind(R.id.number9)
    LinearLayout number9;
    @Bind(R.id.number0)
    LinearLayout number0;
    @Bind(R.id.reset_option)
    TextView resetOption;
    @Bind(R.id.delete_option)
    TextView deleteOption;

    @Bind(R.id.pin1)
    LinearLayout mPin1;
    @Bind(R.id.pin2)
    LinearLayout mPin2;
    @Bind(R.id.pin3)
    LinearLayout mPin3;

    @Bind(R.id.btn_back)
    Button btnBack;


    String mPin;
    private String mPayType;
    private Receipt mReceiptSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cvv);
        ButterKnife.bind(this);
        mPin = "";
        Gson gson = new Gson();
        mPayType = getIntent().getStringExtra(Constants.TAG_PAY_TYPE);

        if(mPayType.equalsIgnoreCase("receipt")){
            mReceiptSelected= new Receipt();
            mReceiptSelected = gson.fromJson(getIntent().getStringExtra(Constants.TAG_RECEIPT_OBJECT), Receipt.class);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Util.setActivity(this);
    }

    public void addNumber(String n){
        if(mPin.length()<3){
            mPin = mPin+ n;
            Util.showMessage(mPin);

            switch (mPin.length()) {
                case 1:
                    mPin1.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    mPin2.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    mPin3.setVisibility(View.VISIBLE);
                    //Go to listo
                    if(mPayType.equalsIgnoreCase("receipt")){
                        payReceipt();
                    }else{
                        Util.goToActivitySlide(
                            Util.getActivity(),
                            PayAcceptedActivity.class);
                    }
                    break;
            }
        }
    }

    private void payReceipt() {
        Map<String,String> map = new HashMap<>();
        map.put("receipt", mReceiptSelected.get_id());
        map.put("status", "paid");
        ServiceGenerator.getService(ApiService.class)
            .payReceipt(map)
            .enqueue(new CustomRetrofitCallback<CustomResponse<Map<String,String>>>() {

                @Override
                public void handleSuccess(Object response) {
                    Util.goToActivitySlide(
                            Util.getActivity(),
                            PayAcceptedActivity.class);
                }

                @Override
                public void handleResponseError(Response response) {

                }

                @Override
                public void handleFailError(Call<CustomResponse<Map<String, String>>> call, Throwable t) {

                }

            });
    }

    public void deleteNumber(){
        if(mPin.length()>0){
            mPin = mPin.substring(0,mPin.length()-1);
            Util.showMessage(mPin);
            switch (mPin.length()){
                case 0:
                    mPin1.setVisibility(View.INVISIBLE);
                    break;
                case 1:
                    mPin2.setVisibility(View.INVISIBLE);
                    break;
                case 2:
                    mPin3.setVisibility(View.INVISIBLE);
                    break;
            }
        }
    }

    @OnClick(R.id.number1)
    public void setNumber1() {
        this.addNumber("1");
    }
    @OnClick(R.id.number2)
    public void setNumber2() {
        this.addNumber("2");
    }
    @OnClick(R.id.number3)
    public void setNumber3() {
        this.addNumber("3");
    }
    @OnClick(R.id.number4)
    public void setNumber4() {
        this.addNumber("4");
    }
    @OnClick(R.id.number5)
    public void setNumber5() {
        this.addNumber("5");
    }
    @OnClick(R.id.number6)
    public void setNumber6() {
        this.addNumber("6");
    }
    @OnClick(R.id.number7)
    public void setNumber7() {
        this.addNumber("7");
    }
    @OnClick(R.id.number8)
    public void setNumber8() {
        this.addNumber("8");
    }
    @OnClick(R.id.number9)
    public void setNumber9() {
        this.addNumber("9");
    }
    @OnClick(R.id.number0)
    public void setNumber0() {
        this.addNumber("0");
    }

    @OnClick(R.id.reset_option)
    public void reset() {
        this.mPin = "";
        mPin1.setVisibility(View.INVISIBLE);
        mPin2.setVisibility(View.INVISIBLE);
        mPin3.setVisibility(View.INVISIBLE);
    }
    @OnClick(R.id.delete_option)
    public void delete() {
        deleteNumber();
    }

    @OnClick(R.id.btn_back)
    public void goToBack(){
        this.finish();
    }
}
