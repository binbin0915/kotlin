package com.heima.takeout36.presenter;

import android.widget.AbsListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.heima.takeout36.model.net.GoodsInfo;
import com.heima.takeout36.model.net.GoodsTypeInfo;
import com.heima.takeout36.model.net.ResponseInfo;
import com.heima.takeout36.utils.TakeoutApp;
import com.heima.takeout36.view.activity.BusinessActivity;
import com.heima.takeout36.view.fragment.GoodsFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Created by lidongzhi on 2017/6/8.
 */

public class GoodsFragmentPresenter extends NetPresenter {

    private GoodsFragment mGoodsFragment;
    private List<GoodsInfo> mAllTypeGoodsList;
    public List<GoodsTypeInfo> mGoodsTypeInfoList;

    public GoodsFragmentPresenter(GoodsFragment goodsFragment) {
        mGoodsFragment = goodsFragment;
    }

    public void loadBusinessInfo(int sellerId) {
        Call<ResponseInfo> businessInfoCall = mTakeoutService.loadBusinessInfo(sellerId);
        businessInfoCall.enqueue(mCallback);
    }

    @Override
    protected void onConnectError(String message) {

    }

    @Override
    protected void onServerBug(int code) {

    }

    @Override
    protected void onSuccess(String jsonData) {
        boolean hasSelectInfo = ((BusinessActivity) mGoodsFragment.getActivity()).hasSelectInfo;
        mAllTypeGoodsList = new ArrayList<>();
        //可以解析了
        Gson gson = new Gson();
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            String listStr = jsonObject.getString("list");
            //List<GoodsTypeInfo>
            mGoodsTypeInfoList = gson.fromJson(listStr, new TypeToken<List<GoodsTypeInfo>>() {
            }.getType());
            mGoodsFragment.onGoodsTypeSuccess(mGoodsTypeInfoList);

            //取出每个类别的商品
            for (int i = 0; i < mGoodsTypeInfoList.size(); i++) {
                GoodsTypeInfo goodsTypeInfo = mGoodsTypeInfoList.get(i); //饮料类别
                //TODO:查找此类别有多少缓存
                int redcount = 0;
                if(hasSelectInfo) {
                    redcount = TakeoutApp.sInstance.queryCacheSelectedInfoByTypeId(goodsTypeInfo.getId());
                    goodsTypeInfo.setRedCount(redcount);
                }

                List<GoodsInfo> list = goodsTypeInfo.getList();//可乐 王老吉 橙汁
                for (int j = 0; j < list.size(); j++) {
                    GoodsInfo goodsInfo = list.get(j);
                    if(redcount > 0){
                        int count = TakeoutApp.sInstance.queryCacheSelectedInfoByGoodsId(goodsInfo.getId());
                        goodsInfo.setCount(count);
                    }

                    //TODO：获取到数据时给孩子（商品）设置外键（属于哪个类别）？
                    goodsInfo.setTypeId(goodsTypeInfo.getId());
                    goodsInfo.setTypeName(goodsTypeInfo.getName());
                    mAllTypeGoodsList.add(goodsInfo);
                }
            }
            mGoodsFragment.onAllGoodsSuccess(mAllTypeGoodsList);

            //刷新下方UI
            ((BusinessActivity) mGoodsFragment.getActivity()).updateCartUi(getCartList());

            //有数据后添加滚动
            mGoodsFragment.mSlhlv.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    //firstVisibleItem第一个可见的条目
                    //TODO:新的条目成为firstVisibleItem，顶掉旧的条目时，切换类别
                    int oldIndex = mGoodsFragment.mGoodsTypeRVAdapter.selectIndex;

                    int newTypeId = mAllTypeGoodsList.get(firstVisibleItem).getTypeId();
                    int newIndex = getTypeIndexByTypeId(newTypeId);

                    if (newIndex != oldIndex) {
                        //切换了类别
                        mGoodsFragment.mGoodsTypeRVAdapter.selectIndex = newIndex;
                        //刷新左侧列表
                        mGoodsFragment.mGoodsTypeRVAdapter.notifyDataSetChanged();
                    }


                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 循环左侧列表
     *
     * @param newTypeId
     * @return
     */
    public int getTypeIndexByTypeId(int newTypeId) {
        int position = -1;
        for (int i = 0; i < mGoodsTypeInfoList.size(); i++) {
            GoodsTypeInfo goodsTypeInfo = mGoodsTypeInfoList.get(i);
            if (goodsTypeInfo.getId() == newTypeId) {
                position = i;
            }
        }
        return position;
    }


    /**
     * 循环右侧列表
     *
     * @param typeId
     * @return
     */
    public int getGoodsPositionByTypeId(int typeId) {
        int position = -1; //没有该商品的position
        if (mAllTypeGoodsList != null) {
            for (int i = 0; i < mAllTypeGoodsList.size(); i++) {
                GoodsInfo goodsInfo = mAllTypeGoodsList.get(i);
                if (goodsInfo.getTypeId() == typeId) {
                    position = i;
                    break;
                }
            }
        }
        return position;
    }

    /**
     * 筛选获取购物车商品
     */
    public void clearCart() {
        if (mAllTypeGoodsList != null) {
            for (GoodsInfo goodsInfo : mAllTypeGoodsList) {
                goodsInfo.setCount(0);
            }
        }
    }

    /**
     * 筛选获取购物车商品
     */
    public List<GoodsInfo> getCartList() {
        List<GoodsInfo> cartList = new ArrayList<>();
        if (mAllTypeGoodsList != null) {
            for (GoodsInfo goodsInfo : mAllTypeGoodsList) {
                if (goodsInfo.getCount() > 0) {
                    //点过的商品
                    cartList.add(goodsInfo);
                }
            }
        }
        return cartList;
    }
}
