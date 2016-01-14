package com.simicart.core.home.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.catalog.category.entity.Category;
import com.simicart.core.catalog.product.entity.ProductList;

public class HomeModel extends SimiModel {
	private SimiCollection banner = null;
	private SimiCollection home_cate = null;
	private ArrayList<ProductList> home_spot = null;

	@Override
	protected void setShowNotifi() {
		isShowNotify = false;
	}

	@Override
	protected void paserData() {
		super.paserData();
		if (collection != null) {
			JSONObject jobs = collection.getCollection().get(0).getJSONObject();
			this.banner = this.createBannerdata(jobs);
			this.home_spot = this.createSpotdata(jobs);
			this.home_cate = this.createHomeCatedata(jobs);
		}

	}

	@Override
	protected void setUrlAction() {
		url_action = "connector/config/get_home_data";
	}

	@Override
	protected void setEnableCache() {
		this.enableCache = true;
	}

	public SimiCollection getBannerData() {
		return this.banner;
	}

	public SimiCollection getHomeCateData() {
		return this.home_cate;
	}

	public ArrayList<ProductList> getHomeSpotData() {
		return this.home_spot;
	}

	public SimiCollection createBannerdata(JSONObject jSonData) {
		if (jSonData == null)
			return null;
		try {
			JSONArray jsAr = jSonData.getJSONArray("banner");
			SimiCollection collection = new SimiCollection();
			for (int i = 0; i < jsAr.length(); i++) {
				SimiEntity entity = new SimiEntity();
				entity.setJSONObject(jsAr.getJSONObject(i));
				collection.addEntity(entity);
			}
			return collection;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public ArrayList<ProductList> createSpotdata(JSONObject jSonData) {
		if (jSonData == null)
			return null;
		try {
			JSONArray jsAr = jSonData.getJSONArray("spot_product");
			ArrayList<ProductList> mProductList = new ArrayList<ProductList>();
			for (int i = 0; i < jsAr.length(); i++) {
				ProductList product = new ProductList();
				product.setJSONObject(jsAr.getJSONObject(i));
				mProductList.add(product);
			}
			return mProductList;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public SimiCollection createHomeCatedata(JSONObject jSonData) {
		if (jSonData == null)
			return null;
		try {
			JSONArray jsAr = jSonData.getJSONArray("categories");
			SimiCollection collection = new SimiCollection();
			for (int i = 0; i < jsAr.length(); i++) {
				Category category = new Category();
				category.setJSONObject(jsAr.getJSONObject(i));
				collection.addEntity(category);
			}
			return collection;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
}
