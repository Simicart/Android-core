package com.simicart.core.customer.controller;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.ModelDelegate;
import com.simicart.core.base.delegate.SimiDelegate;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.checkout.controller.ConfigCheckout;
import com.simicart.core.config.Constants;
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
	public static Bundle bundleAddress;

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
		Gson gson = new GsonBuilder().create();
		JSONObject jsonObject = entity.getJSONObject();
		MyAddress addressbook = gson.fromJson(jsonObject.toString(), MyAddress.class);
//		MyAddress addressbook = new MyAddress();
		addressbook.setJSONObject(jsonObject);
		Log.d("quang12", "==selectItem==addressbook=="+addressbook.toString());
//		Log.d("quang12", "==selectItem==addressbook=="+addressbook.getStreet());
		Log.d("quang12", "==selectItem==getJSONObject=="+jsonObject.toString());
		AddressBookDetailFragment fragment = AddressBookDetailFragment
				.newInstance();
		bundleAddress= new Bundle();
		bundleAddress.putSerializable(Constants.KeyData.BOOK_ADDRESS, addressbook);
		fragment.setArguments(bundleAddress);
		if (DataLocal.isTablet) {
			SimiManager.getIntance().replacePopupFragment(fragment);
		} else {
			SimiManager.getIntance().replaceFragment(fragment);
		}
	}

	@Override
	public void onResume() {
		if(ConfigCheckout.getInstance().getStatusAddressBook() == true){
			if(mModel != null)
			mDelegate.updateView(mModel.getCollection());
		}else{
			mDelegate.updateView(ConfigCheckout.getInstance().getCollectionAddressBook());
		}
	}

}
