package com.qryl.qryl.adapter;

import android.content.Context;
import android.provider.Contacts;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qryl.qryl.R;
import com.qryl.qryl.VO.HgPersonVO.Data;
import com.qryl.qryl.util.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp on 2017/9/5.
 */

public class HlAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Data> datas = new ArrayList<>();

    public HlAdapter(List<Data> list) {
        datas = list;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout rlRootItem;
        TextView tvNameItem;
        TextView tvGenderItem;
        TextView tvExperienceItem;
        TextView tvAgeItem;
        TextView tvStarItem;

        public ViewHolder(View itemView) {
            super(itemView);
            rlRootItem = (RelativeLayout) itemView;
            tvNameItem = (TextView) itemView.findViewById(R.id.tv_name_item);
            tvGenderItem = (TextView) itemView.findViewById(R.id.tv_gender_item);
            tvExperienceItem = (TextView) itemView.findViewById(R.id.tv_experience_item);
            tvAgeItem = (TextView) itemView.findViewById(R.id.tv_age_item);
            tvStarItem = (TextView) itemView.findViewById(R.id.tv_star_item);
        }
    }

    @Override
    public HlAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(UIUtils.getContext()).inflate(R.layout.item_hl, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //修改内容
    }


    @Override
    public int getItemCount() {
        return datas.size();
    }
}
