package com.btm.pagodirecto.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.btm.pagodirecto.R;
import com.btm.pagodirecto.dto.Product;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * Created by Pedro on 4/10/2017.
 */

public class ProductsRecyclerViewAdapter extends RecyclerView.Adapter<ProductsRecyclerViewAdapter.ViewHolder> {

    private final Context ctx;
    private  ArrayList<Product> items;
    private LayoutInflater inflater;
    public ProductsRecyclerViewAdapter(Context ctx, ArrayList<Product> items) {
        this.ctx = ctx;
        inflater = (LayoutInflater) this.ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.items = items;
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
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = items.get(position);

        GlideUrl glideUrl = new GlideUrl(items.get(position).getPhoto_url(), new LazyHeaders.Builder()
                .build());
        holder.productPrice.setText("Bs. "+items.get(position).getPrice());
        holder.productRating.setText(items.get(position).getRating());
        holder.productName.setText(items.get(position).getName());

        MultiTransformation multi = new MultiTransformation(
                new RoundedCornersTransformation(100, 0, RoundedCornersTransformation.CornerType.TOP_RIGHT),
                new RoundedCornersTransformation(100, 0, RoundedCornersTransformation.CornerType.BOTTOM_LEFT));

        Glide.with(ctx).load(glideUrl).apply(bitmapTransform(multi)).into(holder.productImage);

        //holder.mView.setTag(items.get(position).getId());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Long id = (Long) v.getTag();
                // Util.replaceFragment(((BaseActivity)ctx).getSupportFragmentManager(), PromotionDetailFragment.newInstance(id,false),R.id.fragment_container);
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
        public final TextView productRating;
        public final TextView productName;

        public Product mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            productImage = (ImageView) view.findViewById(R.id.product_image);
            productPrice = (TextView) view.findViewById(R.id.product_price);
            productRating = (TextView) view.findViewById(R.id.product_rating);
            productName = (TextView) view.findViewById(R.id.product_name);


        }
    }
}
