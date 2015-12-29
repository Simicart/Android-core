package com.simicart.plugins.rewardpoint.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.simicart.core.base.delegate.ModelDelegate;
import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.config.Config;
import com.simicart.core.config.Rconfig;
import com.simicart.plugins.rewardpoint.model.ModelRewardSetting;

public class FragmentRewardSetting extends SimiFragment {

	private boolean is_notification;
	private boolean expire_notification;

	private Switch switch_point_update;
	private Switch switch_point_transaction;
	private Button btn_save;
	protected ProgressDialog pd_loading;
	private Context mContext;
	
	private TextView txt_email;
	private TextView txt_pointupdate;
	private TextView txt_subscribe_updatepoint;
	private TextView txt_pointtransaction;
	private TextView txt_notification;

	public FragmentRewardSetting() {
	}

	public FragmentRewardSetting(int isnotification, int expire_notification) {
		if(isnotification == 1){
			this.is_notification = true;
		}else{
			this.is_notification = false;
		}
		if(expire_notification == 1){
			this.expire_notification = true;
		}else{
			this.expire_notification = false;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = getActivity();
		View view = inflater.inflate(
				Rconfig.getInstance().layout(
						"plugins_rewardpoint_settingfragment"), container,
				false);
		switch_point_update = (Switch) view.findViewById(Rconfig.getInstance()
				.id("switch_pointupdate"));
		switch_point_transaction = (Switch) view.findViewById(Rconfig
				.getInstance().id("switch_pointtransaction"));
		btn_save = (Button) view.findViewById(Rconfig.getInstance().id(
				"btn_save"));
		switch_point_update.setChecked(is_notification);
		switch_point_transaction.setChecked(expire_notification);
		
		txt_email = (TextView) view.findViewById(Rconfig.getInstance().id("txt_rewardsetting_email"));
		txt_pointupdate = (TextView) view.findViewById(Rconfig.getInstance().id("txt_pointupdate"));
		txt_subscribe_updatepoint = (TextView) view.findViewById(Rconfig.getInstance().id("txt_rewardsetting_subscribe_updatepoint"));
		txt_pointtransaction = (TextView) view.findViewById(Rconfig.getInstance().id("txt_pointtransaction"));
		txt_notification = (TextView) view.findViewById(Rconfig.getInstance().id("txt_rewardsetting_subscribe_notification"));
		txt_email.setText(Config.getInstance().getText("EMAIL SUBCRIPTIONS"));
		txt_pointupdate.setText(Config.getInstance().getText("Point Balance Update"));
		txt_subscribe_updatepoint.setText(Config.getInstance().getText("Subscribe to receive updates on your point balance"));
		txt_pointtransaction.setText(Config.getInstance().getText("Expired Point Transaction"));
		txt_notification.setText(Config.getInstance().getText("Subscribe to receive notification of expiring points in advance"));
		
		GradientDrawable gdDefault = new GradientDrawable();
		gdDefault.setColor(Config.getInstance().getColorMain());
		gdDefault.setCornerRadius(15);
		btn_save.setBackgroundDrawable(gdDefault);
		btn_save.setText(Config.getInstance().getText("Save"));
		btn_save.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				GradientDrawable gdDefault = new GradientDrawable();
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					gdDefault.setColor(Color.parseColor("#ABABAB"));
					gdDefault.setCornerRadius(15);
					btn_save.setBackgroundDrawable(gdDefault);
					break;
				case MotionEvent.ACTION_UP:
					gdDefault.setColor(Config.getInstance().getColorMain());
					gdDefault.setCornerRadius(15);
					btn_save.setBackgroundDrawable(gdDefault);
					saveStatus();
					break;
				}
				return true;
			}
		});
		return view;
	}

	private void saveStatus() {
		boolean point_check_update = switch_point_update.isChecked();
		boolean point_check_transaction = switch_point_transaction.isChecked();
		if(point_check_update == this.is_notification && point_check_transaction == this.expire_notification){
			
		}else {
			pd_loading = ProgressDialog.show(mContext, null, null, true, false);
			pd_loading.setContentView(Rconfig.getInstance().layout(
					"core_base_loading"));
			pd_loading.setCanceledOnTouchOutside(false);
			pd_loading.setCancelable(false);
			pd_loading.show();
			int point_update = 0;
			int point_transaction = 0;
			if (point_check_update == true) {
				point_update = 1;
			} else {
				point_update = 0;
			}
			if (point_check_transaction == true) {
				point_transaction = 1;
			} else {
				point_transaction = 0;
			}
			// send request to server
			ModelRewardSetting model = new ModelRewardSetting();
			ModelDelegate delegate = new ModelDelegate() {

				@Override
				public void callBack(String message, boolean isSuccess) {
					pd_loading.dismiss();
					if (isSuccess) {
						FragmentRewardPoint rewardPoint = new FragmentRewardPoint();
						SimiManager.getIntance().replacePopupFragment(rewardPoint);
					}
				}
			};
			model.addParam("expire_notification", String.valueOf(point_transaction));
			model.addParam("is_notification", String.valueOf(point_update));
			model.setDelegate(delegate);
			model.request();
		}
		
	}

}
