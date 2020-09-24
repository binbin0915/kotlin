package com.heima.takeout36.model.net;

import java.io.Serializable;

public class GoodsInfo implements Serializable{
    int id;//商品id
    String name;//商品名称
    String icon;//商品图片
    String form;//组成
    int monthSaleNum;//月销售量
    boolean bargainPrice;//特价
    boolean isNew;//是否是新产品
    String newPrice;//新价
    int oldPrice;//原价
    int sellerId;

	int count;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	int typeId;   //该商品属于哪个类别
	String typeName;  //这个类别谁叫啥名字

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public GoodsInfo() {
		super();
	}
	
	public GoodsInfo(int sellerId,int id, String name, String icon, String form, int monthSaleNum, boolean bargainPrice,
			boolean isNew, String newPrice, int oldPrice) {
		super();
		this.id = id;
		this.name = name;
		this.icon = icon;
		this.form = form;
		this.monthSaleNum = monthSaleNum;
		this.bargainPrice = bargainPrice;
		this.isNew = isNew;
		this.newPrice = newPrice;
		this.oldPrice = oldPrice;
		this.sellerId = sellerId;
	}
	
	public int getSellerId() {
		return sellerId;
	}

	public void setSellerId(int sellerId) {
		this.sellerId = sellerId;
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

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getForm() {
		return form;
	}

	public void setForm(String form) {
		this.form = form;
	}

	public int getMonthSaleNum() {
		return monthSaleNum;
	}

	public void setMonthSaleNum(int monthSaleNum) {
		this.monthSaleNum = monthSaleNum;
	}

	public boolean isBargainPrice() {
		return bargainPrice;
	}

	public void setBargainPrice(boolean bargainPrice) {
		this.bargainPrice = bargainPrice;
	}

	public boolean isNew() {
		return isNew;
	}

	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}

	public String getNewPrice() {
		return newPrice;
	}

	public void setNewPrice(String newPrice) {
		this.newPrice = newPrice;
	}

	public int getOldPrice() {
		return oldPrice;
	}

	public void setOldPrice(int oldPrice) {
		this.oldPrice = oldPrice;
	}

	@Override
	public String toString() {
		return "GoodsInfo [id=" + id + ", name=" + name + ", icon=" + icon + ", form=" + form + ", monthSaleNum="
				+ monthSaleNum + ", bargainPrice=" + bargainPrice + ", isNew=" + isNew + ", newPrice=" + newPrice
				+ ", oldPrice=" + oldPrice + "]";
	}
    
}
