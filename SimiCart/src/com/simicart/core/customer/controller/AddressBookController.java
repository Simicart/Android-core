package com.simicart.core.customer.controller;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.ModelDelegate;
import com.simicart.core.base.delegate.SimiDelegate;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.checkout.controller.ConfigCheckout;
import com.simicart.core.config.DataLocal;
import com.simicart.core.customer.entity.MyAddress;
import com.simicart.core.customer.fragment.AddressBookDetailFragment;
import com.simicart.core.customer.fragment.NewAddressBookFragment;
import com.simicart.core.customer.model.AddressBookModel;

@SuppressLint("ClickableViewAccessibility")
public class AddressBookController extends SimiController {

	protected SimiDelegate mDelegate;
	protected OnTouchListener mListener;
	protected OnItemClickListener mClicker;

	public OnTouchListener getListener() {
		return mListener;
	}

	public OnItemClickListener getItemClicker() {
		return mClicker;
	}

	public void setDelegate(SimiDelegate delegate) {
		mDelegate = delegate;
	}

	@Override
	public void onStart() {

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
		mModel.addParam("is_get_order_address", "NO");
		mModel.request();

		mListener = new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				NewAddressBookFragment fragment = NewAddressBookFragment
						.newInstance();
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

	protected void addNewAddress() {

	}

	protected void selectItem(int position) {
		SimiEntity entity = mModel.getCollection().getCollection()
				.get(position);

		MyAddress addressbook = new MyAddress();
		addressbook.setJSONObject(entity.getJSONObject());
		Log.e("AddressBookController : ",
				"Address ID : " + addressbook.getAddressId());
		AddressBookDetailFragment fragment = AddressBookDetailFragment
				.newInstance();
		fragment.setAddressbook(addressbook);
		if (DataLocal.isTablet) {
			SimiManager.getIntance().replacePopupFragment(fragment);
		} else {
			SimiManager.getIntance().replaceFragment(fragment);
		}
	}

	@Override
	public void onResume() {
		if(ConfigCheckout.getInstance().getStatusAddressBook() == true){
			mDelegate.updateView(mModel.getCollection());
		}else{
			mDelegate.updateView(ConfigCheckout.getInstance().getCollectionAddressBook());
		}
	}

}
