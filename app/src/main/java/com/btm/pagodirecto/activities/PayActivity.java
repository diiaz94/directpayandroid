package com.btm.pagodirecto.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import de.hdodenhof.circleimageview.CircleImageView;

public class PayActivity extends BaseActivity {

    @Bind(R.id.btn_back)
    Button btnBack;

    @Bind(R.id.user_name)
    TextView userName;

    @Bind(R.id.image_profile)
    CircleImageView userImage;

    @Bind(R.id.arrows)
    ImageButton arrowsAccounts;

    @Bind(R.id.continue_pay)
    Button continuePay;

    @Bind(R.id.debit_account)
    TextView debitAccount;

    @Bind(R.id.amount_edit_text)
    EditText ammountEditText;


    final Context context = this;
    private String mImageUrl;
    private String mEntityName;
    private String mPayType;
    private User mUserSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        ButterKnife.bind(this);
        Gson gson = new Gson();
        mUserSelected= new User();
        mUserSelected = gson.fromJson(getIntent().getStringExtra(Constants.TAG_USER_OBJECT), User.class);
        setUserAttributes();
        hideSoftKeyboard();

        ammountEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //ammountEditText.setText("00");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

            public String formatAmount(int num)
            {
                DecimalFormat decimalFormat = new DecimalFormat();
                DecimalFormatSymbols decimalFormateSymbol = new DecimalFormatSymbols();
                decimalFormateSymbol.setGroupingSeparator(',');
                decimalFormat.setDecimalFormatSymbols(decimalFormateSymbol);
                return decimalFormat.format(num);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Util.setActivity(this);
    }



    private void setUserAttributes() {
        userName.setText(mUserSelected.getName());
        GlideUrl glideUrl = new GlideUrl(mUserSelected.getPhoto_url(), new LazyHeaders.Builder()
                .build());

        Glide.with(Util.getContext()).load(glideUrl).into(userImage);
    }

    @OnClick(R.id.continue_pay)
    public void continuePay(){
        Gson g = new Gson();
        Intent intent = new Intent(this,PayMethodActivity.class);
        intent.putExtra(Constants.TAG_PAY_TYPE,"user");
        intent.putExtra(Constants.TAG_USER_OBJECT,g.toJson(mUserSelected));
        this.startActivity(intent);
        this.overridePendingTransition(R.anim.slide_from_right,R.anim.slide_to_left);
    }

    @OnClick(R.id.arrows)
    public void arrows(){
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.accounts_prompt, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final RadioGroup radioGroup = (RadioGroup) promptsView
                .findViewById(R.id.radioGroup);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // get user input and set it to result
                                // edit text
                                int radioButtonID = radioGroup.getCheckedRadioButtonId();
                                View radioButton = radioGroup.findViewById(radioButtonID);
                                int idx = radioGroup.indexOfChild(radioButton);
                                RadioButton r = (RadioButton)  radioGroup.getChildAt(idx);
                                debitAccount.setText(r.getText());
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }

    @OnClick(R.id.btn_back)
    public void goToBack(){
        this.finish();
    }


    public void hideSoftKeyboard() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

}

