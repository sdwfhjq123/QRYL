package com.qryl.qryl.adapter;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qryl.qryl.R;
import com.qryl.qryl.VO.ServiceVO.ItemList;
import com.qryl.qryl.util.ConstantValue;
import com.qryl.qryl.util.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yinhao on 2017/9/5.
 */

public class XzServiceAdapter extends RecyclerView.Adapter<XzServiceAdapter.ViewHolder> {

    private List<ItemList> datas = new ArrayList<>();

    public XzServiceAdapter(List<ItemList> list) {
        datas = list;
    }

    public void setData(List<ItemList> datas) {
        this.datas = datas;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout cardView;
        ImageView ivServiceItem;
        TextView tvTitleItem;
        TextView tvInfoItem;

        ViewHolder(View itemView) {
            super(itemView);
            cardView = (RelativeLayout) itemView;
            ivServiceItem = (ImageView) itemView.findViewById(R.id.iv_service_item);
            tvTitleItem = (TextView) itemView.findViewById(R.id.tv_title_item);
            tvInfoItem = (TextView) itemView.findViewById(R.id.tv_info_item);
        }
    }

    @Override
    public XzServiceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(UIUtils.getContext()).inflate(R.layout.item_service, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final XzServiceAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        //修改内容
        holder.tvTitleItem.setText(datas.get(position).getName());
        Glide.with(UIUtils.getContext()).load(ConstantValue.URL + datas.get(position).getHeadshotImg()).into(holder.ivServiceItem);
        holder.tvInfoItem.setText(datas.get(position).getContent());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //int position = holder.getAdapterPosition();
                //int id = datas.get(position).getId();
                onItemClickListener.onItemClick(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
