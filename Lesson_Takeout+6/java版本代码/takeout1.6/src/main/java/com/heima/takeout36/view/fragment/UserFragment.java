package com.heima.takeout36.view.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.heima.takeout36.R;
import com.heima.takeout36.utils.TakeoutApp;
import com.heima.takeout36.view.activity.LoginActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lidongzhi on 2017/6/5.
 */

public class UserFragment extends Fragment {

    @Bind(R.id.tv_user_setting)
    ImageView mTvUserSetting;
    @Bind(R.id.iv_user_notice)
    ImageView mIvUserNotice;
    @Bind(R.id.login)
    ImageView mLogin;
    @Bind(R.id.username)
    TextView mUsername;
    @Bind(R.id.phone)
    TextView mPhone;
    @Bind(R.id.ll_userinfo)
    LinearLayout mLlUserinfo;
    @Bind(R.id.iv_address_manager)
    ImageView mIvAddressManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = View.inflate(getActivity(), R.layout.fragment_user, null);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        //此时有可能已经登录成功，显示不同的UI
        int id = TakeoutApp.sUser.getId();
        if(id == -1){
            //未登录
            mLlUserinfo.setVisibility(View.GONE);
            mLogin.setVisibility(View.VISIBLE);

        }else{
            mLlUserinfo.setVisibility(View.VISIBLE);
            mLogin.setVisibility(View.GONE);
            mUsername.setText("欢迎您," + TakeoutApp.sUser.getName());
            mPhone.setText(TakeoutApp.sUser.getPhone());
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.login)
    public void onClick() {
        Intent intent = new Intent(getActivity(),LoginActivity.class);
        getActivity().startActivity(intent);
    }
}
