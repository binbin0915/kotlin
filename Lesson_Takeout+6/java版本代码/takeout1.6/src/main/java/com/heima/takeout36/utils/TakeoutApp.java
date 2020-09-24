package com.heima.takeout36.utils;

import android.app.Application;

import com.heima.takeout36.model.dao.CacheSelectedInfo;
import com.heima.takeout36.model.net.User;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by lidongzhi on 2017/6/6.
 */

public class TakeoutApp extends Application {

    public CopyOnWriteArrayList<CacheSelectedInfo> infos = new CopyOnWriteArrayList(); //线程安全的

    public int queryCacheSelectedInfoByGoodsId(int goodsId){
        int count = 0;
        for (int i = 0; i < infos.size(); i++) {
            CacheSelectedInfo info = infos.get(i);
            if(info.getGoodsId() == goodsId){
                count = info.getCount();
                break;
            }
        }
        return count;
    }

    public int queryCacheSelectedInfoByTypeId(int typeId){
        int count = 0;
        for (int i = 0; i < infos.size(); i++) {
            CacheSelectedInfo info = infos.get(i);
            if(info.getTypeId() == typeId){
                count = count + info.getCount();
            }
        }
        return count;
    }

    public int queryCacheSelectedInfoBySellerId(int sellerId){
        int count = 0;
        for (int i = 0; i < infos.size(); i++) {
            CacheSelectedInfo info = infos.get(i);
            if(info.getSellerId() == sellerId){
                count = count + info.getCount();
            }
        }
        return count;
    }

    public void addCacheSelectedInfo(CacheSelectedInfo info) {
        infos.add(info);
    }

    public void clearCacheSelectedInfo(int sellerId){
        ArrayList<CacheSelectedInfo> temp = new ArrayList();
        for (int i = 0; i < infos.size(); i++) {
            CacheSelectedInfo info = infos.get(i);
            if(info.getSellerId() == sellerId){
                temp.add(info);
            }
        }
        infos.removeAll(temp);
    }

    public void deleteCacheSelectedInfo(int goodsId) {
        for (int i = 0; i < infos.size(); i++) {
            CacheSelectedInfo info = infos.get(i);
            if (info.getGoodsId() == goodsId) {
                infos.remove(info);
                break;
            }
        }
    }

    public void updateCacheSelectedInfo(int goodsId, int operation) {
        for (int i = 0; i < infos.size(); i++) {
            CacheSelectedInfo info = infos.get(i);
            if (info.getGoodsId() == goodsId) {
                switch (operation) {
                    case Constant.ADD:
                        info.setCount(info.getCount() + 1);
                        break;
                    case Constant.MINUS:
                        info.setCount(info.getCount() - 1);
                        break;
                }

            }
        }
    }

    public static User sUser;
    public static TakeoutApp sInstance;

    /**
     * 应用启动时调用
     */
    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;

        sUser = new User();
        sUser.setId(-1);  //未登录

        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }
}
