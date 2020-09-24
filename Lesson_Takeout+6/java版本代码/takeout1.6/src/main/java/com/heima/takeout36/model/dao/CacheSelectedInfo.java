package com.heima.takeout36.model.dao;

/**
 * 多个CacheSelectInfo对应一个点餐单
 */

public class CacheSelectedInfo {

    private int sellerId;  //371983

    private int typeId;  //粗粮主食

    private int goodsId;  //馒头

    private int count; // 27个

    public CacheSelectedInfo(int sellerId, int typeId, int goodsId, int count) {
        this.sellerId = sellerId;
        this.typeId = typeId;
        this.goodsId = goodsId;
        this.count = count;
    }

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
