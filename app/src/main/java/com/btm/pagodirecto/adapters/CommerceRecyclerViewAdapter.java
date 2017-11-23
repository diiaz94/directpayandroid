package com.btm.pagodirecto.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.btm.pagodirecto.R;
import com.btm.pagodirecto.activities.CommerceProductsActivity;
import com.btm.pagodirecto.dto.Commerce;
import com.btm.pagodirecto.util.Util;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;

import java.util.ArrayList;

/**
 * Created by edwinalvarado on 11/7/17.
 */

public class CommerceRecyclerViewAdapter extends RecyclerView.Adapter<CommerceRecyclerViewAdapter.ViewHolder> {


    private final Context ctx;
    private final OnItemClickListener listener;
    private ArrayList<Commerce> items;
    private LayoutInflater inflater;
    public CommerceRecyclerViewAdapter(Context ctx, ArrayList<Commerce> items,CommerceRecyclerViewAdapter.OnItemClickListener listener) {
        this.ctx = ctx;
        inflater = (LayoutInflater) this.ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.items = items;
        this.listener = listener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_store, parent, false);

        Double height = (new Double(parent.getMeasuredHeight() / 2.2));
        int width = parent.getMeasuredWidth() / 1;

        view.setLayoutParams(new RecyclerView.LayoutParams(width, height.intValue()));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CommerceRecyclerViewAdapter.ViewHolder holder, final int position) {
        holder.mItem = items.get(position);

        //set all values to row
        GlideUrl glideUrl = new GlideUrl(items.get(position).getPhoto_url_large(), new LazyHeaders.Builder()
                .build());
        holder.comerceTittle.setText(items.get(position).getName());
        holder.comerceDescription.setText(items.get(position).getDescription());

        Glide.with(ctx).load(glideUrl).into(holder.comerceImage);

        //holder.mView.setTag(items.get(position).getId());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(position,0);
               /* Log.i("FLAG", "onClick: main button");
                Util.saveInSharedPreferences("COMMERCE_NAME",items.get(position).getTitle());
                Util.goToActivitySlide(
                    Util.getActivity(),
                    CommerceProductsActivity.class);*/
                //Long id = (Long) v.getTag();
                // Util.replaceFragment(((BaseActivity)ctx).getSupportFragmentManager(), PromotionDetailFragment.newInstance(id,false),R.id.fragment_container);
            }
        });

        //Heart click
        holder.btnHeart.setTag(position);
        holder.btnHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Change heart Status
                if (items.get((Integer) v.getTag()).getFavorite()){
                    holder.btnHeart.setImageResource(R.drawable.unlike);
                    items.get((Integer) v.getTag()).setFavorite(false);
                }else{
                    holder.btnHeart.setImageResource(R.drawable.like);
                    items.get((Integer) v.getTag()).setFavorite(true);
                }
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
        public final ImageView comerceImage;
        public final TextView comerceTittle;
        public final TextView comerceDescription;
        public final TextView comerceDistance;
        public final ImageButton btnHeart;

        public Commerce mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            comerceImage = (ImageView) view.findViewById(R.id.commerce_image);
            comerceTittle = (TextView) view.findViewById(R.id.comerce_tittle);
            comerceDescription = (TextView) view.findViewById(R.id.commerce_description);
            comerceDistance = (TextView) view.findViewById(R.id.comerce_distance);
            btnHeart = (ImageButton) view.findViewById(R.id.heart_image);
        }
    }


}
