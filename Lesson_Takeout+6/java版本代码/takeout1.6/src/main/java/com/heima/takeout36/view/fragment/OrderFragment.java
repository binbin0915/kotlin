package com.heima.takeout36.view.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.heima.takeout36.R;
import com.heima.takeout36.model.net.Order;
import com.heima.takeout36.presenter.OrderFragmentPresenter;
import com.heima.takeout36.utils.TakeoutApp;
import com.heima.takeout36.view.adapter.OrderRvAdapter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lidongzhi on 2017/6/5.
 */

public class OrderFragment extends Fragment {

    @Bind(R.id.rv_order_list)
    RecyclerView mRvOrderList;
    @Bind(R.id.srl_order)
    SwipeRefreshLayout mSrlOrder;
    private OrderRvAdapter mOrderRvAdapter;
    private OrderFragmentPresenter mOrderFragmentPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = View.inflate(getActivity(), R.layout.fragment_order, null);
        ButterKnife.bind(this, rootView);
        mOrderFragmentPresenter = new OrderFragmentPresenter(this);
        mRvOrderList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mOrderRvAdapter = new OrderRvAdapter(getActivity());
        mRvOrderList.setAdapter(mOrderRvAdapter);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        int id = TakeoutApp.sUser.getId();
        if (id == -1) {
            Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT).show();
        } else {
            //请求服务器数据
            mOrderFragmentPresenter.getOrderList(id + "");
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public void onLoadOrderSuccess(List<Order> orderList) {
        mOrderRvAdapter.setOrderList(orderList);

        mSrlOrder.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //TODO:下拉松开后的回调，可以去加载最新数据
                int id = TakeoutApp.sUser.getId();

                //再次请求服务器数据
                mOrderFragmentPresenter.getOrderList(id + "");

                mSrlOrder.setRefreshing(false);  //关闭转圈圈
            }
        });
    }
}
