package com.heima.takeout36.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.heima.takeout36.model.net.ResponseInfo;
import com.heima.takeout36.model.net.Seller;
import com.heima.takeout36.view.fragment.HomeFragment;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;
import retrofit2.Call;

/**
 * 获取服务器数据业务
 */

public class HomeFragmentPresenter extends NetPresenter {

    private HomeFragment mHomeFragment;

    public HomeFragmentPresenter(HomeFragment homeFragment) {
        mHomeFragment = homeFragment;
    }

    public void loadHomeInfo() {
        Call<ResponseInfo> homeCall = mTakeoutService.getHomeInfo();
        homeCall.enqueue(mCallback);
    }


    @Override
    protected void onConnectError(String message) {
        mHomeFragment.onHomeError(message);
    }

    @Override
    protected void onServerBug(int code) {
        mHomeFragment.onHomeServerBug(code);
    }

    @Override
    protected void onSuccess(String jsonStr) {
        //TODO:解析数据
        Gson gson = new Gson();
        //抽取部分数据去解析？
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            String nearby = jsonObject.getString("nearbySellerList");
            List<Seller> nearbySellerList = gson.fromJson(nearby, new TypeToken<List<Seller>>(){}.getType());

            String other = jsonObject.getString("otherSellerList");
            List<Seller> otherSellerList = gson.fromJson(other, new TypeToken<List<Seller>>(){}.getType());
            //TODO:业务已完成，让view层刷新

            mHomeFragment.onHomeSuccess(nearbySellerList, otherSellerList);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
