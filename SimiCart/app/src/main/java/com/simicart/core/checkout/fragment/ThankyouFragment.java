package com.simicart.core.checkout.fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.checkout.controller.ConfigCheckout;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Config;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.fragment.OrderHistoryDetailFragment;
import com.simicart.core.material.ButtonRectangle;
import com.simicart.core.material.LayoutRipple;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ThankyouFragment extends SimiFragment implements OnKeyListener{

	private TextView txt_title_message;
	private TextView txt_order;
	private LayoutRipple layout_order;
	private TextView txt_content_message;
	private ButtonRectangle btn_continue_shopping;
	private ImageView imageView;

	private String message = "";
	private JSONObject jsonObject;
	
	private String invoice_number = "";

	public static ThankyouFragment newInstance(String message, JSONObject jsonObject) {
		ThankyouFragment fragment = new ThankyouFragment();
		fragment.setTargetFragment(fragment, ConfigCheckout.TARGET_REVIEWORDER);
		Bundle bundle= new Bundle();
		setData(Constants.KeyData.MESSAGE, message, Constants.KeyData.TYPE_STRING, bundle);
		if(jsonObject != null){
		setData(Constants.KeyData.JSON_FILTER, jsonObject.toString(), Constants.KeyData.TYPE_JSONOBJECT, bundle);
		}
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(
				Rconfig.getInstance().layout("core_thankyou_layout"),
				container, false);
		if(getArguments() != null){
		message = (String) getData(Constants.KeyData.MESSAGE, Constants.KeyData.TYPE_STRING, getArguments());
		String json = (String) getData(Constants.KeyData.JSON_FILTER, Constants.KeyData.TYPE_JSONOBJECT, getArguments());
		try {
			jsonObject = new JSONObject(json);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		
		view.setBackgroundColor(Config.getInstance().getApp_backrground());
		view.setOnKeyListener(this);
		parseJson(jsonObject);
		initView(view);
		handleEvent();
		return view;
	}

	private void initView(View rootView) {
		txt_title_message = (TextView) rootView.findViewById(Rconfig
				.getInstance().id("txt_title_message"));
		txt_order = (TextView) rootView.findViewById(Rconfig.getInstance().id(
				"txt_order"));
		txt_content_message = (TextView) rootView.findViewById(Rconfig
				.getInstance().id("txt_content_message"));
		txt_title_message.setTextColor(Config.getInstance().getContent_color());
		txt_order.setTextColor(Config.getInstance().getContent_color());
		txt_content_message.setTextColor(Config.getInstance().getContent_color());
		layout_order = (LayoutRipple) rootView.findViewById(Rconfig
				.getInstance().id("layout_order"));
		btn_continue_shopping = (ButtonRectangle) rootView.findViewById(Rconfig
				.getInstance().id("btn_continue_shopping"));
		btn_continue_shopping.setText(Config.getInstance().getText("Continue Shopping"));
		btn_continue_shopping.setTextColor(Color.parseColor("#ffffff"));
		btn_continue_shopping.setBackgroundColor(Config.getInstance()
				.getColorMain());
		imageView = (ImageView) rootView.findViewById(Rconfig
				.getInstance().id("img_order"));
		Utils.changeColorImageview(SimiManager.getIntance().getCurrentContext(), imageView, "ic_extend");
		//set data
		txt_title_message.setText(Config.getInstance().getText(message));
		txt_order.setText(Config.getInstance().getText("Your order # is:")+ invoice_number);
		txt_content_message.setText(Config.getInstance().getText("You will receive an order confirmation email with detail of your order and alink to track its progress"));
		if(DataLocal.isSignInComplete()){
			layout_order.setVisibility(View.VISIBLE);
		}else{
			layout_order.setVisibility(View.GONE);
		}
	}
	
	private void parseJson(JSONObject object){
		try {
			if(object.getString("status").equals("SUCCESS")){
			JSONArray arrayData = object.getJSONArray("data");
			if(arrayData.length() > 0){
				JSONObject jsonObject = arrayData.getJSONObject(0);
				if(jsonObject.has("invoice_number")){
					invoice_number = jsonObject.getString("invoice_number");
				}
			}
			}
		} catch (Exception e) {
			Log.e("Error paser json:", e.getMessage());
		}
	}

	private void handleEvent() {
		btn_continue_shopping.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
					SimiManager.getIntance().backToHomeFragment();
			}
		});
		layout_order.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				OrderHistoryDetailFragment fragment = OrderHistoryDetailFragment
						.newInstance(ConfigCheckout.TARGET_REVIEWORDER, invoice_number.trim());
//				fragment.setID(invoice_number.trim());
				if (DataLocal.isTablet) {
					SimiManager.getIntance().replacePopupFragment(fragment);
				} else {
					SimiManager.getIntance().replaceFragment(fragment);
				}
			}
		});
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK	) {
			SimiManager.getIntance().showToast("Thankyou page back");
			return true;
		}
		return true;
	}
	

}
