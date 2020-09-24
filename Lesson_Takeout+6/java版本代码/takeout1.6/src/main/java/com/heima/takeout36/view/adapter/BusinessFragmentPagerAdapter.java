package com.heima.takeout36.view.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v13.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by lidongzhi on 2017/6/8.
 */
public class BusinessFragmentPagerAdapter extends FragmentPagerAdapter {

    String[] mTitles = new String[]{"商品", "评论", "商家"};

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }

    private List<Fragment> mFragmentList = new ArrayList<>();

    public void setFragmentList(List<Fragment> fragmentList) {
        mFragmentList = fragmentList;
    }

    private Context mContext;

    public BusinessFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    public BusinessFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (mFragmentList != null) {
            return mFragmentList.get(position);
        }
        return null;
    }

    @Override
    public int getCount() {
        if (mFragmentList != null) {
            return mFragmentList.size();
        }
        return 0;
    }
}
