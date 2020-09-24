package cn.itcast.tackout.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import cn.itcast.tackout.ConstantValue;
import cn.itcast.tackout.bean.Category;
import cn.itcast.tackout.bean.HomeInfo;
import cn.itcast.tackout.bean.Promotion;
import cn.itcast.tackout.bean.Response;
import cn.itcast.tackout.bean.Seller;
import cn.itcast.tackout.utils.CommonUtil;

public class HomeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public HomeServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
	String[] names = new String[] { "肯德基", "李先生牛肉面", "长青小吃", "麻辣村香锅", "麦当劳", "川湘渝味", "秦家面馆", "炸鸡啤酒", "辣亦有道", "麻辣风暴",
			"零食赞", "水果", };

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 设置data部分数据
		// 促销信息
		ArrayList<Promotion> promotionList = new ArrayList<Promotion>();
		for (int id = 1; id < 4; id++) {
			Promotion promotion = new Promotion();
			promotion.setId(id);
			// http://localhost:8080/TakeoutService/home/
			promotion.setPic(ConstantValue.HOST + "/TakeoutService/" + "imgs/promotion/" + id + ".jpg");
			promotion.setInfo("促销信息" + id);
			promotionList.add(promotion);
		}

		// 分类信息
		ArrayList<Category> categorieList = new ArrayList<Category>();
		for (int id = 1; id < 9; id++) {
			Category category = new Category();
			category.setId(id);
			category.setPic(ConstantValue.HOST + "/TakeoutService/" + "imgs/category/" + id + ".png");
			category.setName(names[id]);
			categorieList.add(category);
		}

		// nearbySellerList
		ArrayList<Seller> nearbySellerList = new ArrayList<Seller>();

		for (int i = 0; i < 10; i++) {
			Seller seller = new Seller();
			seller.setId(i);
			seller.setName("附近第" + i + "家分店");
			seller.setSendPrice("20");
			seller.setDeliveryFee("4");
			seller.setScore("5");
			seller.setDistance("200米/33分钟");
			seller.setSale("月售99份");
			seller.setIcon(ConstantValue.HOST + "/TakeoutService/" + "imgs/seller/" + i%5 + ".jpg");
			nearbySellerList.add(seller);
		}

		// ortherSellerList
		ArrayList<Seller> ortherSellerList = new ArrayList<Seller>();

		for (int i = 0; i < 25; i++) {
			Seller seller = new Seller();
			seller.setId(i);
			seller.setName("其他第" + i + "家分店");
			seller.setSendPrice("30");
			seller.setDeliveryFee("6");
			seller.setScore("4");
			seller.setDistance("996米/50分钟");
			seller.setSale("月售8份");
			seller.setIcon(ConstantValue.HOST + "/TakeoutService/" + "imgs/seller/" + i%5 + ".jpg");
			ortherSellerList.add(seller);
		}

		Response res = new Response();
		res.setCode("0");
		HomeInfo info = new HomeInfo(promotionList, categorieList, nearbySellerList, ortherSellerList);
		res.setData(JSONObject.fromObject(info).toString());
		CommonUtil.renderJson(response, res);
	}

}
