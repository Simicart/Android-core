package com.simicart.core.notification.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.config.Config;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;

public class SettingAppFragment extends SimiFragment {

	public static SettingAppFragment newInstance() {
		SettingAppFragment fragment = new SettingAppFragment();
		return fragment;
	}

	@Override
	public View onCreateView(final LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(
				Rconfig.getInstance().layout("core_setting_layout"), container,
				false);

		final TextView tv_notification = (TextView) rootView
				.findViewById(Rconfig.getInstance().id("tv_notification"));
		tv_notification.setText(Config.getInstance().getText(
				"Show notifications"));

//		final ToggleButton tg_button = (ToggleButton) rootView
//				.findViewById(Rconfig.getInstance().id("tg_button"));
		Switch tg_button = (Switch) rootView.findViewById(Rconfig.getInstance().id("tg_button"));
		
		if (DataLocal.enableNotification()) {
			tg_button.setChecked(true);
		} else {
			tg_button.setChecked(false);
		}
		tg_button.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				DataLocal.saveNotificationSet(isChecked);
			}
		});
		

		final RelativeLayout rl_location = (RelativeLayout) rootView
				.findViewById(Rconfig.getInstance().id("rl_location"));
		rl_location.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent viewIntent = new Intent(
						Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				startActivity(viewIntent);
			}
		});

		final TextView tv_location = (TextView) rootView.findViewById(Rconfig
				.getInstance().id("tv_location"));
		tv_location.setText(Config.getInstance().getText("Location Setting"));

		return rootView;
	}
}
