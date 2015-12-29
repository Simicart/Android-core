package com.simicart.core.common;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;

import org.apache.http.HttpVersion;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.Display;
import android.widget.ImageView;
import android.widget.TextView;

import com.simicart.MainActivity;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.network.request.MySSLSocketFactory;
import com.simicart.core.config.Config;
import com.simicart.core.config.Rconfig;

public class DrawableManager {

	protected static LruCache<String, Bitmap> mMemoryCache;
	protected static DiskLruCache mDiskLruCache;
	protected static Object mDiskCackeLock = new Object();
	protected static boolean mDiskCacheStarting = true;
	protected static int DISK_CACHE_SIZE = 1024 * 1024 * 100;
	protected static String DISK_CACHE_SUBDIR = "thumbnails";
	protected static boolean isInitial = false;

	public static void init() {
		if (!isInitial) {
			if (null == mMemoryCache) {
				final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
				final int cacheSize = maxMemory / 8;
				mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
					@Override
					protected int sizeOf(String key, Bitmap bitmap) {
						return bitmap.getByteCount() / 1024;
					}
				};
			}
			File cacheDir = getDiskCacheDir();

			InitDiskCacheTask task = (new DrawableManager()).new InitDiskCacheTask();
			task.execute(cacheDir);

			isInitial = true;
		}
	}

	public class InitDiskCacheTask extends AsyncTask<File, Void, Void> {

		@Override
		protected Void doInBackground(File... params) {

			synchronized (mDiskCackeLock) {
				File cacheDir = params[0];

				try {
					mDiskLruCache = DiskLruCache.open(cacheDir, 1, 1,
							DISK_CACHE_SIZE);
					mDiskCacheStarting = false;
					mDiskCackeLock.notifyAll();
				} catch (IOException e) {
					Log.e("DrawableManager ", "InitDiskCacheTask IOEception "
							+ e.getMessage());
				}
			}

			return null;
		}

	}

	public static File getDiskCacheDir() {
		Context context = MainActivity.context;
		String uniqueName = DISK_CACHE_SUBDIR;
		final String cachePath = Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())
				|| !Environment.isExternalStorageRemovable() ? context
				.getExternalCacheDir().getPath() : context.getCacheDir()
				.getPath();

		File cacheFile = new File(cachePath + File.separator + uniqueName);
		if (!cacheFile.exists()) {
			cacheFile.mkdirs();
		}
		return cacheFile;
	}

	public static Bitmap getBitmapFromDiskCache(String key) {
		String key_md5 = Utils.md5(key);
		synchronized (mDiskCackeLock) {
			// Wait while disk cache is started from background thread
			while (mDiskCacheStarting) {
				try {
					mDiskCackeLock.wait();
				} catch (InterruptedException e) {

					Log.e("DrawableManager getBitmapFromDiskCache ",
							"InterrupedException " + e.getMessage());
				}
			}

			if (mDiskLruCache != null) {
				try {
					DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key_md5);
					if (null != snapshot) {
						InputStream inputStream = snapshot.getInputStream(0);
						if (null != inputStream) {
							return BitmapFactory.decodeStream(inputStream);
						}
					}
				} catch (Exception e) {

					Log.e("DrawableManager getBitmapFromDiskCache ",
							"Exception " + e.getMessage());

					return null;
				}
			}
		}
		return null;
	}

	public static void fetchDrawableDetailOnThread(final String urlString,
			final ImageView imageView) {

		init();

		Bitmap cache_bitMap = getBitmapFromMemCache(urlString);

		if (null != cache_bitMap) {
			imageView.setImageBitmap(cache_bitMap);
			return;
		}

		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message message) {
				Bitmap bitmap = (Bitmap) message.obj;
				if (bitmap != null) {
					imageView.setImageBitmap(bitmap);
					addBitmapToMemoryCache(urlString, bitmap);
				} else {
					Resources resource = SimiManager.getIntance()
							.getCurrentContext().getResources();
					bitmap = BitmapFactory.decodeResource(resource, Rconfig
							.getInstance().drawable("default_icon"));
					bitmap = Bitmap.createScaledBitmap(bitmap, 80, 80, true);
					imageView.setImageBitmap(bitmap);
				}
			}
		};

		getBitmap(handler, urlString);
	}

	public static void fetchDrawableIConOnThread(final String urlString,
			final ImageView imageView, final Context context, final int color) {

		init();

		Bitmap cache_bitMap = getBitmapFromMemCache(urlString);

		if (null != cache_bitMap) {
			Resources resource = SimiManager.getIntance().getCurrentContext()
					.getResources();
			Drawable drawable = new BitmapDrawable(resource, cache_bitMap);
			drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);

			imageView.setImageDrawable(drawable);
			return;
		}

		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message message) {
				Bitmap bitmap = (Bitmap) message.obj;
				addBitmapToMemoryCache(urlString, bitmap);
				Resources resource = SimiManager.getIntance()
						.getCurrentContext().getResources();
				Drawable drawable = null;
				if (bitmap != null) {
					drawable = new BitmapDrawable(resource, bitmap);
					drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
				} else {
					bitmap = BitmapFactory.decodeResource(resource, Rconfig
							.getInstance().drawable("default_icon"));
					bitmap = Bitmap.createScaledBitmap(bitmap, 80, 80, true);
					drawable = new BitmapDrawable(context.getResources(),
							bitmap);
					drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
				}
				if (null != drawable) {
					imageView.setImageDrawable(drawable);
				}

			}
		};

		getBitmap(handler, urlString);
	}

	public static void fetchDrawableOnThread(final String urlString,
			final ImageView imageView) {

		init();

		Bitmap cache_bitMap = getBitmapFromMemCache(urlString);

		if (null != cache_bitMap) {
			imageView.setImageBitmap(cache_bitMap);
			return;
		}

		else {
			cache_bitMap = getBitmapFromDiskCache(urlString);
			if (null != cache_bitMap) {
				imageView.setImageBitmap(cache_bitMap);

				String key_md5 = Utils.md5(urlString);

				if (null != mMemoryCache) {
					if (getBitmapFromMemCache(key_md5) == null) {
						mMemoryCache.put(key_md5, cache_bitMap);
					}
				}
				return;
			}
		}

		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message message) {
				Bitmap bitmap = (Bitmap) message.obj;
				if (bitmap != null) {
					imageView.setImageBitmap(bitmap);
					addBitmapToMemoryCache(urlString, bitmap);
				} else {
					Resources resources = SimiManager.getIntance()
							.getCurrentContext().getResources();
					bitmap = BitmapFactory.decodeResource(resources, Rconfig
							.getInstance().drawable("default_icon"));
					bitmap = Bitmap.createScaledBitmap(bitmap, 80, 80, true);
					imageView.setImageBitmap(bitmap);
					bitmap = null;
				}
			}
		};

		getBitmap(handler, urlString);
	}

	public static void addBitmapToMemoryCache(String key, Bitmap bitmap) {

		String key_md5 = Utils.md5(key);

		if (null != mMemoryCache) {
			if (getBitmapFromMemCache(key_md5) == null) {
				mMemoryCache.put(key_md5, bitmap);
			}
		}

		synchronized (mDiskCackeLock) {
			if (mDiskLruCache != null) {

				DiskLruCache.Editor editor = null;
				try {
					if (mDiskLruCache.get(key_md5) == null) {
						editor = mDiskLruCache.edit(key_md5);
						if (null == editor) {
							Log.e("DrawableManager  addBitMapToMemory ",
									"EDITOR NULL");
							return;
						}

						if (writeBitmapToFile(bitmap, editor)) {
							mDiskLruCache.flush();
							editor.commit();
						} else {
							editor.abort();
						}
					}
				} catch (Exception e) {

					Log.e("DrawableManager  addBitMapToMemory ", "Exception "
							+ e.getMessage());

					try {
						mDiskLruCache.remove(key_md5);
					} catch (IOException ex) {
						Log.e("DrawableManager addBitMapToMemory ",
								"DiskLruCache REMOVE IOException "
										+ ex.getMessage());
					}

				}
			}
		}
	}

	protected static boolean writeBitmapToFile(Bitmap bitmap,
			DiskLruCache.Editor editor) throws IOException,
			FileNotFoundException {
		OutputStream out = null;
		try {
			out = new BufferedOutputStream(editor.newOutputStream(0), 8 * 1024);
			CompressFormat mCompressFormat = CompressFormat.JPEG;
			return bitmap.compress(mCompressFormat, 80, out);
		}

		finally {
			if (out != null) {
				out.close();
			}
		}
	}

	public static Bitmap getBitmapFromMemCache(String key) {

		String key_md5 = Utils.md5(key);

		return mMemoryCache.get(key_md5);
	}

	public static void fetchDrawableOnThreadForZTheme(final String urlString,
			final ImageView imageView) {

		init();

		Bitmap cache_bitMap = getBitmapFromMemCache(urlString);

		Display display = SimiManager.getIntance().getCurrentActivity()
				.getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		final int w = (size.x * 4) / 5;
		final int h = (size.y * 4) / 5;

		if (null != cache_bitMap) {
			Bitmap bMapRotate = Utils.scaleToFill(cache_bitMap, w, h);
			imageView.setImageBitmap(bMapRotate);
			cache_bitMap = null;
			bMapRotate = null;
			return;
		}

		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message message) {
				Bitmap bitmap = (Bitmap) message.obj;
				if (bitmap != null) {
					try {
						Bitmap bMapRotate = Utils.scaleToFill(bitmap, w, h);
						imageView.setImageBitmap(bMapRotate);
						addBitmapToMemoryCache(urlString, bitmap);
						bitmap = null;
						bMapRotate = null;
					} catch (Exception e) {

					}
				} else {
					Resources resources = SimiManager.getIntance()
							.getCurrentContext().getResources();
					bitmap = BitmapFactory.decodeResource(resources, Rconfig
							.getInstance().drawable("default_icon"));
					Matrix mat = new Matrix();
					mat.postRotate(-90);
					Bitmap bMapRotate = Bitmap.createBitmap(bitmap, 0, 0,
							bitmap.getWidth(), bitmap.getHeight(), mat, true);
					imageView.setImageBitmap(bMapRotate);
					bitmap = null;
				}
			}
		};

		getBitmap(handler, urlString);
	}

	@SuppressWarnings("deprecation")
	public static void fetchDrawableOnThread(final String urlString,
			final TextView textview) {

		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message message) {
				Bitmap bitmap = (Bitmap) message.obj;
				if (bitmap != null) {
					Resources resource = SimiManager.getIntance()
							.getCurrentContext().getResources();
					Drawable drawable = new BitmapDrawable(resource, bitmap);
					textview.setBackgroundDrawable(drawable);
				} else {
					textview.setBackgroundResource(Rconfig.getInstance()
							.drawable("default_icon"));
				}

			}
		};

		getBitmap(handler, urlString);
	}

	public static void fetchItemDrawableOnThread(final String urlString,
			final ImageView imageView) {

		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message message) {
				Bitmap bitmap = (Bitmap) message.obj;
				Resources resource = SimiManager.getIntance()
						.getCurrentContext().getResources();
				if (bitmap != null) {
					Drawable drawable = new BitmapDrawable(resource, bitmap);
					imageView.setImageDrawable(drawable);
				} else {
					bitmap = BitmapFactory.decodeResource(resource, Rconfig
							.getInstance().drawable("default_icon"));
					bitmap = Bitmap.createScaledBitmap(bitmap, 80, 80, true);
					imageView.setImageBitmap(bitmap);
				}
			}
		};

		getBitmap(handler, urlString);
	}

	public static void getBitmap(final Handler handler, final String urlString) {
		Thread thread = new Thread() {
			@Override
			public void run() {
				Bitmap bitmap = excutePostForBitMap(urlString);
				if (bitmap != null) {
					Message message = handler.obtainMessage(1, bitmap);
					handler.sendMessage(message);
				}
			}
		};
		thread.start();
	}

	public static Bitmap excutePostForBitMap(String url) {
		try {
			Bitmap bitMap = null;
			URL url_con = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) url_con
					.openConnection();
			conn.setReadTimeout(10000 /* milliseconds */);
			conn.setConnectTimeout(15000 /* milliseconds */);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Token", Config.getInstance()
					.getSecretKey());
			conn.setDoInput(true);
			conn.setDoOutput(false);
			conn.connect();

			int status = conn.getResponseCode();
			if (status < 300) {
				InputStream is = conn.getInputStream();
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				BufferedInputStream bs = new BufferedInputStream(is);
				byte[] buff = new byte[1024];
				int read = 0;
				while ((read = bs.read(buff)) > 0) {
					bos.write(buff, 0, read);
					buff = new byte[1024];
				}
				byte[] bytes = bos.toByteArray();
				bitMap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
				// conn.disconnect();
				return bitMap;
			} else {
				Log.e("Drawable Manager ", "STATUS CODE " + status);
				return null;
			}
		} catch (Exception e) {
			Log.e("Drawable Manager ", e.toString());
			return null;
		}

	}

	public static DefaultHttpClient getNewHttpClient() {
		DefaultHttpClient httpClient = null;
		try {
			if (httpClient == null) {
				KeyStore trustStore = KeyStore.getInstance(KeyStore
						.getDefaultType());
				trustStore.load(null, null);
				SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
				sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
				HttpParams params = new BasicHttpParams();
				HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
				HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
				SchemeRegistry registry = new SchemeRegistry();
				registry.register(new Scheme("http", PlainSocketFactory
						.getSocketFactory(), 80));
				registry.register(new Scheme("https", sf, 443));
				ClientConnectionManager ccm = new ThreadSafeClientConnManager(
						params, registry);
				httpClient = new DefaultHttpClient(ccm, params);
			}
			return httpClient;

		} catch (Exception e) {
			if (httpClient == null) {
				httpClient = new DefaultHttpClient();
			}
			return httpClient;
		}
	}

}
