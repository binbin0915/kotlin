package com.heima.takeout36.model.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.heima.takeout36.model.net.User;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * APP版本       数据版本
 * 1.1首页
 * 1.2用户中心     1
 * 1.3商务页面     1
 * 1.4地址管理     2
 */

public class TakeoutOpenHelper extends OrmLiteSqliteOpenHelper {

    public TakeoutOpenHelper(Context context) {
        super(context, "takeout36.db", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        //创建数据库执行,只是新用户执行一次
        try {
            TableUtils.createTable(connectionSource, User.class);
            TableUtils.createTable(connectionSource, RecepitAddress.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        //更新数据库执行，多次执行（只要数据库升级)
        try {
            TableUtils.createTable(connectionSource, RecepitAddress.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
