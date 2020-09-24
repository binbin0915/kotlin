package com.heima.takeout36.view.fragment;

import android.animation.ArgbEvaluator;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.heima.takeout36.R;
import com.heima.takeout36.model.net.Seller;
import com.heima.takeout36.presenter.HomeFragmentPresenter;
import com.heima.takeout36.view.adapter.HomeRVAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lidongzhi on 2017/6/5.
 */

public class HomeFragment extends Fragment {

    @Bind(R.id.rv_home)
    RecyclerView mRvHome;
    @Bind(R.id.home_tv_address)
    TextView mHomeTvAddress;
    @Bind(R.id.ll_title_search)
    LinearLayout mLlTitleSearch;
    @Bind(R.id.ll_title_container)
    LinearLayout mLlTitleContainer;
    private HomeRVAdapter mHomeRVAdapter;
    private HomeFragmentPresenter mHomeFragmentPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = View.inflate(getActivity(), R.layout.fragment_home, null);
        ButterKnife.bind(this, rootView);
        mHomeFragmentPresenter = new HomeFragmentPresenter(this);
        mRvHome.setLayoutManager(new LinearLayoutManager(getActivity()));  //默认像listview一样上下滚动
        mHomeRVAdapter = new HomeRVAdapter(getActivity());
        mRvHome.setAdapter(mHomeRVAdapter);
        return rootView;
    }

    int sumY = 0;
    float distance = 150;  //最远滚动距离，超过这个距离还是最蓝色
    int startColor = 0x553190E8;
    int endColor = 0xff3190E8;
    ArgbEvaluator mEvaluator = new ArgbEvaluator();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        testData();
        mHomeFragmentPresenter.loadHomeInfo();

        //有数据以后才设置滚动事件
        mRvHome.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //差值
//                Log.e("home", dy + "");
                sumY += dy;
                int bgColor;
                if (sumY < 0) {
                    //初始最浅的蓝色0x553190E8
                    bgColor = startColor;
                } else if (sumY > distance) {
                    //最蓝的颜色0xff3190E8
                    bgColor = endColor;
                } else {
                    //在0-distance范围内计算alpha的值  255
                    bgColor = (int) mEvaluator.evaluate(sumY / distance, startColor, endColor);
                }
                mLlTitleContainer.setBackgroundColor(bgColor);
            }

        });
    }

    private List<String> mDatas = new ArrayList<>();

    private void testData() {
        for (int i = 0; i < 100; i++) {
            mDatas.add("我是模拟条目" + i);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public void onHomeError(String message) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), "压根没连上网", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void onHomeServerBug(int code) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), "服务器代码错误", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void onHomeSuccess(List<Seller> nearbySellerList, List<Seller> otherSellerList) {
        mHomeRVAdapter.setData(nearbySellerList, otherSellerList);
    }
}
