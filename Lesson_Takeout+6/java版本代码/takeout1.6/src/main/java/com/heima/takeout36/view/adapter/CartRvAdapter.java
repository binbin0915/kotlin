package com.heima.takeout36.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.heima.takeout36.R;
import com.heima.takeout36.model.net.GoodsInfo;
import com.heima.takeout36.model.net.GoodsTypeInfo;
import com.heima.takeout36.utils.Constant;
import com.heima.takeout36.utils.PriceFormater;
import com.heima.takeout36.utils.TakeoutApp;
import com.heima.takeout36.view.activity.BusinessActivity;
import com.heima.takeout36.view.fragment.GoodsFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lidongzhi on 2017/6/9.
 */
public class CartRvAdapter extends RecyclerView.Adapter {
    private GoodsFragment mGoodsFragment;
    private Context mContext;

    public CartRvAdapter(Context context, GoodsFragment goodsFragment) {
        mContext = context;
        this.mGoodsFragment = goodsFragment;
    }

    private List<GoodsInfo> cartList = new ArrayList<>();

    public void setCartList(List<GoodsInfo> cartList) {
        this.cartList = cartList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_cart, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setData(cartList.get(position));
    }

    @Override
    public int getItemCount() {
        if (cartList != null) {
            return cartList.size();
        }
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_name)
        TextView mTvName;
        @Bind(R.id.tv_type_all_price)
        TextView mTvTypeAllPrice;
        @Bind(R.id.ib_minus)
        ImageButton mIbMinus;
        @Bind(R.id.tv_count)
        TextView mTvCount;
        @Bind(R.id.ib_add)
        ImageButton mIbAdd;
        @Bind(R.id.ll)
        LinearLayout mLl;
        private GoodsInfo mGoodsInfo;

        @OnClick({R.id.ib_minus, R.id.ib_add})
        public void onAddOrMinusClick(View view) {
            boolean isAdd = true;
            switch (view.getId()) {
                case R.id.ib_minus:
                    doMinusOperation();
                    isAdd = false;
                    break;
                case R.id.ib_add:
                    doAddOperation();
                    break;
            }

            //2.刷新左侧列表红点
            processRetCount(isAdd);

            //3.同步右侧列表数量
            mGoodsFragment.mGoodsAdapter.notifyDataSetChanged();

            //4.更新下边购物篮数量与价格
            List<GoodsInfo> cartList = mGoodsFragment.mGoodsFragmentPresenter.getCartList();
            ((BusinessActivity) mGoodsFragment.getActivity()).updateCartUi(cartList);
        }

        private void doAddOperation() {
            //1.刷新当前adapter(购物车内部  数量和价格）
            int count = mGoodsInfo.getCount();
            count++;
            mGoodsInfo.setCount(count);
            notifyDataSetChanged();

            //更新缓存
            TakeoutApp.sInstance.updateCacheSelectedInfo(mGoodsInfo.getId(), Constant.ADD);
        }

        private void processRetCount(boolean isAdd) {
            //增加或减少红点个数
            //1.右侧找到（馒头）对应的typeId
            int typeId = mGoodsInfo.getTypeId();
            //2.遍历左侧，根据（粗粮主食）typeId找到对应的index（1）
            int index = mGoodsFragment.mGoodsFragmentPresenter.getTypeIndexByTypeId(typeId);
            //3.根据index找到goodtypeInfo
            GoodsTypeInfo goodsTypeInfo = mGoodsFragment.mGoodsFragmentPresenter.mGoodsTypeInfoList.get(index);
            //4.最后改变红点数量
            int redCount = goodsTypeInfo.getRedCount();
            if (isAdd) {
                redCount++;
            } else {
                redCount--;
            }
            goodsTypeInfo.setRedCount(redCount);
            //5.刷新左侧的列表
            mGoodsFragment.mGoodsTypeRVAdapter.notifyDataSetChanged();
        }

        private void doMinusOperation() {
            //1.刷新当前adapter(购物车内部  数量和价格）
            int count = mGoodsInfo.getCount();
            count--;
            //TODO:增加对数量为0的处理
            if(count == 0){
                //移除此商品
                cartList.remove(mGoodsInfo);
                if(cartList.size() ==0){
                    //最后一种商品也移除掉了，关闭购物车
                    ((BusinessActivity) mGoodsFragment.getActivity()).showOrHideCart();
                }
                //删除缓存
                TakeoutApp.sInstance.deleteCacheSelectedInfo(mGoodsInfo.getId());
            } else {
                //更新缓存
                TakeoutApp.sInstance.updateCacheSelectedInfo(mGoodsInfo.getId(),Constant.MINUS);
            }
            mGoodsInfo.setCount(count);
            notifyDataSetChanged();
        }

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void setData(GoodsInfo goodsInfo) {
            this.mGoodsInfo = goodsInfo;
            mTvName.setText(goodsInfo.getName());
            mTvTypeAllPrice.setText(PriceFormater.format(goodsInfo.getCount() * Float.parseFloat(goodsInfo.getNewPrice())));

            mTvCount.setText(goodsInfo.getCount() + "");
        }
    }
}
