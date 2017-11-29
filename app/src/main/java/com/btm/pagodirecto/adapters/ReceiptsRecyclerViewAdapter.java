package com.btm.pagodirecto.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.btm.pagodirecto.R;
import com.btm.pagodirecto.dto.Receipt;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by pedrodiaz on 11/13/17.
 */

public class ReceiptsRecyclerViewAdapter extends RecyclerView.Adapter<ReceiptsRecyclerViewAdapter.ViewHolder> {

    private final Context ctx;
    private  ArrayList<Receipt> items;
    private LayoutInflater inflater;
    private final ReceiptsRecyclerViewAdapter.OnItemClickListener listener;
    private int mViewType;

    public ReceiptsRecyclerViewAdapter(Context ctx, ArrayList<Receipt> items, int viewType, ReceiptsRecyclerViewAdapter.OnItemClickListener listener) {

        this.ctx = ctx;
        inflater = (LayoutInflater) this.ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.items = items;
        this.listener = listener;
        this.mViewType= viewType;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        int layout = -1;
        Double height = new Double(parent.getMeasuredHeight());
        int width = parent.getMeasuredWidth() ;
        switch (mViewType) {
            case 0:
                layout = R.layout.list_item_pending;
                height= new Double(parent.getMeasuredHeight() / 4);
                width = parent.getMeasuredWidth() / 1;
                break;
            case 1:
                layout = R.layout.grid_item_user;
                height = new Double(parent.getMeasuredHeight() / 2.25);
                 width = parent.getMeasuredWidth() / 2;
                break;
        }
        View view = LayoutInflater.from(parent.getContext())
                .inflate(layout, parent, false);


        view.setLayoutParams(new RecyclerView.LayoutParams(width, height.intValue()));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = items.get(position);


        if(mViewType == 0){

            if(items.get(position).getType().equalsIgnoreCase("order")){
                String uri = "@drawable/receipt_order";
                int imageResource = ctx.getResources().getIdentifier(uri, null, ctx.getPackageName());
                holder.receiptTitle.setText(items.get(position).getName());
                holder.receiptDescription.setText(items.get(position).getDescription());
                holder.receiptImage.setImageDrawable(ctx.getResources().getDrawable(imageResource));

            }

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(position,0);
                }
            });

        }

        if(mViewType == 1){
            GlideUrl glideUrl = new GlideUrl(items.get(position).getPhoto_url(), new LazyHeaders.Builder()
                    .build());
            holder.userName.setText(items.get(position).getName());
            Glide.with(ctx).load(glideUrl).into(holder.userImage).onLoadFailed(ctx.getResources().getDrawable(R.drawable.logo));
            holder.userBtn.setText("COBRAR");
            holder.userBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(position,0);
                }
            });

        }






    }

    public interface OnItemClickListener {
        void onItemClick(int i,int action);
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView receiptImage;
        public final TextView receiptTitle;
        public final TextView receiptDescription;

        public Receipt mItem;


        public final CircleImageView userImage;
        public final TextView userName;
        public final Button userBtn;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            receiptImage = (ImageView) view.findViewById(R.id.pending_image);
            receiptTitle = (TextView) view.findViewById(R.id.pending_title);
            receiptDescription = (TextView) view.findViewById(R.id.pending_description);

            userImage = (CircleImageView) view.findViewById(R.id.user_image);
            userName = (TextView) view.findViewById(R.id.user_name);
            userBtn = (Button) view.findViewById(R.id.btn_pay);
        }
    }
}

