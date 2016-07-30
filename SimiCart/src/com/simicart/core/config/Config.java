package com.simicart.core.config;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.graphics.Color;

import com.simicart.core.common.Utils;

@SuppressLint("DefaultLocale")
public class Config {

	private String mBaseUrl = "https://jadebloom.com/index.php/";
    private String mSecretKey = "f86f87da4915da2215305a61d81cdc9aff90be53";
	private String mBaseCloudUrl = "https://api.jajahub.com/rest/";
	private String mSecretCloudKey = "a98634abb7afc2c8b3434e8cf0ed694c8c23dc1e";

	private String mThemeColor = "#000000";
	private String key_color = "#FFFFFF";
	private String top_menu_icon_color = "#FFFFFF";
	private String button_background = "";
	private String button_text_color = "#FFFFFF";
	private String menu_background = "#F21b1b1b";
	private String menu_text_color = "#ECEFF1";
	private String menu_line_color = "#444444";
	private String menu_icon_color = "#ECEFF1";
	private String app_backrground = "#FFFFFF";
	private String content_color = "#000000";
	private String line_color = "#CACACA";
	private String image_boder_color = "#d3d3d3";
	private String icon_color = "#000000";
	private String section_color = "#E0E0E0";
	private String section_text_color = "#000000";
	private String price_color = "#BF360C";
	private String special_price_color = "#BF360C";
	private String search_box_background = "#E6E0E0E0";
	private String out_stock_background = "#FF9800";
	private String out_stock_text = "#FFFFFF";
	private String search_text_color = "#8b8b8b";
	private String search_icon_color = "#8b8b8b";

	public int getTop_menu_icon_color() {
		return Color.parseColor(top_menu_icon_color);
	}

	public void setTop_menu_icon_color(String top_menu_icon_color) {
		this.top_menu_icon_color = top_menu_icon_color;
	}

	public int getSection_text_color() {
		return Color.parseColor(section_text_color);
	}

	public void setSection_text_color(String section_text_color) {
		this.section_text_color = section_text_color;
	}

	public int getKey_color() {
		return Color.parseColor(key_color);
	}

	public void setKey_color(String key_color) {
		this.key_color = key_color;
	}

	public int getButton_background() {
		if (Utils.validateString(button_background)) {
			return Color.parseColor(button_background);
		} else {
			return Color.parseColor(key_color);
		}
	}

	public void setButton_background(String button_background) {
		this.button_background = button_background;
	}

	public int getButton_text_color() {
		return Color.parseColor(button_text_color);
	}

	public void setButton_text_color(String button_text_color) {
		this.button_text_color = button_text_color;
	}

	public int getMenu_background() {
		return Color.parseColor(menu_background);
	}

	public void setMenu_background(String menu_background) {
		this.menu_background = menu_background;
	}

	public int getMenu_text_color() {
		return Color.parseColor(menu_text_color);
	}

	public void setMenu_text_color(String menu_text_color) {
		this.menu_text_color = menu_text_color;
	}

	public int getMenu_line_color() {
		return Color.parseColor(menu_line_color);
	}

	public void setMenu_line_color(String menu_line_color) {
		this.menu_line_color = menu_line_color;
	}

	public int getMenu_icon_color() {
		return Color.parseColor(menu_icon_color);
	}

	public void setMenu_icon_color(String menu_icon_color) {
		this.menu_icon_color = menu_icon_color;
	}

	public int getApp_backrground() {
		return Color.parseColor(app_backrground);
	}

	public void setApp_backrground(String app_backrground) {
		this.app_backrground = app_backrground;
	}

	public int getContent_color() {
		return Color.parseColor(content_color);
	}

	public int getHintContent_color() {
		String color = content_color.replace("#", "#7D");
		return Color.parseColor(color);
	}

	public String getContent_color_string() {
		return content_color;
	}

	public void setContent_color(String content_color) {
		this.content_color = content_color;
	}

	public int getLine_color() {
		return Color.parseColor(line_color);
	}

	public void setLine_color(String line_color) {
		this.line_color = line_color;
	}

	public int getImage_boder_color() {
		return Color.parseColor(image_boder_color);
	}

	public void setImage_boder_color(String image_boder_color) {
		this.image_boder_color = image_boder_color;
	}

	public String getIcon_color() {
		return icon_color;
	}

	public void setIcon_color(String icon_color) {
		this.icon_color = icon_color;
	}

	public String getSection_color() {
		return section_color;
	}

	public void setSection_color(String section_color) {
		this.section_color = section_color;
	}

	public String getPrice_color() {
		return price_color;
	}

	public void setPrice_color(String price_color) {
		this.price_color = price_color;
	}

	public String getSpecial_price_color() {
		return special_price_color;
	}

	public void setSpecial_price_color(String special_price_color) {
		this.special_price_color = special_price_color;
	}

	public int getSearch_box_background() {
		return Color.parseColor(search_box_background);
	}

	public void setSearch_box_background(String search_box_background) {
		this.search_box_background = search_box_background;
	}

	public int getOut_stock_background() {
		if (DataLocal.isCloud) {
			return Color.parseColor(out_stock_background);
		} else {
			return Color.parseColor(key_color);
		}
	}

	public void setOut_stock_background(String out_stock_background) {
		this.out_stock_background = out_stock_background;
	}

	public int getOut_stock_text() {
		return Color.parseColor(out_stock_text);
	}

	public void setOut_stock_text(String out_stock_text) {
		this.out_stock_text = out_stock_text;
	}

	public int getSearch_text_color() {
		return Color.parseColor(search_text_color);
	}

	public void setSearch_text_color(String search_text_color) {
		this.search_text_color = search_text_color;
	}

	public int getSearch_icon_color() {
		return Color.parseColor(search_text_color);
	}

	public void setSearch_icon_color(String search_icon_color) {
		this.search_icon_color = search_icon_color;
	}

	private String mDemoEnable = "DEMO_ENABLE";
	private String mSenderID = "";
	private String mColorPrice = "#F31A1A";
	private String mColorIconMenu = "#FFFFFF";
	private String mColorMenuTop = "#FFFFFF";
	private String mColorSplashScreen = "#123456";
	// private String mFontCustom = "fonts/atmostsphere.ttf";
	private String mFontCustom = "fonts/ProximaNovaLight.ttf";

	private String mCountryName;
	private String mCountryCode;
	private String mCurrencySymbol;
	private String mCurrencyCode;
	private String mDefaulList;

	private int mStoreID;
	private String mStoreName;
	private String mLocaleIdentifier;
	private String mUseStore;
	private String mCookie = "";

	private int mGuestCheckout = 1;
	private int mEnableAgreements = 0;

	private boolean isShowZeroPrice = true;
	private boolean isShowLinkAllProduct = false;
	private boolean isReloadPaymentMethod = false;
	private String mCurrencyPosition = "before";
	private Map<String, String> mLanguages;

	private int mTheme = 0; // 0 : default, 1 : matrix , 2 ztheme

	private static Config instance;

	private Config() {
		mLanguages = new HashMap<String, String>();
	}

	public static Config getInstance() {
		if (null == instance) {
			instance = new Config();
		}

		return instance;
	}

	public String getDefaultList() {
		return mDefaulList;
	}

	public void setDefaultList(String mDefaulList) {
		this.mDefaulList = mDefaulList;
	}

	public String getDemoEnable() {
		return mDemoEnable;
	}

	public void setDemoEnable(String demo_enable) {
		mDemoEnable = demo_enable;
	}

	public int getTheme() {
		return mTheme;
	}

	public void setTheme(int mTheme) {
		this.mTheme = mTheme;
	}

	public boolean isShow_link_all_product() {
		return isShowLinkAllProduct;
	}

	public void setShow_link_all_product(boolean show_link_all_product) {
		isShowLinkAllProduct = show_link_all_product;
	}

	public boolean isReload_payment_method() {
		return isReloadPaymentMethod;
	}

	public void setReload_payment_method(boolean isReloadPaymentMethod) {
		this.isReloadPaymentMethod = isReloadPaymentMethod;
	}

	public boolean isShow_zero_price() {
		return isShowZeroPrice;
	}

	public void setIs_show_zero_price(boolean show_zero_price) {
		isShowZeroPrice = show_zero_price;
	}

	public String getSecretKey() {
		return mSecretKey;
	}

	public String getSecretCloudKey() {
		return mSecretCloudKey;
	}

	public void setSecretKey(String secret_key) {
		mSecretKey = secret_key;
	}

	public String getUse_store() {
		return mUseStore;
	}

	public void setUse_store(String use_store) {
		mUseStore = use_store;
	}

	public void setLanguages(Map<String, String> languages) {
		try {
			mLanguages = new HashMap<String, String>();
			mLanguages = languages;
		} catch (Exception e) {

		}
	}

	public String getSenderId() {
		return mSenderID;
	}

	public void setSenderId(String sender_id) {
		mSenderID = sender_id;
	}

	public void setStore_name(String store_name) {
		mStoreName = store_name;
	}

	public String getLocale_identifier() {
		return mLocaleIdentifier;
	}

	public void setLocale_identifier(String locale_identifier) {
		mLocaleIdentifier = locale_identifier;
	}

	public int getGuest_checkout() {
		return mGuestCheckout;
	}

	public void setGuest_checkout(int guest_checkout) {
		mGuestCheckout = guest_checkout;
	}

	public int getEnable_agreements() {
		return mEnableAgreements;
	}

	public void setEnable_agreements(int enable_agreements) {
		mEnableAgreements = enable_agreements;
	}

	public int getStore_id() {
		return mStoreID;
	}

	public void setStore_id(int store_id) {
		mStoreID = store_id;
	}

	public void setCurrency_symbol(String currency_symbol) {
		mCurrencySymbol = currency_symbol;
	}

	public String getCurrency_code() {
		return mCurrencyCode;
	}

	public void setCurrency_code(String currency_code) {
		mCurrencyCode = currency_code;
	}

	public String getConnectorUrl() {
		return mBaseUrl + "connector/";
	}

	public void setBaseUrl() {
		int lenght = mBaseUrl.length();
		char last = mBaseUrl.charAt(lenght - 1);
		if (last != '/') {
			mBaseUrl += "/";
		}

		int last_index = mBaseCloudUrl.length() - 1;
		char lastChar = mBaseCloudUrl.charAt(last_index);
		if (lastChar == '/') {
			mBaseCloudUrl = mBaseCloudUrl.substring(0, last_index);
		}
	}

	public String getBaseUrl() {
		return mBaseUrl;
	}

	public String getBaseCloudUrl() {
		return mBaseCloudUrl;
	}

	public void setBase_url(String base_url) {
		mBaseUrl = base_url;
	}

	public String getBannersUrl() {
		return getConnectorUrl() + "config/get_banner";
	}

	public void setColorMain(String color) {
		mThemeColor = color;
	}

	public int getColorMain() {
		return Color.parseColor(mThemeColor);
	}

	public int getColorMenu() {
		return Color.parseColor(mColorIconMenu);
	}

	public int getColorMenuTop() {
		return Color.parseColor(mColorMenuTop);
	}

	public int getColorSplash() {
		return Color.parseColor(mColorSplashScreen);
	}

	public int getColorPrice() {
		return Color.parseColor(mColorPrice);
	}

	public int getColorSort() {
		String cbase = mThemeColor.substring(1);
		return Color.parseColor("#A8" + cbase);
	}

	public String getFontCustom() {
		return mFontCustom;
	}

	public void setFontCustom(String mFontCustom) {
		this.mFontCustom = mFontCustom;
	}

	public String getText(String language) {
		if (language == null) {
			return null;
		}
		String translater = language;
		if (!mLanguages.isEmpty()) {
			if (mLanguages.get(language.toLowerCase().trim()) != null) {
				translater = mLanguages.get(language.toLowerCase().trim());
				return translater;
			}
		}
		return translater;
	}

	public String getWidthImage() {
		return "500";
	}

	public String getHeightImage() {
		return "500";
	}

	public String getPrice(String price) {
		DecimalFormat df = new DecimalFormat("#,##0.00");
		if (price == null || price.equals("null")) {
			price = "0";
		}
		float pricef = Float.parseFloat(price);
		price = df.format(pricef);
		if (mCurrencyPosition.equals("before")) {
			if ((null == mCurrencySymbol) || (mCurrencySymbol.equals("null"))
					&& null != mCurrencyCode && !mCurrencyCode.equals("null")) {
				return mCurrencyCode + price;
			} else {
				return mCurrencySymbol + price;
			}
		} else {
			if ((null == mCurrencySymbol) || (mCurrencySymbol.equals("null"))
					&& null != mCurrencyCode && !mCurrencyCode.equals("null")) {
				return price + " " + mCurrencyCode;
			} else {
				return price + " " + mCurrencySymbol;
			}
		}
		// return price + " " + mCurrencySymbol;
	}

	public String getPrice(String price, String symbol) {
		DecimalFormat df = new DecimalFormat("#,##0.00");
		if (price == null || price.equals("null")) {
			price = "0";
		}
		float pricef = Float.parseFloat(price);
		price = df.format(pricef);
		if (mCurrencyPosition.equals("before")) {
			if ((null == symbol) || (symbol.equals("null"))
					&& null != mCurrencyCode && !mCurrencyCode.equals("null")) {
				return mCurrencyCode + price;
			} else {
				return symbol + price;
			}
		} else {
			if ((null == mCurrencySymbol) || (mCurrencySymbol.equals("null"))
					&& null != mCurrencyCode && !mCurrencyCode.equals("null")) {
				return price + " " + mCurrencyCode;
			} else {
				return price + " " + mCurrencySymbol;
			}
		}
		// return price + " " + symbol;
	}

	public String getCountryName() {
		return mCountryName;
	}

	public void setCountryName(String mCountryName) {
		this.mCountryName = mCountryName;
	}

	public String getCurrencyCode() {
		return mCurrencyCode;
	}

	public void setCurrencyCode(String mCurrencyCode) {
		this.mCurrencyCode = mCurrencyCode;
	}

	public String getStoreName() {
		return mStoreName;
	}

	public void setStoreName(String mStoreName) {
		this.mStoreName = mStoreName;
	}

	public String getCountryCode() {
		return mCountryCode;
	}

	public void setCountryCode(String mCountryCode) {
		this.mCountryCode = mCountryCode;
	}

	public String getCurrencyPosition() {
		return mCurrencyPosition;
	}

	public void setCurrencyPosition(String mCurrencyPosition) {
		this.mCurrencyPosition = mCurrencyPosition;
	}

	public String getCookie() {
		return mCookie;
	}

	public void setCookie(String _cookie) {
		this.mCookie = _cookie;
	}

}
