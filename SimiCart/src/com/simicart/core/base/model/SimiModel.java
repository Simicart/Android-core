package com.simicart.core.base.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.simicart.core.base.delegate.ModelDelegate;
import com.simicart.core.base.delegate.NetWorkDelegate;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.base.network.request.CoreRequest;
import com.simicart.core.base.network.request.multi.SimiJSONRequest;
import com.simicart.core.base.network.request.multi.SimiRequest;
import com.simicart.core.base.network.request.multi.SimiRequest.Priority;
import com.simicart.core.base.network.response.CoreResponse;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;

public class SimiModel {

	private NetWorkDelegate mDelegate;
	private CoreRequest coreRequest;
	private ModelDelegate bDelegate;
	protected JSONObject mJSON;
	protected SimiCollection collection = null;
	protected String url_action;
	protected boolean isShowNotify = true;
	protected HashMap<String, Object> mHashMap;
	protected boolean isDebug = false;

	protected SimiRequest mRequest;
	protected Priority mCurrentPriority = Priority.NORMAL;
	protected boolean isMultiRequest = true;

	protected boolean enableCache = false;

	public void setPriority(Priority priority) {
		mCurrentPriority = priority;
	}

	public void setDebugMode(boolean debug) {
		isDebug = debug;
	}

	public SimiModel() {
		mHashMap = new HashMap<String, Object>();

	}

	public JSONObject getJSON() {
		return mJSON;
	}

	private void initDelegate() {
		this.mDelegate = new NetWorkDelegate() {

			@Override
			public void callBack(CoreResponse coreResponse, boolean isSuccess) {
				// TODO Auto-generated method stub
				if (isSuccess) {
					beforePaser();
					mJSON = coreResponse.getDataJSON();
					if (enableCache
							&& !DataLocal.dataJson.containsKey(url_action + "/"
									+ mHashMap.toString())) {
						Log.e("CACHE DATA",
								"" + url_action + "/" + mHashMap.toString());
						DataLocal.dataJson.put(
								url_action + "/" + mHashMap.toString(),
								coreResponse);
					}
					paserData();
					afterPaser();
				}
				bDelegate.callBack(coreResponse.getMessage(), isSuccess);
			}
		};

	}

	/**
	 * override to edit information before parser
	 */
	protected void beforePaser() {

	}

	/**
	 * override to edit information before parser
	 */
	protected void afterPaser() {

	}

	public void addParam(String tag, String value) {
		mHashMap.put(tag, value);

	}

	public void addParam(String tag, JSONArray value) {
		mHashMap.put(tag, value);
	}

	public void addParam(String tag, JSONObject value) {
		mHashMap.put(tag, value);
	}

	/**
	 * override this function to add params
	 */
	private void addParams() {

		if (DataLocal.isSignInComplete()
				&& (!url_action.equals(Constants.SIGN_IN) || !url_action
						.equals(Constants.SIGN_OUT))) {
			String email = DataLocal.getEmail();
			String pass_word = DataLocal.getPassword();

			addParam(Constants.USER_EMAIL, email);
			addParam(Constants.USER_PASSWORD, pass_word);
		}

		if (!mHashMap.isEmpty()) {
			Iterator<Entry<String, Object>> iterator = mHashMap.entrySet()
					.iterator();
			while (iterator.hasNext()) {
				@SuppressWarnings("rawtypes")
				Map.Entry entry = (Map.Entry) iterator.next();
				Object value = entry.getValue();
				String key = String.valueOf(entry.getKey());
				if (value instanceof String) {
					if (isMultiRequest) {
						mRequest.addParams(key, String.valueOf(value));
					} else {
						coreRequest.addParams(key, String.valueOf(value));
					}
				} else if (value instanceof JSONObject) {
					if (isMultiRequest) {
						mRequest.addParams(key, (JSONObject) value);
					} else {
						coreRequest.addParams(key, (JSONObject) value);
					}
				} else if (value instanceof JSONArray) {
					if (isMultiRequest) {
						mRequest.addParams(key, (JSONArray) value);
					} else {
						coreRequest.addParams(key, (JSONArray) value);
					}
				}
			}
		}
	}

	/**
	 * override this function to change Entity
	 */
	protected void paserData() {
		try {
			JSONArray list = this.mJSON.getJSONArray("data");
			collection = new SimiCollection();
			for (int i = 0; i < list.length(); i++) {
				SimiEntity entity = new SimiEntity();
				entity.setJSONObject(list.getJSONObject(i));
				collection.addEntity(entity);
			}

			if (mJSON.has(Constants.OTHER)) {
				JSONObject jsonOther = mJSON.getJSONObject(Constants.OTHER);
				if (null != jsonOther) {
					collection.setJSONOther(jsonOther);
				}
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * override this function to set path url
	 * 
	 */
	protected void setUrlAction() {
	}

	public void setUrlActionSearch(String url) {
		url_action = url;
	}

	protected void setShowNotifi() {
	}

	/**
	 * setUrlAction initial Request add parameter request
	 */
	public void request() {
		this.initRequest();
		if (enableCache
				&& DataLocal.dataJson.containsKey(url_action + "/"
						+ mHashMap.toString())) {
			CoreResponse coreResponse = DataLocal.dataJson.get(url_action + "/"
					+ mHashMap.toString());
			mDelegate.callBack(coreResponse, true);
		} else {
			if (isMultiRequest) {
				SimiManager.getIntance().getRequestQueue().add(mRequest);
			} else {
				this.coreRequest.request();
			}
		}
	}

	protected void setEnableCache() {
	}

	private void initRequest() {
		this.initDelegate();
		setUrlAction();
		setShowNotifi();
		setEnableCache();
		if (isMultiRequest) {
			mRequest = new SimiJSONRequest(url_action, mDelegate);
			mRequest.setPriority(mCurrentPriority);
			mRequest.setShowNotify(isShowNotify);
		} else {
			this.coreRequest = new CoreRequest(mDelegate);
			if (isDebug) {
				coreRequest.setDebugMode(isDebug);
			}
			this.coreRequest.setUrlAction(url_action);
			this.coreRequest.setShowNotify(isShowNotify);
		}

		addParams();
	}

	public JSONObject getDataJSON() {
		return mJSON;
	}

	public void setDelegate(ModelDelegate delegate) {
		bDelegate = delegate;
	}

	public SimiCollection getCollection() {
		if (collection == null)
			return new SimiCollection();
		return this.collection;
	}

}
