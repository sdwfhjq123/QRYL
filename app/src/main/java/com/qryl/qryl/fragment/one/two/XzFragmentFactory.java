package com.qryl.qryl.fragment.one.two;

import com.qryl.qryl.fragment.one.two.three.XzServerFragment;
import com.qryl.qryl.fragment.one.two.three.XzServiceFragment;

import java.util.HashMap;

/**
 * Created by hp on 2017/8/21.
 * tab标签的切换点单派单工厂类----优化代码
 */

public class XzFragmentFactory {

    private static HashMap<Integer, BaseFragment> mBaseFragments = new HashMap<Integer, BaseFragment>();


    static BaseFragment createFragment(int pos) {

        BaseFragment baseFragment = mBaseFragments.get(pos);

        if (baseFragment == null) {
            switch (pos) {
                case 0:
                    baseFragment = new XzServiceFragment();//服务项目
                    break;
                case 1:
                    baseFragment = new XzServerFragment();//服务人员
                    break;
            }

            mBaseFragments.put(pos, baseFragment);
        }
        return baseFragment;
    }
}
