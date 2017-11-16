package com.btm.pagodirecto.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.LinearLayout;

import com.btm.pagodirecto.R;
import com.btm.pagodirecto.activities.baseActivities.BaseActivity;
import com.btm.pagodirecto.adapters.ProductsRecyclerViewAdapter;
import com.btm.pagodirecto.adapters.ProductsResumeRecyclerViewAdapter;
import com.btm.pagodirecto.custom.CustomResponse;
import com.btm.pagodirecto.custom.CustomRetrofitCallback;
import com.btm.pagodirecto.dto.Product;
import com.btm.pagodirecto.responses.ResponseProducts;
import com.btm.pagodirecto.services.ApiService;
import com.btm.pagodirecto.services.ServiceGenerator;
import com.btm.pagodirecto.util.Util;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class PayResume extends BaseActivity {

    @Bind(R.id.products_container)
    LinearLayout productsContainer;

    @Bind(R.id.grid)
    RecyclerView recyclerView;

    @Bind(R.id.btn_pay)
    Button btnPay;

    @Bind(R.id.btn_back)
    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_resume);
        ButterKnife.bind(this);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        loadProducts();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Util.setActivity(this);
    }

    private void loadProducts() {
        ServiceGenerator.getService(ApiService.class)
                .products()
                .enqueue(new CustomRetrofitCallback<CustomResponse<ResponseProducts>>() {

                    @Override
                    public void handleSuccess(Object response) {
                        ResponseProducts responseProducts = (ResponseProducts) response;
                        ArrayList<Product> products = responseProducts.getProducts();
                        recyclerView.setAdapter(new ProductsResumeRecyclerViewAdapter(getApplicationContext(),products));
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
        Util.goToActivitySlide(
                this,
                PayMethod.class);
    }

    @OnClick(R.id.btn_back)
    public void goToBack(){
        this.finish();
    }

}
