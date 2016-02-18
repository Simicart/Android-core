package com.simicart.core.customer.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.model.entity.BusEntity;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.block.AddressBookDetailBlock;
import com.simicart.core.customer.controller.AddressBookDetailController;
import com.simicart.core.customer.entity.MyAddress;

import de.greenrobot.event.EventBus;

public class AddressBookDetailFragment extends SimiFragment {
	protected MyAddress addressbook;
	protected AddressBookDetailBlock mBlock;
	protected AddressBookDetailController mController;
	View view;
	public MyAddress getAddressbook() {
		return addressbook;
	}

	public static AddressBookDetailFragment newInstance(MyAddress addressbook) {
		Log.d("quang12", "==addressbook==newInstance=="+addressbook.toString());
		AddressBookDetailFragment fragment = new AddressBookDetailFragment();
		Bundle bundle= new Bundle();
		bundle.putSerializable(Constants.KeyData.BOOK_ADDRESS, addressbook);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		 view = inflater.inflate(
				Rconfig.getInstance().layout("core_new_address_layout"),
				container, false);
		if (DataLocal.isLanguageRTL) {
			view = inflater
					.inflate(
							Rconfig.getInstance().layout(
									"rtl_core_new_address_layout"), container,
							false);
		}
		
		return view;
	}
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Context context = getActivity();
		if(getArguments() != null){
		addressbook = (MyAddress) getArguments().getSerializable(Constants.KeyData.BOOK_ADDRESS);
		}
		BusEntity<MyAddress> busEntity = new BusEntity<>();
		busEntity.setKey(Constants.KeyBus.BOOK_ADDRESS);
		busEntity.setValue(addressbook);
		EventBus.getDefault().postSticky(busEntity);
		Log.d("quang12", "==addressbook==getArguments=="+addressbook.toString());
		mBlock = new AddressBookDetailBlock(view, context);
		mBlock.setAddressBookDetail(addressbook);
		mBlock.initView();
		if (null == mController) {
			mController = new AddressBookDetailController();
			mController.setDelegate(mBlock);
			mController.onStart();
		} else {
			mController.setDelegate(mBlock);
			mController.onResume();
		}
		mBlock.setSaveClicker(mController.getClickSave());
		mBlock.setChooseCountry(mController.getChooseCountry());
		mBlock.setChooseStates(mController.getChooseStates());
	}
	
}
