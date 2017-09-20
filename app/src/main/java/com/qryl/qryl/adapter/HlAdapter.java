package com.qryl.qryl.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qryl.qryl.R;
import com.qryl.qryl.VO.DataArea;
import com.qryl.qryl.util.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp on 2017/9/5.
 */

public class HlAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<DataArea> datas = new ArrayList<>();
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    private OnItemClickListener onItemClickListener;

    public void setData(List<DataArea> data) {
        this.datas = data;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public HlAdapter(List<DataArea> data) {
        datas = data;
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(UIUtils.getContext()).inflate(R.layout.item_hl, parent, false);
            return new ItemViewHolder(view);
        } else if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(UIUtils.getContext()).inflate(R.layout.item_footer_view, parent, false);
            return new FootViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        //修改内容
        if (holder instanceof ItemViewHolder) {
            ((ItemViewHolder) holder).tvAgeItem.setText(datas.get(position).getAge());
            ((ItemViewHolder) holder).tvExperienceItem.setText(datas.get(position).getWorkYears());
            if (datas.get(position).getGender() == 0) {
                ((ItemViewHolder) holder).tvGenderItem.setText("男");
            } else if (datas.get(position).getGender() == 1) {
                ((ItemViewHolder) holder).tvGenderItem.setText("女");
            }
            ((ItemViewHolder) holder).tvNameItem.setText(datas.get(position).getRealName());
            ((ItemViewHolder) holder).tvStarItem.setText("★★★★★");
            int id = datas.get(position).getId();
            if (onItemClickListener != null) {
                ((ItemViewHolder) holder).rlRootItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //int layoutPosition = holder.getLayoutPosition();
                        onItemClickListener.onItemClick(((ItemViewHolder) holder).rlRootItem, position);
                    }
                });
            }
        }
    }


    @Override
    public int getItemCount() {
        return datas.size() == 0 ? 0 : datas.size() + 1;
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout rlRootItem;
        TextView tvNameItem;
        TextView tvGenderItem;
        TextView tvExperienceItem;
        TextView tvAgeItem;
        TextView tvStarItem;

        public ItemViewHolder(View itemView) {
            super(itemView);
            rlRootItem = (RelativeLayout) itemView;
            tvNameItem = (TextView) itemView.findViewById(R.id.tv_name_item);
            tvGenderItem = (TextView) itemView.findViewById(R.id.tv_gender_item);
            tvExperienceItem = (TextView) itemView.findViewById(R.id.tv_experience_item);
            tvAgeItem = (TextView) itemView.findViewById(R.id.tv_age_item);
            tvStarItem = (TextView) itemView.findViewById(R.id.tv_star_item);
        }
    }

    static class FootViewHolder extends RecyclerView.ViewHolder {

        public FootViewHolder(View itemView) {
            super(itemView);
        }
    }

}
