package com.btm.pagodirecto.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.btm.pagodirecto.R;
import com.btm.pagodirecto.activities.ProductDetailActivity;
import com.btm.pagodirecto.activities.SellActivity;
import com.btm.pagodirecto.dto.User;
import com.btm.pagodirecto.util.Util;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Pedro on 4/10/2017.
 */

public class UsersRecyclerViewAdapter extends RecyclerView.Adapter<UsersRecyclerViewAdapter.ViewHolder> {

private final Context ctx;
private  ArrayList<User> items;
private LayoutInflater inflater;

    public UsersRecyclerViewAdapter(Context ctx, ArrayList<User> items) {

        this.ctx = ctx;
        inflater = (LayoutInflater) this.ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.items = items;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_item_user, parent, false);


        Double height = new Double(parent.getMeasuredHeight() / 2.5);
        int width = parent.getMeasuredWidth() / 2;

        view.setLayoutParams(new RecyclerView.LayoutParams(width, height.intValue()));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = items.get(position);

        GlideUrl glideUrl = new GlideUrl(items.get(position).getPhoto_url(), new LazyHeaders.Builder()
                .build());
        holder.userName.setText(items.get(position).getName());
        Glide.with(ctx).load(glideUrl).into(holder.userImage);



        holder.mView.setTag(items.get(position).getId());
        holder.mView.setOnClickListener(new View.OnClickListener() {
        @Override
            public void onClick(View v) {
                //Long id = (Long) v.getTag();
               // Util.replaceFragment(((BaseActivity)ctx).getSupportFragmentManager(), PromotionDetailFragment.newInstance(id,false),R.id.fragment_container);
            }
        });

        holder.btnSell.setTag(items.get(position).getId());
        holder.btnSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Open sell activity
                Util.goToActivitySlide(
                        Util.getActivity(),
                        SellActivity.class,
                        Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK
                );
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final CircleImageView userImage;
        public final TextView userName;
        public final Button btnSell;

        public User mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            userImage = (CircleImageView) view.findViewById(R.id.user_image);
            userName = (TextView) view.findViewById(R.id.user_name);
            btnSell = (Button) view.findViewById(R.id.btn_sell);
        }
    }
}