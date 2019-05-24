package com.example.root.relicstest.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.root.relicstest.Activity.RelicsTransferActivity;
import com.example.root.relicstest.Entities.Relics;
import com.example.root.relicstest.R;
import com.example.root.relicstest.Utils.ConstantUtils;

import java.util.List;

public class RelicsAdapter extends RecyclerView.Adapter<RelicsAdapter.ViewHolder> {

    private Context mContext;
    private List<Relics> mRelicsList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView iv_relicsImage;
        TextView tv_name;
        TextView tv_price;


        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            iv_relicsImage = view.findViewById(R.id.iv_rvrelics);
            tv_name = view.findViewById(R.id.tv_rvname);
            tv_price = view.findViewById(R.id.tv_rvvalue);
        }
    }

    public RelicsAdapter(List<Relics> relicsList){
        this.mRelicsList = relicsList;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if(mContext == null){
            mContext = viewGroup.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.relics_item,viewGroup,false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = viewHolder.getAdapterPosition();
                Relics relics = mRelicsList.get(pos);
                Intent intent = new Intent(mContext,RelicsTransferActivity.class);
                intent.putExtra("id",relics.getId());
                mContext.startActivity(intent);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Relics relics = mRelicsList.get(position);
        viewHolder.tv_name.setText(relics.getName());
        viewHolder.tv_price.setText(relics.getValue().toString());
        Glide.with(mContext).load(ConstantUtils.BaseIMGURL + relics.getPhoto()).into(viewHolder.iv_relicsImage);
    }




    @Override
    public int getItemCount() {
        return mRelicsList.size();
    }
}
