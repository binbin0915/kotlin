package com.heima.takeout36.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.heima.takeout36.R;
import com.heima.takeout36.model.net.Seller;
import com.heima.takeout36.utils.TakeoutApp;
import com.heima.takeout36.view.activity.BusinessActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lidongzhi on 2017/6/5.
 */
public class HomeRVAdapter extends RecyclerView.Adapter {

    public static final int TYPE_TITLE = 0; //头部
    public static final int TYPE_DIVISION = 1; //广告，也是分割线
    public static final int TYPE_SELLER = 2; //商家，普通条目
    private List<Seller> mOtherSellerList = new ArrayList<>();
    private List<Seller> mNearbySeller = new ArrayList<>();

    @Override
    public int getItemViewType(int position) {
        //TODO: 头布局 （附近0-附近6) 广告1  （其他0—其他9） 广告2 （其他10—其他19） 广告3 （其他20—其他29） 广告4 （其他30—其他33）
        if (position == 0) {
            return TYPE_TITLE;
        } else if (position == mNearbySeller.size() + 1
                || ((position - mNearbySeller.size() - 1) > 0 && (position - mNearbySeller.size() - 1) % 11 == 0)) {
            return TYPE_DIVISION;
        } else {
            return TYPE_SELLER;
        }
    }

    private Context mContext;

    public HomeRVAdapter(Context context) {
        mContext = context;
    }

//    private List<Seller> mDatas = new ArrayList<>();

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_TITLE:
                View itemView = View.inflate(mContext, R.layout.item_title, null);
                TitleHolder titleHolder = new TitleHolder(itemView);
                return titleHolder;
            case TYPE_DIVISION:
                View itemView3 = View.inflate(mContext, R.layout.item_division, null);
                DivisionHolder divisionHolder = new DivisionHolder(itemView3);
                return divisionHolder;
            case TYPE_SELLER:
                View itemView2 = View.inflate(mContext, R.layout.item_seller, null);
                SellerHolder sellerHolder = new SellerHolder(itemView2);
                return sellerHolder;
            default:
                Log.e("home", "竟然还有第三种holder");
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case TYPE_TITLE:
                TitleHolder titleHolder = (TitleHolder) holder;
                titleHolder.setData("我是大哥大------------------------------");
                break;
            case TYPE_DIVISION:
                DivisionHolder divisionHolder = (DivisionHolder) holder;
                divisionHolder.setData("--------------------我是华丽的分割线------------------------------");
                break;
            case TYPE_SELLER:
                SellerHolder viewHolder = (SellerHolder) holder;
                //TODO:附近商家和其他商家的区别

                //TODO: 头布局 （附近0-附近6) 广告1  （其他0—其他9） 广告2 （其他10—其他19） 广告3 （其他20—其他29） 广告4 （其他30—其他33）
                int index;
                if (position < mNearbySeller.size() + 1) {
                    //附近商家
                    index = position - 1;
                    viewHolder.setData(mNearbySeller.get(index));
                } else {
                    //其他商家
                    index = position - 1 - mNearbySeller.size() - 1;
                    index -= index / 11;
                    viewHolder.setData(mOtherSellerList.get(index));
                }
                break;
        }
    }

    //TODO: 头布局 （附近0-附近6) 广告1  （其他0—其他9） 广告2 （其他10—其他19） 广告3 （其他20—其他29） 广告4 （其他30—其他33）
    @Override
    public int getItemCount() {
        int count = 1; //头布局

        if (mNearbySeller.size() == 0 && mOtherSellerList.size() == 0) {
            //用户出去旅游，或者程序出问题
            return count;
        }

        if (mNearbySeller.size() > 0) {
            count += mNearbySeller.size();
        }

        if (mOtherSellerList.size() > 0) {
            count += 1;//第一个广告
            count += mOtherSellerList.size();
            count += mOtherSellerList.size() / 10;
            if (mOtherSellerList.size() % 10 == 0) {
                count -= 1;//整除回头减去一个
            }
        }
        return count;
    }

    public void setData(List<Seller> nearbySellerList, List<Seller> otherSellerList) {
//        mDatas.clear();
        this.mNearbySeller = nearbySellerList;
        this.mOtherSellerList = otherSellerList;
//        mDatas.addAll(nearbySellerList);
//        mDatas.addAll(otherSellerList);
        notifyDataSetChanged();
    }

    class TitleHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.slider)
        SliderLayout mSliderLayout;

        TitleHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void setData(String data) {
            HashMap<String, String> url_maps = new HashMap<String, String>();
            url_maps.put("Hannibal", "http://static2.hypable.com/wp-content/uploads/2013/12/hannibal-season-2-release-date.jpg");
            url_maps.put("Big Bang Theory", "http://tvfiles.alphacoders.com/100/hdclearart-10.png");
            url_maps.put("House of Cards", "http://cdn3.nflximg.net/images/3093/2043093.jpg");
            url_maps.put("Game of Thrones", "http://images.boomsbeat.com/data/images/full/19640/game-of-thrones-season-4-jpg.jpg");

            for (String desc : url_maps.keySet()) {
                TextSliderView textSliderView = new TextSliderView(itemView.getContext());
                textSliderView
                        .description(desc)
                        .image(url_maps.get(desc));
                mSliderLayout.addSlider(textSliderView);
            }

        }
    }

    class SellerHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.seller_logo)
        ImageView mSellerLogo;
        @Bind(R.id.tvCount)
        TextView mTvCount;
        @Bind(R.id.tv_title)
        TextView mTvTitle;
        @Bind(R.id.ratingBar)
        RatingBar mRatingBar;
        @Bind(R.id.tv_home_sale)
        TextView mTvHomeSale;
        @Bind(R.id.tv_home_send_price)
        TextView mTvHomeSendPrice;
        @Bind(R.id.tv_home_distance)
        TextView mTvHomeDistance;
        private Seller mSeller;

        SellerHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, BusinessActivity.class);
                    boolean hasSelectInfo = false;
                    int count = TakeoutApp.sInstance.queryCacheSelectedInfoBySellerId((int) mSeller.getId());
                    if (count > 0) {
                        hasSelectInfo = true;
                    }
                    intent.putExtra("hasSelectInfo", hasSelectInfo);
                    intent.putExtra("seller", mSeller);
                    mContext.startActivity(intent);
                }
            });
        }

        public void setData(Seller seller) {
            this.mSeller = seller;
            mTvTitle.setText(seller.getName());
            //TODO:赋值其他字段
            mRatingBar.setRating(Float.parseFloat(seller.getScore()));
            mTvHomeSale.setText("月售" + seller.getSale() + "单");
            mTvHomeSendPrice.setText("￥" + seller.getSendPrice() + "起送/配送费￥" + seller.getDeliveryFee());
            mTvHomeDistance.setText(seller.getDistance());
        }

        public void setData(int position) {
            mTvTitle.setText("我是条目：" + position);
        }
    }

    class DivisionHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_division_title)
        TextView mTvDivisionTitle;

        DivisionHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void setData(String data) {
//            mTvDivisionTitle.setText(data);
        }
    }
}
