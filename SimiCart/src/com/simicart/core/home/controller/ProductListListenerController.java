package com.simicart.core.home.controller;

import java.util.ArrayList;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.catalog.product.entity.Product;
import com.simicart.core.catalog.product.fragment.ProductDetailParentFragment;

public class ProductListListenerController {
	protected ArrayList<Product> mProductList;

	public void setProductList(ArrayList<Product> productList) {
		this.mProductList = productList;
	}

	public OnItemClickListener createTouchProductList() {
		return new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				ArrayList<String> listID = new ArrayList<String>();
				for (int i = 0; i < mProductList.size(); i++) {
					listID.add(mProductList.get(i).getId());
				}
				String id = mProductList.get(position).getData("product_id");
				if (id != null) {
					ProductDetailParentFragment fragment = ProductDetailParentFragment
							.newInstance();
					Log.e("LONGTB", "id" + id);
					fragment.setProductID(id);
					fragment.setListIDProduct(listID);
					SimiManager.getIntance().addFragment(fragment);
					SimiManager.getIntance().hideKeyboard();
				}
			}
		};
	}
}
