package com.qryl.qryl.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qryl.qryl.R;
import com.qryl.qryl.util.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yinhao on 2017/9/5.
 */

public class XzServerAdapter extends RecyclerView.Adapter<XzServerAdapter.ViewHolder> {

    private List<String> stringList = new ArrayList<>();

    public XzServerAdapter(List<String> list) {
        stringList = list;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout rlRootItem;
        ImageView ivHeadItem;
        TextView tvNameItem;
        TextView tvExperienceItem;
        TextView tvBeGoodAtItem;
        TextView tvProfessionItem;

        public ViewHolder(View itemView) {
            super(itemView);
            rlRootItem = (RelativeLayout) itemView;
            ivHeadItem = (ImageView) itemView.findViewById(R.id.iv_head_item);
            tvNameItem = (TextView) itemView.findViewById(R.id.tv_name_item);
            tvExperienceItem = (TextView) itemView.findViewById(R.id.tv_experience_item);
            tvBeGoodAtItem = (TextView) itemView.findViewById(R.id.tv_be_good_at_item);
            tvProfessionItem = (TextView) itemView.findViewById(R.id.tv_profession_item);
            //itemView.findViewById(R.id.tv_sign_item);//是否签到的imageview
        }
    }

    @Override
    public XzServerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(UIUtils.getContext()).inflate(R.layout.item_server, null);
        final ViewHolder holder = new ViewHolder(view);
        holder.rlRootItem.setOnClickListener(new View.OnClickListener() {
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
    public void onBindViewHolder(XzServerAdapter.ViewHolder holder, int position) {
        //修改内容
        String s = stringList.get(position);
        holder.tvExperienceItem.setText(s);
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }
}
