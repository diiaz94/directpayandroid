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

    public ReceiptsRecyclerViewAdapter(Context ctx, ArrayList<Receipt> items, ReceiptsRecyclerViewAdapter.OnItemClickListener listener) {

        this.ctx = ctx;
        inflater = (LayoutInflater) this.ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.items = items;
        this.listener = listener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_pending, parent, false);


        Double height = new Double(parent.getMeasuredHeight() / 4);
        int width = parent.getMeasuredWidth() / 1;

        view.setLayoutParams(new RecyclerView.LayoutParams(width, height.intValue()));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = items.get(position);

        GlideUrl glideUrl = new GlideUrl(items.get(position).getPhoto_url(), new LazyHeaders.Builder()
                .build());
        holder.receiptTitle.setText(items.get(position).getName());
        holder.receiptDescription.setText(items.get(position).getDescription());
        Glide.with(ctx).load(glideUrl).into(holder.receiptImage);



        //holder.mView.setTag(items.get(position).getId());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             listener.onItemClick(position,0);
            }
        });

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

        public ViewHolder(View view) {
            super(view);
            mView = view;
            receiptImage = (ImageView) view.findViewById(R.id.pending_image);
            receiptTitle = (TextView) view.findViewById(R.id.pending_title);
            receiptDescription = (TextView) view.findViewById(R.id.pending_description);
        }
    }
}

