package com.btm.pagodirecto.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.btm.pagodirecto.R;
import com.btm.pagodirecto.activities.baseActivities.BaseActivity;
import com.btm.pagodirecto.adapters.ProductsCartRecyclerViewAdapter;
import com.btm.pagodirecto.adapters.ProductsRecyclerViewAdapter;
import com.btm.pagodirecto.custom.CustomResponse;
import com.btm.pagodirecto.custom.CustomRetrofitCallback;
import com.btm.pagodirecto.dto.Product;
import com.btm.pagodirecto.responses.ResponseProducts;
import com.btm.pagodirecto.services.ApiService;
import com.btm.pagodirecto.services.ServiceGenerator;
import com.btm.pagodirecto.transforms.RoundedCornersTransformation;
import com.btm.pagodirecto.util.Util;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;

import org.w3c.dom.Text;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class CommerceProducts extends BaseActivity {


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
    @Bind(R.id.car_list)
    RecyclerView carListRecyclerView;

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

    //Detail items
    @Bind(R.id.product_detail)
    LinearLayout linearDetailProduct;

    @Bind(R.id.product_image)
    ImageView productImage;

    @Bind(R.id.product_name)
    TextView productName;

    @Bind(R.id.product_description)
    TextView productDescription;

    @Bind(R.id.product_price)
    TextView productPrice;

    @Bind(R.id.btn_main)
    Button mainButton;

    @Bind(R.id.btn_card_send)
    Button btnCardSent;

    @Bind(R.id.btn_card_pay)
    Button btnCardPay;

    @Bind(R.id.textView)
    TextView titleCommerce;

    private boolean carOpen;
    private boolean detailOpen;
    private Double subTotal;

    private  ArrayList<Product> cartItems;
    private RecyclerView.Adapter cartListAdapter;

    private Product currentProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commerce_products);
        ButterKnife.bind(this);
        carOpen = false;
        subTotal=0.00;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        //int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        productsContainer.setLayoutParams(new LinearLayout.LayoutParams(width, productsContainer.getLayoutParams().height ));
        carContainer.setLayoutParams(new LinearLayout.LayoutParams((new Double(width/ 1.14f)).intValue(), carContainer.getLayoutParams().height ));
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        loadProducts();
        initCartList();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Util.setActivity(this);
    }

    private void initCartList() {

        cartItems= new ArrayList<Product>();
        cartListAdapter = new ProductsCartRecyclerViewAdapter(getApplicationContext(),cartItems,new ProductsCartRecyclerViewAdapter.OnItemClickListener() {
            @Override public synchronized void onItemClick(int i,int type) {
                int actualSize = 0;
                switch (type){
                    case 0://inc
                        subTotal+=Double.valueOf(cartItems.get(i).getPrice());
                        subTotalAmount.setText(String.valueOf(subTotal));
                        cartItems.get(i).setCartQty(cartItems.get(i).getCartQty()+1);
                        cartItems.get(i).setCartPrice(cartItems.get(i).getCartQty()*cartItems.get(i).getCartPrice());
                        cartListAdapter.notifyItemChanged(i);
                        break;
                    case 1://dec
                        i = (i<=1)? 0:i;
                        subTotal-=Double.valueOf(cartItems.get(i).getPrice());
                        subTotalAmount.setText(String.valueOf(subTotal));
                        cartItems.get(i).setCartQty(cartItems.get(i).getCartQty()-1);
                        cartItems.get(i).setCartPrice(cartItems.get(i).getCartQty()*cartItems.get(i).getCartPrice());
                        cartListAdapter.notifyItemChanged(i);

                        actualSize = Integer.parseInt((String) cartItemsCountTxt.getText());
                        actualSize -=1;
                        cartItemsCountTxt.setText(String.valueOf(actualSize));

                        break;
                    case 2://delete
                        i = (i<=1)? 0:i;
                        subTotal-=Double.valueOf(cartItems.get(i).getPrice());
                        subTotalAmount.setText(String.valueOf(subTotal));
                        cartItems.remove(i);
                        cartListAdapter.notifyItemRemoved(i);

                        actualSize = Integer.parseInt((String) cartItemsCountTxt.getText());
                        actualSize -=1;
                        cartItemsCountTxt.setText(String.valueOf(actualSize));
                        break;
                }
            }
        });
        carListRecyclerView.setAdapter(cartListAdapter);
        titleCommerce.setText(Util.getFromSharedPreferences("COMMERCE_NAME"));
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
                            @Override public void onItemClick(Product item, int option) {
                                switch (option){
                                    case 0:
                                        Util.showMessage(item.getPrice());
                                        attemptAddProduct(item);
                                        break;
                                    case 1:
                                        //Util.showMessage(item.getPrice());
                                        showProductDetail(item);
                                        break;

                                }
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

    private synchronized void attemptAddProduct(Product item) {
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
            cartListAdapter.notifyItemInserted(cartItems.size()-1);
            carListRecyclerView.scrollToPosition(cartItems.size()-1);

            subTotal += Double.valueOf(item.getPrice());
            updateItemsCount();
        }else{
            int index = getIndexForItem(item.get_id());

            subTotal+=Double.valueOf(cartItems.get(index).getPrice());
            subTotalAmount.setText(String.valueOf(subTotal));
            cartItems.get(index).setCartQty(cartItems.get(index).getCartQty()+1);
            cartItems.get(index).setCartPrice(cartItems.get(index).getCartQty()*cartItems.get(index).getCartPrice());
            cartListAdapter.notifyItemChanged(index);

            int actualSize = Integer.parseInt((String) cartItemsCountTxt.getText());
            actualSize +=1;
            cartItemsCountTxt.setText(String.valueOf(actualSize));
        }
    }

    private Integer getIndexForItem(String id ){
        for (int i = 0; i < cartItems.size(); i++ ){
            if (cartItems.get(i).get_id().equals(id)){
                return i;
            }
        }
        return 0;
    }

    private boolean exist(String id) {
        for (int i = 0; i < cartItems.size(); i++) {
            if(cartItems.get(i).get_id().equalsIgnoreCase(id)) return true;
        }
        return false;
    }

    private void updateItemsCount() {

        int actualSize = Integer.parseInt((String) cartItemsCountTxt.getText());
        actualSize +=1;
        cartItemsCountTxt.setText(String.valueOf(actualSize));

        carItemsCountLabel.setText(String.valueOf(cartItems.size())+" items en tu carrito de compras");
        subTotalAmount.setText(String.valueOf(subTotal));
        containerItemCount.setVisibility(cartItems.isEmpty()?View.GONE:View.VISIBLE);
    }

    @OnClick(R.id.btn_back)
    public void goBack(){
        if (detailOpen){
            recyclerView.setVisibility(View.VISIBLE);
            linearDetailProduct.setVisibility(View.GONE);
            detailOpen = false;
        }else{
            this.finish();
        }
    }

    @OnClick(R.id.shop_container_icon)
    public void goToShop(){

        if(!carOpen)
            scrollContainer.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
        else
            scrollContainer.fullScroll(HorizontalScrollView.FOCUS_LEFT);

        carOpen = !carOpen;
    }

    private void  showProductDetail(Product item){
        currentProduct = item;
        GlideUrl glideUrl = new GlideUrl(item.getPhoto_url(), new LazyHeaders.Builder()
                .build());

        Glide.with(Util.getContext()).load(glideUrl).into(productImage);

        productName.setText(item.getName());
        productDescription.setText(item.getDescription());
        productPrice.setText(item.getPrice());
        mainButton.setText("COMPRAR");
        detailOpen = true;

        linearDetailProduct.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    @OnClick(R.id.btn_main)
    public void mainButton(){
        //Util.showMessage(item.getPrice());
        attemptAddProduct(currentProduct);
    }

    @OnClick(R.id.btn_card_pay)
    public void goToResume(){
        Util.goToActivitySlide(
                Util.getActivity(),
                PayResume.class);
    }

    @OnClick(R.id.btn_card_send)
    public void sendToCommerce(){
        Util.saveInSharedPreferences("FROM","SEND_PAY");
        Util.goToActivitySlide(
                Util.getActivity(),
                PayAccepted.class);
    }

}
