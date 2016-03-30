package com.simicart.core.base.networkcloud.request.multi;

import org.json.JSONObject;

/**
 * Created by MSI on 25/01/2016.
 */
public interface SimiNetworkCache {
    public JSONObject get(String key);

    public void put(String key, JSONObject response);

    public void clear();

    public void remove(String key);

}
