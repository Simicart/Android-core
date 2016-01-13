package com.simicart.core.base.network.request;

import org.json.JSONObject;

public interface SimiNetworkCache {

	public JSONObject get(String key);

	public void put(String key, JSONObject response);

	public void clear();

	public void remove(String key);

}
