package com.simicart.core.setting.delegate;

import com.simicart.core.base.delegate.SimiDelegate;

public interface SettingAppDelegate extends SimiDelegate {

	public void setCurrency();

	public void successData();

	public void setLanguage();

	public void updateSettingNotification();

	public String getCurrency();

	public String getLanguage();

}
