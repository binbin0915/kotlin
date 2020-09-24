package com.heima.takeout36.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.heima.takeout36.model.net.Order;
import com.heima.takeout36.model.net.ResponseInfo;
import com.heima.takeout36.view.fragment.OrderFragment;

import java.util.List;

import retrofit2.Call;

/**
 * Created by lidongzhi on 2017/6/6.
 */

public class OrderFragmentPresenter extends NetPresenter {

    private OrderFragment mOrderFragment;

    public OrderFragmentPresenter(OrderFragment orderFragment) {
        mOrderFragment = orderFragment;
    }

    public void getOrderList(String userId){
        Call<ResponseInfo> orderList = mTakeoutService.getOrderList(userId);
        orderList.enqueue(mCallback);
    }

    @Override
    protected void onConnectError(String message) {

    }

    @Override
    protected void onServerBug(int code) {

    }

    @Override
    protected void onSuccess(String jsonData) {
        Gson gson = new Gson();
        List<Order> orderList = gson.fromJson(jsonData, new TypeToken<List<Order>>(){}.getType());

        mOrderFragment.onLoadOrderSuccess(orderList);
    }
}
