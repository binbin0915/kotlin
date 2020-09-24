package com.heima.takeout36.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.heima.takeout36.R;
import com.heima.takeout36.model.dao.AddressDao;
import com.heima.takeout36.model.dao.RecepitAddress;
import com.heima.takeout36.utils.RecycleViewDivider;
import com.heima.takeout36.view.adapter.AddressRvAdapter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lidongzhi on 2017/6/9.
 */

public class RecepitAddressActivity extends Activity {
    @Bind(R.id.ib_back)
    ImageButton mIbBack;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.rv_receipt_address)
    RecyclerView mRvReceiptAddress;
    @Bind(R.id.tv_add_address)
    TextView mTvAddAddress;
    private AddressDao mAddressDao;
    private AddressRvAdapter mAddressRvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_address);
        ButterKnife.bind(this);
        mAddressDao = new AddressDao(this);

        mRvReceiptAddress.setLayoutManager(new LinearLayoutManager(this));
        mRvReceiptAddress.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL));
        mAddressRvAdapter = new AddressRvAdapter(this);
        mRvReceiptAddress.setAdapter(mAddressRvAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //查找所有地址

        List<RecepitAddress> recepitAddresses = mAddressDao.queryAllAddress();
        if (recepitAddresses != null) {
            mAddressRvAdapter.setAddressList(recepitAddresses);
        }
    }

    @OnClick({R.id.ib_back, R.id.tv_add_address})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.tv_add_address:
                //跳转到新增地址页面
                Intent intent = new Intent(this, AddOrEditAddressActivity.class);
                startActivity(intent);
                break;
        }
    }
}
