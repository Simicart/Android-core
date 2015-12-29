package com.simicart.plugins.klarna.delegate;

import org.json.JSONArray;

import com.simicart.core.base.delegate.SimiDelegate;

public interface KlarnaDelegate extends SimiDelegate{
	
	public void onLoadWebView(JSONArray json);

}
