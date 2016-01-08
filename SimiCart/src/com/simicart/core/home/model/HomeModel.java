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
import com.simicart.core.home.model.page.CMSModel;
import com.simicart.core.home.model.page.PluginModel;
import com.simicart.core.home.model.page.StoreViewModel;

public class HomeModel extends SimiModel {
	private String bannerCollection = null;
	private String homeCategoriesCollection = null;
	private String mProductList = null;

	@Override
	protected void setShowNotifi() {
		isShowNotify = false;
	}

	@Override
	protected void paserData() {
		super.paserData();
		if (collection != null) {
			JSONObject jobs = collection.getCollection().get(0).getJSONObject();

			StoreViewModel storeViewM = new StoreViewModel();
			storeViewM.setData(jobs);

			CMSModel cmsM = new CMSModel();
			cmsM.setData(jobs);

			PluginModel pluginM = new PluginModel();
			pluginM.setData(jobs);

			this.createBannerData(jobs);
			this.createHomeCategoriesData(jobs);
			this.setSpotProductData(jobs);
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

	public String getBannerData() {
		return this.bannerCollection;
	}

	public void createBannerData(JSONObject jobs) {
		try {
			JSONArray dataOAj = jobs.getJSONArray("home_banner");

			// this.bannerCollection = new SimiCollection();
			// for (int i = 0; i < dataOAj.length(); i++) {
			// SimiEntity entity = new SimiEntity();
			// entity.setJSONObject(dataOAj.getJSONObject(i));
			// this.bannerCollection.addEntity(entity);
			// }
			this.bannerCollection = dataOAj.toString();

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getHomeCategoriesData() {
		return this.homeCategoriesCollection;
	}

	public void createHomeCategoriesData(JSONObject jobs) {
		try {
			JSONArray dataOAj = jobs.getJSONArray("home_categories");
			// this.homeCategoriesCollection = new SimiCollection();
			// for (int i = 0; i < dataOAj.length(); i++) {
			// SimiEntity entity = new SimiEntity();
			// entity.setJSONObject(dataOAj.getJSONObject(i));
			// this.homeCategoriesCollection.addEntity(entity);
			// }
			this.homeCategoriesCollection = dataOAj.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getSpotProductData() {
		return this.mProductList;
	}

	public void setSpotProductData(JSONObject jobs) {

		try {
			JSONArray dataOAj = jobs.getJSONArray("home_spot_product");
			this.mProductList = dataOAj.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public SimiCollection convertBannerdata(String jSonData) {
		if (jSonData == null)
			return null;
		try {
			JSONArray jsAr = new JSONArray(jSonData);
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

	public ArrayList<ProductList> convertSpotdata(String jSonData) {
		if (jSonData == null)
			return null;
		try {
			JSONArray jsAr = new JSONArray(jSonData);
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

	public SimiCollection convertHomeCatedata(String jSonData) {
		if (jSonData == null)
			return null;
		try {
			JSONArray jsAr = new JSONArray(jSonData);
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
