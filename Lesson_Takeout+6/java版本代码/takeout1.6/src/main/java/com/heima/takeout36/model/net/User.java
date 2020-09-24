package com.heima.takeout36.model.net;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "t_user")
public class User {
	//id = true是手动的
	@DatabaseField(id = true)
	private int id;

	@DatabaseField(columnName = "name")
	private String name;
	@DatabaseField(columnName = "balance")
	private float balance;
	@DatabaseField(columnName = "discount")
	private int discount;
	@DatabaseField(columnName = "integral")
	private int integral;
	@DatabaseField(columnName = "phone")
	private String phone;

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

	public float getBalance() {
		return balance;
	}

	public void setBalance(float balance) {
		this.balance = balance;
	}

	public int getDiscount() {
		return discount;
	}

	public void setDiscount(int discount) {
		this.discount = discount;
	}

	public int getIntegral() {
		return integral;
	}

	public void setIntegral(int integral) {
		this.integral = integral;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	

}
