package com.heima.takeout36.presenter;

import com.heima.takeout36.model.net.ResponseInfo;
import com.heima.takeout36.utils.Constant;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by lidongzhi on 2017/6/5.
 */

public abstract class NetPresenter {

    protected   Retrofit mRetrofit;
    protected  TakeoutService mTakeoutService;

    public NetPresenter() {
        mRetrofit = new Retrofit.Builder().baseUrl(Constant.HOST).addConverterFactory(GsonConverterFactory.create()).build();
        mTakeoutService = mRetrofit.create(TakeoutService.class);
    }

    protected Callback mCallback = new Callback<ResponseInfo>() {
        @Override
        public void onResponse(Call<ResponseInfo> call, Response<ResponseInfo> response) {
            if(response.isSuccessful()){
                //返回了正确的数据
                ResponseInfo responseInfo = response.body();
                onSuccess(responseInfo.getData());
//                mHomeFragment.onHomeSuccess(responseInfo.getData());
            }else{
                //服务器代码出错了
                onServerBug(response.code());
//                mHomeFragment.onHomeServerBug(response.code());
            }
        }

        @Override
        public void onFailure(Call<ResponseInfo> call, Throwable t) {
            //没有连上服务器
            onConnectError(t.getMessage());
//            mHomeFragment.onHomeError(t.getMessage());
        }
    };

    protected abstract void onConnectError(String message);

    protected abstract void onServerBug(int code);

    protected abstract void onSuccess(String jsonData);
}
