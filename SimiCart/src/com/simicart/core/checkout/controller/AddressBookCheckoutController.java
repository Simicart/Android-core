package com.simicart.core.checkout.controller;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.simicart.core.base.delegate.ModelDelegate;
import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.checkout.fragment.ReviewOrderFragment;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.core.customer.controller.AddressBookController;
import com.simicart.core.customer.entity.MyAddress;
import com.simicart.core.customer.fragment.NewAddressBookFragment;
import com.simicart.core.customer.model.AddressBookModel;

@SuppressLint("ClickableViewAccessibility")
public class AddressBookCheckoutController extends AddressBookController {

	protected int addressFor;
	protected MyAddress mBillingAddress;
	protected MyAddress mShippingAddress;
	protected int mAfterController;
	public Bundle bundle;

	public void setAddressFor(int addressFor) {
		this.addressFor = addressFor;
	}

	public void setBillingAddress(MyAddress mBillingAddress) {
		this.mBillingAddress = mBillingAddress;
	}

	public void setShippingAddress(MyAddress mShippingAddress) {
		this.mShippingAddress = mShippingAddress;
	}

	public void setAfterController(int afterController) {
		mAfterController = afterController;
	}

	@Override
	public void onStart() {
		if (ConfigCheckout.getInstance().getAddressBookFirstRequest() == true) {
			request();
			ConfigCheckout.getInstance().setAddressBookFirstRequest(false);
		} else {
			if (ConfigCheckout.getInstance().getStatusAddressBook() == true) {
				request();
				ConfigCheckout.getInstance().setStatusAddressBook(false);
			} else {
				resumeJson();
			}
		}

		mListener = new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				NewAddressBookFragment fragment = NewAddressBookFragment
						.newInstance();
				bundle = new Bundle();
				SimiFragment.setData(Constants.KeyData.AFTER_CONTROL,
						Constants.NEW_ADDRESS_CHECKOUT,
						Constants.KeyData.TYPE_INT, bundle);
				SimiFragment.setData(Constants.KeyData.ADDRESS_FOR, addressFor,
						Constants.KeyData.TYPE_INT, bundle);
				bundle.putSerializable(Constants.KeyData.BILLING_ADDRESS,
						mBillingAddress);
				bundle.putSerializable(Constants.KeyData.SHIPPING_ADDRESS,
						mShippingAddress);
				fragment.setArguments(bundle);
				if (DataLocal.isTablet) {
					SimiManager.getIntance().replacePopupFragment(fragment);
				} else {
					SimiManager.getIntance().replaceFragment(fragment);
				}
				return false;
			}
		};

		mClicker = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				selectItem(position);
			}

		};

	}

	private void request() {
		if (DataLocal.isSignInComplete()) {
			mDelegate.showLoading();
			mModel = new AddressBookModel();
			mModel.setDelegate(new ModelDelegate() {

				@Override
				public void callBack(String message, boolean isSuccess) {
					mDelegate.dismissLoading();
					if (isSuccess) {
						mDelegate.updateView(mModel.getCollection());
					}
				}
			});
			mModel.addParam("is_get_order_address", "YES");
			mModel.request();
		}
	}

	private void resumeJson() {
		mDelegate.updateView(ConfigCheckout.getInstance()
				.getCollectionAddressBook());
	}

	@Override
	protected void selectItem(int position) {
		MyAddress address;
		try {
			if (ConfigCheckout.getInstance().getStatusAddressBook() == true) {
				address = (MyAddress) mModel.getCollection().getCollection()
						.get(position);
			} else {
				address = (MyAddress) ConfigCheckout.getInstance()
						.getCollectionAddressBook().getCollection()
						.get(position);
			}
			MyAddress shippingAdd = null, billingAdd = null;
			switch (addressFor) {
			case Constants.KeyAddress.ALL_ADDRESS:
				billingAdd = address;
				shippingAdd = address;
				break;
			case Constants.KeyAddress.BILLING_ADDRESS:
				billingAdd = address;
				shippingAdd = mShippingAddress;
				break;
			case Constants.KeyAddress.SHIPPING_ADDRESS:
				billingAdd = mBillingAddress;
				shippingAdd = address;
				break;
			default:
				break;
			}
			Log.d("quang123", "=AddressBookCheckoutController==selectItem");
			ReviewOrderFragment fragment = ReviewOrderFragment.newInstance(0,
					shippingAdd, billingAdd);

			SimiManager.getIntance().removeDialog();
			SimiManager.getIntance().replaceFragment(fragment);
		} catch (Exception e) {
			Log.e("Error SelectItem AddressBookCheckoutController",
					e.getMessage());
		}
	}

}
