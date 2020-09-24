package com.heima.takeout36.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.heima.takeout36.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lidongzhi on 2017/6/11.
 */
public class AroundRvAdapter extends RecyclerView.Adapter {
    private Context mContext;

    public AroundRvAdapter(Context context) {
        mContext = context;
    }

    private List<PoiItem> mPoiItemList = new ArrayList<>();

    public void setPoiItemList(List<PoiItem> poiItemList) {
        mPoiItemList = poiItemList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_around, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setData(mPoiItemList.get(position));
    }

    @Override
    public int getItemCount() {
        if(mPoiItemList!=null){
            return mPoiItemList.size();
        }
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.title)
        TextView mTitle;
        @Bind(R.id.address)
        TextView mAddress;
        private PoiItem mPoiItem;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent data = new Intent();
                    data.putExtra("title", mPoiItem.getTitle());
                    data.putExtra("address", mPoiItem.getSnippet());
                    ((Activity) mContext).setResult(200, data);
                    ((Activity) mContext).finish();
                }
            });
        }

        public void setData(PoiItem poiItem) {
            this.mPoiItem = poiItem;
            mTitle.setText(poiItem.getTitle());
            mAddress.setText(poiItem.getSnippet()); //摘要信息
        }
    }
}
