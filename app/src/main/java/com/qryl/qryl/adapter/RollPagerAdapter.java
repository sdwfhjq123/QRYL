package com.qryl.qryl.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.qryl.qryl.VO.Picture;
import com.qryl.qryl.util.ConstantValue;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp on 2017/8/16.
 */

public class RollPagerAdapter extends StaticPagerAdapter {
    private List<Picture> data = new ArrayList<>();
    private int mChildCount = 0;

    public RollPagerAdapter(List<Picture> mImgList) {
        this.data = mImgList;
    }

    @Override
    public View getView(ViewGroup container, int position) {
        ImageView view = new ImageView(container.getContext());
        Glide.with(container.getContext()).load(ConstantValue.URL + data.get(position).getImgUrl()).into(view);
        //Glide.with(container.getContext()).load("http://192.168.2.134:8080/qryl/uploads/adver/1.jpg").into(view);
        view.setScaleType(ImageView.ScaleType.CENTER_CROP);//按比例扩大图片的size居中显示，使得图片长(宽)等于或大于View的长(宽)
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return view;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    public void setData(List<Picture> data) {
        this.data = data;
    }

    @Override
    public void notifyDataSetChanged() {
        mChildCount = getCount();
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        if (mChildCount > 0) {
            mChildCount--;
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }
}
