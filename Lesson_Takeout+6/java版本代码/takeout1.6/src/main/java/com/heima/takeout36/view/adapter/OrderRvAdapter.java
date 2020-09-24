package com.heima.takeout36.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.heima.takeout36.R;
import com.heima.takeout36.model.net.Order;
import com.heima.takeout36.utils.OrderObservable;
import com.heima.takeout36.view.activity.OrderDetailActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lidongzhi on 2017/6/6.
 */
public class OrderRvAdapter extends RecyclerView.Adapter implements Observer {

    @Override
    public void update(Observable o, Object arg) {
        //观察者收到事件后的响应（员工对加班事件的响应)
        Log.e("observer", "新员工收到了明晚加班");

        Map<String, String> data = (Map<String, String>) arg;
        String pushOrderId = data.get("orderId");
        String pushType = data.get("type");

        int index = -1;
        for (int i = 0; i < mOrderList.size(); i++) {
            Order order = mOrderList.get(i);
            if (order.id.equals(pushOrderId)) {
                //改变他的状态
                order.type = pushType;
                index = i;
            }
        }
        if (index != -1) {
            //找到了
            notifyItemChanged(index);
        }
//        notifyDataSetChanged();
    }

    private Context mContext;

    public OrderRvAdapter(Context context) {
        mContext = context;
        //新员工创建，相当于入职，可以观察项目经理了
        OrderObservable.getInstance().addObserver(this);
    }

    private List<Order> mOrderList = new ArrayList<>();

    public void setOrderList(List<Order> orderList) {
        mOrderList = orderList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View itemView = View.inflate(mContext, R.layout.item_order_item, null);
        //默认是true，应该填false，attchtoRoot就是被添加到RooView里
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_order_item, parent, false);
        OrderItemHolder orderItemHolder = new OrderItemHolder(itemView);
        return orderItemHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        OrderItemHolder orderItemHolder = (OrderItemHolder) holder;
        orderItemHolder.setData(mOrderList.get(position));
    }

    private String getOrderTypeInfo(String type) {
        /**
         * 订单状态
         * 1 未支付 2 已提交订单 3 商家接单  4 配送中,等待送达 5已送达 6 取消的订单
         */
//            public static final String ORDERTYPE_UNPAYMENT = "10";
//            public static final String ORDERTYPE_SUBMIT = "20";
//            public static final String ORDERTYPE_RECEIVEORDER = "30";
//            public static final String ORDERTYPE_DISTRIBUTION = "40";
//            public static final String ORDERTYPE_SERVED = "50";
//            public static final String ORDERTYPE_CANCELLEDORDER = "60";

        String typeInfo = "";
        switch (type) {
            case OrderObservable.ORDERTYPE_UNPAYMENT:
                typeInfo = "未支付";
                break;
            case OrderObservable.ORDERTYPE_SUBMIT:
                typeInfo = "已提交订单";
                break;
            case OrderObservable.ORDERTYPE_RECEIVEORDER:
                typeInfo = "商家接单";
                break;
            case OrderObservable.ORDERTYPE_DISTRIBUTION:
                typeInfo = "配送中";
                break;
            case OrderObservable.ORDERTYPE_SERVED:
                typeInfo = "已送达";
                break;
            case OrderObservable.ORDERTYPE_CANCELLEDORDER:
                typeInfo = "取消的订单";
                break;
        }
        return typeInfo;
    }

    @Override
    public int getItemCount() {
        if (mOrderList != null) {
            return mOrderList.size();
        }
        return 0;
    }


    class OrderItemHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_order_item_seller_logo)
        ImageView mIvOrderItemSellerLogo;
        @Bind(R.id.tv_order_item_seller_name)
        TextView mTvOrderItemSellerName;
        @Bind(R.id.tv_order_item_type)
        TextView mTvOrderItemType;
        @Bind(R.id.tv_order_item_time)
        TextView mTvOrderItemTime;
        @Bind(R.id.tv_order_item_foods)
        TextView mTvOrderItemFoods;
        @Bind(R.id.tv_order_item_money)
        TextView mTvOrderItemMoney;
        @Bind(R.id.tv_order_item_multi_function)
        TextView mTvOrderItemMultiFunction;
        private Order mOrder;

        OrderItemHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, OrderDetailActivity.class);
                    intent.putExtra("orderId", mOrder.id);
                    intent.putExtra("type", mOrder.type);
                    mContext.startActivity(intent);
                }
            });
        }

        public void setData(Order order) {
            this.mOrder =order;
            mTvOrderItemSellerName.setText(order.seller.getName());
            mTvOrderItemType.setText(getOrderTypeInfo(order.type)); //订单的状态

            //TODO:其他赋值待定

        }
    }
}
