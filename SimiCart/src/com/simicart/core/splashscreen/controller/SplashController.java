package com.simicart.core.splashscreen.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.simicart.core.base.delegate.ModelDelegate;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.cms.entity.Cms;
import com.simicart.core.common.ReadXMLLanguage;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Config;
import com.simicart.core.config.Constants;
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
import com.simicart.core.splashscreen.model.PluginModel;
import com.simicart.core.splashscreen.model.SaveCurrencyModel;
import com.simicart.core.splashscreen.model.StoreViewModel;
import com.simicart.core.store.entity.Stores;

public class SplashController {

	protected SplashDelegate mDelegate;
	protected Context mContext;
	protected boolean flagHome = false;

	public SplashController(SplashDelegate delegate, Context context) {
		mDelegate = delegate;
		mContext = context;
	}

	public void create() {
		initCommon();

		String id = DataLocal.getCurrencyID();
		flagHome = false;
		if (id != null && !id.equals("")) {
			Log.e("AAAAAAA SplashController", "saveCurrency" + id);
			saveCurrency(id);
		} else {
			Log.e("BBBBBBB SplashController", "saveCurrency" + id);
			getCMSPage();
			getPlugin();
			// getStore();
			getStoreView();
		}
	}

	private void saveCurrency(String id) {
		SaveCurrencyModel model = new SaveCurrencyModel();
		model.setDelegate(new ModelDelegate() {

			@Override
			public void callBack(String message, boolean isSuccess) {
				Log.e("AAAAAAA SplashController", "callBack SAVE CURRENTCY"
						+ message);
				getCMSPage();
				getPlugin();
				// getStore();
				getStoreView();
			}
		});

		model.addParam("currency", id);

		model.request();
	}

	private void initCommon() {
		DataLocal.init(mContext);
		Config.getInstance().setBaseUrl();
		EventListener.listEvent.clear();
		UtilsEvent.itemsList.clear();
		DataLocal.listCms.clear();
		EventListener.setEvent("simi_developer");
		DataLocal.dataJson.clear();
	}

	private void getPlugin() {
		ReadXML readXml = new ReadXML(mContext);
		readXml.read();

		final PluginModel model = new PluginModel();
		// model.setDebugMode(true);
		model.setDelegate(new ModelDelegate() {

			@Override
			public void callBack(String message, boolean isSuccess) {
				if (isSuccess) {
					ArrayList<SimiEntity> entity = model.getCollection()
							.getCollection();
					if (entity.size() > 0) {
						for (SimiEntity simiEntity : entity) {
							EventListener.setEvent(simiEntity
									.getData(Constants.SKU));
						}
					}
				}
				if (flagHome) {
					Log.e("SplashController",
							"call MainActicity from GetPlugin");
					mDelegate.creatMain();
				} else {
					flagHome = true;
				}
			}
		});
		model.request();
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
				}
			}
		});
		model.request();
	}

	public void getStoreView() {
		final StoreViewModel model = new StoreViewModel();
		// model.setDebugMode(true);
		ModelDelegate delegate = new ModelDelegate() {

			@Override
			public void callBack(String message, boolean isSuccess) {
				if (isSuccess) {

					SimiEntity entity = model.getCollection().getCollection()
							.get(0);
					try {
						parseJSONStoreView(entity);
						CacheBlock cacheBlock = new CacheBlock();
						EventBlock eventBlock = new EventBlock();
						eventBlock
								.dispatchEvent(
										"com.simicart.splashscreen.controller.SplashController",
										cacheBlock);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					createLanguage();
					if (flagHome) {
						Log.e("SplashController",
								"call MainActicity from GetPlugin");
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

		// longtb cloud 13-10-15
		// theme configuration
		if (entity.getJSONObject().has("theme_config")) {
			// DataLocal.isCloud = true;
			JSONObject js_theme_config = entity.getJSONObject().getJSONObject(
					"theme_config");
			parserThemeConfig(js_theme_config);
		}
	}

	private void parserThemeConfig(JSONObject js_theme_config)
			throws JSONException {
		if (getDataColor(js_theme_config, "key_color"))
			Config.getInstance().setKey_color(
					js_theme_config.getString("key_color"));
		if (getDataColor(js_theme_config, "top_menu_icon_color"))
			Config.getInstance().setTop_menu_icon_color(
					js_theme_config.getString("top_menu_icon_color"));
		if (getDataColor(js_theme_config, "button_background"))
			Config.getInstance().setButton_background(
					js_theme_config.getString("button_background"));
		if (getDataColor(js_theme_config, "button_text_color"))
			Config.getInstance().setButton_text_color(
					js_theme_config.getString("button_text_color"));
		if (getDataColor(js_theme_config, "menu_background"))
			Config.getInstance().setMenu_background(
					js_theme_config.getString("menu_background"));
		if (getDataColor(js_theme_config, "menu_text_color"))
			Config.getInstance().setMenu_text_color(
					js_theme_config.getString("menu_text_color"));
		if (getDataColor(js_theme_config, "menu_line_color"))
			Config.getInstance().setMenu_line_color(
					js_theme_config.getString("menu_line_color"));
		if (getDataColor(js_theme_config, "menu_icon_color"))
			Config.getInstance().setMenu_icon_color(
					js_theme_config.getString("menu_icon_color"));
		if (getDataColor(js_theme_config, "app_background"))
			Config.getInstance().setApp_backrground(
					js_theme_config.getString("app_background"));
		if (getDataColor(js_theme_config, "content_color"))
			Config.getInstance().setContent_color(
					js_theme_config.getString("content_color"));
		if (getDataColor(js_theme_config, "line_color"))
			Config.getInstance().setLine_color(
					js_theme_config.getString("line_color"));
		if (getDataColor(js_theme_config, "image_boder_color"))
			Config.getInstance().setImage_boder_color(
					js_theme_config.getString("image_boder_color"));
		if (getDataColor(js_theme_config, "icon_color"))
			Config.getInstance().setIcon_color(
					js_theme_config.getString("icon_color"));
		if (getDataColor(js_theme_config, "section_color"))
			Config.getInstance().setSection_color(
					js_theme_config.getString("section_color"));
		if (getDataColor(js_theme_config, "price_color"))
			Config.getInstance().setPrice_color(
					js_theme_config.getString("price_color"));
		if (getDataColor(js_theme_config, "special_price_color"))
			Config.getInstance().setSpecial_price_color(
					js_theme_config.getString("special_price_color"));
		if (getDataColor(js_theme_config, "search_box_background"))
			Config.getInstance().setSearch_box_background(
					js_theme_config.getString("search_box_background"));
		if (getDataColor(js_theme_config, "out_stock_background"))
			Config.getInstance().setOut_stock_background(
					js_theme_config.getString("out_stock_background"));
		if (getDataColor(js_theme_config, "out_stock_text"))
			Config.getInstance().setOut_stock_text(
					js_theme_config.getString("out_stock_text"));
		if (getDataColor(js_theme_config, "search_text_color"))
			Config.getInstance().setSearch_text_color(
					js_theme_config.getString("search_text_color"));
		if (getDataColor(js_theme_config, "search_icon_color"))
			Config.getInstance().setSearch_icon_color(
					js_theme_config.getString("search_icon_color"));

	}

	private boolean getDataColor(JSONObject jsonObject, String key)
			throws JSONException {
		if (jsonObject.has(key)) {
			String color = jsonObject.getString(key);
			if (color != null && !color.equals("null") && !color.equals("")) {
				return true;
			}
		}
		return false;
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
