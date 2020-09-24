package com.heima.takeout36.view.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.heima.takeout36.R;
import com.heima.takeout36.view.fragment.HomeFragment;
import com.heima.takeout36.view.fragment.MoreFragment;
import com.heima.takeout36.view.fragment.OrderFragment;
import com.heima.takeout36.view.fragment.UserFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.main_fragment_container)
    FrameLayout mMainFragmentContainer;
    @Bind(R.id.main_bottome_switcher_container)
    LinearLayout mMainBottomeSwitcherContainer;

    //4个fragment，首页是头布局 + recycleview
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initFragments();
        initBottomBar();
        refreshUiByIndex(0);
    }

    List<Fragment> mFragmentList = new ArrayList<>();
    private void initFragments() {
        mFragmentList.add(new HomeFragment());
        mFragmentList.add(new OrderFragment());
        mFragmentList.add(new UserFragment());
        mFragmentList.add(new MoreFragment());
    }

    private void initBottomBar() {
        int childCount = mMainBottomeSwitcherContainer.getChildCount();
        for(int i=0;i<childCount;i++){
            final View child = mMainBottomeSwitcherContainer.getChildAt(i);
            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int selectIndex = mMainBottomeSwitcherContainer.indexOfChild(child);
                    refreshUiByIndex(selectIndex);
                }
            });
        }
    }

    private void refreshUiByIndex(int selectIndex) {
        //1.下面变成蓝色
        // TODO:(选中的禁用，未选中的启用）
        int childCount = mMainBottomeSwitcherContainer.getChildCount();
        for(int i=0;i<childCount;i++){
            final View child = mMainBottomeSwitcherContainer.getChildAt(i);
            if(i == selectIndex){
                //选中的禁用
//                child.setEnabled(false);
                setEnable(child, false);  //禁用它，如果它还有孩子，继续禁用
            }else{
                //未选中的启用
//                child.setEnabled(true);
                setEnable(child, true);
            }
        }


        //2.切换fragment
        getFragmentManager().beginTransaction().replace(R.id.main_fragment_container, mFragmentList.get(selectIndex)).commit();
    }

    private void setEnable(View child, boolean isEnable) {
        child.setEnabled(isEnable);

        if(child instanceof ViewGroup){
            ViewGroup viewGroup  = (ViewGroup) child;
            int childCount = viewGroup.getChildCount();
            for(int i=0;i<childCount;i++){
                View item = viewGroup.getChildAt(i);
                setEnable(item, isEnable);
            }
        }
    }
}
