package com.simicart.core.base.network.mock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONObject;

import android.content.res.AssetManager;
import android.util.Log;

import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.config.Config;

public class MockCoreAPIService {

	protected static MockCoreAPIService instance;
	String fail_result = "{\"data\":[],\"message\":[\"NO MOCK FILE \"],\"status\":\"FAIL\"}";

	public static MockCoreAPIService getInstance() {
		if (null == instance) {
			instance = new MockCoreAPIService();
		}
		return instance;
	}

	public String getDataFromServer(JSONObject params, String urlExtension) {
		if (null == params) {
			return null;
		}
		String url = Config.getInstance().getBaseUrl() + urlExtension;
		Log.e("MockCoreAPIService", "Url : " + url);
		Log.e("MockCoreAPIService", "params : " + params.toString());

		String[] lists = urlExtension.split("/");
		if (lists.length > 0) {
			String name_file = lists[lists.length - 1];
			Log.e("MockCoreAPIService ", "Name_File " + name_file);

			AssetManager assetManager = SimiManager.getIntance()
					.getCurrentActivity().getAssets();
			try {
				String path = "mock/" + name_file + ".txt";

				Log.e("MockAPIService ", "PATH " + path);
				InputStream is = assetManager.open(path);
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "UTF-8"), 8);

				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				is.close();

				String result = sb.toString();
				Log.e("MockCoreAPIService ", "Result " + result);

				return result;
			} catch (IOException e) {
				Log.e("MockCoreAPIService ", "Exception " + e.getMessage());

				return fail_result;
			}

		}
		return fail_result;

	}

}
