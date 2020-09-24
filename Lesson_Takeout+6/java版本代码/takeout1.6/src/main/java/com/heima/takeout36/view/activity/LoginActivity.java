package com.heima.takeout36.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.heima.takeout36.R;
import com.heima.takeout36.presenter.LoginActivityPresenter;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by lidongzhi on 2017/6/6.
 */
public class LoginActivity extends Activity {

    private static final int TIME_MINUS = -1;
    private static final int TIME_IS_OUT = 0;

    @Bind(R.id.iv_user_back)
    ImageView mIvUserBack;
    @Bind(R.id.iv_user_password_login)
    TextView mIvUserPasswordLogin;
    @Bind(R.id.et_user_phone)
    EditText mEtUserPhone;
    @Bind(R.id.tv_user_code)
    TextView mTvUserCode;
    @Bind(R.id.et_user_code)
    EditText mEtUserCode;
    @Bind(R.id.login)
    TextView mLogin;
    private LoginActivityPresenter mLoginPresenter;
    private String mPhone;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mEventHandler != null) {
            SMSSDK.unregisterAllEventHandler();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mLoginPresenter = new LoginActivityPresenter(this);
        SMSSDK.initSDK(this, "181c39656a7ae", "29eea8ebe615953942de64e153c4df34");

        SMSSDK.registerEventHandler(mEventHandler);
    }

    EventHandler mEventHandler = new EventHandler() {

        @Override
        public void afterEvent(int event, int result, Object data) {

            if (result == SMSSDK.RESULT_COMPLETE) {
                //回调完成
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    //TODO:提交验证码成功
                    Log.e("sms", "提交验证码成功");

                    //TODO:短信验证已通过，登录外卖服务器
//                    mLoginPresenter.loginByPhone(mPhone, 2);
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    //TODO:获取验证码成功
                    Log.e("sms", "获取验证码成功");
                } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                    //返回支持发送验证码的国家列表
                }
            } else {
                ((Throwable) data).printStackTrace();
            }
        }
    };

    @OnClick({R.id.tv_user_code, R.id.login})
    public void onClick(View view) {
        mPhone = mEtUserPhone.getText().toString().trim();
//        if(!SMSUtil.judgePhoneNums(this, mPhone)){
//            return;
//        }
        switch (view.getId()) {

            case R.id.tv_user_code:
                //点击去获取验证码
//                getVerificationCode(String country, String phone)
                SMSSDK.getVerificationCode("86", mPhone);
                //开启倒计时
                mTvUserCode.setEnabled(false);
                new Thread(new CutDownTask()).start();
                break;
            case R.id.login:
                //点击去登录
//                submitVerificationCode(String country, String phone, String code)
//                String code = mEtUserCode.getText().toString().trim();
//                if(TextUtils.isEmpty(code)){
//                    return;
//                }
//                SMSSDK.submitVerificationCode("86", mPhone, code);

                mLoginPresenter.loginByPhone(mPhone, 2);
                break;
        }
    }

    int time = 60;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case TIME_MINUS:
                    mTvUserCode.setText("剩余时间(" + time + ")秒");
                    break;
                case TIME_IS_OUT:
                    mTvUserCode.setText("重新发送");
                    mTvUserCode.setEnabled(true);
                    time = 60;
                    break;
            }

        }
    };

    public void onLoginSuccess() {
        finish();
    }

    public void onLoginFaied() {
        Toast.makeText(this, "请仔细检查验证码", Toast.LENGTH_SHORT).show();
    }

    private class CutDownTask implements Runnable {


        @Override
        public void run() {
            for (; time > 0; time--) {
                SystemClock.sleep(999);
                mHandler.sendEmptyMessage(TIME_MINUS);
            }
            mHandler.sendEmptyMessage(TIME_IS_OUT);
        }
    }
}
