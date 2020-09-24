package com.heima.takeout36.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.heima.takeout36.R;
import com.heima.takeout36.model.dao.RecepitAddress;
import com.heima.takeout36.view.activity.AddOrEditAddressActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lidongzhi on 2017/6/9.
 */
public class AddressRvAdapter extends RecyclerView.Adapter {
    private Context mContext;

    public AddressRvAdapter(Context context) {
        mContext = context;
    }

    private List<RecepitAddress> mAddressList = new ArrayList<>();

    public void setAddressList(List<RecepitAddress> addressList) {
        mAddressList = addressList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_receipt_address, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setData(mAddressList.get(position));
    }

    @Override
    public int getItemCount() {
        if (mAddressList != null) {
            return mAddressList.size();
        }
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
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
        @Bind(R.id.iv_edit)
        ImageView mIvEdit;
        private RecepitAddress mAddress;

        @OnClick(R.id.iv_edit)
        public void onclick(View view){
            Intent intent = new Intent(mContext, AddOrEditAddressActivity.class);
            //原来的信息带过去，准备更新
            intent.putExtra("address", mAddress);
            mContext.startActivity(intent);
        }

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void setData(RecepitAddress address) {
            this.mAddress = address;
            mTvName.setText(address.getName());
            mTvSex.setText(address.getSex());
            mTvPhone.setText(address.getPhone() + "," + address.getPhoneOther());
            mTvAddress.setText(address.getAddress() + "," + address.getDetailAddress());
            mTvLabel.setText(address.getSelectLabel());
            mTvLabel.setTextColor(Color.BLACK);
        }
    }
}
