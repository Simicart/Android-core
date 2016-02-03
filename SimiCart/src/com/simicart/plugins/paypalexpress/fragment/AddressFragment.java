package com.simicart.plugins.paypalexpress.fragment;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.delegate.ModelDelegate;
import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.checkout.entity.ShippingMethod;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Config;
import com.simicart.core.config.Constants;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.entity.MyAddress;
import com.simicart.plugins.paypalexpress.model.RequestUpdateAddressModel;

public class AddressFragment extends SimiFragment {
	View mImageView;

	Button bt_updateAddress;

	MyAddress shippingAddress = new MyAddress();
	MyAddress billingAddress = new MyAddress();
	public static int SHIPPING = 1;
	public static int BILLING = 2;

	public static AddressFragment newInstance (MyAddress shippingAddress, MyAddress billingAddress){
		AddressFragment fragment = new AddressFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable(Constants.KeyData.SHIPPING_ADDRESS, shippingAddress);
		bundle.putSerializable(Constants.KeyData.BILLING_ADDRESS, billingAddress);
		fragment.setArguments(bundle);
		return fragment;
	}
	@Override
	public View onCreateView(final LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(
				Rconfig.getInstance().layout(
						"plugin_paypalexpress_fragment_address_list"),
				container, false);

		TextView tv_billing_chooseAddress = (TextView) rootView
				.findViewById(Rconfig.getInstance().id("billing_chooseAddress"));
		tv_billing_chooseAddress.setText(Config.getInstance().getText(
				"Billing Address"));

		TextView tv_shipping_chooseAddress = (TextView) rootView
				.findViewById(Rconfig.getInstance()
						.id("shipping_chooseAddress"));
		tv_shipping_chooseAddress.setText(Config.getInstance().getText(
				"Shipping Address"));

		bt_updateAddress = (Button) rootView.findViewById(Rconfig.getInstance()
				.id("bt_update"));
		
		//setdata
		if(getArguments() != null){
			shippingAddress = (MyAddress) getArguments().getSerializable(Constants.KeyData.SHIPPING_ADDRESS);
			billingAddress = (MyAddress) getArguments().getSerializable(Constants.KeyData.BILLING_ADDRESS);
		}
		
		bt_updateAddress.setTextColor(Color.WHITE);
		bt_updateAddress.setText(Config.getInstance()
				.getText("Confirm Address"));
		GradientDrawable gdDefault = new GradientDrawable();
		gdDefault.setColor(Config.getInstance().getColorMain());
		gdDefault.setCornerRadius(15);
		bt_updateAddress.setBackgroundDrawable(gdDefault);
		updateAddress(bt_updateAddress);

		setBillingDataView(billingAddress);
		setShippingDataView(shippingAddress);

		return rootView;
	}

	public void setBillingDataView(MyAddress address) {
		TextView name = (TextView) rootView.findViewById(Rconfig.getInstance()
				.id("billing_name"));
		TextView street = (TextView) rootView.findViewById(Rconfig
				.getInstance().id("billing_street"));
		TextView city = (TextView) rootView.findViewById(Rconfig.getInstance()
				.id("billing_city"));
		TextView country = (TextView) rootView.findViewById(Rconfig
				.getInstance().id("billing_country"));
		TextView phone = (TextView) rootView.findViewById(Rconfig.getInstance()
				.id("billing_phone"));
		TextView email = (TextView) rootView.findViewById(Rconfig.getInstance()
				.id("billing_email"));

		name.setText(address.getName());
		street.setText(address.getStreet());

		if (address.getStateName().equals("null")
				|| address.getStateName() == null) {
			city.setText(address.getCity() + ", " + address.getZipCode());
		} else {
			city.setText(address.getCity() + ", " + address.getStateName()
					+ ", " + address.getZipCode());
		}
		country.setText(address.getCountryName());
		phone.setText(address.getPhone());
		email.setText(address.getEmail());
		onTouchBilling();
	}

	public void setShippingDataView(MyAddress address) {
		TextView name = (TextView) rootView.findViewById(Rconfig.getInstance()
				.id("shipping_name"));
		TextView street = (TextView) rootView.findViewById(Rconfig
				.getInstance().id("shipping_street"));
		TextView city = (TextView) rootView.findViewById(Rconfig.getInstance()
				.id("shipping_city"));
		TextView country = (TextView) rootView.findViewById(Rconfig
				.getInstance().id("shipping_country"));
		TextView phone = (TextView) rootView.findViewById(Rconfig.getInstance()
				.id("shipping_phone"));
		TextView email = (TextView) rootView.findViewById(Rconfig.getInstance()
				.id("shipping_email"));

		name.setText(address.getName());
		street.setText(address.getStreet());
		if (address.getStateName().equals("null")
				|| address.getStateName() == null) {
			city.setText(address.getCity() + ", " + address.getZipCode());
		} else {
			city.setText(address.getCity() + ", " + address.getStateName()
					+ ", " + address.getZipCode());
		}
		country.setText(address.getCountryName());
		email.setText(address.getEmail());
		phone.setText(address.getPhone());
		onTouchShipping();
	}

	public void updateAddress(Button button) {
		button.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN: {
					GradientDrawable gdDefault = new GradientDrawable();
					gdDefault.setColor(Color.GRAY);
					bt_updateAddress.setBackgroundDrawable(gdDefault);
					break;
				}
				case MotionEvent.ACTION_UP: {
					requestUpdateAddress();
				}

				case MotionEvent.ACTION_CANCEL: {
					GradientDrawable gdDefault = new GradientDrawable();
					gdDefault.setColor(Config.getInstance().getColorMain());
					bt_updateAddress.setBackgroundDrawable(gdDefault);
					break;
				}
				}
				return true;
			}
		});
	}

	private void requestUpdateAddress() {
		final RequestUpdateAddressModel mModel = new RequestUpdateAddressModel();
		final SimiBlock mDelegate = new SimiBlock(rootView, getActivity());
		mDelegate.showLoading();
		mModel.setDelegate(new ModelDelegate() {

			@Override
			public void callBack(String message, boolean isSuccess) {
				mDelegate.dismissLoading();
				if (isSuccess) {
//					ShippingFragment fShipping = new ShippingFragment();
					ArrayList<SimiEntity> entity = mModel.getCollection()
							.getCollection();
					ArrayList<ShippingMethod> shippingMethods = new ArrayList<ShippingMethod>();
					for (SimiEntity simiEntity : entity) {
						ShippingMethod shippingMethod = (ShippingMethod) simiEntity;
						shippingMethods.add(shippingMethod);
					}
					ShippingFragment fShipping =  ShippingFragment.newInstance(shippingMethods);
//					fShipping.setShippingMethodList(shippingMethods);
					SimiManager.getIntance().addPopupFragment(fShipping);
				} else {
					SimiManager.getIntance().showToast(message);
				}
			}
		});
		if (null != billingAddress) {
			try {
				JSONObject json_billingAddres = new JSONObject(
						Utils.endCodeJson(billingAddress.toParamsRequest()));

				mModel.addParam(Constants.BILLING_ADDRESS, json_billingAddres);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		if (null != shippingAddress) {
			try {
				JSONObject json_shippingAddress = new JSONObject(
						Utils.endCodeJson(shippingAddress.toParamsRequest()));

				mModel.addParam(Constants.SHIPPING_ADDRESS,
						json_shippingAddress);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		mModel.request();
	}

	public void onTouchShipping() {
		View rl = (View) rootView.findViewById(Rconfig.getInstance().id(
				"rl_shipping"));
		rl.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN: {
					v.setBackgroundColor(Color.parseColor("#CACACA"));
					break;
				}
				case MotionEvent.ACTION_UP: {
//					EditAddressExpressFragment fragmentEditAddressExpress = new EditAddressExpressFragment();
//					fragmentEditAddressExpress
//							.setShippingAddressbook(shippingAddress);
//					fragmentEditAddressExpress
//							.setAddressbookTemp(billingAddress);
					EditAddressExpressFragment editAddressExpressFragment =  EditAddressExpressFragment.newInstance(billingAddress, shippingAddress, SHIPPING);
					SimiManager.getIntance().addPopupFragment(
							editAddressExpressFragment);
				}

				case MotionEvent.ACTION_CANCEL: {
					v.setBackgroundResource(Rconfig.getInstance().drawable(
							"line_border"));
					break;
				}
				default:
					break;
				}
				return true;
			}
		});
	}

	public void onTouchBilling() {
		View rl = (View) rootView.findViewById(Rconfig.getInstance().id(
				"rl_billing"));
		rl.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN: {
					v.setBackgroundColor(Color.parseColor("#CACACA"));
					break;
				}
				case MotionEvent.ACTION_UP: {
//					EditAddressExpressFragment fragmentEditAddressExpress = new EditAddressExpressFragment();
//					fragmentEditAddressExpress
//							.setBillingAddressbook(billingAddress);
//					fragmentEditAddressExpress
//							.setAddressbookTemp(shippingAddress);
					EditAddressExpressFragment editAddressExpressFragment =  EditAddressExpressFragment.newInstance(shippingAddress, billingAddress, BILLING);
					SimiManager.getIntance().addPopupFragment(
							editAddressExpressFragment);
				}

				case MotionEvent.ACTION_CANCEL: {
					v.setBackgroundResource(Rconfig.getInstance().drawable(
							"line_border"));
					break;
				}
				default:
					break;
				}
				return true;
			}
		});
	}
}
