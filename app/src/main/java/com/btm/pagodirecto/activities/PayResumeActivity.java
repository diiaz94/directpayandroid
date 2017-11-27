package com.btm.pagodirecto.activities;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.btm.pagodirecto.R;
import com.btm.pagodirecto.activities.baseActivities.BaseActivity;
import com.btm.pagodirecto.adapters.ProductsResumeRecyclerViewAdapter;
import com.btm.pagodirecto.custom.CustomResponse;
import com.btm.pagodirecto.custom.CustomRetrofitCallback;
import com.btm.pagodirecto.dto.Product;
import com.btm.pagodirecto.dto.Receipt;
import com.btm.pagodirecto.responses.ResponseProducts;
import com.btm.pagodirecto.services.ApiService;
import com.btm.pagodirecto.services.ServiceGenerator;
import com.btm.pagodirecto.util.Constants;
import com.btm.pagodirecto.util.Util;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class PayResumeActivity extends BaseActivity {

    @Bind(R.id.products_container)
    LinearLayout productsContainer;

    @Bind(R.id.ticket_container)
    LinearLayout ticketContainer;

    @Bind(R.id.grid)
    RecyclerView recyclerView;

    @Bind(R.id.btn_pay)
    Button btnPay;

    @Bind(R.id.btn_back)
    Button btnBack;

    @Bind(R.id.title)
    TextView title;

    private Receipt mReceiptSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_resume);
        ButterKnife.bind(this);
        Gson gson = new Gson();
        mReceiptSelected= new Receipt();
        mReceiptSelected = gson.fromJson(getIntent().getStringExtra(Constants.TAG_RECEIPT_OBJECT), Receipt.class);


        if(mReceiptSelected.getType().equalsIgnoreCase("ticket")){
            loadTicket();
        }

        if(mReceiptSelected.getType().equalsIgnoreCase("order")){
            loadProducts();
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        Util.setActivity(this);
    }

    private void loadTicket() {
        title.setText("Detalle de pago pendiente");
        productsContainer.setVisibility(View.GONE);
        ticketContainer.setVisibility(View.VISIBLE);

    }


    private void loadProducts() {

        title.setText("Resumen de compra");



        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        ServiceGenerator.getService(ApiService.class)
                .products()
                .enqueue(new CustomRetrofitCallback<CustomResponse<ResponseProducts>>() {

                    @Override
                    public void handleSuccess(Object response) {
                        productsContainer.setVisibility(View.VISIBLE);
                        ticketContainer.setVisibility(View.GONE);
                        ResponseProducts responseProducts = (ResponseProducts) response;
                        ArrayList<Product> products = responseProducts.getProducts();
                        recyclerView.setAdapter(new ProductsResumeRecyclerViewAdapter(getApplicationContext(),products));
                        recyclerView.post(new Runnable() {

                            @Override
                            public void run() {
                                Display display = getWindowManager().getDefaultDisplay();
                                Point sizeP = new Point();
                                display.getSize(sizeP);
                                int width = sizeP.x;
                                int totalHeight = sizeP.y;

                                recyclerView.setLayoutParams(new LinearLayout.LayoutParams((new Double(width * 0.946)).intValue(),productsContainer.getMeasuredHeight()));


                            }
                        });
                    }

                    @Override
                    public void handleResponseError(Response response) {

                    }

                    @Override
                    public void handleFailError(Call<CustomResponse<ResponseProducts>> call, Throwable t) {

                    }
                });
    }

    @OnClick(R.id.btn_pay)
    public void goToPayMethod(){

        Gson g = new Gson();
        Intent intent = new Intent(this,PayMethodActivity.class);
        intent.putExtra(Constants.TAG_PAY_TYPE,"receipt");
        intent.putExtra(Constants.TAG_RECEIPT_OBJECT,g.toJson(mReceiptSelected));
        this.startActivity(intent);
        this.overridePendingTransition(R.anim.slide_from_right,R.anim.slide_to_left);

    }

    @OnClick(R.id.btn_back)
    public void goToBack(){
        this.finish();
    }

}
