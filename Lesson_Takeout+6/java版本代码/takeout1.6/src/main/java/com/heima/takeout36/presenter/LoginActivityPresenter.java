package com.heima.takeout36.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.heima.takeout36.model.dao.TakeoutOpenHelper;
import com.heima.takeout36.model.net.ResponseInfo;
import com.heima.takeout36.model.net.User;
import com.heima.takeout36.utils.TakeoutApp;
import com.heima.takeout36.view.activity.LoginActivity;
import com.j256.ormlite.android.AndroidDatabaseConnection;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.sql.Savepoint;

import retrofit2.Call;

/**
 * Created by lidongzhi on 2017/6/6.
 */

public class LoginActivityPresenter extends NetPresenter {

    private LoginActivity mLoginActivity;

    public LoginActivityPresenter(LoginActivity loginActivity) {
        mLoginActivity = loginActivity;
    }

    public void loginByPhone(String phone, int type) {
        //1表示是普通账号，2表示是手机号码账号    17712345678
        Call<ResponseInfo> responseInfoCall = mTakeoutService.loginbyPhone(phone, type);
        responseInfoCall.enqueue(mCallback);

    }


    @Override
    protected void onConnectError(String message) {

    }

    @Override
    protected void onServerBug(int code) {

    }

    @Override
    protected void onSuccess(String jsonData) {
        //在这里拿到数据
        Gson gson = new Gson();
        User user = gson.fromJson(jsonData, User.class);
        Log.e("login", "userId:" + user.getId());

        //TODO:保存到内存中
        TakeoutApp.sUser = user;

        //缓存到sqlite里面，[SqliteOpenHelper], OrmLite
//        OrmLiteSqliteOpenHelper
        TakeoutOpenHelper takeoutOpenHelper = new TakeoutOpenHelper(mLoginActivity);

        AndroidDatabaseConnection androidDatabaseConnection = new AndroidDatabaseConnection(takeoutOpenHelper.getWritableDatabase(),true);
        Savepoint startPoint = null;
        try {
            startPoint = androidDatabaseConnection.setSavePoint("start");
            androidDatabaseConnection.setAutoCommit(false); //取消自动提交事务

            Dao<User, Integer> userDao = takeoutOpenHelper.getDao(User.class);
            //TODO:第一步，先查询数据库记录
            User oldUser = userDao.queryForId(36);
            //TODO:第二部，根据查询的结果更新或者新建
            if (oldUser != null) {
                //已经有此用户,更新信息
                userDao.update(user);
                Log.e("ormlite","已经有此用户,更新信息");
//                TecentStasticSdk.userAction(-1);
            } else {
                //新用户，创建
                userDao.create(user);
                Log.e("ormlite","新用户，创建");
//                TecentStasticSdk.userAction(0);
            }
//            userDao.createIfNotExists(user);


            androidDatabaseConnection.commit(startPoint);
            Log.e("ormlite", "提交事务");
            mLoginActivity.onLoginSuccess();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                androidDatabaseConnection.rollback(startPoint);
                Log.e("ormlite", "异常，回滚事务");
            } catch (SQLException e1) {
                e1.printStackTrace();
                mLoginActivity.onLoginFaied();
            }
        }
    }
}
