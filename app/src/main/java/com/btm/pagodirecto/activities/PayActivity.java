package com.btm.pagodirecto.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

    @Bind(R.id.arrows)
    ImageButton arrowsAccounts;

    @Bind(R.id.continue_pay)
    Button continuePay;

    @Bind(R.id.debit_account)
    TextView debitAccount;


    final Context context = this;

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
    @OnClick(R.id.continue_pay)
    public void continuePay(){
        Util.goToActivitySlide(
                this,
                PayResume.class);
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
}
