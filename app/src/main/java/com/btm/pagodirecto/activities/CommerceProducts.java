package com.btm.pagodirecto.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.btm.pagodirecto.R;
import com.btm.pagodirecto.adapters.ProductsRecyclerViewAdapter;
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

public class CommerceProducts extends AppCompatActivity {


    @Bind(R.id.btn_back)
    Button btnBack;
    @Bind(R.id.scroll_container)
    HorizontalScrollView scrollContainer;
    @Bind(R.id.products_container)
    LinearLayout productsContainer;
    @Bind(R.id.car_container)
    LinearLayout carContainer;
    @Bind(R.id.grid)
    RecyclerView recyclerView;
    @Bind(R.id.shop_container_icon)
    RelativeLayout shopContainerIcon;
    @Bind(R.id.container_item_count)
    LinearLayout containerItemCount;
    @Bind(R.id.cart_items_count_txt)
    TextView cartItemsCountTxt;
    @Bind(R.id.car_items_count_label)
    TextView carItemsCountLabel;
    @Bind(R.id.sub_total_label)
    TextView subTotalLabel;
    @Bind(R.id.sub_total_amount)
    TextView subTotalAmount;

    private boolean carOpen;
    private Double subTotal;

    private  ArrayList<Product> cartItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commerce_products);
        ButterKnife.bind(this);
        carOpen = false;
        subTotal=0.00;
        cartItems= new ArrayList<Product>();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        //int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        productsContainer.setLayoutParams(new LinearLayout.LayoutParams(width, productsContainer.getLayoutParams().height ));
        carContainer.setLayoutParams(new LinearLayout.LayoutParams((new Double(width/ 1.14f)).intValue(), carContainer.getLayoutParams().height ));
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        loadProducts();

    }

    private void loadProducts() {
        ServiceGenerator.getService(ApiService.class)
                .products()
                .enqueue(new CustomRetrofitCallback<CustomResponse<ResponseProducts>>() {

                    @Override
                    public void handleSuccess(Object response) {
                        ResponseProducts responseProducts = (ResponseProducts) response;
                        ArrayList<Product> products = responseProducts.getProducts();

                        recyclerView.setAdapter(new ProductsRecyclerViewAdapter(getApplicationContext(),products,new ProductsRecyclerViewAdapter.OnItemClickListener() {
                            @Override public void onItemClick(Product item) {
                                Util.showMessage(item.getPrice());
                                attemptAddProduct(item);
                            }
                        }));
                    }

                    @Override
                    public void handleResponseError(Response response) {

                    }

                    @Override
                    public void handleFailError(Call<CustomResponse<ResponseProducts>> call, Throwable t) {

                    }
                });
    }

    private void attemptAddProduct(Product item) {
        Product p = new Product();
        if(!exist(item.get_id())){
            p.set_id(item.get_id());
            p.setDescription(item.getDescription());
            p.setName(item.getName());
            p.setPhoto_url(item.getPhoto_url());
            p.setPrice(item.getPrice());
            p.setRating(item.getRating());
            p.setStatus(item.getStatus());
            cartItems.add(p);
            subTotal += Double.valueOf(item.getPrice());
            updateItemsCount();
        }
    }

    private boolean exist(String id) {
        for (int i = 0; i < cartItems.size(); i++) {
            if(cartItems.get(i).get_id().equalsIgnoreCase(id)) return true;
        }
        return false;
    }

    private void updateItemsCount() {

        cartItemsCountTxt.setText(String.valueOf(cartItems.size()));
        carItemsCountLabel.setText(String.valueOf(cartItems.size())+" items en tu carrito de compras");
        subTotalAmount.setText(String.valueOf(subTotal));
        containerItemCount.setVisibility(cartItems.isEmpty()?View.GONE:View.VISIBLE);
    }

    @OnClick(R.id.btn_back)
    public void goBack(){

        this.finish();
    }

    @OnClick(R.id.shop_container_icon)
    public void goToShop(){

        if(carOpen)
            scrollContainer.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
        else
            scrollContainer.fullScroll(HorizontalScrollView.FOCUS_LEFT);

        carOpen = !carOpen;
    }
}
