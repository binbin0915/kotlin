package com.heima.takeout36.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by lidongzhi on 2017/6/6.
 */

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {


        Bundle bundle = intent.getExtras();

//        String title = bundle.getString(JPushInterface.EXTRA_TITLE);
//        Log.e("jpush", title);
//        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
//        Log.e("jpush", "自定义消息：" + message);

        String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
        Log.e("jpush", "额外信息" + extra);

        if (TextUtils.isEmpty(extra)) {
            return;
        }

        //TODO:如何去修改订单列表页面第二个订单的type值
        //1.找到主页，再找到orderfragment,在找到OrderRvAdapter,最终setData()
        OrderObservable.getInstance().newMsgComing(extra);
    }


}
