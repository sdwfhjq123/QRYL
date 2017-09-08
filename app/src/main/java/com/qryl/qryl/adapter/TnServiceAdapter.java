package com.qryl.qryl.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.qryl.qryl.R;
import com.qryl.qryl.util.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yinhao on 2017/9/5.
 */

public class TnServiceAdapter extends RecyclerView.Adapter<TnServiceAdapter.ViewHolder> {

    private List<String> stringList = new ArrayList<>();

    public TnServiceAdapter(List<String> list) {
        stringList = list;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        ImageView ivServiceItem;
        TextView tvTitleItem;
        TextView tvInfoItem;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            ivServiceItem = (ImageView) itemView.findViewById(R.id.iv_service_item);
            tvTitleItem = (TextView) itemView.findViewById(R.id.tv_title_item);
            tvInfoItem = (TextView) itemView.findViewById(R.id.tv_info_item);
            //cardView.setCardBackgroundColor(UIUtils.getContext().getResources().getColor(R.color.card_view_bg));
        }
    }

    @Override
    public TnServiceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(UIUtils.getContext()).inflate(R.layout.item_service, null);
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                String s = stringList.get(position);
                Toast.makeText(UIUtils.getContext(), "点击了第s个条目 position: " + position, Toast.LENGTH_SHORT).show();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(TnServiceAdapter.ViewHolder holder, int position) {
        //修改内容
        String s = stringList.get(position);
        holder.tvTitleItem.setText(s);
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }
}
