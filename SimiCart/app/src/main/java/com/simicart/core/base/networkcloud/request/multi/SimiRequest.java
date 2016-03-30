package com.simicart.core.base.networkcloud.request.multi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.simicart.core.base.networkcloud.delegate.NetWorkDelegate;
import com.simicart.core.base.networkcloud.response.CoreResponse;

public class SimiRequest implements Comparable<SimiRequest> {

	public interface Method {
		int GET = 0;
		int POST = 1;
		int PUT = 2;
		int DELETE = 3;
		int HEAD = 4;
		int OPTIONS = 5;
		int TRACE = 6;
		int PATCH = 7;
	}

	public enum Priority {
		LOW, NORMAL, HIGH, IMMEDIATE
	}

	public interface TYPEREQUEST {
		int HTTPCLIENT = 0;
		int URL = 1;
	}

	protected int mTypeMethod = Method.POST;
	protected int mTypeRequest = TYPEREQUEST.HTTPCLIENT;
	protected String mUrl;
	protected HashMap<String, Object> mDataBody = new HashMap<String, Object>();
	protected HashMap<String, String> mDataExtendURL = new HashMap<String, String>();
	protected HashMap<String, String> mHeaderAddtional;
	protected List<NameValuePair> mNameValue = new ArrayList<NameValuePair>();
	protected NetWorkDelegate mDelegate;
	protected Integer mSequence;
	protected boolean mCancel;
	protected JSONObject mJsonBody;
	protected SimiRequestQueueCloud mRequestQueue;
	protected Priority mCurrentPriority = Priority.NORMAL;
	protected boolean isShowNotify = true;
	protected HashMap<String, String> mDataParameter;
	protected boolean mShouldCache;
	protected String mCacheKey;

	public HashMap<String, String> getDataParameter() {
		return mDataParameter;
	}

	public void setDataParameter(HashMap<String, String> mDataParameter) {
		this.mDataParameter = mDataParameter;
	}

	public HashMap<String, String> getDataExtendURL() {
		return mDataExtendURL;
	}

	public void setDataBody(HashMap<String, Object> data) {
		mDataBody = data;
	}

	public void setDataExtendURL(HashMap<String, String> data) {
		mDataExtendURL = data;
	}

	public void setJsonBody(JSONObject json) {

		mJsonBody = json;

	}

	public boolean isShowNotify() {
		return isShowNotify;
	}

	public void setShowNotify(boolean isShowNotify) {
		this.isShowNotify = isShowNotify;
	}

	public void setRequestQueue(SimiRequestQueueCloud requestQueue) {
		mRequestQueue = requestQueue;
	}

	public void onStopRequestQueue() {
		mRequestQueue.stop();
	}

	public void onStartRequestQueue() {
		mRequestQueue.start();
	}

	public JSONObject getBody() {
		if (null != mJsonBody) {
			return mJsonBody;
		}

		JSONObject json = prepareRequest();
		return json;
	}

	public HashMap<String, String> getHeaderAddtional() {
		return mHeaderAddtional;
	}

	public void setHeaderAddtional(HashMap<String, String> data) {
		mHeaderAddtional = data;
	}

	public SimiRequest(String url, NetWorkDelegate delegate) {
		mUrl = url;
		mDelegate = delegate;
	}

	public SimiRequest(String url, NetWorkDelegate delegate, int typeMethod) {
		this(url, delegate);
		mTypeMethod = typeMethod;
	}

	public SimiRequest(String url, NetWorkDelegate delegate, int typeMethod,
			int typeRequest) {
		this(url, delegate, typeMethod);
		mTypeRequest = typeRequest;
	}

	public CoreResponse parseNetworkResponse(SimiNetworkResponse response) {
		return null;
	}

	public void deliveryCoreResponse(CoreResponse response) {

	}

	public final SimiRequest setSequence(int sequence) {
		mSequence = sequence;
		return this;
	}

	public void finish() {

	}

	public void setShouldCache(boolean shouldCache) {
		mShouldCache = shouldCache;
	}

	public boolean isShouldCache() {
		return mShouldCache;
	}

	public String getCacheKey() {
		return mCacheKey;
	}

	public void setCacheKey(String key) {
		mCacheKey = key;
	}

	public boolean isCancel() {
		return mCancel;
	}

	public void cancel(boolean mCancel) {
		this.mCancel = mCancel;
	}

	public int getTypeMethod() {
		return mTypeMethod;
	}

	public void setTypeMethod(int mTypeMethod) {
		this.mTypeMethod = mTypeMethod;
	}

	public int getTypeRequest() {
		return mTypeRequest;
	}

	public void setTypeRequest(int mTypeRequest) {
		this.mTypeRequest = mTypeRequest;
	}

	public String getUrl() {
		return mUrl;
	}

	public void setUrl(String mUrl) {
		this.mUrl = mUrl;
	}

	protected JSONObject prepareRequest() {

		JSONObject json = null;
		try {

			if (mDataBody.size() > 0) {
				json = new JSONObject();
				Iterator<Entry<String, Object>> iter = mDataBody.entrySet()
						.iterator();
				while (iter.hasNext()) {
					Entry mEntry = (Entry) iter.next();
					json.put((String) mEntry.getKey(), mEntry.getValue());
				}
			} else {
				json = null;
			}
		} catch (JSONException e) {
			json = null;
		}
		return json;
	}

	public Priority getPriority() {
		return mCurrentPriority;
	}

	public void setPriority(Priority priority) {
		mCurrentPriority = priority;
	}

	@Override
	public int compareTo(SimiRequest another) {
		Priority left = this.getPriority();
		Priority right = another.getPriority();
		int tmp1 = this.mSequence - another.mSequence;
		int tmp2 = right.ordinal() - left.ordinal();
		int result = left == right ? tmp1 : tmp2;
		return result;
	}

}
