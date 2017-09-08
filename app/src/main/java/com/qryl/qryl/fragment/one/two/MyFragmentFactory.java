package com.qryl.qryl.fragment.one.two;

import com.qryl.qryl.fragment.one.two.three.MyServerFragment;
import com.qryl.qryl.fragment.one.two.three.MyServiceFragment;

import java.util.HashMap;

/**
 * Created by hp on 2017/8/21.
 * tab标签的切换点单派单工厂类----优化代码
 */

public class MyFragmentFactory {

    private static HashMap<Integer, BaseFragment> mBaseFragments = new HashMap<Integer, BaseFragment>();


    public static BaseFragment createFragment(int pos) {

        BaseFragment baseFragment = mBaseFragments.get(pos);

        if (baseFragment == null) {
            switch (pos) {
                case 0:
                    baseFragment = new MyServiceFragment();//服务项目
                    break;
                case 1:
                    baseFragment = new MyServerFragment();//服务人员
                    break;
            }

            mBaseFragments.put(pos, baseFragment);
        }
        return baseFragment;
    }
}
