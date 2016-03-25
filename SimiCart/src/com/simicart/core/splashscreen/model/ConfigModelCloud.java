package com.simicart.core.splashscreen.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.simicart.core.base.model.SimiModelCloud;
import com.simicart.core.base.networkcloud.request.multi.SimiRequest;
import com.simicart.core.config.Config;

public class ConfigModelCloud extends SimiModelCloud {

	String mLayout = "default";

	public String getLayout() {
		return mLayout;
	}

	@Override
	protected void setUrlAction() {
		addDataExtendURL("app-configs/");
	}

	@Override
	protected void setTypeMethod() {
		mTypeMethod = SimiRequest.Method.GET;
	}

	@Override
	protected void paserData() {
		super.paserData();
		if (mJSONResult != null) {
			Log.e("AppConfigModel", mJSONResult.toString());
			if (mJSONResult.has("app-configs")) {
				try {
					JSONArray appConfigArr = mJSONResult
							.getJSONArray("app-configs");
					if (appConfigArr != null) {
						if (appConfigArr.length() > 0) {
							for (int i = 0; i < appConfigArr.length(); i++) {
								JSONObject jsonObject = appConfigArr
										.getJSONObject(0);
								parserThemeConfig(jsonObject
										.getJSONObject("theme"));
								if (jsonObject.has("layout")) {
									mLayout = jsonObject.getString("layout");
								}
							}
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void parserThemeConfig(JSONObject js_theme_config)
			throws JSONException {
		if (getDataColor(js_theme_config, "key_color")) {
			Config.getInstance().setKey_color(
					js_theme_config.getString("key_color"));
			Config.getInstance().setColorMain(
					js_theme_config.getString("key_color"));
		}
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
}
