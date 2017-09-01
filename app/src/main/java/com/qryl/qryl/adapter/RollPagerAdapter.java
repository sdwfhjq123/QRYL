package com.qryl.qryl.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jude.rollviewpager.adapter.StaticPagerAdapter;

import java.util.ArrayList;

/**
 * Created by hp on 2017/8/16.
 */

public class RollPagerAdapter extends StaticPagerAdapter {
    private ArrayList<Integer> mImgList = new ArrayList<>();

    public RollPagerAdapter(ArrayList<Integer> mImgList) {
        this.mImgList = mImgList;
    }

    @Override
    public View getView(ViewGroup container, int position) {
        ImageView view = new ImageView(container.getContext());
        view.setImageResource(mImgList.get(position));
        view.setScaleType(ImageView.ScaleType.CENTER_CROP);//按比例扩大图片的size居中显示，使得图片长(宽)等于或大于View的长(宽)
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return view;
    }

    @Override
    public int getCount() {
        return mImgList.size();
    }
}
