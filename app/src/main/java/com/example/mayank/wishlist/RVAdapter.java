package com.example.mayank.wishlist;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Mayank on 27-01-2016.
 */
public class RVAdapter extends RecyclerView.Adapter<RVAdapter.DealsViewHolder> {

    public static class DealsViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView product_name_tv;
        TextView website_tv;
        TextView price_tv;
        TextView discount_tv;
        TextView delivery_date_tv;

        DealsViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            product_name_tv = (TextView) itemView.findViewById(R.id.product_name);
            website_tv = (TextView) itemView.findViewById(R.id.website);
            price_tv = (TextView) itemView.findViewById(R.id.price);
            discount_tv = (TextView) itemView.findViewById(R.id.discount);
            delivery_date_tv = (TextView) itemView.findViewById(R.id.delivery_date);
        }

    }

    List<Deal> deals;

    RVAdapter(List<Deal> deals) {
        this.deals = deals;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public DealsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card, viewGroup, false);
        DealsViewHolder dvh = new DealsViewHolder(v);
        return dvh;
    }

    @Override
    public void onBindViewHolder(DealsViewHolder dealsViewHolder, int i) {
        dealsViewHolder.product_name_tv.setText(deals.get(i).product_name);
        dealsViewHolder.website_tv.setText(deals.get(i).website);
        dealsViewHolder.price_tv.setText(deals.get(i).price);
        dealsViewHolder.discount_tv.setText(deals.get(i).discount);
        dealsViewHolder.delivery_date_tv.setText(deals.get(i).delivery_date);
    }

    @Override
    public int getItemCount() {
        return deals.size();
    }


}
