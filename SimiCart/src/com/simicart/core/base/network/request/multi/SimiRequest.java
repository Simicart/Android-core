package com.simicart.core.base.network.request.multi;

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

import android.util.Log;

import com.simicart.core.base.delegate.NetWorkDelegate;
import com.simicart.core.base.helper.SimiHelper;
import com.simicart.core.base.network.response.CoreResponse;

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
	protected HashMap<String, Object> mHashMapBody = new HashMap<String, Object>();
	protected HashMap<String, String> mHeaderAddtional;
	protected List<NameValuePair> mNameValue = new ArrayList<NameValuePair>();
	protected NetWorkDelegate mDelegate;
	protected Integer mSequence;
	protected boolean mCancel;
	protected JSONObject mJsonPostBody;
	protected SimiRequestQueue mRequestQueue;
	protected Priority mCurrentPriority = Priority.NORMAL;
	protected boolean isShowNotify = true;

	public boolean isShowNotify() {
		return isShowNotify;
	}

	public void setShowNotify(boolean isShowNotify) {
		this.isShowNotify = isShowNotify;
	}

	public void setRequestQueue(SimiRequestQueue requestQueue) {
		mRequestQueue = requestQueue;
	}

	public void onStopRequestQueue() {
		mRequestQueue.stop();
	}

	public void onStartRequestQueue() {

		mRequestQueue.start();
	}

	public JSONObject getPostBody() {
		if (null == mJsonPostBody) {
			prepareRequest();
		}
		return mJsonPostBody;
	}

	public HashMap<String, String> getHeaderAddtional() {
		return mHeaderAddtional;
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

	@SuppressWarnings("rawtypes")
	protected void prepareRequest() {
		try {
			mJsonPostBody = new JSONObject(SimiHelper.endCodeJson(mNameValue));
			Iterator<Entry<String, Object>> iter = mHashMapBody.entrySet()
					.iterator();
			while (iter.hasNext()) {
				Map.Entry mEntry = (Map.Entry) iter.next();
				mJsonPostBody.put((String) mEntry.getKey(), mEntry.getValue());
			}
		} catch (JSONException e) {
			mJsonPostBody = null;
		}
	}

	public void addParams(String tag, String value) {
		if (tag != null && value != null) {
			mNameValue.add(new BasicNameValuePair(tag, value));
		}
	}

	public void addParams(String tag, JSONArray value) {
		if (tag != null && value != null) {
			mHashMapBody.put(tag, value);
		}
	}

	public void addParams(String tag, JSONObject value) {
		if (tag != null && value != null) {
			mHashMapBody.put(tag, value);
		}
	}

	public Priority getPriority() {
		return mCurrentPriority;
	}

	public void setPriority(Priority priority) {
		mCurrentPriority = priority;
	}

	@Override
	public int compareTo(SimiRequest another) {
		Log.e("SimiRequest ", mUrl + " comparreTo " + another.getUrl());
		Priority left = this.getPriority();
		Priority right = another.getPriority();
		int tmp1 = this.mSequence - another.mSequence;
		int tmp2 = right.ordinal() - left.ordinal();

		Log.e("SimiRequest ", "compareTo TEMP1 " + ": " + tmp1);
		Log.e("SimiRequest ", "compareTo TEMP2 " + ": " + tmp2);

		int result = left == right ? tmp1 : tmp2;

		Log.e("SimiRequest ", "compare RESULT " + result);

		return result;
	}

}
