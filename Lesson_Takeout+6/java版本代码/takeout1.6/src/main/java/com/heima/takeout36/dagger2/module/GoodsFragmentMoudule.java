package com.heima.takeout36.dagger2.module;

import com.heima.takeout36.presenter.GoodsFragmentPresenter;
import com.heima.takeout36.view.fragment.GoodsFragment;

import dagger.Module;
import dagger.Provides;

/**
 * Created by lidongzhi on 2017/6/9.
 */
@Module
public class GoodsFragmentMoudule {
    GoodsFragment mGoodsFragment;

    public GoodsFragmentMoudule(GoodsFragment goodsFragment) {
        mGoodsFragment = goodsFragment;
    }

    @Provides
    GoodsFragmentPresenter providerGoodsFragmentPresenter() {
        return new GoodsFragmentPresenter(mGoodsFragment);
    }

}
