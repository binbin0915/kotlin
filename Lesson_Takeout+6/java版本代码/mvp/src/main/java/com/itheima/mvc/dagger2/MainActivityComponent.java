package com.itheima.mvc.dagger2;

import com.itheima.mvc.MainActivity;

import dagger.Component;

/**
 * Created by lidongzhi on 2017/6/5.
 */
@Component(modules = MainActivityModule.class)
public interface MainActivityComponent {
    //in方法是申请加入小组
    void in(MainActivity mainActivity);
}
