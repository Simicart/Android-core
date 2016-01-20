package com.simicart.core.base.network.request;

import java.util.Map;

import org.json.JSONObject;

import android.support.v4.util.LruCache;
import android.util.Log;

import com.simicart.core.common.Utils;

/**
 * 
 * @author Frank Simi This class will store the response to LruCache. It will be
 *         Cache Level 1.
 * 
 */
public class SimiNetworkCacheL1 implements SimiNetworkCache {

	private LruCache<String, JSONObject> mCacheL1;

	public SimiNetworkCacheL1() {
		// we can store 1024 response at the same time.
		int maxSize = 1024 * 1024;
		mCacheL1 = new LruCache<String, JSONObject>(maxSize);
	}

	@Override
	public JSONObject get(String key) {
		String key_md5 = Utils.md5(key);
		return mCacheL1.get(key_md5);
	}

	@Override
	public void put(String key, JSONObject response) {
		Log.e("SimiNetworkCacheL1 ", "PUT ");
		synchronized (mCacheL1) {
			if (null != key && null != response) {
				Log.e("SimiNetworkCacheL1 ", "PUT " + key);
				String key_md5 = Utils.md5(key);
				if (mCacheL1.get(key_md5) == null) {
					Log.e("SimiNetworkCacheL1 ", "PUT " + response.toString());

					mCacheL1.put(key_md5, response);
				}
				else
				{
					Log.e("SimiNetworkCacheL1 ", "PUT DATA NOT NULLL");
				}
			}
			else
			{
				Log.e("SimiNetworkCacheL1 ", "PUT NULLLLLLLLLLL");
			}
		}

	}

	@Override
	public void clear() {
		mCacheL1.evictAll();
	}

	@Override
	public void remove(String key) {
		synchronized (mCacheL1) {
			String key_md5 = Utils.md5(key);
			mCacheL1.remove(key_md5);
		}

	}

	/**
	 * This method will return a copy of the current cache
	 */
	public Map<String, JSONObject> makeCopy() {
		return mCacheL1.snapshot();
	}

}
