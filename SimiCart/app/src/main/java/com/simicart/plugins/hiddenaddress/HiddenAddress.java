package com.simicart.plugins.hiddenaddress;

import com.simicart.core.base.delegate.ModelDelegate;
import com.simicart.core.config.DataLocal;
import com.simicart.core.customer.entity.ConfigCustomerAddress;
import com.simicart.core.event.block.CacheBlock;

public class HiddenAddress {

	public HiddenAddress(String method, CacheBlock cacheBlock) {
		if (method.equals("requestHiddenAddress")) {
			requestHideAddress();
		}
	}

	private void requestHideAddress() {
		final HiddenAddressModel model = new HiddenAddressModel();
		model.setDelegate(new ModelDelegate() {

			@Override
			public void callBack(String message, boolean isSuccess) {
				if (isSuccess) {
					ConfigCustomerAddress address = (ConfigCustomerAddress) model
							.getCollection().getCollection().get(0);
					if (null != address) {
						DataLocal.ConfigCustomerAddress = address;
					}
				}

			}
		});
		model.request();
	}
}