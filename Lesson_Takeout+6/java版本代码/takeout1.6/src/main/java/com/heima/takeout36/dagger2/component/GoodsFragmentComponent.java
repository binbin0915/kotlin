package com.heima.takeout36.dagger2.component;

import com.heima.takeout36.dagger2.module.GoodsFragmentMoudule;
import com.heima.takeout36.view.fragment.GoodsFragment;

import dagger.Component;

/**
 * Created by lidongzhi on 2017/6/9.
 */
@Component(modules = GoodsFragmentMoudule.class)
public interface GoodsFragmentComponent {

    void in(GoodsFragment goodsFragment);
}
