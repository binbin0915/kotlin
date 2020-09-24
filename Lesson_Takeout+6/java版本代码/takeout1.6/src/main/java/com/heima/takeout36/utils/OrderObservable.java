package com.heima.takeout36.utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

/**
 * 被观察者，数据源，用来存放被观察的数据
 */
public class OrderObservable extends Observable {

    private static OrderObservable sOrderObserver = new OrderObservable();
    private OrderObservable() {
    }

    public static OrderObservable getInstance(){
        return sOrderObserver;
    }

    /* 订单状态
       * 1 未支付 2 已提交订单 3 商家接单  4 配送中,等待送达 5已送达 6 取消的订单*/
    public static final String ORDERTYPE_UNPAYMENT = "10";
    public static final String ORDERTYPE_SUBMIT = "20";
    public static final String ORDERTYPE_RECEIVEORDER = "30";
    public static final String ORDERTYPE_DISTRIBUTION = "40";
    // 骑手状态：接单、取餐、送餐
    public static final String ORDERTYPE_DISTRIBUTION_RIDER_RECEIVE = "43";
    public static final String ORDERTYPE_DISTRIBUTION_RIDER_TAKE_MEAL = "46";
    public static final String ORDERTYPE_DISTRIBUTION_RIDER_GIVE_MEAL = "48";

    public static final String ORDERTYPE_SERVED = "50";
    public static final String ORDERTYPE_CANCELLEDORDER = "60";

    public void newMsgComing(String extra) {

        //TODO:处理消息,需要解析json，可以转换成map
        Map<String, String> data = processJson(extra);

        //TODO:通知所有的观察者，新消息来了，其中通知OrderRVAdapter
        setChanged();  //告诉所有观察者有变化
        notifyObservers(data);  //变化了哪些内容
    }

    private Map<String,String> processJson(String extra) {
        Map<String,String> map = new HashMap<>();
        try {
            JSONObject jsonObject = new JSONObject(extra);
            String orderId = jsonObject.getString("orderId");
            String type = jsonObject.getString("type");
            map.put("orderId", orderId);
            map.put("type", type + "");
            if(jsonObject.has("lat")){
                String lat = jsonObject.getString("lat");
                String lng = jsonObject.getString("lng");
                map.put("lat", lat);
                map.put("lng", lng);
            }
            return map;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
}
