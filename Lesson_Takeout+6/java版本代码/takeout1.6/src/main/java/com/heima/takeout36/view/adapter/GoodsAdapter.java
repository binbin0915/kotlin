package com.heima.takeout36.view.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.heima.takeout36.R;
import com.heima.takeout36.model.dao.CacheSelectedInfo;
import com.heima.takeout36.model.net.GoodsInfo;
import com.heima.takeout36.model.net.GoodsTypeInfo;
import com.heima.takeout36.utils.Constant;
import com.heima.takeout36.utils.PriceFormater;
import com.heima.takeout36.utils.TakeoutApp;
import com.heima.takeout36.view.activity.BusinessActivity;
import com.heima.takeout36.view.fragment.GoodsFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by lidongzhi on 2017/6/8.
 */
public class GoodsAdapter extends BaseAdapter implements StickyListHeadersAdapter {
    private Context mContext;
    private GoodsFragment mGoodsFragment;

    public void setGoodsFragment(GoodsFragment goodsFragment) {
        mGoodsFragment = goodsFragment;
    }

    public GoodsAdapter(Context context) {
        mContext = context;
    }

    private List<GoodsInfo> mGoodsInfoList = new ArrayList<>();

    public void setGoodsInfoList(List<GoodsInfo> goodsInfoList) {
        mGoodsInfoList = goodsInfoList;
        notifyDataSetChanged();

    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        GoodsInfo goodsInfo = mGoodsInfoList.get(position);

        View headView = LayoutInflater.from(mContext).inflate(R.layout.item_type_header, parent, false);
        ((TextView) headView).setText(goodsInfo.getTypeName());
        return headView;
    }

    @Override
    public long getHeaderId(int position) {
        GoodsInfo goodsInfo = mGoodsInfoList.get(position);
        return goodsInfo.getTypeId();
    }


    @Override
    public int getCount() {
        if (mGoodsInfoList != null) {
            return mGoodsInfoList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (mGoodsInfoList != null) {
            return mGoodsInfoList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_goods, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.setData(mGoodsInfoList.get(position));
        return convertView;
    }

    class ViewHolder {
        private static final long DURATION = 1500;
        @Bind(R.id.iv_icon)
        ImageView mIvIcon;
        @Bind(R.id.tv_name)
        TextView mTvName;
        @Bind(R.id.tv_zucheng)
        TextView mTvZucheng;
        @Bind(R.id.tv_yueshaoshounum)
        TextView mTvYueshaoshounum;
        @Bind(R.id.tv_newprice)
        TextView mTvNewprice;
        @Bind(R.id.tv_oldprice)
        TextView mTvOldprice;
        @Bind(R.id.ib_minus)
        ImageButton mIbMinus;
        @Bind(R.id.tv_count)
        TextView mTvCount;
        @Bind(R.id.ib_add)
        ImageButton mIbAdd;
        private GoodsInfo mGoodsInfo;

        @OnClick({R.id.ib_add, R.id.ib_minus})
        public void onAddOrMinusClick(View view) {
            boolean isAdd = true;
            switch (view.getId()) {
                case R.id.ib_add:
                    doAddOperation();
                    break;
                case R.id.ib_minus:
                    doMinusOperation();
                    isAdd = false;
                    break;
            }
            processRetCount(isAdd);

            //刷新下方购物篮（数量 总价）
            List<GoodsInfo> cartList = mGoodsFragment.mGoodsFragmentPresenter.getCartList();
            ((BusinessActivity) mGoodsFragment.getActivity()).updateCartUi(cartList);
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
            int count = mGoodsInfo.getCount();  //如果没有使用，count=0
            if (count == 1) {
                //需要做动画
                AnimationSet hideAnimation = getHideAnimation();
                mIbMinus.startAnimation(hideAnimation);
                mTvCount.startAnimation(hideAnimation);
                //删除缓存
                TakeoutApp.sInstance.deleteCacheSelectedInfo(mGoodsInfo.getId());
            } else {
                //更新缓存
                TakeoutApp.sInstance.updateCacheSelectedInfo(mGoodsInfo.getId(),Constant.MINUS);
            }
            count--;
            mGoodsInfo.setCount(count);
            notifyDataSetChanged();
        }

        private AnimationSet getHideAnimation() {
            AnimationSet animationSet = new AnimationSet(false);

            AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
            alphaAnimation.setDuration(DURATION);
            animationSet.addAnimation(alphaAnimation);

            RotateAnimation rotateAnimation = new RotateAnimation(720, 0, Animation.RELATIVE_TO_SELF, 0.5F, Animation.RELATIVE_TO_SELF, 0.5F);
            rotateAnimation.setDuration(DURATION);
            animationSet.addAnimation(rotateAnimation);

            TranslateAnimation translateAnimation = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0,
                    Animation.RELATIVE_TO_SELF, 2,
                    Animation.RELATIVE_TO_SELF, 0,
                    Animation.RELATIVE_TO_SELF, 0
            );
            translateAnimation.setDuration(DURATION);
            animationSet.addAnimation(translateAnimation);

            animationSet.setDuration(DURATION);
            return animationSet;
        }

        private void doAddOperation() {
            //第一次需要执行动画，后面改变数量就可以了
            int count = mGoodsInfo.getCount();  //如果没有使用，count=0
            if (count == 0) {
                //需要做动画
                AnimationSet showAnimation = getShowAnimation();
                mIbMinus.startAnimation(showAnimation);
                mTvCount.startAnimation(showAnimation);

                //新增缓存
                long sellerId = ((BusinessActivity) mGoodsFragment.getActivity()).mSeller.getId();
                TakeoutApp.sInstance.addCacheSelectedInfo(new CacheSelectedInfo((int) sellerId, mGoodsInfo.getTypeId(), mGoodsInfo.getId(), 1));
            } else {
                //更新缓存
                TakeoutApp.sInstance.updateCacheSelectedInfo(mGoodsInfo.getId(),Constant.ADD);
            }
            count++;
            mGoodsInfo.setCount(count);
            notifyDataSetChanged();

            //TODO:抛物线动画

            //1.在原来位置增加一个加号
//            mIbAdd.getX(); //相对位置
//            getRawX() getX()
            int[] srcLocation = new int[2];
            mIbAdd.getLocationInWindow(srcLocation);
            Log.e("location", srcLocation[0] + ":" + srcLocation[1]);

            ImageButton ib = new ImageButton(mContext);
            ib.setBackgroundResource(R.drawable.button_add);
            ib.setX(srcLocation[0]);
            ib.setY(srcLocation[1]);
            ((BusinessActivity) mGoodsFragment.getActivity()).addImageButton(ib, mIbAdd.getWidth(), mIbAdd.getHeight());

            //2.执行抛物线轨迹（购物栏的位置）
            int[] destLocation = new int[2];
            ((BusinessActivity) mGoodsFragment.getActivity()).getImgCartLocation(destLocation);

            AnimationSet parabolaAnimation = getParabolaAnimation(ib, srcLocation, destLocation);
            ib.startAnimation(parabolaAnimation);
            //3.回收加号
        }

        private AnimationSet getParabolaAnimation(final ImageButton ib, int[] srcLocation, int[] destLocation) {
            AnimationSet animationSet = new AnimationSet(false);
            animationSet.setDuration(DURATION);

            TranslateAnimation transX = new TranslateAnimation(
                    Animation.ABSOLUTE, 0,
                    Animation.ABSOLUTE, destLocation[0] - srcLocation[0],
                    Animation.ABSOLUTE, 0,
                    Animation.ABSOLUTE, 0
            );
            transX.setDuration(DURATION);
            animationSet.addAnimation(transX);

            TranslateAnimation transY = new TranslateAnimation(
                    Animation.ABSOLUTE, 0,
                    Animation.ABSOLUTE, 0,
                    Animation.ABSOLUTE, 0,
                    Animation.ABSOLUTE, destLocation[1] - srcLocation[1]
            );
            transY.setInterpolator(new AccelerateInterpolator());
            transY.setDuration(DURATION);
            animationSet.addAnimation(transY);
            animationSet.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    //动画结束后回收+号，找到viewparent，再移除自己
                    ViewParent parent = ib.getParent();
                    if (parent != null) {
                        ((ViewGroup) parent).removeView(ib);
                    }

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            return animationSet;
        }


        private AnimationSet getShowAnimation() {
            AnimationSet animationSet = new AnimationSet(false);

            AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
            alphaAnimation.setDuration(DURATION);
            animationSet.addAnimation(alphaAnimation);

            RotateAnimation rotateAnimation = new RotateAnimation(0, 720, Animation.RELATIVE_TO_SELF, 0.5F, Animation.RELATIVE_TO_SELF, 0.5F);
            rotateAnimation.setDuration(DURATION);
            animationSet.addAnimation(rotateAnimation);

            TranslateAnimation translateAnimation = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 2,
                    Animation.RELATIVE_TO_SELF, 0,
                    Animation.RELATIVE_TO_SELF, 0,
                    Animation.RELATIVE_TO_SELF, 0
            );
            translateAnimation.setDuration(DURATION);
            animationSet.addAnimation(translateAnimation);

            animationSet.setDuration(DURATION);
            return animationSet;
        }

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void setData(GoodsInfo goodsInfo) {
            this.mGoodsInfo = goodsInfo;
            mTvName.setText(goodsInfo.getName());
            //TODO:
            Log.e("goods", Constant.HOST + goodsInfo.getIcon());
            Picasso.with(mContext).load(goodsInfo.getIcon()).into(mIvIcon);
            mTvZucheng.setText(goodsInfo.getForm());

            mTvYueshaoshounum.setText("月售" + goodsInfo.getMonthSaleNum() + "份");
            mTvNewprice.setText(PriceFormater.format(Float.parseFloat(goodsInfo.getNewPrice())));

            if (goodsInfo.getOldPrice() > 0) {
                mTvOldprice.setVisibility(View.VISIBLE);
                mTvOldprice.setText(PriceFormater.format(goodsInfo.getOldPrice()));

                mTvOldprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                mTvOldprice.setVisibility(View.GONE);
            }

            //count>0才显示
            int count = goodsInfo.getCount();
            if (count > 0) {
                mTvCount.setVisibility(View.VISIBLE);
                mIbMinus.setVisibility(View.VISIBLE);
            } else {
                mTvCount.setVisibility(View.INVISIBLE);
                mIbMinus.setVisibility(View.INVISIBLE);
            }
            mTvCount.setText(count + "");
        }
    }
}
