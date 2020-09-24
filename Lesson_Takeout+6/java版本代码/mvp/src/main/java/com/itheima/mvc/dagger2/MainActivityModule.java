package com.itheima.mvc.dagger2;

import com.itheima.mvc.MainActivity;
import com.itheima.mvc.MainActivityPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * 本质还是创建一个MainActivityPresenter
 */

@Module
public class MainActivityModule {

    private MainActivity mMainActivity;

    public MainActivityModule(MainActivity mainActivity) {
        mMainActivity = mainActivity;
    }

    @Provides
    public MainActivityPresenter providerMainActivityPresenter(){
        return new MainActivityPresenter(mMainActivity);
    }
}
