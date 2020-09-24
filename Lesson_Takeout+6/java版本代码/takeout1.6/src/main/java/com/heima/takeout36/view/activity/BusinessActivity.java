package com.heima.takeout36.view.activity;

import android.app.Activity;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flipboard.bottomsheet.BottomSheetLayout;
import com.heima.takeout36.R;
import com.heima.takeout36.model.net.GoodsInfo;
import com.heima.takeout36.model.net.GoodsTypeInfo;
import com.heima.takeout36.model.net.Seller;
import com.heima.takeout36.utils.PriceFormater;
import com.heima.takeout36.utils.TakeoutApp;
import com.heima.takeout36.view.adapter.BusinessFragmentPagerAdapter;
import com.heima.takeout36.view.adapter.CartRvAdapter;
import com.heima.takeout36.view.fragment.CommentsFragment;
import com.heima.takeout36.view.fragment.GoodsFragment;
import com.heima.takeout36.view.fragment.SellerFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.heima.takeout36.R.id.bottomSheetLayout;

/**
 * 商家列表页面
 */
public class BusinessActivity extends Activity {

    @Bind(R.id.ib_back)
    ImageButton mIbBack;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.ib_menu)
    ImageButton mIbMenu;
    @Bind(R.id.vp)
    ViewPager mVp;
    @Bind(bottomSheetLayout)
    BottomSheetLayout mBottomSheetLayout;
    @Bind(R.id.imgCart)
    ImageView mImgCart;
    @Bind(R.id.tvSelectNum)
    TextView mTvSelectNum;
    @Bind(R.id.tvCountPrice)
    TextView mTvCountPrice;
    @Bind(R.id.tvSendPrice)
    TextView mTvSendPrice;
    @Bind(R.id.tvDeliveryFee)
    TextView mTvDeliveryFee;
    @Bind(R.id.tvSubmit)
    TextView mTvSubmit;
    @Bind(R.id.bottom)
    LinearLayout mBottom;
    @Bind(R.id.fl_Container)
    FrameLayout mFlContainer;
    @Bind(R.id.tabs)
    TabLayout mTabs;
    public Seller mSeller;
    private View bottomSheetView;
    private RecyclerView mRvCart;
    private CartRvAdapter mCartRvAdapter;
    public boolean hasSelectInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business);
        ButterKnife.bind(this);
        procesIntent();
        initFragments();
        BusinessFragmentPagerAdapter businessFragmentPagerAdapter = new BusinessFragmentPagerAdapter(getFragmentManager());
        businessFragmentPagerAdapter.setFragmentList(mFragmentList);
        mVp.setAdapter(businessFragmentPagerAdapter);

        //绑定指示器和viewpager
        mTabs.setupWithViewPager(mVp);


    }

    private void procesIntent() {
        if (getIntent() != null) {
            mSeller = (Seller) getIntent().getSerializableExtra("seller");
            mTvDeliveryFee.setText("另需配送费" + PriceFormater.format(Float.parseFloat(mSeller.getDeliveryFee())));
            mTvSendPrice.setText(PriceFormater.format(Float.parseFloat(mSeller.getSendPrice())) + "起送");
            hasSelectInfo = getIntent().getBooleanExtra("hasSelectInfo", false);
        }
    }

    List<Fragment> mFragmentList = new ArrayList<>();

    private void initFragments() {
        mFragmentList.add(new GoodsFragment());
        mFragmentList.add(new CommentsFragment());
        mFragmentList.add(new SellerFragment());
    }

    @OnClick({R.id.ib_back, R.id.tvSubmit, R.id.bottom})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.tvSubmit:
                //结算按钮
                Intent intent = new Intent(this, ComfirmOrderActivity.class);
                intent.putExtra("seller", mSeller);
                GoodsFragment goodsFragment = (GoodsFragment) mFragmentList.get(0);
                List<GoodsInfo> cartList = goodsFragment.mGoodsFragmentPresenter.getCartList();
                intent.putExtra("cartList", (Serializable) cartList);
                startActivity(intent);
                break;
            case R.id.bottom:
                showOrHideCart();
                break;
        }
    }

    public void showOrHideCart() {
        GoodsFragment goodsFragment = (GoodsFragment) mFragmentList.get(0);
        if (bottomSheetView == null) {
            //加载要显示的布局
            bottomSheetView = LayoutInflater.from(this).inflate(R.layout.cart_list, (ViewGroup) getWindow().getDecorView(), false);
            TextView tvClearCart = (TextView) bottomSheetView.findViewById(R.id.tvClear);
            tvClearCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showClearCartDialog();
                }
            });
            mRvCart = (RecyclerView) bottomSheetView.findViewById(R.id.rvCart);
            mRvCart.setLayoutManager(new LinearLayoutManager(this));
            mCartRvAdapter = new CartRvAdapter(this, goodsFragment);
            mRvCart.setAdapter(mCartRvAdapter);
        }
        //判断BottomSheetLayout内容是否显示
        if (mBottomSheetLayout.isSheetShowing()) {
            //关闭内容显示
            mBottomSheetLayout.dismissSheet();
        } else {
            //显示BottomSheetLayout里面的内容
            //TODO:显示前要获取最新的购物车数据

            List<GoodsInfo> cartList = goodsFragment.mGoodsFragmentPresenter.getCartList();
            mCartRvAdapter.setCartList(cartList);
            if(cartList!=null) {
                mBottomSheetLayout.showWithSheetView(bottomSheetView);
            }
        }
    }

    private void showClearCartDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("确定拒绝美食么？");
        builder.setPositiveButton("是，我要减肥", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                clearCart();
            }
        });
        builder.setNegativeButton("不，我还要吃", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();
    }

    private void clearCart() {
        //重置所有购物车商品的数量为0
        GoodsFragment goodsFragment = (GoodsFragment) mFragmentList.get(0);
        goodsFragment.mGoodsFragmentPresenter.clearCart();

        //TODO:还需要刷新UI，还是四个地方
        mCartRvAdapter.notifyDataSetChanged();
        showOrHideCart();

        List<GoodsTypeInfo> goodsTypeInfoList = goodsFragment.mGoodsFragmentPresenter.mGoodsTypeInfoList;
        for(GoodsTypeInfo goodsTypeInfo : goodsTypeInfoList){
            goodsTypeInfo.setRedCount(0);
        }
        //刷新左侧的列表
        goodsFragment.mGoodsTypeRVAdapter.notifyDataSetChanged();

        goodsFragment.mGoodsAdapter.notifyDataSetChanged();

        updateCartUi(null);


        //删除该商家所有缓存
        TakeoutApp.sInstance.clearCacheSelectedInfo((int) mSeller.getId());
    }

    public void addImageButton(ImageButton ib, int width, int height) {
        mFlContainer.addView(ib, width, height);
    }

    public void getImgCartLocation(int[] destLocation) {
        mImgCart.getLocationInWindow(destLocation);  //增加10，这样更准
    }

    public void updateCartUi(List<GoodsInfo> cartList) {
        int count = 0;
        float countPrice = 0.0f;
        if (cartList != null) {
            for (GoodsInfo goodsInfo : cartList) {
                count += goodsInfo.getCount();
                countPrice += goodsInfo.getCount() * Float.parseFloat(goodsInfo.getNewPrice());
            }
        }

        mTvSelectNum.setText(count + "");
        if (count > 0) {
            mTvSelectNum.setVisibility(View.VISIBLE);
        } else {
            mTvSelectNum.setVisibility(View.GONE);
        }

        mTvCountPrice.setText(PriceFormater.format(countPrice));
        if (countPrice > 0) {
            mTvCountPrice.setVisibility(View.VISIBLE);
        } else {
            mTvCountPrice.setVisibility(View.GONE);
        }

        //点餐更新购物车后，有可能达到了起送价格
        if(countPrice >= Float.parseFloat(mSeller.getSendPrice())){
            mTvSubmit.setVisibility(View.VISIBLE);
            mTvSendPrice.setVisibility(View.GONE);
        }else{
            mTvSubmit.setVisibility(View.GONE);
            mTvSendPrice.setVisibility(View.VISIBLE);
        }
    }
}
