package com.heima.takeout36.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.heima.takeout36.R;
import com.heima.takeout36.model.net.GoodsTypeInfo;
import com.heima.takeout36.view.fragment.GoodsFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lidongzhi on 2017/6/8.
 */
public class GoodsTypeRVAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private GoodsFragment mGoodsFragment;

    public void setGoodsFragment(GoodsFragment goodsFragment) {
        mGoodsFragment = goodsFragment;
    }

    public GoodsTypeRVAdapter(Context context) {
        mContext = context;
    }

    private List<GoodsTypeInfo> mGoodsTypeInfoList = new ArrayList<>();

    public void setGoodsTypeInfoList(List<GoodsTypeInfo> goodsTypeInfoList) {
        mGoodsTypeInfoList = goodsTypeInfoList;
        notifyDataSetChanged();
    }

    public int selectIndex = 0;//默认选第一个

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_type, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setData(mGoodsTypeInfoList.get(position), position);
    }

    @Override
    public int getItemCount() {
        if (mGoodsTypeInfoList != null) {
            return mGoodsTypeInfoList.size();
        }
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private View mView;
        @Bind(R.id.tvCount)
        TextView mTvCount;
        @Bind(R.id.type)
        TextView mType;
        private int mPosition;
        private GoodsTypeInfo mGoodsTypeInfo;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.mView = view;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //点击后切换selectIndex，需要知道position
                    selectIndex = mPosition;
                    notifyDataSetChanged();

                    //2.计算汇源果汁是第多少个商品
                    int typeId = mGoodsTypeInfo.getId();
                    int position = mGoodsFragment.mGoodsFragmentPresenter.getGoodsPositionByTypeId(typeId);

                    //3.让右侧列表选中它
                    mGoodsFragment.mSlhlv.setSelection(position);
                }
            });
        }

        public void setData(GoodsTypeInfo goodsTypeInfo, int position) {
            this.mGoodsTypeInfo = goodsTypeInfo;
            this.mPosition = position;

            if (mPosition == selectIndex) {
                //选中的事白底黑字
                mView.setBackgroundColor(Color.WHITE);
                mType.setTextColor(Color.BLACK);
                mType.setTypeface(Typeface.DEFAULT_BOLD);
            } else {
                // ，未选中灰色
                mView.setBackgroundColor(Color.parseColor("#b9dedcdc"));
                mType.setTextColor(Color.GRAY);
                mType.setTypeface(Typeface.DEFAULT);
            }

            mType.setText(goodsTypeInfo.getName());


            //增加红点效果
            if (goodsTypeInfo.getRedCount() > 0) {
                mTvCount.setVisibility(View.VISIBLE);
            } else {
                mTvCount.setVisibility(View.GONE);
            }
            mTvCount.setText(goodsTypeInfo.getRedCount() + "");
        }
    }
}
