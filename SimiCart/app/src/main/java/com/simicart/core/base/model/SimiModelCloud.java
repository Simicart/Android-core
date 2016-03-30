package com.simicart.core.base.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.networkcloud.delegate.ModelDelegate;
import com.simicart.core.base.networkcloud.delegate.NetWorkDelegate;
import com.simicart.core.base.networkcloud.request.multi.SimiJSONRequest;
import com.simicart.core.base.networkcloud.request.multi.SimiRequest;
import com.simicart.core.base.networkcloud.request.multi.SimiRequest.Priority;
import com.simicart.core.base.networkcloud.response.CoreResponse;
import com.simicart.core.common.Utils;

public class SimiModelCloud {
	private NetWorkDelegate mDelegate;
	private ModelDelegate bDelegate;
	protected JSONObject mJSONResult;
	protected SimiCollection collection = null;
	protected String url_action = "";
	protected boolean isShowNotify = true;
	protected HashMap<String, Object> mDataBody;
	protected HashMap<String, String> mDataParameter;
	protected LinkedHashMap<String, String> mDataExtendURL;
	protected JSONObject mJSONBody;
	protected HashMap<String, String> mHeader;
	protected boolean isDebug = false;
	protected SimiRequest mRequest;
	protected Priority mCurrentPriority = Priority.NORMAL;
	protected int mTypeMethod;
	protected boolean isSetURLAction = false;
	protected boolean isProcessURLAction = false;
	protected boolean mShouldCache;

	/* The number of all results that match the critical */
	protected int mTotal;
	/* The number of items per page */
	protected int mPageSize;
	/* The start index of item in the result */
	protected int mFrom;
	/*
	 * An ids array of all results that match the critical, not only the
	 * response items
	 */
	protected ArrayList<String> mAllIDs;
	/* Current page of result */
	protected int mCurPage;
	/* The number of pages of result */
	protected int mTotalPages;

	private String total = "total";
	private String page_size = "page_size";
	private String from = "from";
	private String all_ids = "all_ids";
	private String cur_pages = "cur_pages";
	private String total_pages = "total_pages";

	public void setPriority(Priority priority) {
		mCurrentPriority = priority;
	}

	public void setDebugMode(boolean debug) {
		isDebug = debug;
	}

	public SimiModelCloud() {
		mDataBody = new HashMap<String, Object>();
		mDataParameter = new HashMap<String, String>();
		mDataExtendURL = new LinkedHashMap<String, String>();
		mHeader = new HashMap<String, String>();

	}

	public JSONObject getJSON() {
		return mJSONResult;
	}

	/**
	 * override this function to set path url
	 */
	protected void setUrlAction() {
	}

	public void setUrlActionSearch(String url) {
		url_action = url;
	}

	protected void setShowNotifi() {
	}

	public JSONObject getDataJSON() {
		return mJSONResult;
	}

	public void setDelegate(ModelDelegate delegate) {
		bDelegate = delegate;
	}

	public SimiCollection getCollection() {
		if (collection == null) {
			return new SimiCollection();
		}
		return this.collection;
	}

	// set data extend url
	public void addDataExtendURL(String key) {
		addDataExtendURL(key, null);
	}

	public void addDataExtendURL(String key, String value) {
		if (Utils.validateString(key)) {
			if (!isSetURLAction) {
				isSetURLAction = true;
				setUrlAction();
			}
			mDataExtendURL.put(key, value);
		}
	}

	// set data parameter
	public void addDataParameter(String key, String value) {
		if (Utils.validateString(key) && Utils.validateString(value)) {
			if (!isSetURLAction) {
				isSetURLAction = true;
				setUrlAction();
			}
			mDataParameter.put(key, value);
		}
	}

	// set data for filter parameter
	public void addFilterDataParameter(String key, String value) {
		if (Utils.validateString(key) && Utils.validateString(value)) {
			String key_filter = "filter[" + key + "]";
			Log.e("SimiModel", "Keyfilter" + key_filter + value);
			addDataParameter(key_filter, value);
		}
	}

	// set data for fields
	public void addFieldsDataParameter(ArrayList<String> listField) {
		if (null != listField && listField.size() > 0) {
			int size = listField.size();
			StringBuilder builder = new StringBuilder();
			builder.append(listField.get(0));
			if (size > 10) {
				for (int i = 1; i < size; i++) {
					String field = listField.get(i);
					builder.append("," + field);
				}
			}
			addDataParameter("fields", builder.toString());
		}
	}

	// set data for limit
	public void addLimitDataParameter(String limit) {
		if (Utils.validateString(limit)) {
			addDataParameter("limit", limit);
		}
	}

	// set data for offset
	public void addOffsetDataParameter(String offset) {
		if (Utils.validateString(offset)) {
			addDataParameter("offset", offset);
		}
	}

	// set data for order
	public void addOrderDataParameter(String key) {
		if (Utils.validateString(key)) {
			addDataParameter("order", key);
		}
	}

	// sort direction: asc
	public void sortDirASC() {
		addDataParameter("dir", "asc");
	}

	// sort direction: desc
	public void sortDirDESC() {
		addDataParameter("dir", "desc");
	}

	protected void setTypeMethod() {
	}

	public void setTypeMethod(int type) {
		mTypeMethod = type;
	}

	// set data for body
	public void addDataBody(String tag, String value) {
		mDataBody.put(tag, value);
	}

	public void addDataBody(String tag, JSONArray value) {
		mDataBody.put(tag, value);
	}

	public void addDataBody(String tag, JSONObject value) {
		mDataBody.put(tag, value);
	}

	public void setJSONBody(JSONObject json) {
		mJSONBody = json;
	}

	/**
	 * setUrlAction initial Request add parameter request
	 */
	public void request() {
		this.initRequest();
		String class_name = this.getClass().getName();

		String cache_key = class_name + url_action;
		JSONObject body = mRequest.getBody();
		if (null != body) {
			String post_body = body.toString();
			if (Utils.validateString(post_body)) {
				cache_key = cache_key + post_body;
			}
		}
		mRequest.setCacheKey(cache_key);
		if (mShouldCache) {
			boolean isRefreshCart = SimiManager.getIntance().isRereshCart();
			if (cache_key.contains("CartModel/quotes") && isRefreshCart) {
				refreshRequest();
			} else {
				getDataFromCache();
			}
		} else {
			SimiManager.getIntance().getRequestQueueCloud().add(mRequest);
		}
	}

	private void initRequest() {
		this.initDelegate();
		if (!isSetURLAction) {
			isSetURLAction = true;
			setUrlAction();
		}
		// setUrlAction();
		if (!isProcessURLAction) {
			isProcessURLAction = true;
			processUrlAction();
		}

		setShowNotifi();
		setTypeMethod();
		setShouldCache();
		mRequest = new SimiJSONRequest(url_action, mDelegate);
		mRequest.setPriority(mCurrentPriority);
		mRequest.setShowNotify(isShowNotify);
		mRequest.setShouldCache(mShouldCache);
		mRequest.setDataBody(mDataBody);
		mRequest.setDataExtendURL(mDataExtendURL);
		mRequest.setDataParameter(mDataParameter);
		mRequest.setJsonBody(mJSONBody);
		mRequest.setTypeMethod(mTypeMethod);
	}

	private void getDataFromCache() {
		// JSONObject json = SimiManager.getIntance().getRequestQueue()
		// .getDataFromCacheL1(mRequest);
		// if (null != json) {
		// CoreResponse coreResponse = new CoreResponse();
		// Log.e("SimiModel  DATA FROM CACHE ", json.toString());
		// coreResponse.setData(json.toString());
		// coreResponse.parse();
		// mDelegate.callBack(coreResponse, true);
		// } else {
		SimiManager.getIntance().getRequestQueueCloud().add(mRequest);
		// }
	}

	public void refreshRequest() {
		SimiManager.getIntance().getRequestQueueCloud().add(mRequest);
	}

	private void initDelegate() {
		this.mDelegate = new NetWorkDelegate() {

			@Override
			public void callBack(CoreResponse coreResponse, boolean isSuccess) {
				// TODO Auto-generated method stub
				if (isSuccess) {
					mJSONResult = coreResponse.getDataJSON();
					paserData();
					bDelegate.onSuccess(collection);
				} else {
					bDelegate.onFail(coreResponse.getSimiError());
				}
			}
		};

	}

	protected void processUrlAction() {
		try {
			if (mDataExtendURL.size() > 0) {
				StringBuilder builder = new StringBuilder();
				builder.append(url_action);

				Iterator<Entry<String, String>> iter = mDataExtendURL
						.entrySet().iterator();

				while (iter.hasNext()) {
					Entry entry = (Entry) iter.next();
					// key
					String key = (String) entry.getKey();
					Log.e("SimiModel", "Key Entry " + key);
					if (Utils.validateString(key)) {
						builder.append("/");
						builder.append(key);
					}
					// value
					String value = (String) entry.getValue();
					if (Utils.validateString(value)) {
						Log.e("SimiModel", "Value Entry " + value);
						builder.append("/");
						builder.append(value);
					}
				}

				url_action = builder.toString();
			}
		} catch (Exception e) {
			Log.e("SimiModel", "Exception " + e.getMessage());
		}

	}

	protected void paserData() {
		if (null != mJSONResult) {
			if (null == collection) {
				collection = new SimiCollection();
			}
			collection.setJSON(mJSONResult);

			// parse total
			if (mJSONResult.has(total)) {
				String s_total = getData(total);
				if (Utils.validateString(s_total)) {
					mTotal = getInt(s_total);
				}
			}

			// parse page size
			if (mJSONResult.has(page_size)) {
				String s_page_size = getData(page_size);
				if (Utils.validateString(s_page_size)) {
					mPageSize = getInt(s_page_size);
				}
			}

			// parse from
			if (mJSONResult.has(from)) {
				String s_from = getData(from);
				if (Utils.validateString(s_from)) {
					mFrom = getInt(s_from);
				}
			}

			// parse all_ids
			if (mJSONResult.has(all_ids)) {
				JSONArray js_all = getJSON(all_ids);
				mAllIDs = new ArrayList<String>();
				if (null != js_all) {
					for (int i = 0; i < js_all.length(); i++) {
						try {
							String id_id = js_all.getString(i);
							if (null != id_id) {
								mAllIDs.add(id_id);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}
			}

			// parse current page
			if (mJSONResult.has(cur_pages)) {
				String s_cur_pages = getData(cur_pages);
				if (Utils.validateString(s_cur_pages)) {
					mCurPage = getInt(s_cur_pages);
				}
			}

			// parse total page
			if (mJSONResult.has(total_pages)) {
				String s_total_pages = getData(total_pages);
				if (Utils.validateString(s_total_pages)) {
					mTotalPages = getInt(s_total_pages);
				}
			}
		}
	}

	protected JSONArray getJSON(String key) {
		try {
			JSONArray json = mJSONResult.getJSONArray(key);
			return json;
		} catch (JSONException e) {

		}
		return null;
	}

	protected String getData(String key) {
		try {
			String value = mJSONResult.getString(key);
			return value;
		} catch (JSONException e) {
			return null;
		}
	}

	protected int getInt(String sValue) {
		try {
			int iValue = Integer.parseInt(sValue);
			return iValue;
		} catch (Exception e) {
			return 0;
		}
	}

	protected ArrayList<String> getArray(String data) {
		String[] list = data.split(",");
		int length = list.length;
		if (length > 0) {
			ArrayList<String> array = new ArrayList<>();
			for (int i = 0; i < length; i++) {
				array.add(list[i]);
			}
			return array;
		}
		return null;
	}

	public void setShouldCache() {
		mShouldCache = false;
	}

	public boolean isShouldCache() {
		return mShouldCache;
	}

}
