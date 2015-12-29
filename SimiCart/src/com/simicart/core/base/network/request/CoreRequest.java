package com.simicart.core.base.network.request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.simicart.core.base.delegate.NetWorkDelegate;
import com.simicart.core.base.helper.SimiHelper;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.network.mock.MockCoreAPIService;
import com.simicart.core.base.network.response.CoreResponse;
import com.simicart.core.base.network.volley.AppController;
import com.simicart.core.base.network.volley.Request.Method;
import com.simicart.core.base.network.volley.Response;
import com.simicart.core.base.network.volley.VolleyError;
import com.simicart.core.base.network.volley.customrequest.CustomRequest;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Config;

@SuppressLint("NewApi")
public class CoreRequest extends AsyncTask<Void, Void, String> {
	protected NetWorkDelegate mDelegate;
	protected Context mContext = null;
	protected String mPhoneType;
	protected boolean isShowNotify = true;
	protected List<NameValuePair> mNameValue = new ArrayList<NameValuePair>();
	protected JSONObject mJsonParams;
	protected HashMap<String, Object> mHashMap;
	protected String url_action;

	protected boolean isDebug = false;

	public void setDebugMode(boolean debug) {
		isDebug = debug;
	}

	public void setPhoneType(String type) {
		mPhoneType = type;
	}

	public void setContext(Context context) {
		mContext = context;
	}

	public void setShowNotify(boolean isShowNotify) {
		this.isShowNotify = isShowNotify;
	}

	public void setUrlAction(String url) {
		this.url_action = url;
	}

	public CoreRequest(NetWorkDelegate delegate) {
		this.mDelegate = delegate;
		this.mContext = SimiManager.getIntance().getCurrentContext();
		mJsonParams = new JSONObject();
		mHashMap = new HashMap<String, Object>();
	}

	public void request() {
		prepareRequest();

		// sdung single request
		// if (Build.VERSION.SDK_INT >= 11) {
		// this.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, (Void[]) null);
		// } else {
		// this.execute();
		// }

		// sdung volley
		requestCustom(Config.getInstance().getBaseUrl() + url_action);
	}

	void requestCustom(final String url) {
		final CustomRequest request = new CustomRequest(Method.POST, url,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						excuteResult(response);
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						handleErrorVolley(error, url);
					}
				});
		request.setParams(mJsonParams);
		request.setShouldCache(false);
		AppController.getInstance().addToRequestQueue(request, "tagjson");
	}

	private void handleErrorVolley(VolleyError volleyError, String url) {
		Log.e("CoreRequest-HandleVolleyError:", volleyError + "");
		Log.e("CoreRequest-HandleVolleyError URL:", url);
		mDelegate.callBack(new CoreResponse(), false);
	}

	private void excuteResult(String result) {
		Log.e("JSon Return", result);
		CoreResponse response = new CoreResponse();
		if (response.parse(result)) {
			mDelegate.callBack(response, true);
		} else {
			String message = response.getMessage();
			if (!Utils.validateString(message) && isShowNotify) {
				message = Config.getInstance().getText(
						"Some errors occured. Please try again later");
			}
			if (isShowNotify) {
				SimiManager.getIntance().showNotify(message);
			}
			mDelegate.callBack(response, false);
		}
	}

	@Override
	protected String doInBackground(Void... params) {
		long currentTime = System.currentTimeMillis();
		Log.e("CoreRequest Start", url_action + " : " + currentTime);
		return makeRequest();
	}

	@SuppressWarnings("rawtypes")
	protected void prepareRequest() {
		try {
			mJsonParams = new JSONObject(SimiHelper.endCodeJson(mNameValue));
			Iterator<Entry<String, Object>> iter = mHashMap.entrySet()
					.iterator();
			while (iter.hasNext()) {
				Map.Entry mEntry = (Map.Entry) iter.next();
				mJsonParams.put((String) mEntry.getKey(), mEntry.getValue());
			}
		} catch (JSONException e) {
			mJsonParams = null;
		}
	}

	public void addParams(String tag, String value) {
		if (tag != null && value != null) {
			mNameValue.add(new BasicNameValuePair(tag, value));
		}
	}

	public void addParams(String tag, JSONArray value) {
		if (tag != null && value != null) {
			mHashMap.put(tag, value);
		}
	}

	public void addParams(String tag, JSONObject value) {
		if (tag != null && value != null) {
			mHashMap.put(tag, value);
		}
	}

	protected String makeRequest() {
		if (isDebug) {
			return MockCoreAPIService.getInstance().getDataFromServer(
					mJsonParams, url_action);
		}

		return CoreAPIService.getInstacnce().getDataFromServer(mJsonParams,
				url_action);
	}

	@Override
	protected void onPreExecute() {
		if (!checkInternetConnect()) {
			showError(Config.getInstance().getText("Cannot connect to server"));
			cancel(true);
		}
		super.onPreExecute();
	}

	protected boolean checkInternetConnect() {
		ConnectivityManager conMgr = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo i = conMgr.getActiveNetworkInfo();
		if (i == null)
			return false;
		if (!i.isConnected())
			return false;
		if (!i.isAvailable())
			return false;
		return true;
	}

	protected void showError(String message) {
		if (isShowNotify) {
			SimiManager.getIntance().showError(message);
		}
	}

	@Override
	protected void onPostExecute(String result) {
		long currentTime = System.currentTimeMillis();
		Log.e("CoreRequest Finish", url_action + " : " + currentTime);
		if (result == null || result.equals("")) {
			CoreResponse response = new CoreResponse();
			mDelegate.callBack(response, false);
			return;
		} else if (TextUtils.isDigitsOnly(result)) {
			if (result.equals("")) {
				showErrorRequest(0);
			} else {
				int statusCode = Integer.parseInt(result);
				showErrorRequest(statusCode);
			}
			CoreResponse response = new CoreResponse();
			mDelegate.callBack(response, false);
		} else {
			CoreResponse response = new CoreResponse();
			boolean status = false;
			if (response.parse(result)) {
				status = true;
			}
			mDelegate.callBack(response, status);
		}

	}

	protected void showErrorRequest(int statusCode) {
		String message = "";
		if (statusCode == 500) {
			message = Config.getInstance().getText("Internal Server Error");
			showError(message);
		} else if (statusCode == 503) {
			message = Config.getInstance().getText("Service Unavailable");
			showError(message);
		} else {
			message = Config.getInstance().getText(
					"Some errors are occurred. Please try again later");
			// showError(message);
		}
	}
}
