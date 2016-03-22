package com.simicart.core.checkout.block;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.checkout.adapter.ShippingMethodAdapter;
import com.simicart.core.checkout.delegate.ShippingMethodDelegate;
import com.simicart.core.checkout.entity.ShippingMethod;
import com.simicart.core.config.Config;
import com.simicart.core.config.Rconfig;

public class ShippingMethodBlock extends SimiBlock implements
		ShippingMethodDelegate {
	protected ListView lv_shippingMethod;
	protected ShippingMethodAdapter mAdapter;

	public void setItemClicker(OnItemClickListener listener) {
		lv_shippingMethod.setOnItemClickListener(listener);
	}

	public ShippingMethodBlock(View view, Context context) {
		super(view, context);

	}

	@Override
	public void initView() {
		// title
		TextView tv_name = (TextView) mView.findViewById(Rconfig.getInstance()
				.id("label_shipping"));
		tv_name.setText(Config.getInstance()
				.getText("Select a shipping method"));

		// initial list view
		lv_shippingMethod = (ListView) mView.findViewById(Rconfig.getInstance()
				.id("list_shipping"));
	}

	@Override
	public void updateListShippingMethod(ArrayList<ShippingMethod> listShipping) {

		if (null == mAdapter) {
			mAdapter = new ShippingMethodAdapter(mContext, listShipping);
			lv_shippingMethod.setAdapter(mAdapter);
		} else {
			mAdapter.setListShipping(listShipping);
			mAdapter.notifyDataSetChanged();
		}

	}

}
