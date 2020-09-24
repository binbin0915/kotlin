package com.heima.takeout36.model.dao;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by lidongzhi on 2017/6/9.
 */

public class AddressDao {

    private Dao<RecepitAddress, Integer> mAddressDao;

    public AddressDao(Context context) {
        TakeoutOpenHelper takeoutOpenHelper = new TakeoutOpenHelper(context);
        try {
            mAddressDao = takeoutOpenHelper.getDao(RecepitAddress.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean insertAddress(RecepitAddress recepitAddress){
        try {
            mAddressDao.create(recepitAddress);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void deleteAddress(RecepitAddress recepitAddress){
        try {
            mAddressDao.delete(recepitAddress);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean updateAddress(RecepitAddress recepitAddress){
        try {
            mAddressDao.update(recepitAddress);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<RecepitAddress> queryAllAddress(){
        try {
           return mAddressDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<RecepitAddress> queryAddressByUserId(int userId){
        try {
//            RecepitAddress recepitAddress = mAddressDao.queryForId(userId);//根据地址id查找
           return mAddressDao.queryForEq("userId", userId);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


}
