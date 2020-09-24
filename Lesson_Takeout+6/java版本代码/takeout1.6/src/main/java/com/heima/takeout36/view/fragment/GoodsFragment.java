package com.heima.takeout36.view.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.heima.takeout36.R;
import com.heima.takeout36.dagger2.component.DaggerGoodsFragmentComponent;
import com.heima.takeout36.dagger2.module.GoodsFragmentMoudule;
import com.heima.takeout36.model.net.GoodsInfo;
import com.heima.takeout36.model.net.GoodsTypeInfo;
import com.heima.takeout36.presenter.GoodsFragmentPresenter;
import com.heima.takeout36.view.activity.BusinessActivity;
import com.heima.takeout36.view.adapter.GoodsAdapter;
import com.heima.takeout36.view.adapter.GoodsTypeRVAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lidongzhi on 2017/6/8.
 */
public class GoodsFragment extends Fragment {

    @Bind(R.id.rv_goods_type)
    RecyclerView mRvGoodsType;
    @Bind(R.id.slhlv)
    public se.emilsjolander.stickylistheaders.StickyListHeadersListView mSlhlv;
    public GoodsTypeRVAdapter mGoodsTypeRVAdapter;
    public GoodsAdapter mGoodsAdapter;

    @Inject
    public GoodsFragmentPresenter mGoodsFragmentPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View goodView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_goods, container, false);
        ButterKnife.bind(this, goodView);
//        mGoodsFragmentPresenter = new GoodsFragmentPresenter(this);
        DaggerGoodsFragmentComponent.builder().goodsFragmentMoudule(new GoodsFragmentMoudule(this)).build().in(this);

        mRvGoodsType.setLayoutManager(new LinearLayoutManager(getActivity()));
        mGoodsTypeRVAdapter = new GoodsTypeRVAdapter(getActivity());
        mGoodsTypeRVAdapter.setGoodsFragment(this);
        mRvGoodsType.setAdapter(mGoodsTypeRVAdapter);

        //右侧列表
        mGoodsAdapter = new GoodsAdapter(getActivity());
        mGoodsAdapter.setGoodsFragment(this);
        mSlhlv.setAdapter(mGoodsAdapter);
        return goodView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //从上一个页面传过来
        long id = ((BusinessActivity) getActivity()).mSeller.getId();
        mGoodsFragmentPresenter.loadBusinessInfo((int) id);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public void onGoodsTypeSuccess(List<GoodsTypeInfo> goodsTypeInfoList) {
        mGoodsTypeRVAdapter.setGoodsTypeInfoList(goodsTypeInfoList);
    }

    public void onAllGoodsSuccess(List<GoodsInfo> allTypeGoodsList) {
        mGoodsAdapter.setGoodsInfoList(allTypeGoodsList);
    }
}
