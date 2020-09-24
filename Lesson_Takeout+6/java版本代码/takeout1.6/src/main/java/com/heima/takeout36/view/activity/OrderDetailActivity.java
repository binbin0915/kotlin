package com.heima.takeout36.view.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.PolylineOptions;
import com.heima.takeout36.R;
import com.heima.takeout36.utils.OrderObservable;

import java.util.ArrayList;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.heima.takeout36.utils.OrderObservable.ORDERTYPE_DISTRIBUTION_RIDER_GIVE_MEAL;
import static com.heima.takeout36.utils.OrderObservable.ORDERTYPE_DISTRIBUTION_RIDER_RECEIVE;
import static com.heima.takeout36.utils.OrderObservable.ORDERTYPE_DISTRIBUTION_RIDER_TAKE_MEAL;

/**
 * Created by lidongzhi on 2017/6/12.
 */
public class OrderDetailActivity extends Activity implements Observer {
    @Bind(R.id.iv_order_detail_back)
    ImageView mIvOrderDetailBack;
    @Bind(R.id.tv_seller_name)
    TextView mTvSellerName;
    @Bind(R.id.tv_order_detail_time)
    TextView mTvOrderDetailTime;
    @Bind(R.id.ll_order_detail_type_container)
    LinearLayout mLlOrderDetailTypeContainer;
    @Bind(R.id.ll_order_detail_type_point_container)
    LinearLayout mLlOrderDetailTypePointContainer;
    @Bind(R.id.map)
    MapView mapView;
    private String mOrderId;
    private String mType;
    private AMap aMap;
    private Marker mRiderMarker;

    @Override
    public void update(Observable o, Object arg) {
        Map<String, String> data = (Map<String, String>) arg;
        String pushOrderId = data.get("orderId");
        String pushType = data.get("type");
        String lat = "";
        String lng = "";
        if (data.containsKey("lat")) {
            lat = data.get("lat");
            lng = data.get("lng");
        }

        if (mOrderId.equals(pushOrderId)) {
            //改变他的状态
            mType = pushType;

            int index = getIndex(mType);
            if (index != -1) {
                //找到了
                ((ImageView) mLlOrderDetailTypePointContainer.getChildAt(index)).setImageResource(R.drawable.order_time_node_disabled);
                ((TextView) mLlOrderDetailTypeContainer.getChildAt(index)).setTextColor(Color.BLUE);
            }

            switch (mType) {
                case OrderObservable.ORDERTYPE_RECEIVEORDER:
                    mapView.setVisibility(View.VISIBLE);
                    //移动到中粮商务公园附近22.5757890000,113.9232180000
//                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(22.5757890000, 113.9232180000)));
                    aMap.moveCamera(CameraUpdateFactory.zoomTo(17));

                    //标注卖家
                    MarkerOptions sellerMakerOption = new MarkerOptions();
                    sellerMakerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.order_seller_icon));
                    sellerMakerOption.position(new LatLng(22.5788340000, 113.9216700000));
                    sellerMakerOption.title("丰顺自选快餐");
                    sellerMakerOption.snippet("我是丰顺自选快餐");
                    aMap.addMarker(sellerMakerOption);

                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(22.5788340000, 113.9216700000)));

                    MarkerOptions buyerMakerOption = new MarkerOptions();
                    ImageView imageView = new ImageView(this);
                    imageView.setImageResource(R.drawable.order_buyer_icon);
                    buyerMakerOption.icon(BitmapDescriptorFactory.fromView(imageView));
                    buyerMakerOption.position(new LatLng(22.5765800000, 113.9237520000));
                    buyerMakerOption.title("黑马程序员");
                    buyerMakerOption.snippet("我是黑马程序员");
                    aMap.addMarker(buyerMakerOption);

                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(22.5765800000, 113.9237520000)));
                    break;
                case OrderObservable.ORDERTYPE_DISTRIBUTION_RIDER_RECEIVE:
                    initRider();
                    break;
                case OrderObservable.ORDERTYPE_DISTRIBUTION_RIDER_GIVE_MEAL:
                    if (!TextUtils.isEmpty(lat)) {
                        updateRider(Double.parseDouble(lat), Double.parseDouble(lng));
                    }
                    break;
            }
        }

    }

    private void updateRider(Double lat, Double lng) {
        mRiderMarker.hideInfoWindow();
        mRiderMarker.setPosition(new LatLng(lat, lng)); //骑手移动
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(lat, lng)));

        //更新后画最新的一小段线
        mPoints.add(new LatLng(lat, lng));
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.color(Color.RED);
        polylineOptions.width(3);
        polylineOptions.add(mPoints.get(mPoints.size() - 1), mPoints.get(mPoints.size() - 2));
        aMap.addPolyline(polylineOptions);

        //测算距离
        float distance = AMapUtils.calculateLineDistance(new LatLng(lat, lng), new LatLng(22.5765800000, 113.9237520000));
        mRiderMarker.setSnippet("距离目标" + distance + "米");
        mRiderMarker.showInfoWindow();

    }

    ArrayList<LatLng> mPoints = new ArrayList<>();

    private void initRider() {
        MarkerOptions riderMakerOption = new MarkerOptions();
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.order_rider_icon);
        riderMakerOption.icon(BitmapDescriptorFactory.fromView(imageView));
        LatLng intPoint = new LatLng(22.5766790000, 113.9205490000);
        mPoints.add(intPoint);
        riderMakerOption.position(intPoint);
//        riderMakerOption.title("百度骑士");
        riderMakerOption.snippet("我是百度骑士");

        mRiderMarker = aMap.addMarker(riderMakerOption);
        mRiderMarker.showInfoWindow();
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(22.5766790000, 113.9205490000)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    private int getIndex(String type) {
        int index = -1;
//        String typeInfo = "";
        switch (type) {
            case OrderObservable.ORDERTYPE_UNPAYMENT:
//                typeInfo = "未支付";
                break;
            case OrderObservable.ORDERTYPE_SUBMIT:
//                typeInfo = "已提交订单";
                index = 0;
                break;
            case OrderObservable.ORDERTYPE_RECEIVEORDER:
//                typeInfo = "商家接单";
                index = 1;
                break;
            case OrderObservable.ORDERTYPE_DISTRIBUTION:
            case ORDERTYPE_DISTRIBUTION_RIDER_GIVE_MEAL:
            case ORDERTYPE_DISTRIBUTION_RIDER_TAKE_MEAL:
            case ORDERTYPE_DISTRIBUTION_RIDER_RECEIVE:
//                typeInfo = "配送中";
                index = 2;
                break;
            case OrderObservable.ORDERTYPE_SERVED:
//                typeInfo = "已送达";
                index = 3;
                break;
            case OrderObservable.ORDERTYPE_CANCELLEDORDER:
//                typeInfo = "取消的订单";
                break;
        }
        return index;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        ButterKnife.bind(this);
        processIntent();
        OrderObservable.getInstance().addObserver(this);

        mapView.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
        }
    }

    private void processIntent() {
        if (getIntent() != null) {
            mOrderId = getIntent().getStringExtra("orderId");
            mType = getIntent().getStringExtra("type");

            //根据type选中对应的点
            int index = getIndex(mType);
            if (index != -1) {
                ((ImageView) mLlOrderDetailTypePointContainer.getChildAt(index)).setImageResource(R.drawable.order_time_node_disabled);
                ((TextView) mLlOrderDetailTypeContainer.getChildAt(index)).setTextColor(Color.BLUE);
            }
        }
    }

    @OnClick(R.id.iv_order_detail_back)
    public void onClick(View view) {
        finish();
    }


}
