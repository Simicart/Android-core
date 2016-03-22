package com.simicart.core.base.network.request;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Environment;

import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.common.Utils;

/**
 * 
 * @author Frank Simi This class will store the response to File. It will be the
 *         Cache level 2.
 * 
 */
public class SimiNetworkCacheL2 implements SimiNetworkCache {

	private File mRootDirectory;

	public SimiNetworkCacheL2() {
		init();
	}

	private  void init() {
		Context context = SimiManager.getIntance().getCurrentActivity();
		String uniqueName = "response_cache";
		final String cachePath = Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())
				|| !Environment.isExternalStorageRemovable() ? context
				.getExternalCacheDir().getPath() : context.getCacheDir()
				.getPath();

		mRootDirectory = new File(cachePath + File.separator + uniqueName);
		if (!mRootDirectory.exists()) {
			mRootDirectory.mkdirs();
		}
	}

	@Override
	public synchronized JSONObject get(String key) {
		if (null != key) {
			try {
				File file = getFileForKey(key);
				int size = (int) file.length();
				BufferedInputStream bis = new BufferedInputStream(
						new FileInputStream(file));
				byte[] data = new byte[size];
				bis.read(data);
				bis.close();
				return bytesToJSON(data);
			} catch (Exception e) {
				return null;
			}
		}
		return null;
	}

	@Override
	public synchronized void put(String key, JSONObject response) {
		if (null != key && null != response) {
			try {
				File file = getFileForKey(key);
				byte[] data = response.toString().getBytes();
				BufferedOutputStream bos = new BufferedOutputStream(
						new FileOutputStream(file));
				bos.write(data);
				bos.close();

			} catch (IOException e) {

			}
		}
	}

	@Override
	public synchronized void clear() {
		File[] files = mRootDirectory.listFiles();
		if (files != null) {
			for (File file : files) {
				file.delete();
			}
		}

	}

	@Override
	public synchronized void remove(String key) {
		File file = getFileForKey(key);
		file.delete();

	}

	/**
	 * This method will store data from Cache L1 to Cache L2
	 */

	public synchronized void copyCache(Map<String, JSONObject> cache) {
		clear();
		if (null != cache) {
			Set<String> keys = cache.keySet();
			Iterator<String> iterator = keys.iterator();
			while (iterator.hasNext()) {
				String key = iterator.next();
				put(key, cache.get(key));
			}
		}
	}

	private File getFileForKey(String key) {
		String key_md5 = Utils.md5(key);
		return new File(mRootDirectory, key_md5);
	}

	private JSONObject bytesToJSON(byte[] data) {
		String content = new String(data);
		try {
			JSONObject json = new JSONObject(content);
			return json;
		} catch (JSONException e) {
			return null;
		}
	}

}
