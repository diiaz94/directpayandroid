package com.btm.pagodirecto.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.btm.pagodirecto.R;
import com.btm.pagodirecto.activities.ProductDetailActivity;
import com.btm.pagodirecto.activities.SellActivity;
import com.btm.pagodirecto.dto.Product;
import com.btm.pagodirecto.transforms.RoundedCornersTransformation;
import com.btm.pagodirecto.util.Util;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;

import java.util.ArrayList;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * Created by Pedro on 4/10/2017.
 */

public class ProductsRecyclerViewAdapter extends RecyclerView.Adapter<ProductsRecyclerViewAdapter.ViewHolder> {

    private final Context ctx;
    private  ArrayList<Product> items;
    private LayoutInflater inflater;
    private final OnItemClickListener listener;

    public ProductsRecyclerViewAdapter(Context ctx, ArrayList<Product> items, OnItemClickListener listener) {
        this.ctx = ctx;
        inflater = (LayoutInflater) this.ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.items = items;
        this.listener = listener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_product, parent, false);


        Double height = (new Double(parent.getMeasuredHeight() / 5));
        int width = parent.getMeasuredWidth() / 1;

        view.setLayoutParams(new RecyclerView.LayoutParams(width, height.intValue()));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = items.get(position);

        GlideUrl glideUrl = new GlideUrl(items.get(position).getPhoto_url(), new LazyHeaders.Builder()
                .build());
        holder.productPrice.setText("Bs. "+items.get(position).getPrice());
        holder.productName.setText(items.get(position).getName());

        MultiTransformation multi = new MultiTransformation(
                new RoundedCornersTransformation(50, 0, RoundedCornersTransformation.CornerType.TOP_LEFT),
                new RoundedCornersTransformation(50, 0, RoundedCornersTransformation.CornerType.BOTTOM_LEFT));

        Glide.with(ctx).load(glideUrl).apply(bitmapTransform(multi)).into(holder.productImage);

        //holder.mView.setTag(items.get(position).getId());
        holder.productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Flag", "onClick all row");
                listener.onItemClick(items.get(position),1);
            }
        });
        holder.productInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Flag", "onClick all row");
                listener.onItemClick(items.get(position),1);
            }
        });
        holder.containerBtnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(items.get(position), 0);
            }
        });
        holder.btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(items.get(position), 0);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface OnItemClickListener {
        void onItemClick(Product item, int option);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView productImage;
        public final LinearLayout productInfo;
        public final TextView productPrice;;
        public final TextView productName;
        public final LinearLayout containerBtnBuy;
        public final ImageButton btnBuy;

        public Product mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            productImage = (ImageView) view.findViewById(R.id.product_image);
            productInfo = (LinearLayout) view.findViewById(R.id.product_info);
            productPrice = (TextView) view.findViewById(R.id.product_price);
            productName = (TextView) view.findViewById(R.id.product_name);
            containerBtnBuy = (LinearLayout) view.findViewById(R.id.container_btn_buy);
            btnBuy = (ImageButton) view.findViewById(R.id.btn_buy);

        }
    }
}
