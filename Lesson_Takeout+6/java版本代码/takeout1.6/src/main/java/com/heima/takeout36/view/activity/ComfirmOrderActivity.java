package com.heima.takeout36.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.heima.takeout36.R;
import com.heima.takeout36.model.net.GoodsInfo;
import com.heima.takeout36.model.net.Seller;

import java.io.Serializable;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lidongzhi on 2017/6/11.
 */
public class ComfirmOrderActivity extends Activity {

    @Bind(R.id.ib_back)
    ImageButton mIbBack;
    @Bind(R.id.iv_location)
    ImageView mIvLocation;
    @Bind(R.id.tv_name)
    TextView mTvName;
    @Bind(R.id.tv_sex)
    TextView mTvSex;
    @Bind(R.id.tv_phone)
    TextView mTvPhone;
    @Bind(R.id.tv_label)
    TextView mTvLabel;
    @Bind(R.id.tv_address)
    TextView mTvAddress;
    @Bind(R.id.rl_location)
    RelativeLayout mRlLocation;
    @Bind(R.id.iv_arrow)
    ImageView mIvArrow;
    @Bind(R.id.iv_icon)
    ImageView mIvIcon;
    @Bind(R.id.tv_seller_name)
    TextView mTvSellerName;
    @Bind(R.id.ll_select_goods)
    LinearLayout mLlSelectGoods;
    @Bind(R.id.tv_deliveryFee)
    TextView mTvDeliveryFee;
    @Bind(R.id.tv_CountPrice)
    TextView mTvCountPrice;
    @Bind(R.id.tvSubmit)
    TextView mTvSubmit;
    private Seller mSeller;
    private List<GoodsInfo> mCartList;
    private float mCoutPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        ButterKnife.bind(this);
        processIntent();
    }

    private void processIntent() {
        if(getIntent()!=null){
            mSeller = (Seller) getIntent().getSerializableExtra("seller");
            mCartList = (List<GoodsInfo>) getIntent().getSerializableExtra("cartList");
            mTvSellerName.setText(mSeller.getName());
            mTvDeliveryFee.setText("￥" + mSeller.getDeliveryFee());

            mCoutPrice = Float.parseFloat(mSeller.getDeliveryFee());
            for(GoodsInfo goodsInfo : mCartList){
                mCoutPrice += goodsInfo.getCount() * Float.parseFloat(goodsInfo.getNewPrice());
            }
            mTvCountPrice.setText("待支付￥" + mCoutPrice);
        }
    }

    @OnClick({R.id.ib_back, R.id.rl_location, R.id.tvSubmit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.rl_location:
                //跳轉到選地址的頁面
                Intent intent2 = new Intent(this, RecepitAddressActivity.class);
                startActivity(intent2);
                break;
            case R.id.tvSubmit:
                Intent intent = new Intent(this, OnlinePaymentActivity.class);
                intent.putExtra("seller", mSeller);
                intent.putExtra("cartList", (Serializable) mCartList);
                intent.putExtra("countPrice", mCoutPrice);
                startActivity(intent);
                break;
        }
    }
}
