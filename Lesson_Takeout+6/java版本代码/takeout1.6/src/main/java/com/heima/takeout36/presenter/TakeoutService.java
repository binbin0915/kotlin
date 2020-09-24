package com.heima.takeout36.presenter;

import com.heima.takeout36.model.net.ResponseInfo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 把所有接口都放到这个类中（首页、订单、登录）
 */
public interface TakeoutService {

    @GET("home")
    Call<ResponseInfo> getHomeInfo();

    @GET("login")
    Call<ResponseInfo> loginbyPhone(@Query("phone") String phone, @Query("type") int type);

    @GET("order")
    Call<ResponseInfo> getOrderList(@Query("userId") String userId);

    @GET("business")
    Call<ResponseInfo> loadBusinessInfo(@Query("sellerId") int sellerId);

}
