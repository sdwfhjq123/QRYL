package com.qryl.qryl.fragment.one.two;

import com.qryl.qryl.fragment.one.two.three.TnServerFragment;
import com.qryl.qryl.fragment.one.two.three.TnServiceFragment;

import java.util.HashMap;

/**
 * Created by hp on 2017/8/21.
 * tab标签的切换点单派单工厂类----优化代码
 */

public class TnFragmentFactory {

    private static HashMap<Integer, BaseFragment> mBaseFragments = new HashMap<Integer, BaseFragment>();


    public static BaseFragment createFragment(int pos) {

        BaseFragment baseFragment = mBaseFragments.get(pos);

        if (baseFragment == null) {
            switch (pos) {
                case 0:
                    baseFragment = new TnServiceFragment();//服务项目
                    break;
                case 1:
                    baseFragment = new TnServerFragment();//服务人员
                    break;
            }

            mBaseFragments.put(pos, baseFragment);
        }
        return baseFragment;
    }
}
