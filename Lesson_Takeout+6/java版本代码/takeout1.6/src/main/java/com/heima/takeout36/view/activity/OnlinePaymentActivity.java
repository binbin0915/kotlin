package com.heima.takeout36.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.heima.takeout36.R;
import com.heima.takeout36.model.net.GoodsInfo;
import com.heima.takeout36.model.net.Seller;
import com.heima.takeout36.payment.PayResult;
import com.heima.takeout36.payment.SignUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lidongzhi on 2017/6/11.
 */
public class OnlinePaymentActivity extends Activity {

    private static final int SDK_PAY_FLAG = 1;
    @Bind(R.id.ib_back)
    ImageButton mIbBack;
    @Bind(R.id.tv_residualTime)
    TextView mTvResidualTime;
    @Bind(R.id.tv_order_name)
    TextView mTvOrderName;
    @Bind(R.id.tv)
    TextView mTv;
    @Bind(R.id.tv_order_detail)
    TextView mTvOrderDetail;
    @Bind(R.id.iv_triangle)
    ImageView mIvTriangle;
    @Bind(R.id.ll_order_toggle)
    RelativeLayout mLlOrderToggle;
    @Bind(R.id.tv_receipt_connect_info)
    TextView mTvReceiptConnectInfo;
    @Bind(R.id.tv_receipt_address_info)
    TextView mTvReceiptAddressInfo;
    @Bind(R.id.ll_goods)
    LinearLayout mLlGoods;
    @Bind(R.id.ll_order_detail)
    LinearLayout mLlOrderDetail;
    @Bind(R.id.tv_pay_money)
    TextView mTvPayMoney;
    @Bind(R.id.iv_pay_alipay)
    ImageView mIvPayAlipay;
    @Bind(R.id.cb_pay_alipay)
    CheckBox mCbPayAlipay;
    @Bind(R.id.tv_selector_other_payment)
    TextView mTvSelectorOtherPayment;
    @Bind(R.id.ll_hint_info)
    LinearLayout mLlHintInfo;
    @Bind(R.id.iv_pay_wechat)
    ImageView mIvPayWechat;
    @Bind(R.id.cb_pay_wechat)
    CheckBox mCbPayWechat;
    @Bind(R.id.iv_pay_qq)
    ImageView mIvPayQq;
    @Bind(R.id.cb_pay_qq)
    CheckBox mCbPayQq;
    @Bind(R.id.iv_pay_fenqile)
    ImageView mIvPayFenqile;
    @Bind(R.id.cb_pay_fenqile)
    CheckBox mCbPayFenqile;
    @Bind(R.id.ll_other_payment)
    LinearLayout mLlOtherPayment;
    @Bind(R.id.bt_confirm_pay)
    Button mBtConfirmPay;
    private Seller mSeller;
    private List<GoodsInfo> mCartList;
    private float mCountPrice;

    //商户PID
    public static final String PARTNER = "2088011085074233";
    //商户收款账号
    public static final String SELLER = "917356107@qq.com";
    //商户私钥，pkcs8格式
    public static final String RSA_PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAL51jaxQhxW9PnWpW+nz6yJ76tp9eGFXmfGnuxMK+Pmx/qavdsewXOLBfI2OSCR39TzxwMYvCmUrnrt0fVSa7mblbNos2FnMM9ijnx8bsAAhm+i7BKhuaHMunJKH69L+D753zH3P1YIh0ly5DnAr3WPqHydp384qBvb8NS9Tay0HAgMBAAECgYB82PIVknP6fCMFXg8yPQJViIVa1ASlSpdPIXQv93FdvKABA+QI4kMBIXRUFoCT506KtK55OzzFNOLIXoQJgcXj69z0l6pmjJJgXMaBW/9rOzelot13CiGatrIrGngEZO+bCBTud/jQA598zjZ1g182tT+FLDL7GIftW2hC8GqtAQJBAN+XrYsyfL+uSmLdAVEz1vzziU1naGr10Msm1jMnnO/JYdB+84j7FSHxsQ4YOgsmeN5YVsJcVfc/CReOxknns38CQQDaEHnVPDt+Z7sqT7bN0UKh0/CrqkDTiIjhz1lJyIIoqVRoeJjJn1wlEKBV5R9gkTJutQTVU19XFtblMEnOy6p5AkEAw170rEmMSa0QoHw+d2bVtydR1QnDapqqO6kOx5oYfkm4J4eWYx4J5CQdMpSmuzF9scL85E3sa+NvnV8LEm7cHwJALtXzFPWG4bNt47yTSslzQka/Hl/G5Kginj1mtA44xnr4AihEyKlNpThY95nqj1cgOd7vVtI9W/sv1LH2aFAeIQJBAIqXbMc6xGVfuiFAJKtg+AFNMBP0UOEgMEoKo4RPFp21nBhFgL9/WYM4ZjyHUdr45rCySAqQovw4DCHLfQZC23I=";
    //支付宝公钥
    public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC+dY2sUIcVvT51qVvp8+sie+rafXhhV5nxp7sTCvj5sf6mr3bHsFziwXyNjkgkd/U88cDGLwplK567dH1Umu5m5WzaLNhZzDPYo58fG7AAIZvouwSobmhzLpySh+vS/g++d8x9z9WCIdJcuQ5wK91j6h8nad/OKgb2/DUvU2stBwIDAQAB";

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);

                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();

                    String resultStatus = payResult.getResultStatus();

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(OnlinePaymentActivity.this, "支付成功",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(OnlinePaymentActivity.this, "支付结果确认中",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(OnlinePaymentActivity.this, "支付失败",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                    break;
                }
                default:
                    break;
            }
        };
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_payment);
        ButterKnife.bind(this);

        processIntent();
    }

    private void processIntent() {
        if (getIntent() != null) {
            mSeller = (Seller) getIntent().getSerializableExtra("seller");
            mCartList = (List<GoodsInfo>) getIntent().getSerializableExtra("cartList");
            mCountPrice = getIntent().getFloatExtra("countPrice", 0.01f);
            mTvPayMoney.setText("￥0.01");

            fillGoods();
        }
    }

    private void fillGoods() {
        for(GoodsInfo goodsInfo :mCartList) {
            TextView textView = new TextView(this);
            textView.setText(goodsInfo.getName() + " ￥" + goodsInfo.getNewPrice() + " * " + goodsInfo.getCount());
            mLlGoods.addView(textView);
        }
    }

    boolean isOpened = false;
    @OnClick({R.id.ib_back, R.id.iv_triangle, R.id.bt_confirm_pay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.iv_triangle:
                //展开商品列表
                if(isOpened){
                    mLlOrderDetail.setVisibility(View.GONE);
                }else{
                    mLlOrderDetail.setVisibility(View.VISIBLE);
                }
                isOpened = !isOpened;
                break;
            case R.id.bt_confirm_pay:
                //付钱
                pay();
                break;
        }
    }

    public String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }

    public String getSignType() {
        return "sign_type=\"RSA\"";
    }

    private void pay() {
        // 订单
        String orderInfo = getOrderInfo("测试的商品", "该测试商品的详细描述", "0.01");

        // 对订单做RSA 签名
        String sign = sign(orderInfo);
        try {
            // 仅需对sign 做URL编码
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 完整的符合支付宝参数规范的订单信息
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
                + getSignType();

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(OnlinePaymentActivity.this);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    public String getOrderInfo(String subject, String body, String price) {
        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm"
                + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    public String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
                Locale.getDefault());
        Date date = new Date();
        String key = mSeller.getId() + format.format(date);

        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }
}
