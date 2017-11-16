package com.btm.pagodirecto.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.btm.pagodirecto.R;
import com.btm.pagodirecto.dto.Product;
import com.btm.pagodirecto.transforms.RoundedCornersTransformation;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;

import java.util.ArrayList;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * Created by edwinalvarado on 11/13/17.
 */

public class ProductsResumeRecyclerViewAdapter extends RecyclerView.Adapter<ProductsResumeRecyclerViewAdapter.ViewHolder> {

    private final Context ctx;
    private ArrayList<Product> items;
    private LayoutInflater inflater;

    public ProductsResumeRecyclerViewAdapter(Context ctx, ArrayList<Product> items) {
        this.ctx = ctx;
        inflater = (LayoutInflater) this.ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.items = items;
    }


    @Override
    public ProductsResumeRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_pay_resume, parent, false);

        Double height = (new Double(parent.getMeasuredHeight() / 3.5));
        int width = parent.getMeasuredWidth() / 1;

        view.setLayoutParams(new RecyclerView.LayoutParams(width, height.intValue()));
        return new ProductsResumeRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductsResumeRecyclerViewAdapter.ViewHolder holder, final int position) {
        holder.mItem = items.get(position);

        GlideUrl glideUrl = new GlideUrl(items.get(position).getPhoto_url(), new LazyHeaders.Builder()
                .build());
        holder.productPrice.setText("Bs. "+items.get(position).getPrice());
        holder.productName.setText(items.get(position).getName());

        //MultiTransformation multi = new MultiTransformation(
        //        new RoundedCornersTransformation(50, 0, RoundedCornersTransformation.CornerType.TOP_LEFT),
        //        new RoundedCornersTransformation(50, 0, RoundedCornersTransformation.CornerType.BOTTOM_LEFT));

        //Glide.with(ctx).load(glideUrl).apply(bitmapTransform(multi)).into(holder.productImage);

        Glide.with(ctx).load(glideUrl).into(holder.productImage);

        //holder.mView.setTag(items.get(position).getId());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Flag", "onClick all row");
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView productImage;
        public final TextView productPrice;
        public final TextView productName;

        public Product mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            productImage = (ImageView) view.findViewById(R.id.product_image);
            productPrice = (TextView) view.findViewById(R.id.product_price);
            productName = (TextView) view.findViewById(R.id.product_name);

        }
    }
}
