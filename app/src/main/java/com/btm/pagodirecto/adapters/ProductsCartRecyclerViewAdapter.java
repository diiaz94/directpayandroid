package com.btm.pagodirecto.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
 * Created by Pedro on 10/11/2017.
 */

public class ProductsCartRecyclerViewAdapter extends RecyclerView.Adapter<ProductsCartRecyclerViewAdapter.ViewHolder>  {
    private final Context ctx;
    private  ArrayList<Product> items;
    private LayoutInflater inflater;
    private final ProductsCartRecyclerViewAdapter.OnItemClickListener listener;

    public ProductsCartRecyclerViewAdapter(Context ctx, ArrayList<Product> items, ProductsCartRecyclerViewAdapter.OnItemClickListener listener) {
        this.ctx = ctx;
        inflater = (LayoutInflater) this.ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.items = items;
        this.listener = listener;
    }

    @Override
    public ProductsCartRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.car_item, parent, false);


        Double height = (new Double(parent.getMeasuredHeight() / 4));
        int width = parent.getMeasuredWidth() / 1;

        view.setLayoutParams(new RecyclerView.LayoutParams(width, height.intValue()));
        return new ProductsCartRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductsCartRecyclerViewAdapter.ViewHolder holder, final int position) {
        holder.mItem = items.get(position);

        GlideUrl glideUrl = new GlideUrl(items.get(position).getPhoto_url(), new LazyHeaders.Builder()
                .build());
        holder.itemTitle.setText(items.get(position).getName());
        holder.itemPrice.setText("Bs. "+String.valueOf(Double.valueOf(items.get(position).getPrice())*items.get(position).getCartQty()));
        holder.itemQty.setText(String.valueOf(items.get(position).getCartQty()));

        MultiTransformation multi = new MultiTransformation(
                new RoundedCornersTransformation(50, 0, RoundedCornersTransformation.CornerType.TOP_LEFT),
                new RoundedCornersTransformation(50, 0, RoundedCornersTransformation.CornerType.BOTTOM_LEFT));

        Glide.with(ctx).load(glideUrl).apply(bitmapTransform(multi)).into(holder.itemImage);

        if(items.get(position).getCartQty()>1){
            holder.defaultView.setVisibility(View.VISIBLE);
            holder.deleteView.setVisibility(View.GONE);
        }else{
            holder.defaultView.setVisibility(View.GONE);
            holder.deleteView.setVisibility(View.VISIBLE);
        }
        //holder.mView.setTag(items.get(position).getId());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Long id = (Long) v.getTag();
                // Util.replaceFragment(((BaseActivity)ctx).getSupportFragmentManager(), PromotionDetailFragment.newInstance(id,false),R.id.fragment_container);
            }
        });

        holder.btnInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(position,0);
            }
        });
        holder.btnDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(position,1);
            }
        });
        holder.btnInc2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(position,0);
            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(position,2);
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int i,int action);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView itemImage;
        public final TextView itemTitle;
        public final TextView itemPrice;
        public final TextView itemQty;
        public final LinearLayout btnInc;
        public final LinearLayout btnInc2;
        public final LinearLayout btnDec;
        public final Button btnDelete;
        public final LinearLayout defaultView;
        public final LinearLayout deleteView;

        public Product mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            itemImage = (ImageView) view.findViewById(R.id.item_image);
            itemTitle = (TextView) view.findViewById(R.id.item_title);
            itemPrice = (TextView) view.findViewById(R.id.item_price);
            itemQty = (TextView) view.findViewById(R.id.item_qty);
            btnInc = (LinearLayout) view.findViewById(R.id.item_inc);
            btnInc2 = (LinearLayout) view.findViewById(R.id.item_inc2);
            btnDec = (LinearLayout) view.findViewById(R.id.item_dec);
            btnDelete = (Button) view.findViewById(R.id.item_delete);
            defaultView = (LinearLayout) view.findViewById(R.id.default_view);
            deleteView = (LinearLayout) view.findViewById(R.id.delete_view);
        }
    }
}


