package com.simicart.core.catalog.product.controller;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.SimiDelegate;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.catalog.product.entity.Product;
import com.simicart.core.config.Config;

public class ProductMorePluginController extends SimiController {

	protected OnClickListener mClickerShare;
	protected Product mProduct;
	protected SimiDelegate mDelegate;
	
	public void setDelegate(SimiDelegate mDelegate) {
		this.mDelegate = mDelegate;
	}

	public void setProduct(Product product) {
		mProduct = product;
	}

	public OnClickListener getClickerShare() {
		return mClickerShare;
	}

	@Override
	public void onStart() {
		mClickerShare = new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent sharingIntent = new Intent(
						android.content.Intent.ACTION_SEND);
				sharingIntent.setType("text/plain");
				sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,
						mProduct.getData("product_url"));
				SimiManager
						.getIntance()
						.getCurrentActivity()
						.startActivity(
								Intent.createChooser(sharingIntent, Config
										.getInstance().getText("Share via")));
			}
		};
	}

	@Override
	public void onResume() {
	}

}
