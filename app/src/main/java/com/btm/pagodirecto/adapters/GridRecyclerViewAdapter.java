package com.btm.pagodirecto.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.btm.pagodirecto.R;
import com.btm.pagodirecto.dto.User;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Pedro on 4/10/2017.
 */

public class GridRecyclerViewAdapter extends RecyclerView.Adapter<GridRecyclerViewAdapter.ViewHolder> {

private final Context ctx;
private  ArrayList<User> items;
private LayoutInflater inflater;
public GridRecyclerViewAdapter(Context ctx, ArrayList<User> items) {
        this.ctx = ctx;
        inflater = (LayoutInflater) this.ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.items = items;
        }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_item_user, parent, false);


        Double height = (new Double(parent.getMeasuredHeight() / 2.5));
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
        }

@Override
public int getItemCount() {
        return items.size();
        }

public class ViewHolder extends RecyclerView.ViewHolder {
    public final View mView;
    public final CircleImageView userImage;
    public final TextView userName;

    public User mItem;

    public ViewHolder(View view) {
        super(view);
        mView = view;
        userImage = (CircleImageView) view.findViewById(R.id.user_image);
        userName = (TextView) view.findViewById(R.id.user_name);
    }
}
}
