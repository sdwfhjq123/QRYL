package com.qryl.qryl.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.qryl.qryl.VO.Picture;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp on 2017/8/16.
 */

public class RollPagerAdapter extends StaticPagerAdapter {
    private List<Picture> data = new ArrayList<>();

    public RollPagerAdapter(List<Picture> mImgList) {
        this.data = mImgList;
    }

    @Override
    public View getView(ViewGroup container, int position) {
        ImageView view = new ImageView(container.getContext());
        Glide.with(container.getContext()).load(data.get(position).getImgUrl()).into(view);
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
}
