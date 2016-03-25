package com.simicart.core.splashscreen.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.simicart.core.base.model.SimiModelCloud;
import com.simicart.core.base.networkcloud.request.multi.SimiRequest;
import com.simicart.core.common.Utils;

/**
 * Created by MSI on 18/01/2016.
 */
public class GetIDPluginsModel extends SimiModelCloud {

    private StringBuilder mIds = new StringBuilder();
    private String site_plugins = "site_plugins";
    private String config = "config";
    private String plugin_id = "plugin_id";
    private String enable = "enable";

	public String getIDs() {
        return mIds.toString();
    }

    @Override
    public void addDataBody(String tag, JSONArray value) {
        super.addDataBody(tag, value);
    }

    @Override
    protected void setTypeMethod() {
        mTypeMethod = SimiRequest.Method.GET;
    }

    @Override
    protected void setUrlAction() {
        addDataExtendURL("site-plugins/");
    }

    @Override
    protected void paserData() {
        super.paserData();
        if (mJSONResult.has(site_plugins)) {
            try {
                JSONArray array = mJSONResult.getJSONArray(site_plugins);
                if (null != array && array.length() > 0) {
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonPlugins = array.getJSONObject(i);
                        if (jsonPlugins.has(config)) {
                            JSONObject jsonConfig = jsonPlugins.getJSONObject(config);
                            if (jsonConfig.has(enable)) {
                                String isEnable = jsonConfig.getString(enable);
                                if (Utils.validateString(isEnable) && isEnable.equals("1")) {
                                    String pluginID = jsonPlugins.getString(plugin_id);
                                    mIds.append(pluginID);
                                    mIds.append(",");
                                }
                            }

                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (mIds.length() > 0) {
            int lastIndex = mIds.length() - 1;
            mIds.deleteCharAt(lastIndex);
            Log.e("GetIDPluginsModel ", "LIST ID " + mIds.toString());
        }
    }
}
