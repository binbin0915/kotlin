package com.heima.takeout36.model.dao;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by lidongzhi on 2017/6/9.
 */

@DatabaseTable(tableName = "t_address")
public class RecepitAddress implements Serializable {

    @DatabaseField(generatedId = true) //auto increment
    private int id;

    @DatabaseField(columnName = "name")
    private String name;
    @DatabaseField(columnName = "sex")
    private String sex;
    @DatabaseField(columnName = "phone")
    private String phone;
    @DatabaseField(columnName = "phoneOther")
    private String phoneOther;
    @DatabaseField(columnName = "address")
    private String address;
    @DatabaseField(columnName = "detailAddress")
    private String detailAddress;
    @DatabaseField(columnName = "selectLabel")
    private String selectLabel;

    @DatabaseField(columnName = "userId")
    private String userId; //该地址是属于哪个用户，支持多用户

    public RecepitAddress() {
    }

    public RecepitAddress(int id, String name, String sex, String phone, String phoneOther, String address, String detailAddress, String selectLabel, String userId) {
        this.id = id;
        this.name = name;
        this.sex = sex;
        this.phone = phone;
        this.phoneOther = phoneOther;
        this.address = address;
        this.detailAddress = detailAddress;
        this.selectLabel = selectLabel;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoneOther() {
        return phoneOther;
    }

    public void setPhoneOther(String phoneOther) {
        this.phoneOther = phoneOther;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public String getSelectLabel() {
        return selectLabel;
    }

    public void setSelectLabel(String selectLabel) {
        this.selectLabel = selectLabel;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
