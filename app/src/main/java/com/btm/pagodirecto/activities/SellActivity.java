package com.btm.pagodirecto.activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.PersistableBundle;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.btm.pagodirecto.R;
import com.btm.pagodirecto.activities.baseActivities.BaseActivity;
import com.btm.pagodirecto.adapters.ProductsCartRecyclerViewAdapter;
import com.btm.pagodirecto.custom.CustomResponse;
import com.btm.pagodirecto.custom.CustomRetrofitCallback;
import com.btm.pagodirecto.custom.SocketHandle;
import com.btm.pagodirecto.dto.Product;
import com.btm.pagodirecto.dto.User;
import com.btm.pagodirecto.fragments.CalculatorFragment;
import com.btm.pagodirecto.fragments.ProductsFragment;
import com.btm.pagodirecto.services.ApiService;
import com.btm.pagodirecto.services.ServiceGenerator;
import com.btm.pagodirecto.util.Constants;
import com.btm.pagodirecto.util.Util;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.google.gson.Gson;

import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class SellActivity extends BaseActivity implements CalculatorFragment.OnFragmentInteractionListener,
                                                            ProductsFragment.OnFragmentInteractionListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private TabLayout tabLayout;
    private int[] tabIcons = {
            R.drawable.list,
            R.drawable.calculator
    };

    @Bind(R.id.sell_container)
    LinearLayout sellContainer;

    //Variables for cart
    private ArrayList<Product> cartItems;
    private RecyclerView.Adapter cartListAdapter;
    private Product currentProduct;

    //Binds for views -- items detail
    @Bind(R.id.sub_total_label)
    TextView subTotalLabel;

    @Bind(R.id.sub_total_amount)
    TextView subTotalAmount;

    @Bind(R.id.cart_items_count_txt)
    TextView cartItemsCountTxt;

    @Bind(R.id.car_list)
    RecyclerView carListRecyclerView;

    @Bind(R.id.textView)
    TextView titleCommerce;

    @Bind(R.id.car_container)
    LinearLayout cartContainer;

    @Bind(R.id.car_items_count_label)
    TextView carItemsCountLabel;

    @Bind(R.id.container_item_count)
    LinearLayout containerItemCount;

    @Bind(R.id.product_name)
    TextView productName;

    @Bind(R.id.product_description)
    TextView productDescription;

    @Bind(R.id.product_price)
    TextView productPrice;

    @Bind(R.id.btn_main)
    Button mainButton;

    @Bind(R.id.product_image)
    ImageView productImage;

    @Bind(R.id.product_detail)
    LinearLayout linearDetailProduct;

    @Bind(R.id.btn_card_pay)
    Button btnCartPay;

    private Double subTotal;


    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private User mUserSelected;
    public String cartMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);
        ButterKnife.bind(this);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        Gson gson = new Gson();
        mUserSelected = new User();
        mUserSelected = gson.fromJson(getIntent().getStringExtra(Constants.TAG_USER_OBJECT), User.class);
        subTotal=0.00;

        cartMode = getIntent().getStringExtra("CART_MODE");

        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        //int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        cartContainer.setLayoutParams(new LinearLayout.LayoutParams((new Double(width)).intValue(), cartContainer.getLayoutParams().height ));

        initCartList();

        setupTabIcons();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Util.setActivity(this);

        btnCartPay.setVisibility(View.GONE);

        if (cartMode.equals("PENDING")){
            cartContainer.setVisibility(View.VISIBLE);
            sellContainer.setVisibility(View.GONE);
            addDummieItems();
        }else{
            sellContainer.setVisibility(View.VISIBLE);
            cartContainer.setVisibility(View.GONE);
        }

    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);

    }

    private void setupTabIcons(){
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void initCartList() {

        cartItems= new ArrayList<Product>();
        cartListAdapter = new ProductsCartRecyclerViewAdapter(getApplicationContext(),cartItems,new ProductsCartRecyclerViewAdapter.OnItemClickListener() {
            @Override public synchronized void onItemClick(int i,int type) {
                int actualSize = 0;
                switch (type){
                    case 0://inc
                        if(cartItems.size()>0&&i<cartItems.size()) {
                            subTotal += Double.valueOf(cartItems.get(i).getPrice());
                            subTotalAmount.setText(String.valueOf(subTotal));
                            cartItems.get(i).setCartQty(cartItems.get(i).getCartQty() + 1);
                            cartItems.get(i).setCartPrice(cartItems.get(i).getCartQty() * cartItems.get(i).getCartPrice());
                            cartListAdapter.notifyItemChanged(i);
                            actualSize = Integer.parseInt((String) cartItemsCountTxt.getText());
                            actualSize += 1;
                            cartItemsCountTxt.setText(String.valueOf(actualSize));
                        }
                        break;
                    case 1://dec
                        // i = (i<=1)? 0:i;
                        if(cartItems.size()>0&&i<cartItems.size()) {
                            subTotal -= Double.valueOf(cartItems.get(i).getPrice());
                            subTotalAmount.setText(String.valueOf(subTotal));
                            cartItems.get(i).setCartQty(cartItems.get(i).getCartQty() - 1);
                            cartItems.get(i).setCartPrice(cartItems.get(i).getCartQty() * cartItems.get(i).getCartPrice());
                            cartListAdapter.notifyItemChanged(i);

                            actualSize = Integer.parseInt((String) cartItemsCountTxt.getText());
                            actualSize -= 1;
                            cartItemsCountTxt.setText(String.valueOf(actualSize));
                        }
                        break;
                    case 2://delete
                        //  i = (i<=1)? 0:i;
                        if(cartItems.size()>0 && i<cartItems.size()) {
                            subTotal-=Double.valueOf(cartItems.get(i).getPrice());
                            subTotalAmount.setText(String.valueOf(subTotal));
                            cartItems.remove(i);
                            cartListAdapter.notifyItemRemoved(i);

                            actualSize = Integer.parseInt((String) cartItemsCountTxt.getText());
                            actualSize -=1;
                            if(actualSize == 0){
                                containerItemCount.setVisibility(View.GONE);
                            }else {
                                cartItemsCountTxt.setText(String.valueOf(actualSize));
                            }
                            carItemsCountLabel.setText(String.valueOf(cartItems.size())+" items en tu carrito de compras");
                        }
                        break;
                }
            }
        });
        carListRecyclerView.setAdapter(cartListAdapter);
        titleCommerce.setText(Util.getFromSharedPreferences("COMMERCE_NAME"));
    }

    public synchronized void attemptAddProduct(Product item) {
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

    @Override
    public void onBackPressed() {
        // code here to show dialog
        if (cartContainer.getVisibility() == View.VISIBLE){
            cartContainer.setVisibility(View.GONE);
            sellContainer.setVisibility(View.VISIBLE);
        }else{
            this.finish();
        }
    }

    @OnClick(R.id.btn_back)
    public void goBack(){
        // code here to show dialog
        if (cartContainer.getVisibility()  == View.VISIBLE){
            cartContainer.setVisibility(View.GONE);
            sellContainer.setVisibility(View.VISIBLE);
        }else{
            this.finish();
        }
    }

    @OnClick(R.id.shop_container_icon)
    public void goToShop(){
        cartContainer.setVisibility(View.VISIBLE);
        sellContainer.setVisibility(View.GONE);
    }

    public void  showProductDetail(Product item){
        currentProduct = item;
        GlideUrl glideUrl = new GlideUrl(item.getPhoto_url(), new LazyHeaders.Builder()
                .build());

        Glide.with(Util.getContext()).load(glideUrl).into(productImage);

        productName.setText(item.getName());
        productDescription.setText(item.getDescription());
        productPrice.setText(item.getPrice());
        mainButton.setText("COMPRAR");

        linearDetailProduct.setVisibility(View.VISIBLE);
        //cartContainer.setVisibility(View.GONE);

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_list, container, false);
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    ProductsFragment tab1 =  new ProductsFragment();
                    return tab1;
                case 1:
                    CalculatorFragment tab2 =  new CalculatorFragment();
                    return tab2;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            /*switch (position) {
                case 0:
                    return "List";
                case 1:
                    return "Calculator";
            }*/
            return null;
        }
    }

    @OnClick(R.id.btn_card_send)
    public void sendToCommerce() {

        Map<String,String> map = new HashMap<>();
        map.put("sent_by", Util.getFromSharedPreferences(Constants.TAG_USER_ID));
        map.put("sent_to", mUserSelected.getId());
        map.put("type", "order");
        map.put("total", subTotal.toString());

        ServiceGenerator.getService(ApiService.class)
                .sendReceipt(map)
                .enqueue(new CustomRetrofitCallback<CustomResponse<Map<String,String>>>() {

                    @Override
                    public void handleSuccess(Object response) {
                        Util.saveInSharedPreferences("FROM","SEND_PAY");
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

    public void addDummieItems(){

        //Add first item
        Product item = new Product();

        item.set_id("59dd8fb30d5a9b00127a7cbc");
        item.setCartQty(2);
        item.setCartPrice(0.0);
        item.setDescription("Einstein has just become the world's first time traveler. I sent him into the future.");
        item.setName("Croissant");
        item.setPhoto_url("http://i.imgur.com/eo4h3h3.png");
        item.setPrice("4500");
        item.setRating("");
        item.setStatus("");

        attemptAddProduct(item);

        //Add second item
        Product item2 = new Product();

        item2.set_id("59dd902a0d5a9b00127a7cbd");
        item2.setCartQty(1);
        item2.setCartPrice(0.0);
        item2.setDescription("Einstein has just become the world's first time traveler. I sent him into the future.");
        item2.setName("Cupcake");
        item2.setPhoto_url("http://i.imgur.com/zGquB7R.png");
        item2.setPrice("2000");
        item2.setRating("");
        item2.setStatus("");

        attemptAddProduct(item2);

        //Add thrid item
        Product item3 = new Product();

        item3.set_id("59dd90600d5a9b00127a7cbe");
        item3.setCartQty(2);
        item3.setCartPrice(0.0);
        item3.setDescription("Einstein has just become the world's first time traveler. I sent him into the future.");
        item3.setName("Cafe Grande");
        item3.setPhoto_url("http://i.imgur.com/bBfAjJ4.png");
        item3.setPrice("2000");
        item3.setRating("");
        item3.setStatus("");

        attemptAddProduct(item3);

    }

}