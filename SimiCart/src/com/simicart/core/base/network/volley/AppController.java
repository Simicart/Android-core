package com.simicart.core.base.network.volley;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.simicart.core.base.network.volley.toolbox.ImageLoader;
import com.simicart.core.base.network.volley.toolbox.Volley;

public class AppController extends Application {

	public static final String TAG = AppController.class.getSimpleName();

	private RequestQueue mRequestQueue;
	private ImageLoader mImageLoader;

	private static AppController mInstance;

	private SharedPreferences _preferences;

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		_preferences = PreferenceManager.getDefaultSharedPreferences(this);
	}

	public static synchronized AppController getInstance() {
		return mInstance;
	}

	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		}

		return mRequestQueue;
	}

	public ImageLoader getImageLoader() {
		getRequestQueue();
		if (mImageLoader == null) {
			mImageLoader = new ImageLoader(this.mRequestQueue,
					new LruBitmapCache());
		}
		return this.mImageLoader;
	}

	public <T> void addToRequestQueue(Request<T> req, String tag) {
		// set the default tag if tag is empty
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
		getRequestQueue().add(req);
	}

	public <T> void addToRequestQueue(Request<T> req) {
		req.setTag(TAG);
		getRequestQueue().add(req);
	}

	public void cancelPendingRequests(Object tag) {
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(tag);
		}
	}

	public static void saveCookie(Context context, String cookie, String url) {
		if (cookie == null) {
			return;
		}
		// Save in the preferences
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context.getApplicationContext());
		if (null == sharedPreferences) {
			return;
		}
		SharedPreferences.Editor editor = sharedPreferences.edit();
//		if (url.contains("data/{")) {
//			String jsonString = url.substring(url.indexOf("data")+5, url.length());
//			try {
//				JSONObject object = new JSONObject(jsonString);
//				if(object.has("user_email") && object.has("user_password")){
//					editor.putString("cookie_signin", cookie);
//				}
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
//		} else {
			editor.putString("cookie", cookie);
//		}
		editor.commit();
	}

	public static String getCookie(Context context) {
		String cookie = "";
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context.getApplicationContext());
		// if (cookie.contains("expires")) {
		// removeCookie(context);
		// return "";
		// }
//		if (DataLocal.isSignInComplete()) {
//			if (sharedPreferences.getString("cookie_signin", "") != null) {
//				cookie = sharedPreferences.getString("cookie_signin", "");
//			} else {
//				cookie = sharedPreferences.getString("cookie", "");
//			}
//		} else {
			cookie = sharedPreferences.getString("cookie", "");
//		}
		return cookie;
	}
}
