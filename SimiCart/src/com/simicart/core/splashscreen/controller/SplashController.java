package com.simicart.core.splashscreen.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.simicart.core.base.delegate.ModelDelegate;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.base.networkcloud.request.error.SimiError;
import com.simicart.core.cms.entity.Cms;
import com.simicart.core.common.ReadXMLLanguage;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Config;
import com.simicart.core.config.DataLocal;
import com.simicart.core.customer.entity.ConfigCustomerAddress;
import com.simicart.core.customer.entity.GenderConfig;
import com.simicart.core.event.base.EventListener;
import com.simicart.core.event.base.ReadXML;
import com.simicart.core.event.base.UtilsEvent;
import com.simicart.core.event.block.CacheBlock;
import com.simicart.core.event.block.EventBlock;
import com.simicart.core.splashscreen.delegate.SplashDelegate;
import com.simicart.core.splashscreen.model.CMSPageModel;
import com.simicart.core.splashscreen.model.ConfigModelCloud;
import com.simicart.core.splashscreen.model.GetIDPluginsModel;
import com.simicart.core.splashscreen.model.GetSKUPluginModel;
import com.simicart.core.splashscreen.model.SaveCurrencyModel;
import com.simicart.core.splashscreen.model.StoreViewModel;
import com.simicart.core.store.entity.Stores;

import android.content.Context;
import android.util.Log;

public class SplashController {

	protected SplashDelegate mDelegate;
	protected Context mContext;
	protected boolean flagHome = false;
	protected boolean flagGetTheme = false;

	private String MATRIX_THEME = "matrix";
	private String ZARA_THEME = "zara";

	public SplashController(SplashDelegate delegate, Context context) {
		mDelegate = delegate;
		mContext = context;
	}

	public void create() {
		initCommon();

		getAppConfig();
		getAvaiablePlugin();

		flagHome = false;
		flagGetTheme = false;

		String id = DataLocal.getCurrencyID();
		if (id != null && !id.equals("")) {
			saveCurrency(id);
		} else {
			getCMSPage();
			getStoreView();
		}
	}

	private void getAppConfig() {
		final ConfigModelCloud appConfigModel = new ConfigModelCloud();
		appConfigModel
				.setDelegate(new com.simicart.core.base.networkcloud.delegate.ModelDelegate() {
					@Override
					public void onFail(SimiError error) {
						if (error != null) {
							SimiManager.getIntance().showNotify(null,
									error.getMessage(), "Ok");
						}
					}

					@Override
					public void onSuccess(SimiCollection collection) {
						if (collection != null) {
							getTheme(appConfigModel.getLayout());
						}
					}
				});

		appConfigModel.request();
	}

	private void getTheme(String layout) {
		if (!flagGetTheme) {
			ReadXML readXml = new ReadXML(mContext);
			readXml.read();
			flagGetTheme = true;
		}

		if (layout.equals(MATRIX_THEME)) {
			EventListener.setEvent("simi_themeone");
		} else if (layout.equals(ZARA_THEME)) {
			EventListener.setEvent("simi_ztheme");
		}
	}

	private void getAvaiablePlugin() {
		// http://dev-api.jajahub.com/rest/site-plugins
		// lay ve danh sach id cua plugin enable
		final GetIDPluginsModel idsModel = new GetIDPluginsModel();
		idsModel.setDelegate(new com.simicart.core.base.networkcloud.delegate.ModelDelegate() {
			@Override
			public void onFail(SimiError error) {
				if (flagHome) {
					callEvent();
					mDelegate.creatMain();
				} else {
					flagHome = true;
				}
			}

			@Override
			public void onSuccess(SimiCollection collection) {
				String ids = idsModel.getIDs();
				if (!flagGetTheme) {
					ReadXML readXml = new ReadXML(mContext);
					readXml.read();
					flagGetTheme = true;
				}
				if (Utils.validateString(ids)) {
					getSKUPlugin(ids);
				} else {
					if (flagHome) {
						callEvent();
						mDelegate.creatMain();
					} else {
						flagHome = true;
					}
				}
			}
		});

		idsModel.addDataParameter("limit", "100");
		idsModel.addDataParameter("offset", "0");
		idsModel.request();

	}

	private void getSKUPlugin(String ids) {
		// http://dev-api.jajahub.com/rest/public_plugins
		// lay sku cua cac plugin enbale

		final GetSKUPluginModel skuModel = new GetSKUPluginModel();
		skuModel.setDelegate(new com.simicart.core.base.networkcloud.delegate.ModelDelegate() {
			@Override
			public void onFail(SimiError error) {

			}

			@Override
			public void onSuccess(SimiCollection collection) {
				ArrayList<String> mSKUs = skuModel.getListSKU();
				if (mSKUs.size() > 0) {
					for (String mSKU : mSKUs) {
						EventListener.setEvent(mSKU);
					}
				}

				if (flagHome) {
					callEvent();
					mDelegate.creatMain();
				} else {
					flagHome = true;
				}
			}
		});

		skuModel.addDataParameter("limit", "100");
		skuModel.addDataParameter("offset", "0");
		skuModel.addDataParameter("ids", ids);
		skuModel.request();

	}

	private void saveCurrency(String id) {
		SaveCurrencyModel model = new SaveCurrencyModel();
		model.setDelegate(new ModelDelegate() {

			@Override
			public void callBack(String message, boolean isSuccess) {
				getCMSPage();
				getStoreView();
			}
		});

		model.addParam("currency", id);

		model.request();
	}

	private void callEvent() {
		CacheBlock cacheBlock = new CacheBlock();
		EventBlock eventBlock = new EventBlock();
		eventBlock.dispatchEvent(
				"com.simicart.splashscreen.controller.SplashController",
				cacheBlock);
	}

	private void initCommon() {
		DataLocal.init(mContext);
		Config.getInstance().setBaseUrl();
		EventListener.listEvent.clear();
		UtilsEvent.itemsList.clear();
		DataLocal.listCms.clear();
		EventListener.setEvent("simi_developer");
	}

	private void getCMSPage() {
		DataLocal.listCms.clear();
		final CMSPageModel model = new CMSPageModel();
		model.setDelegate(new ModelDelegate() {

			@Override
			public void callBack(String message, boolean isSuccess) {
				if (isSuccess) {

					ArrayList<SimiEntity> entity = model.getCollection()
							.getCollection();
					if (entity.size() > 0) {
						for (SimiEntity simiEntity : entity) {
							Cms cms = new Cms();
							cms.setJSONObject(simiEntity.getJSONObject());
							DataLocal.listCms.add(cms);
						}
					}
					Log.e("Data local cms", "++" + DataLocal.listCms.size());
					SimiManager.getIntance().onUpdateCms();
				}
			}
		});
		model.request();
	}

	public void getStoreView() {
		final StoreViewModel model = new StoreViewModel();
		ModelDelegate delegate = new ModelDelegate() {

			@Override
			public void callBack(String message, boolean isSuccess) {
				if (isSuccess) {

					SimiEntity entity = model.getCollection().getCollection()
							.get(0);
					try {
						parseJSONStoreView(entity);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					createLanguage();
					if (flagHome) {
						callEvent();
						mDelegate.creatMain();
					} else {
						flagHome = true;
					}
				}
			}
		};

		model.setDelegate(delegate);
		String id = DataLocal.getStoreID();
		if (Utils.validateString(id)) {
			model.addParam("store_id", id);
		}
		model.request();

	}

	private void parseJSONStoreView(SimiEntity entity) throws JSONException {
		JSONObject js_storeView = entity.getJSONObject();

		// hien thi list hoac grid product
		String check = "0";
		if (js_storeView.has("view_products_default")) {
			check = entity.getData("view_products_default");
		}
		Config.getInstance().setDefaultList(check);

		// store configuration
		JSONObject js_store_config = entity.getJSONObject().getJSONObject(
				"store_config");

		Config.getInstance().setCurrency_code(
				js_store_config.getString("currency_code"));
		DataLocal.saveCurrencyID(Config.getInstance().getCurrencyCode());
		Config.getInstance().setStore_id(js_store_config.getInt("store_id"));
		Config.getInstance().setStore_name(
				js_store_config.getString("store_name"));
		Config.getInstance().setLocale_identifier(
				js_store_config.getString("locale_identifier"));

		Config.getInstance().setCurrency_symbol(
				js_store_config.getString("currency_symbol"));
		if (js_store_config.has("use_store")) {
			Config.getInstance().setUse_store(
					js_store_config.getString("use_store"));
		} else {
			Config.getInstance().setUse_store("0");
		}
		if (js_store_config.has("is_rtl")) {
			if (js_store_config.getString("is_rtl").equals("1"))
				DataLocal.isLanguageRTL = true;
		}

		if (js_store_config.has("is_show_zero_price")) {
			if (js_store_config.getString("is_show_zero_price").equals("0")) {
				Config.getInstance().setIs_show_zero_price(false);
			} else {
				Config.getInstance().setIs_show_zero_price(true);
			}
		}
		if (js_store_config.has("is_show_link_all_product")) {
			if (js_store_config.getString("is_show_link_all_product").equals(
					"0")) {
				Config.getInstance().setShow_link_all_product(false);
			} else {
				Config.getInstance().setShow_link_all_product(true);
			}
		}
		if (js_store_config.has("currency_position")) {
			Config.getInstance().setCurrencyPosition(
					js_store_config.getString("currency_position"));
		}
		if (js_store_config.has("is_reload_payment_method")) {
			if (js_store_config.getString("is_reload_payment_method").equals(
					"0")) {
				Config.getInstance().setReload_payment_method(false);
			} else {
				Config.getInstance().setReload_payment_method(true);
			}
		}

		// Customer address configuration
		JSONObject js_customer_address_config = js_storeView
				.getJSONObject("customer_address_config");
		ConfigCustomerAddress configCustomerAddress = new ConfigCustomerAddress();
		configCustomerAddress.setJSONObject(js_customer_address_config);
		configCustomerAddress.setPrefix(js_customer_address_config
				.getString("prefix_show"));
		configCustomerAddress.setSuffix(js_customer_address_config
				.getString("suffix_show"));
		configCustomerAddress.setDob(js_customer_address_config
				.getString("dob_show"));
		configCustomerAddress.setGender(js_customer_address_config
				.getString("gender_show"));
		configCustomerAddress
				.setGenderConfigs(getGenderConfigs(js_customer_address_config
						.getJSONArray("gender_value")));
		if (js_customer_address_config.has("taxvat_show")) {
			try {
				String tav_show = js_customer_address_config
						.getString("taxvat_show");
				if (Utils.validateString(tav_show)) {
					configCustomerAddress.setTaxvat(tav_show);
				}
			} catch (Exception e) {
				configCustomerAddress
						.setTaxvat(ConfigCustomerAddress.OPTION_HIDE);
			}
		}

		// checkout configuration
		JSONObject js_checkout_config = js_storeView
				.getJSONObject("checkout_config");
		Config.getInstance().setGuest_checkout(
				js_checkout_config.getInt("enable_guest_checkout"));
		Config.getInstance().setEnable_agreements(
				js_checkout_config.getInt("enable_agreements"));
		if (js_storeView.has("android_sender")) {
			Config.getInstance().setSenderId(
					js_storeView.getString("android_sender"));
		}
		if (js_checkout_config.has("taxvat_show")) {
			try {
				if ((js_checkout_config.getString("taxvat_show").equals("1"))) {
					configCustomerAddress
							.setVat_id(ConfigCustomerAddress.OPTION_REQUIRE);
				}
			} catch (Exception e) {
			}
		}

		DataLocal.ConfigCustomerAddress = configCustomerAddress;
		DataLocal.ConfigCustomerProfile = configCustomerAddress;

		if (Config.getInstance().getUse_store().equals("1")) {
			changeBaseUrl();
		}
	}

	private ArrayList<GenderConfig> getGenderConfigs(JSONArray jsonArray)
			throws JSONException {
		int n = jsonArray.length();
		ArrayList<GenderConfig> genderList = new ArrayList<GenderConfig>();
		for (int i = 0; i < n; i++) {
			GenderConfig gender = new GenderConfig();
			JSONObject js_item = jsonArray.getJSONObject(i);
			gender.setLabel(js_item.getString("label"));
			gender.setValue(js_item.getString("value"));
			genderList.add(gender);
		}
		return genderList;
	}

	private void changeBaseUrl() {
		for (Stores store : DataLocal.listStores) {
			if (store.getStoreID().equals(
					"" + Config.getInstance().getStore_id())) {
				int leg = Config.getInstance().getBaseUrl().split("/").length;
				String last = Config.getInstance().getBaseUrl().split("/")[leg - 1];
				Config.getInstance().setBase_url(
						Config.getInstance().getBaseUrl()
								.replace(last, store.getStoreCode()));
			}
		}
	}

	private void createLanguage() {
		try {
			ReadXMLLanguage readlanguage = new ReadXMLLanguage(mContext);
			readlanguage.parseXML(Config.getInstance().getLocale_identifier()
					+ "/localize.xml");
			Config.getInstance().setLanguages(readlanguage.getLanguages());
		} catch (Exception e) {
			Map<String, String> languages = new HashMap<String, String>();
			Config.getInstance().setLanguages(languages);
		}

	}
}
