package com.btm.pagodirecto.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.btm.pagodirecto.R;
import com.btm.pagodirecto.util.Util;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PinActivity extends AppCompatActivity {

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
    @Bind(R.id.pin4)
    LinearLayout mPin4;


    String mPin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);
        ButterKnife.bind(this);
        mPin = "";
    }

    public void addNumber(String n){
        if(mPin.length()<4){
            mPin = mPin+ n;
            Util.showMessage(mPin);

            switch (mPin.length()){
                case 1:
                    mPin1.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    mPin2.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    mPin3.setVisibility(View.VISIBLE);
                    break;
                case 4:
                    mPin4.setVisibility(View.VISIBLE);
                    //Go to listo
                    Util.goToActivitySlide(
                            Util.getActivity(),
                            PayAccepted.class,
                            Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    break;
            }
        }

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
                case 3:
                    mPin4.setVisibility(View.INVISIBLE);
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
        mPin4.setVisibility(View.INVISIBLE);
    }
    @OnClick(R.id.delete_option)
    public void delete() {
        deleteNumber();
    }
}
