package com.simicart.plugins.paypalexpress.fragment;

import java.util.ArrayList;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.delegate.ModelDelegate;
import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.checkout.adapter.ShippingMethodAdapter;
import com.simicart.core.checkout.entity.ShippingMethod;
import com.simicart.core.config.Config;
import com.simicart.core.config.Rconfig;
import com.simicart.plugins.paypalexpress.model.RequestPlaceOrderModel;

public class FragmentShipping extends SimiFragment {

	View rootView;
	ArrayList<ShippingMethod> shippingList = new ArrayList<ShippingMethod>();
	FragmentShipping fshipping;
	LinearLayout child;
	int pos = -1;
	View mImageView;
	String code = "";

	public void setShippingMethodList(ArrayList<ShippingMethod> shippingMethods) {
		this.shippingList = shippingMethods;
		this.fshipping = this;
	}

	@Override
	public View onCreateView(final LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(
				Rconfig.getInstance().layout("core_shipping_method_layout"),
				container, false);
		child = (LinearLayout) rootView.findViewById(Rconfig.getInstance().id(
				"ll_shipping_method"));
		TextView label = (TextView) rootView.findViewById(Rconfig.getInstance()
				.id("label_shipping"));
		label.setText(Config.getInstance().getText(
				"Select a shipping method to complete an order"));

		if (shippingList.isEmpty()) {
			label.setText("");
		}

		final ListView listview = (ListView) rootView.findViewById(Rconfig
				.getInstance().id("list_shipping"));
		Button button_check = new Button(rootView.getContext());
		button_check.setText(Config.getInstance().getText("Place Order"));
		button_check.setTextColor(Color.WHITE);
		button_check.setBackgroundColor(Config.getInstance().getColorMain());
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		params.setMargins(0, 20, 0, 0);
		button_check.setLayoutParams(params);
		child.addView(button_check);
		ShippingMethodAdapter shippingAdapter = new ShippingMethodAdapter(
				rootView.getContext(), shippingList);
		listview.setAdapter(shippingAdapter);

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (pos == position)
					return;

				pos = position;
				CheckBox check = (CheckBox) view.findViewById(Rconfig
						.getInstance().id("shipping_checkbox"));
				check.setBackgroundResource(Rconfig.getInstance().drawable(
						"ic_action_accept"));
				for (int i = 0; i < listview.getChildCount(); i++) {
					if (i != position) {
						RelativeLayout rl = (RelativeLayout) listview
								.getChildAt(i);
						CheckBox checkx = (CheckBox) rl.findViewById(Rconfig
								.getInstance().id("shipping_checkbox"));
						checkx.setBackgroundColor(0);
					}
				}

				// save order
				code = shippingList.get(position).getS_method_code();
			}
		});

		button_check.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN: {
					v.setBackgroundColor(Color.parseColor("#EBEBEB"));
					break;
				}
				case MotionEvent.ACTION_UP: {
					if (code.equals("")) {
						SimiManager
								.getIntance()
								.showToast(
										Config.getInstance()
												.getText(
														"Please choose a shipping method before placing order"));
					} else {

						RequestPlaceOrderModel model = new RequestPlaceOrderModel();
						final SimiBlock delegate = new SimiBlock(rootView,
								getActivity());
						delegate.showLoading();
						model.setDelegate(new ModelDelegate() {

							@Override
							public void callBack(String message,
									boolean isSuccess) {
								delegate.dismissLoading();
								if (isSuccess) {
									SimiManager.getIntance().onUpdateCartQty(
											null);
								}
								SimiManager.getIntance().showToast(message);
								SimiManager.getIntance().backToHomeFragment();
							}
						});
						model.addParam("shipping_method", code);
						model.request();
					}
				}

				case MotionEvent.ACTION_CANCEL: {
					v.setBackgroundColor(Config.getInstance().getColorMain());
					break;
				}
				}
				return true;

			}
		});
		return rootView;
	}

}
