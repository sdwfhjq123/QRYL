package com.qryl.qryl.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.qryl.qryl.R;
import com.qryl.qryl.VO.XzVO.XzInfo;
import com.qryl.qryl.util.ConstantValue;
import com.qryl.qryl.util.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yinhao on 2017/9/5.
 */

public class XzServerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<XzInfo> datas = new ArrayList<>();
    private static final int TYPE_NORMAL = 0;
    private static final int TYPE_FOOTER = 1;

    public XzServerAdapter(List<XzInfo> list) {
        datas = list;
    }

    public void setData(List<XzInfo> data) {
        this.datas = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_NORMAL) {
            View view = LayoutInflater.from(UIUtils.getContext()).inflate(R.layout.item_server, parent, false);
            return new ItemViewHolder(view);
        } else if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(UIUtils.getContext()).inflate(R.layout.item_footer_view, parent, false);
            return new FooterViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ItemViewHolder) {
            Glide.with(UIUtils.getContext()).load(ConstantValue.URL + datas.get(position).getProfessionIds()).into(((ItemViewHolder) holder).ivHeadItem);
            ((ItemViewHolder) holder).tvNameItem.setText(datas.get(position).getRealName());
            ((ItemViewHolder) holder).tvExperienceItem.setText(datas.get(position).getWorkYears() + "");
            ((ItemViewHolder) holder).tvBeGoodAtItem.setText(datas.get(position).getProfessionNames());
            ((ItemViewHolder) holder).tvHospital.setText(datas.get(position).getHospitalName());
            ((ItemViewHolder) holder).tvDepartment.setText(datas.get(position).getDepartmentName());
            int roleType = datas.get(position).getRoleType();
            if (roleType == 0) {
                ((ItemViewHolder) holder).tvProfessionItem.setText("护士");
            } else if (roleType == 1) {
                ((ItemViewHolder) holder).tvProfessionItem.setText("医生");
            } else if (roleType == 2) {
                ((ItemViewHolder) holder).tvProfessionItem.setText("按摩师");
            }
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

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_NORMAL;
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout rlRootItem;
        ImageView ivHeadItem;
        TextView tvNameItem;
        TextView tvExperienceItem;
        TextView tvBeGoodAtItem;
        TextView tvProfessionItem;
        TextView tvHospital;
        TextView tvDepartment;

        public ItemViewHolder(View itemView) {
            super(itemView);
            rlRootItem = (RelativeLayout) itemView;
            ivHeadItem = (ImageView) itemView.findViewById(R.id.iv_head_item);
            tvNameItem = (TextView) itemView.findViewById(R.id.tv_name_item);
            tvExperienceItem = (TextView) itemView.findViewById(R.id.tv_experience_item);
            tvBeGoodAtItem = (TextView) itemView.findViewById(R.id.tv_be_good_at_item);
            tvProfessionItem = (TextView) itemView.findViewById(R.id.tv_profession);
            tvHospital = (TextView) itemView.findViewById(R.id.tv_hospital);
            tvDepartment = (TextView) itemView.findViewById(R.id.tv_department);
        }
    }


    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onDeleteItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
